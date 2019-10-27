package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

/**
 * 内容分类
 * @author Kyle
 * @create 2019-05-27 21:38
 */
public interface ContentCategoryService {

    List<EasyUITreeNode> getCategoryList(long parentId);

    TaotaoResult insertContentCategory(long parentId,String name);

    TaotaoResult deleteContentCategory(long parentId,long id);

    TaotaoResult updateContentCategory(Long id,String name);
}
