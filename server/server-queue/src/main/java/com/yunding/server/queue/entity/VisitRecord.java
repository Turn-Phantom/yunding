package com.yunding.server.queue.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @desc 访问记录信息
 * @date 2020-04-08
 */
@Table(name = "tb_visit_dayrecord")
@Alias("visitRecord")
@Setter
@Getter
@ToString
public class VisitRecord implements Serializable {

    // 访问者 0 匿名用户、 1 游客、2 登录用户
    @Column(name = "visitor")
    private Integer visitor;

    // 访问者id，只有登录用户才有id
    @Column(name = "visit_id")
    private Integer visitId;

    // 访问者ip地址
    @Column(name = "visit_ip")
    private String visitIp;

    // 访问者代理ip地址
    @Column(name = "proxy_ip")
    private String proxyIp;

    // 访问量
    @Column(name = "visit_count")
    private Integer visitCount;

    // 虚拟访问量
    @Column(name = "fictitious_count")
    private Integer fictitiousCount;

    // 访问者mac地址
    @Column(name = "visit_mac")
    private String visitMac;

    // 浏览器版本
    @Column(name = "browser_ver")
    private String browserVer;

    // 操作系统
    @Column(name = "sys_ver")
    private String sysVer;

    // IP归属地
    @Column(name = "ip_address")
    private String ipAddress;

    // 点击网站来源
    @Column(name = "visit_source")
    private String visitSource;

    // 访问日期
    @Column(name = "visit_date")
    private String visitDate;

    // 第一次访问时间
    @Column(name = "visit_time")
    private Long visitTime;

    // 更新时间
    @Column(name = "update_time")
    private Long updateTime;

    // 更新次数
    @Column(name = "update_count")
    private Integer updateCount;
}
