package com.li.cloud.tool.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.li.cloud.tool.entity.MobileNumInfo;
import com.li.cloud.tool.service.MobileMsgService;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc excel数据处理监听器
 * @date 2020-05-06
 */
public class GroupExcelDataListener extends AnalysisEventListener<MobileNumInfo> {

    /** 批量插入最大条数 */
    private final int BATCH_COUNT = 200;

    /** 插入数据临时存储 */
    List<MobileNumInfo> insertList = new ArrayList<>();

    private MobileMsgService mobileMsgService;

    public GroupExcelDataListener() {
    }

    public GroupExcelDataListener(MobileMsgService mobileMsgService){
        this.mobileMsgService = mobileMsgService;
    }

    /** 每解析一条数据，调用一次 */
    @Override
    public void invoke(MobileNumInfo mobileNumInfo, AnalysisContext analysisContext) {
        insertList.add(mobileNumInfo);
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
        int insertNum = mobileMsgService.importPhoneNum(insertList);
        if(insertNum <= 0 ){
            throw new RuntimeException("导入失败");
        }
        insertList.clear();
    }
}
