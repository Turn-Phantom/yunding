package com.li.cloud.tool.controller;

import com.alibaba.excel.EasyExcel;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.NumberUtils;
import com.li.cloud.tool.entity.CustomerExportData;
import com.li.cloud.tool.entity.CustomerManage;
import com.li.cloud.tool.listener.ExcelDataListener;
import com.li.cloud.tool.service.CustomerManageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @desc 客户资源管理
 * @date 2020-06-01
 */
@RestController
@RequestMapping("/rest/customer")
public class CustomerManageController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private CustomerManageService customerManageService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 分页查询数据 */
    @GetMapping("/queryPageList")
    public ReturnData queryPageList(Pagination<CustomerManage> pagination){
        customerManageService.queryPageList(pagination);
        return ReturnData.successData(pagination);
    }

    /** 根据id查询数据 */
    @GetMapping("/queryCustomerInfoById")
    public ReturnData queryCustomerInfoById(CustomerManage customerManage){
        if(customerManage.getId() == null){
            return ReturnData.error("数据id不能为空！");
        }
        return ReturnData.successData(baseService.queryById(customerManage));
    }

    /** 新增号码 */
    @PostMapping("/insert")
    public ReturnData insert(CustomerManage customerManage){
        // 字段校验
        String validRes = validateField(customerManage);
        if(!ReturnData.SUCCESS.equals(validRes)){
            return ReturnData.error(validRes);
        }
        int insert = baseService.insert(customerManage);
        if(insert <= 0){
            return ReturnData.error("新增失败，无法插入到数据库");
        }
        return ReturnData.success("新增成功");
    }

    /** 修改号码 */
    @PostMapping("/update")
    public ReturnData update(CustomerManage customerManage){
        // 字段校验
        String validRes = validateField(customerManage);
        if(!ReturnData.SUCCESS.equals(validRes)){
            return ReturnData.error(validRes);
        }
        int update = baseService.update(customerManage);
        if(update <= 0){
            return ReturnData.error("修改失败，无法更新数据库");
        }
        return ReturnData.success("修改成功!");
    }

    /** 修改号码 */
    @PostMapping("/updateByField")
    public ReturnData updateByField(CustomerManage customerManage){
        // 字段校验
        if(null == customerManage.getId()){
            return ReturnData.error("数据id不能为空");
        }
        int update = baseService.updateField(customerManage);
        if(update <= 0){
            return ReturnData.error("修改失败，无法更新数据库");
        }
        return ReturnData.success("修改成功!");
    }

    /** 删除数据 */
    @GetMapping("/delete")
    public ReturnData delete(String id){
        if(StringUtils.isBlank(id)){
            return ReturnData.error("删除失败：数据id不能为空！");
        }
        int delNum = baseService.deleteBatch(CustomerManage.class, "id", id);
        if(delNum <= 0){
            return ReturnData.error("删除失败,原因：删除影响行数为: " + delNum);
        }
        return ReturnData.success("成功删除"+ delNum +"条数据");
    }

    /** 上传文件 */
    @PostMapping("/importCustomerData")
    public ReturnData importCustomerData(MultipartFile file){
        if(null == file){
            return ReturnData.error("导入失败：上传文件流为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), CustomerManage.class, new ExcelDataListener(customerManageService)).sheet().doRead();
        } catch (IOException e) {
            logger.error("文件：" + file.getOriginalFilename() + "导入失败；", e);
            return ReturnData.error("文件：" + file.getOriginalFilename() + "导入失败!");
        }
        return ReturnData.success("导入成功！");
    }

    /** 导出号码数据 */
    @PostMapping("/exportCustomerData")
    public void exportCustomerData(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("导出资源", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        List<CustomerExportData> customerExportData = customerManageService.exportCustomerData(params);

        EasyExcel.write(response.getOutputStream(), CustomerExportData.class).sheet("sheet1").doWrite(customerExportData);
    }

    /** 查询未检测的手机号码 */
    @GetMapping("/queryEmptyNumber")
    public ReturnData queryEmptyNumber(){
        return ReturnData.successData(customerManageService.queryEmptyNumber());
    }

    /** 空号检测 */
    @GetMapping("/executeEmptyNumberCheck")
    public ReturnData executeEmptyNumberCheck(){
        String ret = customerManageService.executeEmptyNumberCheck();
        if(!ReturnData.SUCCESS.equals(ret)){
            return ReturnData.error(ret);
        }
        return ReturnData.success("后台检测中，请刷新数据查看检查状态！");
    }

    /** 实体字段校验 */
    private String validateField(CustomerManage customerManage){
        if(StringUtils.isBlank(customerManage.getPhone())){
            return "手机号码不能为空";
        }
        if(!NumberUtils.isPhoneNun(StringUtils.replace(customerManage.getPhone(), "+86", ""))){
            return "请输入正确的手机号码";
        }
        return ReturnData.SUCCESS;
    }

}
