package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.util.FileUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dell on 2017/1/13.
 */
@Service
public class BiRepExcelFileCreator {
    /**
     * sheet开始描绘参数
     */
    private static Integer rowIndex=0;

    public static Integer getRowIndex() {
        return rowIndex;
    }

    public static void setRowIndex(Integer rowIndex) {
        BiRepExcelFileCreator.rowIndex = rowIndex;
    }
    /**
     * 获取这个workbook
     * @param dataset
     * @param <T>
     * @return
     */
    public <T> Workbook createExcelFile( Collection<T> dataset,List<HeaderInfo> mergeRangeList,String sheetName,String[] ... headers) {

        rowIndex=0;
        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet(sheetName);
        sheet.setDefaultColumnWidth((short) 30);
        //design the head;
        CellStyle h1style = book.createCellStyle();
        CellStyle rateStyle = book.createCellStyle();
        CellStyle h3style = book.createCellStyle();
        CellStyle basicstyle = book.createCellStyle();
        XSSFCellStyle h1xCellStyle = (XSSFCellStyle) h1style;
        XSSFCellStyle xRateStyle = (XSSFCellStyle) rateStyle;
        XSSFCellStyle h3xCellStyle = (XSSFCellStyle) h3style;
        XSSFCellStyle bxCellStyle = (XSSFCellStyle) basicstyle;
        //设置h1 cellstyle
        h1xCellStyle=CellStyleFactory.getHeaderStyle(h1xCellStyle);
        bxCellStyle=CellStyleFactory.getThoSepStyle(bxCellStyle);

        for(String[] header: headers)
        {
            writeHead(sheet, header, h1xCellStyle);
        }
        int colunmNum=sheet.getRow(rowIndex-1).getLastCellNum();
        for(short k=0;k<colunmNum;k++)
        {
            String dataValue=sheet.getRow(rowIndex-1).getCell(k).getStringCellValue();
            sheet.setColumnWidth(k,dataValue.getBytes().length*2*256);
        }
       /*
        sheet.setDefaultColumnWidth((short) 30);
        writeHead(sheet, headers1, h1xCellStyle);
        writeHead(sheet, headers2, h1xCellStyle);
        writeHead(sheet, headers3, h1xCellStyle);*/
        writeData(dataset, "yyyy-MM-dd", bxCellStyle, sheet, rowIndex-1);
        RangeMerge(sheet,mergeRangeList,h1xCellStyle);
        return book;
    }
    /**
     * @sheet 所在的sheet
     * String [] headers ，headers数据
     * 设置开头，默认类型是String
     * **/
    public void writeHead(Sheet sheet,String[] headers,CellStyle style) {
//        book.createSheet("header1");
//        Sheet sheet=book.getSheetAt(1);
        Row row1= FileUtils.row(sheet,rowIndex++);
        for(int cellIndex=0;cellIndex<headers.length;cellIndex++)
        {
            FileUtils.cell(row1,cellIndex,style).setCellValue(headers[cellIndex]);
        }
    }

    /**
     * 写入数据
     * @param dataset
     * @param pattern
     * @param style
     * @param sheet
     * @param startrow
     * @param <T>
     */
      public <T> void  writeData(Collection<T> dataset, String pattern,CellStyle style,Sheet sheet,int startrow) {

//          Sheet sheet=workbook.getSheet(sheetName);
          Iterator<T> it = dataset.iterator();
          int index =startrow;
          while (it.hasNext()) {
              index++;
              Row row = sheet.createRow(index);
              T t = (T) it.next();
              //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
              Field[] fields = t.getClass().getDeclaredFields();
              for (short i = 0; i < fields.length; i++) {

                  Field field = fields[i];
                  String fieldName = field.getName();
                  String getMethodName = "get"
                          + fieldName.substring(0, 1).toUpperCase()
                          + fieldName.substring(1);
                  try {
                      Class tCls = t.getClass();
                      Method getMethod = tCls.getMethod(getMethodName,
                              new Class[]{});
                      Object value = getMethod.invoke(t, new Object[]{});
                      //判断值的类型后进行强制类型转换
                      String textValue = null;
                      if(value==null)
                      {
                          XSSFRichTextString richString = new XSSFRichTextString(textValue);
                          FileUtils.cell(row,i,style).setCellValue(richString);
                      }
                 else if (value instanceof Integer) {
                     int intValue = (Integer) value;
                      FileUtils.cell(row,i,style).setCellValue(intValue);
                  } else if (value instanceof Float) {
                     float fValue = (Float) value;
                     textValue = new XSSFRichTextString(
                           String.valueOf(fValue)).toString();
                      FileUtils.cell(row,i,style).setCellValue(textValue);
                  } else if (value instanceof Double) {
                     double dValue = (Double) value;
                     textValue = new XSSFRichTextString(
                           String.valueOf(dValue)).toString();
                      FileUtils.cell(row,i,style).setCellValue(textValue);
                  } else if (value instanceof Long) {
                     long longValue = (Long) value;
                      FileUtils.cell(row,i,style).setCellValue(longValue);
                  }
                 else if (value instanceof Boolean) {
                      boolean bValue = (Boolean) value;
                      textValue = "男";
                      if (!bValue) {
                          textValue = "女";
                      }
                  } else if (value instanceof Date) {
                      Date date = (Date) value;
                      SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                      textValue = sdf.format(date);
                  } else if (value instanceof BigDecimal)
                  {
                      BigDecimal bgValue=(BigDecimal)value;
                      BigDecimal nbgValue=bgValue.setScale(2,BigDecimal.ROUND_HALF_UP);
                      FileUtils.cell(row,i,style).setCellValue(nbgValue.doubleValue());
                  }
                      else
                  {
                      //其它数据类型都当作字符串简单处理
                      textValue = value.toString();
                  }
                  //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                  if (textValue != null) {
                      Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                      Matcher matcher = p.matcher(textValue);
                      if (matcher.matches()) {
                          //是数字当作double处理
                          FileUtils.cell(row,i,style).setCellValue(textValue);
                      } else {
                          XSSFRichTextString richString = new XSSFRichTextString(textValue);
                          FileUtils.cell(row,i,style).setCellValue(richString);
                      }
                  }
              } catch (SecurityException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (NoSuchMethodException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (IllegalArgumentException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (IllegalAccessException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } catch (InvocationTargetException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              } finally {
                  //清理资源
              }
          }

      }
  }

    /**
     * 设置合并单元格
     * @param sheet
     * @param mergeRangeList
     * @param h1xCellStyle
     */
  public void RangeMerge(Sheet sheet, List<HeaderInfo> mergeRangeList,XSSFCellStyle h1xCellStyle)
  {
      Iterator it = mergeRangeList.iterator();
      while (it.hasNext()) {
          HeaderInfo headerInfo = (HeaderInfo) it.next();
          CellRangeAddress address = headerInfo.getAddress();
          if (address != null) {
              setRegionStyle(sheet,address,h1xCellStyle);
             /* Row row = sheet.getRow(address.getFirstRow());
              Cell cell = row.getCell(address.getFirstColumn());
              if (cell == null) {
                  cell = row.createCell(address.getFirstColumn());
              }*/
              sheet.getRow(address.getFirstRow()).getCell(address.getFirstColumn()).setCellValue(headerInfo.getHeaderName());
//              cell.setCellStyle(h1xCellStyle);
              sheet.addMergedRegion(address);//merge the cell

          } else {
              System.out.println("addrees is null");
          }
      }
  }
  /**
   设置合并单元，避免没有边框的情形
   */
    @SuppressWarnings("deprecation")
    public static void setRegionStyle(Sheet sheet,CellRangeAddress address, XSSFCellStyle cs) {
            for(int i=address.getFirstRow();i<=address.getLastRow();i++)
            {
                for(int j=address.getFirstColumn();j<=address.getLastColumn();j++)
                {
                    Row rRow=sheet.getRow(i);
                    Cell rCell=rRow.getCell(j);
                    if(rCell==null)
                    {
                        rCell=rRow.createCell(j);
                        rCell.setCellStyle(cs);
                    }
                }
            }
    }
}
