package com.taotao.portal.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.CartItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Kyle
 * @create 2019-07-23 9:48
 */
public interface CartService {

    TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request , HttpServletResponse response);

    List<CartItem> getCartItemList(HttpServletRequest request , HttpServletResponse response);

    TaotaoResult deleteCartItem(Long itemId,HttpServletRequest request, HttpServletResponse response);
}
