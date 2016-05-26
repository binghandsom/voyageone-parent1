package com.voyageone.web2.cms.openapi.control;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.openapi.image.*;
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

    @RequestMapping(value = "get", method = RequestMethod.GET)
    public GetImageResultBean get(@RequestParam String cId,
                                  @RequestParam int templateId,
                                  @RequestParam String file,
                                  @RequestParam String vparam,
                                  @RequestParam(value = "isUploadUSCDN", required = false) boolean isUploadUSCDN) throws Exception {
        $info("CmsImageFileController:get[post] start cId:=[%s],templateId=[%s],file=[%s],isUploadUSCDN=[%s],vparam=[%s]", cId, templateId, file, isUploadUSCDN, vparam);
        return service.getImage(cId,
                templateId,
                file,
                isUploadUSCDN,
                vparam,
                CREATE_USER);
    }

    @RequestMapping(value = "get", method = RequestMethod.POST)
    public GetImageResultBean get(@RequestBody CreateImageParameter parameter) throws Exception {
        $info("CmsImageFileController:get[post] start parameter=[%s]", JacksonUtil.bean2Json(parameter));
        return service.getImage(parameter.getChannelId(),
                parameter.getTemplateId(),
                parameter.getFile(),
                parameter.isUploadUsCdn(),
                parameter.getVParamStr(),
                CREATE_USER);
    }

    @RequestMapping(value = "getImage", method = RequestMethod.GET)
    public void getImage(HttpServletResponse response,
                         @RequestParam String cId,
                         @RequestParam int templateId,
                         @RequestParam String file,
                         @RequestParam String vparam,
                         @RequestParam(value = "isUploadUSCDN", required = false) boolean isUploadUSCDN) throws Exception {
        $info("CmsImageFileController:getImage start cId:=[%s],templateId=[%s],file=[%s],isUploadUSCDN=[%s], vparam=[%s]", cId, templateId, isUploadUSCDN, file, vparam);
        getImageBean(response, cId, templateId, file, vparam, isUploadUSCDN);
        $info("CmsImageFileController:getImage end cId:=[%s],templateId=[%s],file=[%s],isUploadUSCDN=[%s], vparam=[%s]", cId, templateId, isUploadUSCDN, file, vparam);
    }

    @RequestMapping(value = "getImage", method = RequestMethod.POST)
    public void getImage(HttpServletResponse response, @RequestBody CreateImageParameter parameter) throws Exception {
        $info("CmsImageFileController:getImage[post] start parameter=[%s]", JacksonUtil.bean2Json(parameter));
        getImageBean(response, parameter.getChannelId(), parameter.getTemplateId(), parameter.getFile(), parameter.getVParamStr(), parameter.isUploadUsCdn());
        $info("CmsImageFileController:getImage[post] end parameter=[%s]", JacksonUtil.bean2Json(parameter));
    }

    private void getImageBean(HttpServletResponse response,
                              String cId,
                              long templateId,
                              String file,
                              String vparam,
                              boolean isUploadUSCDN) throws Exception {
        GetImageResultBean resultBean = service.getImage(cId, templateId, file, isUploadUSCDN, vparam, CREATE_USER);
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
