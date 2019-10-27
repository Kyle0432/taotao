package com.taotao.controller;

import com.taotao.service.ItemParamItemService;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 展现商品规格参数
 * @author Kyle
 * @create 2019-05-20 23:45
 */
@Controller
public class ItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;

    @RequestMapping("/showitem/{itemId}")  //要传递给jsp这里可以使用model
    public String showItemParam(@PathVariable Long itemId, Model model){
        String string = itemParamItemService.getItemParamByItemId(itemId);
        model.addAttribute("itemParam",string);
        return "item";
    }
}
