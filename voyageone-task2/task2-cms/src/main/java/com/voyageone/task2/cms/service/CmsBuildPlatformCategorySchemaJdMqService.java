package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdCategoryService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by desmond on 2016/4/9.
 * 京东平台类目schema信息取得
 */
@Service
public class CmsBuildPlatformCategorySchemaJdMqService extends BaseMQTaskService {

    private final static String JOB_NAME = "CmsBuildPlatformCategorySchemaJdJob";

    @Autowired
    JdCategoryService jdCategoryService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

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
//                    doSetPlatformCategoryAttrJd(shop, Integer.parseInt(shop.getCart_id()));
                }

            }
        }

        //正常结束
        $info("正常结束");
    }
//
//    /**
//     * 京东平台类目属性信息取得
//     *
//     * @param shop ShopBean 店铺信息
//     * @param cartId int    平台ID
//     */
//    private void doSetPlatformCategoryAttrJd(ShopBean shop, int cartId) {
//
//        List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = new ArrayList<>();
//
//        // 取得类目属性叶子数据并去掉重复叶子类目
//        allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(cartId);
//
//        // 去掉重复项的类目叶子件数大于0的场合
//        if (allCategoryTreeLeaves.size() > 0) {
//            int i = 1;
//            int cnt = allCategoryTreeLeaves.size();
//
//            // 删除已有的数据
//            int delNum = 0;
//            delNum = platformCategoryService.deletePlatformCategorySchemaByCartId(cartId);
//            $info("批量删除Schema, CART_ID 为：" + cartId + " 的数据为: " + delNum + "条...");
//
//            // 取得每个叶子类目的属性和属性值插入到MangoDB的schema表中
//            for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allCategoryTreeLeaves) {
//                // 京东类目属性schema信息插入
//                $info("京东类目属性:" + i + "/" + cnt + ":CHANNEL_ID:" + platformCategoriesModel.getChannelId() + ":CART_ID:" + platformCategoriesModel.getCartId() + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getCatId());
//                doSetPlatformPropJdSub(shop, platformCategoriesModel);
//
//                i++;
//            }
//        }
//    }
//
//    /**
//     * 京东类目属性schema信息插入
//     *
//     * @param shop ShopBean  店铺信息
//     * @param platformCategoriesModel CmsMtPlatformCategoryTreeModel 叶子类目信息
//     */
//    private void doSetPlatformPropJdSub(ShopBean shop, CmsMtPlatformCategoryTreeModel platformCategoriesModel) {
//
//        CmsMtPlatformCategorySchemaModel schemaModel = new CmsMtPlatformCategorySchemaModel();
//        schemaModel.setCartId(platformCategoriesModel.getCartId());
//        schemaModel.setCatId(platformCategoriesModel.getCatId());
//        schemaModel.setCreater(this.getTaskName());
//        schemaModel.setModifier(this.getTaskName());
//        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());
//
//        // 调用京东API获取类目属性信息
//        List<CategoryAttr> jdCategoryAttrList = new ArrayList<>();
//        // 京东类目属性的属性类型(1:关键属性 2:不变属性 3:可变属性 4:销售属性)
//        int attributeType = 3;
//        // 调用京东API获取类目属性信息(只取3:可变属性)
//        jdCategoryAttrList = jdCategoryService.getCategoryAttrInfo(shop, platformCategoriesModel.getCatId(), attributeType);
//
//        // 类目属性schema作成用field列表
//        List<Field> fieldsList = new ArrayList<>();
//
//        // 根据类目属性列表循环，取得类目属性值，加入schema field列表
//        for (CategoryAttr categoryAttr : jdCategoryAttrList) {
//
//            // 调用京东API获取类目属性值信息
//            List<CategoryAttrValue> jdCategoryAttrValueList = new ArrayList<>();
//            jdCategoryAttrValueList = jdCategoryService.getCategoryAttrValueInfo(shop, categoryAttr.getCategoryAttrId());
//
//            // 类目属性的输入类型（1:单选 2:多选 3:可输入）
//            int inputType = categoryAttr.getInputType();
//            switch (inputType) {
//                case 1:
//                    // 单选
//                    SingleCheckField singleCheckField = new SingleCheckField();
//                    // 根据类目属性和属性值取得单选field
//                    singleCheckField = getSingleCheckField(categoryAttr, jdCategoryAttrValueList);
//                    // 类目属性列表追加
//                    fieldsList.add(singleCheckField);
//                    break;
//                case 2:
//                    // 多选
//                    MultiCheckField multiCheckField = new MultiCheckField();
//                    // 根据类目属性和属性值取得多选field
//                    multiCheckField = getMultiCheckField(categoryAttr, jdCategoryAttrValueList);
//                    // 类目属性列表追加
//                    fieldsList.add(multiCheckField);
//                    break;
//                case 3:
//                    // 可输入
//                    InputField inputField = new InputField();
//                    // 根据类目属性和属性值取得可输入field
//                    inputField = getInputField(categoryAttr, jdCategoryAttrValueList);
//                    // 类目属性列表追加
//                    fieldsList.add(inputField);
//                    break;
//            }
//        }
//
//        // 根据属性值列表转换成XML文件
//        String xmlContent = null;
//        if (fieldsList.size() > 0) {
//            xmlContent = SchemaWriter.writeRuleXmlString(fieldsList);
//            schemaModel.setPropsItem(xmlContent);
//        }
//
//        // 把schema信息插进入到MangoDB中
//        platformCategoryService.insertPlatformCategorySchema(schemaModel);
//    }
//
//    /**
//     * 根据京东类目属性值列表取得单选field选项列表
//     *
//     * @param categoryAttr     CategoryAttr      京东类目属性
//     * @param jdCategoryAttrValueList  List<CategoryAttrValue>  京东类目属性值列表
//     * @return SingleCheckField    单选field对象
//     */
//    private SingleCheckField getSingleCheckField(CategoryAttr categoryAttr, List<CategoryAttrValue> jdCategoryAttrValueList) {
//        // 多选
//        SingleCheckField singleCheckField = new SingleCheckField();
//        // 项目列表
//        List<Option> attrValueOptionList = new ArrayList<>();
//
//        // field共通信息设定
//        // 类目属性id
//        singleCheckField.setId(String.valueOf(categoryAttr.getCategoryAttrId()));
//        // 类目属性名
//        singleCheckField.setName(categoryAttr.getAttName());
//        // 类目属性类型
//        singleCheckField.setType(FieldTypeEnum.SINGLECHECK);
//
//        // field的rule信息设定
//        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
//        if (rulesList != null && rulesList.size() > 0) {
//            singleCheckField.setRules(rulesList);
//        }
//
//        // 类目属性值选项列表取得
//        attrValueOptionList = this.getOptionList(jdCategoryAttrValueList);
//        // 类目属性选项列表设定
//        singleCheckField.setOptions(attrValueOptionList);
//
//        return singleCheckField;
//    }
//
//    /**
//     * 根据京东类目属性值列表取得多选field选项列表
//     *
//     * @param categoryAttr     CategoryAttr      京东类目属性
//     * @param jdCategoryAttrValueList  List<CategoryAttrValue>  京东类目属性值列表
//     * @return MultiCheckField    多选field对象
//     */
//    private MultiCheckField getMultiCheckField(CategoryAttr categoryAttr, List<CategoryAttrValue> jdCategoryAttrValueList) {
//        // 多选
//        MultiCheckField multiCheckField = new MultiCheckField();
//        // 项目列表
//        List<Option> attrValueOptionList = new ArrayList<>();
//
//        // field共通信息设定
//        // 类目属性id
//        multiCheckField.setId(String.valueOf(categoryAttr.getCategoryAttrId()));
//        // 类目属性名
//        multiCheckField.setName(categoryAttr.getAttName());
//        // 类目属性类型
//        multiCheckField.setType(FieldTypeEnum.MULTICHECK);
//
//        // field的rule信息设定
//        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
//        if (rulesList != null && rulesList.size() > 0) {
//            multiCheckField.setRules(rulesList);
//        }
//
//        // 类目属性值选项列表取得
//        attrValueOptionList = this.getOptionList(jdCategoryAttrValueList);
//        // 类目属性选项列表设定
//        multiCheckField.setOptions(attrValueOptionList);
//
//        return multiCheckField;
//    }
//
//    /**
//     * 根据京东类目属性值列表取得多选field选项列表
//     *
//     * @param categoryAttr     CategoryAttr      京东类目属性
//     * @param jdCategoryAttrValueList  List<CategoryAttrValue>  京东类目属性值列表
//     * @return InputField    可输入field对象
//     */
//    private InputField getInputField(CategoryAttr categoryAttr, List<CategoryAttrValue> jdCategoryAttrValueList) {
//        // 可输入
//        InputField inputField = new InputField();
//
//        // field共通信息设定
//        // 类目属性id
//        inputField.setId(String.valueOf(categoryAttr.getCategoryAttrId()));
//        // 类目属性名
//        inputField.setName(categoryAttr.getAttName());
//        // 类目属性类型
//        inputField.setType(FieldTypeEnum.INPUT);
//
//        // field的rule信息设定
//        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
//        if (rulesList != null && rulesList.size() > 0) {
//            inputField.setRules(rulesList);
//        }
//
//        // 类目属性选项列表不需要设定
//
//        return inputField;
//    }
//
//    /**
//     * 根据京东类目属性值列表取得field选项列表
//     *
//     * @param jdCategoryAttrValueList List<CategoryAttrValue>  京东类目属性列表
//     * @return List<Option>    field选项列表
//     */
//    private List<Option> getOptionList(List<CategoryAttrValue> jdCategoryAttrValueList) {
//        List<CategoryAttrValue> sortedCategoryAttrValueList = new ArrayList<>();
//        List<Option> optionList = new ArrayList<>();
//
//        if (jdCategoryAttrValueList != null && jdCategoryAttrValueList.size() > 0) {
//            // 对取得的京东类目属性值列表按照value从小到大排序
//            sortedCategoryAttrValueList = this.getSortedList(jdCategoryAttrValueList);
//
//            // 类目属性值项目设定
//            for(CategoryAttrValue catAttrValue:sortedCategoryAttrValueList) {
//                // 属性值项目设定
//                Option attrValueOption =  new Option();
//                // 属性值id
//                attrValueOption.setValue(String.valueOf(catAttrValue.getId()));
//                // 属性值(画面显示值)
//                attrValueOption.setDisplayName(catAttrValue.getValue());
//
//                // 追加到选项列表中
//                optionList.add(attrValueOption);
//            }
//        }
//
//        return optionList;
//    }
//
//    /**
//     * 对京东类目属性值列表按照value从小到大排序
//     *
//     * @param jdCategoryAttrValueList List<CategoryAttrValue>  京东类目属性值列表
//     * @return List<CategoryAttrValue>    按照value从小到大排好序的列表
//     */
//    private List<CategoryAttrValue> getSortedList(List<CategoryAttrValue> jdCategoryAttrValueList) {
//        List<CategoryAttrValue> nullList = new ArrayList<>();
//        List<CategoryAttrValue> dataList = new ArrayList<>();
//
//        if (jdCategoryAttrValueList != null && jdCategoryAttrValueList.size() > 0) {
//            // 如果传入列表中有value值为空的数据
//            for (CategoryAttrValue categoryAttrValue : jdCategoryAttrValueList) {
//                if (categoryAttrValue.getId() == 0 || "".equals(String.valueOf(categoryAttrValue.getId()))) {
//                    nullList.add(categoryAttrValue);
//                } else {
//                    dataList.add(categoryAttrValue);
//                }
//            }
//
//            // 排序
//            for (int i = 0; i < dataList.size() - 1; i++) {
//                for (int j = 1; j < dataList.size() - i; j++) {
//                    Long a = dataList.get(j - 1).getId();
//                    Long b = dataList.get(j).getId();
//
//                    if (a.compareTo(b) > 0) { // 比较两个id的大小
//                        CategoryAttrValue temp = (CategoryAttrValue)dataList.get(j - 1);
//                        dataList.set((j - 1), dataList.get(j));
//                        dataList.set(j, temp);
//                    }
//                }
//            }
//
//            dataList.addAll(nullList);
//        }
//
//        return dataList;
//    }
//
//    /**
//     * 根据京东类目属性Feature列表取得Rule列表
//     *
//     * @param categoryAttr CategoryAttr  京东类目属性
//     * @return List<Rule>    Rule列表
//     */
//    private List<Rule> getCategoryAttrRulesList(CategoryAttr categoryAttr) {
//        List<Rule> rulesList = new ArrayList<>();
//
//        // 京东类目属性的Feature列表信息取得
//        Set<Feature> featureSet = categoryAttr.getAttrFeatures();
//        if (featureSet != null && featureSet.size() > 0) {
//            // rule作成
//            Rule rule = new RequiredRule();
//            // 循环feature列表作成rule
//            for (Feature feature:featureSet) {
//                // 必须rule作成
//                if ("isRequired".equals(feature.getAttrFeatureKey())) {
//                    // 必须的时候
//                    if("1".equals(feature.getAttrFeatureValue())) {
//                        rule.setName("requiredRule");
//                        rule.setValue("true");
//                        // Rule列表追加
//                        rulesList.add(rule);
//                    }
//                }
//            }
//        }
//
//        return rulesList;
//    }

}
