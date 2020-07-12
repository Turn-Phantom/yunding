package com.li.cloud.common.utils.enumutils;

import lombok.Getter;

/**
 * @desc: Java特殊数据类型枚举
 * @date: 2019-06-18
 */
@Getter
public enum SpecialDataType {
    Byte("java.lang.Byte"),
    Integer("java.lang.Integer"),
    Long("java.lang.Long"),
    Character("java.lang.Character"),
    Float("java.lang.Float"),
    Double("java.lang.Double"),
    String("java.lang.String"),
    Boolean("java.lang.Boolean"),
    Object("java.lang.Object"),
    Date("java.util.Date"),
    LocalDate("java.time.LocalDate"),
    LocalDateTime("java.time.LocalDateTime");

    private String typeVal;

    SpecialDataType(String type) {
        this.typeVal = type;
    }
}
