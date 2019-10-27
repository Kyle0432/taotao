package com.taotao.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

/**
 * @author Kyle
 * @create 2019-05-14 21:22
 */
public interface ItemService {


   TbItem getItemById(Long itemId);

   EasyUIDataGridResult getItemList(int page,int rows);

   TaotaoResult createItem(TbItem tbItem,String desc,String itemParam) throws Exception;

}
