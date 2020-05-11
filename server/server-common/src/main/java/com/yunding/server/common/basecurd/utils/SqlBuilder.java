package com.yunding.server.common.basecurd.utils;

import org.apache.ibatis.jdbc.SQL;

/**
 * @desc: sql 构建器
 * @date: 2019-06-15
 */
public class SqlBuilder {

    private EntityUtil entityUtil;
    private String tableName;
    private String[] col;
    private Boolean isUpdateAll;

    /**
     * @desc: 无参构造
     * @date: 2019-06-22
     */
    public SqlBuilder(){
        entityUtil = new EntityUtil();
    }

    /**
     * @desc: 新增
     * @param: entity 实体对象
     * @return: String sql插入语句
     * @date: 2019-06-15
     */
    public <T> String insert(T entity){
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getInsertColumn(entity);
        SQL sql = new SQL(){
            {
                INSERT_INTO(tableName).VALUES(col[0], col[1]);
            }
        };
        return sql.toString();
    }

    /**
     * @desc: 更新(更新所有字段)
     * @param: entity 对象实体
     * @return: String sql更新语句
     * @date: 2019-06-22
     */
    public <T> String update(T entity){
        isUpdateAll = true;
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getUpdateColumn(entity, isUpdateAll);
        SQL sql = new SQL(){
            {
                UPDATE(tableName).SET(col[0]).WHERE(col[1]);
            }
        };
        return sql.toString();
    }

    /**
     * @desc: 更新(更新部分字段)
     * @param: entity 对象实体
     * @return: String sql更新语句
     * @date: 2019-06-22
     */
    public <T> String updateField(T entity){
        isUpdateAll = false;
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getUpdateColumn(entity, isUpdateAll);
        SQL sql = new SQL(){
            {
                UPDATE(tableName).SET(col[0]).WHERE(col[1]);
            }
        };
        return sql.toString();
    }

    /**
     * @desc: 根据实体id删除，id为String类型可批量
     * @param: entity 对象实体
     * @return: 
     * @date: 2019-06-23
     */
    public <T> String delete(T entity){
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getDeleteIds(entity); // 获取需要删除的id条件
        SQL sql = new SQL(){
            {
                DELETE_FROM(tableName).WHERE(col[0]);
            }
        };
        return sql.toString();
    }

    /**
     * @desc: 根据指定字段批量删除数据（指定字段指定值删除）
     * @param: entity 实体对象
     * @param: deleteField 条件字段
     * @param: deleteVals  条件值
     * @return: String 删除sql
     * @date: 2019-10-02
     */
    public <T> String deleteBatch(T entity, String deleteField, String deleteVals){
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getDeleteBatch(entity, deleteField, deleteVals); // 获取需要删除的id条件
        SQL sql = new SQL(){
            {
                DELETE_FROM(tableName).WHERE(col[0]);
            }
        };
        return sql.toString();
    }

    /**
     * @desc: 根据指定字段值删除数据（指定字段删除）
     * @param: entity 对象实体
     * @param: deleteField 条件字段
     * @return: sql
     * @date: 2019-06-23
     */
    public <T> String deleteByField(T entity,  String deleteField){
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getDeleteByField(entity, deleteField); // 获取需要删除的id条件
        SQL sql = new SQL(){
            {
                DELETE_FROM(tableName).WHERE(col[0]);
            }
        };
        return sql.toString();
    }

    /**
     * @desc: 根据id查询数据
     * @param: id 数据id
     * @return: String sql查询语句
     * @date: 2019-06-23
     */
    public <T> String queryById(T entity){
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getSelectByIdCol(entity); // 获取需要查询的字段
        SQL sql  = new SQL(){
            {
                SELECT(col[0]).FROM(tableName).WHERE(col[1]);
            }
        };
        return sql.toString();
    }

    /**
     * @desc: sql 语句： 根据实体某一字段查询数据，仅限唯一数据
     * @param: entity 实体对象
     * @param: field 查询字段
     * @return: 
     * @date: 2019-10-02
     */
    public <T> String queryDataByField(T entity, String queryField){
        tableName = entityUtil.getTableName(entity);
        col = entityUtil.getSelectByFieldCol(entity, queryField);
        SQL sql  = new SQL(){
            {
                SELECT(col[0]).FROM(tableName).WHERE(col[1]);
            }
        };
        return sql.toString();
    }
}
