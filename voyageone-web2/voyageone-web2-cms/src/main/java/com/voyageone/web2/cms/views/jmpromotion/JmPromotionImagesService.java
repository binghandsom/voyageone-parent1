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

    public Map<String,Object> getJmPromotionImage(int promotionId, int jmPromotionId) {

        CmsBtJmPromotionImagesModel promotionImagesModel = cmsBtJmPromotionImagesDao.selectJmPromotionImage(promotionId, jmPromotionId);
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(jmPromotionId,true);

        Map<String,String> promotionImageUrl = new HashMap<String,String>();
        Map<String, Object> imageMap = JacksonUtil.jsonToMap(JacksonUtil.bean2Json(promotionImagesModel));
        imageMap.forEach((s, o) -> {
            if (o instanceof String && o.toString().contains("-")) {
                promotionImageUrl.put(s, cmsBtJmImageTemplateService.getUrl(jmPromotionId+"-"+o.toString(),"appEntrance",cmsBtJmPromotionSaveBean));
            }
        });

        Map<String,Object> result = new HashMap<String,Object>();
        result.put("promotionImagesModel",promotionImagesModel);
        result.put("promotionImageUrl",promotionImageUrl);

        return result;
    }

    public void saveJmPromotionImages(CmsBtJmPromotionImagesModel model) {

        if (model == null)
            return;

        cmsBtJmPromotionImagesDao.saveJmPromotionImages(model);
    }

}
