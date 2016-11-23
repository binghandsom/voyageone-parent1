package com.voyageone.task2.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.taobao.api.ApiException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.bean.ItemSchema;
import com.voyageone.components.tmall.service.TbCategoryService;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaTmDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.model.cms.CmsMtPlatformCategoryExtendInfoModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaTmModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.dao.BrandDao;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetPlatformCategorySchemaService extends BaseCronTaskService {

    //【注意】这个取得淘宝类目schema信息的job不单独执行了，现在都是从GetAllPlatformsInfoService调用的，job名"CmsGetAllPlatformsInfoJob"
    private final static String JOB_NAME = "getPlatformCategorySchemaTask";

    // Active有效
    private final static int Active_1 = 1;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private TbCategoryService tbCategoryService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

    @Autowired
    private CmsMtPlatformCategorySchemaTmDao cmsMtPlatformCategorySchemaTmDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 初始化
        Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap = doInit();

        // cart列表
        List<Integer> cartList = new ArrayList<>();
        cartList.add(CartEnums.Cart.TM.getValue());
        cartList.add(CartEnums.Cart.TG.getValue());
        cartList.add(CartEnums.Cart.TT.getValue());
        cartList.add(CartEnums.Cart.USTT.getValue());

        // 获取该任务可以运行的销售渠道
        int idxChannel = 1;
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);
        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {
            for (String channelId : orderChannelIdList) {
                int idxCart = 1;
                for (Integer cartId : cartList) {
                    ShopBean shopBean = Shops.getShop(channelId, cartId);
                    if (shopBean == null ||
                            StringUtils.isEmpty(shopBean.getApp_url()) ||
                            StringUtils.isEmpty(shopBean.getAppKey()) ||
                            StringUtils.isEmpty(shopBean.getAppSecret()) ||
                            StringUtils.isEmpty(shopBean.getSessionKey())
                            ) {
                        $info(String.format("店铺信息不存在, 无需获取平台schema, channelId:[%s], cartId:[%s]", channelId, cartId));
                        continue;
                    }

                    // 调用主逻辑(channel, cart, 特殊处理类目的信息一览)
                    String logInfo = String.format("获取天猫类目schema[完成度]-> channel:[%s], cart:[%s]",
                            idxChannel + "/" + orderChannelIdList.size(),
                            idxCart + "/" + cartList.size());
                    doLogic(shopBean, platformCategoryExtendInfoMap, logInfo);

                    idxCart++;
                }
                idxChannel++;
            }
        }

        //正常结束
        $info("正常结束");
    }

    /**
     * 初始化
     */
    public Map<String, CmsMtPlatformCategoryExtendInfoModel> doInit() {
        // 平台类目schema获取的时候, 需要的一些特殊参数信息(key: channel, cart, category)(条件：active='1')
        List<CmsMtPlatformCategoryExtendInfoModel> cmsMtPlatformCategoryExtendInfoModelList = platformCategoryService.getPlatformCategoryExtendInfoList(Active_1);
        if (cmsMtPlatformCategoryExtendInfoModelList == null) {
            $warn("获取有效的平台产品ID一览表数据失败！");
            cmsMtPlatformCategoryExtendInfoModelList = new ArrayList<>();
        }
        // 整理一下便于检索
        Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap = new HashMap<>();
        for (CmsMtPlatformCategoryExtendInfoModel model : cmsMtPlatformCategoryExtendInfoModelList) {
            platformCategoryExtendInfoMap.put(getPlatformCategoryExtendInfoKey(model.getChannelId(), model.getCartId(), model.getCategoryId()), model);
        }

        return platformCategoryExtendInfoMap;
    }

	/**
     * 主逻辑
     * @param shopBean shopBean
     * @param platformCategoryExtendInfoMap 特殊处理类目的信息一览
     * @param logInfo log用的信息的前缀
     */
    public void doLogic(ShopBean shopBean, Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap, String logInfo) {
        List<Map> allCategoryLeavesMap = new ArrayList<>();

        // 根据channel和cart, 获取platform category tree (一条记录就是一级类目含以下的一整棵树)
        List<CmsMtPlatformCategoryTreeModel> categoryTrees = this.platformCategoryDao.selectByChannel_CartId(shopBean.getOrder_channel_id(), Integer.parseInt(shopBean.getCart_id()));

        // 删除数据库现有数据
        cmsMtPlatformCategorySchemaTmDao.deletePlatformCategorySchemaByChannnelCart(shopBean.getOrder_channel_id(), Integer.parseInt(shopBean.getCart_id()));

        // 遍历一级类目, 获取下面所有的叶子类目
        for (CmsMtPlatformCategoryTreeModel root : categoryTrees) {
            Object jsonObj = JsonPath.parse(root.toString()).json();
            List<Map> leavesMap = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
            allCategoryLeavesMap.addAll(leavesMap);
        }

        // 遍历所有的叶子类目
        int idxCategory = 1;
        for (Map leafMap : allCategoryLeavesMap) {
            CmsMtPlatformCategoryTreeModel leafObj = new CmsMtPlatformCategoryTreeModel();
            try {
                BeanUtils.populate(leafObj, leafMap);
            } catch (IllegalAccessException | InvocationTargetException e) {
                $warn(String.format("转换树map为bean的过程失败, 一般来说可能性不大, channelId:[%s], cartId:[%s]", shopBean.getOrder_channel_id(), shopBean.getCart_id()));
            }

            // 目前获取tree的程序有个bug, cart不会回写, 等修正完成后, 这段可以不要了 START
            leafObj.setCartId(Integer.parseInt(shopBean.getCart_id()));
            // 目前获取tree的程序有个bug, cart不会回写, 等修正完成后, 这段可以不要了 END

            doSetPlatformPropTm(shopBean, leafObj, platformCategoryExtendInfoMap);

            String logInfoFull = logInfo + String.format(", category:[%s]", idxCategory++ + "/" + allCategoryLeavesMap.size());
            $info(logInfoFull);
        }
    }

    /**
     * 主逻辑 (单类目)
     * @param shopBean shopBean
     * @param platformCategoryExtendInfoMap 特殊处理类目的信息一览
     * @param categoryId 指定类目
     * @param categoryPath 指定类目的path
     * @param logInfo log用的信息的前缀
     */
    public void doLogicSimple(ShopBean shopBean, Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap, String categoryId, String categoryPath, String logInfo) {

        // 删除数据库现有数据(单类目)
        cmsMtPlatformCategorySchemaTmDao.deletePlatformCategorySchemaByChannnelCartCategory(shopBean.getOrder_channel_id(), Integer.parseInt(shopBean.getCart_id()), categoryId);

        CmsMtPlatformCategoryTreeModel leafObj = new CmsMtPlatformCategoryTreeModel();
        leafObj.setChannelId(shopBean.getOrder_channel_id());
        leafObj.setCartId(Integer.parseInt(shopBean.getCart_id()));
        leafObj.setCatId(categoryId);
        leafObj.setCatPath(categoryPath);

        doSetPlatformPropTm(shopBean, leafObj, platformCategoryExtendInfoMap);

        String logInfoFull = logInfo + String.format(", category:[%s][%s]", categoryId, categoryPath);
        $info(logInfoFull);

    }

	/**
     * 处理单个类目的逻辑
     * @param shopBean 店铺信息
     * @param platformCategoriesModel 等待处理的类目信息
     * @param platformCategoryExtendInfoMap 特殊处理类目的信息一览
     */
    private void doSetPlatformPropTm(ShopBean shopBean, CmsMtPlatformCategoryTreeModel platformCategoriesModel, Map<String, CmsMtPlatformCategoryExtendInfoModel> platformCategoryExtendInfoMap) {

        // 最终需要插入到表里的数据准备
        CmsMtPlatformCategorySchemaTmModel schemaModel = new CmsMtPlatformCategorySchemaTmModel();
        schemaModel.setChannelId(platformCategoriesModel.getChannelId());
        schemaModel.setCartId(platformCategoriesModel.getCartId());
        schemaModel.setCatId(platformCategoriesModel.getCatId());
        schemaModel.setCreater(this.getTaskName());
        schemaModel.setModifier(this.getTaskName());
        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());

        // 天猫官网同购的时候，不设置扩展信息,不取得schema信息
        if (!CartEnums.Cart.isSimple(CartEnums.Cart.getValueByID(shopBean.getCart_id()))) {
            // 扩展信息
            CmsMtPlatformCategoryExtendInfoModel extendInfo = platformCategoryExtendInfoMap.get(getPlatformCategoryExtendInfoKey(schemaModel.getChannelId(), schemaModel.getCartId(), schemaModel.getCatId()));

            // 获取天猫产品schema
            {
                Long brandId = null;
                if (extendInfo != null && !StringUtils.isEmpty(extendInfo.getPlatformBrandId())) {
                    brandId = Long.parseLong(extendInfo.getPlatformBrandId());
                }

                // 调用API获取产品属性规则
                String productXmlContent = null;
                try {
                    productXmlContent = tbCategoryService.getTbProductAddSchema(shopBean, Long.parseLong(schemaModel.getCatId()), brandId);
                } catch (ApiException e) {
                    $error(String.format("天猫ProductSchema获取异常[A]: channel:[%s], cart:[%s], category:[%s], fullPath:[%s], brandId:[%s]", schemaModel.getChannelId(), schemaModel.getCartId(), schemaModel.getCatId(), schemaModel.getCatFullPath(), brandId));
                }
                if (productXmlContent != null && !productXmlContent.startsWith("ERROR:")) {
                    schemaModel.setPropsProduct(productXmlContent);
                } else {
                    if (productXmlContent != null && productXmlContent.contains("不需要发布产品")) {
                        // 忽略
                    } else {
                        $error(String.format("天猫ProductSchema获取异常[B]: channel:[%s], cart:[%s], category:[%s], fullPath:[%s], brandId:[%s], err:[%s]", schemaModel.getChannelId(), schemaModel.getCartId(), schemaModel.getCatId(), schemaModel.getCatFullPath(), brandId, productXmlContent));
                    }
                }

            }

            // 获取天猫商品schema
            {
                Long productId = null;
                if (extendInfo != null && !StringUtils.isEmpty(extendInfo.getPlatformProductId())) {
                    productId = Long.parseLong(extendInfo.getPlatformProductId());
                }
                ItemSchema result = null;
                String itemXmlContent = null;
                try {
                    result = tbCategoryService.getTbItemAddSchema(shopBean, Long.parseLong(schemaModel.getCatId()), productId);
                } catch (ApiException e) {
                    $error(String.format("天猫ItemSchema获取异常[A]: channel:[%s], cart:[%s], category:[%s], fullPath:[%s], productId:[%s]", schemaModel.getChannelId(), schemaModel.getCartId(), schemaModel.getCatId(), schemaModel.getCatFullPath(), productId));
                }
                if (result != null) {
                    if (result.getResult() == 0) {
                        itemXmlContent = result.getItemResult();
                    } else if (result.getResult() == 1) {
                        if (result.getItemResult().contains("不能发布")) {
                            itemXmlContent =
                                    String.format("<itemRule><field id=\"infos\" name=\"信息\" type=\"label\"><label-group name=\"\"><label-group name=\"sys_infos\"><label desc=\"\" name=\"禁用\" value=\"%s\"/></label-group></label-group></field></itemRule>",
                                            result.getItemResult()
                                    );
                        }
                    }
                }
                if (!StringUtils.isEmpty(itemXmlContent)) {
                    schemaModel.setPropsItem(itemXmlContent);
                } else {
                    $error(String.format("天猫ItemSchema获取异常[B]: channel:[%s], cart:[%s], category:[%s], fullPath:[%s], productId:[%s]", schemaModel.getChannelId(), schemaModel.getCartId(), schemaModel.getCatId(), schemaModel.getCatFullPath(), productId));
                }

            }
        }

        // 插入数据库
        if (CartEnums.Cart.isSimple(CartEnums.Cart.getValueByID(shopBean.getCart_id()))  // 天猫官网同购
                || !StringUtils.isEmpty(schemaModel.getPropsProduct())
                || !StringUtils.isEmpty(schemaModel.getPropsItem())) {
            cmsMtPlatformCategorySchemaTmDao.insert(schemaModel);
        }

        // 最终log
        $info(String.format("获取天猫类目schema-> channel:[%s], cart:[%s], category:[%s][%s]",
                schemaModel.getChannelId(), schemaModel.getCartId(), schemaModel.getCatId(), schemaModel.getCatFullPath()));
    }

	/**
     * 拼接key
     * @param channelId channel
     * @param cartId cart
     * @param categoryId category
     * @return 拼接好的字符串
     */
    private String getPlatformCategoryExtendInfoKey(String channelId, int cartId, String categoryId) {
        return channelId + cartId + categoryId;
    }

//    /**
//     * 第三方平台属性信息取得（天猫系）
//     *
//     * @param cartId 渠道信息
//     */
//    private void doSetPlatformPropTm(int cartId) throws ApiException, InvocationTargetException, IllegalAccessException, TopSchemaException {
//
//        List<CmsMtPlatformCategoryTreeModel> allLeaves = new ArrayList<>();
//
//        Set<String> savedList = new HashSet<>();
//
//        List<Map> allCategoryLeavesMap = new ArrayList<>();
//
//        List<CmsMtPlatformCategoryTreeModel> categoryTrees = this.platformCategoryDao.selectPlatformCategoriesByCartId(cartId);
//
//        for (CmsMtPlatformCategoryTreeModel root : categoryTrees) {
//            Object jsonObj = JsonPath.parse(root.toString()).json();
//            List<Map> leavesMap = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
//            allCategoryLeavesMap.addAll(leavesMap);
//        }
//
//        for (Map leafMap : allCategoryLeavesMap) {
//            CmsMtPlatformCategoryTreeModel leafObj = new CmsMtPlatformCategoryTreeModel();
//            BeanUtils.populate(leafObj, leafMap);
//            String key = leafObj.getCatId();
//
//            if (!savedList.contains(key)) {
//                // 20160526 tom bug修正 START
////                // 到平台产品ID一览表中去找对应的项目
////                for (CmsMtPlatformProductIdListModel productId : cmsMtPlatformProductIdList) {
////                    // 如果channelId,cartId,categoryId完全一致，找到并推出循环
////                    if (leafObj.getChannelId().equals(productId.getChannelId()) &&
////                            cartId == productId.getCartId() &&
////                            key.equals(productId.getCategoryId())) {
////                        // 设置product id
////                        savedList.add(key);
////                        leafObj.setCartId(cartId);
////                        allLeaves.add(leafObj);
////                        break;
////                    }
////                }
//
//                boolean blnFound = false;
//                String strChannelId = "";
//                int intCartId = 0;
//                for (CmsMtPlatformCategoryExtendInfoModel productId : cmsMtPlatformProductIdList) {
//                    // 如果channelId,cartId,categoryId完全一致，找到并推出循环
//                    if (key.equals(productId.getCategoryId())) {
//                        blnFound = true;
//                        strChannelId = productId.getChannelId();
//                        intCartId = productId.getCartId();
//                        break;
//                    }
//                }
//
//                if (!blnFound || (leafObj.getChannelId().equals(strChannelId) && cartId == intCartId)) {
//                    // 设置product id
//                    savedList.add(key);
//                    leafObj.setCartId(cartId);
//                    allLeaves.add(leafObj);
//                }
//                // 20160526 tom bug修正 END
//            }
//
//        }
//
//        //删除已有的数据
//        WriteResult delResult = cmsMtPlatformCategorySchemaDao.deletePlatformCategorySchemaByCartId(cartId);
//
//        $info("批量删除Schema, CART_ID 为：" + cartId + " 的数据为: " + delResult.getN() + "条...");
//
//        int i = 1;
//        int cnt = allLeaves.size();
//        for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allLeaves) {
//
//            // 获取产品属性
//            $info("获取产品和商品属性:" + i + "/" + cnt + ":CHANNEL_ID:" + platformCategoriesModel.getChannelId() + ":CART_ID:" + platformCategoriesModel.getCartId() + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getCatId());
//            doSetPlatformPropTmSub(platformCategoriesModel);
//
//            i++;
//        }
//
//
//    }
//
//    /**
//     * 第三方平台属性信息取得（天猫系）
//     *
//     * @param platformCategoriesModel 店铺信息
//     */
//    private ItemSchema doSetPlatformPropTmSub(CmsMtPlatformCategoryTreeModel platformCategoriesModel) throws ApiException {
//
//        CmsMtPlatformCategorySchemaModel schemaModel = new CmsMtPlatformCategorySchemaModel();
//        schemaModel.setCartId(platformCategoriesModel.getCartId());
//        schemaModel.setCatId(platformCategoriesModel.getCatId());
//        schemaModel.setCreater(this.getTaskName());
//        schemaModel.setModifier(this.getTaskName());
//        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());
//
//        // 获取店铺信息
//        ShopBean shopProp = Shops.getShop(platformCategoriesModel.getChannelId(), String.valueOf(platformCategoriesModel.getCartId()));
//        if (shopProp == null) {
//            $error("获取到店铺信息失败, shopProp == null");
//        } else {
//
//            // 调用API获取产品属性规则
//            String productXmlContent = tbCategoryService.getTbProductAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()));
//
//            if (productXmlContent != null) {
//                schemaModel.setPropsProduct(productXmlContent);
//            } else {
//                $error("获取产品schema失败, CategoryId: " + platformCategoriesModel.getCatId());
//            }
//
//            // 属性规则信息
//            String itemXmlContent = null;
//
//            ItemSchema result;
//
//            // 取得productId值
//            String productIdStr = "";
//            // 到平台产品ID一览表中去找对应的项目
//            for (CmsMtPlatformCategoryExtendInfoModel productId : cmsMtPlatformProductIdList) {
//                // 如果channelId,cartId,categoryId完全一致，找到并推出循环
//                if (platformCategoriesModel.getChannelId().equals(productId.getChannelId()) &&
//                        platformCategoriesModel.getCartId() == productId.getCartId() &&
//                        platformCategoriesModel.getCatId().equals(productId.getCategoryId())) {
//                    // 取得product id的值
//                    productIdStr = productId.getPlatformProductId();
//                    break;
//                }
//            }
//
//            // 20160526 tom bug修正 START
////            if (productIdStr != null){
//            if (!StringUtils.isEmpty(productIdStr)) {
//                // 20160526 tom bug修正 END
//
//                Long productId = Long.parseLong(productIdStr);
//
//                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()), productId);
//
//            } else {
//
//                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()), null);
//
//            }
//            if (result != null) {
//                //保存为XML文件
//                if (result.getResult() == 0) {
//                    itemXmlContent = result.getItemResult();
//                } else {
//                    if (productIdStr != null) {
//                        $error("获取商品schema失败, CategoryId: " + platformCategoriesModel.getCatId() + " productId: " + productIdStr + " 错误信息: " + result.getItemResult());
//                    } else {
//                        $error("获取商品schema失败, CategoryId: " + platformCategoriesModel.getCatId() + " 错误信息: " + result.getItemResult());
//                    }
//
//                }
//            }
//
//            schemaModel.setPropsItem(itemXmlContent);
//
//            if (!StringUtils.isEmpty(schemaModel.getPropsItem())) {
//                cmsMtPlatformCategorySchemaDao.insert(schemaModel);
//            }
//        }
//
//        return new ItemSchema();
//    }

}
