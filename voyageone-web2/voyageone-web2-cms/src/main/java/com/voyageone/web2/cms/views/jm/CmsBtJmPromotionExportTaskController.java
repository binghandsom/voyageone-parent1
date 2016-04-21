package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.mq.MqSender;
import com.voyageone.common.mq.enums.MqRoutingKey;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionExportTaskService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionExportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
//@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionExportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionExportTaskService service;
    @Autowired
    private MqSender sender;
    ///cms/CmsBtJmPromotionExportTask/index/getByPromotionId
    //  @RequestMapping( value = "/cms/CmsBtJmPromotionExportTask/index/getByPromotionId", method = RequestMethod.POST)//CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    // @ResponseBody
    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    @ResponseBody
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service.getByPromotionId(promotionId));
    }

    @RequestMapping("downloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
        CmsBtJmPromotionExportTaskModel model = service.get(Integer.parseInt(hm.get("id").toString()));
        String path = Properties.readValue(CmsConstants.Props.CMS_JM_EXPORT_PATH);
        String fileName = model.getFileName().trim();
        String filePath = path + "/" + fileName;
        File excelFile = new File(filePath);
        com.voyageone.common.util.FileUtils.downloadFile(response, fileName, filePath);
    }

    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.ADDEXPORT)
    @ResponseBody
    public AjaxResponse addExport(@RequestBody CmsBtJmPromotionExportTaskModel model) {
        CallResult result = new CallResult();
        model.setCreater(getUser().getUserName());
        model.setCreated(new java.util.Date());
        service.insert(model);
        Map<String, Object> message = new HashMap<String, Object>();
        message.put("id", model.getId());
        sender.sendMessage(MqRoutingKey.CMS_BATCH_JmBtPromotionExportTask, message);
        return success(result);
    }
}
