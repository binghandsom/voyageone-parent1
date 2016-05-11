package com.voyageone.web2.cms.openapi.service;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.service.bean.cms.businessmodel.CmsBtJmImportProduct;
import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.bean.openapi.image.GetImageResultBean;
import com.voyageone.service.dao.cms.mongo.CmsBtImageTemplateDao;
import com.voyageone.service.impl.cms.CmsImageTemplateService;
import com.voyageone.service.impl.cms.jumei.enumjm.EnumCount;
import com.voyageone.service.impl.cms.jumei.enumjm.EnumJMProductImportColumn;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
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
    @Autowired
    private CmsBtImageTemplateDao dao;
    @Autowired
    private CmsImageTemplateService serviceCmsImageTemplate;
    @Test
    public void test() throws Exception {
        // String url = "http://image.voyageone.net/product/getImage?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        String cId = "001";
        int templateId = 15;
        String file = "nike-air-penny-ii-333886005-1";//"test-test-1";//
        String vparam = "[\"file:bcbg/bcbg-sku.png\",\"file:bcbg/bcbgtupian.jpg\",\"Text String to be rendered\"]";
        String queryString = "cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        GetImageResultBean result = service.getImage(cId, templateId, file, false, vparam, "测试创建");

    }

    ////http://localhost:8081/rest/product/image/get?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=["file:bcbg/bcbg-sku.png","file:bcbg/bcbgtupian.jpg","Text String to be rendered"]
    @Test
    public void testAddList() {
        AddListParameter parameter = new AddListParameter();
        parameter.setData(new ArrayList<CreateImageParameter>());
        for (int i = 1000; i < 1200; i++) {
            parameter.getData().add(getCreateImageParameter(i));
        }
        long start = System.currentTimeMillis();
        AddListResultBean resultBean = service.addListWithTrans(parameter);
        // AddListResultBean resultBean = service.addList(parameter);
        System.out.println("total time:" + (System.currentTimeMillis() - start));
        Assert.isTrue(resultBean.getErrorCode() == 0, resultBean.getErrorCode() + "");
    }

    CreateImageParameter getCreateImageParameter(int fileIndex) {
        //http://localhost:8081/rest/product/image/get?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=["file:bcbg/bcbg-sku.png","file:bcbg/bcbgtupian.jpg","Text String to be rendered"]
        String cId = "001";
        int templateId = 15;
        String file = "nike-air-penny-ii-333886005-1" + fileIndex;//"test-test-1";//
        String[] vparam = {"file:bcbg/bcbg-sku.png", "file:bcbg/bcbgtupian.jpg", "Text String to be rendered"};
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
    public void testImportCreateImageInfo() throws Exception {
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

//    @Autowired
//    CmsMtImageCreateTemplateDao daoCmsMtImageCreateTemplate;

    @Test
    public void textImportImageTemplate() throws Exception {
        //   CmsMtImageCreateTemplateModel template = new CmsMtImageCreateTemplateModel();
        String filePath = "/usr/imageTemplate/template1.xls";
        File excelFile = new File(filePath);
        InputStream fileInputStream = null;
        fileInputStream = new FileInputStream(excelFile);
        HSSFWorkbook book = null;
        book = new HSSFWorkbook(fileInputStream);
        HSSFSheet productSheet = book.getSheet("Sheet1");
        List<CmsMtImageCreateTemplateModel> listModel = new ArrayList<>();//导入的集合
        List<Map<String, Object>> listErrorMap = new ArrayList<>();//错误行集合  导出错误文件
        List<ExcelColumn> listColumn = new ArrayList<>();    //配置列信息
        listColumn.add(new ExcelColumn("channelId", 1, "cms_mt_image_create_file", "渠道Id"));
        listColumn.add(new ExcelColumn("content", 2, "cms_mt_image_create_file", "模板Id"));
        listColumn.add(new ExcelColumn("name", 3, "cms_mt_image_create_file", "文件名"));
        ExcelImportUtil.importSheet(productSheet, listColumn, listModel, listErrorMap, CmsMtImageCreateTemplateModel.class, 0);
        //ftp://ftp.xpairs.com/007/test1.png
        for (CmsMtImageCreateTemplateModel model : listModel) {
            String Content = model.getContent();
            model.setModified(new Date());
            model.setCreated(new Date());
            model.setCreater("system");
            model.setModifier("system");
           // daoCmsMtImageCreateTemplate.insert(model);
        }
        String str = "source=name[icon],url[%s]&source=name[s],url[%s]&scale=height[1100],width[700]&blank=color[white],height[1200],name[bcc],width[1200]&select=name[bcc]&composite=compose[Over],image[s],x[200],y[100]&composite=compose[Over],image[icon],x[100],y[32]&annotate=fill[red],font[VeraSans-Bold],pointsize[18],text[%s],x[923],y[832]&sink";
    }

    @Test
    public void testAllTemplate() throws Exception {
        Map<String, Object> map = new HashMap<>();
       // List<CmsMtImageCreateTemplateModel> modelList = daoCmsMtImageCreateTemplate.selectList(map);
//        for (CmsMtImageCreateTemplateModel model : modelList) {
//            testTemplate(model.getId());
//        }
         List<CmsBtImageTemplateModel> modelList =  dao.selectAll();// daoCmsMtImageCreateTemplate.selectList(map);
        for (CmsBtImageTemplateModel model : modelList) {
            testTemplate(model.getImageTemplateId());
        }
    }

    @Test
    public void testTemplateOne() throws Exception {
        //36  &问题  champion_700x700_tg_pc_info
        //35
        //34 编码后%OA
        //33
        //32 &问题   编码后%OA
        //31
        //30 编码后%OA
        testTemplate(31);
       // testTemplate(31);
       // testTemplate(32);
       // testTemplate(34);
       // testTemplate(35);
       // testTemplate(36);

    }
//shenzhen-vo.oss-cn-shenzhen.aliyuncs.com
    public void testTemplate(long templateId) throws Exception {
        System.out.println("templateId:" + templateId);
        /*
        source=url[ftp://images@xpairs.com:voyageone5102@ftp.xpairs.com/007/%s],name[tupian]
blank=color[#1c2127],name[color1],height[30],width[250]
annotate=text[%s｜%s],pointsize[20],fill[white],font[SourceHanSansCN-Regular],gravity[center]
blank=color[white],name[bg],height[770],width[750]
composite=compose[Over],image[tupian],x[25],y[15]
composite=compose[Over],image[color1],x[250],y[715]
sink=format[jpg],quality[100]
         */
        String prefix = "ftp://images@xpairs.com:voyageone5102@ftp.xpairs.com";
        CmsBtImageTemplateModel model= serviceCmsImageTemplate.get(templateId);
        String content =  model.getImageTemplateContent();
        String[] strList = content.split("%s");
        String[] paramList = new String[strList.length - 1];
        for (int i = 0; i < strList.length - 1; i++) {
            if (strList[i].indexOf(prefix) > 0) {
                paramList[i] = "test1.png";
            } else {
                paramList[i] =templateId+ "test中国&" + i;
            }
        }
        String cId = model.getChannelId();
        //int templateId = 15;
        String file = "nike-air-penny-ii-333886005-1";// + System.currentTimeMillis() + "templateId:" + templateId;//"test-test-1";//
        String vparam = JacksonUtil.bean2Json(paramList);
        GetImageResultBean result = service.getImage(cId, templateId, file, false, vparam, "测试创建");
        System.out.println(result.getErrorCode()+""+result.getErrorMsg());
       // Assert.isTrue(result.getErrorCode()==0,"有错误");
    }
}
