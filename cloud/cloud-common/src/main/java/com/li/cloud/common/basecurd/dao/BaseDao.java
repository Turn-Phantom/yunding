package com.li.cloud.common.basecurd.dao;

import com.li.cloud.common.basecurd.utils.SqlBuilder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @desc: 通用dao 接口
 * @date: 2019-06-14
 */
@Repository
public interface BaseDao {

    /**
     * @desc: 新增
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    @InsertProvider(type = SqlBuilder.class, method = "insert")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    <T> int insert(T entity);

    /**
     * @desc 不使用自增长id插入数据
     * @param entity
     * @return
     * @date 2020-04-08
     */
    @InsertProvider(type = SqlBuilder.class, method = "insert")
    <T> int insertNoId(T entity);
    
    /**
     * @desc: 删除
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    @DeleteProvider(type = SqlBuilder.class, method = "delete")
    <T> int delete(T entity);

    /**
     * @desc: 批量删除；指定字段且指定字段值， 默认以逗号分割
     * @param: entity 实体对象
     * @param: deleteField 删除条件的字段
     * @param: deleteVals 删除条件的值（批量)
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    @DeleteProvider(type = SqlBuilder.class, method = "deleteBatch")
    <T> int deleteBatch(Class<T> entity, String deleteField, String deleteVals);

    /**
     * @desc: 删除,根据实体某一字段值删除对象
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    @DeleteProvider(type = SqlBuilder.class, method = "deleteByField")
    <T> int deleteByField(T entity, String deleteField);

    /**
     * @desc: 更新所有字段
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    @UpdateProvider(type = SqlBuilder.class, method = "update")
    <T> int update(T entity);

    /**
     * @desc: 更新部分字段
     * @param: entity 实体对象
     * @return: int 受影响行数
     * @date: 2019-06-14
     */
    @UpdateProvider(type = SqlBuilder.class, method = "updateField")
    <T> int updateField(T entity);

    /**
     * @desc: 根据id查询数据
     * @param: id 数据id
     * @return: T 实体类型数据
     * @date: 2019-06-14
     */
    @SelectProvider(type = SqlBuilder.class, method = "queryById")
    @ResultType(value = Map.class)
    <T> Map<String, Object> queryById(T entity);

    /**
     * @desc: 根据实体某一字段查询数据，仅限唯一数据,若查询多条结果集则报错
     * @param: entity 实体对象
     * @param: field 查询字段
     * @return: map 集合
     * @date: 2019-10-02
     */
    @SelectProvider(type = SqlBuilder.class, method = "queryDataByField")
    @ResultType(value = Map.class)
    <T> Map<String, Object> queryDataByField(@Param("arg0") T entity, String queryField);
}





















