package com.li.cloud.tool.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * @desc
 * @date 2020-06-08
 */
@Alias("customerExportData")
@Setter
@Getter
@Data
public class CustomerExportData {
    // 排列顺序：尊称，"姓名", "手机号码", "邮箱", "会员卡ID", "会员卡类型", "身份编号", "生日", "国籍", "省份", "国家"

    // 尊称
    @ColumnWidth(8)
    @ExcelProperty(value = "尊称")
    private String honorificTitle;

    // 姓名
    @ColumnWidth(20)
    @ExcelProperty(value = "姓名")
    private String name;

    // 手机号码
    @ColumnWidth(17)
    @ExcelProperty(value = "手机号码")
    private String phone;

    // 邮箱
    @ColumnWidth(20)
    @ExcelProperty(value = "邮箱")
    private String email;

    // 会员卡ID
    @ColumnWidth(13)
    @ExcelProperty(value = "会员卡ID")
    private String vipId;

    // 会员卡类型
    @ColumnWidth(13)
    @ExcelProperty(value = "会员类型")
    private String vipType;

    // 身份编号
    @ColumnWidth(24)
    @ExcelProperty(value = "护照(身份)编号")
    private String passportId;

    // 生日
    @ColumnWidth(13)
    @ExcelProperty(value = "生日")
    private String birthday;

    // 国籍
    @ColumnWidth(10)
    @ExcelProperty(value = "国籍")
    private String nationality;

    // 省份
    @ColumnWidth(15)
    @ExcelProperty(value = "省份")
    private String province;

    // 国家
    @ColumnWidth(10)
    @ExcelProperty(value = "国家")
    private String country;

    @ColumnWidth(10)
    @ExcelProperty(value = "状态")
    private String callRes;

    @ColumnWidth(10)
    @ExcelProperty(value = "电销员")
    private String manager;
}
