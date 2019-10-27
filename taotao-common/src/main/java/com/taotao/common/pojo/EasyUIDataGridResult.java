package com.taotao.common.pojo;

import java.util.List;

/**
 * 因为其他工程也会用到这个pojo,所以放在common里面
 * @author Kyle
 * @create 2019-05-15 18:03
 */
public class EasyUIDataGridResult {
    private long total;
    //表示泛型为任意类型
    private List<?> rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
