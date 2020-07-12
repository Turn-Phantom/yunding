package com.li.cloud.common.basecurd.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.li.cloud.common.basecurd.dao.BaseDao;
import com.li.cloud.common.basecurd.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;

/**
 * @desc: 通用业务逻辑实现类
 * @date: 2019-06-14
 */
@Service
@Transactional
public class BaseServiceImpl implements BaseService {

    @Autowired
    private BaseDao baseDao;

     /* 新增 */
    @Override
    public <T> int insert(T entity) {
        return baseDao.insert(entity);
    }

    /* 新增 */
    @Override
    public <T> int insertNoId(T entity) {
        return baseDao.insertNoId(entity);
    }

    /* 删除，当id为String类型时可批量，否则删除单一数据 */
    @Override
    public <T> int delete(T entity) {
        return baseDao.delete(entity);
    }

    /* 根据指定字段的字段值删除数据 */
    @Override
    public <T> int deleteBatch(Class<T> entityClass, String deleteField, String deleteVals) {
        if(StringUtils.isEmpty(deleteField)){
            throw new RuntimeException("指定字段删除操作：指定字段不能为空！");
        }
        if(StringUtils.isEmpty(deleteVals)){
            throw new RuntimeException("指定字段删除操作：指定字段值不能为空！");
        }
        return baseDao.deleteBatch(entityClass, deleteField, deleteVals);
    }

    /* 根据指定字段值删除数据 */
    @Override
    public <T> int deleteByField(T entity, String deleteField) {
        if(StringUtils.isEmpty(deleteField)){
            throw new RuntimeException("指定字段删除操作：指定字段不能为空！");
        }
        return baseDao.deleteByField(entity, deleteField);
    }

    /*  更新 */
    @Override
    public <T> int update(T entity) {
        return baseDao.update(entity);
    }

    /* 根据id更新有值的字段 */
    @Override
    public <T> int updateField(T entity) {
        return baseDao.updateField(entity);
    }

    /* 根据id查询数据 */
    @Override
    public <T> T queryById(T entity) {
        return JSONObject.parseObject(JSONObject.toJSONString(baseDao.queryById(entity)), (Type) entity.getClass());
    }

    /* 根据指定字段值查询数据，仅限唯一结果数据 */
    @Override
    public <T> T queryDataByField(T entity, String queryField) {
        if(StringUtils.isEmpty(queryField)){
            throw new RuntimeException("指定字段值查询数据操作：指定字段不能为空！");
        }
        return JSONObject.parseObject(JSONObject.toJSONString(baseDao.queryDataByField(entity, queryField)), (Type) entity.getClass());
    }
}
