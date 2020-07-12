package com.li.cloud.tool.dao;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.tool.entity.CustomerExportData;
import com.li.cloud.tool.entity.CustomerManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @desc 客户资源管理
 * @date 2020-06-01
 */
public interface CustomerManageDao {

    /** 分页查询数据 */
    List<CustomerManage> queryPageList(Pagination<CustomerManage> pagination);

    /** 分页查询重复数据 */
    List<CustomerManage> queryPageListForDup(Pagination<CustomerManage> pagination);

    /** 导入手机号码 */
    int importPhoneNum(@Param("insertList") List<CustomerManage> insertList);

    /** 导出数据 */
    List<CustomerExportData> exportCustomerData(Map<String, Object> params);

    /** 查询未检测的手机号码 */
    List<String> queryEmptyNumber();

    /** 根据手机号码更新数据状态 */
    int updateByNumber(@Param("phoneNumber") String phoneNumber, @Param("status") Byte status, @Param("remark") String remark);

}
