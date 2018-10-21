package com.april.house.web.controller;

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
        return "redirect:/index";
    }

}
