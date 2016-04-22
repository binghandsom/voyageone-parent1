package com.voyageone.task2.cms.service;

import com.jd.open.api.sdk.domain.category.AttValue;
import com.jd.open.api.sdk.response.category.CategoryAttributeSearchResponse;
import com.voyageone.common.configs.Enums.PlatFormEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.rule.RequiredRule;
import com.voyageone.common.masterdate.schema.rule.Rule;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.jd.service.JdCategoryService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by desmond on 2016/4/9.
 * 京东平台类目schema信息取得
 */
@Service
public class CmsBuildPlatformCategorySchemaJdMqService extends BaseMQCmsService {

    @Autowired
    JdCategoryService jdCategoryService;

    @Autowired
    private PlatformCategoryService platformCategoryService;

    // 京东类目属性的属性类型(3:可变属性)
    private final static String Attribute_Type_3 = "3";

    // 京东类目属性的输入属性类型(1:dropbox 单选)
    private final static int Input_Type_1 = 1;

    // 京东类目属性的输入属性类型(2:checkbox 多选)
    private final static int Input_Type_2 = 2;

    // 京东类目属性的输入属性类型(3:inputfield 可输入)
    private final static int Input_Type_3 = 3;

    @RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformCategorySchemaJdJob)
    protected void onMessage(Message message){
        super.onMessage(message);
    }

    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
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
                    doSetPlatformCategoryAttrJd(shop, Integer.parseInt(shop.getCart_id()));
                }

            }
        }

        //正常结束
        $info("正常结束");
    }

    /**
     * 京东平台类目属性信息取得
     *
     * @param shop ShopBean 店铺信息
     * @param cartId int    平台ID
     */
    private void doSetPlatformCategoryAttrJd(ShopBean shop, int cartId) {

        List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = new ArrayList<>();

        // 取得类目属性叶子数据并去掉重复叶子类目
        allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(cartId);

        // 去掉重复项的类目叶子件数大于0的场合
        if (allCategoryTreeLeaves.size() > 0) {
            int i = 1;
            int cnt = allCategoryTreeLeaves.size();

            // 删除已有的数据
            int delNum = 0;
            delNum = platformCategoryService.deletePlatformCategorySchemaByCartId(cartId);
            $info("批量删除Schema, CART_ID 为：" + cartId + " 的数据为: " + delNum + "条...");

            // 取得每个叶子类目的属性和属性值插入到MangoDB的schema表中
            for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allCategoryTreeLeaves) {
                // 京东类目属性schema信息插入
                $info("京东类目属性:" + i + "/" + cnt + ":CHANNEL_ID:" + platformCategoriesModel.getChannelId() + ":CART_ID:" + platformCategoriesModel.getCartId() + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getCatId());
                doSetPlatformPropJdSub(shop, platformCategoriesModel);

                i++;
            }
        }
    }

    /**
     * 京东类目属性schema信息插入
     *
     * @param shop ShopBean  店铺信息
     * @param platformCategoriesModel CmsMtPlatformCategoryTreeModel 叶子类目信息
     */
    private void doSetPlatformPropJdSub(ShopBean shop, CmsMtPlatformCategoryTreeModel platformCategoriesModel) {

        CmsMtPlatformCategorySchemaModel schemaModel = new CmsMtPlatformCategorySchemaModel();
        schemaModel.setCartId(platformCategoriesModel.getCartId());
        schemaModel.setCatId(platformCategoriesModel.getCatId());
        schemaModel.setCreater(this.getTaskName());
        schemaModel.setModifier(this.getTaskName());
        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());

        // 调用京东API获取类目属性信息
        List<CategoryAttributeSearchResponse.Attribute> jdCategoryAttrList = new ArrayList<>();
        // 调用京东API获取类目属性信息
        jdCategoryAttrList = jdCategoryService.getCategoryAttrInfo(shop, platformCategoriesModel.getCatId());

        // 类目属性schema作成用field列表
        List<Field> fieldsList = new ArrayList<>();

        // 根据类目属性列表循环，取得类目属性值，加入schema field列表
        for (CategoryAttributeSearchResponse.Attribute categoryAttr : jdCategoryAttrList) {

            // 京东类目属性的属性类型(1:关键属性 2:不变属性 3:可变属性 4:销售属性)
            if (Attribute_Type_3.equals(categoryAttr.getAttType())) {
                // 只有当属性类型是(3:可变属性)的时候才保存到DB

                List<AttValue> jdCategoryAttrValueList = new ArrayList<>();

                // 类目属性的输入类型（1:单选 2:多选 3:可输入）
                int inputType = Integer.parseInt(String.valueOf(categoryAttr.getInputType()));
                // 单选和多选的时候，取得属性值
                if (Input_Type_1 == inputType || Input_Type_2 == inputType) {
                    // 调用京东API获取类目属性值信息
                    jdCategoryAttrValueList = jdCategoryService.getCategoryAttrValueInfo(shop, categoryAttr.getAid());
                }

                switch (inputType) {
                    case Input_Type_1:
                        // 单选
                        SingleCheckField singleCheckField = new SingleCheckField();
                        // 根据类目属性和属性值取得单选field
                        singleCheckField = getSingleCheckField(categoryAttr, jdCategoryAttrValueList);
                        // 类目属性列表追加
                        fieldsList.add(singleCheckField);
                        break;
                    case Input_Type_2:
                        // 多选
                        MultiCheckField multiCheckField = new MultiCheckField();
                        // 根据类目属性和属性值取得多选field
                        multiCheckField = getMultiCheckField(categoryAttr, jdCategoryAttrValueList);
                        // 类目属性列表追加
                        fieldsList.add(multiCheckField);
                        break;
                    case Input_Type_3:
                        // 可输入
                        InputField inputField = new InputField();
                        // 根据类目属性和属性值取得可输入field
                        inputField = getInputField(categoryAttr);
                        // 类目属性列表追加
                        fieldsList.add(inputField);
                        break;
                }
            }
        }

        // 根据属性值列表转换成XML文件
        String xmlContent = null;
        if (fieldsList.size() > 0) {
            xmlContent = SchemaWriter.writeRuleXmlString(fieldsList);
            schemaModel.setPropsItem(xmlContent);
        }

        // 把schema信息插进入到MangoDB中
        platformCategoryService.insertPlatformCategorySchema(schemaModel);
    }

    /**
     * 根据京东类目属性值列表取得单选field选项列表
     *
     * @param categoryAttr     CategoryAttributeSearchResponse.Attribute      京东类目属性
     * @param jdCategoryAttrValueList  List<CategoryAttrValue>  京东类目属性值列表
     * @return SingleCheckField    单选field对象
     */
    private SingleCheckField getSingleCheckField(CategoryAttributeSearchResponse.Attribute categoryAttr, List<AttValue> jdCategoryAttrValueList) {
        // 多选
        SingleCheckField singleCheckField = new SingleCheckField();
        // 项目列表
        List<Option> attrValueOptionList = new ArrayList<>();

        // field共通信息设定
        // 类目属性id
        singleCheckField.setId(String.valueOf(categoryAttr.getAid()));
        // 类目属性名
        singleCheckField.setName(categoryAttr.getName());
        // 类目属性类型
        singleCheckField.setType(FieldTypeEnum.SINGLECHECK);

        // field的rule信息设定
        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
        if (rulesList != null && rulesList.size() > 0) {
            singleCheckField.setRules(rulesList);
        }

        // 类目属性值选项列表取得
        attrValueOptionList = this.getOptionList(jdCategoryAttrValueList);
        // 类目属性选项列表设定
        singleCheckField.setOptions(attrValueOptionList);

        return singleCheckField;
    }

    /**
     * 根据京东类目属性值列表取得多选field选项列表
     *
     * @param categoryAttr     CategoryAttr      京东类目属性
     * @param jdCategoryAttrValueList  List<CategoryAttrValue>  京东类目属性值列表
     * @return MultiCheckField    多选field对象
     */
    private MultiCheckField getMultiCheckField(CategoryAttributeSearchResponse.Attribute categoryAttr, List<AttValue> jdCategoryAttrValueList) {
        // 多选
        MultiCheckField multiCheckField = new MultiCheckField();
        // 项目列表
        List<Option> attrValueOptionList = new ArrayList<>();

        // field共通信息设定
        // 类目属性id
        multiCheckField.setId(String.valueOf(categoryAttr.getAid()));
        // 类目属性名
        multiCheckField.setName(categoryAttr.getName());
        // 类目属性类型
        multiCheckField.setType(FieldTypeEnum.MULTICHECK);

        // field的rule信息设定
        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
        if (rulesList != null && rulesList.size() > 0) {
            multiCheckField.setRules(rulesList);
        }

        // 类目属性值选项列表取得
        attrValueOptionList = this.getOptionList(jdCategoryAttrValueList);
        // 类目属性选项列表设定
        multiCheckField.setOptions(attrValueOptionList);

        return multiCheckField;
    }

    /**
     * 根据京东类目属性值列表取得多选field选项列表
     *
     * @param categoryAttr     CategoryAttr      京东类目属性
     * @return InputField    可输入field对象
     */
    private InputField getInputField(CategoryAttributeSearchResponse.Attribute categoryAttr) {
        // 可输入
        InputField inputField = new InputField();

        // field共通信息设定
        // 类目属性id
        inputField.setId(String.valueOf(categoryAttr.getAid()));
        // 类目属性名
        inputField.setName(categoryAttr.getName());
        // 类目属性类型
        inputField.setType(FieldTypeEnum.INPUT);

        // field的rule信息设定
        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
        if (rulesList != null && rulesList.size() > 0) {
            inputField.setRules(rulesList);
        }

        // 类目属性选项列表不需要设定

        return inputField;
    }

    /**
     * 根据京东类目属性值列表取得field选项列表
     *
     * @param jdCategoryAttrValueList List<AttValue>  京东类目属性列表
     * @return List<Option>    field选项列表
     */
    private List<Option> getOptionList(List<AttValue> jdCategoryAttrValueList) {
        List<AttValue> sortedCategoryAttrValueList = new ArrayList<>();
        List<Option> optionList = new ArrayList<>();

        if (jdCategoryAttrValueList != null && jdCategoryAttrValueList.size() > 0) {
            // 对取得的京东类目属性值列表按照value从小到大排序
            sortedCategoryAttrValueList = this.getSortedList(jdCategoryAttrValueList);

            // 类目属性值项目设定
            for(AttValue catAttrValue:sortedCategoryAttrValueList) {
                // 属性值项目设定
                Option attrValueOption =  new Option();
                // 属性值id
                attrValueOption.setValue(String.valueOf(catAttrValue.getVid()));
                // 属性值(画面显示值)
                attrValueOption.setDisplayName(catAttrValue.getName());

                // 追加到选项列表中
                optionList.add(attrValueOption);
            }
        }

        return optionList;
    }

    /**
     * 对京东类目属性值列表按照value从小到大排序
     *
     * @param jdCategoryAttrValueList List<CategoryAttrValue>  京东类目属性值列表
     * @return List<CategoryAttrValue>    按照value从小到大排好序的列表
     */
    private List<AttValue> getSortedList(List<AttValue> jdCategoryAttrValueList) {
        List<AttValue> nullList = new ArrayList<>();
        List<AttValue> dataList = new ArrayList<>();

        if (jdCategoryAttrValueList != null && jdCategoryAttrValueList.size() > 0) {
            // 如果传入列表中有value值为空的数据
            for (AttValue categoryAttrValue : jdCategoryAttrValueList) {
                if (categoryAttrValue.getVid() == 0 || "".equals(String.valueOf(categoryAttrValue.getVid()))) {
                    nullList.add(categoryAttrValue);
                } else {
                    dataList.add(categoryAttrValue);
                }
            }

            // 排序
            for (int i = 0; i < dataList.size() - 1; i++) {
                for (int j = 1; j < dataList.size() - i; j++) {
                    Long a = dataList.get(j - 1).getVid();
                    Long b = dataList.get(j).getVid();

                    if (a.compareTo(b) > 0) { // 比较两个id的大小
                        AttValue temp = (AttValue)dataList.get(j - 1);
                        dataList.set((j - 1), dataList.get(j));
                        dataList.set(j, temp);
                    }
                }
            }

            dataList.addAll(nullList);
        }

        return dataList;
    }

    /**
     * 根据京东类目属性Feature列表取得Rule列表
     *
     * @param categoryAttr CategoryAttr  京东类目属性
     * @return List<Rule>    Rule列表
     */
    private List<Rule> getCategoryAttrRulesList(CategoryAttributeSearchResponse.Attribute categoryAttr) {
        List<Rule> rulesList = new ArrayList<>();

        // 根据京东类目属性的设置Rule规则
        if (categoryAttr != null) {
            // 必填Rule设定
            if ("true".equals(categoryAttr.getReq())) {
                // rule作成
                Rule rule = new RequiredRule();
                rule.setName("requiredRule");
                rule.setValue("true");
                // Rule列表追加
                rulesList.add(rule);
            }
        }

        return rulesList;
    }
}
