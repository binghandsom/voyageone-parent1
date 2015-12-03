package com.voyageone.core.ajax.dt;

/**
 * 对应 datatables 的排序参数
 * <p>
 * Created by Jonas on 7/21/15.
 */
public class DtOrder {
    private int column;

    private String dir;

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
