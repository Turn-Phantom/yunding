package com.yunding.server.tool.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @desc 手机号码信息
 * @date 2020-05-06
 */
@Table(name = "tb_sms_unsent")
@Alias("mobileNumInfo")
@Setter
@Getter
@Entity
@ToString
@Data
public class MobileNumInfo {
    // 主键
    @ExcelIgnore
    @Column(name = "id")
    private Integer id;

    // 手机号码
    @Column(name = "phone_num")
    private String phoneNum;

    // 姓名
    @ExcelIgnore
    @Column(name = "name")
    private String name;

    // 性别
    @ExcelIgnore
    @Column(name = "sex")
    private Byte sex;

    // 是否新导入手机号码； 1 是，0 否; 默认为是
    @ExcelIgnore
    @Column(name = "is_new")
    private Byte isNew = 1;

    // 是否已发送， 1 已发送， 0 未发送； 默认否
    @ExcelIgnore
    @Column(name = "is_send")
    private Byte isSend = 0;

    // 发送结果：1 成功； 0 失败；
    @ExcelIgnore
    @Column(name = "send_res")
    private Byte sendRes;

    @ExcelIgnore
    @Column(name = "import_date")
    private String importDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

}
