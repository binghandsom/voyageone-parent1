package com.voyageone.web2.cms.views.jm;

import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.impl.CmsProperty;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionExportTask3Service;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JmPromotionExportMQMessageBody;
import com.voyageone.service.model.cms.CmsBtJmPromotionExportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
    private CmsBtJmPromotionExportTask3Service service3;
//    @Autowired
//    private MqSender sender;

    @Autowired
    private CmsJmPromotionExportService cmsJmPromotionExportService;

    ///cms/CmsBtJmPromotionExportTask/index/selectByPromotionId
    //  @RequestMapping( value = "/cms/CmsBtJmPromotionExportTask/index/selectByPromotionId", method = RequestMethod.POST)//CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    // @ResponseBody
    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.GET_BY_PROMOTIONID)
    @ResponseBody
    public AjaxResponse getByPromotionId(@RequestBody int promotionId) {
        return success(service3.getByPromotionId(promotionId));
    }

    @RequestMapping("downloadExcel")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String source = request.getParameter("source");
        HashMap<String, Object> hm = JacksonUtil.ToObjectFromJson(source, HashMap.class);
        CmsBtJmPromotionExportTaskModel model = service3.get(Integer.parseInt(hm.get("id").toString()));
        String path = Properties.readValue(CmsProperty.Props.CMS_JM_EXPORT_PATH);
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
        service3.insert(model);
        Map<String, Object> message = new HashMap<>();
        message.put("id", model.getId());
        //sender.sendMessage(MqRoutingKey.CMS_BATCH_JmBtPromotionExportTask, message);

        JmPromotionExportMQMessageBody jmPromotionExportMQMessageBody = new JmPromotionExportMQMessageBody();
        jmPromotionExportMQMessageBody.setChannelId(getUser().getSelChannelId());
        jmPromotionExportMQMessageBody.setJmBtPromotionExportTaskId(model.getId());
        jmPromotionExportMQMessageBody.setSender(this.getUser().getUserName());
        service3.sendMessage(jmPromotionExportMQMessageBody);
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.CmsBtJmPromotionExportTask.LIST.INDEX.EXPORT_JM_PROMOTION_INFO)
    public ResponseEntity<byte[]> doExportJmPromotionInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer jmPromotionId, @RequestParam Integer type, @RequestParam String promotionName)
            throws Exception {
        byte[] data = cmsJmPromotionExportService.doExportJmPromotionFile(jmPromotionId, type);
        return genResponseEntityFromBytes(String.format("%s(%s).xlsx",promotionName , DateTimeUtil.getLocalTime(getUserTimeZone(), "yyyyMMddHHmmss") , ".xlsx"), data);
    }
}
