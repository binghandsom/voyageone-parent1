package com.voyageone.web2.cms.views.imagecreate;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.imagecreate.CmsMtImageCreateImportService;
import com.voyageone.service.model.cms.CmsMtImageCreateImportModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.openapi.service.CmsImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            String timerstr = DateTimeUtil.format(new Date(), "yyyyMMddHHmmssSSS");
            List<String> listFileName = FileUtils.uploadFile(request, path,timerstr);//上传文件
            for (String fileName : listFileName) {
                resultBean = service.importImageCreateInfo(path, timerstr+fileName, userName);
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
        public List getPageByWhere(@RequestBody Map<String, Object> map) {
            return serviceCmsMtImageCreateImport.getPageByWhere(map);
        }
        @RequestMapping(CmsUrlConstants.ImageCreate.GetCountByWhere)
        public int getCountByWhere(@RequestBody Map<String, Object> map) {
            return serviceCmsMtImageCreateImport.getCountByWhere(map);
        }
    }
