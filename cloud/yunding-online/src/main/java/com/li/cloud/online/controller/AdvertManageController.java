package com.li.cloud.online.controller;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.online.entity.AdvertManage;
import com.li.cloud.online.entity.DbSystemParams;
import com.li.cloud.online.service.AdvertManageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 广告管理 控制器
 * @date 2020-05-02
 */
@RestController
@RequestMapping("/rest/advert/manage")
public class AdvertManageController {

    @Autowired
    private AdvertManageService advertManageService;

    @Autowired
    private BaseService baseService;

    /** 分页查询列表数据 */
    @GetMapping("/queryAdvertDataList")
    public ReturnData queryAdvertDataList(Pagination<AdvertManage> pagination){
        advertManageService.queryAdvertDataList(pagination);
        return ReturnData.successData(pagination);
    }

    /** 根据id查询数据 */
    @GetMapping("/queryAdvertDataById")
    public ReturnData queryAdvertDataById(AdvertManage advertManage){
        return ReturnData.successData(baseService.queryById(advertManage));
    }

    /** 新增广告管理 */
    @PostMapping("/save")
    public ReturnData save(AdvertManage advertManage){
        String validRet = validAdvertEntity(advertManage);
        if(!ReturnData.SUCCESS.equals(validRet)){
            return ReturnData.error(validRet);
        }
        // 根据位置查询是否已经存在;
        AdvertManage advPos = advertManageService.queryDataByField(advertManage.getPagePos(), advertManage.getAdvPos());
        if(null != advPos){
            return ReturnData.error("该位置广告已存在，不允许重复添加！");
        }
        advertManage.setCreateTime(System.currentTimeMillis());
        int insertNum = baseService.insert(advertManage);
        if(insertNum <= 0){
            return ReturnData.error("新增广告管理失败！");
        }
        return ReturnData.success("新增成功！");
    }

    /** 更新 */
    @PostMapping("/update")
    public ReturnData update(AdvertManage advertManage){
        if(null == advertManage.getId()){
            return ReturnData.error("数据id不能为空");
        }
        // 查询是否已经存在， 且判断id是否相同
        AdvertManage advPos = advertManageService.queryDataByField(advertManage.getPagePos(), advertManage.getAdvPos());
        if(null != advPos && !advPos.getId().equals(advertManage.getId())){
            return ReturnData.error("该位置已存在广告，请重新选择！");
        }
        advertManage.setUpdateTime(System.currentTimeMillis());
        int updateNum = baseService.updateField(advertManage);
        if(updateNum <= 0){
            return ReturnData.error("更新失败，更新行数为0");
        }
        return ReturnData.success("更新成功！");
    }

    /** 删除 */
    @GetMapping("/delete")
    public ReturnData delete(AdvertManage advertManage){
        if(null == advertManage.getId()){
            return ReturnData.error("数据id不能为空");
        }
        int delNum = baseService.delete(advertManage);
        if(delNum <= 0){
            return ReturnData.error("删除失败，删除行数为0");
        }
        return ReturnData.success("删除成功！");
    }

    /* 校验 */
    private String validAdvertEntity(AdvertManage advertManage){
        if(StringUtils.isBlank(advertManage.getPagePos())){
            return "请选择页面位置！";
        }
        if(StringUtils.isBlank(advertManage.getAdvPos())){
            return "请输入广告详细位置";
        }
        if(StringUtils.isBlank(advertManage.getAdvSource())){
            return "请选择广告来源";
        }
        if(StringUtils.isBlank(advertManage.getAdvLink())){
            return "请输入广告链接";
        }
        return ReturnData.SUCCESS;
    }

    /** 设置图片地址前缀 */
    @GetMapping("/setImgPre")
    public ReturnData setImgPre(String imgPre){
        if(StringUtils.isBlank(imgPre)){
            return ReturnData.error("图片地址前缀不可为空！");
        }
        DbSystemParams systemParams = new DbSystemParams();
        systemParams.setParKey("advertImgPreUrl");
        systemParams.setParVal(imgPre);
        systemParams.setParDesc("广告图片地址前缀");
        int insertNum = baseService.insert(systemParams);
        if(insertNum <= 0){
            return ReturnData.error("设置广告图片地址前缀失败！");
        }
        return ReturnData.success("设置成功！");
    }

    /** 更新广告地址前缀 */
    @GetMapping("/updateImgPre")
    public ReturnData updateImgPre(Integer id, String imgPre){
        if(null == id || StringUtils.isBlank(imgPre)){
            return ReturnData.error("参数不可为空！");
        }
        DbSystemParams systemParams = new DbSystemParams();
        systemParams.setId(id);
        systemParams.setParVal(imgPre);
        int updateNum = baseService.updateField(systemParams);
        if(updateNum <= 0){
            return ReturnData.error("修改广告图片地址前缀失败！");
        }
        return ReturnData.success("修改成功！");
    }

    /** 根据页面位置，获取广告链接数据 */
    @GetMapping("/queryAdvertByPagePos")
    public ReturnData queryAdvertByPagePos(String pagePos){
        if(StringUtils.isBlank(pagePos)){
            return ReturnData.error("参数不可为空！");
        }
        return ReturnData.successData(advertManageService.queryAdvertByPagePos(pagePos));
    }
}
