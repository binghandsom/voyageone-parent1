package com.voyageone.web2.cms.views.jm;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionImportTask3Service;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionImportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtJmPromotionImportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
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
    private CmsBtJmPromotionImportTask3Service service;
    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }

    @RequestMapping("downloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
        CmsBtJmPromotionImportTaskModel model = service.get(Integer.parseInt(hm.get("id").toString()));
        String importPath = Properties.readValue(CmsProperty.Props.CMS_JM_IMPORT_PATH);
        String fileName = model.getFileName().trim();
        String path = importPath + "/" + fileName;//"/Product20160324164706.xls";
        FileUtils.downloadFile(response, fileName, path);
    }
    @RequestMapping("downloadImportErrorExcel")
    public void downloadImportErrorExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
        CmsBtJmPromotionImportTaskModel model = service.get(Integer.parseInt(hm.get("id").toString()));
        String path = Properties.readValue(CmsProperty.Props.CMS_JM_IMPORT_PATH);
        String fileName = model.getFailuresFileName().trim();
        String filepath = path + "/" + fileName;//"/Product20160324164706.xls";
        FileUtils.downloadFile(response, fileName, filepath);
    }
    @RequestMapping("upload")
    public AjaxResponse upload(HttpServletRequest request, @RequestParam int promotionId) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        String userName = getUser().getUserName();
        String path = Properties.readValue(CmsProperty.Props.CMS_JM_IMPORT_PATH);
        FileUtils.mkdirPath(path);
        String timerstr = DateTimeUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        List<String> listFileName = FileUtils.uploadFile(request, path, timerstr);//上传文件
        List<CmsBtJmPromotionImportTaskModel> listModel = new ArrayList<>();
        CmsBtJmPromotionImportTaskModel model = null;
        for (String fileName : listFileName) {
            model = new CmsBtJmPromotionImportTaskModel();
            model.setFileName(timerstr + fileName);
            model.setCmsBtJmPromotionId(promotionId);
            model.setCreater(userName);
            model.setCreated(new Date());
            model.setModifier(userName);
            model.setModified(new Date());
            model.setOriginFileName(fileName);

            model.setBeginTime(DateTimeUtil.getCreatedDefaultDate());
            model.setEndTime(DateTimeUtil.getCreatedDefaultDate());
            model.setErrorCode(0);
            model.setErrorMsg("");
            model.setFailuresFileName("");
            model.setFailuresRows(0);
            model.setIsImport(false);
            model.setSuccessRows(0);
            listModel.add(model);
        }
        service.saveList(listModel);
        for (CmsBtJmPromotionImportTaskModel taskModel : listModel) {
            JmPromotionImportMQMessageBody mqMessageBody = new JmPromotionImportMQMessageBody();
            mqMessageBody.setChannelId(getUser().getSelChannelId());
            mqMessageBody.setJmBtPromotionImportTaskId(taskModel.getId());
            mqMessageBody.setSender(this.getUser().getUserName());
            service.sendMessage(mqMessageBody);
        }
        Map<String, Object> reponse = new HashMap<>();// = cmsPromotionDetailService.uploadPromotion(input, promotionId, getUser().getUserName());
        reponse.put("result", true);
        return success(reponse);
    }
}
