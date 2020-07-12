package com.li.cloud.online.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc 服务联系方式
 * @date 2020-05-19
 */
@Table(name = "tb_service_contact")
@Alias("serviceContact")
@Setter
@Getter
@Entity
@ToString
public class ServiceContact {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 联系方式唯一识别key
    @Column(name = "data_key")
    private String dataKey;

    // 联系方式类型：1 QQ
    @Column(name = "type")
    private Byte type;

    // 类型说明
    @Column(name = "type_desc")
    private String typeDesc;

    // 联系方式； 多个以逗号隔开
    @Column(name = "way")
    private String way;

    // 备注说明
    @Column(name = "remark")
    private String remark;
}
