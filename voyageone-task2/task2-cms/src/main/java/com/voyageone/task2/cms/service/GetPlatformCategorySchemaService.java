package com.voyageone.task2.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.WriteResult;
import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.BrandDao;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.tmall.TbCategoryService;
import com.voyageone.common.components.tmall.bean.ItemSchema;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.beanutils.BeanUtils;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class GetPlatformCategorySchemaService extends BaseTaskService {

    private final static String JOB_NAME = "getPlatformCategorySchemaTask";

    @Autowired
    BrandDao brandDao;

    @Autowired
    TbCategoryService tbCategoryService;

    @Autowired
    CmsMtPlatformCategoryDao platformCategoryDao;

    @Autowired
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Resource(name = "categoryProductMap")
    Map<String,String> categoryProductMap;

    @Resource(name = "paltformCartList")
    List<String> paltformCartList;

    @Resource(name = "availableChannelList")
    List availableChannelList;

    @Resource(name = "productChannelMap")
    Map<String,String> productChannelMap;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception{

        for (String cartId:paltformCartList){
            doSetPlatformPropTm(Integer.valueOf(cartId));
        }

        //正常结束
        $info("正常结束");
    }


    /**
     * 第三方平台属性信息取得（天猫系）
     * @param cartId 渠道信息
     */
    private void doSetPlatformPropTm(int cartId) throws ApiException, InvocationTargetException, IllegalAccessException, TopSchemaException {

        List<CmsMtPlatformCategoryTreeModel> allLeaves = new ArrayList<>();

        Set<String> savedList = new HashSet<>();

        List<Map> allCategoryLeavesMap = new ArrayList<>();

        List<CmsMtPlatformCategoryTreeModel> categoryTrees = this.platformCategoryDao.selectPlatformCategoriesByCartId(cartId);

        for (CmsMtPlatformCategoryTreeModel root:categoryTrees){
            Object jsonObj = JsonPath.parse(root.toString()).json();
            List<Map> leavesMap = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
            allCategoryLeavesMap.addAll(leavesMap);
        }

        for (Map leafMap:allCategoryLeavesMap){
            CmsMtPlatformCategoryTreeModel leafObj = new CmsMtPlatformCategoryTreeModel();
            BeanUtils.populate(leafObj, leafMap);
            String key = leafObj.getCatId();

            if(!savedList.contains(key)) {
                if (categoryProductMap.containsKey(key) && !productChannelMap.get(categoryProductMap.get(key)).equals(leafObj.getChannelId())) {
                    continue;
                } else {
                    savedList.add(key);
                    leafObj.setCartId(cartId);
                    allLeaves.add(leafObj);
                }
            }

        }

        //删除已有的数据
        WriteResult delResult = cmsMtPlatformCategorySchemaDao.deletePlatformCategorySchemaByCartId(cartId);

        $info("批量删除Schema, CART_ID 为：" + cartId + " 的数据为: " + delResult.getN() + "条...");

        int i = 1;
        int cnt = allLeaves.size();
        for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allLeaves) {

            // 获取产品属性
            $info("获取产品和商品属性:" + i + "/" + cnt + ":CHANNEL_ID:" + platformCategoriesModel.getChannelId() + ":CART_ID:" + platformCategoriesModel.getCartId() + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getCatId());
            doSetPlatformPropTmSub(platformCategoriesModel);

            i++;
        }


    }

    /**
     * 第三方平台属性信息取得（天猫系）
     * @param platformCategoriesModel 店铺信息
     */
    private ItemSchema doSetPlatformPropTmSub(CmsMtPlatformCategoryTreeModel platformCategoriesModel) throws ApiException {

        CmsMtPlatformCategorySchemaModel schemaModel = new CmsMtPlatformCategorySchemaModel();
        schemaModel.setCartId(platformCategoriesModel.getCartId());
        schemaModel.setCatId(platformCategoriesModel.getCatId());
        schemaModel.setCreater(this.getTaskName());
        schemaModel.setModifier(this.getTaskName());
        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());

        // 获取店铺信息
        ShopBean shopProp = Shops.getShop(platformCategoriesModel.getChannelId(), String.valueOf(platformCategoriesModel.getCartId()));
        if (shopProp == null) {
            $error("获取到店铺信息失败, shopProp == null");
        } else {

            // 调用API获取产品属性规则
            String productXmlContent = tbCategoryService.getTbProductAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()));

            if (productXmlContent != null){
                schemaModel.setPropsProduct(productXmlContent);
            } else {
                $error("获取产品schema失败, CategoryId: " + platformCategoriesModel.getCatId());
            }

            // 属性规则信息
            String itemXmlContent = null;

            ItemSchema result;

            // 调用API获取产品属性规则
            String productIdStr =categoryProductMap.get(platformCategoriesModel.getCatId());

            if (productIdStr != null){

                Long productId = Long.parseLong(productIdStr);

                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()),productId);

            } else {

                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()),null);

            }
            if (result != null){
                //保存为XML文件
                if (result.getResult() == 0 ){
                    itemXmlContent = result.getItemResult();
                } else {
                    if (productIdStr != null){
                        $error("获取商品schema失败, CategoryId: " + platformCategoriesModel.getCatId() + " productId: " + productIdStr + " 错误信息: " + result.getItemResult());
                    } else {
                        $error("获取商品schema失败, CategoryId: " + platformCategoriesModel.getCatId() + " 错误信息: " + result.getItemResult());
                    }

                }
            }

            schemaModel.setPropsItem(itemXmlContent);

            if(!StringUtils.isEmpty(schemaModel.getPropsItem())){
                cmsMtPlatformCategorySchemaDao.insert(schemaModel);
            }
        }

        return new ItemSchema();
    }

}
