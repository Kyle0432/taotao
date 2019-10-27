package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理controller
 * @author Kyle
 * @create 2019-05-28 21:20
 */
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService ;

    @RequestMapping("/content/save")
    @ResponseBody
    public TaotaoResult insertContent(TbContent tbContent){
        TaotaoResult result = contentService.insertContent(tbContent);
        return result;
    }

}
