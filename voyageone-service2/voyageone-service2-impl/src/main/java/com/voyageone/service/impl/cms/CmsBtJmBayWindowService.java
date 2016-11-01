package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.dao.cms.mongo.CmsBtJmBayWindowDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagJmModuleExtensionModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmBayWindowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by james on 2016/10/17.
 *
 * @version 2.8.0
 * @since 2.8.0
 */
@Service
public class CmsBtJmBayWindowService extends BaseService {

    private final CmsBtJmBayWindowDao cmsBtJmBayWindowDao;

    @Autowired
    public CmsBtJmBayWindowService(CmsBtJmBayWindowDao cmsBtJmBayWindowDao) {
        this.cmsBtJmBayWindowDao = cmsBtJmBayWindowDao;
    }

    public CmsBtJmBayWindowModel getBayWindowByJmPromotionId(Integer JmPromotionId) {
        return cmsBtJmBayWindowDao.selectOneWithQuery("{jmPromotionId:" + JmPromotionId + "}");
    }

    public void insert(CmsBtJmBayWindowModel cmsBtJmBayWindowModel) {
        cmsBtJmBayWindowDao.insert(cmsBtJmBayWindowModel);
    }

    public CmsBtJmBayWindowModel createByPromotion(CmsBtJmPromotionModel jmPromotionModel, String username) {
        CmsBtJmBayWindowModel jmBayWindowModel = new CmsBtJmBayWindowModel();

        jmBayWindowModel.setJmPromotionId(jmPromotionModel.getId());
        jmBayWindowModel.setFixed(true);
        jmBayWindowModel.setCreater(username);
        jmBayWindowModel.setModifier(username);

        return jmBayWindowModel;
    }

    public List<CmsBtJmBayWindowModel.BayWindow> createBayWindows(List<CmsBtTagJmModuleExtensionModel> tagJmModuleExtensionModelList, List<String> bayWindowTemplateUrls) {
        List<CmsBtJmBayWindowModel.BayWindow> bayWindowList = tagJmModuleExtensionModelList
                .stream()
                .filter(tagModule -> !tagModule.getFeatured())
                .map(tagModule -> createBayWindow(tagModule.getModuleTitle(), bayWindowTemplateUrls.get(1)))
                .collect(toList());

        bayWindowList.add(0, createBayWindow("聚美专场", bayWindowTemplateUrls.get(0)));

        for (int i = 0; i < bayWindowList.size(); i++) {
            bayWindowList.get(i).setOrder(i);
        }

        return bayWindowList;
    }

    public void update(CmsBtJmBayWindowModel bayWindowModel) {
        cmsBtJmBayWindowDao.update(bayWindowModel);
    }

    public void updateBayWindows(int jmPromotionId, List<CmsBtTagJmModuleExtensionModel> tagJmModuleExtensionModelList, List<String> bayWindowTemplateUrls) {

        List<CmsBtJmBayWindowModel.BayWindow> bayWindowList = createBayWindows(tagJmModuleExtensionModelList, bayWindowTemplateUrls);

        CmsBtJmBayWindowModel cmsBtJmBayWindowModel = getBayWindowByJmPromotionId(jmPromotionId);

        cmsBtJmBayWindowModel.setBayWindows(bayWindowList);

        update(cmsBtJmBayWindowModel);
    }

    private CmsBtJmBayWindowModel.BayWindow createBayWindow(String name, String templateUrl) {
        CmsBtJmBayWindowModel.BayWindow bayWindow = new CmsBtJmBayWindowModel.BayWindow();
        bayWindow.setName(name);

        try {
            bayWindow.setUrl(String.format(templateUrl, URLEncoder.encode(name, "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new BusinessException("创建固定飘窗信息时出现错误", e);
        }

        bayWindow.setLink("");
        bayWindow.setEnabled(true);
        return bayWindow;
    }
}
