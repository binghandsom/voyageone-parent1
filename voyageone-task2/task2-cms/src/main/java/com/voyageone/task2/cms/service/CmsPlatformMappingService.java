package com.voyageone.task2.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.cms.service.bean.*;
import com.voyageone.cms.service.dao.CmsMtCommonPropDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformMappingDao;
import com.voyageone.cms.service.model.*;
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
                            cmsMtPlatformMappingDao.insert(makePlatformMapping(finallyCategory));
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
            RuleExpression ruleExpression = new RuleExpression();
            ruleExpression.addRuleWord(new DictWord("详情页描述"));
            SimpleMappingBean simpleMappingBean = new SimpleMappingBean(field.getId(),ruleExpression);
            return simpleMappingBean;
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
                b.add("priceSale");
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
