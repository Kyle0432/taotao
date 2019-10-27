package com.taotao.portal.controller;

import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.CartService;
import com.taotao.portal.service.OrderService;
import com.taotao.utils.ExceptionUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Kyle
 * @create 2019-07-26 10:20
 */

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order-cart")
    public String showOrderCart(HttpServletRequest request, HttpServletResponse response, Model model){
        //取购物车商品列表
        List<CartItem> cartItemList = cartService.getCartItemList(request, response);
        //传传递给页面
        model.addAttribute("cartList",cartItemList);
        return "order-cart";
    }

    @RequestMapping("/create")
    public String createOrder(Order order,Model model,HttpServletRequest request){
        try {
            //从LoginInterceptor中取出保存用户信息
            TbUser user = (TbUser) request.getAttribute("user");
            //在order对象中补全用户信息
            order.setUserId(user.getId());
            order.setBuyerNick(user.getUsername());
            //调用服务
            String orderId = orderService.createOrder(order);
            model.addAttribute("orderId",orderId);
            model.addAttribute("payment",order.getPayment());
            model.addAttribute("date",new DateTime().plusDays(3).toString("yyyy-MM-dd"));
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("message", ExceptionUtil.getStackTrace(e));
            return "error/exception";
        }
    }
}
