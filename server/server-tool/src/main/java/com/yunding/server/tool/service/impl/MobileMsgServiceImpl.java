package com.yunding.server.tool.service.impl;

import com.yunding.server.common.annotations.CustomPage;
import com.yunding.server.common.basecurd.entity.Pagination;
import com.yunding.server.common.basecurd.entity.ReturnData;
import com.yunding.server.tool.dao.MobileMsgDao;
import com.yunding.server.tool.entity.MobileNumInfo;
import com.yunding.server.tool.service.MobileMsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc 手机短信 逻辑
 * @date 2020-05-06
 */
@Service
public class MobileMsgServiceImpl implements MobileMsgService {

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
    public List<String> queryPhoneNoList() {
        return mobileMsgDao.queryPhoneNoList();
    }

    /** 更新手机发送状态 */
    @Override
    public void updatePhoneStatus(String phoneNum, Byte status) {
        int updateNum = mobileMsgDao.updatePhoneStatus(phoneNum, status);
        if(updateNum <= 0){
            logger.error("更新数据状态失败：" + phoneNum + "，状态：" + status);
        }
    }
}
