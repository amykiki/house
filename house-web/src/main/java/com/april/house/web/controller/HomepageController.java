package com.april.house.web.controller;

import com.april.house.common.constants.CommonConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {

    @RequestMapping("index")
    public String accountsRegister() {
        return "/homepage/index";
    }

    @RequestMapping("")
    public String index() {
        return CommonConstants.REDIRECT +  "/index";
    }

}
