package com.voyageone.web2.cms.views.biReport.consult;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.awt.*;

/**
 * Created by dell on 2017/1/20.
 */
public class CellStyleFactory {
//    private static XSSFCellStyle cellStyle;
    /**
     * 设置外部来的cellStyle;
     *//*
    private CellStyle cellStyle;

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }
    public CellStyleFactory(CellStyle cellStyle)
    {
        this.cellStyle=cellStyle;
    }*/

    public static XSSFCellStyle getCommonStyle(XSSFCellStyle cellStyle)
    {
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    /**
     * 获取标题cellstyle
     * @param cellStyle
     * @return
     */
    public static XSSFCellStyle getHeaderStyle(XSSFCellStyle cellStyle)
    {
        cellStyle=getCommonStyle(cellStyle);
        cellStyle.setFillForegroundColor(new XSSFColor(new Color(250, 120, 119)));
        cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        return cellStyle;
    }

    /**
     * 获取千位分割符
     * @param cellStyle
     * @return
     */
     public static XSSFCellStyle getThoSepStyle(XSSFCellStyle cellStyle)
     {
         getCommonStyle(cellStyle);
         cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
         return cellStyle;
     }

    /**
     * 获取百分位格式
     * @param cellStyle
     * @return
     */
     public static XSSFCellStyle getPercentStyle(XSSFCellStyle cellStyle)
     {
         getCommonStyle(cellStyle);
         cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00%"));
         return cellStyle;
     }

    /**
     * 获取日期格式风格
     * @param cellStyle
     * @return
     */

    public static XSSFCellStyle getDateStyle(XSSFCellStyle cellStyle)
    {
        getCommonStyle(cellStyle);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy-MM-dd"));
        return cellStyle;
    }
}
