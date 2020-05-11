package com.yunding.server.queue.constant;

/**
 * @desc
 * @date 2020-04-09
 */
public interface ConstantStr {

    int FOUR_SECOND = 4 * 60;

    /** redis缓存中ip地址前缀信息 */
    String IP_PREFIX = "ipAddr:";

    /** 查询ip地址归属地 */
    String URL_IP_ADDR = "http://opendata.baidu.com/api.php?query=%s&co=&resource_id=6006&t=1412300361645&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu";

    /** 影片id集合 */
    String VIDEO_ID_LIST_KEY = "videoIdListForCount";

}
