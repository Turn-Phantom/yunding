package com.online.yunding.service.impl;

import com.online.yunding.common.annotations.CustomPage;
import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.dao.AdvertManageDao;
import com.online.yunding.entity.AdvertManage;
import com.online.yunding.service.AdvertManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc 广告管理 逻辑
 * @date 2020-05-02
 */
@Service
public class AdvertManageServiceImpl implements AdvertManageService {

    @Autowired
    private AdvertManageDao advertManageDao;

    /** 分页查询列表数据 */
    @Override
    @CustomPage
    public List<AdvertManage> queryAdvertDataList(Pagination<AdvertManage> pagination) {
        return advertManageDao.queryAdvertDataList(pagination);
    }

    /** 根据位置查询是否已经存在数据; */
    @Override
    public AdvertManage queryDataByField(String pagePos, String advPos) {
        return advertManageDao.queryDataByField(pagePos, advPos);
    }

    /** 根据页面位置，获取广告链接数据 */
    @Override
    public List<AdvertManage> queryAdvertByPagePos(String pagePos) {
        return advertManageDao.queryAdvertByPagePos(pagePos);
    }
}
