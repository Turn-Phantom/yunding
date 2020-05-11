package com.online.yunding.entity;

import cn.hutool.core.clone.CloneSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @desc 访问记录信息
 *  使用hutool工具类的克隆方法： extents CloneSupport<T>
 * @date 2020-04-08
 */
@Table(name = "tb_visit_dayrecord")
@Alias("visitRecord")
@Setter
@Getter
@Entity
@ToString
public class VisitRecord extends CloneSupport<VisitRecord> implements Serializable {

    // 访问者 0 匿名用户、 1 游客、2 登录用户
    private Integer visitor;

    // 访问者id，只有登录用户才有id
    private Integer visitId;

    // 访问者ip地址
    private String visitIp;

    // 访问者代理ip地址
    private String proxyIp;

    // 访问量
    private Integer visitCount;

    // 虚拟访问量
    private Integer fictitiousCount;

    // 访问者mac地址
    private String visitMac;

    // 浏览器版本
    private String browserVer;

    // 操作系统
    private String sysVer;

    // ip归属地
    private String ipAddress;

    // 点击网站来源
    private String visitSource;

    // 访问日期
    private String visitDate;

    // 第一次访问时间
    private Long visitTime;

    // 更新时间
    private Long updateTime;

    // 更新次数
    private Integer updateCount;

    // 用户账号
    private String accountNo;
}
