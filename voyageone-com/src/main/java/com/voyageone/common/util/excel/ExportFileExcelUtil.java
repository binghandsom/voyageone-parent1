package com.voyageone.common.util.excel;

import com.voyageone.common.util.DateTimeUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.SheetUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2015/10/9.
 */
public class ExportFileExcelUtil {

    public static <T> void exportExcel(String filePath, ExportExcelInfo<T>... infolist) throws IOException, ExcelException {
        FileOutputStream fileout = new FileOutputStream(filePath);
        exportExcel(Arrays.asList(infolist), fileout);
        fileout.flush();
        fileout.close();
    }

    /**
     * @param listInfo 类的英文属性和Excel中的中文列名的对应关系
     *                 如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
     *                 如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
     *                 fieldMap.put("college.collegeName","学院名称")
     * @param out      导出流
     * @throws ExcelException
     * @MethodName : listToExcel
     * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
     */
    private static <T> void exportExcel(
            List<ExportExcelInfo<T>> listInfo,
            OutputStream out
    ) throws ExcelException {
        int sheetSize = 65535;
        //创建工作簿并发送到OutputStream指定的地方
        HSSFWorkbook wwb = null;
        try {
            wwb = new HSSFWorkbook();
            //因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
            //所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
            //1.计算一共有多少个工作表
            for (ExportExcelInfo<T> info : listInfo) {
                exportExcel(info, sheetSize, wwb);
            }
            //2.创建相应的工作表，并向其中填充数据
            wwb.write(out);
        } catch (Exception e) {
            e.printStackTrace();
            //如果是ExcelException，则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;

                //否则将其它异常包装成ExcelException再抛出
            } else {
                throw new ExcelException("导出Excel失败");
            }
        } finally {
            if (wwb != null) {
                try {
                    wwb.close();
                } catch (IOException ignored) {
                }
            }
        }

    }

    private static <T> void exportExcel(ExportExcelInfo<T> info, int sheetSize, HSSFWorkbook wwb) throws Exception {
        int sheetNum = 0;
        if (info.getDataSource() != null) {
            sheetNum = (info.getDataSource().size() - 1) / sheetSize;
        }
        //获取各工作表的数据源
        if (sheetNum > 0) {
            List<List<T>> pageList = ListHelp.getPageList(info.getDataSource(), sheetSize);
            int sheetIndex = 0;
            for (List<T> page : pageList) {
                HSSFSheet sheet = wwb.createSheet(info.getSheet() + sheetIndex++);//sheetName, i
                fillSheet(sheet, page, info);
            }
        } else {
            HSSFSheet sheet = wwb.createSheet(info.getSheet());//sheetName, i
            fillSheet(sheet, info.getDataSource(), info);
        }
    }


    /**
     * @param listInfo 类的英文属性和Excel中的中文列名的对应关系
     * @param response 使用response可以导出到浏览器
     * @throws ExcelException
     * @MethodName : listToExcel
     * @Description : 导出Excel（导出到浏览器，可以自定义工作表的大小）
     */
    public static <T> void exportExcel(
            HttpServletResponse response, ExportExcelInfo<Map<String, Object>>... listInfo
    ) throws ExcelException, UnsupportedEncodingException {
        //设置默认文件名为当前时间：年月日时分秒
        String fileName = listInfo[0].getFileName() + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
        //设置response头信息
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");        //改成输出excel文件
        response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName + ".xls", "utf-8"));
        //创建工作簿并发送到浏览器
        try {
            OutputStream out = response.getOutputStream();
            exportExcel(Arrays.asList(listInfo), out);
        } catch (Exception e) {
            e.printStackTrace();

            //如果是ExcelException，则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;

                //否则将其它异常包装成ExcelException再抛出
            } else {
                throw new ExcelException("导出Excel失败");
            }
        }
    }

    /**
     * @param sheet 工作表
     * @param list  数据源
     * @param info  中英文字段对应关系的Map
     * @MethodName : fillSheet
     * @Description : 向工作表中填充数据
     */
    private static <T> void fillSheet(
            HSSFSheet sheet,
            List<T> list,
            ExportExcelInfo<T> info

    ) throws Exception {
        List<ExcelColumn<T>> listColumn = info.getListColumn();
        int rowNo = 0;
        HSSFRow hssfRow = sheet.createRow(rowNo++);
        //填充表头
        for (int i = 0; i < listColumn.size(); i++) {
            // Label label=new Label(i, 0, cnFields[i]);
            HSSFCell xh = hssfRow.createCell(i);
            xh.setCellValue(listColumn.get(i).getText());
        }
        if (info.isDisplayColumnName()) {
            hssfRow = sheet.createRow(rowNo++);
            for (int i = 0; i < listColumn.size(); i++) {
                // Label label=new Label(i, 0, cnFields[i]);
                HSSFCell xh = hssfRow.createCell(i);
                xh.setCellValue(listColumn.get(i).getCamelColumnName());
            }
        }
        if (list != null) {
            HSSFWorkbook wwb = sheet.getWorkbook();
            ExcelColumn column;
            //填充内容

            for (T item : list) {
                //获取单个对象
                hssfRow = sheet.createRow(rowNo);
                for (int i = 0; i < listColumn.size(); i++) {
                    column = listColumn.get(i);
                    Object objValue = ((Map<String, Object>) item).get(column.getCamelColumnName());
                    ;
                    if (column.getFormatter() != null) {
                        objValue = column.getFormatter().apply(objValue, item, rowNo);
                        ;
                    }
                    String fieldValue = objValue == null ? "" : objValue.toString();
                    HSSFCell xh = hssfRow.createCell(i);
                    setCellValue(column, objValue, fieldValue, xh);//设置单元格的值
                }
                rowNo++;
            }
        }
        for (int i = 0; i < listColumn.size(); i++) {
            //没有设置列宽就设置为自动列宽
            setColumnAutoSize(sheet, i, listColumn.get(i).getColumnWidth());
        }
    }

    private static void setCellValue(ExcelColumn column, Object objValue, String fieldValue, HSSFCell xh) throws ParseException {
        if (column.getColumnType() == EnumExcelColumnType.ColumnType_Double) {
            if (objValue == null) {
                xh.setCellValue(0);
            } else {
                xh.setCellValue(Double.parseDouble(objValue.toString()));
            }
            xh.setCellType(Cell.CELL_TYPE_NUMERIC);
        } else if (column.getColumnType() == EnumExcelColumnType.ColumnType__BOOLEAN) {
            if (objValue == null) {
                xh.setCellValue(false);
            } else {
                xh.setCellValue(Boolean.parseBoolean(objValue.toString()));
            }
            xh.setCellType(Cell.CELL_TYPE_BOOLEAN);
        } else if (column.getColumnType() == EnumExcelColumnType.ColumnType_Date) {
            if (objValue == null) {
                xh.setCellValue(DateTimeUtil.getCreatedDefaultDate());
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                xh.setCellValue(sdf.parse(objValue.toString()));
                xh.setCellType(Cell.CELL_TYPE_FORMULA);
            }
        } else {
            xh.setCellValue(fieldValue);
            xh.setCellType(Cell.CELL_TYPE_STRING);
        }
    }

    /**
     * @param ws
     * @MethodName : setColumnAutoSize
     * @Description : 设置工作表自动列宽和首行加粗
     */
    private static void setColumnAutoSize(HSSFSheet ws, int columnIndex, double ColumnWidth) {
        double width = 0;
        if (ColumnWidth == 0) {
            width = SheetUtil.getColumnWidth(ws, columnIndex, false);
            width = width + (double) 2;
        } else {
            width = ColumnWidth;
        }
        if (width != -1) {
            width *= 256;
            int maxColumnWidth = 255 * 256; // The maximum column width for an individual cell is 255 characters
            if (width > maxColumnWidth) {
                width = maxColumnWidth;
            }
            ws.setColumnWidth(columnIndex, (int) (width));
        }
    }

    /**
     * @param ws
     * @MethodName : setColumnAutoSize
     * @Description : 设置工作表自动列宽和首行加粗
     */
    private static void setAllColumnAutoSize(HSSFSheet ws) {
        //获取本列的最宽单元格的宽度
        int columns = ws.getRow(0).getPhysicalNumberOfCells();
        for (int column = 0; column < columns; column++) {
            double width = SheetUtil.getColumnWidth(ws, column, false);
            width = width + (double) 2;
            if (width != -1) {
                width *= 256;
                int maxColumnWidth = 255 * 256; // The maximum column width for an individual cell is 255 characters
                if (width > maxColumnWidth) {
                    width = maxColumnWidth;
                }
                ws.setColumnWidth(column, (int) (width));
            }
        }
    }
}
