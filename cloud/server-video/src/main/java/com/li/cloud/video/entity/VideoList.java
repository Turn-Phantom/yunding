package com.li.cloud.video.entity;

import cn.hutool.core.clone.CloneSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc 影片清单
 * @date 2020-04-16
 */
@Table(name = "tb_video_list")
@Alias("videoList")
@Setter
@Getter
@Entity
@ToString
public class VideoList extends CloneSupport<VideoList> {
    // 主键id
    @Id
    @Column(name = "id")
    private Integer id;

    // 影片id
    @Column(name = "cid")
    private String cid;

    // 状态; 2 上架；  3 已下架
    @Column(name = "state")
    private Integer state;

    // 影片分类 : "ch": "歐美",
    @Column(name = "video_class")
    private String videoClass;

    // 是否支持影片画质，1200K； 1 支持，0 不支持
    @Column(name = "is_1200")
    private Integer imgQuality1;

    // 是否支持影片画质，600K； 1 支持，0 不支持
    @Column(name = "is_600")
    private Integer imgQuality2;

    // 是否支持影片画质，300K； 1 支持，0 不支持
    @Column(name = "is_300")
    private Integer imgQuality3;

    // 影片地址: "v_url": "/2020/2/0117/PTB-05291/PTB-05291-600K.mp4",
    @Column(name = "video_url")
    private String videoUrl;

    // 影片播放前图片地址："p_url": "/PTB-05291/PTB-05291m.jpg",
    @Column(name = "pic_url")
    private String picUrl;

    // 影片名称
    @Column(name = "video_name")
    private String videoName;

    // 演员， 多个通过|分隔
    @Column(name = "actor")
    private String actor;

    // 影片标签， 多个通过|分隔
    @Column(name = "video_tag")
    private String videoTag;

    // 影片提供者， 多个通过|分隔
    @Column(name = "vendor")
    private String vendor;

    // 影片描述
    @Column(name = "video_desc")
    private String videoDesc;

    // 更新时间; 单位/秒; "utime": 1585673582,
    @Column(name = "update_time")
    private Long updateTime;

    // 点击次数： "clicks": 0,
    @Column(name = "click_count")
    private Integer clickCount;

    // 影片异动日期；（修改日期） "enable_date": 20200416
    @Column(name = "enable_date")
    private Integer enableDate;

    // 操作时间
    @Column(name = "operate_time")
    private Long operateTime;

    // 影片播放次数
    private Integer playCount;
}
