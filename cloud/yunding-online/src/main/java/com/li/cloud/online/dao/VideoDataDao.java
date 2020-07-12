package com.li.cloud.online.dao;

import java.util.List;
import java.util.Map;

/**
 * @desc 视频数据 dao
 * @date 2020-04-26
 */
public interface VideoDataDao {

    /** 获取滚动数据 */
    List<Map<String,Object>> queryScrollData();
}
