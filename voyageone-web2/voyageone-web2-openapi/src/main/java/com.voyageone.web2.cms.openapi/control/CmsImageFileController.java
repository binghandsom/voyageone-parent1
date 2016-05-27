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
                                  @RequestParam(value = "isUploadUSCDN", required = false) boolean isUploadUSCDN,
                                  @RequestParam(value = "skipCache", required = false) boolean skipCache,
                                  @RequestParam String vparam
    ) throws Exception {
        $info("CmsImageFileController:get[get] start parameters:[%s][%s][%s][%s][%s][%s]", cId, templateId, file, isUploadUSCDN, skipCache, vparam);
        return service.getImage(cId,
                templateId,
                file,
                isUploadUSCDN,
                skipCache,
                vparam,
                CREATE_USER);
    }

    @RequestMapping(value = "get", method = RequestMethod.POST)
    public GetImageResultBean get(@RequestBody CreateImageParameter parameter) throws Exception {
        $info("CmsImageFileController:get[post] start parameters=[%s]", JacksonUtil.bean2Json(parameter));
        return service.getImage(parameter.getChannelId(),
                parameter.getTemplateId(),
                parameter.getFile(),
                parameter.isUploadUsCdn(),
                parameter.isSkipCache(),
                parameter.getVParamStr(),
                CREATE_USER);
    }

    @RequestMapping(value = "getImage", method = RequestMethod.GET)
    public void getImage(HttpServletResponse response,
                         @RequestParam String cId,
                         @RequestParam int templateId,
                         @RequestParam String file,
                         @RequestParam(value = "isUploadUSCDN", required = false) boolean isUploadUSCDN,
                         @RequestParam(value = "skipCache", required = false) boolean skipCache,
                         @RequestParam String vparam
    ) throws Exception {
        $info("CmsImageFileController:getImage[get] start parameters:[%s][%s][%s][%s][%s][%s]", cId, templateId, file, isUploadUSCDN, skipCache, vparam);
        getImageBean(response, cId, templateId, file, isUploadUSCDN, skipCache, vparam);
    }

    @RequestMapping(value = "getImage", method = RequestMethod.POST)
    public void getImage(HttpServletResponse response, @RequestBody CreateImageParameter parameter) throws Exception {
        $info("CmsImageFileController:getImage[post] start parameters=[%s]", JacksonUtil.bean2Json(parameter));
        getImageBean(response, parameter.getChannelId(), parameter.getTemplateId(), parameter.getFile(), parameter.isUploadUsCdn(), parameter.isSkipCache(), parameter.getVParamStr());
    }

    private void getImageBean(HttpServletResponse response,
                              String cId,
                              long templateId,
                              String file,
                              boolean isUploadUSCDN,
                              boolean skipCache,
                              String vparam) throws Exception {
        GetImageResultBean resultBean = service.getImage(cId, templateId, file, isUploadUSCDN, skipCache, vparam, CREATE_USER);
        if (resultBean.getErrorCode() == 0) {
            String url = ImageConfig.getAliYunUrl();
            if (isUploadUSCDN) {
                //http://wac.fb70.edgecastcdn.net/00FB70/images/products/001/50/under-armour-fire-shot-1269276669-5-2-1.jpg
                url = ImageConfig.getAliYunUrl();
            }
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", url + "/" + resultBean.getResultData().getFilePath());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setHeader("errorCode", String.valueOf(resultBean.getErrorCode()));
            response.setHeader("errorMsg", resultBean.getErrorMsg());
        }
        $info("CmsImageFileController:getImageBean end parameters:[%s][%s][%s][%s][%s][%s]", cId, templateId, file, isUploadUSCDN, skipCache, vparam);
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
