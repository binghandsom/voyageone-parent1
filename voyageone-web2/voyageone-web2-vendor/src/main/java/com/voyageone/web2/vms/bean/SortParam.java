package com.voyageone.web2.vms.bean;

import com.github.miemiedev.mybatis.paginator.domain.Order;

/**
 * 排序结构
 * Created by vantis on 16-7-13.
 */
public class SortParam {

    private String columnName;
    private Order.Direction direction;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Order.Direction getDirection() {
        return direction;
    }

    public void setDirection(Order.Direction direction) {
        this.direction = direction;
    }
}
