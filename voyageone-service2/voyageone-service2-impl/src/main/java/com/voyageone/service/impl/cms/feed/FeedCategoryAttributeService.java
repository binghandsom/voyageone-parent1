package com.voyageone.service.impl.cms.feed;

import com.mongodb.WriteResult;
import com.voyageone.service.dao.cms.mongo.CmsBtFeedCategoryAttributeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.feed.CmsMtFeedAttributesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/4/19.
 * @version 2.0.0
 */
@Service
public class FeedCategoryAttributeService extends BaseService {

    @Autowired
    private CmsBtFeedCategoryAttributeDao cmsBtFeedCategoryAttributeDao;

    /**
     * 根据叶子类目的catId找出改类目下的属性值
     *
     * @param channelId 渠道ID
     * @param catId     叶子类目的类目ID
     * @return 属性值
     */
    public CmsMtFeedAttributesModel getCategoryAttributeByCatId(String channelId, String catId) {
        return cmsBtFeedCategoryAttributeDao.selectCategoryAttributeByCatId(channelId, catId);
    }

    public List<String> getAttributeNameByChannelId(String channelId) {
        List<CmsMtFeedAttributesModel> cmsMtFeedAttributesModels = cmsBtFeedCategoryAttributeDao.selectCategoryAttributeByChannelId(channelId);
        List<String> attributesName = new ArrayList<>();
        cmsMtFeedAttributesModels.forEach(cmsMtFeedAttributesModel -> {
            cmsMtFeedAttributesModel.getAttribute().forEach((s, strings) -> {
                if(!attributesName.contains(s)) attributesName.add(s);
            });
        });
        return attributesName;
    }

    /**
     * 根据叶子类目找出改类目下的属性值
     *
     * @param channelId 渠道ID
     * @param category  叶子类目
     * @return 属性值
     */
    public CmsMtFeedAttributesModel getCategoryAttributeByCategory(String channelId, String category) {
        return cmsBtFeedCategoryAttributeDao.selectCategoryAttributeByCategory(channelId, category);
    }

    /**
     * 更新类目属性
     *
     * @param cmsMtFeedAttributesModel 类目属性
     * @return WriteResult
     */
    public WriteResult updateAttributes(CmsMtFeedAttributesModel cmsMtFeedAttributesModel) {
        return cmsBtFeedCategoryAttributeDao.update(cmsMtFeedAttributesModel);
    }
}
