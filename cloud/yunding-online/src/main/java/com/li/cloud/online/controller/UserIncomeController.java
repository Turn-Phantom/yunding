package com.li.cloud.online.controller;

import com.li.cloud.online.service.UserIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 用户收益 控制器
 * @date 2020-05-17
 */
@RestController
@RequestMapping("/rest/user/income")
public class UserIncomeController {

    @Autowired
    private UserIncomeService userIncomeService;

}
