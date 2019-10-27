package com.taotao.portal.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import com.taotao.utils.CookieUtils;
import com.taotao.utils.HttpClientUtil;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车Service
 * @author Kyle
 * @create 2019-07-23 9:52
 */
@Service
public class CartServiceImpl implements CartService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;

    @Value("${ITEM_INFO_URL}")
    private String ITEM_INFO_URL;

    /**
     * 添加购物车商品
     * @param itemId:商品id
     * @param num:数量
     * @return TaotaoResult
     * break:表示跳出当前循环体
     * return:表示直接结束整个函数
     * continue:表示当i=2的时候结束这一次的循环,继续执行i=3的循环
     */
    @Override
    public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
        //取商品信息
        CartItem cartItem = null;
        //获取购物车商品列表
        List<CartItem> cartItemList = getCartItemList(request);
        //判断购物车商品列表中是否存在此商品
        for (CartItem cItem : cartItemList) {
            //如果存在此商品
            if (cItem.getId() == itemId){
                //增加商品数量
                cItem.setNum(cItem.getNum() + num);
                cartItem = cItem;
                break;
            }
        }
        //如果不存在此商品
        if (cartItem == null) {
            //创建一个新的购物车项
            cartItem = new CartItem();
            //根据商品id查询商品的基本信息
            String json = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
            //把json转换成java对象
            TaotaoResult result = TaotaoResult.formatToPojo(json, TbItem.class);
            if (result.getStatus() == 200) {
                TbItem tbItem = (TbItem) result.getData();
                cartItem.setId(tbItem.getId());
                cartItem.setTitle(tbItem.getTitle());
                cartItem.setImage(tbItem.getImage() == null ? "" : tbItem.getImage().split(",")[0]);
                cartItem.setNum(num);
                cartItem.setPrice(tbItem.getPrice());
            }
            //添加到购物车列表
            cartItemList.add(cartItem);
        }
        //把购物车列表写入cookie
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(cartItemList),true);
        return TaotaoResult.ok();
    }

    /**
     * 从cookie中获取商品列表信息
     * @param request
     * @param response
     * @return
     */
    @Override
    public List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> cartItemList = getCartItemList(request);
        return cartItemList;
    }

    /**
     * 删除购物车项
     * @param itemId
     * @return
     */
    @Override
    public TaotaoResult deleteCartItem(Long itemId,HttpServletRequest request,HttpServletResponse response) {
        //从cookie中获取购物车商品列表
        List<CartItem> cartItemList = getCartItemList(request);
        //从列表中循环遍历找到此商品
        for (CartItem cartItem : cartItemList) {
            //如果此时id匹配就表示找到此商品
            if (cartItem.getId() == itemId){
                //把此商品的删除
                cartItemList.remove(cartItem);
                break;
            }
        }
        //把购物车列表重新写入cookie
        CookieUtils.setCookie(request,response,"TT_CART",JsonUtils.objectToJson(cartItemList),true);
        return TaotaoResult.ok();
    }



    /**
     * 封装一个从cookie中获取商品列表的方法
     * @param request
     * @return
     */
    private List<CartItem> getCartItemList(HttpServletRequest request){
        //从cookie中获取商品列表
        String cartJson = CookieUtils.getCookieValue(request, "TT_CART", true);
        if (cartJson == null){
            return new ArrayList<>();
        }
        try {
            //把json转换成商品列表
            List<CartItem> list = JsonUtils.jsonToList(cartJson, CartItem.class);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
 }
