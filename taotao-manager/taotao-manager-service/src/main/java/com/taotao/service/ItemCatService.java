package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * @author Kyle
 * @create 2019-05-16 18:01
 */
public interface ItemCatService {

    List<EasyUITreeNode>  getItemCatList(long parentId);
}
