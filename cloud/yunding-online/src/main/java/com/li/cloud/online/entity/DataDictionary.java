package com.li.cloud.online.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @desc 数据字典
 * @date 2020-05-02
 */
@Table(name = "tb_data_dictionary")
@Alias("dataDictionary")
@Setter
@Getter
@Entity
@ToString
public class DataDictionary {

    // 字典key值
    private String dicKey;

    // 字典value值
    private String dicVal;

    // 字典代码
    private String dicCode;

    // 备注描述
    private String dicDesc;

    // 字段顺序
    private Integer sortId;

    // 相同key数据排序
    private Integer dataSort;
}
