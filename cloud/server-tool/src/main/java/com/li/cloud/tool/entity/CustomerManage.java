package com.li.cloud.tool.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.li.cloud.common.annotations.ExcludeField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @desc 客户资源管理实体
 * @date 2020-06-01
 */
@Table(name = "tb_customer_manage")
@Alias("customerManage")
@Setter
@Getter
@Entity
@ToString
@Data
public class CustomerManage {

    // 主键id
    @Id
    @Column(name = "id")
    @ExcelIgnore
    private Integer id;

    // 姓名
    @Column(name = "name")
    private String name;

    // 手机号码
    @Column(name = "phone")
     private String phone;

    // 区域代码（手机区号）
    @Column(name = "area_num")
    @ExcelIgnore
    private Byte areaNum;

    // 邮箱
    @Column(name = "email")
    private String email;

    // 号码类型：1 新资源， 2 旧资源
    @Column(name = "number_type")
    private Byte numberType;

    // 状态; -1 未知,0,空号 1,实号 2,停机 3,库无 4,沉默号 5, 风险号
    @Column(name = "status")
    @ExcelIgnore
    private Byte status = -1;

    // 拨打记录
    @Column(name = "call_res")
    @ExcelIgnore
    private Byte callRes;

    // 是否充值
    @Column(name = "is_pay")
    @ExcelIgnore
    private Byte isPay;

    // 负责人
    @Column(name = "manager")
    private Integer manager;

    // 号码保存日期
    @Column(name = "create_date")
    @ExcelIgnore
    @ExcludeField
    private String createDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    // 备注
    @Column(name = "remark")
    private String remark;

    // 云顶 会员信息更新时间； 对应： Modified On
    @Column(name = "modify_time")
    private String modifyTime;

    // 云顶 会员卡id,对应：Member ID
    @Column(name = "vip_id")
    private String vipId;

    // 云顶 会员卡类型；对应：Member Type
    @Column(name = "vip_type")
    private String vipType;

    // 云顶 护照ID； 对应：Identity
    @Column(name = "passport_id")
    private String passportId;

    // 云顶 尊称：MR 先生、MS 小姐、MDM 女士； 对应：Title
    @Column(name = "honorific_title")
    private String honorificTitle;

    // 云顶 出生年月日； 对应： Birth Date
    @Column(name = "birthday")
    private String birthday;

    // 云顶 国籍；对应： Nationality
    @Column(name = "nationality")
    private String nationality;

    // 云顶 省份； 对应： State
    @Column(name = "province")
    private String province;

    // 云顶 国家； 对应： Country
    @Column(name = "country")
    private String country;
}
