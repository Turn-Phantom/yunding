package com.online.yunding.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @desc 服务参数对象
 * @date 2020-04-17
 */
@Getter
@Setter
public class ServerObj {

    // 主机地址
    private String host;

    // 端口
    private Integer port;

    // 地址前缀
    private String url;
}
