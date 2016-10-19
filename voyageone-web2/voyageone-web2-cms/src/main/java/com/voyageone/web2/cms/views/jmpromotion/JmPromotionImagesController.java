package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.common.asserts.Assert;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.impl.cms.jumei.CmsBtJmImageTemplateService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author piao
 * @version 2.8.0
 * @since 2.8.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.JMPROMOTION.Images.ROOT, method = RequestMethod.POST)
public class JmPromotionImagesController extends CmsController {

    @Autowired
    private JmPromotionImagesService jmPromotionImagesService;

    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.INIT)
    public AjaxResponse init(@RequestBody Map<String, String> requestMap) {

        int promotionId = Integer.parseInt(requestMap.get("promotionId"));
        Assert.notNull(promotionId).elseThrowDefaultWithTitle("promotionId");

        int jmPromotionId = Integer.parseInt(requestMap.get("jmPromotionId"));
        Assert.notNull(jmPromotionId).elseThrowDefaultWithTitle("jmPromotionId");

        jmPromotionImagesService.getJmPromotionImage(promotionId, jmPromotionId);

/*        List<CmsBtJmPromotionImagesModel> jmPromotionImagesList = jmPromotionImagesService.getJmPromotionImagesList(getUser(), promotionId, jmPromotionId);

        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(jmPromotionId,true);
        List<String> imgUrls = new ArrayList<String>();

        for(CmsBtJmPromotionImagesModel entity:jmPromotionImagesList){
            cmsBtJmImageTemplateService.getUrl();
        }*/

        return success(null);
    }

    /**
     * 存储聚美图片信息
     *
     * @param params promotionImages：图片模型
     *               promotionId： 活动Id
     *               jmPromotionId: 聚美活动Id
     * @return
     */
    @RequestMapping(CmsUrlConstants.JMPROMOTION.Images.SAVE)
    public AjaxResponse save(@RequestBody Wrapper params) {

        Assert.notNull(params.getPromotionImages()).elseThrowDefaultWithTitle("promotionImages");

        CmsBtJmPromotionImagesModel imageEntity = params.getPromotionImages();

        imageEntity.setPromotionId(params.getPromotionId());
        imageEntity.setJmPromotionId(params.getJmPromotionId());

        jmPromotionImagesService.saveJmPromotionImages(imageEntity);

        return success(null);
    }


    public static class Wrapper {
        CmsBtJmPromotionImagesModel promotionImages;

        private Integer promotionId;

        private Integer jmPromotionId;

        public CmsBtJmPromotionImagesModel getPromotionImages() {
            return promotionImages;
        }

        public void setPromotionImages(CmsBtJmPromotionImagesModel promotionImages) {
            this.promotionImages = promotionImages;
        }

        public Integer getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(Integer promotionId) {
            this.promotionId = promotionId;
        }

        public Integer getJmPromotionId() {
            return jmPromotionId;
        }

        public void setJmPromotionId(Integer jmPromotionId) {
            this.jmPromotionId = jmPromotionId;
        }
    }
}
