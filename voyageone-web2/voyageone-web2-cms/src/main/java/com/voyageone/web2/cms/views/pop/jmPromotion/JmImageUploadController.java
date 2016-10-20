package com.voyageone.web2.cms.views.pop.jmPromotion;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.ConvertUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.jmpromotion.JmPromotionImagesService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

/**
 * @author piao
 * @version 2.8.0
 * @since 2.8.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.POP.JM_IMAGE_UPLOAD.ROOT, method = RequestMethod.POST)
public class JmImageUploadController extends CmsController {

    @Autowired
    JmImageUploadService jmImageUploadService;
    @Autowired
    private JmPromotionImagesService jmPromotionImagesService;

    @RequestMapping(CmsUrlConstants.POP.JM_IMAGE_UPLOAD.UPLOAD)
    public AjaxResponse upload(HttpServletRequest request, @RequestParam Long promotionId, @RequestParam String imageName) throws Exception {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");

        InputStream input = file.getInputStream();
        Map<String, Object> response = jmImageUploadService.uploadImage(file, promotionId, imageName, getUser());

        if (response == null) {
            throw new BusinessException("图片上传失败");
        }

        input.close();

        return success(response);

    }

    @RequestMapping(CmsUrlConstants.POP.JM_IMAGE_UPLOAD.BATCH_UPLOAD)
    public AjaxResponse batUpload(HttpServletRequest request) throws Exception {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");

        String objStr = request.getParameter("promotionImages");
        int promotionId = Integer.parseInt(request.getParameter("promotionId"));
        String imageType = request.getParameter("imageType");

        if (!objStr.equals("null")) {
            CmsBtJmPromotionImagesModel model = JsonUtil.jsonToBean(objStr, CmsBtJmPromotionImagesModel.class);
            jmPromotionImagesService.saveJmPromotionImages(model);
        }

        InputStream input = file.getInputStream();
        Map<String, Object> response = jmImageUploadService.batchUploadImage(file, promotionId, imageType, getUser());

        if (response.get("result").equals("error")) {
            throw new BusinessException("图片上传失败");
        }

        input.close();

        return success(response);

    }


}

