package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jumei.bean.JmCategoryBean;
import com.voyageone.components.jumei.service.JumeiCategoryService;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItemService;
import com.voyageone.ecerp.interfaces.third.koala.KoalaVenderService;
import com.voyageone.ecerp.interfaces.third.koala.beans.Category;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/6/16.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_PlatformCategoryTreeKlJob)
public class CmsBuildPaltformCategoryTreeKLMqService extends BaseMQCmsService {

    @Autowired
    private KoalaVenderService koalaVenderService;

    @Autowired
    PlatformCategoryService platformCategoryService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId = messageMap.get("channelId").toString();
        KoalaConfig shopBean = Shops.getShopKoala(channelId, "34");
        List<CmsMtPlatformCategoryTreeModel> tree = new ArrayList<>();
        List<CmsMtPlatformCategoryTreeModel> cmsMtPlatformCategoryTreeModels = new ArrayList<>();
        List<Category> categoryBeans = Arrays.asList(koalaVenderService.categoryGet(shopBean));
        categoryBeans.sort((o1, o2) -> Integer.compare(o1.getCategoryLevel(), o2.getCategoryLevel()));
        for (Category categoryBean : categoryBeans) {
            CmsMtPlatformCategoryTreeModel category = new CmsMtPlatformCategoryTreeModel();
            category.setChannelId(channelId);
            category.setCatName(categoryBean.getCategoryName());
            category.setCatPath(categoryBean.getCategoryName());
            category.setCatId(categoryBean.getCategoryId() + "");
            category.setParentCatId(categoryBean.getParentId() + "");
            category.setIsParent(categoryBean.getIsLeaf() ^ 1);
            category.setChildren(new ArrayList<CmsMtPlatformCategoryTreeModel>());
            category.setCartId(34);
            cmsMtPlatformCategoryTreeModels.add(category);
        }

        for (int i = 0; i < cmsMtPlatformCategoryTreeModels.size(); i++) {
            boolean flg = false;
            CmsMtPlatformCategoryTreeModel firstItem = cmsMtPlatformCategoryTreeModels.get(i);
            for (int j = 0; j < cmsMtPlatformCategoryTreeModels.size(); j++) {
                CmsMtPlatformCategoryTreeModel second = cmsMtPlatformCategoryTreeModels.get(j);
                if (firstItem.getParentCatId().equalsIgnoreCase(second.getCatId())) {
                    firstItem.setCatPath(second.getCatPath() + ">" + firstItem.getCatName());
                    second.getChildren().add(firstItem);
                    flg = true;
                }
            }
            if (!flg) {
                firstItem.setCreater("CmsBuildPaltformCategoryTreeKLMqService");
                firstItem.setCreated(DateTimeUtil.getNow());
                firstItem.setModifier("CmsBuildPaltformCategoryTreeKLMqService");
                firstItem.setModified(DateTimeUtil.getNow());
                tree.add(firstItem);
            }
        }
        platformCategoryService.setMangoDBPlatformCatTrees(tree, "34", channelId);
    }


}
