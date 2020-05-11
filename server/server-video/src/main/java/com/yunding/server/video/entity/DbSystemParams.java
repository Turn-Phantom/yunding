package com.yunding.server.video.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc 数据库系统参数
 * @date 2020-04-17
 */
@Table(name = "tb_sys_params")
@Alias("dbSystemParams")
@Setter
@Getter
@Entity
@ToString
public class DbSystemParams {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 参数key，唯一值
    @Column(name = "par_key")
    private String parKey;

    // 参数值
    @Column(name = "par_val")
     private String parVal;

    // 参数状态
    @Column(name = "par_state")
    private Integer parState;

    // 参数描述
    @Column(name = "par_desc")
    private String parDesc;

    // 备用字段
    @Column(name = "spare_field")
    private String spareField;
}
