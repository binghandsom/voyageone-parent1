package com.voyageone.web2.cms.views.pop.image;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.ImgUtils;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field_Image;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.bean.CmsProductInfoBean;
import com.voyageone.web2.cms.views.product.CmsProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.POP.IMAGE_SETTING.ROOT,
        method = RequestMethod.POST
)
public class CmsImageSettingController extends CmsController {

    @Autowired
    private CmsImageSettingService cmsImageSettingService;
    @Autowired
    CmsProductDetailService productPropsEditService;

    // 允许上传的默认图片文件的后缀名
    private List<String> imageExtends = new ArrayList<String>(){{
        add(".jpg");
        add(".JPG");
        add(".png");
        add(".PNG");
    }};

    @RequestMapping(CmsUrlConstants.POP.IMAGE_SETTING.UPLOAD_IMAGE)
    public AjaxResponse uploadImage(HttpServletRequest request, @RequestParam Long productId,@RequestParam String imageType) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        // 获得输入流：

        if (!imageExtends.contains(ImgUtils.getImageExtend(file.getOriginalFilename())))
            throw new BusinessException("上传的图片后缀名不正确,请上传正确的图片, eg: jpg, JPG, png, PNG");

        InputStream input = file.getInputStream();
        Map<String,Object> response = cmsImageSettingService.uploadImage(file, productId, imageType, getUser(), ImgUtils.getImageExtend(file.getOriginalFilename()));
        if(response == null){
            throw new BusinessException("图片上传失败");
        }

//        int cartId = (int) getCmsSession().getPlatformType().get("cartId");
//        Map productInfo = productPropsEditService.getProductInfo(getUser().getSelChannelId(), productId, cartId, getLang());
//        CmsProductInfoBean cmsProductInfoBean = (CmsProductInfoBean) productInfo.get("productInfo");

//        List<CmsBtProductModel_Field_Image> images = cmsProductInfoBean.getProductImages().get(imageType);
//        if(images.size() > 0){
//            images.remove(images.size() - 1);
//        }
//        response.put("productInfo", productInfo.get("productInfo"));
//        productInfo.remove("productInfo");
        input.close();

        // 返回用户信息
        return success(response);
    }

}
