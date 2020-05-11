package com.yunding.server.tool.controller;

import com.alibaba.excel.EasyExcel;
import com.yunding.server.common.basecurd.entity.Pagination;
import com.yunding.server.common.basecurd.entity.ReturnData;
import com.yunding.server.tool.entity.MobileNumInfo;
import com.yunding.server.tool.listener.ExcelDataListener;
import com.yunding.server.tool.service.MobileMsgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @desc 手机短信控制器
 * @date 2020-05-06
 */
@RestController
@RequestMapping("/rest/mobile/msg")
public class MobileMsgController {

    @Autowired
    private MobileMsgService mobileMsgService;

    /** 分页查询手机号码数据 */
    @GetMapping("/queryPhoneNumberData")
    public ReturnData queryPhoneNumberData(Pagination<MobileNumInfo> pagination){
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
            EasyExcel.read(file.getInputStream(), MobileNumInfo.class, new ExcelDataListener(mobileMsgService)).sheet().doRead();
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

    /** 查询未发送过的手机号码列表 */
    @GetMapping("/queryPhoneNoList")
    public ReturnData queryPhoneNoList(){
        return ReturnData.successData(mobileMsgService.queryPhoneNoList());
    }

    /** 更新手机发送状态 */
    @PostMapping("/updatePhoneStatus")
    public void updatePhoneStatus(String phoneNum, Byte status){
        mobileMsgService.updatePhoneStatus(phoneNum, status);
    }
}
