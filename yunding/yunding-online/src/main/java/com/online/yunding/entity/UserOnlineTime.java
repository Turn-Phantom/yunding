package com.online.yunding.entity;

import cn.hutool.core.clone.CloneSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @desc 用户在线时长
 * @date 2020-04-20
 */
@Table(name = "tb_user_online")
@Alias("userOnlineTime")
@Setter
@Getter
@Entity
@ToString
public class UserOnlineTime extends CloneSupport<UserOnlineTime> {

    // 用户Id
    @Column(name = "user_id")
    private Integer userId;

    // 在线时长，单位：秒
    @Column(name = "online_sec")
    private Integer onlineSec;

    // 下级代理在线时长
    @Column(name = "child_online")
    private Integer childOnline;

    //  统计日期
    @Column(name = "online_date")
    private String onlineDate;

    // 开始计算时间
    @Column(name = "start_time")
    private Long startTime;

    // 结束时间
    @Column(name = "end_time")
    private Long endTime;
}
