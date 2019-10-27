package com.taotao.order.service.impl;

import com.mysql.jdbc.StringUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.dao.JedisClient;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单管理service
 * @author Kyle
 * @create 2019-07-25 15:46
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_GEN_KEY}")
    private String ORDER_GEN_KEY;

    @Value("${ORDER_INIT_ID}")
    private  String ORDER_INIT_ID;

    @Value("${ORDER_DETAIL_GEN_KEY}")
    private String ORDER_DETAIL_GEN_KEY;

    /**
     * 生成订单的过程
     * @param order
     * @param itemList
     * @param orderShipping
     * @return
     */

    @Override
    public TaotaoResult createOrder(TbOrder order, List<TbOrderItem> itemList, TbOrderShipping orderShipping) {
       //向订单表中插入记录
        // 获得订单号
        String string = jedisClient.get(ORDER_GEN_KEY);
        //判断redis里面是否有这个值
        if(StringUtils.isNullOrEmpty(string)){
            //没有的话设置一个初始化的值
            jedisClient.set(ORDER_GEN_KEY,ORDER_INIT_ID);
        }
        //对该订单号加1
        long orderId = jedisClient.incr(ORDER_GEN_KEY);
        //补全pojo的属性
          //把orderId的类型转换成字符串
          order.setOrderId(orderId+"");
          //状态:1、未付款 2、已付款 3、未发货  4、已发货 5、交易成功 6、交易关闭
          order.setStatus(1);
          //创建更改订单时间
          Date date = new Date();
          order.setCreateTime(date);
          order.setUpdateTime(date);
          //0、未评价  1、已评价
          order.setBuyerRate(0);
          //向订单表中插入数据
          tbOrderMapper.insertSelective(order);

       //插入订单明细
        for (TbOrderItem tbOrderItem : itemList) {
            //补全订单明细
            //取订单明细id
            long orderDetailId = jedisClient.incr(ORDER_DETAIL_GEN_KEY);
            //把orderDetailId的类型转换成字符串
            tbOrderItem.setId(orderDetailId+"");
            tbOrderItem.setOrderId(orderId+"");
        //向订单明细插入记录
            tbOrderItemMapper.insertSelective(tbOrderItem);
        }

        //插入物流表
        //补全物流表的属性
        orderShipping.setOrderId(orderId + "");
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        tbOrderShippingMapper.insertSelective(orderShipping);

        //返回订单号
        return TaotaoResult.ok(orderId);
    }
}
