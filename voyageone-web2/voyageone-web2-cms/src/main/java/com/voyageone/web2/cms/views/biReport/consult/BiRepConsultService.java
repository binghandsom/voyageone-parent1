package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.common.configs.beans.TestBean;
import com.voyageone.service.dao.cms.BiReportSalesProduct010Dao;
import com.voyageone.service.model.cms.BiReportSalesProduct010Model;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public Map<String, Object> biRepDownload(PageQueryParameters parameters) {
        String channel=parameters.getParameterValue("channel");
        String channelStart=parameters.getParameterValue("channelStart");
        String channelEnd=parameters.getParameterValue("channelEnd");
        Map<String, Object> result = new HashMap<>();
        List<TestBean> testBeanList=new ArrayList<TestBean>();
        for(int i=0;i<10;i++)
        {
            TestBean testBean=new TestBean("channel"+i,channel,channelStart,channelEnd);
            testBeanList.add(testBean);
        }
        result.put("testBeanList",testBeanList);
      /*  result.put("platformTypeList", TypeChannels.getTypeListSkuCarts(channelId, Constants.comMtTypeChannel.SKU_CARTS_53_A, language));
        result.put("promotionStatus", TypeConfigEnums.MastType.promotionStatus.getList(language));*/

        return result;
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

        // 测试数据
        BiRepSupport<Student> biRepSupport = new BiRepSupport<Student>();
        String[] headers = {"学号", "姓名", "年龄", "性别", "出生日期"};
        List<Student> dataset = new ArrayList<Student>();
        dataset.add(new Student(10000001, "张三", 20, true, new Date()));
        dataset.add(new Student(20000002, "李四", 24, false, new Date()));
        dataset.add(new Student(30000003, "王五", 22, true, new Date()));
        String[] headers2 = {"图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN",
                "图书出版社", "封面图片"};
        HSSFWorkbook workbook=biRepSupport.exportExcel(headers2, dataset);
        try {
            workbook.write(baos);
            baos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            byte[] aa = baos.toByteArray();
            return aa;
        }

//        try {
//            OutputStream out = new FileOutputStream("E://test.xls");
////            OutputStream out2 = new FileOutputStream("E://test1.xls");
//            biRepSupport.exportExcel(headers, dataset, out);
////            biRepSupport.exportExcel(headers2, dataset2, out2);
//            out.close();
//            JOptionPane.showMessageDialog(null, "导出成功!");
//            System.out.println("excel导出成功！");
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("process end!");
//        }

    }
    public List<BiReportSalesProduct010Model> getData()
    {
        List<BiReportSalesProduct010Model> product010ModelList=new ArrayList<BiReportSalesProduct010Model>();
        product010ModelList =biReportSalesProduct010Dao.selectList(null);
        for(int i=0;i<10;i++)
        {
            System.out.println(product010ModelList.get(i).getAddCart()+"------"+product010ModelList.get(i).getAddFavorite());
        }
        return null;
    }


}
