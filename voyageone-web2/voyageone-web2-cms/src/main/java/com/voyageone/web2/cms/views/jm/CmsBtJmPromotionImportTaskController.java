package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.util.FileUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionImportTaskService;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionProductService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionExportTaskModel;
import com.voyageone.service.model.jumei.CmsBtJmPromotionImportTaskModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping(
        value = CmsUrlConstants.CmsBtJmPromotionImportTask.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsBtJmPromotionImportTaskController extends CmsController {
    @Autowired
    private CmsBtJmPromotionImportTaskService service;

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
}
