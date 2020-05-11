package com.yunding.server.common.basecurd.utils;

import com.yunding.server.common.annotations.ExcludeField;
import com.yunding.server.common.basecurd.config.ConfigParam;
import com.yunding.server.common.utils.EmptyUtil;
import com.yunding.server.common.utils.MathZeroUtil;
import com.yunding.server.common.utils.StringUtil;
import com.yunding.server.common.utils.enumutils.SpecialDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;

/**
 * @desc: 通用实体操作工具类
 * @date: 2019-06-15
 */
public class EntityUtil {

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityUtil.class);

    private static ConfigParam configParam = new ConfigParam();

    /**
     * @desc: 获取table注解name属性
     * @param: entity 实体对象
     * @return: String 注解类属性值value
     * @date: 2019-06-16
     */
   public <T> String getTableName(T entity){
        Table tabAnno = entity.getClass().getAnnotation(Table.class);
        if(EmptyUtil.isEmpty(tabAnno) || EmptyUtil.isEmpty(tabAnno.name())){
            throw new RuntimeException("获取数据库表名失败；若未对实体指定表名注解，需添加@Table注解，" +
                    "若未指定表名，则需要指定数据库对应的表名；例如：@Table(name = \"table_name\")");
        }
        return tabAnno.name();
    }

    /**
     * @desc: 获取@Column注解的属性及属性值
     * @param: entity 实体对象
     * @return: String[] 对象属性key和value
     * @date: 2019-06-17
     */
     <T> String[] getInsertColumn(T entity) {
        String []val = new String[2];
        String databaseType = configParam.getDatabaseType();
        // 获取所有字段
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuffer col = new StringBuffer();
        StringBuffer colVal = new StringBuffer();
        try {
            for (Field field : fields) {
                Column colAnno = field.getAnnotation(Column.class);
                // 若没有@Column注解，则不处理字段
                if(EmptyUtil.isEmpty(colAnno)){
                    continue;
                }
                boolean hasGenerater = false;
                String generator = "";
                field.setAccessible(true);
                Id idAnno = field.getAnnotation(Id.class);
                // 若存在@Id注解
                if(!EmptyUtil.isEmpty(idAnno)){
                    // 判断数据库类型; 默认为mysql类型；
                    if(EmptyUtil.isEmpty(databaseType) || "MYSQL".equals(databaseType.toUpperCase())){
                        // 判断字段值是否为空（数值类型为0，默认为空处理）, 为空使用自增长id
                        boolean isNull = fieldValIsNull(entity, field, false);
                        if(isNull){
                            continue;
                        }
                    } else if ("ORACLE".equals(databaseType.toUpperCase())){ // oracle数据库
                        GeneratedValue sqAnno = field.getAnnotation(GeneratedValue.class);
                        if(!EmptyUtil.isEmpty(sqAnno) && !EmptyUtil.isEmpty(sqAnno.generator())){
                            generator = sqAnno.generator();
                            hasGenerater = true;
                        }
                    }
                }

                col.append(colAnno.name());
                String fieldName = field.getName();
                // 获取jdbcType
                String jdbcType = getJdbcType(field);
                if(!hasGenerater){
                    if(EmptyUtil.isEmpty(jdbcType)){
                        colVal.append("#{").append(fieldName).append("}");
                    } else {
                        colVal.append("#{").append(fieldName).append(", jdbcType=").append(jdbcType).append("}");
                    }
                } else{
                    colVal.append(generator).append(".nextval");
                }
                col.append(",");
                colVal.append(",");
            }
            String columnKey = StringUtil.delLastChar(col).toString();
            String columnValue = StringUtil.delLastChar(colVal).toString();
            if(EmptyUtil.isEmpty(columnKey.replaceAll("\"", "")) || EmptyUtil.isEmpty(columnValue.replaceAll("\"", ""))){
                LOGGER.error("未添加@Column注解或成员属性不存在！");
                return null;
            }
            val[0] = columnKey;
            val[1] = columnValue;
        } catch (Exception e) {
            LOGGER.error("获取实体字段属性值异常：", e);
            throw new RuntimeException();
        }
        return val;
    }

    /**
     * @desc: 获取jdbcType
     * @param: field 成员属性对象
     * @return: String 数据库存储类型
     * @date: 2019-06-21
     */
    private String getJdbcType(Field field) {
        String jdbcType = "";
        String fieldType = getFieldObjectType(field);
        if(fieldType.equals(SpecialDataType.Integer.getTypeVal())){ // 整型
            jdbcType = "INTEGER";
        } else if(fieldType.equals(SpecialDataType.Date.getTypeVal())
                || fieldType.equals(SpecialDataType.LocalDate.getTypeVal())
                || fieldType.equals(SpecialDataType.LocalDateTime.getTypeVal())){ // 日期型
            DateTimeFormat dateAnno = field.getAnnotation(DateTimeFormat.class);
            if(!EmptyUtil.isEmpty(dateAnno)){ // 注解非空判断
                String pattern = dateAnno.pattern();
                if(!EmptyUtil.isEmpty(pattern) && "TIMESTAMP".equals(pattern.toUpperCase())){
                    jdbcType = "TIMESTAMP";
                } else{
                    jdbcType = "DATE";
                }
            }
        } else if(fieldType.equals(SpecialDataType.String.getTypeVal())) { // 字符串
            jdbcType = "VARCHAR";
        } else {
            return null;
        }
        return jdbcType;
    }

    /**
     * @desc: 根据字段类型，并返回对应的类型字符串
     * @param: field 类成员属性
     * @return: String 对象类型
     * @date: 2019-06-18
     */
    private String getFieldObjectType(Field field){
        String fieldType = field.getType().toString();
        for (SpecialDataType javaType : SpecialDataType.values()) {
            if(fieldType.endsWith(javaType.getTypeVal())){
                return javaType.getTypeVal();
            }
        }
        return SpecialDataType.Object.getTypeVal();
    }

    /**
     * @desc: 获取成员属性值(数字类型不能为0)
     * @param: entity 对象实体
     * @param: field 成员属性
     * @param: isIncludeNum 是否对数值类型进行判空
     * @return: boolean 是否为空
     * @date: 2019-06-22
     */
    private <T> boolean fieldValIsNull(T entity, Field field, boolean isIncludeNum){
        Object fieldVal = null;
        try {
            fieldVal = field.get(entity);
        } catch (IllegalAccessException e) {
            LOGGER.error("获取成员属性值异常:", e);
        }
        if(EmptyUtil.isEmpty(fieldVal)){
            return true;
        }
        // 是否对数字类型进行判空
        if(!isIncludeNum){
            return false;
        }
        String fieldType = field.getType().toString();
        switch (fieldType){
            case "byte":
                if((byte)fieldVal == 0){
                    return true;
                }
                break;
            case "int":
                if((int)fieldVal == 0){
                    return true;
                }
                break;
            case "float":
                if(MathZeroUtil.isZero((float)fieldVal)){
                    return true;
                }
                break;
            case "double":
                if(MathZeroUtil.isZero((double)fieldVal)){
                    return true;
                }
                break;
            case "char":
                if(EmptyUtil.isEmpty((char)fieldVal)){
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    /**
     * @desc: 判断成员属性是否为数值类型
     * @param: field 成员属性
     * @return: boolean
     * @date: 2019-06-23
     */
    private boolean isNumberType(Field field){
        String fieldType = field.getType().toString();
        switch (fieldType){
            case "byte":
                return true;

            case "int":
                return true;

            case "float":
                return true;

            case "double":
                return true;

            case "long":
                return true;

            default:
                return false;
        }
    }

    /**
     * @desc: 判断成员属性是否为数值对象
     * @param: field 成员属性
     * @return: boolean
     * @date: 2019-06-23
     */
    private boolean isNumberObject(Field field){
        // 获取对象字段类型
        String fieldType = getFieldObjectType(field);
        switch (fieldType){
            case "java.lang.Byte":
                return true;

            case "java.lang.Integer":
                return true;

            case "java.lang.Float":
                return true;

            case "java.lang.Double":
                return true;

            case "java.lang.Long":
                return true;

            default:
                return false;
        }
    }

    /**
     * @desc: 判断成员属性是否为字符串类型
     * @param: field 成员属性
     * @return: boolean
     * @date: 2019-06-23
     */
    private boolean isStringType(Field field){
        String fieldType = getFieldObjectType(field);
        return fieldType.equals(SpecialDataType.String.getTypeVal());
    }

    /**
     * @desc: 获取更新的设置列
     * @param: entity 对象实体
     * @param: isUpdateAll 更新标识，是否更新所有； true：更新所有；false：更新有值的部分
     * @return: String[]
     * @date: 2019-06-22
     */
     <T> String[] getUpdateColumn(T entity, Boolean isUpdateAll) {
         boolean hasId = false;
         String []val = new String[2];
         Field[] fields = entity.getClass().getDeclaredFields();
         StringBuffer sbField = new StringBuffer();
         StringBuffer sbCondition = new StringBuffer();
         for (Field field : fields) {
             field.setAccessible(true);
             // 获取排除处理字段,并不处理该字段
             ExcludeField excludeField = field.getAnnotation(ExcludeField.class);
             if(!EmptyUtil.isEmpty(excludeField)){
                 continue;
             }
             Column colAnno = field.getAnnotation(Column.class);
             if(EmptyUtil.isEmpty(colAnno)){
                 continue;
             }
             String fieldName = field.getName(); // 成员名称
             String colAnnoName = colAnno.name(); // @Column注解name属性值
             Id idAnno = field.getAnnotation(Id.class);
             if(!EmptyUtil.isEmpty(idAnno)){
                 if(hasId){
                     throw new RuntimeException("更新失败：成员属性中，只允许一个@Id注解，请检查该实体是否存在多个");
                 }
                 boolean fieldIsNull = fieldValIsNull(entity, field, true);
                 if(fieldIsNull){
                     throw new RuntimeException("更新失败：@Id下的属性值不允许为空或数值类型不允许为0");
                 }
                 sbCondition.append(colAnnoName).append(" = #{").append(fieldName).append("}");
                 hasId = true;
                 continue;
             } else{
                 if(isUpdateAll){ // 更新所有字段
                     String jdbcType = getJdbcType(field);
                     if(EmptyUtil.isEmpty(jdbcType)){
                         sbField.append(colAnnoName).append(" = #{").append(fieldName).append("}");
                     } else{
                         sbField.append(colAnnoName).append(" = #{").append(fieldName).append(", jdbcType=").append(jdbcType).append("}");
                     }
                 } else{
                    if(fieldValIsNull(entity,field,false)){ // 成员属性值判空
                        continue;
                    }
                    sbField.append(colAnnoName).append(" = #{").append(fieldName).append("}");
                 }
             }
             sbField.append(",");
         }
         if(!hasId){
             throw new RuntimeException("更新失败：未检测到@Id注解，请检查对应数据Id字段是否存在@Id");
         }
         String setField = StringUtil.delLastChar(sbField).toString();
         // 更新值空处理
         if(EmptyUtil.isEmpty(setField.replaceAll("\"", ""))){
             throw new RuntimeException("更新失败：未获取到更新字段，请检查更新字段是否存在@Column属性或值为空");
         }
         val[0] = setField;
         val[1] = sbCondition.toString();
         return val;
     }

     /**
      * @desc: 根据id查询数据sqlqueryById
      * @param: entity 对象实体
      * @return: String[]
      * @date: 2019-06-23
      */
     <T> String[] getSelectByIdCol(T entity) {
         String []val = new String[2];
         Field[] fields = entity.getClass().getDeclaredFields();
         StringBuffer sbField = new StringBuffer();
         StringBuffer sbCondition = new StringBuffer();
         // 遍历对象的所有字段
         for (Field field : fields) {
             field.setAccessible(true);
             // 排除未添加@Column注解的字段
             Column colAnno = field.getAnnotation(Column.class);
             if(EmptyUtil.isEmpty(colAnno)){
                 continue;
             }
             String colName = colAnno.name();
             String fieldName = field.getName();
             // 识别是否存在@Id注解，查找主键标识
             Id idAnno = field.getAnnotation(Id.class);
             if(!EmptyUtil.isEmpty(idAnno)){
                 // 对主键值非空处理
                 boolean fieldIsNull = fieldValIsNull(entity, field, true);
                 if(fieldIsNull){
                     throw new RuntimeException("根据id查询数据失败：@Id下的属性值不允许为空或数值类型不允许为0");
                 }
                 sbCondition.append(fieldName).append(" = #{id}");
             }
             sbField.append(colName).append(" as ").append(fieldName);
             sbField.append(",");
         }
         String columnVal = StringUtil.delLastChar(sbField).toString();
         String condStr = sbCondition.toString();
         // 对sql字段片段和条件片段进行非空处理
         if(EmptyUtil.isEmpty(columnVal.replaceAll("\"",""))){
             throw new RuntimeException("根据id查询数据失败：查询字段未检测到@Column注解");
         }
         if(EmptyUtil.isEmpty(condStr.replaceAll("\"", ""))){
             throw new RuntimeException("根据id查询数据失败：请检查实体对象是否存在@Id注解表示更新数据的唯一标识");
         }
         // 赋值并返回
         val[0] = columnVal;
         val[1] = condStr;
         return val;
     }

     /**
      * @desc: 获取需要删除的id条件（根据实体id删除）
      * @param: entity 对象实体
      * @return: String[]
      * @date: 2019-06-23
      */
    public <T> String[] getDeleteIds(T entity) {
        String []val = new String[2];
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder idCondition = new StringBuilder();
        boolean hasId = false;
        // 遍历所有字段
        for (Field field : fields) {
            field.setAccessible(true);
            Column colAnno = field.getAnnotation(Column.class);
            // 查找含有@Id注解的字段
            Id idAnno = field.getAnnotation(Id.class);
            if(EmptyUtil.isEmpty(idAnno) || EmptyUtil.isEmpty(colAnno)){
                continue;
            }
            if(hasId){
                throw new RuntimeException("删除失败：检测到实体对象存在多个@Id注解,一个实体只允许存在一个@Id注解");
            }
            String colAnnoName = colAnno.name();
            String fieldName = field.getName();
            // 对主键值作非空处理，数值不允许为0
            boolean isNullVal = fieldValIsNull(entity, field, true);
            if(isNullVal){
                throw new RuntimeException("删除失败：含有@Id注解的成员属性值不能为空或数值不能0");
            }
            // 获取成员属性值
            Object fieldVal = null;
            try {
                fieldVal = field.get(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("删除失败：获取"+ fieldName +"属性值异常：", e);
            }
            if(EmptyUtil.isEmpty(fieldVal)){
                throw new RuntimeException("删除失败：获取"+ fieldName +"属性值失败：");
            }
            // 判断成员属性是否为数值类型,若为数值类型
            if(isStringType(field)){
                String[] splitVal = ((String) fieldVal).split(",");
                idCondition.append(colAnnoName).append(" in ").append("(");
                for (String valStr : splitVal) {
                    idCondition.append("'").append(valStr).append("'").append(",");
                }
                idCondition = StringUtil.delLastChar(idCondition);
                idCondition.append(")");
            } else{
                idCondition.append(colAnnoName).append(" = ").append("'").append(fieldVal).append("'");
            }
            hasId = true;
        }
        val[0] = idCondition.toString();
        return val;
    }

    /**
     * @desc: 根据指定字段批量删除数据（指定字段指定值删除）
     * @param: entity 实体对象
     * @param: deleteField 条件字段
     * @param: deleteVals  条件值
     * @return:
     * @date: 2019-10-02
     */
    public <T> String[] getDeleteBatch(T entity, String deleteField, String deleteVals) {
        String []val = new String[2];
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder deleteCondition = new StringBuilder();
        // 遍历所有字段
        for (Field field : fields) {
            field.setAccessible(true);
            // 获取@Column注解
            Column colAnno = field.getAnnotation(Column.class);
            if(EmptyUtil.isEmpty(colAnno)){
                continue;
            }
            String colAnnoName = colAnno.name();
            String fieldName = field.getName();
            // 跳过非自定字段
            if(!deleteField.equals(fieldName)){
                continue;
            }
            // 遍历删除条件值
            String[] splitVal = deleteVals.split(",");
            deleteCondition.append(colAnnoName).append(" in ").append("(");
            for (String aSplitVal : splitVal) {
                deleteCondition.append("'").append(aSplitVal).append("'").append(",");
            }
            deleteCondition = StringUtil.delLastChar(deleteCondition);
            deleteCondition.append(")");
        }
        val[0] = deleteCondition.toString();
        return val;
    }

    /**
     * @desc: 根据指定字段删除数据（指定字段删除）
     * @param: entity 实体对象
     * @param: deleteField 指定字段
     * @return: String[] 条件sql
     * @date: 2019-10-02
     */
    public <T> String[] getDeleteByField(T entity, String deleteField) {
        String []val = new String[2];
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder sbCondition = new StringBuilder();
        // 遍历所有字段
        for (Field field : fields) {
            field.setAccessible(true);
            Column colAnno = field.getAnnotation(Column.class);
            if(EmptyUtil.isEmpty(colAnno)){
                continue;
            }
            String colAnnoName = colAnno.name();
            String fieldName = field.getName();
            // 跳过非指定字段
            if(!fieldName.equals(deleteField)){
                continue;
            }
            // 获取成员属性值
            Object fieldVal = null;
            try {
                fieldVal = field.get(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("删除失败：获取"+ fieldName +"属性值异常：", e);
            }
            if(EmptyUtil.isEmpty(fieldVal)){
                throw new RuntimeException("删除失败：获取"+ fieldName +"属性值失败：");
            }
            // 判断成员属性是否为数值类型,若为数值类型
            if(isStringType(field)){
                String[] splitVal = ((String) fieldVal).split(",");
                sbCondition.append(colAnnoName).append(" in ").append("(");
                for (String valStr : splitVal) {
                    sbCondition.append("'").append(valStr).append("'").append(",");
                }
                sbCondition = StringUtil.delLastChar(sbCondition);
                sbCondition.append(")");
            }else{
                sbCondition.append(colAnnoName).append(" = ").append("'").append(fieldVal).append("'");
            }
        }
        if(sbCondition.length() <= 0){
            throw new RuntimeException("删除失败： 传入指定字段无法与实体字段匹配：");
        }
        val[0] = sbCondition.toString();
        return val;
    }

    /**
     * @desc: 获取id字段的字符串拼接
     * @param: fieldVal id字符串（逗号形式分割）
     * @return: 字符串
     * @date: 2019-06-23
     */
    private String getIdsStr(String fieldVal) {
        String[] ids = fieldVal.split(",");
        StringBuffer sb = new StringBuffer();
        for (String id : ids) {
            sb.append("'").append(id).append("'");
            sb.append(",");
        }
        return StringUtil.delLastChar(sb).toString();
    }

    /**
     * @desc: 根据字段查询数据sql字符串拼接
     * @param: entity 实体对象
     * @param: queryField 查询字段
     * @return: String[]
     * @date: 2019-10-02
     */
    public <T> String[] getSelectByFieldCol(T entity, String queryField) {
        String []val = new String[2];
        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuffer sbField = new StringBuffer();
        StringBuilder sbCondition = new StringBuilder();
        // 遍历对象的所有字段
        for (Field field : fields) {
            field.setAccessible(true);
            // 排除未添加@Column注解的字段
            Column colAnno = field.getAnnotation(Column.class);
            if(EmptyUtil.isEmpty(colAnno)){
                continue;
            }
            String colName = colAnno.name();
            String fieldName = field.getName();
            // 判断传入的字段是否匹配对象中的字段
            if(queryField.equals(fieldName)){
                sbCondition.append(queryField).append("=#{").append("arg0.").append(queryField).append("}");
            }
            sbField.append(colName).append(" as ").append(fieldName);
            sbField.append(",");
        }
        String columnVal = StringUtil.delLastChar(sbField).toString();
        if(sbCondition.length() <= 0){
            throw new RuntimeException("根据字段查询数据失败：实体字段中无匹配参数字段");
        }
        // 赋值并返回
        val[0] = columnVal;
        val[1] = sbCondition.toString();
        return val;
    }


}
