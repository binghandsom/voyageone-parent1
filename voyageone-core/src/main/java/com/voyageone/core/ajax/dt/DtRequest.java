package com.voyageone.core.ajax.dt;

import java.util.List;

/**
 * 对应 DataTable 的传入参数
 * <p>
 * Created by Jonas on 7/21/15.
 */
public class DtRequest<T> {
    private int draw;

    private int start;

    private int length;

    private List<DtOrder> order;

    private DtSearch search;

    private List<DtColumn> columns;

    private T param;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<DtOrder> getOrder() {
        return order;
    }

    public void setOrder(List<DtOrder> order) {
        this.order = order;
    }

    public DtSearch getSearch() {
        return search;
    }

    public void setSearch(DtSearch search) {
        this.search = search;
    }

    public List<DtColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DtColumn> columns) {
        this.columns = columns;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }
}
