package com.voyageone.task2.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.mongodb.WriteResult;
import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.bean.ItemSchema;
import com.voyageone.components.tmall.service.TbCategoryService;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.model.cms.CmsMtPlatformProductIdListModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.BrandDao;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class GetPlatformCategorySchemaService extends BaseTaskService {

    private final static String JOB_NAME = "getPlatformCategorySchemaTask";

    // Active有效
    private final static int Active_1 = 1;

    @Autowired
    BrandDao brandDao;

    @Autowired
    TbCategoryService tbCategoryService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    CmsMtPlatformCategoryDao platformCategoryDao;

    @Autowired
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Resource(name = "paltformCartList")
    List<String> paltformCartList;

    @Resource(name = "availableChannelList")
    List availableChannelList;

    // 有效的平台产品ID一览表
    List<CmsMtPlatformProductIdListModel> cmsMtPlatformProductIdList = null;

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

        // 取得有效的平台产品ID一览表(条件：active='1')
        cmsMtPlatformProductIdList = platformCategoryService.getPlatformProductIdList(Active_1);
        if (cmsMtPlatformProductIdList == null) {
            $warn("获取有效的平台产品ID一览表数据失败！");
            cmsMtPlatformProductIdList = new ArrayList<>();
        }

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
                // 20160526 tom bug修正 START
//                // 到平台产品ID一览表中去找对应的项目
//                for (CmsMtPlatformProductIdListModel productId : cmsMtPlatformProductIdList) {
//                    // 如果channelId,cartId,categoryId完全一致，找到并推出循环
//                    if (leafObj.getChannelId().equals(productId.getChannelId()) &&
//                            cartId == productId.getCartId() &&
//                            key.equals(productId.getCategoryId())) {
//                        // 设置product id
//                        savedList.add(key);
//                        leafObj.setCartId(cartId);
//                        allLeaves.add(leafObj);
//                        break;
//                    }
//                }

                boolean blnFound = false;
                String strChannelId = "";
                int intCartId = 0;
                for (CmsMtPlatformProductIdListModel productId : cmsMtPlatformProductIdList) {
                    // 如果channelId,cartId,categoryId完全一致，找到并推出循环
                    if (key.equals(productId.getCategoryId())) {
                        blnFound = true;
                        strChannelId = productId.getChannelId();
                        intCartId = productId.getCartId();
                        break;
                    }
                }

                if (!blnFound || (leafObj.getChannelId().equals(strChannelId) && cartId == intCartId) ) {
                    // 设置product id
                    savedList.add(key);
                    leafObj.setCartId(cartId);
                    allLeaves.add(leafObj);
                }
                // 20160526 tom bug修正 END
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

            // 取得productId值
            String productIdStr = "";
            // 到平台产品ID一览表中去找对应的项目
            for (CmsMtPlatformProductIdListModel productId : cmsMtPlatformProductIdList) {
                // 如果channelId,cartId,categoryId完全一致，找到并推出循环
                if (platformCategoriesModel.getChannelId().equals(productId.getChannelId()) &&
                        platformCategoriesModel.getCartId() == productId.getCartId() &&
                        platformCategoriesModel.getCatId().equals(productId.getCategoryId())) {
                    // 取得product id的值
                    productIdStr = productId.getPlatformProductId();
                    break;
                }
            }

            // 20160526 tom bug修正 START
//            if (productIdStr != null){
            if (!StringUtils.isEmpty(productIdStr)){
            // 20160526 tom bug修正 END

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
