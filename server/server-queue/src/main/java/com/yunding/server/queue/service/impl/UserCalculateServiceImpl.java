package com.yunding.server.queue.service.impl;

import com.yunding.server.common.basecurd.entity.ReturnData;
import com.yunding.server.queue.config.CusParamConf;
import com.yunding.server.queue.dao.UserCalculateDao;
import com.yunding.server.queue.entity.UserIncome;
import com.yunding.server.queue.service.UserCalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @desc 用户相关计算 逻辑
 * @date 2020-04-21
 */
@Service
public class UserCalculateServiceImpl implements UserCalculateService {

    @Autowired
    private UserCalculateDao userCalculateDao;

    @Resource(name = "cusRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private CusParamConf cusParamConf;

    /** 根据用户id查询用户信息 */
    @Override
    public UserIncome queryIncomeByUserId(Integer userId) {
        return userCalculateDao.queryIncomeByUserId(userId);
    }

    /** 更新用户访问量 */
    @Override
    public int updateUserVisitView(Integer userId, Integer visitCount) {
        return userCalculateDao.updateUserVisitView(userId, visitCount);
    }

    /** 更新用户收益 */
    @Override
    public int updateUserIncome(Integer userId, long onlineSec, BigDecimal incomeTol) {
        return userCalculateDao.updateUserIncome(userId, onlineSec, incomeTol);
    }

    /** 根据id查询该用户的下级代理用户id */
    @Override
    public List<Integer> queryChildId(int userId) {
        return userCalculateDao.queryChildId(userId);
    }

    /** 清空用户前一天的修改记录 */
    @Override
    public int clearUserUpdateRecordByLast() {
        return userCalculateDao.clearUserUpdateRecordByLast();
    }

    /** 每隔两个小时查询一次短信验证码剩余次数 */
    @Override
    public void querySmsSurplus() {
        String pre = (String) cusParamConf.getMainServer().get("url");
        ReturnData forObject = restTemplate.getForObject(pre + "/rest/manage/querySmsSurplus", ReturnData.class);
    }
}
