package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kyle
 * @create 2019-05-14 22:27
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 查询全部商品
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId){
        TbItem itemById = itemService.getItemById(itemId);
        return itemById;
    }

    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){
       EasyUIDataGridResult result = itemService.getItemList(page,rows);
       return result;
    }

    /**
     * 提交表单,添加商品
     * @param item
     * @return
     */
    @RequestMapping(value = "/item/save",method = RequestMethod.POST)
    @ResponseBody
    //这个desc和item是分开来的不是在同一张表的,此处是一个方法同时操作两张表
    public TaotaoResult createItem(TbItem item,String desc,String itemParams) throws Exception {
        TaotaoResult result = itemService.createItem(item,desc,itemParams);

        return result;
    }
}
