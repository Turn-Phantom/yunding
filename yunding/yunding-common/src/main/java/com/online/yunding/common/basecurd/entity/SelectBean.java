package com.online.yunding.common.basecurd.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * @desc: 公共下拉数据实体
 * @date: 2019-09-18
 */
@Setter
@Getter
@Alias("selectBean")
public class SelectBean {

    // 数据id
    private String id ;

    // 数据title
    private String title;

    // 数据内容
    private String content;

    // 数据状态
    private String dataStatus;

    // 数据代码
    private String dataCode;

    // 数据描述
    private String dataDescription;
}
