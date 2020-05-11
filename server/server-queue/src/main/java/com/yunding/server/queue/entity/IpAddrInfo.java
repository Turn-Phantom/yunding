package com.yunding.server.queue.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc IP地址福归属地信息表
 * @date 2020-04-09
 */
@Table(name = "tb_addr_ipAddr")
@Alias("ipAddrInfo")
@Setter
@Getter
@Entity
@ToString
public class IpAddrInfo {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // IP地址
    @Column(name = "ip")
    private String ip;

    // 地址
    @Column(name = "address")
    private String address;

    // 国家
    @Column(name = "country")
    private String country;

    // 省份
    @Column(name = "province")
    private String province;

    // 城市
    @Column(name = "city")
    private String city;

    // 乡镇
    @Column(name = "village")
    private String village;

    // 备用字段1
    @Column(name = "spare1")
    private String spare1;

    // 备用字段2
    @Column(name = "spare2")
    private String spare2;

    // 备用字段3
    @Column(name = "spare3")
    private String spare3;
}
