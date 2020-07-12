package com.li.cloud.tool.service;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.tool.entity.CustomerExportData;
import com.li.cloud.tool.entity.CustomerManage;

import java.util.List;
import java.util.Map;

/**
 * @desc 客户资源管理
 * @date 2020-06-01
 */
public interface CustomerManageService {

    /** 分页查询数据 */
    List<CustomerManage> queryPageList(Pagination<CustomerManage> pagination);

    /** 插入数据 */
    int importPhoneNum(List<CustomerManage> insertList);

    /** 导出数据 */
    List<CustomerExportData> exportCustomerData(Map<String, Object> params);

    /** 查询未检测的手机号码 */
    List<String> queryEmptyNumber();

    /** 空号检测 */
    String executeEmptyNumberCheck();

    /** 根据手机号码更新数据状态 */
    int updateByNumber(String phoneNumber, Byte status);
}
