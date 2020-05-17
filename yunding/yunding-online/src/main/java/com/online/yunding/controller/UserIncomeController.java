package com.online.yunding.controller;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.service.UserIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
