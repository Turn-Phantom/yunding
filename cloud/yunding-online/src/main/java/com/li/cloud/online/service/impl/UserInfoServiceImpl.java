package com.li.cloud.online.service.impl;

import com.li.cloud.common.annotations.CustomPage;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.HttpClientUtils;
import com.li.cloud.common.utils.NumberUtils;
import com.li.cloud.common.utils.PasswordUtil;
import com.li.cloud.online.config.params.ServiceIdConfig;
import com.li.cloud.online.dao.UserInfoDao;
import com.li.cloud.online.entity.*;
import com.li.cloud.online.pojo.ValidateCodeType;
import com.li.cloud.online.service.UserInfoService;
import com.li.cloud.online.service.VideoDataService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc 用户信息 实现
 * @date 2020-03-31
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private LoginHistory orgLoginHistory = new LoginHistory();

    private UserInfo orgUserInfo = new UserInfo();

    @Autowired
    private BaseService baseService;

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private ServiceIdConfig serviceIdConfig;

    // 注入RedisTemplate工具
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public UserInfoServiceImpl(){}
    public UserInfoServiceImpl(RestTemplate restTemplateForPar){
        if(null == restTemplate){
            this.restTemplate = restTemplateForPar;
        }
    }

    /** 分页查询用户集合 */
    @Override
    @CustomPage // 作用：before：设置分页条件，afterReturn：设置分页数据
    public List<UserInfo> queryUserList(Pagination<UserInfo> pagination) {
        return userInfoDao.queryUserList(pagination);
    }

    /** 用户注册 */
    @Override
    public String register(Map<String, String> registerData, HttpServletRequest request) {
        ServletWebRequest webRequest = new ServletWebRequest(request);
        // 存储注册验证码的key
        Object redisForSmsKey = buildKey(webRequest, ValidateCodeType.SMS);
        if(StringUtils.startsWith(redisForSmsKey.toString(), "error")){
            return "注册失败：" + redisForSmsKey;
        }
        // 校验短信验证码是否正确
        String validateCode = registerData.get("validateCode");
        String validRet = smsCodeValidate(validateCode, webRequest);
        if(!ReturnData.SUCCESS.equals(validRet)){
            return validRet;
        }
        // 当前服务保存注册信息
        UserInfo userInfo = new UserInfo();
        userInfo.setPhoneNo(registerData.get("phoneNo"));
        userInfo.setAccountNo(registerData.get("accountNo"));
        try {
            userInfo.setPwd(PasswordUtil.encode(registerData.get("password")));
        } catch (Exception e) {
            logger.error("加密登录密码失败，请联系管理员！", e);
            return "加密登录密码失败，请联系管理员！";
        }
        userInfo.setRealName(registerData.get("realName"));
        userInfo.setQqNo(registerData.get("qqNumber"));
        String regUserParent = registerData.get("parentVipNo");
        if(!StringUtils.isEmpty(regUserParent)){
            userInfo.setParentVipNo(Integer.valueOf(regUserParent));
        }
        try {
            userInfo.setPayPwd(PasswordUtil.encode(registerData.get("payPwd")));
        } catch (Exception e) {
            logger.error("加密取款密码失败，请联系管理员！", e);
            return "加密取款密码失败，请联系管理员！";
        }
        userInfo.setCreateTime(System.currentTimeMillis());
        userInfo.setAccountStatus((byte) 0);
        int insert = baseService.insert(userInfo);
        if(insert <= 0){
            return "注册失败,请联系客服处理！";
        }
        // 记录用户注册操作
        UserOperateLog userOperateLog = new UserOperateLog();
        userOperateLog.setUserId(userInfo.getId());
        userOperateLog.setUpdateContent("用户注册；账号：" + userInfo.getAccountNo());
        userOperateLog.setUpdateTime(System.currentTimeMillis());
        baseService.insert(userOperateLog);
        // 给当前用户/推荐人的账户新增 金额
        addBalance(userInfo.getId());
        // 当推荐人不为空时，给推荐人增加推荐金额
        if(!StringUtils.isEmpty(regUserParent)){
            addBalance(userInfo.getParentVipNo());
        }
        // 移除redis中的验证码
        redisTemplate.delete(buildKey(webRequest, ValidateCodeType.SMS));
        // 请求云顶平台注册
        return reqYunDingReg(registerData);
    }

    /** 构建redis存储验证码的key */
    private Object buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        String deviceId = request.getHeader("deviceId");
        if(org.apache.commons.lang.StringUtils.isBlank(deviceId)){
            return "error,请在请求头中携带deviceId参数";
        }
        return "code:" + validateCodeType.toString().toLowerCase() + ":" + deviceId;
    }

    /** 注册用户，新增注册金额/推荐金额 */
    @Override
    public void addBalance(Integer userId) {
            // 用户增加3元
            Map<String, Object> incomeUser = userInfoDao.queryIncomeByUserId(userId);
            if(null == incomeUser){
                UserIncome userIncome = new UserIncome();
                userIncome.setUserId(userId);
                userIncome.setRecommendMoney(new BigDecimal("3"));
                int insertNum = baseService.insertNoId(userIncome);
                if(insertNum <= 0){
                    logger.error("新注册用户， 增加注册金额失败");
                }
            } else {
                BigDecimal recommend = (BigDecimal) incomeUser.get("recommendMoney");
                recommend = null == recommend?BigDecimal.ZERO:recommend;
                int updateNum = userInfoDao.updateIncomeByUserId(recommend.add(new BigDecimal("3")), userId);
                if(updateNum <= 0){
                    logger.error("新注册用户， 推荐人增加金额失败");
                }
            }
    }

    /** 用户登录成功后，记录用户登录痕迹 */
    @Override
    public String saveLoginHis(Integer userId, String loginIp){
        LoginHistory loginHistory = this.orgLoginHistory.clone();
        loginHistory.setUserId(userId);
        loginHistory.setLoginIp(loginIp);
        loginHistory.setLoginDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        loginHistory.setLoginTime(System.currentTimeMillis());
        int insert = baseService.insert(loginHistory);
        if(insert <= 0){
            logger.error("保存登录记录失败！");
        }
        return ReturnData.SUCCESS;
    }

    /** 同步云顶娱乐注册 */
    public String reqYunDingReg(Map<String, String> registerData){
        String registerRes = ReturnData.SUCCESS;
        // 根据当前平台的会员id，查询云顶平台的会员id
        String parentVipNo = registerData.get("parentVipNo");
        String yd_parentId = "";
        if(!StringUtils.isEmpty(parentVipNo)){
            YdVipInfo ydVipInfo = new YdVipInfo();
            ydVipInfo.setVipNo(Integer.valueOf(parentVipNo));
            ydVipInfo = baseService.queryDataByField(ydVipInfo, "vipNo");
            if(null != ydVipInfo){
                yd_parentId = ydVipInfo.getYdVipId();
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", registerData.get("accountNo"));
        params.put("password", registerData.get("password"));
        params.put("repassword", registerData.get("password"));
        params.put("realname", registerData.get("realName"));
        params.put("tel", registerData.get("phoneNo"));
        params.put("qq", registerData.get("qqNumber"));
        params.put("qkmm", registerData.get("payPwd"));
        params.put("reqkmm", registerData.get("payPwd"));
        // TODO 推荐人id 目前先设置为空，
        params.put("tjid", "");
        params.put("submit", " 提 交 ");
        try {
            HttpResponse httpResponse = HttpClientUtils.httpPost("https://ydydyd.com/cn/register", params, null);
            if(httpResponse.getStatusLine().getStatusCode() == 302){
                logger.info(String.format("用户：%s同步云顶娱乐平台注册成功！", registerData.get("accountNo")));
            } else{
                String errMsg = "warning:注册成功，%s，请联系客服！";
                logger.error(String.format("用户：%s同步云顶娱乐平台注册失败！", registerData.get("accountNo")));
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    String responseStr = EntityUtils.toString(entity, "UTF-8");
                    if(responseStr.contains("该名称已经被使用,请更换")){
                        logger.error("当前账号已在云顶平台注册！");
                        registerRes = String.format(errMsg, "该账号已被云顶娱乐注册");
                    }else{
                        logger.error(responseStr);
                        registerRes = String.format(errMsg, "同步云顶娱乐平台失败");
                    }
                } else{
                    registerRes = String.format(errMsg, "同步云顶娱乐平台失败");
                }
            }
        } catch (Exception e) {
            registerRes = "warning:当前平台注册成功，同步云顶娱乐平台失败，请联系客服！";
            logger.info("同步云顶平台注册失败：" , e);
        }
        return registerRes;
    }

    /** 登录并获取云顶会员的推荐号 */
    /*private void getYdRecommendNum(){
        Map<String, String> params = new HashMap<>();
        params.put("username","a37569398");
        params.put("password", "231218");
        params.put("crypt", "0");
        Header[] headers = new Header[3];
        headers[0] = new BasicHeader("Host", "ydydyd.com");
        headers[1] = new BasicHeader("Origin", "https://ydydyd.com");
        headers[2] = new BasicHeader("Referer", "https://ydydyd.com/cn/index");
        try {
            HttpResponse httpResponse = HttpClientUtils.httpPost("https://ydydyd.com/cn/login", params, headers);
            System.out.println("状态码：" + httpResponse.getStatusLine().getStatusCode());
            System.out.print("请求头：");
            System.out.println(Arrays.toString(httpResponse.getAllHeaders()));
            HttpEntity entity = httpResponse.getEntity();
            if(entity != null){
                String responseStr = EntityUtils.toString(entity, "UTF-8");
                logger.error(responseStr);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }*/

    /** 同步云顶平台的会员推荐id */
    /*public String saveYdVipId(){
        try {
            Header[] headers = new Header[3];
            headers[0] = new BasicHeader("Host", "ydydyd.com");
            headers[1] = new BasicHeader("Referer", "https://ydydyd.com/member-center");
            //  Set-Cookie: web=p4pqcomot5gl8k025rspu047i1; path=/, Set-Cookie: randomYes=43663012701; path=/, Set-Cookie: randomYes=43663012701; path=/, Location: /cn/index, Server: CDNFly]
            headers[2] = new BasicHeader("Cookie", "firstVisit=1; rbADcookie=Y; BASEJS_IS_MEMBER_LOGIN=1; NP_118449=Y; web=1kg6avc64cm8gs6qn18lmbqdb1; randomYes=43663012701; randomId=91eee770874430e626ef6023f8501c4a0df9d93c80; BASEJS_MENU_LINK=%23module%2FmemberLevel");
            HttpResponse httpResponse = HttpClientUtils.httpGet("https://ydydyd.com/member-center/tj", headers);
            HttpEntity entity = httpResponse.getEntity();
            String reqStr = EntityUtils.toString(entity, "UTF-8");
            System.out.println(reqStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnData.SUCCESS;
    }*/

    /** 获取最近15天的登录记录 */
    @Override
    @CustomPage
    public List<LoginHistory> getLoginHistory(Pagination<LoginHistory> pagination) {
        return userInfoDao.getLoginHistory(pagination);
    }

    /** 修改用户信息 */
    @Override
    public String updateUserInfo(UserInfo userInfo, String field, HttpServletRequest request) {
        if(StringUtils.isBlank(field)){
            return "标识字段不可为空";
        }
        UserInfo user = orgUserInfo.clone();
        // 校验对应的更新字段
        String validRes = validateField(user, userInfo, field, request);
        if(!ReturnData.SUCCESS.equals(validRes)){
            return validRes;
        }
        // 更新
        int updateNum = baseService.updateField(user);
        if(updateNum <= 0){
            logger.error("更新用户信息失败！");
            return "更新失败";
        }
        // 记录用户注册操作
        UserOperateLog userOperateLog = new UserOperateLog();
        userOperateLog.setUserId(user.getId());
        userOperateLog.setUpdateContent(userInfo.toString());
        userOperateLog.setUpdateContent("修改用户信息,修改字段：" + field);
        userOperateLog.setUpdateTime(System.currentTimeMillis());
        baseService.insert(userOperateLog);
        // 更新修改次数
        updateModifyCount(field, userInfo.getId());
        return ReturnData.SUCCESS;
    }

    /** 更新修改次数(目前只对手机号码，密码，付款密码限制) */
    private int updateModifyCount(String field, Integer userId) {
        if(!"phoneNo".equals(field) && !"pwd".equals(field) && !"payPwd".equals(field)){
            return 0;
        }
        return userInfoDao.updateModifyCount(field, userId);
    }

    /** 校验用户字段 */
    private String validateField(UserInfo user, UserInfo userInfo, String field, HttpServletRequest request) {
        if(null == userInfo.getId()){
            return "用户id不能为空！";
        }
        user.setId(userInfo.getId());
        // 根据id，查询用户剩余修改次数
        UserUpdateCount userUpdateCount = queryUpdateCountByUserId(userInfo.getId());
        if(null == userUpdateCount){
            userUpdateCount = new UserUpdateCount();
        }
        switch (field){
            case "phoneNo":
                String phoneValid = validPhone(user, userInfo, request, userUpdateCount.getPhone());
                if(!phoneValid.equals(ReturnData.SUCCESS)){
                    return phoneValid;
                }
                break;
            case "pwd":
                String pwdValid = validPassword(user, userInfo, userUpdateCount.getPassword());
                if(!pwdValid.equals(ReturnData.SUCCESS)){
                    return pwdValid;
                }
                break;
            case "payPwd":
                String payPwdValid = validPayPassword(user, userInfo, userUpdateCount.getPayPassword());
                if(!payPwdValid.equals(ReturnData.SUCCESS)){
                    return payPwdValid;
                }
                break;
            case "qqNo":
                String qqValid = validQQ(user, userInfo);
                if(!qqValid.equals(ReturnData.SUCCESS)){
                    return qqValid;
                }
                break;
            case "sex":
                if(null == userInfo.getSex()){
                    return "性别不允许为空！";
                }
                if(userInfo.getSex() !=0 && userInfo.getSex() != 1){
                    return "无法识别的值";
                }
                user.setSex(userInfo.getSex());
                break;
            case "identityNo":
                if(StringUtils.isBlank(userInfo.getIdentityNo())){
                    return "身份证号不允许为空";
                }
                user.setIdentityNo(userInfo.getIdentityNo());
                break;
            case "bankCardNo1":
                if(StringUtils.isBlank(userInfo.getBankCardNo1())){
                    return "银行卡号不允许为空";
                }
                user.setBankCardNo1(userInfo.getBankCardNo1());
                break;
            case "bankCardNo2":
                if(StringUtils.isBlank(userInfo.getBankCardNo2())){
                    return "银行卡号不允许为空";
                }
                user.setBankCardNo2(userInfo.getBankCardNo2());
                break;
            case "bankCardNo3":
                if(StringUtils.isBlank(userInfo.getBankCardNo3())){
                    return "银行卡号不允许为空";
                }
                user.setBankCardNo3(userInfo.getBankCardNo3());
                break;
        }
        return ReturnData.SUCCESS;
    }

    /** 根据用户id，查询用户信息可更新数据 */
    private UserUpdateCount queryUpdateCountByUserId(Integer userId) {
        return userInfoDao.queryUpdateCountByUserId(userId);
    }

    /** 校验短信验证码 */
    @Override
    public String smsCodeValidate(String validateCode, ServletWebRequest webRequest){
        String deviceId = webRequest.getHeader("deviceId");
        if(StringUtils.isBlank(deviceId)){
            return "请在请求头中携带deviceId参数";
        }
        // 调用网关接口校验验证码
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getGatewayUrl() + String.format(this.VALIDATE_SMS_CODE, deviceId, validateCode), ReturnData.class);
        if(null == returnData){
            return "校验验证码失败：请求对象返回为空";
        }
        if (!returnData.getReturnType().equals(ReturnData.SUCCESS)){
            return returnData.getContent();
        }
        return ReturnData.SUCCESS;
    }

    /** 根据用户账号查询用户信息 */
    @Override
    public UserInfo queryUserInfoByAccount(String accountNo) {
        return userInfoDao.queryUserInfoByAccount(accountNo);
    }

    /** 修改：校验手机号码 */
    private String validPhone(UserInfo user, UserInfo userInfo, HttpServletRequest request, int phoneCount){
        String phoneNo = userInfo.getPhoneNo();
        if(StringUtils.isBlank(phoneNo)){
            return "手机号码不允许为空！";
        }
        if(!NumberUtils.isPhoneNun(phoneNo)){
            return "请输入正确的手机号码！";
        }
        if(phoneCount > 0 ){
            return "换绑手机号码次数已上限";
        }
        // 校验验证码
        ServletWebRequest webRequest = new ServletWebRequest(request);
        String validRet = smsCodeValidate(userInfo.getValidateCode(), webRequest);
        if(!ReturnData.SUCCESS.equals(validRet)){
            return validRet;
        }
        user.setPhoneNo(userInfo.getPhoneNo());
        return ReturnData.SUCCESS;
    }

    /** 修改：校验密码 */
    private String validPassword(UserInfo user, UserInfo userInfo, int passwordCount){
        if(StringUtils.isBlank(userInfo.getOldPwd())){
            return "旧密码不能为空！";
        }
        if(StringUtils.isBlank(userInfo.getPwd())){
            return "新密码不允许为空！";
        }
        if(StringUtils.isBlank(userInfo.getConfirmPwd())){
            return "确认密码不能为空！";
        }
        if(passwordCount > 2){
            return "修改密码次数已上限";
        }
        // 查询验证数据库密码和输入的旧密码是否一致
        UserInfo oldInfo = baseService.queryById(userInfo);
        try {
            String oldPassword = PasswordUtil.decode(oldInfo.getPwd());
            if(!oldPassword.equals(userInfo.getOldPwd())){
                return "旧密码错误，请重新输入！";
            }
        } catch (Exception e) {
            logger.error("修改用户密码，解码失败！", e);
            return "修改失败：密码解码失败，请联系客服处理";
        }
        // 若新旧密码一致，则不允许修改
        if(userInfo.getOldPwd().equals(userInfo.getPwd())){
            return "旧密码不能与新密码相同";
        }
        // 若新密码与确认密码不一致
        if(!userInfo.getPwd().equals(userInfo.getConfirmPwd())){
            return "新密码与确认密码不一致";
        }
        try {
            user.setPwd(PasswordUtil.encode(userInfo.getPwd()));
        } catch (Exception e) {
            logger.error("修改用户密码，设置密码过程中，加密失败！", e);
            return "修改失败：密码加密失败，请联系客服处理";
        }
        return ReturnData.SUCCESS;
    }

    /** 修改：校验取款密码 */
    private String validPayPassword(UserInfo user, UserInfo userInfo, int payPwdCount){
        if(StringUtils.isBlank(userInfo.getOldPwd())){
            return "旧密码不能为空！";
        }
        if(StringUtils.isBlank(userInfo.getPwd())){
            return "新密码不允许为空！";
        }
        if(StringUtils.isBlank(userInfo.getConfirmPwd())){
            return "确认密码不能为空！";
        }
        if(payPwdCount > 2){
            return "修改密码次数已上限";
        }
        // 查询，并判断旧支付密码是否与输入的旧密码相同
        // 查询验证数据库密码和输入的旧密码是否一致
        UserInfo oldInfo = baseService.queryById(userInfo);
        try {
            String oldPayPwd = PasswordUtil.decode(oldInfo.getPayPwd());
            if(!oldPayPwd.equals(userInfo.getOldPwd())){
                return "旧密码错误，请重新输入！";
            }
        } catch (Exception e) {
            logger.error("修改用户取款密码，解码失败！", e);
            return "密码解码失败，请联系客服处理";
        }
        // 若新旧密码一致，则不允许修改
        if(userInfo.getOldPwd().equals(userInfo.getPwd())){
            return "旧密码不能与新密码相同";
        }
        // 若新密码与确认密码不一致
        if(!userInfo.getPwd().equals(userInfo.getConfirmPwd())){
            return "新密码与确认密码不一致";
        }
        try {
            user.setPayPwd(PasswordUtil.encode(userInfo.getPwd()));
        } catch (Exception e) {
            logger.error("修改用户取款密码，设置密码过程中，加密失败！", e);
            return "密码加密失败，请联系客服处理";
        }
        return ReturnData.SUCCESS;
    }

    /** 修改：校验QQ号 */
    private String validQQ(UserInfo user, UserInfo userInfo){
        UserInfo oldInfo = baseService.queryById(userInfo);
        if(StringUtils.isBlank(userInfo.getQqNo())){
            return "QQ号码不允许为空";
        }
        if(userInfo.getQqNo().length() < 3){
            return "QQ号码不得少于3位";
        }
        if(userInfo.getQqNo().length() > 20){
            return "QQ号码不得超过20位";
        }
        if(oldInfo.getQqNo().equals(userInfo.getQqNo())){
            return "不能与当前QQ号码相同";
        }
        user.setQqNo(userInfo.getQqNo());
        return ReturnData.SUCCESS;
    }

    /** 根据手机号码更新用户密码 */
    @Override
    public int updateUserInfoByPhoneNo(String password, String phoneNum) {
        return userInfoDao.updateUserInfoByPhoneNo(password, phoneNum);
    }

    /** 根据用户id，查询用户收益 */
    @Override
    public BigDecimal queryUserIncomeBalance(Integer userId) {
        UserInfo userInfo = userInfoDao.queryUserIncomeBalance(userId);
        // 余额 + 在线收益 + 推荐收益 - 冻结金额
        return userInfo.getBalance().add(userInfo.getOnlineMoney()).add(userInfo.getRecommendMoney()).subtract(userInfo.getFreezeMoney());
    }

    /** 换绑用户手机号码*/
    @Override
    public String handsetChange(UserInfo userInfo) {
        UserInfo userInfo1 = baseService.queryDataByField(userInfo,"phoneNo");
        if (null == userInfo1){
            baseService.updateField(userInfo);
            return ReturnData.SUCCESS;
        }else{
            return "该手机号已被绑定";
        }
    }
}
