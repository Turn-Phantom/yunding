package com.li.cloud.tool.controller;

import com.alibaba.excel.EasyExcel;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.tool.entity.MobileNumInfo;
import com.li.cloud.tool.listener.GroupExcelDataListener;
import com.li.cloud.tool.service.MobileMsgService;
import com.li.cloud.tool.service.impl.MobileMsgServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @desc 群发号码管理 控制器
 * @date 2020-06-09
 */
@RestController
@RequestMapping("/rest/mobile/msg")
public class GroupSendController {

    @Autowired
    private MobileMsgService mobileMsgService;

    /** 分页查询手机号码数据 */
    @GetMapping("/queryPhoneNumberData")
    public ReturnData queryPhoneNumberData(Pagination<MobileNumInfo> pagination){
        // 判断是否查询冻结数据
        String freezeData = (String)pagination.getParams().get("queryFreezeNumber");
        if(!StringUtils.isBlank(freezeData) && "freeze".equals(freezeData)){
            pagination.getParams().clear();
            pagination.getParams().put("freezeNumber", MobileMsgServiceImpl.freezeNumber);
            if(MobileMsgServiceImpl.freezeNumber.isEmpty()){
                return ReturnData.successData(pagination);
            }
        }
        mobileMsgService.queryPhoneNumberData(pagination);
        return ReturnData.successData(pagination);
    }

    /** 导入手机号码 */
    @PostMapping("/importPhoneNo")
    public ReturnData importPhoneNum(MultipartFile file){
        if(null == file){
            return ReturnData.error("导入文件不能为空！");
        }
        // 读取excel数据，并通过监听器处理数据
        try {
            EasyExcel.read(file.getInputStream(), MobileNumInfo.class, new GroupExcelDataListener(mobileMsgService)).sheet().doRead();
        } catch (IOException e) {
            return ReturnData.error("导入失败：" + e.getMessage());
        }
        return ReturnData.success("导入成功！");
    }

    /** 查询新导入的数据 */
    @GetMapping("/queryImport")
    public ReturnData queryImport(){
        return ReturnData.successData(mobileMsgService.queryImport());
    }

    /** 根据id删除数据 */
    @PostMapping("/deletePhoneNum")
    public ReturnData deletePhoneNum(String id){
        if(StringUtils.isBlank(id)){
            return ReturnData.error("数据id不能为空");
        }
        int delNum = mobileMsgService.deletePhoneNum(id);
        if(delNum <= 0){
            return ReturnData.error("删除失败");
        }
        return ReturnData.success("删除成功");
    }

    /** 清空缓存待发送号码 */
    @GetMapping("/cleanFreezeNumber")
    public ReturnData cleanFreezeNumber(){
        MobileMsgServiceImpl.freezeNumber.clear();
        return ReturnData.success("清除成功！");
    }
}
