package com.voyageone.web2.cms.views.jmpromotion;

import com.voyageone.service.dao.cms.mongo.CmsBtJmPromotionImagesDao;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmPromotionImagesModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CmsBtJmPromotionImagesModel> getJmPromotionImagesList(UserSessionBean user, int promotionId, int jmPromotionId) {
        List<CmsBtJmPromotionImagesModel> juImageList = null;

        ///juImageList = cmsBtJmPromotionImagesDao.selectJmPromotionImagesList(user.getSelChannelId(), promotionId, jmPromotionId);
        return juImageList;
    }

    public void saveJmPromotionImages(CmsBtJmPromotionImagesModel model) {

        if (model == null)
            return;

        cmsBtJmPromotionImagesDao.saveJmPromotionImages(model);
    }

}
