package com.li.cloud.gateway.security.authentication;

import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.NumberUtils;
import com.li.cloud.common.utils.PasswordUtil;
import com.li.cloud.common.utils.RegularUtils;
import com.li.cloud.gateway.security.vo.ManagerUser;
import com.li.cloud.gateway.security.vo.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @desc 自定义身份认证
 * @date 2020-03-22
 */
@Component
public class CusUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 注入密码加密对象：PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BaseService baseService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("登录用户为：" + username);
        String loginPassword = "";
        boolean isManager = false;
        UserInfo userInfo = new UserInfo();
        // 如果用户名为11位的数字，根据手机号码查询用户信息，
        if (NumberUtils.isPhoneNun(username)) {
            userInfo.setPhoneNo(username);
            userInfo = baseService.queryDataByField(userInfo, "phoneNo");
        }
        // 否则根据用户名查找
        else if(!username.equals("admin")){
            if(StringUtils.isEmpty(username) || username.length() < 6 || username.length() > 12 || !RegularUtils.hasNumAndLetter(username)){
                throw new BadCredentialsException("请输入正确的登录账号");
            }
            userInfo.setAccountNo(username);
            userInfo = baseService.queryDataByField(userInfo, "accountNo");
        }

        ManagerUser managerUser = new ManagerUser();
        // 普通用户查找为空，则查找管理员用户表
        if(null == userInfo || null == userInfo.getId()){
            managerUser.setAccount(username);
            managerUser = baseService.queryDataByField(managerUser, "account");
            // 判断用户是否存在
            if(null == managerUser){
                throw new BadCredentialsException("userNotFound");
            }
            isManager = true;
            loginPassword = managerUser.getPassword();
        } else {
            // 判断用户状态是否正常
            if(userInfo.getAccountStatus() == 1 ){
                throw new BadCredentialsException("accountLocked");
            }
            loginPassword = userInfo.getPwd();
        }

        // 数据库密码解密
        String decodePwd = "";
        if (!StringUtils.isEmpty(loginPassword)) {
            try {
                decodePwd = isManager?loginPassword:PasswordUtil.decode(loginPassword);
            } catch (Exception e) {
                logger.error("解析用户密码异常", e);
                throw new RuntimeException("解析用户密码异常!");
            }
        }
        // 验证用户
        String password = passwordEncoder.encode(decodePwd);
        return new User(username, password, true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
    }
}
