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
 * @desc 用户操作日志
 * @date 2020-04-15
 */
@Table(name = "tb_user_operate_log")
@Alias("userOperateLog")
@Setter
@Getter
@Entity
@ToString
public class UserOperateLog {
    // 主键ID
    @Id
    @Column(name = "id")
    private Integer id;

    // 用户id
    @Column(name = "user_id")
     private Integer userId;

    // 修改内容
    @Column(name = "update_content")
     private String updateContent;

    // 修改时间
    @Column(name = "update_time")
     private Long updateTime;
}
