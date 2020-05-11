package com.yunding.server.video.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @desc
 * @date 2020-04-16
 */
@Getter
@Setter
public class VideoConfig {

    // 影片清单地址
    private String videoListUrl;

    // 影片异动地址
    private String videoChangeUrl;

    // 影片更新地址
    private String videoUpdateUrl;

    // 账户id
    private String accountId;

    private String sid;

    private String secret;

    private String videoDns;
}
