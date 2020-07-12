package com.li.cloud.tool.dao;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.tool.entity.MobileNumInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 手机信息 dao
 * @date 2020-05-06
 */
public interface MobileMsgDao {

    /** 导入手机号码 */
    int importPhoneNum(@Param("mobileNumInfoList") List<MobileNumInfo> mobileNumInfoList);

    /** 查询新导入的数据 */
    List<MobileNumInfo> queryImport();

    /** 分页查询手机号码数据 */
    List<MobileNumInfo> queryPhoneNumberData(Pagination<MobileNumInfo> pagination);

    /** 根据id删除数据 */
    int deletePhoneNum(String id);

    /** 查询未发送过的手机号码列表 */
    List<String> queryPhoneNoList(@Param("readCount") Integer readCount, @Param("freezeNumber") List<String> freezeNumber);

    /** 更新手机发送状态 */
    int updatePhoneStatus(@Param("phoneNum") String phoneNum, @Param("status") Byte status);
}
