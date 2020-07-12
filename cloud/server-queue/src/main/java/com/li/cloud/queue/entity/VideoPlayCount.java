package com.li.cloud.queue.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc
 * @date 2020-04-20
 */
@Table(name = "tb_video_play_log")
@Alias("videoPlayCount")
@Setter
@Getter
@Entity
@ToString
public class VideoPlayCount {
    // 主键id
    @Id
    @Column(name = "id")
    private Integer id;

    // 影片id
    @Column(name = "video_id")
    private String videoId;

    // 影片点击播放次数
    @Column(name = "click_play")
    private Integer clickPlay;
}
