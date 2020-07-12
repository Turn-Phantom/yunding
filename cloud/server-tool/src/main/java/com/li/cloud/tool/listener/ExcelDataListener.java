package com.li.cloud.tool.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.li.cloud.tool.entity.CustomerManage;
import com.li.cloud.tool.service.CustomerManageService;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc excel数据处理监听器; 不支持spring自动管理bean
 * @date 2020-05-06
 */
public class ExcelDataListener extends AnalysisEventListener<CustomerManage> {

    /** 批量插入最大条数 */
    private final int BATCH_COUNT = 200;

    /** 插入数据临时存储 */
    List<CustomerManage> insertList = new ArrayList<>();

    private CustomerManageService customerManageService;

    public ExcelDataListener() {
    }

    public ExcelDataListener(CustomerManageService customerManageService){
        this.customerManageService = customerManageService;
    }

    /** 每解析一条数据，调用一次 */
    @Override
    public void invoke(CustomerManage customerManage, AnalysisContext analysisContext) {
        String phoneNum = customerManage.getPhone();
        if(StringUtils.isEmpty(phoneNum)){
            return;
        }
        if(StringUtils.startsWith(phoneNum, "0086")){
            customerManage.setPhone(StringUtils.replaceOnce(phoneNum, "00", "+"));
        }
        insertList.add(customerManage);
        // 当数据集合大于批量插入上限时，先插入数据库
        if(insertList.size() >= BATCH_COUNT){
            saveData();
        }
    }

    /** 所有数据解析完成后调用一次 */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(insertList.size() > 0 ){
            saveData();
        }
    }

    /** 保存用户数据 */
    private void saveData() {
        // 插入成功后清除数据
        int insertNum = customerManageService.importPhoneNum(insertList);
        if(insertNum <= 0 ){
            throw new RuntimeException("导入失败");
        }
        insertList.clear();
    }
}
