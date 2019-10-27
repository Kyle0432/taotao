package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Kyle
 * @create 2019-05-16 10:11
 */
@Controller
public class PageController {
    /**
     * 在WEB-INF下展现首页index,在controller进行页面跳转
     * @return
     */
    @RequestMapping("/")
    public String showIndex(){
         return "index";
    }

    /**
     *展示其他页面(通过在首页点击请求url可以直接展示其他页面)
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }


}
