package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import com.taotao.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Kyle
 * @create 2019-05-14 21:25
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public TbItem getItemById(Long itemId) {
        //方法一:
        TbItem tbItem =  tbItemMapper.selectByPrimaryKey(itemId);
        return tbItem;

/*        //方法二:根据条件来查询
        TbItemExample example = new TbItemExample();
        //添加查询条件
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = tbItemMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            TbItem tbItem = list.get(0);
            return tbItem;
        }
        return  null;*/

    }

    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //分页处理
        PageHelper.startPage(page,rows);
        //执行查询,并且是不带条件
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> list = this.tbItemMapper.selectByExample(tbItemExample);
        //取分页信息,把list进行包装,之后就可以调用一些扩展方法
        PageInfo<TbItem> pageInfo =  new PageInfo<TbItem>(list);

        //返回处理结果
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }


    @Override
    public TaotaoResult createItem(TbItem tbItem,String desc,String itemParam) throws Exception {
        //首先补全item
        //生成商品ID,通过IDUtils工具
        long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        //设置商品的状态 1-正常, 2-下架, 3-删除
        tbItem.setStatus((byte) 1);
        //设置时间
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        //把商品信息插入到数据库
        tbItemMapper.insertSelective(tbItem);
        //添加商品描述,只能在同一个service方法里面调用,
        // 这样才是同一个事务,否则在controller调用就是两个事务了
        //主要调用insertItemDesc方法是为了执行createItem方法时也同步insertItemDesc方法
        TaotaoResult result = insertItemDesc(itemId, desc);
        if(result.getStatus() != 200){
            throw new Exception();
        }
        TaotaoResult result1 = insertItemParamItem(itemId, itemParam);
        if(result1.getStatus() != 200){
            throw new Exception();
        }
        return TaotaoResult.ok();
    }

    /**
     * 添加商品描述
     * @param itemId
     * @param desc
     * @return
     */
    private TaotaoResult insertItemDesc(Long itemId,String desc){
        //创建pojo
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDescMapper.insertSelective(tbItemDesc);
        return TaotaoResult.ok();
    }

    /**
     * 添加规格参数
     * @param itemId
     * @param itemParam
     * @return
     */
    private TaotaoResult insertItemParamItem(Long itemId,String itemParam){
       //创建pojo
        TbItemParamItem tbItemParamItem = new TbItemParamItem();
        tbItemParamItem.setItemId(itemId);
        tbItemParamItem.setParamData(itemParam);
        tbItemParamItem.setCreated(new Date());
        tbItemParamItem.setUpdated(new Date());
        //向表中插入数据
        tbItemParamItemMapper.insertSelective(tbItemParamItem);
        return TaotaoResult.ok();
    }

}
