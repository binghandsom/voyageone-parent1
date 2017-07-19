package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.common.ImageServer;
import com.voyageone.common.util.DateTimeUtil;
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
 * @author piao
 * @version 2.8.0
 * @since 2.8.0
 */
@Service
public class JmPromotionImagesService extends BaseViewService {

    @Autowired
    private CmsBtJmPromotionImagesDao cmsBtJmPromotionImagesDao;
    @Autowired
    private CmsBtJmPromotionService cmsBtJmPromotionService;
    @Autowired
    private CmsBtJmImageTemplateService cmsBtJmImageTemplateService;

    public Map<String, Object> getJmPromotionImage(int promotionId, int jmPromotionId) {

        CmsBtJmPromotionImagesModel promotionImagesModel = cmsBtJmPromotionImagesDao.selectJmPromotionImage(promotionId, jmPromotionId);

        if (promotionImagesModel == null) {
            return new HashMap<>(0);
        }

        //更新时间戳
        promotionImagesModel.setModified(DateTimeUtil.getNowTimeStamp());

        return getJmImageTemplate(promotionImagesModel);
    }

    /**
     * 通过模型中的图片名获取地址
     *
     * @param model 聚美图片模型
     * @return
     */
    public Map<String, Object> getJmImageTemplate(CmsBtJmPromotionImagesModel model) {
        CmsBtJmPromotionSaveBean cmsBtJmPromotionSaveBean = cmsBtJmPromotionService.getEditModel(model.getJmPromotionId(), true);

        Map<String, String> promotionImageUrl = new HashMap<String, String>();
        Map<String, Object> imageMap = JacksonUtil.jsonToMap(JacksonUtil.bean2Json(model));
        if (imageMap != null) {
            final String channelId = cmsBtJmPromotionSaveBean.getModel().getChannelId();
            imageMap.forEach((s, o) -> {
                if (s != null && o instanceof String && o.toString().contains("-" + s + "-")) {
                    if (model.getUseTemplate() != null && model.getUseTemplate())
                        promotionImageUrl.put(s, cmsBtJmImageTemplateService.getUrl(o.toString(), s, cmsBtJmPromotionSaveBean));
                    else
                        promotionImageUrl.put(s, ImageServer.imageUrl(channelId, o.toString() + "?fmt=jpg&scl=1&qlt=100"));
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
