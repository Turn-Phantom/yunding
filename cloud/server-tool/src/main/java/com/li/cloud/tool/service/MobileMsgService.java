package com.li.cloud.tool.service;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.tool.entity.MobileNumInfo;

import java.util.List;

/**
 * @desc 手机短信 业务
 * @date 2020-05-06
 */
public interface MobileMsgService {

    /** 导入手机号码 */
    int importPhoneNum(List<MobileNumInfo> mobileNumInfoList);

    /** 查询新导入的数据 */
    List<MobileNumInfo> queryImport();

    /** 分页查询手机号码数据 */
    List<MobileNumInfo> queryPhoneNumberData(Pagination<MobileNumInfo> pagination);

    /** 根据id删除数据 */
    int deletePhoneNum(String id);

    /** 查询未发送过的手机号码列表 */
    List<String> queryPhoneNoList(Integer readCount);

    /** 更新手机发送状态 */
    void updatePhoneStatus(String phoneNum, Byte status);

    /** 清除缓存的号码 */
    String cleanCacheNumber(String numbers);
}
