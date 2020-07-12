package com.li.cloud.queue.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.queue.constant.ConstantStr;
import com.li.cloud.queue.dao.VisitRecordDao;
import com.li.cloud.queue.entity.IpAddrInfo;
import com.li.cloud.queue.entity.LinkedQueue;
import com.li.cloud.queue.entity.UserIncome;
import com.li.cloud.queue.entity.VisitRecord;
import com.li.cloud.queue.service.UserCalculateService;
import com.li.cloud.queue.service.VisitRecordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.li.cloud.queue.entity.LinkedQueue.linkQueue;


/**
 * @desc 访问记录实现类
 * @date 2020-04-08
 */
@Service
public class VisitRecordServiceImpl implements VisitRecordService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 注入RedisTemplate工具
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private BaseService baseService;

    @Autowired
    private VisitRecordDao visitRecordDao;

    @Autowired
    private UserCalculateService userCalculateService;

    /** 存在多个，需按名称注入； 默认6秒超时 */
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;

    /** 将访问数据添加到队列中 */
    @Override
    public boolean recordVisit(VisitRecord visitRecord) {
        // 添加，并返回添加标识，true成功
        return linkQueue.offer(visitRecord);
    }

    /** 将任务队列中的数据，累计到redis中 */
    @Override
    public String pushRecordToRedis() {
        int size = linkQueue.size();
        if(size <= 0){
            return "队列内容为空，无需处理";
        }
        do{
            // 从队列中拉取访问记录数据
            VisitRecord visitRecord = linkQueue.poll();
            if(null == visitRecord){
                continue;
            }
            // 获取ip相关归属地
            if(!StringUtils.isBlank(visitRecord.getVisitIp())){
                // 从redis中获取
                String ipAddr = (String)redisTemplate.opsForValue().get(ConstantStr.IP_PREFIX + visitRecord.getVisitIp());
                // 通过查询接口
                if(StringUtils.isEmpty(ipAddr)){
                    queryIpAddr(visitRecord.getVisitIp(), visitRecord);
                } else{
                    visitRecord.setIpAddress(ipAddr);
                }
            }
            // 将数据存储到redis中
            saveToRedis(visitRecord);
        } while (linkQueue.size() > 0);
        return ReturnData.SUCCESS;
    }

    /**
     * 将数据存储到redis中，以ip + 时间戳（秒）的形式为key存储：
     *  1、匿名用户：anonymousVisit:时间戳
     *  2、游客：ip:时间戳
     *  3、用户：ip:userId:时间戳
     * */
    private void saveToRedis(VisitRecord visitRecord) {
        int currSecond = LinkedQueue.currTime;
        // 当时间变化时，改变当前时间，并将当前时间存储到队列中
        if(currSecond != LinkedQueue.changeTime){
            currSecond = LinkedQueue.changeTime;
            LinkedQueue.currTime = LinkedQueue.changeTime;
        }
        // 匿名用户
        if(0 == visitRecord.getVisitor()){
            String key = "anonymousVisit:" + String.valueOf(currSecond);
            save(key, visitRecord);
        }
        // 游客
        else if(1 == visitRecord.getVisitor()){
            String key = visitRecord.getVisitIp() + ":" + String.valueOf(currSecond);
            save(key, visitRecord);
        }
        // 用户
        else if(2 == visitRecord.getVisitor()){
            String key = String.format("%s:%s:%s", visitRecord.getVisitIp(), visitRecord.getVisitId(), String.valueOf(currSecond));
            save(key, visitRecord);
        }
    }

    private void save(String key, VisitRecord visitRecord){
        VisitRecord anoVisitor = (VisitRecord) redisTemplate.opsForValue().get(key);
        // 若数据不存在，直接存储,并且将redis中key值存到队列中
        if(null == anoVisitor){
            redisTemplate.opsForValue().set(key, visitRecord);
            LinkedQueue.redisKeys.offer(key);
        } else{
            anoVisitor.setVisitCount(visitRecord.getVisitCount() + anoVisitor.getVisitCount());
            anoVisitor.setFictitiousCount(visitRecord.getFictitiousCount() + anoVisitor.getFictitiousCount());
            redisTemplate.opsForValue().set(key, anoVisitor);
        }
    }

    /** 查询ip归属地 */
    private void queryIpAddr(String ip, VisitRecord visitRecord){
        String location = "";
        IpAddrInfo ipAddrInfo = new IpAddrInfo();
        ipAddrInfo.setIp(ip);
        // 使用百度ip地址查询接口，查询ip归属地
        try {
            // 数据库查询
            ipAddrInfo = baseService.queryDataByField(ipAddrInfo, "ip");
            if(null != ipAddrInfo && !StringUtils.isEmpty(ipAddrInfo.getAddress())){
                // 设置ip地址信息到redis
                redisTemplate.persist(ConstantStr.IP_PREFIX + ip); // key永久保存
                redisTemplate.opsForValue().set(ConstantStr.IP_PREFIX + ip, ipAddrInfo.getAddress());
                location = ipAddrInfo.getAddress();
            } else{
                String baiDuRes = restTemplate.getForObject(String.format(ConstantStr.URL_IP_ADDR, ip), String.class);
                if(StringUtils.isBlank(baiDuRes)){
                    return;
                }
                String addressInfo = baiDuRes.trim();
                addressInfo = StringUtils.substring(addressInfo, addressInfo.indexOf("{"), addressInfo.lastIndexOf("}") + 1);
                JSONObject jsonObject = JSONObject.parseObject(addressInfo);
                JSONObject data = (JSONObject)jsonObject.getJSONArray("data").get(0);
                location = data.getString("location");
                // 查询到数据，加入redis，保存数据库
                if(!StringUtils.isEmpty(location.trim())){
                    // key永久保存
                    redisTemplate.persist(ConstantStr.IP_PREFIX + ip);
                    redisTemplate.opsForValue().set(ConstantStr.IP_PREFIX + ip, location);
                    ipAddrInfo = new IpAddrInfo();
                    ipAddrInfo.setIp(ip);
                    ipAddrInfo.setAddress(location);
                    baseService.insert(ipAddrInfo);
                }
            }
            visitRecord.setIpAddress(location);
            // 保存到数据库中
        } catch (RestClientException e) {
            logger.error(ip + "：未查询到ip归属地");
        }
    }

    /** 将数据更新到数据库 */
    @Override
    public void executeUpdate(long currTime) {
        do{
            // 取出key队列的数据
            String peek = LinkedQueue.redisKeys.peek();
            if(StringUtils.isEmpty(peek)){
                break;
            }
            String[] keyArr = peek.split(":");
            int len = keyArr.length;
            // 若当前key的时间小于四分钟前，则处理数据
            int secondTime = Integer.valueOf(keyArr[len - 1]);
            boolean hasNext = secondTime < (currTime - ConstantStr.FOUR_SECOND);
            if(!hasNext){
                break;
            }
            // 取出队列中的key，通过key去redis中查找数据，并存储到数据库
            String pollKey = LinkedQueue.redisKeys.poll();
            assert null != pollKey;
            VisitRecord visitRecord = (VisitRecord) redisTemplate.opsForValue().get(pollKey);
            List<VisitRecord> visitRecordList = visitRecordDao.queryByField(visitRecord.getVisitor(), visitRecord.getVisitId(), visitRecord.getVisitIp());
            // 若未查询到数据，则插入，否则更新
            if(null == visitRecordList || visitRecordList.isEmpty()){
                baseService.insertNoId(visitRecord);
            } else{
                boolean isInsert = false;
                VisitRecord updateDate = visitRecordList.get(0);
                int visitCount = updateDate.getVisitCount();
                int fictitiousCount = updateDate.getFictitiousCount();
                // 若存在多条数据，则删除，在统计重新插入
                if(visitRecordList.size() > 1){
                    isInsert = true;
                    visitCount = 0;
                    fictitiousCount = 0;
                    int delNum = visitRecordDao.deleteByField(visitRecord.getVisitor(), visitRecord.getVisitId(), visitRecord.getVisitIp());
                    if(delNum < 1)
                        logger.error("删除失败！");
                    for (VisitRecord record : visitRecordList) {
                        visitCount += record.getVisitCount();
                        fictitiousCount += record.getFictitiousCount();
                    }
                }
                updateDate.setVisitCount(visitCount + visitRecord.getVisitCount());
                updateDate.setFictitiousCount(fictitiousCount + visitRecord.getFictitiousCount());
                updateDate.setUpdateTime(System.currentTimeMillis());
                updateDate.setUpdateCount(updateDate.getUpdateCount() + 1);
                // 若已经删除，则需要插入，否则直接更新
                if(isInsert){
                    baseService.insertNoId(updateDate);
                } else{
                    int updateNum = visitRecordDao.updateVisitRecord(updateDate);
                    if(updateNum < 1){
                        logger.error("更新失败！");
                    }
                }
            }
            // 删除redis中的数据
            redisTemplate.delete(pollKey);
        } while (true);
    }

    /** 更新历史记录表 */
    @Override
    public int insertRecordHisByLastDay() {
        // 查询前一天的历史访问记录
        List<VisitRecord> visitRecordList = visitRecordDao.queryLastDayData();
        Integer[] visitCountArr = new Integer[]{0,0,0}; // 0：用户； 1 游客； 2 匿名
        // 遍历，将前一天的记录统计到总记录中;
        /** 访问记录可能存在 游客以及匿名的情况 */
        visitRecordList.forEach(visitRecord -> {
            // 统计用户访问记录
            if(null != visitRecord.getVisitId()){
                // 根据id查询数据
                UserIncome userIncome = userCalculateService.queryIncomeByUserId(visitRecord.getVisitId());
                if(null == userIncome){
                    userIncome = new UserIncome();
                    userIncome.setUserId(visitRecord.getVisitId());
                    userIncome.setVisitCount(visitRecord.getVisitCount());
                    baseService.insertNoId(userIncome);
                } else{
                    int visitCount = visitRecord.getVisitCount() + (null == userIncome.getVisitCount()? 0 : userIncome.getVisitCount());
                    userCalculateService.updateUserVisitView(visitRecord.getVisitId(), visitCount);
                }
                // 累加用户访问总量
                visitCountArr[0] = visitCountArr[0] + visitRecord.getVisitCount();
            }
            // 统计游客和匿名访问者量
            else{
                // 游客
                if(!StringUtils.isEmpty(visitRecord.getVisitIp())){
                    visitCountArr[1] = visitCountArr[1] + visitRecord.getVisitCount();
                }
                // 匿名
                else{
                    visitCountArr[2] = visitCountArr[2] + visitRecord.getVisitCount();
                }
            }
        });
        // 保存用户总访问量
        int saveNum = saveVisitTotals(visitCountArr[0], visitCountArr[1], visitCountArr[2]);
        if(saveNum > 0){
            logger.info("保存网站访问总量成功!");
        } else{
            logger.error("保存网站访问总量失败!");
        }
        return visitRecordDao.insertRecordHisByLastDay();
    }

    /** 保存用户总访问量 */
    private int saveVisitTotals(Integer user, Integer tourist, Integer anoy) {
        int userCount = visitRecordDao.saveVisitTotals(user, 1);
        int touristCount = visitRecordDao.saveVisitTotals(tourist, 0);
        int anoyCount = visitRecordDao.saveVisitTotals(anoy, -1);
        return userCount + touristCount + anoyCount;
    }

    /** 删除非当天的访问记录 */
    @Override
    public int delVisitRecordByLast() {
        return visitRecordDao.delByLastDay();
    }

    /** 清除超过15天的登录记录 */
    @Override
    public int clearLoginHis() {
        return visitRecordDao.clearLoginHis();
    }
}
