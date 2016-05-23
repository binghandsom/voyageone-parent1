package com.voyageone.web2.cms.openapi.control;

import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.AddListResultBean;
import com.voyageone.service.bean.openapi.image.GetImageResultBean;
import com.voyageone.service.bean.openapi.image.GetListResultBean;
import com.voyageone.service.impl.cms.imagecreate.ImageConfig;
import com.voyageone.web2.cms.openapi.OpenAipBaseController;
import com.voyageone.web2.cms.openapi.service.CmsImageFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(
        value = "/rest/product/image"
)
public class CmsImageFileController extends OpenAipBaseController {
    private static final String CREATE_USER = "SystemCreateImage";

    @Autowired
    private CmsImageFileService service;

    @RequestMapping(value = "get")
    public GetImageResultBean get(@RequestParam String cId, @RequestParam int templateId, @RequestParam String file, @RequestParam String vparam,
                                  @RequestParam(value = "isUploadUSCDN", required = false) boolean isUploadUSCDN) throws Exception {
        $info("CmsImageFileController:get start cId:=[%s],templateId=[%s],file=[%s],isUploadUSCDN=[%s],vparam=[%s]", cId, templateId, file, isUploadUSCDN, vparam);
        return service.getImage(cId, templateId, file, isUploadUSCDN, vparam, CREATE_USER);
    }

    @RequestMapping(value = "getImage")
    public void getImage(HttpServletResponse response, @RequestParam String cId, @RequestParam int templateId, @RequestParam String file, @RequestParam String vparam,
                         @RequestParam(value = "isUploadUSCDN", required = false) boolean isUploadUSCDN) throws Exception {
        $info("CmsImageFileController:getImage start cId:=[%s],templateId=[%s],file=[%s],isUploadUSCDN=[%s], vparam=[%s]", cId, templateId, isUploadUSCDN, file, vparam);

        GetImageResultBean resultBean = service.getImage(cId, templateId, file, false, vparam, CREATE_USER);
        if (resultBean.getErrorCode() == 0) {
            String url = ImageConfig.getAliYunUrl();
//            if (isUploadUSCDN) {
//                url = ImageConfig.getUSCDNUrl();
//            }
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", url + "/" + resultBean.getResultData().getFilePath());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setHeader("errorCode", String.valueOf(resultBean.getErrorCode()));
            response.setHeader("errorMsg", resultBean.getErrorMsg());
        }
        $info("CmsImageFileController:getImage end cId:=[%s],templateId=[%s],file=[%s],isUploadUSCDN=[%s], vparam=[%s]", cId, templateId, isUploadUSCDN, file, vparam);
    }

    @RequestMapping(value = "addList", method = RequestMethod.POST)
    public AddListResultBean addList(@RequestBody AddListParameter parameter) {
        return service.addListWithTrans(parameter);
    }

    @RequestMapping(value = "getListResult", method = RequestMethod.POST)
    public GetListResultBean getListResult(@RequestParam int taskId) {
        return service.getListResult(taskId);
    }
}
