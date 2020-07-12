package com.li.cloud.video.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc 影片操作日志
 * @date 2020-04-16
 */
@Table(name = "tb_video_operate_log")
@Alias("videoOperateLog")
@Setter
@Getter
@Entity
@ToString
public class VideoOperateLog {
    // 主键id
    @Id
    @Column(name = "id")
    private Integer id;

    // 操作位置
    @Column(name = "operate_pos")
    private String operatePos;

    // 操作内容
    @Column(name = "operate_content")
    private String operateContent;

    // 操作结果
    @Column(name = "operate_res")
    private String operateRes;

    // 操作时间
    @Column(name = "operate_time")
    private Long operateTime;
}
