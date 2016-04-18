package com.voyageone.common.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
        }

        return null;
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
        }
        return null;
    }
}
