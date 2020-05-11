package com.online.yunding.security.constant;

/**
 * @desc 固定请求路径
 * @date 2020-03-22
 */
public interface FinalUrl {

    // 表单登录
    String LOGIN_FORM = "/authentication/loginForm";
    // 登录处理器
    String LOGIN_PROCESSOR = "/authentication/require";
    // 短信验证码发送
    String SMS_CODE_SEND = "/validate/code/sendSmsCode";
    // 短信登录
    String LOGIN_SMS = "/authentication/mobile";
    // SESSION失效处理
    String SESSION_INVALID = "/authentication/session/invalid";
    // 查询用户是否存在
    String QUERY_HAS_USER = "/rest/user/isRegister";
    // 注册
    String USER_REGISTER = "/rest/user/register";
    // 记录ip
    String RECORD_IP = "/rest/visit/refreshTime";
    // 获取影片清单分类数据
    String VIDEO_CLASS_LIST = "/rest/video/queryVideoListByClass";
    // 获取热播影片
    String VIDEO_HOT_VIDEO = "/rest/video/queryHotVideo";
    // 获取推荐影片数据
    String QUERY_RCMD_VIDEO = "/rest/video/queryRecommendVideo";
    // 获取免费影片TOKEN
    String FREE_VIDEO_TOKEN = "/rest/video/queryFredVideoToken";
    // 根据影片id获取影片信息
    String QUERY_INFO_BY_VIDEO_ID = "/rest/video/getVideoByVideoId";
    // 观看影片次数统计
    String VIDEO_PLAY_COUNT = "/rest/video/recordVideoPlay";
    /** 获取播放频率最高的，高清的10条数据 */
    String QUERY_HEIGHT_VIDEO = "/rest/video/queryHeightVideo";

    // 获取首页虚拟滚动数据
    String QUERY_SCROLL_DATA = "/rest/video/getScrollData";

    // 查询短信剩余量
    String QUERY_SMS_SURPLUS = "/rest/manage/querySmsSurplus";

    // 放行 web socket 连接
    String WEB_SOCKET_LINK = "/web/socket/*";
    // 放行获取广告链接
    String ADVERT_LINK_DATA = "/rest/advert/manage/queryAdvertByPagePos";
}
