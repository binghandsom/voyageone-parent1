package com.voyageone.batch.cms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.cms.service.bean.ComplexMappingBean;
import com.voyageone.cms.service.bean.MappingBean;
import com.voyageone.cms.service.bean.SingleMappingBean;
import com.voyageone.cms.service.dao.CmsMtCommonPropDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformMappingDao;
import com.voyageone.cms.service.model.CmsMtCommonPropModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import com.voyageone.cms.service.model.CmsMtPlatformMappingModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.masterdate.schema.Util.StringUtil;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.ims.rule_expression.MasterWord;
import com.voyageone.ims.rule_expression.RuleExpression;
import com.voyageone.ims.rule_expression.RuleJsonMapper;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by james.li on 2015/12/7.
 * 主数据->平台的mapping做成
 */
@Service
public class PlatformMappingService extends BaseTaskService {
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
    private List<CmsMtCommonPropModel> commonProp;

    ObjectMapper om = new ObjectMapper();

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

        commonProp = cmsMtCommonPropDao.selectCommonProp().stream().filter(cmsMtCommonPropModel -> !StringUtil.isEmpty(cmsMtCommonPropModel.getMapping())).collect(Collectors.toList());

        String channelId = "001";
        int cartId = 23;
        // 获取该渠道下所有类目树
        List<CmsMtPlatformCategoryTreeModel> platformCategoryTree = cmsMtPlatformCategoryDao.selectByChannel_CartId(channelId, cartId);
        // 每棵数据循环
        for (CmsMtPlatformCategoryTreeModel platformCategory : platformCategoryTree) {
            // 找出这棵树下所有的叶子节点
            List<CmsMtPlatformCategoryTreeModel> finallyCategories = getFinallyCategories(platformCategory.getChannelId(), platformCategory.getCartId(), platformCategory.getCatId());
            // 叶子节点循环
            for (CmsMtPlatformCategoryTreeModel finallyCategory : finallyCategories) {
                // 该叶子节点mapping关系没有生成过的场合
                if (cmsMtPlatformMappingDao.getMapping(channelId, cartId, finallyCategory.getCatId()) == null) {
                    logger.info(finallyCategory.getCatPath());
                    finallyCategory.setCartId(platformCategory.getCartId());
                    // 生成mapping关系数据并插入
                    cmsMtPlatformMappingDao.insert(makePlatformMapping(finallyCategory));
                }
            }
        }
        logger.info("platformMappingTask finish");
    }

    /**
     * 获取该channel下所有的叶子类目
     *
     */
    private List<CmsMtPlatformCategoryTreeModel> getFinallyCategories(String channelId, int cartId, String categoryId) {

        CmsMtPlatformCategoryTreeModel cmsMtFeedCategoryTreeModel = cmsMtPlatformCategoryDao.selectByChannel_CartId_CatId(channelId, cartId, categoryId);
        Object jsonObj = JsonPath.parse(JsonUtil.bean2Json(cmsMtFeedCategoryTreeModel)).json();
        JSONArray jsonArray = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
        List<CmsMtPlatformCategoryTreeModel> child = JsonUtil.jsonToBeanList(JsonUtil.bean2Json(jsonArray), CmsMtPlatformCategoryTreeModel.class);
        return child;
    }

    private CmsMtPlatformMappingModel makePlatformMapping(CmsMtPlatformCategoryTreeModel cmsMtPlatformCategoryTree) {
        CmsMtPlatformMappingModel cmsMtPlatformMappingModel = new CmsMtPlatformMappingModel();
        // channelid
        cmsMtPlatformMappingModel.setChannelId(cmsMtPlatformCategoryTree.getChannelId());
        // 类目ID
        cmsMtPlatformMappingModel.setMainCategoryId(StringUtils.encodeBase64(cmsMtPlatformCategoryTree.getCatPath()));
        // 类目ID
        cmsMtPlatformMappingModel.setPlatformCategoryId(cmsMtPlatformCategoryTree.getCatId());
        // 渠道ID
        cmsMtPlatformMappingModel.setPlatformCartId(cmsMtPlatformCategoryTree.getCartId());

        cmsMtPlatformMappingModel.setProps(makeProps(cmsMtPlatformCategoryTree.getCartId(), cmsMtPlatformCategoryTree.getCatId()));
        return cmsMtPlatformMappingModel;
    }

    /**
     * Props生成
     *
     */
    private List<MappingBean> makeProps(int cartId, String categoryId) {

        List<MappingBean> props = new ArrayList<>();
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModel(categoryId, cartId);
        try {
            if (cmsMtPlatformCategorySchemaModel != null) {
                //PropsItem 生成props
                if (!StringUtil.isEmpty(cmsMtPlatformCategorySchemaModel.getPropsItem())) {
                    List<Field> fields = SchemaReader.readXmlForList(cmsMtPlatformCategorySchemaModel.getPropsItem());
                    for (Field field : fields) {
                        props.add(makeMapping(field));
                    }
                }
                //PropsProduct 生成props
                if (!StringUtil.isEmpty(cmsMtPlatformCategorySchemaModel.getPropsProduct())) {
                    List<Field> fields = SchemaReader.readXmlForList(cmsMtPlatformCategorySchemaModel.getPropsItem());
                    for (Field field : fields) {
                        props.add(makeMapping(field));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return props;
    }

    /**
     * 每个field生成一个具体的object
     *
     */
    private MappingBean makeMapping(Field field) {

        field.setId(StringUtils.replaceDot(field.getId()));
        Map<String,Object> mapping = new HashMap<>();
        Map<String,Object> value = new HashMap<>();
        Map<String,Object> extra = new HashMap<>();
        Map<String,Object> subProps = new HashMap<>();
        value = new HashMap<>();
        SingleMappingBean singleMappingBean;
        MasterWord masterWord;
        RuleJsonMapper ruleJsonMapper = new RuleJsonMapper();
        RuleExpression ruleExpression;
        switch (field.getType()) {
            case INPUT:
            case MULTIINPUT:
            case LABEL:
                singleMappingBean = new SingleMappingBean();
                singleMappingBean.setPlatformPropId(field.getId());
                masterWord = new MasterWord( SearchCommProp(field.getId()));
                ruleExpression = new RuleExpression();
                ruleExpression.addRuleWord(masterWord);
                singleMappingBean.setExpression(ruleJsonMapper.serializeRuleExpression(ruleExpression));
                return singleMappingBean;
            case SINGLECHECK:
            case MULTICHECK:
//                extra = new HashMap<>();
//                for (Option option : ((OptionsField) field).getOptions()) {
//                    extra.put(option.getValue(), option.getValue());
//                }
//                value.put("type", "MASTER");
//                value.put("value", SearchCommProp(field.getId()));
//                value.put("extra", extra);
//                mapping.put(field.getId(), value);

                singleMappingBean = new SingleMappingBean();
                singleMappingBean.setPlatformPropId(field.getId());
                masterWord = new MasterWord( SearchCommProp(field.getId()));
                ruleExpression = new RuleExpression();
                ruleExpression.addRuleWord(masterWord);

                Map<String, String> optionMapping = new HashMap<>();
                masterWord.setExtra(optionMapping);
                for (Option option : ((OptionsField) field).getOptions()) {
                    optionMapping.put(option.getValue(), option.getValue());
                }
                ruleExpression.addRuleWord(masterWord);

                singleMappingBean.setExpression(ruleJsonMapper.serializeRuleExpression(ruleExpression));
                return singleMappingBean;
            case COMPLEX:
            case MULTICOMPLEX:
                ComplexMappingBean complexMappingBean = new ComplexMappingBean();
                complexMappingBean.setMasterPropId(SearchCommProp(field.getId()));
                complexMappingBean.setPlatformPropId(field.getId());
                List<MappingBean> subMappings = new ArrayList<>();
                complexMappingBean.setSubMappings(subMappings);

                List<Field> fields = new ArrayList<>();
                if (field instanceof ComplexField) {
                    fields =  ((ComplexField) field).getFieldList();
                }else{
                    fields =  ((MultiComplexField) field).getFieldList();
                }
                for (Field fd : fields) {
                    MappingBean temp = makeMapping(fd);
                    subMappings.add(temp);
                }
                return complexMappingBean;
        }
        return null;
    }

    private String SearchCommProp(String fieldId) {
        for (CmsMtCommonPropModel cmsMtCommonProp : commonProp) {
            if (cmsMtCommonProp.getMapping().equalsIgnoreCase(fieldId)) {
                return cmsMtCommonProp.getPropId();
            }
        }
        return fieldId;
    }
}
