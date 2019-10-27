package com.taotao.rest.service;

import com.taotao.common.pojo.TaotaoResult;

/**
 * @author Kyle
 * @create 2019-07-17 23:00
 */
public interface RedisService {

    TaotaoResult syncContent(long contentCid);
}
