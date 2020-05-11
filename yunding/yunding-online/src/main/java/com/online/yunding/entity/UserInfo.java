package com.online.yunding.entity;

import cn.hutool.core.clone.CloneSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @desc 用户信息
 * @date 2020-03-31
 */
@Table(name = "tb_user_info")
@Alias("userInfo")
@Setter
@Getter
@Entity
@ToString
public class UserInfo extends CloneSupport<UserInfo> {
    // 会员id，默认最低4位，当位数不足4位时，默认前面补0
    @Id
    @Column(name = "id")
    private Integer id;

    // 手机号码
    @Column(name = "phoneNo")
    private String phoneNo;

    // 账号
    @Column(name = "accountNo")
    private String accountNo;

    // (会员)登录密码
    @Column(name = "pwd")
    private String pwd;

    // 账号状态; 0 正常，1 锁定；
    @Column(name = "accountStatus")
    private Byte accountStatus;

    // 锁定原因
    @Column(name = "lock_cause")
    private String lockCause;

    // 真实姓名
    @Column(name = "realName")
    private String realName;

    // 昵称
    @Column(name = "nickName")
    private String nickName;

    // qq号码
    @Column(name = "qqNo")
    private String qqNo;

    // 身份证号码
    @Column(name = "identityNo")
    private String identityNo;

    // 性别，0女，1男
    @Column(name = "sex")
    private Byte sex;

    // 推荐人会员号
    @Column(name = "parentVipNo")
    private Integer parentVipNo;

    // 银行卡号1
    @Column(name = "bankCardNo1")
    private String bankCardNo1;

    // 银行卡号2
    @Column(name = "bankCardNo2")
    private String bankCardNo2;

    // 银行卡号3
    @Column(name = "bankCardNo3")
    private String bankCardNo3;

    // 取款密码
    @Column(name = "payPwd")
    private String payPwd;

    // 注册时间
    @Column(name = "createTime")
    private Long createTime;

    // 账号说明/备注
    @Column(name = "account_remark")
    private String accountRemark;

    // 提款次数
    @Column(name = "extract_balance")
    private Integer extractBalance;

    // 同步云顶：账号; 同步云顶娱乐平台状态：默认为成功；1成功，0失败
    private Integer syncYd;
    // 同步云顶：密码; 同步云顶密码； -1 未同步
    private Integer syncPwd;
    // 同步云顶：取款密码； 同步云顶取款密码， -1 未同步； 1已同步
    private Integer syncPayPwd;

    // 最近一次登录ip
    private String loginIp;
    // 最近一次登录时间
    private Long loginTime;
    // ip地址国家/城市
    private String addrStr;
    // 手机验证码
    private String validateCode;

    // 账户余额（充值金额）
    private BigDecimal balance;
    // 冻结金额
    private BigDecimal freezeMoney;
    // 在线收益金额
    private BigDecimal onlineMoney;
    // 推荐成功金额
    private BigDecimal recommendMoney;

    // 在线时长
    private Long onlineTime;
    // 手机号码修改次数
    private int phoneCount;
    // 密码修改次数
    private int pwdCount;
    // 取款密码修改次数
    private int payPwdCount;
    // 旧密码
    private String oldPwd;
    // 确认密码
    private String confirmPwd;
    /** 获取会员号 */
    public String getVipNoStr(){
        int len = String.valueOf(id).length();
        StringBuilder newVipNo = new StringBuilder();
        if(len < 4){
            for (int i = 0; i < (4 - len); i++) {
                newVipNo.append("0");
            }
        }
        return newVipNo.append(id).toString();
    }

}
