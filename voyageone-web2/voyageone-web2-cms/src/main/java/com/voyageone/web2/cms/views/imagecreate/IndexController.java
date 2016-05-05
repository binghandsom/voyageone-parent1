package com.voyageone.web2.cms.views.imagecreate;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.excel.ExcelColumn;
import com.voyageone.common.util.excel.ExcelImportUtil;
import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateImportService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.CmsBtJmPromotionImportTaskModel;
import com.voyageone.service.model.cms.CmsMtImageCreateImportModel;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
    @RestController
    @RequestMapping(value = CmsUrlConstants.ImageCreate.ROOT, method = RequestMethod.POST)
    public class IndexController extends CmsController {
        @Autowired
        CmsImageFileService service;
        @Autowired
        CmsMtImageCreateImportService serviceCmsMtImageCreateImport;

        @RequestMapping(CmsUrlConstants.ImageCreate.Upload)
        public AjaxResponse upload(HttpServletRequest request) throws Exception {
            AddListResultBean resultBean = new AddListResultBean();
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            String userName = getUser().getUserName();
            String path = Properties.readValue(CmsProperty.Props.CMS_Image_Ceate_Import_Path);
            FileUtils.mkdirPath(path);
            List<String> listFileName = FileUtils.uploadFile(request, path);//上传文件
            for (String fileName : listFileName) {
                resultBean = service.importImageCreateInfo(path, fileName, userName);
            }
            Map<String, Object> reponse = new HashMap<>();// = cmsPromotionDetailService.uploadPromotion(input, promotionId, getUser().getUserName());
            if (resultBean.getErrorCode() > 0) {
                reponse.put("result", false);
            } else {
                reponse.put("result", true);
            }
            reponse.put("msg", resultBean.getErrorMsg() + "requestId:" + resultBean.getRequestId());
            return success(reponse);
        }
        @RequestMapping(CmsUrlConstants.ImageCreate.DownloadExcel)
        public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String source = request.getParameter("source");
            HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
            CmsMtImageCreateImportModel model = serviceCmsMtImageCreateImport.get(Integer.parseInt(hm.get("id").toString()));
            String importPath = Properties.readValue(CmsProperty.Props.CMS_Image_Ceate_Import_Path);
            String fileName = model.getFileName().trim();
            String path = importPath + "/" + fileName;//"/Product20160324164706.xls";
            FileUtils.downloadFile(response, fileName, path);
        }
        @RequestMapping(CmsUrlConstants.ImageCreate.DownloadImportErrorExcel)
        public void downloadImportErrorExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
            String source = request.getParameter("source");
            HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
            CmsMtImageCreateImportModel model = serviceCmsMtImageCreateImport.get(Integer.parseInt(hm.get("id").toString()));
            String path = Properties.readValue(CmsProperty.Props.CMS_Image_Ceate_Import_Path);
            String fileName = model.getFailuresFileName().trim();
            String filepath = path + "/" + fileName;//"/Product20160324164706.xls";
            FileUtils.downloadFile(response, fileName, filepath);
        }
        @RequestMapping(CmsUrlConstants.ImageCreate.GetPageByWhere)
        public List getPageByWhere(Map<String, Object> map) {
            return serviceCmsMtImageCreateImport.getPageByWhere(map);
        }
        @RequestMapping(CmsUrlConstants.ImageCreate.GetCountByWhere)
        public int getCountByWhere(Map<String, Object> map) {
            return serviceCmsMtImageCreateImport.getCountByWhere(map);
        }
    }
