package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理
 * @author Kyle
 * @create 2019-05-27 22:26
 */

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(value = "id",defaultValue = "0") Long parentId){
        List<EasyUITreeNode> categoryList = contentCategoryService.getCategoryList(parentId);
        return categoryList;
    }

    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult createContentCategory(Long parentId,String name){
        TaotaoResult result = contentCategoryService.insertContentCategory(parentId, name);
        return  result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteContentCategory(@RequestParam(value = "parentId",defaultValue = "0") Long parentId,Long id){
        System.out.println(parentId);
        System.out.println(id);
        TaotaoResult result = contentCategoryService.deleteContentCategory(parentId, id);
        return result;
    }

    @RequestMapping("/update")
    @ResponseBody
    public  TaotaoResult updateContentCategory(Long id,String name){
        TaotaoResult result = contentCategoryService.updateContentCategory(id, name);
        return  result;
    }

}
