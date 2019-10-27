package com.taotao.rest.controller;

import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品分类列表
 * @author Kyle
 * @create 2019-05-27 17:57
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

   /* //方法一:
    @RequestMapping(value="/itemcat/list"
            ,produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")//可解决乱码问题
    @ResponseBody
    public String getItemCatList(String callback){
        CatResult catResult = itemCatService.getItemCatList();
        //把pojo转换成json字符串
        String json = JsonUtils.objectToJson(catResult);
        //拼装返回值
        String result = callback + "("+ json +");";
        return result;
    }*/
   //方法二:
    @RequestMapping("/itemcat/list")
    @ResponseBody
    public Object getItemCatList(String callback){
        CatResult itemCatList = itemCatService.getItemCatList();
        //这个方法可以自动有字符串转换为json字符串.
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(itemCatList);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }


}
