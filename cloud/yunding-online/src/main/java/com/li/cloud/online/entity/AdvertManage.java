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
 * @desc 广告管理
 * @date 2020-05-02
 */
@Table(name = "tb_advert_manage")
@Alias("advertManage")
@Setter
@Getter
@Entity
@ToString
public class AdvertManage {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 广告来源：1 云顶在线web，2 云顶在线移动
    @Column(name = "adv_source")
    private String advSource;

    // 页面位置
    @Column(name = "page_pos")
    private String pagePos;

    // 广告位置，文字描述
    @Column(name = "adv_pos")
    private String advPos;

    // 广告链接地址
    @Column(name ="adv_link")
    private String advLink;

    // 广告图片地址
    @Column(name = "img_link")
    private String imgLink;

    // 广告大小：宽:高，例如：300:200
    @Column(name = "img_size")
    private String imgSize;

    // 预览图片地址
    @Column(name = "pre_img")
    private String preImg;

    // 放大图片地址
    @Column(name = "max_img")
    private String maxImg;

    // 创建时间
    @Column(name = "create_time")
    private Long createTime;

    // 更新时间
    @Column(name = "update_time")
    private Long updateTime;

    // 备注
    @Column(name = "remark")
    private String remark;
}
