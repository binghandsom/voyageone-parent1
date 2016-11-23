package com.voyageone.common.util;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 对 Excel 操作提供通用支持
 * <p>
 * Created by Jonas on 8/3/15.
 */
public final class ExcelUtils {

    /**
     * 从 row 的第 index 列，获取数字值
     *
     * @param row   目标行
     * @param index 目标列
     * @return 数字
     */
    public static Double getNum(Row row, int index) {
        Cell cell = row.getCell(index);

        if (cell == null) return null;

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                String value = cell.getStringCellValue();
                if (StringUtils.isEmpty(value)) return null;
                return Double.valueOf(value);
            default:
                return null;
        }
    }

    /**
     * 从 row 的第 index 列，获取字符串值
     *
     * @param row   目标行
     * @param index 目标列
     * @return 字符串值
     */
    public static String getString(Row row, int index) {
        return getString(row, index, null);
    }

    /**
     * 从 row 的第 index 列，获取字符串值
     *
     * @param row          目标行
     * @param index        目标列
     * @param numberFormat 对原值为数字时的，数字格式化格式
     * @return 字符串值
     */
    public static String getString(Row row, int index, String numberFormat) {
        Cell cell = row.getCell(index);
        return getString(cell, numberFormat);
    }

    /**
     * 从 Cell，获取字符串值
     * @return 字符串值
     */
    public static String getString(Cell cell) {
        return getString(cell, null);
    }
    /**
     * 从 Cell，获取字符串值
     * @param numberFormat 对原值为数字时的，数字格式化格式
     * @return 字符串值
     */
    public static String getString(Cell cell, String numberFormat) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                double val = cell.getNumericCellValue();
                if (numberFormat == null)
                    return String.valueOf(val);
                NumberFormat formatter = new DecimalFormat(numberFormat);
                return formatter.format(val);
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

    public static void setCellValue(Row row, int index, Object value, CellStyle cellStyle){
        Cell cell = row.getCell(index);

        if (cell == null) cell = row.createCell(index);

        if (cellStyle != null) cell.setCellStyle(cellStyle);

        if(value == null){
            value="";
        }
        if(value instanceof String){
            String temp = (String) value;
            if(temp.length() > SpreadsheetVersion.EXCEL2007.getMaxTextLength()) temp = temp.substring(0,SpreadsheetVersion.EXCEL2007.getMaxTextLength()-1);
            cell.setCellValue((String) value);
        }else if(value instanceof Double){
            cell.setCellValue((Double) value);
        }else if(value instanceof Date){
            cell.setCellValue((Date) value);
        }else if(value instanceof Calendar){
            cell.setCellValue((Calendar) value);
        }else if(value instanceof RichTextString){
            cell.setCellValue((RichTextString) value);
        }else if(value instanceof Boolean){
            cell.setCellValue((Boolean) value);
        }else if(value instanceof Integer){
            cell.setCellValue((Integer) value);
        }
    }

    /**
     * 功能：拷贝sheet
     * 实际调用 	copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true)
     * @param targetSheet
     * @param sourceSheet
     * @param targetWork
     * @param sourceWork
     */
    public static void copySheet(XSSFSheet targetSheet, XSSFSheet sourceSheet,
                                 XSSFWorkbook targetWork, XSSFWorkbook sourceWork) throws Exception{
        if(targetSheet == null || sourceSheet == null || targetWork == null || sourceWork == null){
            throw new IllegalArgumentException("调用PoiUtil.copySheet()方法时，targetSheet、sourceSheet、targetWork、sourceWork都不能为空，故抛出该异常！");
        }
        copySheet(targetSheet, sourceSheet, targetWork, sourceWork, true);
    }

    /**
     * 功能：拷贝sheet
     * @param targetSheet
     * @param sourceSheet
     * @param targetWork
     * @param sourceWork
     * @param copyStyle					boolean 是否拷贝样式
     */
    public static void copySheet(XSSFSheet targetSheet, XSSFSheet sourceSheet,
                                 XSSFWorkbook targetWork, XSSFWorkbook sourceWork, boolean copyStyle)throws Exception {

        if(targetSheet == null || sourceSheet == null || targetWork == null || sourceWork == null){
            throw new IllegalArgumentException("调用PoiUtil.copySheet()方法时，targetSheet、sourceSheet、targetWork、sourceWork都不能为空，故抛出该异常！");
        }

        //复制源表中的行
        int maxColumnNum = 0;

        Map styleMap = (copyStyle) ? new HashMap() : null;

        for (int i = sourceSheet.getFirstRowNum(); i <= sourceSheet.getLastRowNum(); i++) {
            XSSFRow sourceRow = sourceSheet.getRow(i);
            XSSFRow targetRow = targetSheet.createRow(i);

            if (sourceRow != null) {
                copyRow(targetRow, sourceRow,
                        targetWork, sourceWork, styleMap);
                if (sourceRow.getLastCellNum() > maxColumnNum) {
                    maxColumnNum = sourceRow.getLastCellNum();
                }
            }
        }

        //复制源表中的合并单元格
        mergerRegion(targetSheet, sourceSheet);

        //设置目标sheet的列宽
        for (int i = 0; i <= maxColumnNum; i++) {
            targetSheet.setColumnWidth(i, sourceSheet.getColumnWidth(i));
        }
    }

    /**
     * 功能：拷贝row
     * @param targetRow
     * @param sourceRow
     * @param styleMap
     * @param targetWork
     * @param sourceWork
     */
    public static void copyRow(XSSFRow targetRow, XSSFRow sourceRow,
                               XSSFWorkbook targetWork, XSSFWorkbook sourceWork, Map styleMap) throws Exception {
        if(targetRow == null || sourceRow == null || targetWork == null || sourceWork == null){
            throw new IllegalArgumentException("调用PoiUtil.copyRow()方法时，targetRow、sourceRow、targetWork、sourceWork、targetPatriarch都不能为空，故抛出该异常！");
        }

        //设置行高
        targetRow.setHeight(sourceRow.getHeight());

        for (int i = sourceRow.getFirstCellNum(); i <= sourceRow.getLastCellNum(); i++) {
            XSSFCell sourceCell = sourceRow.getCell(i);
            XSSFCell targetCell = targetRow.getCell(i);

            if (sourceCell != null) {
                if (targetCell == null) {
                    targetCell = targetRow.createCell(i);
                }

                //拷贝单元格，包括内容和样式
                copyCell(targetCell, sourceCell, targetWork, sourceWork, styleMap);
            }
        }
    }

    /**
     * 功能：拷贝cell，依据styleMap是否为空判断是否拷贝单元格样式
     * @param targetCell			不能为空
     * @param sourceCell			不能为空
     * @param targetWork			不能为空
     * @param sourceWork			不能为空
     * @param styleMap				可以为空
     */
    public static void copyCell(XSSFCell targetCell, XSSFCell sourceCell, XSSFWorkbook targetWork, XSSFWorkbook sourceWork,Map styleMap) {
        if(targetCell == null || sourceCell == null || targetWork == null || sourceWork == null ){
            throw new IllegalArgumentException("调用PoiUtil.copyCell()方法时，targetCell、sourceCell、targetWork、sourceWork都不能为空，故抛出该异常！");
        }

        //处理单元格样式
        if(styleMap != null){
            if (targetWork == sourceWork) {
                targetCell.setCellStyle(sourceCell.getCellStyle());
            } else {
                String stHashCode = "" + sourceCell.getCellStyle().hashCode();
                XSSFCellStyle targetCellStyle = (XSSFCellStyle) styleMap
                        .get(stHashCode);
                if (targetCellStyle == null) {
                    targetCellStyle = targetWork.createCellStyle();
                    targetCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
                    styleMap.put(stHashCode, targetCellStyle);
                }

                targetCell.setCellStyle(targetCellStyle);
            }
        }

        //处理单元格内容
        switch (sourceCell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                targetCell.setCellValue(sourceCell.getRichStringCellValue());
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                targetCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                targetCell.setCellErrorValue(sourceCell.getErrorCellValue());
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            default:
                break;
        }
    }

    /**
     * 功能：复制原有sheet的合并单元格到新创建的sheet
     *
     * @param targetSheet
     * @param sourceSheet
     */
    public static void mergerRegion(XSSFSheet targetSheet, XSSFSheet sourceSheet)throws Exception {
        if(targetSheet == null || sourceSheet == null){
            throw new IllegalArgumentException("调用PoiUtil.mergerRegion()方法时，targetSheet或者sourceSheet不能为空，故抛出该异常！");
        }

        for (int i = 0; i < sourceSheet.getNumMergedRegions(); i++) {
            CellRangeAddress oldRange = sourceSheet.getMergedRegion(i);
            CellRangeAddress newRange = new CellRangeAddress(
                    oldRange.getFirstRow(), oldRange.getLastRow(),
                    oldRange.getFirstColumn(), oldRange.getLastColumn());
            targetSheet.addMergedRegion(newRange);
        }
    }


}
