package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理
 * @author Kyle
 * @create 2019-05-27 21:41
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    /**
     * 显示节点列表
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUITreeNode> getCategoryList(long parentId) {
        //根据parentId来查询节点列表
        TbContentCategoryExample tbContentCategoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = tbContentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(tbContentCategoryExample);
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
           //创建一个节点
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(tbContentCategory.getId());
            easyUITreeNode.setText(tbContentCategory.getName());
            easyUITreeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }

    /**
     * 添加节点
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public TaotaoResult insertContentCategory(long parentId, String name) {
        //  创建一个pojo
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setName(name);
        tbContentCategory.setIsParent(false);
        //状态.可选值:1(正常) ,2(删除)
        tbContentCategory.setStatus(1);
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //添加记录,此时插入完了主键会自动返回,在这个pojo就会有值了
        // 因为在mapper里面设置了主键返回信息
        tbContentCategoryMapper.insertSelective(tbContentCategory);
        //查看父节点的isParent列是否为true,如果不为true就改为true
        TbContentCategory parentCat = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        //判断是否为true
        if(!parentCat.getIsParent()){
             parentCat.setIsParent(true);
             //更新父节点
            tbContentCategoryMapper.updateByPrimaryKey(parentCat);
        }
        //返回结果,还带data  因为需要tbContentCategory里的主键
        return  TaotaoResult.ok(tbContentCategory);
    }

    /**
     * 删除节点
     * @param parentId
     * @param id
     * @return
     */
    @Override
    public TaotaoResult deleteContentCategory(long parentId, long id) {

        TbContentCategory contentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!contentCategory.getIsParent()){
            contentCategory.setIsParent(false);
            tbContentCategoryMapper.deleteByPrimaryKey(id);
        }
        return TaotaoResult.ok();
    }

    /**
     * 更新节点
     * @param id
     * @param name
     * @return
     */
    @Override
    public TaotaoResult updateContentCategory(Long id, String name) {
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        tbContentCategory.setUpdated(new Date());
        tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        return TaotaoResult.ok();

    }
}
