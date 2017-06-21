package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.factory.SchemaWriter;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.InputField;
import com.voyageone.common.masterdate.schema.field.MultiCheckField;
import com.voyageone.common.masterdate.schema.field.SingleCheckField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.rule.*;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ecerp.interfaces.third.koala.KoalaItempropsService;
import com.voyageone.ecerp.interfaces.third.koala.beans.KoalaConfig;
import com.voyageone.ecerp.interfaces.third.koala.beans.PropertyCategory;
import com.voyageone.ecerp.interfaces.third.koala.beans.PropertyValue;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.CmsMtPlatformSkusService;
import com.voyageone.service.impl.cms.PlatformCategoryService;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.model.cms.CmsMtPlatformSkusModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author tom.zhu on 2017/6/14.
 * @version 2.0.0
 */
@Service
@RabbitListener(queues = CmsMqRoutingKey.CMS_BATCH_PlatformCategorySchemaKLJob)
public class CmsBuildPaltformCategorySchemaKLMqService extends BaseMQCmsService {

    @Autowired
    private KoalaItempropsService koalaItempropsService;

    @Autowired
    private CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;
    @Autowired
    private PlatformCategoryService platformCategoryService;
    @Autowired
    private CmsMtPlatformSkusService cmsMtPlatformSkusService;

    // 考拉类目属性的输入属性类型(1:单行文本)
    private final static String Input_Type_1_TEXT = "TEXT";

    // 考拉类目属性的输入属性类型(2:多行文本)
    private final static String Input_Type_2_MULTITEXT = "TEXTAREA";

    // 考拉类目属性的输入属性类型(3:下拉列表)
    private final static String Input_Type_3_LIST = "3";

    // 考拉类目属性的输入属性类型(4:单选框)
    private final static String Input_Type_4_SIMPLECHECK = "RADIO";

    // 考拉类目属性的输入属性类型(5:多选框)
    private final static String Input_Type_5_MULTICHECK = "CHECKBOX";

    // 考拉类目属性的输入属性类型(6:文件)
    private final static String Input_Type_6_FILE = "6";

    // SKU属性类型(颜色)
    private final static String AttrType_Color = "c";
    // SKU属性类型(尺寸)
    private final static String AttrType_Size = "s";


    @Override
    public void onStartup(Map<String, Object> messageMap) throws Exception {
        String channelId = messageMap.get("channelId").toString();

        doLogic(channelId, CartEnums.Cart.KL.getId());

    }

    private void doLogic(String channelId, String cartId) {
        KoalaConfig shopBean = Shops.getShopKoala(channelId, cartId);

        // 获取叶子类目列表
        List<CmsMtPlatformCategoryTreeModel> allCategoryTreeLeaves = platformCategoryService.getCmsMtPlatformCategoryTreeModelLeafList(Integer.parseInt(cartId));

        // 删除考拉共通类目schema
        cmsMtPlatformCategorySchemaDao.deletePlatformCategorySchemaByCategory(Integer.parseInt(cartId), "1");
        // 创建考拉共通类目schema
        doSetPlatformSchemaCommon(Integer.parseInt(cartId));

        // 创建考拉各个平台叶子类目
        if (ListUtils.notNull(allCategoryTreeLeaves)) {
            int i = 1;
            int cnt = allCategoryTreeLeaves.size();

            for (CmsMtPlatformCategoryTreeModel platformCategoriesModel : allCategoryTreeLeaves) {
                // 删除已有的数据
                cmsMtPlatformCategorySchemaDao.deletePlatformCategorySchemaByCategory(Integer.parseInt(cartId), platformCategoriesModel.getCatId());

                $info("考拉类目属性:" + i + "/" + cnt + ":CHANNEL_ID:" + channelId + ":CART_ID:" + cartId + ":PLATFORM_CATEGORY_ID:" + platformCategoriesModel.getCatId());

                // 设置各个类目schema信息
                doSetPlatformPropSub(shopBean, platformCategoriesModel);

                i++;
            }

        }

    }

    /**
     * 考拉类目属性schema信息插入
     *
     * @param shopBean                ShopBean  店铺信息
     * @param platformCategoriesModel CmsMtPlatformCategoryTreeModel 叶子类目信息
     */
    protected void doSetPlatformPropSub(KoalaConfig shopBean, CmsMtPlatformCategoryTreeModel platformCategoriesModel) {

        CmsMtPlatformCategorySchemaModel schemaModel = new CmsMtPlatformCategorySchemaModel();
        schemaModel.setCartId(platformCategoriesModel.getCartId());
        schemaModel.setCatId(platformCategoriesModel.getCatId());
        schemaModel.setCreater(this.getTaskName());
        schemaModel.setModifier(this.getTaskName());
        schemaModel.setCatFullPath(platformCategoriesModel.getCatPath());

        // 从考拉平台取得类目schema属性值信息转换成为XML设置到mongoDB的schema表中的propsItem字段
        // 调用考拉API获取类目属性信息
        PropertyCategory[] props = null;
        try {
            props = koalaItempropsService.itempropsGet(shopBean, Long.parseLong(platformCategoriesModel.getCatId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 类目属性schema作成用field列表
        List<Field> fieldsList = new ArrayList<>();

        // 根据类目属性列表循环，取得类目属性值，加入schema field列表
        if (props == null) {
            return;
        }
        for (PropertyCategory categoryAttr : props) {
            // 考拉类目属性的属性类型(非sku属性的场合)
            if (0 == categoryAttr.getPropertyName().getRawPropertyName().getIsSku()) {

                // 类目属性的输入类型
                String inputType = categoryAttr.getPropertyName().getPropertyEditPolicy().getInputType();

                switch (inputType) {
                    case Input_Type_1_TEXT:
                    case Input_Type_2_MULTITEXT:
                        InputField inputField;
                        // 根据类目属性和属性值取得可输入field
                        inputField = getInputField(categoryAttr);
                        // 类目属性列表追加
                        fieldsList.add(inputField);
                        break;
                    case Input_Type_3_LIST:
                    case Input_Type_4_SIMPLECHECK:
                        // 单选
                        SingleCheckField singleCheckField;
                        // 根据类目属性和属性值取得单选field
                        singleCheckField = getSingleCheckField(categoryAttr);
                        // 类目属性列表追加
                        fieldsList.add(singleCheckField);
                        break;
                    case Input_Type_5_MULTICHECK:
                        // 多选
                        MultiCheckField multiCheckField;
                        // 根据类目属性和属性值取得多选field
                        multiCheckField = getMultiCheckField(categoryAttr);
                        // 类目属性列表追加
                        fieldsList.add(multiCheckField);
                        break;
                    default:
                        $error("其他类型[" + inputType + "]暂时不支持！");
                }
            }
        }

        // 根据属性值列表转换成XML文件
        String xmlContent;
        if (fieldsList.size() > 0) {
            xmlContent = SchemaWriter.writeRuleXmlString(fieldsList);
            schemaModel.setPropsItem(xmlContent);
        }

        // 把schema信息插进入到MangoDB中
        platformCategoryService.insertPlatformCategorySchema(schemaModel);

        // 开始处理销售属性（颜色、 尺码）
        // 取得voyageone_cms2.cms_mt_platform_skus表中当前渠道平台已经追加过的颜色和尺码件数信息(件数放在idx中返回)
        List<CmsMtPlatformSkusModel> existsPlatfomSkus = cmsMtPlatformSkusService.getModesByAttrType(shopBean.getChannelId(),
                shopBean.getCartId(), platformCategoriesModel.getCatId(), 1);
        // 该叶子类目id在cms_mt_platform_skus中已经存在的颜色件数
        long colorAttrCnt = existsPlatfomSkus
                .stream()
                .filter(s -> platformCategoriesModel.getCatId().equals(s.getPlatformCategoryId()) && AttrType_Color.equals(s.getAttrType()))
                .count();

        // 该叶子类目id在cms_mt_platform_skus中已经存在的尺码件数
        long sizeAttrCnt = existsPlatfomSkus
                .stream()
                .filter(s -> platformCategoriesModel.getCatId().equals(s.getPlatformCategoryId()) && AttrType_Size.equals(s.getAttrType()))
                .count();
        for (PropertyCategory categoryAttr : props) {
            // 考拉类目属性的属性类型(sku属性的场合)
            if (1 == categoryAttr.getPropertyName().getRawPropertyName().getIsSku()) {

                String attrType = AttrType_Size;
                if (1 == categoryAttr.getPropertyName().getRawPropertyName().getIsColor()) {
                    attrType = AttrType_Color;
                }

                if (AttrType_Color.equals(attrType) && colorAttrCnt > 0) {
                    continue;
                }
                if (AttrType_Size.equals(attrType) && sizeAttrCnt > 0) {
                    continue;
                }

                // 添加到数据库里
                addCategoryAttrValueToSkus(
                        shopBean.getChannelId(),
                        String.valueOf(shopBean.getCartId()),
                        platformCategoriesModel.getCatId(),
                        attrType,
                        Long.parseLong(categoryAttr.getRawPropertyCategory().getPropertyNameId()),
                        categoryAttr.getPropertyValueList());

            }
        }
    }

    /**
     * 创建考拉共通类目
     * @param cartId
     */
    protected void doSetPlatformSchemaCommon(int cartId) {
        CmsMtPlatformCategorySchemaModel schemaCommonModel = new CmsMtPlatformCategorySchemaModel();
        schemaCommonModel.setCartId(cartId);
        schemaCommonModel.setCatId("1");
        schemaCommonModel.setCreater(this.getTaskName());
        schemaCommonModel.setModifier(this.getTaskName());
        schemaCommonModel.setCatFullPath("考拉共通");

        // 类目属性schema作成用field列表
        List<Field> productFieldsList = new ArrayList<>();

        // 商品名称（最大64位字符）
        addInputField(productFieldsList, "name", "商品名称", "", false, "64", "默认中文长描述");

        // 副标题（最大200位字符）
        addInputField(productFieldsList, "sub_title", "副标题", "", true, "200", "副标题将用于商品详情页商品名称后的卖点描述");

        // 短标题（最大48位字符）
        addInputField(productFieldsList, "short_title", "短标题", "", true, "48", "短标题将用于移动端和web端首页商品标题、web端活动页商品标题");

        // 十字描述（最大8-24位字符）
        addInputField(productFieldsList, "ten_words_desc", "十字描述", "", true, "24", "最少8个字符， 最大24个字符，用于web端首页活动页商品标题后的卖点描述");

        // 商品货号（最大50位字符）
        addInputField(productFieldsList, "item_NO", "商品货号", "", false, "50", "默认用code");

        // 原产国id
        addInputField(productFieldsList, "original_country_code_id", "原产国id", "", true, null, "");

        // 商品毛重
        addInputField(productFieldsList, "gross_weight", "商品毛重（单位kg）", "", false, null, "默认master详情的重量(kg)，如果没值，此项必填，商品的毛重不能随意填写，将会推送至海关报关，如果和实际重量差别很大，会导致清关失败");

        // 商品外键id
        addInputField(productFieldsList, "Item_outer_id", "商品外键id", "", false, null, "默认用model");

        // 根据考拉平台上新时用到一些平台相关的输入项目转换成XML文件
        String schemaCommonXmlContent = null;
        if (productFieldsList.size() > 0) {
            schemaCommonXmlContent = SchemaWriter.writeRuleXmlString(productFieldsList);
            schemaCommonModel.setPropsProduct(schemaCommonXmlContent);
        }

        // 把schema共通信息插进入到MangoDB中
        platformCategoryService.insertPlatformCategorySchema(schemaCommonModel);
    }


    /**
     * 根据参数生成SingleCheckfield并添加到选项列表
     *
     * @param productFieldsList List<Field>  考拉产品上新共通属性值列表
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
     * 根据参数生成Inputfield并添加到选项列表
     *
     * @param productFieldsList List<Field>  考拉产品上新共通属性值列表
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
        if (maxLength != null) {
            MaxLengthRule maxLengthRule = new MaxLengthRule();
            maxLengthRule.setName("maxLengthRule");
            maxLengthRule.setValue(maxLength);
            maxLengthRule.setExProperty("include");
            maxLengthRule.setUnit("character");  // 可选"byte"或"character"，这里固定成"character"
            rulesList.add(maxLengthRule);
        }

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
     * 根据考拉类目属性值列表取得多选field选项列表
     *
     * @param categoryAttr     CategoryAttr      考拉类目属性
     * @return InputField    可输入field对象
     */
    private InputField getInputField(PropertyCategory categoryAttr) {
        // 可输入
        InputField inputField = new InputField();

        // field共通信息设定
        // 类目属性id
        inputField.setId(String.valueOf(categoryAttr.getRawPropertyCategory().getPropertyNameId()));
        // 类目属性名
        // 类目属性名(如果为空，则设为类目属性id)
        if (StringUtils.isEmpty(categoryAttr.getPropertyName().getRawPropertyName().getPropNameCn())) {
            inputField.setName(String.valueOf(categoryAttr.getRawPropertyCategory().getPropertyNameId()));
        } else {
            inputField.setName(categoryAttr.getPropertyName().getRawPropertyName().getPropNameCn());
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
     * 根据考拉类目属性值列表取得单选field选项列表
     *
     * @param categoryAttr     CategoryAttributeSearchResponse.Attribute      考拉类目属性
     * @return SingleCheckField    单选field对象
     */
    private SingleCheckField getSingleCheckField(PropertyCategory categoryAttr) {
        // 多选
        SingleCheckField singleCheckField = new SingleCheckField();
        // 项目列表
        List<Option> attrValueOptionList;

        // field共通信息设定
        // 类目属性id
        singleCheckField.setId(String.valueOf(categoryAttr.getRawPropertyCategory().getPropertyNameId()));
        // 类目属性名(如果为空，则设为类目属性id)
        if (StringUtils.isEmpty(categoryAttr.getPropertyName().getRawPropertyName().getPropNameCn())) {
            singleCheckField.setName(String.valueOf(categoryAttr.getRawPropertyCategory().getPropertyNameId()));
        } else {
            singleCheckField.setName(categoryAttr.getPropertyName().getRawPropertyName().getPropNameCn());
        }
        // 类目属性类型
        singleCheckField.setType(FieldTypeEnum.SINGLECHECK);

        // field的rule信息设定
        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
        if (ListUtils.notNull(rulesList)) {
            singleCheckField.setRules(rulesList);
        }

        // 类目属性值选项列表取得
        attrValueOptionList = this.getOptionList(categoryAttr);
        // 类目属性选项列表设定
        if (ListUtils.notNull(attrValueOptionList)) {
            singleCheckField.setOptions(attrValueOptionList);
        }

        return singleCheckField;
    }

    /**
     * 根据考拉类目属性值列表取得多选field选项列表
     *
     * @param categoryAttr     CategoryAttr      考拉类目属性
     * @return MultiCheckField    多选field对象
     */
    private MultiCheckField getMultiCheckField(PropertyCategory categoryAttr) {
        // 多选
        MultiCheckField multiCheckField = new MultiCheckField();
        // 项目列表
        List<Option> attrValueOptionList;

        // field共通信息设定
        // 类目属性id
        multiCheckField.setId(String.valueOf(categoryAttr.getRawPropertyCategory().getPropertyNameId()));
        // 类目属性名(如果为空，则设为类目属性id)
        if (StringUtils.isEmpty(categoryAttr.getPropertyName().getRawPropertyName().getPropNameCn())) {
            multiCheckField.setName(String.valueOf(categoryAttr.getRawPropertyCategory().getPropertyNameId()));
        } else {
            multiCheckField.setName(categoryAttr.getPropertyName().getRawPropertyName().getPropNameCn());
        }
        // 类目属性类型
        multiCheckField.setType(FieldTypeEnum.MULTICHECK);

        // field的rule信息设定
        List<Rule> rulesList = this.getCategoryAttrRulesList(categoryAttr);
        if (rulesList != null && rulesList.size() > 0) {
            multiCheckField.setRules(rulesList);
        }

        // 类目属性值选项列表取得
        attrValueOptionList = this.getOptionList(categoryAttr);
        // 类目属性选项列表设定
        multiCheckField.setOptions(attrValueOptionList);

        return multiCheckField;
    }

    /**
     * 根据考拉类目属性值列表取得field选项列表
     *
     * @param categoryAttr PropertyCategory
     * @return List<Option>    field选项列表
     */
    private List<Option> getOptionList(PropertyCategory categoryAttr) {
        List<Option> optionList = new ArrayList<>();

        if (categoryAttr.getPropertyValueList() != null && categoryAttr.getPropertyValueList().length > 0) {

            // 类目属性值项目设定
            for (PropertyValue catAttrValue : categoryAttr.getPropertyValueList()) {
                // 属性值项目设定
                Option attrValueOption =  new Option();
                // 属性值id
                attrValueOption.setValue(String.valueOf(catAttrValue.getPropertyValueId()));
                // 属性值(画面显示值)
                attrValueOption.setDisplayName(catAttrValue.getPropertyValue());

                // 追加到选项列表中
                optionList.add(attrValueOption);
            }
        }

        return optionList;
    }


    /**
     * 根据考拉类目属性Feature列表取得Rule列表
     *
     * @param categoryAttr CategoryAttr  考拉类目属性
     * @return List<Rule>    Rule列表
     */
    private List<Rule> getCategoryAttrRulesList(PropertyCategory categoryAttr) {
        List<Rule> rulesList = new ArrayList<>();

        // 根据考拉类目属性的设置Rule规则
        if (categoryAttr != null) {
            // 必填Rule设定
            if (1 == categoryAttr.getPropertyName().getPropertyEditPolicy().getIsNecessary()) {
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

    /**
     * 将平台上取得的指定类目属性id下面的属性值列表信息回写到cms_mt_platform_skus表中
     *
     * @param channelId 渠道id
     * @param cartId    平台id
     * @param catId     叶子类目id
     * @param attrType  销售属性类型（颜色c或尺码s）
     * @param attrId    类目属性id
     * @param propValueList 类目属性值列表
     */
    protected void addCategoryAttrValueToSkus(String channelId, String cartId, String catId, String attrType,
                                              Long attrId, PropertyValue[] propValueList) {

        // 遍历类目属性值列表(按IndexId升序排列)，插入新的颜色和尺码记录到cms_mt_platform_skus表
        List<CmsMtPlatformSkusModel> skuModels = new ArrayList<>();
        String modifier = getTaskName();
        Date currentTime = DateTimeUtil.getDate();
        for (PropertyValue attValue : propValueList) {
            CmsMtPlatformSkusModel skuModel = new CmsMtPlatformSkusModel();
            skuModel.setChannelId(channelId);
            skuModel.setCartId(NumberUtils.toInt(cartId));
            skuModel.setPlatformCategoryId(catId);
            skuModel.setAttrType(attrType);
            skuModel.setIdx(NumberUtils.toInt(StringUtils.toString(attValue.getShowOrder())));
            skuModel.setAttrName(attValue.getPropertyValue());
            // 属性值(类目属性id + ":" + 类目属性值id)
            skuModel.setAttrValue(StringUtils.toString(attrId) + ":" + attValue.getPropertyValueId());
            skuModel.setActive(true);
            skuModel.setCreated(currentTime);
            skuModel.setCreater(modifier);
            skuModel.setModified(currentTime);
            skuModel.setCreater(modifier);

            skuModels.add(skuModel);
        }

        // 插入cms_mt_platform_skus表
        cmsMtPlatformSkusService.insertCategorySaleAttrList(skuModels);
    }

}
