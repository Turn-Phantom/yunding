package com.yunding.server.common.basecurd.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @desc: 消息返回对象
 * @date: 2019-06-15
 */
@Getter
@Setter
public class ReturnData {

    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    // 返回类型
    private ReturnType returnType;

    // 返回内容
    private String content;

    // 返回对象
    private Object objectData;

    /**
     * @desc: 无参构造
     * @date: 2019-07-27
     */
    public ReturnData(){
    }

    /**
     * @desc: 有参构造
     * @param: type 返回类型
     * @param: content 返回内容
     * @return: ReturnData 消息返回对象
     * @date: 2019-07-27
     */
    public ReturnData(ReturnType type, String content){
        this.returnType = type;
        this.content = content;
    }

    /**
     * @desc: 有参构造
     * @param: type 返回类型
     * @param: object
     * @date: 2019-07-27
     */
    public ReturnData(ReturnType type, Object object){
        this.returnType = type;
        this.objectData = object;
    }

    /**
     * @desc: 有参构造
     * @param: type 返回类型
     * @param: content 返回内容
     * @param: object 返回对象
     * @date: 2019-07-27
     */
    public ReturnData(ReturnType type, String content, Object object){
        this.returnType = type;
        this.content = content;
        this.objectData = object;
    }

    /**
     * @desc: 成功返回
     * @param: content 返回内容
     * @return: ReturnData 消息返回对象
     * @date: 2019-07-27
     */
    public static ReturnData success(String content){
        return new ReturnData(ReturnType.success, content);
    }

    /**
     * @desc: 成功返回
     * @param: content 返回内容
     * @param: object 返回对象
     * @return: ReturnData 消息返回对象
     * @date: 2019-07-27
     */
    public static ReturnData success(String content, Object object){
        return new ReturnData(ReturnType.success, content, object);
    }

    /**
     * @desc: 成功返回
     * @param: object 返回对象
     * @return: ReturnData 消息返回对象
     * @date: 2019-07-27
     */
    public static ReturnData successData(Object object){
        return new ReturnData(ReturnType.success, object);
    }

    /**
     * @desc: 警告返回
     * @param: content 返回内容
     * @return: ReturnData 消息返回对象
     * @date: 2019-07-27
     */
    public static ReturnData warning(String content){
        return new ReturnData(ReturnType.warning, content);
    }

    /**
     * @desc: 错误返回
     * @param: content 返回内容
     * @return: ReturnData 消息返回对象
     * @date: 2019-07-27
     */
    public static ReturnData error(String content){
        return new ReturnData(ReturnType.error, content);
    }

    /**
     * @desc: 失败返回
     * @param: object 返回对象
     * @return: ReturnData 消息返回对象
     * @date: 2019-07-27
     */
    public static ReturnData errorData(Object object){
        return new ReturnData(ReturnType.error, object);
    }


    /** 未登录状态 */
    public static ReturnData unLogin(String content){
        return new ReturnData(ReturnType.unLogin, content);
    }
}
