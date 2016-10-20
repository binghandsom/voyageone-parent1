package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.jumei.CmsBtJmPromotionSaveBean;
import com.voyageone.service.dao.cms.mongo.CmsBtJmPromotionImagesDao;
import com.voyageone.service.impl.cms.jumei.CmsBtJmImageTemplateService;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import com.voyageone.web2.base.BaseViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by piao on 9/7/16.
 *
 * @author jonas
 * @version 2.6.0
 * @since 2.6.0
 */
@Service
public class JmPromotionImagesService extends BaseViewService {

    @Autowired
    private CmsBtJmPromotionImagesDao cmsBtJmPromotionImagesDao;
    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;
    @Autowired
    private CmsBtJmImageTemplateService cmsBtJmImageTemplateService;

    private static String ORIGINAL_SCENE7_IMAGE_URL = "http://s7d5.scene7.com/is/image/sneakerhead/✓?fmt=jpg&scl=1&qlt=100";

    public Map<String, Object> getJmPromotionImage(int promotionId, int jmPromotionId) {

        CmsBtJmPromotionImagesModel promotionImagesModel = cmsBtJmPromotionImagesDao.selectJmPromotionImage(promotionId, jmPromotionId);

        if (promotionImagesModel == null) {
            return new HashMap<>(0);
        }
        return getJmImageTemplate(promotionImagesModel);
    }

    public Map<String, Object> getJmImageTemplate(CmsBtJmPromotionImagesModel model) {
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(model.getJmPromotionId(), true);

        Map<String, String> promotionImageUrl = new HashMap<String, String>();
        Map<String, Object> imageMap = JacksonUtil.jsonToMap(JacksonUtil.bean2Json(model));
        if (imageMap != null) {
            imageMap.forEach((s, o) -> {
                if (o instanceof String && o.toString().contains(model.getJmPromotionId() + "")) {
                    if(model.getUseTemplate())
                        promotionImageUrl.put(s, cmsBtJmImageTemplateService.getUrl(model.getJmPromotionId() + "-" + s.toString(), s, cmsBtJmPromotionSaveBean));
                    else
                        promotionImageUrl.put(s, ORIGINAL_SCENE7_IMAGE_URL.replace("✓",o.toString()));
                }
            });
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("promotionImagesModel", model);
        result.put("promotionImageUrl", promotionImageUrl);

        return result;
    }

    public void saveJmPromotionImages(CmsBtJmPromotionImagesModel model) {

        if (model == null)
            return;

        cmsBtJmPromotionImagesDao.saveJmPromotionImages(model);
    }

    public List<CmsBtJmPromotionImagesModel> getImageForSuit(String brand) {

        if (brand == null)
            return null;

        return cmsBtJmPromotionImagesDao.selectJmImageForSuit(brand);
    }

}
