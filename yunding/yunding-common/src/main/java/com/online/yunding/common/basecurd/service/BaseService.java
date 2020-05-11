package com.online.yunding.common.basecurd.service;

/**
 * @desc: 通用业务逻辑接口
 * @date: 2019-06-14
 */
public interface BaseService {
    /**
     * @desc: 新增
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    <T> int insert(T entity);

    /**
     * @desc: 新增
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    <T> int insertNoId(T entity);

    /**
     * @desc: 删除
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    <T> int delete(T entity);

    /**
     * @desc: 批量删除（针对id为数字类型），
     * @param: entity 实体对象
     * @param: deleteField 删除条件字段
     * @param: deleteVals 删除条件值（批量)
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    <T> int deleteBatch(T entity, String deleteField, String deleteVals);

    /**
     * @desc: 根据指定字段值删除数据
     * @param: entity 实体对象
     * @param: deleteField 指定字段
     * @return: int 删除影响行数
     * @date: 2019-10-02
     */
    <T> int deleteByField(T entity, String deleteField);

    /**
     * @desc: 更新
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    <T> int update(T entity);

    /**
     * @desc: 更新部分字段
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    <T> int updateField(T entity);

    /**
     * @desc: 根据id查询数据
     * @param: entity 实体对象（只取id）
     * @return: Map 结果集
     * @date: 2019-07-21
     */
    <T> T queryById(T entity);

    /**
     * @desc: 根据实体某一字段查询数据，仅限唯一数据
     * @param: entity 实体对象
     * @param: field 查询字段
     * @return: Map 结果集
     * @date: 2019-10-02
     */
    <T> T queryDataByField(T entity, String field);
}
