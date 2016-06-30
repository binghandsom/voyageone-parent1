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
import com.voyageone.common.masterdate.schema.rule.*;
import com.voyageone.common.util.ListUtils;
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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 京东平台类目schema信息取得
 *
 * @author desmond on 2016/4/9.
 * @version 2.2.0
 * @since 2.0.0
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformCategorySchemaJdJob)
public class CmsBuildPlatformCategorySchemaJdMqService extends BaseMQCmsService {

    @Autowired
    private JdCategoryService jdCategoryService;

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
        if (ListUtils.notNull(orderChannelIdList)) {
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
     * @param shop   ShopBean 店铺信息
     * @param cartId int    平台ID
     */
    private void doSetPlatformCategoryAttrJd(ShopBean shop, int cartId) {

        List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = new ArrayList<>();

        // 取得类目属性叶子数据并去掉重复叶子类目
        allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(cartId);

        // 去掉重复项的类目叶子件数大于0的场合
        if (ListUtils.notNull(allCategoryTreeLeaves)) {
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
                // 设置schema共通信息
                if (i == 1) {
                    doSetPlatformJdSchemaCommon(shop, platformCategoriesModel);
                }
                // 设置各个类目schema信息
                doSetPlatformPropJdSub(shop, platformCategoriesModel);

                i++;
            }
        }
    }

    /**
     * 京东类目属性schema信息插入
     *
     * @param shop                    ShopBean  店铺信息
     * @param platformCategoriesModel CmsMtPlatformCategoryTreeModel 叶子类目信息
     */
    private void doSetPlatformPropJdSub(ShopBean shop, CmsMtPlatformCategoryTreeModel platformCategoriesModel) {

        CmsMtPlatformCategorySchemaModel schemaModel = new CmsMtPlatformCategorySchemaModel();
        schemaModel.setCartId(platformCategoriesModel.getCartId());
        schemaModel.setCatId(platformCategoriesModel.getCatId());
        schemaModel.setCreater(this.getTaskName());
        schemaModel.setModifier(this.getTaskName());
        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());

        // 从京东平台取得类目schema属性值信息转换成为XML设置到mongoDB的schema表中的propsItem字段
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
                    default:
                        $error("复杂类型[" + inputType + "]不能作为属性值来使用！");
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
     * 设置京东类目schema共通信息
     * 将京东平台上新时用到一些平台相关的输入项目转换成XML设置到mongoDB的schema表中的propsItem字段
     *
     * @param shop ShopBean  店铺信息
     * @param platformCategoriesModel CmsMtPlatformCategoryTreeModel 叶子类目信息
     */
    private void doSetPlatformJdSchemaCommon(ShopBean shop, CmsMtPlatformCategoryTreeModel platformCategoriesModel) {

        CmsMtPlatformCategorySchemaModel schemaCommonModel = new CmsMtPlatformCategorySchemaModel();
        schemaCommonModel.setCartId(platformCategoriesModel.getCartId());
        schemaCommonModel.setCatId("1");
        schemaCommonModel.setCreater(this.getTaskName());
        schemaCommonModel.setModifier(this.getTaskName());
        schemaCommonModel.setCatFullPath("京东共通");

        // 类目属性schema作成用field列表
        List<Field> productFieldsList = new ArrayList<>();

        // 商品标题（最大45位字符）
        addInputField(productFieldsList, "productTitle", "商品名称", "", true, "45", "");

        // 长(单位:mm) (必须，最大长度5位, 默认值为50mm)
        addInputField(productFieldsList, "productLengthMm", "长", "50", true, "5", "单位:mm");

        // 宽(单位:mm) (必须，最大长度5位, 默认值为50mm)
        addInputField(productFieldsList, "productWideMm", "宽", "50", true, "5", "单位:mm");

        // 高(单位:mm) (必须，最大长度5位, 默认值为50mm)
        addInputField(productFieldsList, "productHighMm", "高", "50", true, "5", "单位:mm");

        // 重量(单位:kg) (必须，最大长度4位(9999公斤), 默认值为1公斤)
        // 京东后台显示的是公斤
        addInputField(productFieldsList, "productWeightKg", "重量", "1", true, "4", "单位:公斤");

        // 是否先款后货, false为否，true为是(非必须，默认值为true)
        List<Option> isPayFirstOptionList = new ArrayList<>();
        Option isPayFirstOptionTrue = new Option();
        isPayFirstOptionTrue.setValue("true");
        isPayFirstOptionTrue.setDisplayName("是");
        isPayFirstOptionList.add(isPayFirstOptionTrue);
        Option isPayFirstOptionFalse = new Option();
        isPayFirstOptionFalse.setValue("false");
        isPayFirstOptionFalse.setDisplayName("否");
        isPayFirstOptionList.add(isPayFirstOptionFalse);
        addSingleCheckField(productFieldsList, "productIsPayFirst", "是否先款后货",
                isPayFirstOptionTrue.getValue(), false, isPayFirstOptionList);

        // 是否限制开增值税发票(非必须，默认值为true)
        List<Option> isCanVatOptionList = new ArrayList<>();
        Option isCanVatOptionTrue = new Option();
        isCanVatOptionTrue.setValue("true");
        isCanVatOptionTrue.setDisplayName("是");
        isCanVatOptionList.add(isCanVatOptionTrue);
        Option isCanVatOptionFalse = new Option();
        isCanVatOptionFalse.setValue("false");
        isCanVatOptionFalse.setDisplayName("否");
        isCanVatOptionList.add(isCanVatOptionFalse);
        addSingleCheckField(productFieldsList, "productIsCanVat", "是否限制开增值税发票",
                isCanVatOptionTrue.getValue(), false, isCanVatOptionList);

        // 根据京东平台上新时用到一些平台相关的输入项目转换成XML文件
        String schemaCommonXmlContent = null;
        if (productFieldsList.size() > 0) {
            schemaCommonXmlContent = SchemaWriter.writeRuleXmlString(productFieldsList);
            schemaCommonModel.setPropsItem(schemaCommonXmlContent);
        }

        // 把schema共通信息插进入到MangoDB中
        platformCategoryService.insertPlatformCategorySchema(schemaCommonModel);
    }

    /**
     * 根据参数生成Inputfield并添加到选项列表
     *
     * @param productFieldsList List<Field>  京东产品上新共通属性值列表
     * @param fieldId String  filedId
     * @param fieldName String  filed名称
     * @param defaultValue String 默认值
     * @param isRequired boolean  是否必须输入
     * @param maxLength String  可输入最大字符数
     * @return 空
     */
    private void addInputField(List<Field> productFieldsList, String fieldId, String fieldName, String defaultValue,
                               boolean isRequired, String maxLength, String tipRuleString) {
        // 输入框Field
        InputField inputField = new InputField();

        // field共通信息设定
        // 属性id
        inputField.setId(fieldId);
        // 属性名(如果为空，则设为类目属性id)
        inputField.setName(!StringUtils.isEmpty(fieldName) ? fieldName : fieldId);
        // 类目属性类型
        inputField.setType(FieldTypeEnum.INPUT);
        // 属性默认值
        inputField.setDefaultValue(defaultValue);

        // field的rule信息设定
        List<Rule> rulesList = new ArrayList<>();
        // valueTypeRule
        Rule valueTypeRule = new ValueTypeRule();
        valueTypeRule.setName("valueTypeRule");
        valueTypeRule.setValue("text");
        rulesList.add(valueTypeRule);

        // requiredRule
        if (isRequired) {
            Rule requiredRule = new RequiredRule();
            requiredRule.setName("requiredRule");
            requiredRule.setValue("true");
            rulesList.add(requiredRule);
        }

        // minLengthRule
        MinLengthRule minLengthRule = new MinLengthRule();
        minLengthRule.setName("minLengthRule");
        minLengthRule.setValue(isRequired ? "1" : "0");  // 必须输入时，最小值设为"1"
        minLengthRule.setExProperty("include");
        minLengthRule.setUnit("character");  // 可选"byte"或"character"，这里固定成"character"
        rulesList.add(minLengthRule);

        // maxLengthRule
        MaxLengthRule maxLengthRule = new MaxLengthRule();
        maxLengthRule.setName("maxLengthRule");
        maxLengthRule.setValue(maxLength);
        maxLengthRule.setExProperty("include");
        maxLengthRule.setUnit("character");  // 可选"byte"或"character"，这里固定成"character"
        rulesList.add(maxLengthRule);

        // tipRule
        if (!StringUtils.isEmpty(tipRuleString)) {
            TipRule tipRule = new TipRule();
            tipRule.setName("tipRule");
            tipRule.setValue(tipRuleString);
            rulesList.add(tipRule);
        }

        // Rule列表追加
        if (ListUtils.notNull(rulesList)) {
            inputField.setRules(rulesList);
        }
        // 类目属性列表追加
        productFieldsList.add(inputField);
    }

    /**
     * 根据参数生成SingleCheckfield并添加到选项列表
     *
     * @param productFieldsList List<Field>  京东产品上新共通属性值列表
     * @param fieldId String  filedId
     * @param fieldName String  filed名称
     * @param defaultValue String 默认值
     * @param isRequired boolean  是否必须输入(只支持必须requiredRule,不支持tipRule,disableRule,devTipRule等)
     * @param  optionList List<Option> 可选值列表
     * @return 空
     */
    private void addSingleCheckField(List<Field> productFieldsList, String fieldId, String fieldName,
                                     String defaultValue, boolean isRequired, List<Option> optionList) {
        // 单选
        SingleCheckField singleCheckField = new SingleCheckField();
        // field共通信息设定
        // 属性id
        singleCheckField.setId(fieldId);
        // 属性名(如果为空，则设为类目属性id)
        singleCheckField.setName(!StringUtils.isEmpty(fieldName) ? fieldName : fieldId);
        // 类目属性类型
        singleCheckField.setType(FieldTypeEnum.SINGLECHECK);

        // field的rule信息设定
        // rule作成
        List<Rule> rulesList = new ArrayList<>();

        // requiredRule
        if (isRequired) {
            Rule requiredRule = new RequiredRule();
            requiredRule.setName("requiredRule");
            requiredRule.setValue("true");
            rulesList.add(requiredRule);
        }

        // Rule列表追加
        if (ListUtils.notNull(rulesList)) {
            singleCheckField.setRules(rulesList);
        }

        // 单选可选项列表设定
        if (ListUtils.notNull(optionList)) {
            singleCheckField.setOptions(optionList);
        }

        // 属性默认值
        if (!StringUtils.isEmpty(defaultValue)) {
            singleCheckField.setDefaultValue(defaultValue);
        }

        // 选可选项列表追加
        productFieldsList.add(singleCheckField);
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
        // 类目属性名(如果为空，则设为类目属性id)
        if (StringUtils.isEmpty(categoryAttr.getName())) {
            singleCheckField.setName(String.valueOf(categoryAttr.getAid()));
        } else {
            singleCheckField.setName(categoryAttr.getName());
        }
        // 类目属性类型
        singleCheckField.setType(FieldTypeEnum.SINGLECHECK);

        // field的rule信息设定
        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
        if (ListUtils.notNull(rulesList)) {
            singleCheckField.setRules(rulesList);
        }

        // 类目属性值选项列表取得
        attrValueOptionList = this.getOptionList(jdCategoryAttrValueList);
        // 类目属性选项列表设定
        if (ListUtils.notNull(attrValueOptionList)) {
            singleCheckField.setOptions(attrValueOptionList);
        }

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
        // 类目属性名(如果为空，则设为类目属性id)
        if (StringUtils.isEmpty(categoryAttr.getName())) {
            multiCheckField.setName(String.valueOf(categoryAttr.getAid()));
        } else {
            multiCheckField.setName(categoryAttr.getName());
        }
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
        // 类目属性名(如果为空，则设为类目属性id)
        if (StringUtils.isEmpty(categoryAttr.getName())) {
            inputField.setName(String.valueOf(categoryAttr.getAid()));
        } else {
            inputField.setName(categoryAttr.getName());
        }
        // 类目属性类型
        inputField.setType(FieldTypeEnum.INPUT);

        // field的rule信息设定
        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
        if (ListUtils.notNull(rulesList)) {
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

        if (ListUtils.notNull(jdCategoryAttrValueList)) {
            // 对取得的京东类目属性值列表按照value从小到大排序
//            sortedCategoryAttrValueList = this.getSortedList(jdCategoryAttrValueList);
            sortedCategoryAttrValueList = jdCategoryAttrValueList.stream()
                                          .sorted(Comparator.comparing(p -> p.getIndexId()))
                                          .collect(Collectors.toList());

            // 类目属性值项目设定
            for (AttValue catAttrValue : sortedCategoryAttrValueList) {
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
