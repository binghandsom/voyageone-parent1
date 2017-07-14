package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Shops;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.ecerp.interfaces.third.koala.KoalaVenderService;
import com.voyageone.ecerp.interfaces.third.koala.beans.Category;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.BaseTaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

/**
 * @author james.li on 2016/6/16.
 * @version 2.0.0
 */
@Service
public class CmsBuildPaltformCategoryTreeAmazonMqService {

    @Autowired
    PlatformCategoryService platformCategoryService;

    public void onStartup(Map<String, Object> messageMap) throws Exception {

        make(5,"h://amason_category.txt","[/]");
        make(17,"h://Google_Categories.txt"," > ");
        make(18,"h://Google_Departments.txt"," > ");
        make(19,"h://PriceGrabber_Category.txt"," > ");
    }

    private void make(Integer cartId, String file, String split) throws Exception{

        FileReader fileReader  = new FileReader (file);
        BufferedReader br = new BufferedReader(fileReader);

        String str = null;
        Integer sqeParentCatId = 1;
        HashMap<String, Integer> catIdMap = new HashMap<>();
        List<Category> categoryBeans = new ArrayList<>();
        while((str = br.readLine()) != null) {
            String[] categorys = str.split(split);
            String catPath="";
            Integer parentCatId = 0;
            for(int i=0;i<categorys.length;i++){
                catPath += categorys[i];
                if(catIdMap.containsKey(catPath)){
                    parentCatId = catIdMap.get(catPath);
                }else{
                    catIdMap.put(catPath, sqeParentCatId);
                    Category categoryBean = new Category();
                    categoryBean.setParentId(parentCatId);
                    categoryBean.setCategoryId(sqeParentCatId);
                    categoryBean.setCategoryName(categorys[i]);
                    categoryBean.setCategoryLevel(i+1);
                    categoryBean.setIsLeaf(0);
                    categoryBeans.add(categoryBean);
                    parentCatId = sqeParentCatId;
                    sqeParentCatId++;
                }
                catPath+="/";
            }
        }

        br.close();
        fileReader.close();


        List<CmsMtPlatformCategoryTreeModel> tree = new ArrayList<>();
        List<CmsMtPlatformCategoryTreeModel> cmsMtPlatformCategoryTreeModels = new ArrayList<>();
        categoryBeans.sort((o1, o2) -> Integer.compare(o1.getCategoryLevel(), o2.getCategoryLevel()));
        for (Category categoryBean : categoryBeans) {
            CmsMtPlatformCategoryTreeModel category = new CmsMtPlatformCategoryTreeModel();
            category.setChannelId("001");
            category.setCatName(categoryBean.getCategoryName());
            category.setCatPath(categoryBean.getCategoryName());
            category.setCatId(categoryBean.getCategoryId() + "");
            category.setParentCatId(categoryBean.getParentId() + "");
            category.setIsParent(0);
            category.setChildren(new ArrayList<CmsMtPlatformCategoryTreeModel>());
            category.setCartId(cartId);
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
                    second.setIsParent(1);
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
        platformCategoryService.setMangoDBPlatformCatTrees(tree, cartId.toString(), "001");
    }
}
