package com.voyageone.service.impl.cms.jumei.enumjm;

import com.voyageone.common.util.excel.EnumExcelColumnType;

/**
 * Created by dell on 2016/3/22.
 */
public interface EnumImport {
    public String getColumnName();
    public String getTableName();
    public int getOrderIndex();
    public String getText();
    public EnumExcelColumnType getColumnType();
    public double getColumnWidth();
    public  boolean isNull();
}
