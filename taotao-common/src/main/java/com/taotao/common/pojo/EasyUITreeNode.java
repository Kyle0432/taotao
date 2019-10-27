package com.taotao.common.pojo;

/**
 * 接收参数parentId，根据parentId查询分类列表。返回pojo列表。
 * 通过pojo一个节点描述一个节点的格式,最后返回pojo列表
 *Pojo应该包含三个属性：id、text、state
 *应该放到taotao-common工程中
 * @author Kyle
 * @create 2019-05-16 17:53
 */
public class EasyUITreeNode {

    private long id;
    private String text;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
