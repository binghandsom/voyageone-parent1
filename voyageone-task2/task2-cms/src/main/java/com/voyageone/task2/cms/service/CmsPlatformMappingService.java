package com.voyageone.task2.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.*;
import com.voyageone.service.dao.cms.CmsMtCommonPropDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformMappingDao;
import com.voyageone.service.model.cms.CmsMtCommonPropModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingForInsertModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformMappingModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.*;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by james.li on 2015/12/7.
 * 主数据->平台的mapping做成
 */
@Service
public class CmsPlatformMappingService extends BaseTaskService {
    private final static String JOB_NAME = "platformMappingTask";

    @Autowired
    CmsMtPlatformCategoryDao cmsMtPlatformCategoryDao;

    @Autowired
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Autowired
    CmsMtPlatformMappingDao cmsMtPlatformMappingDao;

    @Autowired
    CmsMtCommonPropDao cmsMtCommonPropDao;

    // CmsMtCommonProp数据
    private static List<CmsMtCommonPropModel> commonProp;

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

        commonProp = cmsMtCommonPropDao.selectCommonProp().stream().filter(cmsMtCommonPropModel -> !StringUtils.isEmpty(cmsMtCommonPropModel.getPlatformPropRefId())).collect(Collectors.toList());

        for (TaskControlBean taskControl : taskControlList) {
            if ("order_channel_id".equalsIgnoreCase(taskControl.getCfg_name())) {
                String channelId = taskControl.getCfg_val1();
                int cartId = Integer.parseInt(taskControl.getCfg_val2());
                // 获取该渠道下所有类目树
                List<CmsMtPlatformCategoryTreeModel> platformCategoryTree = cmsMtPlatformCategoryDao.selectByChannel_CartId(channelId, cartId);
                // 每棵数据循环
                for (CmsMtPlatformCategoryTreeModel platformCategory : platformCategoryTree) {
                    // 找出这棵树下所有的叶子节点
                    List<CmsMtPlatformCategoryTreeModel> finallyCategories = getFinallyCategories(platformCategory.getChannelId(), platformCategory.getCartId(), platformCategory.getCatId());
                    // 叶子节点循环
                    for (CmsMtPlatformCategoryTreeModel finallyCategory : finallyCategories) {
                        // 该叶子节点mapping关系没有生成过的场合
                        if (cmsMtPlatformMappingDao.isExist(channelId, cartId, finallyCategory.getCatId()) == 0) {
//                        if (cmsMtPlatformMappingDao.getMapping(channelId, cartId, finallyCategory.getCatId()) == null) {
                            logger.info(finallyCategory.getCatPath());
                            finallyCategory.setCartId(platformCategory.getCartId());
                            // 生成mapping关系数据并插入
                            {
                                CmsMtPlatformMappingModel cmsMtPlatformMappingModel = makePlatformMapping(finallyCategory);

                                List<MappingBean> mappingBeanList = cmsMtPlatformMappingModel.getProps();
                                List<Map<String, Object>> testList = JacksonUtil.jsonToMapList(JacksonUtil.bean2JsonNotNull(mappingBeanList));

                                CmsMtPlatformMappingForInsertModel cmsMtPlatformMappingForInsertModel = new CmsMtPlatformMappingForInsertModel();
                                cmsMtPlatformMappingForInsertModel.setChannelId(cmsMtPlatformMappingModel.getChannelId());
                                cmsMtPlatformMappingForInsertModel.setMainCategoryId(cmsMtPlatformMappingModel.getMainCategoryId());
                                cmsMtPlatformMappingForInsertModel.setPlatformCartId(cmsMtPlatformMappingModel.getPlatformCartId());
                                cmsMtPlatformMappingForInsertModel.setPlatformCategoryId(cmsMtPlatformMappingModel.getPlatformCategoryId());
                                cmsMtPlatformMappingForInsertModel.setMatchOver(cmsMtPlatformMappingModel.getMatchOver());
                                cmsMtPlatformMappingForInsertModel.setProps(testList);
                                cmsMtPlatformMappingDao.insert(cmsMtPlatformMappingForInsertModel);
                            }
                        }
                    }
                }
            }
        }
        logger.info("platformMappingTask finish");
    }

    /**
     * 获取该channel下所有的叶子类目
     */
    private List<CmsMtPlatformCategoryTreeModel> getFinallyCategories(String channelId, int cartId, String categoryId) {

        CmsMtPlatformCategoryTreeModel cmsMtFeedCategoryTreeModel = cmsMtPlatformCategoryDao.selectByChannel_CartId_CatId(channelId, cartId, categoryId);
//        Object jsonObj = JsonPath.parse(JsonUtil.bean2Json(cmsMtFeedCategoryTreeModel)).json();
        LinkedList jsonArray = JsonPath.read(JsonUtil.bean2Json(cmsMtFeedCategoryTreeModel), "$..children[?(@.isParent == 0)]");
        List<CmsMtPlatformCategoryTreeModel> child = JsonUtil.jsonToBeanList(JsonUtil.bean2Json(jsonArray), CmsMtPlatformCategoryTreeModel.class);
        return child;
    }

    private CmsMtPlatformMappingModel makePlatformMapping(CmsMtPlatformCategoryTreeModel cmsMtPlatformCategoryTree) throws Exception {
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = new CmsMtPlatformMappingModel();
        // channelid
        cmsMtPlatformMappingModel.setChannelId(cmsMtPlatformCategoryTree.getChannelId());
        // 类目ID
        cmsMtPlatformMappingModel.setMainCategoryId(StringUtils.generCatId(cmsMtPlatformCategoryTree.getCatPath()));
        // 类目ID
        cmsMtPlatformMappingModel.setPlatformCategoryId(cmsMtPlatformCategoryTree.getCatId());
        // 渠道ID
        cmsMtPlatformMappingModel.setPlatformCartId(cmsMtPlatformCategoryTree.getCartId());

        cmsMtPlatformMappingModel.setProps(makeProps(cmsMtPlatformCategoryTree.getCartId(), cmsMtPlatformCategoryTree.getCatId()));
        return cmsMtPlatformMappingModel;
    }

    /**
     * Props生成
     */
    private List<MappingBean> makeProps(int cartId, String categoryId) throws Exception {

        List<MappingBean> props = new ArrayList<>();
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModel(categoryId, cartId);
        if (cmsMtPlatformCategorySchemaModel != null) {
            //PropsItem 生成props
            if (!StringUtils.isEmpty(cmsMtPlatformCategorySchemaModel.getPropsItem())) {
                List<Field> fields = SchemaReader.readXmlForList(cmsMtPlatformCategorySchemaModel.getPropsItem());
                for (Field field : fields) {
                    MappingBean temp = makeMapping(field);
                    if (temp != null) {
                        props.add(temp);
                    }
                }
            }
            //PropsProduct 生成props
            if (!StringUtils.isEmpty(cmsMtPlatformCategorySchemaModel.getPropsProduct())) {
                List<Field> fields = SchemaReader.readXmlForList(cmsMtPlatformCategorySchemaModel.getPropsProduct());
                for (Field field : fields) {
                    MappingBean temp = makeMapping(field);
                    if (temp != null) {
                        props.add(temp);
                    }
                }
            }
        }
        return props;
    }

    /**
     * 每个field生成一个具体的object
     */
    private MappingBean makeMapping(Field field) {

        if ("product_images".equalsIgnoreCase(field.getId()) || "item_images".equalsIgnoreCase(field.getId())) {
            String imgTemplate = "http://s7d5.scene7.com/is/image/sneakerhead/BHFO%%5F20150819%%5Fx1200%%5F1200x?$1200x1200$&$1200x1200$&$proudct=%s";
            return CreateImgMapping(field.getId(),imgTemplate);
        }

        if("description".equalsIgnoreCase(field.getId())){
            // 这段内容有点复杂, 需要做一个判断
            // 一种是简单类型
            // 一种是复杂类型

            // -------------------------------------------------------------------------------- START
            // 详情页描述这个字段的mapping有时候要改成这样的
//         {
//             "mappingType" : "0",
//             "platformPropId" : "description",
//             "mappingType" : "0",
//             "expression" : {
//                 "ruleWordList" : [
//                     {
//                         "type" : "DICT",
//                         "value" : "详情页描述"
//                     }
//                 ]
//             }
//         },
//
//
//         {
//             "mappingType" : "1",
//             "platformPropId" : "description",
//             "subMappings" : [
//                 {
//                     "mappingType" : "1",
//                     "platformPropId" : "desc_module_25_cat_mod",
//                     "subMappings" : [
//                         {
//                             "mappingType" : "0",
//                             "platformPropId" : "desc_module_25_cat_mod_content",
//                             "expression" : {
//                                 "ruleWordList" : [
//                                     {
//                                         "type" : "DICT",
//                                         "value" : "详情页描述"
//                                     }
//                                 ]
//                             }
//                         }
//                     ]
//                 }
//             ]
//         },
            // -------------------------------------------------------------------------------- END

            if (FieldTypeEnum.INPUT.equals(field.getType())) {
                System.out.println("商品描述之简单类型");
                RuleExpression ruleExpression = new RuleExpression();
                ruleExpression.addRuleWord(new DictWord("详情页描述"));
                SimpleMappingBean simpleMappingBean = new SimpleMappingBean(field.getId(),ruleExpression);
                return simpleMappingBean;
            } else if (FieldTypeEnum.COMPLEX.equals(field.getType())) {
                System.out.println("商品描述之复杂类型");
                ComplexField complexField = (ComplexField)field;
                boolean blnFound = false;
                for (Field f : complexField.getFields()) {
                    if ("desc_module_user_mods".equals(f.getId())) {
                        if (!f.getType().equals(FieldTypeEnum.MULTICOMPLEX)) {
                            System.out.println("出错啦, 好惨...虽然找到了,但是类型不一样");
                        }
                        // 找到了
                        blnFound = true;
                    }
                }
                if (blnFound) {

//                    RuleExpression ruleExpression1 = new RuleExpression();
//                    ruleExpression1.addRuleWord(new DictWord("详情页描述"));
//                    SimpleMappingBean 自定义模块内容 = new SimpleMappingBean("desc_module_user_mod_content",ruleExpression1);
//
//                    RuleExpression ruleExpression2 = new RuleExpression();
//                    ruleExpression2.addRuleWord(new TextWord("详情页描述"));
//                    SimpleMappingBean 自定义模块名称 = new SimpleMappingBean("desc_module_user_mod_name",ruleExpression2);
//
//                    ComplexMappingBean subComplexMappingBean = new ComplexMappingBean();
//                    subComplexMappingBean.setMasterPropId("desc_module_user_mods");
//                    subComplexMappingBean.setPlatformPropId("desc_module_user_mods");
//                    subComplexMappingBean.addSubMapping(自定义模块内容);
//                    subComplexMappingBean.addSubMapping(自定义模块名称);
//
//                    MultiComplexCustomMappingValue value = new MultiComplexCustomMappingValue();
//                    value.addSubMapping(subComplexMappingBean);
//
//                    MultiComplexCustomMappingBean multiComplexCustomMappingBean = new MultiComplexCustomMappingBean();
//                    multiComplexCustomMappingBean.addValue(value);
//                    multiComplexCustomMappingBean.setPlatformPropId("desc_module_user_mods");
//
//                    List<MappingBean> mappingBeanList = new ArrayList<>();
//                    mappingBeanList.add(multiComplexCustomMappingBean);
//
//                    ComplexMappingBean complexMappingBean = new ComplexMappingBean();
//                    complexMappingBean.setMasterPropId(field.getId());
//                    complexMappingBean.setPlatformPropId(field.getId());
//                    complexMappingBean.setSubMappings(mappingBeanList);


                    RuleExpression ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(new DictWord("详情页描述"));
                    SimpleMappingBean 详情页描述 = new SimpleMappingBean("desc_module_25_cat_mod_content",ruleExpression);

                    ComplexMappingBean c1 = new ComplexMappingBean();
                    c1.setPlatformPropId("desc_module_25_cat_mod");
                    c1.addSubMapping(详情页描述);

                    ComplexMappingBean complexMappingBean = new ComplexMappingBean();
                    complexMappingBean.setPlatformPropId(field.getId());
                    complexMappingBean.addSubMapping(c1);

                    return complexMappingBean;

                } else {
                    System.out.println("出错啦, 没找到");
                }

            } else {
                System.out.println("出错啦, 商品描述之未知类型");

            }

        }

        if("wap_desc".equalsIgnoreCase(field.getId())){
            return null;
//            ComplexMappingBean complexMappingBean = new ComplexMappingBean();
//            complexMappingBean.setPlatformPropId("wap_desc");
//
//            RuleExpression summaryExpression = new RuleExpression();
//            summaryExpression.addRuleWord(new TextWord("summary desc"));
//            SimpleMappingBean simpleMappingBean = new SimpleMappingBean("wap_desc_summary", summaryExpression);
//            complexMappingBean.addSubMapping(simpleMappingBean);
//
//            MultiComplexCustomMappingBean mappingBean = new MultiComplexCustomMappingBean();
//            mappingBean.setPlatformPropId("wap_desc_content");
//            MultiComplexCustomMappingValue value1 = new MultiComplexCustomMappingValue();
//            RuleExpression ruleExpression11 = new RuleExpression();
//            RuleExpression ruleExpression12 = new RuleExpression();
//            ruleExpression11.addRuleWord(new TextWord("image"));
//            TextWord textWord = new TextWord("http://img4.imgtn.bdimg.com/it/u=783206025,938869075&fm=21&gp=0.jpg");
//            textWord.setUrl(true);
//            ruleExpression12.addRuleWord(textWord);
//            value1.addSubMapping(new SimpleMappingBean("wap_desc_content_type", ruleExpression11));
//            value1.addSubMapping(new SimpleMappingBean("wap_desc_content_content", ruleExpression12));
//
//            MultiComplexCustomMappingValue value2 = new MultiComplexCustomMappingValue();
//            RuleExpression ruleExpression21 = new RuleExpression();
//            RuleExpression ruleExpression22 = new RuleExpression();
//            ruleExpression21.addRuleWord(new TextWord("text"));
//            ruleExpression22.addRuleWord(new MasterWord("longTitle"));
//            value2.addSubMapping(new SimpleMappingBean("wap_desc_content_type", ruleExpression21));
//            value2.addSubMapping(new SimpleMappingBean("wap_desc_content_content", ruleExpression22));
//            mappingBean.addValue(value1);
//            mappingBean.addValue(value2);
//
//            complexMappingBean.addSubMapping(mappingBean);
//            return complexMappingBean;
        }
        // 把类目ID中的【.】替换成【->】
//        field.setId(StringUtils.replaceDot(field.getId()));

        SimpleMappingBean simpleMappingBean;
        MasterWord masterWord;
        RuleExpression ruleExpression;
        MappingBean mapping = null;
        switch (field.getType()) {
            case INPUT:
            case MULTIINPUT:
            case LABEL:

                List<String> a = new ArrayList<>();
                List<String> b = new ArrayList<>();
                a.add("sku_price");
                a.add("sku_quantity");
                a.add("sku_outerId");
                a.add("sku_barcode");
                b.add("priceMsrp"); // 把sku_price替换为: priceSale 或者 priceMsrp
                b.add("qty");
                b.add("skuCode");
                b.add("barcode");
                if (a.contains(field.getId())) {
                    simpleMappingBean = new SimpleMappingBean();
                    // 设置平台的属性ID
                    simpleMappingBean.setPlatformPropId(field.getId());
                    // 设置对应的主数据的属性ID
                    SkuWord skuWord = new SkuWord(b.get(a.indexOf(field.getId())));
                    // 生成表达式
                    ruleExpression = new RuleExpression();
                    ruleExpression.addRuleWord(skuWord);
                    simpleMappingBean.setExpression(ruleExpression);
                    mapping = simpleMappingBean;

                    break;
                }


                simpleMappingBean = new SimpleMappingBean();
                // 设置平台的属性ID
                simpleMappingBean.setPlatformPropId(field.getId());
                // 设置对应的主数据的属性ID
                masterWord = new MasterWord(StringUtils.replaceDot(SearchCommProp(field.getId())));
                // 生成表达式
                ruleExpression = new RuleExpression();
                ruleExpression.addRuleWord(masterWord);
                simpleMappingBean.setExpression(ruleExpression);
                mapping = simpleMappingBean;
                break;
            case SINGLECHECK:
            case MULTICHECK:
                simpleMappingBean = new SimpleMappingBean();
                // 设置平台的属性ID
                simpleMappingBean.setPlatformPropId(field.getId());
                // 设置对应的主数据的属性ID
                masterWord = new MasterWord(StringUtils.replaceDot(SearchCommProp(field.getId())));

                // 生成表达式
                ruleExpression = new RuleExpression();
                ruleExpression.addRuleWord(masterWord);

                // option设置
                Map<String, String> optionMapping = new HashMap<>();
                masterWord.setExtra(optionMapping);
                for (Option option : ((OptionsField) field).getOptions()) {
                    optionMapping.put(option.getValue(), option.getValue());
                }
                simpleMappingBean.setExpression(ruleExpression);
                mapping = simpleMappingBean;
                break;
            case COMPLEX:
            case MULTICOMPLEX:
                ComplexMappingBean complexMappingBean = new ComplexMappingBean();
                complexMappingBean.setMasterPropId(StringUtils.replaceDot(SearchCommProp(field.getId())));
                complexMappingBean.setPlatformPropId(field.getId());
                List<MappingBean> subMappings = new ArrayList<>();
                complexMappingBean.setSubMappings(subMappings);

                List<Field> fields = new ArrayList<>();
                if (field instanceof ComplexField) {
                    fields = ((ComplexField) field).getFields();
                } else {
                    fields = ((MultiComplexField) field).getFields();
                }
                for (Field fd : fields) {
                    MappingBean temp = makeMapping(fd);
                    if (temp != null) {
                        subMappings.add(temp);
                    }
                }
                mapping = complexMappingBean;
                break;
        }

        // 普通的特殊处理
        if ("prop_extend_1627207".equals(field.getId())) {
            // 颜色分类扩展(prop_extend_1627207)
            System.out.println("颜色分类扩展");

            // 修改别名
            ComplexMappingBean complexMappingBean = (ComplexMappingBean)mapping;
            for (MappingBean mappingBean : complexMappingBean.getSubMappings()) {
                if ("alias_name".equals(mappingBean.getPlatformPropId())) {

                    MasterWord master = new MasterWord("code");
                    RuleExpression rule = new RuleExpression();
                    rule.addRuleWord(master);

                    SimpleMappingBean simple = (SimpleMappingBean) mappingBean;
                    simple.setExpression(rule);

                    break;
                }

            }

        } else if ("prop_extend_122276380".equals(field.getId())) {
            // 颜色扩展(prop_extend_122276380)
            // TODO: 这种情况在珠宝店里没有, 所以没找到, 如果以后有的话, 再单独做处理
            System.out.println("颜色分类扩展");

        } else if ("prop_extend_1627975".equals(field.getId())) {
            // 颜色扩展(prop_extend_1627975)
            // TODO: 这种情况在珠宝店里没有, 所以没找到, 如果以后有的话, 再单独做处理
            System.out.println("颜色分类扩展");
        }



        return mapping;
    }

    private String SearchCommProp(String fieldId) {
        for (CmsMtCommonPropModel cmsMtCommonProp : commonProp) {
            if (cmsMtCommonProp.getPlatformPropRefId().equalsIgnoreCase(fieldId)) {
                return cmsMtCommonProp.getPropId();
            }
        }
        return fieldId;
    }

    private MappingBean CreateImgMapping(String platformPropId, String imgTemplate) {
        RuleExpression imageTemplateExpression = new RuleExpression();
        imageTemplateExpression.addRuleWord(new TextWord(imgTemplate));
        ComplexMappingBean complexMappingBean = new ComplexMappingBean();
        complexMappingBean.setPlatformPropId(platformPropId);
        complexMappingBean.setMasterPropId(null);
        List<MappingBean> subMappings = new ArrayList<>();
        complexMappingBean.setSubMappings(subMappings);
        for (int i = 0; i < 5; i++) {
            RuleExpression imageIndexExpression = new RuleExpression();
            imageIndexExpression.addRuleWord(new TextWord(i + ""));

            RuleExpression imageTypeExpression = new RuleExpression();
            imageTypeExpression.addRuleWord(new TextWord(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE.toString()));

            CustomWord productImageWord = new CustomWord(new CustomWordValueGetMainProductImages(null, imageTemplateExpression, imageIndexExpression, imageTypeExpression, null));
            RuleExpression productImageExpression = new RuleExpression();
            productImageExpression.addRuleWord(productImageWord);

            subMappings.add(new SimpleMappingBean(platformPropId.substring(0,platformPropId.length()-1)/* 去掉最后一个[s]*/ + "_" + i, productImageExpression));
        }
        return complexMappingBean;
    }
}
