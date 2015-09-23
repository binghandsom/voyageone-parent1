package com.voyageone.core.ajax.dt;

/**
 * 对应 datatables 的列定义参数
 * <p>
 * Created by Jonas on 7/21/15.
 */
public class DtColumn {
    private String data;

    private String name;

    private boolean searchable;

    private boolean orderable;

    private DtSearch search;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public DtSearch getSearch() {
        return search;
    }

    public void setSearch(DtSearch search) {
        this.search = search;
    }
}
