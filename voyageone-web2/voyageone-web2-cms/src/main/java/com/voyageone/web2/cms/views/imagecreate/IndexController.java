package com.voyageone.web2.cms.views.imagecreate;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtJmPromotionImportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.openapi.service.CmsImageFileService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
    @RestController
    @RequestMapping(value = CmsUrlConstants.ImageCreate.ROOT, method = RequestMethod.POST)
    public class IndexController extends CmsController {
        @Autowired
        CmsImageFileService service;
        @RequestMapping(CmsUrlConstants.ImageCreate.Upload)
        public AjaxResponse upload(HttpServletRequest request) throws Exception {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            String userName = getUser().getUserName();
            String path = Properties.readValue(CmsProperty.Props.CMS_Image_Ceate_Import_Path);
            FileUtils.mkdirPath(path);
            List<String> listFileName = FileUtils.uploadFile(request, path);//上传文件
            for (String fileName : listFileName) {
                service.importImageCreateInfo(path, fileName, userName);
            }
            Map<String, Object> reponse = new HashMap<>();// = cmsPromotionDetailService.uploadPromotion(input, promotionId, getUser().getUserName());
            reponse.put("result", true);
            return success(reponse);
        }
        private void importImageCreateInfo(String filePath) throws Exception {
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
