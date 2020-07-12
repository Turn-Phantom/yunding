package com.li.cloud.tool.service.impl;

import com.li.cloud.common.annotations.CustomPage;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.tool.dao.MobileMsgDao;
import com.li.cloud.tool.entity.MobileNumInfo;
import com.li.cloud.tool.service.MobileMsgService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc 手机短信 逻辑
 * @date 2020-05-06
 */
@Service
public class MobileMsgServiceImpl implements MobileMsgService {

    // 冻结号码（被其他线程读取，且待发送的号码）
    public static List<String> freezeNumber = new LinkedList<>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MobileMsgDao mobileMsgDao;

    /** 导入手机号码数据 */
    @Override
    public int importPhoneNum(List<MobileNumInfo> mobileNumInfoList) {
        logger.info("导入条数：" + mobileNumInfoList.size());
        return mobileMsgDao.importPhoneNum(mobileNumInfoList);
    }

    /** 查询新导入的数据 */
    @Override
    public List<MobileNumInfo> queryImport() {
        return mobileMsgDao.queryImport();
    }

    /** 分页查询手机号码数据 */
    @Override
    @CustomPage
    public List<MobileNumInfo> queryPhoneNumberData(Pagination<MobileNumInfo> pagination) {
        return mobileMsgDao.queryPhoneNumberData(pagination);
    }

    /** 根据id删除数据 */
    @Override
    public int deletePhoneNum(String id) {
        return mobileMsgDao.deletePhoneNum(id);
    }

    /** 查询未发送过的手机号码列表 */
    @Override
    public List<String> queryPhoneNoList(Integer readCount) {
        List<String> numberList = mobileMsgDao.queryPhoneNoList(readCount, freezeNumber);
        freezeNumber.addAll(numberList);
        return numberList;
    }

    /** 更新手机发送状态 */
    @Override
    public void updatePhoneStatus(String phoneNum, Byte status) {
        int updateNum = mobileMsgDao.updatePhoneStatus(phoneNum, status);
        if(updateNum <= 0){
            logger.error("更新数据状态失败：" + phoneNum + "，状态：" + status);
        }
        // 收集处理发送后，移除冻结缓存的号码
        freezeNumber.remove(phoneNum);
    }

    /** 清除手机上的缓存号码 */
    @Override
    public String cleanCacheNumber(String numbers) {
        List<String> numberList = Arrays.asList(StringUtils.split(numbers, ","));
        boolean removeSuc = freezeNumber.removeAll(numberList);
        if(!removeSuc){
            return "移除失败！";
        }
        return ReturnData.SUCCESS;
    }
}
