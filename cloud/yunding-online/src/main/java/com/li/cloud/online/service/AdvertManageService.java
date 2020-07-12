package com.li.cloud.online.service;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.online.entity.AdvertManage;

import java.util.List;

/**
 * @desc 广告管理 接口
 * @date 2020-05-02
 */
public interface AdvertManageService {

    /** 分页查询列表数据 */
    List<AdvertManage> queryAdvertDataList(Pagination<AdvertManage> pagination);

    /** 根据位置查询是否已经存在; */
    AdvertManage queryDataByField(String pagePos, String advPos);

    /** 根据页面位置，获取广告链接数据 */
    List<AdvertManage> queryAdvertByPagePos(String pagePos);
}
