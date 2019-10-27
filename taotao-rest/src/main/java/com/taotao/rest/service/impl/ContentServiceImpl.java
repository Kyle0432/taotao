package com.taotao.rest.service.impl;

import com.mysql.jdbc.StringUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;
import com.taotao.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 内容管理
 * @author Kyle
 * @create 2019-05-28 22:17
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${INDEX_CONTENT_REDIS_KEY}")
    private String INDEX_CONTENT_REDIS_KEY;

    @Override
    public List<TbContent> getContentList(long contentCid) {
        //从缓存中取内容
        try {    //contentCid+""表示的是把类型转换成字符串类型
            String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCid + "");
                 //判断获取的value是否为空
            if(!StringUtils.isNullOrEmpty(result)){
                //把字符串转换成list
                List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
                return  resultList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //根据内容分类id查询内容列表
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(contentCid);
        //执行查询
        List<TbContent> list = tbContentMapper.selectByExample(example);

        //向缓存中添加内容
        try {
        //把list转换成字符串
            String cacheString = JsonUtils.objectToJson(list);
            //把从数据库查询到的结果list放到redis缓存中保存
            jedisClient.hset(INDEX_CONTENT_REDIS_KEY,contentCid +"",cacheString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
