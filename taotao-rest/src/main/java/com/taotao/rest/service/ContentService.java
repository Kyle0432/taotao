package com.taotao.rest.service;

import com.taotao.pojo.TbContent;

import java.util.List;

/**
 * @author Kyle
 * @create 2019-05-28 22:15
 */
public interface ContentService {

    List<TbContent> getContentList(long contentCid);
}
