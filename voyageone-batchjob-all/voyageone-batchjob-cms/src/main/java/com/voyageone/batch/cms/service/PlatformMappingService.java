package com.voyageone.batch.cms.service;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.modelbean.TaskControlBean;
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
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by james.li on 2015/12/7.
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

    private List<CmsMtCommonPropModel> commonProp;

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
     * @param channelId
     * @return
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
        cmsMtPlatformMappingModel.setMainCategoryId(cmsMtPlatformCategoryTree.getCatId());
        // 类目ID
        cmsMtPlatformMappingModel.setPlatformCategoryId(cmsMtPlatformCategoryTree.getCatId());
        // 渠道ID
        cmsMtPlatformMappingModel.setPlatformCartId(cmsMtPlatformCategoryTree.getCartId());

        cmsMtPlatformMappingModel.setProps(makeProps(cmsMtPlatformCategoryTree.getCartId(), cmsMtPlatformCategoryTree.getCatId()));
        return cmsMtPlatformMappingModel;
    }

    /**
     * Props生成
     * @param cartId
     * @param categoryId
     * @return
     */
    private List<Map> makeProps(int cartId, String categoryId) {

        List<Map> props = new ArrayList<>();
        CmsMtPlatformCategorySchemaModel cmsMtPlatformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModel(categoryId, cartId);
        try {
            if(cmsMtPlatformCategorySchemaModel != null) {
                //PropsItem 生成props
                if (!StringUtil.isEmpty(cmsMtPlatformCategorySchemaModel.getPropsItem())) {
                    List<Field> fields = SchemaReader.readXmlForList(cmsMtPlatformCategorySchemaModel.getPropsItem());
                    for (Field field : fields) {
                        props.add(makeProp(field));
                    }
                }
                //PropsProduct 生成props
                if (!StringUtil.isEmpty(cmsMtPlatformCategorySchemaModel.getPropsProduct())) {
                    List<Field> fields = SchemaReader.readXmlForList(cmsMtPlatformCategorySchemaModel.getPropsItem());
                    for (Field field : fields) {
                        props.add(makeProp(field));
                    }
                }
            }

        } catch (TopSchemaException e) {
            e.printStackTrace();
        }

        return props;
    }

    /**
     * 每个field生成一个具体的object
     * @param field
     * @return
     */
    private Map makeProp(Field field) {

        Map mapping = new HashMap<>();
        Map value = new HashMap<>();
        Map extra = new HashMap<>();
        Map subProps = new HashMap<>();
        value = new HashMap<>();
        value.put("type", "MASTER");
        value.put("value", field.getId());
        switch (field.getType()) {
            case INPUT:
            case MULTIINPUT:
            case LABEL:
                mapping.put(field.getId(), value);
                break;
            case SINGLECHECK:
                extra = new HashMap<>();
                for (Option option : ((SingleCheckField) field).getOptions()) {
                    extra.put(option.getValue(), option.getValue());
                }
                ;
                value.put("extra", extra);
                mapping.put(field.getId(), value);
                break;
            case MULTICHECK:
                extra = new HashMap<>();
                for (Option option : ((MultiCheckField) field).getOptions()) {
                    extra.put(option.getValue(), option.getValue());
                }
                ;
                value.put("extra", extra);
                mapping.put(field.getId(), value);
                break;
            case COMPLEX:
                subProps = new HashMap<>();
                mapping.put("propId", field.getId());
                for (Field fd : ((ComplexField) field).getFieldList()) {
                    Map temp = makeProp(fd);
                    if (fd instanceof ComplexField || fd instanceof MultiComplexField) {
                        subProps.put(temp.get("propId").toString(), temp);
                    } else {
                        for (Object key : temp.keySet()) {
                            subProps.put(key, temp.get(key));
                        }
                    }
                }
                ;
                mapping.put("subProps", subProps);
                break;
            case MULTICOMPLEX:
                subProps = new HashMap<>();
                mapping.put("propId", field.getId());
                for (Field fd : ((MultiComplexField) field).getFieldList()) {
                    Map temp = makeProp(fd);
                    if (fd instanceof ComplexField || fd instanceof MultiComplexField) {
                        subProps.put(temp.get("propId").toString(), temp);
                    } else {
                        for (Object key : temp.keySet()) {
                            subProps.put(key, temp.get(key));
                        }
                    }
                }
                ;
                mapping.put("subProps", subProps);
                break;
        }


        return mapping;
    }
}
