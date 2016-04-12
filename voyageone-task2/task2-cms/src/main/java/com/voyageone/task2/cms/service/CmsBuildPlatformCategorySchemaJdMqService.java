package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.list.CategoryAttrReadService.CategoryAttr;
import com.taobao.api.ApiException;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.field.MultiCheckField;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdCategoryService;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by desmond on 2016/4/9.
 * 京东平台类目schema信息取得
 */
@Service
public class CmsBuildPlatformCategorySchemaJdMqService extends BaseMQTaskService {

    private final static String JOB_NAME = "CmsPlatformCategorySchemaJdJob";

    @Autowired
    JdCategoryService jdCategoryService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    @Autowired
    CmsMtPlatformCategoryDao platformCategoryDao;

    @Autowired
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Resource(name = "paltformCartList")
    List<String> paltformCartList;

//    @Resource(name = "availableChannelList")
//    List availableChannelList;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {
        setJdCategoryAttrInfo(taskControlList);
    }

    /**
     * 京东平台类目属性和属性值信息取得
     *
     * @param taskControlList taskcontrol信息
     */
    protected void setJdCategoryAttrInfo(List<TaskControlBean> taskControlList) throws Exception {

        // GetPlatformCategorySchemaService是从配置文件里面取得cart_id
//        for (String cartId:paltformCartList){
//            doSetPlatformCategoryAttrJd(Integer.valueOf(cartId));
//        }

        // 获取京东系所有店铺
        List<ShopBean> shopList = Shops.getShopListByPlatform(PlatFormEnums.PlatForm.JD);

        for (Iterator<ShopBean> it = shopList.iterator(); it.hasNext(); ) {
            ShopBean shop = it.next();
            if (StringUtils.isEmpty(shop.getAppKey()) || StringUtils.isEmpty(shop.getAppSecret())) {
                $info("Cart " + shop.getCart_id() + " " + shop.getCart_name() + " 对应的app key 和 app secret key 不存在，不做处理！！！");
                it.remove();
            }
        }

        // 获取该任务可以运行的销售渠道
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 循环所有店铺
        if (orderChannelIdList != null && orderChannelIdList.size() > 0) {
            for (ShopBean shop : shopList) {
                boolean isRun = false;
                // 判断该Shop是否需要运行任务
                isRun = orderChannelIdList.contains(shop.getOrder_channel_id());

                if (isRun) {
                    // 第三方平台类目属性信息取得（京东系）
                    doSetPlatformCategoryAttrJd(shop, Integer.parseInt(shop.getCart_id()));
                }

            }
        }

        //正常结束
        $info("正常结束");
    }

    /**
     * 京东平台属性信息取得
     *
     * @param cartId 店铺信息
     */
    private void doSetPlatformCategoryAttrJd(ShopBean shop, int cartId) throws ApiException, InvocationTargetException, IllegalAccessException, TopSchemaException {

        List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = new ArrayList<>();

        // 取得类目属性叶子数据并去掉重复叶子类目
        allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(cartId);

        // 去掉重复项的类目叶子件数大于0的场合
        if (allCategoryTreeLeaves.size() > 0) {
            int i = 1;
            int cnt = allCategoryTreeLeaves.size();
//            ArrayList<JdCategoryAttr> jdCategoryAttrList = new ArrayList<>();

            // 删除已有的数据
            int delNum = 0;
            delNum = platformCategoryService.deletePlatformCategorySchemaByCartId(cartId);
            $info("批量删除Schema, CART_ID 为：" + cartId + " 的数据为: " + delNum + "条...");

            // 取得每个叶子类目的属性和属性值插入到MangoDB的schema表中
            for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allCategoryTreeLeaves) {
//                ArrayList<JdCategoryAttr> tmpJdCategoryAttr = new ArrayList<>();

                // 获取产品属性
                $info("获取产品和商品属性:" + i + "/" + cnt + ":CHANNEL_ID:" + platformCategoriesModel.getChannelId() + ":CART_ID:" + platformCategoriesModel.getCartId() + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getCatId());

                doSetPlatformPropJdSub(shop, platformCategoriesModel);

                i++;
            }
        }

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


    }

    /**
     * 京东类目属性信息取得
     *
     * @param platformCategoriesModel 叶子类目信息
     */
    private void doSetPlatformPropJdSub(ShopBean shop, CmsMtPlatformCategoryTreeModel platformCategoriesModel) throws ApiException {

        CmsMtPlatformCategorySchemaModel schemaModel = new CmsMtPlatformCategorySchemaModel();
        schemaModel.setCartId(platformCategoriesModel.getCartId());
        schemaModel.setCatId(platformCategoriesModel.getCatId());
        schemaModel.setCreater(this.getTaskName());
        schemaModel.setModifier(this.getTaskName());
        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());

//        //获取店铺信息
//        ShopBean shopProp = Shops.getShop(platformCategoriesModel.getChannelId(), String.valueOf(platformCategoriesModel.getCartId()));
//        if (shopProp == null) {
//            $error("获取到店铺信息失败, shopProp == null");
//        } else {
        // 调用京东API获取类目属性信息
        List<CategoryAttr> jdCategoryAttrList = new ArrayList<>();


        // 调用京东API获取类目属性信息(只取3:可变属性)
        jdCategoryAttrList = jdCategoryService.getCategoryAttrInfo(shop, platformCategoriesModel.getCatId(), 3);


        try {
            // 调用京东API获取类目属性信息
//                jdCategoryAttrList = jdCategoryService.getCategoryAttrValueInfo(shopProp, jdCategoryAttrList);
        } catch (Exception ex) {
            $error(ex);
            return;
        }

        // ====================START===============
        // 构建主数据对象并持久化
//            List<Field> fieldList = new ArrayList<>();
//            for (JdCategoryAttrBean jdCategoryAttr : jdCategoryAttrList) {

//                if (jdCategoryAttr.getInputType() == 1 ||
//                    jdCategoryAttr.getInputType() == 2 ||
//                    jdCategoryAttr.getInputType() == 3 ) {
//                    // Field生成
//                    Field jdField = this.createField(String.valueOf(jdCategoryAttr.getInputType()));
//                    jdField.setsetName();
//
//                }

//            }
//            List<Field> fieldList = new ArrayList<>();

//        for (xxx: AAList) {
//            if inputType = 1
//            Field fld = new InputField();
//              fld.setName("xxx");
//        }

//        String xmlContent =  SchemaWriter.writeRuleXmlString(List<Field> fields);
        // ====================START===============


//            // 调用API获取产品属性规则
//            String productXmlContent = tbCategoryService.getTbProductAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()));
//
//            if (productXmlContent != null){
//                schemaModel.setPropsProduct(productXmlContent);
//            } else {
//                $error("获取产品schema失败, CategoryId: " + platformCategoriesModel.getCatId());
//            }

//            // 属性规则信息
//            String itemXmlContent = null;
//
//            ItemSchema result;
//
//            // 调用API获取产品属性规则
//            String productIdStr =categoryProductMap.get(platformCategoriesModel.getCatId());
//
//            if (productIdStr != null){
//
//                Long productId = Long.parseLong(productIdStr);
//
//                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()),productId);
//
//            } else {
//
//                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()),null);
//
//            }
//            if (result != null){
//                //保存为XML文件
//                if (result.getResult() == 0 ){
//                    itemXmlContent = result.getItemResult();
//                } else {
//                    if (productIdStr != null){
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
//            if(!StringUtils.isEmpty(schemaModel.getPropsItem())){
//                cmsMtPlatformCategorySchemaDao.insert(schemaModel);
//            }
//        return new ItemSchema();
    }


    private Field createField(String input_type) {
        Object field = null;

        switch(input_type) {
            case "1":
                field = new SingleCheckField();
                break;
            case "2":
                field = new MultiCheckField();
                break;
            case "3":
                field = new InputField();
                break;
        }

        return (Field)field;
    }

//    private void setFieldDefaultValue(CmsMtCommonPropActionDefModel defModel, Field field){
//
//        FieldTypeEnum type = FieldTypeEnum.getEnum(defModel.getPropType());
//
//        if (!StringUtil.isEmpty(defModel.getDefaultValue()) && type != null) {
//            switch (type) {
//                case LABEL:
//                    LabelField labelField = (LabelField) field;
//                    Label label = new Label();
//                    label.setValue(defModel.getDefaultValue());
//                    labelField.add(label);
//                    break;
//                case INPUT:
//                    InputField inputField = (InputField) field;
//                    inputField.setDefaultValue(defModel.getDefaultValue());
//                    break;
//                case SINGLECHECK:
//                    SingleCheckField singleCheckField = (SingleCheckField) field;
//                    singleCheckField.setDefaultValue(defModel.getDefaultValue());
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }


//    /**
//     * 第三方平台属性信息取得（天猫系）
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
//            if (productXmlContent != null){
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
//            // 调用API获取产品属性规则
//            String productIdStr =categoryProductMap.get(platformCategoriesModel.getCatId());
//
//            if (productIdStr != null){
//
//                Long productId = Long.parseLong(productIdStr);
//
////                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()),productId);
//
//            } else {
//
////                result = tbCategoryService.getTbItemAddSchema(shopProp, Long.parseLong(platformCategoriesModel.getCatId()),null);
//
//            }
//            if (result != null){
//                //保存为XML文件
//                if (result.getResult() == 0 ){
//                    itemXmlContent = result.getItemResult();
//                } else {
//                    if (productIdStr != null){
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
//            if(!StringUtils.isEmpty(schemaModel.getPropsItem())){
//                cmsMtPlatformCategorySchemaDao.insert(schemaModel);
//            }
//        }
//
//        return new ItemSchema();
//    }

}
