package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.dao.report.BiReportSalesProduct010Dao;
import com.voyageone.service.dao.report.BiReportSalesShop010Dao;
import com.voyageone.service.daoext.report.BiReportSalesShop010DaoExt;
import com.voyageone.service.model.report.BiReportSalesShop010Model;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by dell on 2017/1/9.
 */
@Service
public class BiRepConsultService {

    @Autowired
    private BiReportSalesProduct010Dao biReportSalesProduct010Dao;
    @Autowired
    private BiRepSupport BiRepSupport;
    @Autowired
    private BiReportSalesShop010Dao biReportSalesShop010Dao;
    @Autowired
    private BiRepExcelFileCreator biRepExcelFileCreator;
    @Autowired
    private BiReportSalesShop010DaoExt biReportSalesShop010DaoExt;



    public Map<String, Object> biRepDownload(PageQueryParameters parameters) {
//        String channel=parameters.getParameterValue("channel");
//        String channelStart=parameters.getParameterValue("channelStart");
//        String channelEnd=parameters.getParameterValue("channelEnd");
//        Map<String, Object> result = new HashMap<>();
//        List<TestBean> testBeanList=new ArrayList<TestBean>();
//        for(int i=0;i<10;i++)
//        {
//            TestBean testBean=new TestBean("channel"+i,channel,channelStart,channelEnd);
//            testBeanList.add(testBean);
//        }
//        result.put("testBeanList",testBeanList);
//      /*  result.put("platformTypeList", TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
//        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));*/
//
//        return result;
        return null;
    }
    //create the bi_report xls file
    //first step:create the xls recognized fileName

    public byte[] createXLSFile()
    {
        //HSSFWorkbook workbook=BiRepSupport.exportExcel()
       /* Byte[] buffer=new Byte[10000];
        BufferedInputStream bufferedInputStream=new BufferedInputStream(buffer);*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        workbook.write(baos);

        /*// 测试数据
        BiRepSupport<Student> biRepSupport = new BiRepSupport<Student>();
        String[] headers = {"学号", "姓名", "年龄", "性别", "出生日期"};
        List<Student> dataset = new ArrayList<Student>();
        dataset.add(new Student(10000001, "张三", 20, true, new Date()));
        dataset.add(new Student(20000002, "李四", 24, false, new Date()));
        dataset.add(new Student(30000003, "王五", 22, true, new Date()));
        String[] headers2 = {"图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN",
                "图书出版社", "封面图片"};*/
//        HSSFWorkbook workbook=biRepSupport.exportExcel(headers2, dataset);
        Workbook workbook =biRepExcelFileCreator.createExcelFile();
      /*  try {
            workbook.write(baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            byte[] aa = baos.toByteArray();
            return aa;
        }
*/
        try {
            OutputStream out = new FileOutputStream("E://shopSample.xls");
//            OutputStream out2 = new FileOutputStream("E://test1.xls");
         /*   biRepSupport.exportExcel(headers, dataset, out);*/
              workbook.write(out);
//            biRepSupport.exportExcel(headers2, dataset2, out2);
            out.close();
            JOptionPane.showMessageDialog(null, "导出成功!");
            System.out.println("excel导出成功！");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("process end!");
        }
        return null;

    }
    public List<BiReportSalesShop010Model> getData() throws ParseException
    {
        //BiReportSalesProduct010Key key=new BiReportSalesProduct010Key();
//        Map
        List shop010ModelList=new ArrayList<BiReportSalesShop010Model>();
        BiReportSalesShop010Model biReportSalesShop010Model=null;
        Map <String,Object> map=new HashMap<>();


        map.put("shopId",20);
        Map <String,Object> map1=new HashMap<>();
//        map.put("")
//        Date date=new Date("2016-03-05");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date staDate=sdf.parse("2016-11-01");
        Date endDate=sdf.parse("2016-11-11");
        map1.put("staDate",staDate);
        map1.put("endDate",endDate);
//        List<?> shopSalesList=biReportSalesShop010DaoExt.selectListByDate(map1);
        System.out.println();
//        System.out.println(biReportSalesShop010Model.getShopId()+" "+biReportSalesShop010Model.getAmt()+biReportSalesShop010Model.getBuyers());

//        shop010ModelList=biReportSalesShop010Dao.selectList(map);
//        System.out.println(shop010ModelList.size());
       return shop010ModelList;
    }


}
