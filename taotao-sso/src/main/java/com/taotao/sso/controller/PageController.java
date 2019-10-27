package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 该controller进行页面跳转
 * @author Kyle
 * @create 2019-07-20 21:48
 */
@Controller
@RequestMapping("/page")
public class PageController {

    /**
     * 显示register页面
     * @return
     */
    @RequestMapping("/register")
    public String showRegister(){
        return "register";
    }

    /**
     * 显示login页面
     * @return
     */
    @RequestMapping("/login")
    public String showLogin(String redirect, Model model){
        //判断是否有回调url
        model.addAttribute("redirect",redirect);
        return  "login";
    }

}
