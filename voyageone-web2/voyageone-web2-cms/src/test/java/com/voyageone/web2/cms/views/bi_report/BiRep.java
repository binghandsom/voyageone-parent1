package com.voyageone.web2.cms.views.bi_report;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Created by dell on 2017/1/10.
 */
public class BiRep<T>{

    private static int rowIndex=0;
    public void exportExcel(String[] headers1,String[] headers2,String[] headers3,Collection<T> dataset, OutputStream out) {
        exportExcel( null,headers1, headers2,headers3,dataset,  out,null);
    }
   /* String explain="test poi output excel document";
    public void exportExcel(String bigTitle,Collection<T> dataset, OutputStream out) {
        exportExcel( null,headers1, headers2,headers3,dataset,  out,null);
    }

    public void exportExcel(String bigTitle,String[] headers, Collection<T> dataset,
                            OutputStream out) {
        exportExcel( null,headers1, headers2,headers3,dataset,  out,null);
    }*/

    public void exportExcel(String headers1,String[] headers2,String[] headers3, Collection<T> dataset,
                            OutputStream out, String pattern) {
        exportExcel( headers1, headers2,headers3,dataset,  out,pattern);
    }
    public void exportExcel(String SheetTitle,String[] headers1, String[] headers2,String[] headers3,Collection<T> dataset, OutputStream out, String pattern)
    {
        //declaim a woorkbook
        HSSFWorkbook workbook=new HSSFWorkbook();
        //create a new sheet
        HSSFSheet sheet=workbook.createSheet(SheetTitle);
        //set the sheet default column width via character
        sheet.setDefaultColumnWidth((short)15);
        //create a sheet style
        HSSFCellStyle style=workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index); //set the filling foregroundColor
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// what is fill pattern.
        //design the cell border
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);// BORDERBOTTEM
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //create a font style
        HSSFFont font=workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //style set the font
        style.setFont(font);

        //create and set another style
        HSSFCellStyle style2=workbook.createCellStyle();
        //set the bgground color
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //SET the border
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        //CREATE 3rd style
        //create and set another style
        HSSFCellStyle style3=workbook.createCellStyle();
        //set the bgground color
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //SET the border
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);


        //CREATE ANOTHER FONT
        HSSFFont font2=workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        style2.setFont(font2);

        //declaim a top manager painter, what is this?
     /*   HSSFPatriarch patriarch=sheet.createDrawingPatriarch();
        //define comment location nd weight
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        //design the comment context
        comment.setString(new HSSFRichTextString("you can comment in the poi"));

        comment.setAuthor("leno");*/
        //create table header rows

        //Field [] fields=
        HSSFRow headers1Row=sheet.createRow(rowIndex);
        HSSFCell headers1Cell=headers1Row.createCell(rowIndex);
        headers1Cell.setCellStyle(style);
        headers1Cell.setCellValue(headers1[0]);

       HSSFRow headers2Row=sheet.createRow(1);
        for(int i=0;i<headers2.length;i++)
        {
            HSSFCell headers2Cell=headers2Row.createCell(i+3);
            headers2Cell.setCellStyle(style2);
            headers2Cell.setCellValue(headers2[i]);
        }
        //HSSF




        /*
        HSSFRow bigTitlerow=sheet.createRow(0);
        HSSFCell bigTitlecell=bigTitlerow.createCell(0);
        bigTitlecell.setCellStyle(style);
        bigTitlecell.setCellValue(headers1[0]);
        HSSFRow row=sheet.createRow(1);
        for(short i=0;i<headers2.length;i++)
        {
            HSSFCell cell=row.createCell(i);
            cell.setCellStyle(style);
            HSSFRichTextString text= new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //go through  dataSet and create row
        Iterator<T> it=dataset.iterator();
        int index=0;
        while(it.hasNext())
        {
            index++;
            row=sheet.createRow(index);
            T t = (T) it.next();
            //use the refelction and according to the javabean attri sort to get the attr value by method getter();
            Field [] fields=t.getClass().getDeclaredFields();
            for(short i=0;i<fields.length;i++)
            {
                //create cell for the attr
                HSSFCell cell=row.createCell(i);
                cell.setCellStyle(style2);
                Field field=fields[i];
                String fieldName=field.getName();
                String getMethodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);

                try {
                    Class tCls=t.getClass();
                    Method getMethod = tCls.getMethod(getMethodName,new Class[]{});
                    Object value=getMethod.invoke(t,new Object[]{});
                    String textValue=null;
                    if(value instanceof Boolean)
                    {
                        boolean bValue=(Boolean) value;
                        textValue="man";
                        if(!bValue)
                        {
                            textValue="woman";
                        }
                    } else if (value instanceof Date) {
                        Date date=(Date) value;
                        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
                        textValue=sdf.format(date);
                    } else if (value instanceof  byte[]) {
                       row.setHeightInPoints(60);
                        sheet.setColumnWidth(i,(short)(35.7*80));
                        byte[] bsValue=(byte[])value;
                        HSSFClientAnchor anchor=new HSSFClientAnchor(0,0,1023,255,(short)6,index,(short)6,index);
                        anchor.setAnchorType(2);
                        patriarch.createPicture(anchor,workbook.addPicture(bsValue,HSSFWorkbook.PICTURE_TYPE_JPEG));
                    }
                    else
                    {
                        textValue=value.toString();
                    }
                    if (textValue!=null)
                    {
                        Pattern p=Pattern.compile("^//d//d+?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            //是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                finally
                {

                }

            }
        }
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,headers.length-1));//merge the cell
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static void main(String[] args)
    {
        //set the headers value
        String [] header1={"SKU 日报"};
        Map attrMap=new HashMap<String,String>();
        String [] header2={"销售指标","运营指标","属性"};
        String [] headers3_cn={"商品ID","商品名称","商品Code","SKU","销售额","销售量","买家数","人居成交件数","客单价","PV","UV","转化率","加购件数","收藏人数","页面停留时间","跳失率","访客价值","收藏价值","类目","Branch","Color","Origin","Material","Weight","Size"};
        //set the dataset
        List<BiReportModel> biRepList=new ArrayList<BiReportModel>();
        biRepList.add(new BiReportModel(327242940d,"美国原装进口Archer Farms燕麦片340g 营养早餐谷物冲饮 多种口味","112233","aaa",1000,20f,8,2f,7.89f,22506,8345,12.43f,404,429f,48.00f,61.92f,"0.98","食品","amazon","green","amazon",4,5));
        biRepList.add(new BiReportModel(327242934d,"南非原装进口Archer Farms燕麦片560g 营养早餐谷物冲饮 多种口味","112233","aaa",1000,20f,8,2f,7.89f,22506,8345,12.43f,404,429f,48.00f,61.92f,"0.98","食品","amazon","green","amazon",4,5));

        //create the excel xls file;
        BiRep<BiReportModel> bip1=new BiRep<BiReportModel>();
        bip1.exportExcel(header1,header2,headers3_cn,biRepList,null);



      /*  String[] bigTitle={"学生表","书籍表"};
        BiRep<Student> biRep1 = new BiRep<Student>();
        String[] headers = {"学号", "姓名", "年龄", "性别", "出生日期"};
        List<Student> dataset = new ArrayList<Student>();
        dataset.add(new Student(10000001, "张三", 20, true, new Date()));
        dataset.add(new Student(20000002, "李四", 24, false, new Date()));
        dataset.add(new Student(30000003, "王五", 22, true, new Date()));*/
        BiRep<Book> biRep = new BiRep<Book>();
        String[] headers2 = {"图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN",
                "图书出版社", "封面图片"};
        List<Book> dataset2 = new ArrayList<Book>();
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream("voyageone-static/2.0/develop/static/img/01.jpg"));
            byte[] buf = new byte[bis.available()];
            while ((bis.read(buf)) != -1) {
                //
            }
            dataset2.add(new Book(1, "jsp", "leno", 300.33f, "1234567", "清华出版社", buf));
            dataset2.add(new Book(2, "java编程思想", "brucl", 300.33f, "1234567", "阳光出版社", buf));
            dataset2.add(new Book(3, "DOM艺术", "lenotang", 300.33f, "1234567", "清华出版社", buf));
            dataset2.add(new Book(4, "c++经典", "leno", 400.33f, "1234567", "清华出版社", buf));
            dataset2.add(new Book(5, "c#入门", "leno", 300.33f, "1234567", "汤春秀出版社", buf));

            OutputStream out = new FileOutputStream("E://book.xls");
            OutputStream out2 = new FileOutputStream("E://student.xls");
          /*  biRep1.exportExcel(bigTitle[1],headers, dataset, out);*/
           // biRep.exportExcel(bigTitle[2],headers2, dataset2, out2);
            out.close();
            JOptionPane.showMessageDialog(null, "导出成功!");
            System.out.println("excel导出成功！");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
