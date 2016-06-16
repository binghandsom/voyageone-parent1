package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.dao.ShopConfigDao;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.components.jumei.bean.JmCategoryBean;
import com.voyageone.components.jumei.service.JumeiCategoryService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/6/16.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformCategorySchemaJMJob)
public class CmsBuildPaltformCategoryTreeJMMqService extends BaseMQCmsService {

    @Autowired
    JumeiCategoryService jumeiCategoryService;

    @Autowired
    PlatformCategoryService platformCategoryService;

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId= messageMap.get("channelId").toString();
        ShopBean shopBean = Shops.getShop(channelId, "27");
        List<CmsMtPlatformCategoryTreeModel> tree = new ArrayList<>();
        List<CmsMtPlatformCategoryTreeModel> cmsMtPlatformCategoryTreeModels = new ArrayList<>();
        List<JmCategoryBean> jmCategoryBeans = jumeiCategoryService.getCategoryListALL(shopBean);
        jmCategoryBeans.forEach(jmCategoryBean -> {
            CmsMtPlatformCategoryTreeModel category = new CmsMtPlatformCategoryTreeModel();
            category.setChannelId(channelId);
            category.setCatName(jmCategoryBean.getName());
            category.setCatPath(jmCategoryBean.getName());
            category.setCatId(jmCategoryBean.getCategory_id() + "");
            category.setParentCatId(jmCategoryBean.getParent_category_id() + "");
            category.setIsParent(jmCategoryBean.getLevel().equalsIgnoreCase("4") ? 0 : 1);
            category.setChildren(new ArrayList<CmsMtPlatformCategoryTreeModel>());
            category.setCartId(27);
            cmsMtPlatformCategoryTreeModels.add(category);
        });

        for(int i=0;i<cmsMtPlatformCategoryTreeModels.size();i++){
            CmsMtPlatformCategoryTreeModel firstItem = cmsMtPlatformCategoryTreeModels.get(i);
            if(firstItem.getParentCatId().equalsIgnoreCase("0")){
                firstItem.setCreater("CmsBuildPaltformCategoryTreeJMMqService");
                firstItem.setCreated(DateTimeUtil.getNow());
                firstItem.setModifier("CmsBuildPaltformCategoryTreeJMMqService");
                firstItem.setModified(DateTimeUtil.getNow());
                tree.add(firstItem);
            }else{
                for(int j = 0;j<cmsMtPlatformCategoryTreeModels.size();j++){
                    CmsMtPlatformCategoryTreeModel second = cmsMtPlatformCategoryTreeModels.get(j);
                    if(firstItem.getParentCatId().equalsIgnoreCase(second.getCatId())){
                        firstItem.setCatPath(second.getCatPath()+">"+firstItem.getCatName());
                        second.getChildren().add(firstItem);
                    }
                }
            }
        }

        platformCategoryService.setMangoDBPlatformCatTrees(tree, "27", channelId);
    }


}
