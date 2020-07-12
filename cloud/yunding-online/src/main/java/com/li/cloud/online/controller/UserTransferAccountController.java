package com.li.cloud.online.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.DateUtil;
import com.li.cloud.common.utils.PasswordUtil;
import com.li.cloud.online.entity.UserInfo;
import com.li.cloud.online.entity.UserTransferAccountOrder;
import com.li.cloud.online.server.WebSocketServer;
import com.li.cloud.online.service.UserTransferAccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @desc 用户充值/提款 控制器
 * @date 2020-04-22
 */
@RestController
@RequestMapping("/rest/user/account")
public class UserTransferAccountController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserTransferAccountService userTransferAccountService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private WebSocketServer webSocketServer;

    /** 查询我的收益数据 */
    @GetMapping("/queryIncomeData")
    public ReturnData queryIncomeData(Integer id, String account){
        if(null == id || StringUtils.isBlank(account)){
            return ReturnData.error("请求参数不可为空！");
        }
        return ReturnData.successData(userTransferAccountService.queryIncomeData(id, account));
    }

    /** 充值订单申请 */
    @PostMapping("/investOrderApply")
    public ReturnData investOrderApply(UserTransferAccountOrder userTransferAccountOrder){
        if(null == userTransferAccountOrder){
            return ReturnData.error("请求参数对象为空！");
        }
        if(null == userTransferAccountOrder.getUserId()){
            return ReturnData.error("用户id不能为空！");
        }
        if(StringUtils.isBlank(userTransferAccountOrder.getTransferName())){
            return ReturnData.error("付款姓名不能为空！");
        }
        if(StringUtils.isBlank(userTransferAccountOrder.getTransferAccount())){
            return ReturnData.error("付款账户不能为空！");
        }
        if(null == userTransferAccountOrder.getTransferMoney() || BigDecimal.ZERO.compareTo(userTransferAccountOrder.getTransferMoney()) > 0){
            return ReturnData.error("付款金额必须是大于0的数字！");
        }
        if(StringUtils.isBlank(userTransferAccountOrder.getTransferDate())){
            return ReturnData.error("付款日期不能为空！");
        }
        if(StringUtils.isBlank(userTransferAccountOrder.getTransferType())){
            return ReturnData.error("付款类型不能为空 ！");
        }
        userTransferAccountOrder.setOrderType((byte) 1);
        userTransferAccountOrder.setCreateTime(System.currentTimeMillis());
        userTransferAccountOrder.setCheckStatus((byte) 0);
        int insert = baseService.insert(userTransferAccountOrder);
        if(insert <= 0){
            logger.error("用户：" + userTransferAccountOrder.getUserId() +"提交充值订单失败，无法正常插入数据库");
            return ReturnData.error("提交失败!");
        }
        try {
            webSocketServer.sendMessage("用户：" + userTransferAccountOrder.getUserId() + "，充值：" + userTransferAccountOrder.getTransferMoney() + "，待审核");
        } catch (IOException e) {
            logger.error("用户：" + userTransferAccountOrder.getUserId() + "提款通知失败：", e);
        }
        return ReturnData.success("提交成功！");
    }

    /** 查询用户充值记录(注意：根据用户id) */
    @GetMapping("/queryTransByAccount")
    public ReturnData queryInvestData(Integer id, String account, Byte type){
        if(null == id || StringUtils.isBlank(account) || null == type){
            return ReturnData.error("查询参数不能为空");
        }
        if(1 == type || 0 == type){
            return ReturnData.successData(userTransferAccountService.queryTransData(id, type));
        }else{
            return ReturnData.error("无法识别的类型");
        }
    }

    /** 获取用户可用余额 */
    @GetMapping("/getUserAvailableBalance")
    public ReturnData queryUserAvailableBalance(Integer id, String accn){
        if(null == id || StringUtils.isBlank(accn)){
            return ReturnData.error("请求参数不允许为空！");
        }
        return ReturnData.successData(userTransferAccountService.queryUserAvailableBalance(id, accn));
    }

    /** 提交提款申请 */
    @PostMapping("/outMoneyApply")
    public ReturnData outMoneyApply(UserTransferAccountOrder applyOrder){
        // 校验参数
        if(null == applyOrder){
            return ReturnData.error("请求对象参数不能为空");
        }
        if(StringUtils.isBlank(applyOrder.getTransferName())){
            return ReturnData.error("提款人真实姓名不能为空");
        }
        if(StringUtils.isBlank(applyOrder.getTransferBankName())){
            return ReturnData.error("开户银行不能为空");
        }
        if(StringUtils.isBlank(applyOrder.getTransferAccount())){
            return ReturnData.error("提款账号不能为空");
        }
        if(null == applyOrder.getTransferMoney() || BigDecimal.ZERO.compareTo(applyOrder.getTransferMoney()) > 0){
            return ReturnData.error("请输入正确的提款金额");
        }
        // 查询数据库中的提款密码，并对比
        UserInfo userInfo = new UserInfo();
        userInfo.setId(applyOrder.getUserId());
        userInfo = baseService.queryById(userInfo);
        if(null == userInfo){
            return ReturnData.error("该用户不存在");
        }
        if(userInfo.getExtractBalance() > 0){
            if(applyOrder.getTransferMoney().compareTo(new BigDecimal("100")) < 0){
                return ReturnData.error("error:老用户提款，不得低于100元");
            }
            if(applyOrder.getCurrBalance().subtract(applyOrder.getTransferMoney()).compareTo(new BigDecimal("30")) < 0){
                return ReturnData.error("error:老用户必须保留30作为押金");
            }
        }
        // 取款密码核对
        String payPwd = userInfo.getPayPwd();
        try {
            if(!StringUtils.equals(PasswordUtil.decode(payPwd), applyOrder.getTransferPassword())){
                return ReturnData.error("error:取款密码错误");
            }
        } catch (Exception e) {
            logger.error("提现申请：取款密码解密失败");
            return ReturnData.error("提现申请失败，请联系客服处理");
        }
        applyOrder.setOrderType((byte) 0);
        applyOrder.setCheckStatus((byte) 0);
        applyOrder.setTransferDate(DateUtil.getCurrentDateStr("yyyyMMdd"));
        applyOrder.setCreateTime(System.currentTimeMillis());
        // 冻结用户余额
        int updateNum = userTransferAccountService.freezeMoney(applyOrder.getUserId(),applyOrder.getTransferMoney());
        if(updateNum <= 0){
            logger.error("用户：" + applyOrder.getUserId() + "冻结余额失败，提现申请失败");
            return ReturnData.error("冻结余额失败");
        }
        baseService.insert(applyOrder);
        try {
            webSocketServer.sendMessage("用户：" + applyOrder.getUserId() + "，提款：" + applyOrder.getTransferMoney() + "，待审核");
        } catch (IOException e) {
            logger.error("用户：" + applyOrder.getUserId() + "提款通知失败：", e);
        }
        return ReturnData.success("提交申请成功！");
    }

    /** 查询用户充值/提款数据（未审核） */
    @GetMapping("/queryUncheckData")
    public ReturnData queryUncheckData(){
        return ReturnData.successData(userTransferAccountService.queryUncheckData());
    }

    /** 根据id查询数据 */
    @GetMapping("/queryDataById")
    public ReturnData queryDataById(Integer id){
        if(null == id){
            return ReturnData.error("参数不能为空！");
        }
        return ReturnData.successData(userTransferAccountService.queryDataById(id));
    }

    /** 通过充值/提款申请 */
    @PostMapping("/passOrder")
    public ReturnData passOrder(Integer id, Integer userId, String remark){
        if(null == id || null == userId || StringUtils.isBlank(remark)){
            return ReturnData.error("参数不能为空");
        }
        String retStr = userTransferAccountService.updatePassOrder(id, userId, remark);
        if(!ReturnData.SUCCESS.equals(retStr)){
            return ReturnData.success(retStr);
        }
        return ReturnData.success("审核通过");
    }

    /** 拒绝充值/提款申请 */
    @PostMapping("/rejectOrder")
    public ReturnData rejectOrder(Integer id, Integer userId, String ret){
        if(null == id || null == userId || StringUtils.isBlank(ret)){
            return ReturnData.error("参数不能为空");
        }
        String retStr = userTransferAccountService.updateRejectOrder(id, userId, ret);
        if(!ReturnData.SUCCESS.equals(retStr)){
            return ReturnData.error(retStr);
        }
        return ReturnData.success("操作成功");
    }

    /** 云顶后台; 根据用户id，查询用户充值/提款数据 */
    @GetMapping("/queryTransferDataByUserId")
    public ReturnData queryTransferDataByUserId(Integer userId){
        if(null == userId){
            return ReturnData.error("用户id不可为空！");
        }
        return ReturnData.successData(userTransferAccountService.queryTransferDataByUserId(userId));
    }

    /** 根据审核状态查询订单数据 */
    @GetMapping("/queryDataByCheckStatus")
    public ReturnData queryDataByCheckStatus(Byte status){
        if(null == status){
            return ReturnData.error("参数不能为空");
        }
        return ReturnData.successData(userTransferAccountService.queryDataByCheckStatus(status));
    }

}
