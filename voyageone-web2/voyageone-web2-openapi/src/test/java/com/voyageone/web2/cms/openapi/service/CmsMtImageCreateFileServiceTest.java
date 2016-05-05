package com.voyageone.web2.cms.openapi.service;

import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmImportProduct;
import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.impl.cms.jumei.enumjm.EnumCount;
import com.voyageone.service.impl.cms.jumei.enumjm.EnumJMProductImportColumn;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.io.*;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtImageCreateFileServiceTest {

    @Autowired
    CmsImageFileService service;

    @Test
    public void test() throws Exception {
        // String url = "http://image.voyageone.net/product/getImage?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        String cId = "001";
        int templateId = 15;
        String file = "nike-air-penny-ii-333886005-1";//"test-test-1";//
        String vparam = "file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        String queryString = "cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        service.getImage(cId, templateId, file, false, vparam, "测试创建");
    }

    @Test
    public  void  testAddList() {
        AddListParameter parameter = new AddListParameter();
        parameter.setData(new ArrayList<CreateImageParameter>());
        for (int i =1000; i < 1200; i++) {
            parameter.getData().add(getCreateImageParameter(i));
        }
        long start = System.currentTimeMillis();
        AddListResultBean resultBean = service.addListWithTrans(parameter);
       // AddListResultBean resultBean = service.addList(parameter);
        System.out.println("total time:" + (System.currentTimeMillis()-start));
        Assert.isTrue(resultBean.getErrorCode() == 0, resultBean.getErrorCode() + "");
    }
    CreateImageParameter getCreateImageParameter(int fileIndex) {
        //http://localhost:8081/rest/product/image/get?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=["file:bcbg/bcbg-sku.png","file:bcbg/bcbgtupian.jpg","Text String to be rendered"]
        String cId = "001";
        int templateId = 15;
        String file = "nike-air-penny-ii-333886005-1" + fileIndex;//"test-test-1";//
        String vparam = "[\"file:bcbg/bcbg-sku.png\",\"file:bcbg/bcbgtupian.jpg\",\"Text String to be rendered\"]";
        String queryString = "cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";

        CreateImageParameter parameter = new CreateImageParameter();
        parameter.setChannelId(cId);
        parameter.setFile(file);
        parameter.setVParam(vparam);
        parameter.setUploadUsCdn(true);
        parameter.setTemplateId(templateId);
        return parameter;
    }
    @Test
    public  void testImportCreateImageInfo() throws Exception {
        String filePath = "/usr/ImageCreateImport/ImageInfo.xls";
        File excelFile = new File(filePath);
        InputStream fileInputStream = null;
        fileInputStream = new FileInputStream(excelFile);
        HSSFWorkbook book = null;
        book = new HSSFWorkbook(fileInputStream);
        HSSFSheet productSheet = book.getSheet("Sheet1");
        List<CreateImageParameter> listModel = new ArrayList<>();//导入的集合
        List<Map<String, Object>> listErrorMap = new ArrayList<>();//错误行集合  导出错误文件
        List<ExcelColumn> listColumn = new ArrayList<>();    //配置列信息
        listColumn.add(new ExcelColumn("channelId", 1, "cms_mt_image_create_file", "渠道Id"));
        listColumn.add(new ExcelColumn("templateId", 2, "cms_mt_image_create_file", "模板Id"));
        listColumn.add(new ExcelColumn("file", 3, "cms_mt_image_create_file", "文件名"));
        listColumn.add(new ExcelColumn("vParam", 4, "cms_mt_image_create_file", "参数Id"));
        listColumn.add(new ExcelColumn("isUploadUsCdn", 5, "cms_mt_image_create_file", "是否上传美国Cdn"));
        ExcelImportUtil.importSheet(productSheet, listColumn, listModel, listErrorMap, CreateImageParameter.class, 0);
        Assert.isTrue(listErrorMap.size() == 0, "导入错误");
        AddListParameter parameter = new AddListParameter();
        parameter.setData(listModel);
        AddListResultBean resultBean = service.addListWithTrans(parameter);
        Assert.isTrue(resultBean.getErrorCode() == 0, "导入错误");
    }
}
