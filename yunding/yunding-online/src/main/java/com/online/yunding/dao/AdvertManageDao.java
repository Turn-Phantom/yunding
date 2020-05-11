package com.online.yunding.dao;

import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.entity.AdvertManage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 广告管理 dao
 * @date 2020-05-02
 */
public interface AdvertManageDao {

    /** 分页查询列表数据 */
    List<AdvertManage> queryAdvertDataList(Pagination<AdvertManage> pagination);

    /** 根据位置查询是否已经存在; */
    AdvertManage queryDataByField(@Param("pagePos") String pagePos, @Param("advPos") String advPos);

    /** 根据页面位置，获取广告链接数据 */
    List<AdvertManage> queryAdvertByPagePos(String pagePos);
}
