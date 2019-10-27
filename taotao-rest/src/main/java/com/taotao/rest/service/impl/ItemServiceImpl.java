package com.taotao.rest.service.impl;

import com.mysql.jdbc.StringUtils;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ItemService;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品信息管理Service
 * @author Kyle
 * @create 2019-07-23 16:02
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    @Autowired
    private TbItemDescMapper itemDescMapper;

    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_KEY}")
    private String REDIS_ITEM_KEY;
    @Value("${REDIS_ITEM_EXPIRE}")
    private Integer REDIS_ITEM_EXPIRE;

    /**
     * 获取商品的基本信息
     * @param itemId
     * @return
     */
    @Override
    public TaotaoResult getItemBaseInfo(long itemId) {
        try {
            //添加缓存逻辑
            //从缓存中取出商品信息,商品id对应的信息
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":base");
            //判断是否有值
            if(!StringUtils.isNullOrEmpty(json)){
                //把json转换java对象
                TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
                return TaotaoResult.ok(item);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //如果缓存中不存在就根据商品id到数据库查询商品信息
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        //使用TaotaoResult包装一下
        try {
            //把商品信息写入缓存,因为保存在redis里面必须把对象转换成字符串
            jedisClient.set(REDIS_ITEM_KEY+";"+itemId+":base",JsonUtils.objectToJson(tbItem));
           //设置key的有效期
            jedisClient.expire(REDIS_ITEM_KEY+";"+itemId+":base",REDIS_ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return TaotaoResult.ok(tbItem);
    }

    /**
     * 获取商品描述信息
     * @param itemId
     * @return
     */
    @Override
    public TaotaoResult getItemDesc(long itemId) {
        //添加缓存
        try {
              //添加缓存逻辑
              //从缓存中获取商品信息,商品id对应的信息
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":desc");
            if (!StringUtils.isNullOrEmpty(json)){
                //把json转换成java对象
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return TaotaoResult.ok(tbItemDesc);
            }
        }catch (Exception e){
                e.printStackTrace();
        }
        //如果缓存中不存在值就创建查询条件
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        try {
            //把商品信息写入缓存
            jedisClient.set(REDIS_ITEM_KEY+";"+itemId+":desc",JsonUtils.objectToJson(tbItemDesc));
            //设置key的有效期
            jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":desc",REDIS_ITEM_EXPIRE);
        }catch (Exception e){
            e.printStackTrace();
        }
        return TaotaoResult.ok(tbItemDesc);
    }

    @Override
    public TaotaoResult getItemParam(long itemId) {
        //添加缓存
        try {
            //添加缓存逻辑
            //从缓存中获取商品信息,商品id对应的信息
            String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":param");
            //判断是否有值
            if (!StringUtils.isNullOrEmpty(json)){
                //把json转换成java对象
                TbItemParamItem tbItemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
                return TaotaoResult.ok(tbItemParamItem);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //根据商品id查询规格参数
        //设置查询条件
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        //执行查询
        List<TbItemParamItem> list = itemParamItemMapper.selectByExample(example);
        if (list != null && list.size() > 0){
            TbItemParamItem tbItemParamItem = list.get(0);
            try {
                  //把商品信息写入缓存
                jedisClient.set(REDIS_ITEM_KEY+":"+itemId+":param",JsonUtils.objectToJson(tbItemParamItem));
                 //设置key的有效期
                jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":param",REDIS_ITEM_EXPIRE);
            }catch (Exception e){
                e.printStackTrace();
            }
            return TaotaoResult.ok(tbItemParamItem);
        }
        return TaotaoResult.build(400,"无此商品规格");
    }
}
