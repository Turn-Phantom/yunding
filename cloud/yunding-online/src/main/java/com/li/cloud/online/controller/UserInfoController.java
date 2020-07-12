package com.li.cloud.online.controller;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.NumberUtils;
import com.li.cloud.common.utils.PasswordUtil;
import com.li.cloud.common.utils.RegularUtils;
import com.li.cloud.online.entity.UserInfo;
import com.li.cloud.online.entity.YdVipInfo;
import com.li.cloud.online.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @desc 用户信息控制器
 * @date 2020-03-20
 */
@RestController
@RequestMapping(value = "/rest/user")
public class UserInfoController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private UserInfoService userInfoService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 分页查询用户信息 */
    @GetMapping("/queryUserList")
    public ReturnData queryUserList(Pagination<UserInfo> pagination){
        userInfoService.queryUserList(pagination);
        return ReturnData.successData(pagination);
    }

    /** 通过用户id查询用户信息 */
    @GetMapping("/queryUserInfoById")
    public ReturnData queryUserInfoById(String id){
        if(StringUtils.isBlank(id)){
            return ReturnData.error("参数不能为空");
        }
        return ReturnData.successData(userInfoService.queryUserInfoByAccount(id));
    }

    /**
     * @desc 根据手机号/会员号码查询用户信息
     * @param account 手机号/会员号
     * @param type 类型：0 账号， 1 手机
     * @param login 是否为登录成功查询； 1 是
     * @return ReturnData
     * @date 2020-03-22
     */
    @GetMapping("/getUserByAccount")
    public ReturnData getUserInfo(String account, Byte type, Byte login, HttpServletRequest request){
        if(StringUtils.isBlank(account) || null == type){
            return ReturnData.error("参数不能为空");
        }
        if(NumberUtils.isPhoneNun(account)){
            type = 1;
        }
        UserInfo userInfo = new UserInfo();
        if(type == 0){
            userInfo.setAccountNo(account);
            userInfo = baseService.queryDataByField(userInfo, "accountNo");
        } else if(type == 1){
            userInfo.setPhoneNo(account);
            userInfo = baseService.queryDataByField(userInfo, "phoneNo");
        } else{
            return ReturnData.error("无法识别的参数类型：" + type);
        }
        if(userInfo == null){
            return ReturnData.error("未查询到该用户信息");
        }
        // 保存登录记录
        if(null != login && login == 1){
            String reqIp = request.getHeader("reqIp");
            if(StringUtils.isNotBlank(reqIp)){
                userInfoService.saveLoginHis(userInfo.getId(),reqIp);
            }
        }
        return ReturnData.successData(userInfo);
    }

    /**
     * @desc 查询用户手机号码/账号是否被注册
     * @param account 账号/手机号
     * @param type 类型：0 账号，1 手机号
     * @return
     * @date 2020-04-01
     */
    @GetMapping("/isRegister")
    public ReturnData hasPhoneNum(String account, Byte type){
        UserInfo userInfo = new UserInfo();
        if(null == account || null == type){
            return ReturnData.error("参数不可为空！");
        }
        if(type == 0){
            userInfo.setAccountNo(account);
            userInfo = baseService.queryDataByField(userInfo, "accountNo");
        } else if(type == 1){
            userInfo.setPhoneNo(account);
            userInfo = baseService.queryDataByField(userInfo, "phoneNo");
        } else{
            return ReturnData.error("无法识别的参数类型：" + type);
        }
        if(userInfo == null){
            return ReturnData.error("");
        }
        return ReturnData.success("");

    }

    /** 根据手机号码查询账号 */
    @GetMapping("/queryAccountByPhone")
    public ReturnData queryAccountByPhone(String phoneNum){
        if(StringUtils.isBlank(phoneNum)){
            return ReturnData.error("手机号码不允许为空！");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setPhoneNo(phoneNum);
        userInfo = baseService.queryDataByField(userInfo, "phoneNo");
        if(null == userInfo || StringUtils.isEmpty(userInfo.getAccountNo())){
            return ReturnData.error("未查询到账户信息");
        }
        return ReturnData.success(userInfo.getAccountNo());
    }

    /**
     * @desc 用户注册
     * @param registerData 注册数据
     * @return
     * @date 2020-04-02
     */
    @PostMapping("/register")
    public ReturnData register(@RequestParam Map<String, String> registerData,  HttpServletRequest request){
        // 字段校验
        if(registerData == null){
            return ReturnData.error("请填写完整注册表单数据！");
        }
        String phoneNum = registerData.get("phoneNo");
        if(StringUtils.isEmpty(phoneNum)){
            return ReturnData.error("请输入手机号码");
        }
        if(phoneNum.length() != 11 || !NumberUtils.isPhoneNun(phoneNum)){
            return ReturnData.error("请输入11位正确的手机号码");
        }
        String validateCode = registerData.get("validateCode");
        if(StringUtils.isEmpty(validateCode) || validateCode.length() != 6){
            return ReturnData.error("请输入6位正确的验证码");
        }
        String accountNo = registerData.get("accountNo");
        if(StringUtils.isEmpty(accountNo) || accountNo.length() < 6 || accountNo.length() > 12 || !RegularUtils.hasNumAndLetter(accountNo)){
            return ReturnData.error("请输入6-12位包含数字和字母的账号");
        }
        String password = registerData.get("password");
        if(StringUtils.isEmpty(password) || password.length() < 6 || password.length() > 16){
            return ReturnData.error("请输入不少于6位，不超过16位的密码");
        }
        if(StringUtils.isEmpty(registerData.get("realName"))){
            return ReturnData.error("请填写真实姓名！");
        }
        if(StringUtils.isEmpty(registerData.get("qqNumber"))){
            return ReturnData.error("请填写qq号码！");
        }
        // 手机/账号校验
        UserInfo userInfo = new UserInfo();
        userInfo.setPhoneNo(phoneNum);
        userInfo = baseService.queryDataByField(userInfo, "phoneNo");
        if(null != userInfo){
            ReturnData.error("该手机号码已被注册！");
        }
        userInfo = new UserInfo();
        userInfo.setAccountNo(accountNo);
        userInfo = baseService.queryDataByField(userInfo, "accountNo");
        if(null != userInfo){
            ReturnData.error("该账号已被注册！");
        }
        // 注册
        String result = userInfoService.register(registerData, request);
        if(result.startsWith("warning:")){
            return ReturnData.warning(result.replace("warning:", ""));
        } else if(!result.equals(ReturnData.SUCCESS)){
            return ReturnData.error(result);
        }
        return ReturnData.success("注册成功");
    }

    /**
     * @desc 更新同步状态
     * @param status 状态
     * @param errMsg 同步错误消息
     * @param account 用户登录账号
     * @return
     * @date 2020-04-05
     */
    @GetMapping("/updateStatus")
    public ReturnData updateStatus(Byte status, String errMsg, String account){
        if(null == status || StringUtils.isEmpty(account)){
            return ReturnData.error("参数不能为空！");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setAccountNo(account);
        userInfo = baseService.queryDataByField(userInfo, "accountNo");
        if(userInfo == null){
            return ReturnData.error(String.format("用户：%s 未注册！", account));
        }
        YdVipInfo ydVipInfo = new YdVipInfo();
        ydVipInfo.setVipNo(userInfo.getId());
        ydVipInfo.setSyncYunding(status);
        ydVipInfo.setSyncErrMsg(errMsg);
        if(status == 1){
            ydVipInfo.setSyncPwd((byte) 1);
            ydVipInfo.setSyncPayPwd((byte) 1);
        } else{
            ydVipInfo.setSyncPwd((byte) -1);
            ydVipInfo.setSyncPayPwd((byte) -1);
        }
        int insert = baseService.insert(ydVipInfo);
        if(insert <= 0){
            return ReturnData.error("更新同步云顶状态失败！");
        }
        return ReturnData.success("更新同步云顶状态成功！");
    }

    /**
     * @desc 锁定/解锁账户
     * @param id 用户id
     * @param lockCause 锁定原因
     * @param type 请求类型：0 解锁； 1 锁定
     * @return
     * @date 2020-04-13
     */
    @PostMapping("/handlerAccount")
    public ReturnData handlerAccount(Integer id, String lockCause, Byte type){
        if(null == type){
            return ReturnData.error("操作类型不能为空！");
        }
        String opt;
        opt = type == 0 ? "解锁" : "锁定";
        if(null == id){
            return ReturnData.error(opt + "失败：用户id不能为空！");
        }
        if(type == 1 && StringUtils.isBlank(lockCause)){
            return ReturnData.error("锁定失败：锁定原因不能为空！");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        lockCause = type == 0 ? "无" : lockCause;
        userInfo.setLockCause(lockCause);
        userInfo.setAccountStatus(type);
        int updateNum = baseService.updateField(userInfo);
        if(updateNum <= 0){
            return ReturnData.error(opt + "失败！");
        }
        return ReturnData.success(opt + "成功！");
    }

    /** 获取最近15天的登录记录 */
    @GetMapping("/getLoginHistory")
    public ReturnData getLoginHistory(Integer userId){
        if(null == userId){
            return ReturnData.error("查询登录历史记录失败：参数不能为空");
        }
        return ReturnData.successData(userInfoService.getLoginHistory(userId));
    }

    /**
     * @desc 修改用户信息
     * @param userInfo 用户信息实体
     * @param field 修改的字段
     * @date 2020-04-15
     */
    @PostMapping("/updateUserInfo")
    public ReturnData updateUserInfo(UserInfo userInfo, String field, HttpServletRequest request){
        String ret = userInfoService.updateUserInfo(userInfo, field, request);
        if(!ReturnData.SUCCESS.equals(ret)){
            return ReturnData.error(ret);
        }
        // 若修改的为用户手机号码， 则查询用户密码返回，用作重新自动登录
        if("phoneNo".equals(field)){
            String password = baseService.queryById(userInfo).getPwd();
            try {
                return ReturnData.success(PasswordUtil.decode(password));
            } catch (Exception e) {
                return ReturnData.warning("解密登录密码异常");
            }
        }
        return ReturnData.success("操作成功");
    }

    /** 校验验证码是否正确 */
    @PostMapping("/resetPasswordCodeIsCorrect")
    public ReturnData resetPasswordCodeIsCorrect(String code, HttpServletRequest request){
        if(StringUtils.isBlank(code)){
            return ReturnData.error("验证码不能为空！");
        }
        String validRes = userInfoService.smsCodeValidate(code, new ServletWebRequest(request));
        if(!validRes.equals(ReturnData.SUCCESS)){
            return ReturnData.error(validRes);
        }
        return ReturnData.success("验证成功");
    }

    /** 通过手机验证码找回密码 */
    @PostMapping("/resetPasswordForPhone")
    public ReturnData resetPasswordForPhone(String pwd, String confirmPwd, String phoneNo){
        if(StringUtils.isBlank(pwd) || StringUtils.isBlank(confirmPwd)){
            return ReturnData.error("密码不能为空");
        }
        if(StringUtils.isBlank(phoneNo)){
            return ReturnData.error("手机号码不能为空");
        }
        if(!StringUtils.equals(pwd, confirmPwd)){
            return ReturnData.error("两次密码输入不一致");
        }
        // 根据手机号更新用户密码
        String password = null;
        try {
            password = PasswordUtil.encode(pwd);
        } catch (Exception e) {
            logger.error("重置密码失败: ", e);
            return ReturnData.error("重置密码失败，请联系客服");
        }
        int updateNum = userInfoService.updateUserInfoByPhoneNo(password, phoneNo);
        if(updateNum <= 0){
            return ReturnData.error("重置密码失败，请联系客服");
        }
        return ReturnData.success("重置成功！");
    }

    /** 根据用户id，查询用户收益 */
    @GetMapping("/queryUserIncomeBalance")
    public ReturnData queryUserIncomeBalance(Integer userId){
        if(null == userId){
            return ReturnData.error("用户id不能为空");
        }
        BigDecimal money = userInfoService.queryUserIncomeBalance(userId);
        return ReturnData.successData(money);
    }
}

