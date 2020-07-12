package com.li.cloud.tool.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @desc 数据字典
 * @date 2020-06-08
 */
@Table(name = "tb_data_dictionary")
@Alias("dataDictionary")
@Setter
@Getter
@Entity
@ToString
public class DataDictionary {

    // 字典数据类型
    @Column(name = "dic_type")
    private String dicType;

    // 数据key值
    @Column(name = "dic_key")
    private String dicKey;

    // 数据代码
    @Column(name = "dic_code")
    private Integer dicCode;

    // 数据value值
    @Column(name = "dic_val")
    private String dicVal;

    // 数据值描述
    @Column(name = "dic_desc")
    private String dicDesc;

    // 相同key数据排序
    @Column(name = "data_sort")
    private Integer dataSort;

    // 字段顺序
    @Column(name = "sort_id")
    private Integer sortId;
}
