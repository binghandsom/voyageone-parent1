package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.configs.Properties;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionImportTaskService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionImportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionImportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionImportTaskService service;
    @Autowired
    private MqSender sender;
    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }

    @RequestMapping("downloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
        CmsBtJmPromotionImportTaskModel model = service.get(Integer.parseInt(hm.get("id").toString()));
        String importPath = Properties.readValue(CmsConstants.Props.CMS_JM_IMPORT_PATH);
        String fileName = model.getFileName().trim();
        String path = importPath + "/" + fileName;//"/Product20160324164706.xls";
        FileUtils.downloadFile(response, fileName, path);
    }
    @RequestMapping("downloadImportErrorExcel")
    public void downloadImportErrorExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
        CmsBtJmPromotionImportTaskModel model = service.get(Integer.parseInt(hm.get("id").toString()));
        String path = Properties.readValue(CmsConstants.Props.CMS_JM_EXPORT_PATH);
        String fileName = model.getFailuresFileName().trim();
        String filepath = path + "/" + fileName;//"/Product20160324164706.xls";
        FileUtils.downloadFile(response, fileName, filepath);
    }
    @RequestMapping("upload")
    public AjaxResponse upload(HttpServletRequest request, @RequestParam int promotionId) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String userName = getUser().getUserName();
        String path = Properties.readValue(CmsConstants.Props.CMS_JM_IMPORT_PATH);
        FileUtils.mkdirPath(path);
        List<String> listFileName = FileUtils.uploadFile(request, path);//上传文件
        List<CmsBtJmPromotionImportTaskModel> listModel = new ArrayList<>();
        CmsBtJmPromotionImportTaskModel model = null;
        for (String fileName : listFileName) {
            model = new CmsBtJmPromotionImportTaskModel();
            model.setFileName(fileName);
            model.setCmsBtJmPromotionId(promotionId);
            model.setCreater(userName);
            model.setCreated(new Date());
            listModel.add(model);
        }
        service.saveList(listModel);
        for (CmsBtJmPromotionImportTaskModel taskModel : listModel) {
            Map<String, Object> message = new HashMap<String, Object>();
            message.put("id", taskModel.getId());
            sender.sendMessage(MqRoutingKey.CMS_BATCH_JmBtPromotionImportTask, message);
        }
        Map<String, Object> reponse = new HashMap<>();// = cmsPromotionDetailService.uploadPromotion(input, promotionId, getUser().getUserName());
        reponse.put("result", true);
        return success(reponse);
    }
}
