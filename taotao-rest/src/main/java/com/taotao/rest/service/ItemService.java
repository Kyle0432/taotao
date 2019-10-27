package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * @author Kyle
 * @create 2019-07-23 16:02
 */
public interface ItemService {

    TaotaoResult getItemBaseInfo(long itemId);

    TaotaoResult getItemDesc(long itemId);

    TaotaoResult getItemParam(long itemId);
}
