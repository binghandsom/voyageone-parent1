package com.voyageone.batch.cms.service;

import com.voyageone.base.dao.mongodb.model.CmsMtCategorySchemaModel;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.dao.mongo.CmsMtPlatformFieldsRemoveHistoryDao;
import com.voyageone.batch.cms.model.mongo.CmsMtPlatformRemoveFieldsModel;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.cms.service.dao.CmsMtCommonPropDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategorySchemaDao;
import com.voyageone.cms.service.model.MtCommPropActionDefModel;
import com.voyageone.cms.service.model.MtCommPropActionDefRuleModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ActionType;
import com.voyageone.common.masterdate.schema.Util.FieldUtil;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.util.StringUtils;
import net.minidev.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lewis on 15-12-3.
 */
@Service
public class MasterCatSchemaBuildFromTmallService extends BaseTaskService implements MasterCategorySchemaBuildService{

    private final static String JOB_NAME = "buildMasterSchemaFromPlatformTask";

    private static Log logger = LogFactory.getLog(MasterCatSchemaBuildFromTmallService.class);

    @Autowired
    private  CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Autowired
    private  CmsMtCommonPropDao cmsMtCommonPropDao;

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    @Autowired
    private CmsMtPlatformFieldsRemoveHistoryDao cmsMtPlatformFieldsRemoveHistoryDao;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }

    @Override
    public String getTaskName() {
        return this.JOB_NAME;
    }

    @Override
    protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        logger.info(this.JOB_NAME + " start...");

        this.buildMasterCatSchema();

        logger.info(this.JOB_NAME + " finished...");

    }

    public void buildMasterCatSchema() throws TopSchemaException {

        int index = 0;

        //删除原有数据
        cmsMtCategorySchemaDao.deleteAll();

        List<JSONObject> schemaIds = cmsMtPlatformCategorySchemaDao.getAllSchemaKeys();

        List<MtCommPropActionDefModel> allDefModels = cmsMtCommonPropDao.getActionModelList();

        List<MtCommPropActionDefModel> allDefModelsWithHierarchical = MtCommPropActionDefRuleModel.buildComPropHierarchical(allDefModels);

        List<MtCommPropActionDefModel> removeByIdList =new ArrayList<>();

        List<MtCommPropActionDefModel> removeByIdAndNameList =new ArrayList<>();

        List<MtCommPropActionDefModel> addByIdList =new ArrayList<>();

        List<MtCommPropActionDefModel> updateParentByIdList =new ArrayList<>();

        List<MtCommPropActionDefModel> updateSubByIdList =new ArrayList<>();

        //先根据action type 分组
        for (MtCommPropActionDefModel actionDefModel:allDefModelsWithHierarchical){
            ActionType actionType = ActionType.valueOf(Integer.valueOf(actionDefModel.getActionType()));
            switch (actionType){
                case Add:
                    addByIdList.add(actionDefModel);
                    break;
                case RemoveById:
                    removeByIdList.add(actionDefModel);
                    break;
                case RemoveByIdAndName:
                    removeByIdAndNameList.add(actionDefModel);
                    break;
                case Update:
                    if (StringUtils.isEmpty(actionDefModel.getParentPropId())){
                        updateParentByIdList.add(actionDefModel);
                    }else {
                        updateSubByIdList.add(actionDefModel);
                    }

                    break;
            }

        }


        for (JSONObject schemaId:schemaIds){
            String id = schemaId.get("_id").toString();
            CmsMtPlatformCategorySchemaModel schemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModelById(id);
            String itemSchema = schemaModel.getPropsItem();
            String productSchema = schemaModel.getPropsProduct();
            List<Field> masterFields = new ArrayList<>();
            List<Field> removeFields = new ArrayList<>();



            List<Field> itemFields =null;
            List<Field> productFields =null;
            //取得商品fields from item schema
            if (!StringUtils.isEmpty(itemSchema))
                itemFields = SchemaReader.readXmlForList(itemSchema);

            //取得产品fields from item schema
            if (!StringUtils.isEmpty(productSchema))
                productFields = SchemaReader.readXmlForList(productSchema);


            //判断产品和商品中是否有相同的属性id，有则修改id名字加以区分
            if (productFields!=null){
                for (Field proField:productFields){
                    proField.setInputLevel(1);

                    masterFields.add(proField);
                }
            }

            if(itemFields != null){
                for (Field itemField:itemFields){
                    itemField.setInputLevel(2);
                    //判断产品和商品中是否有相同的属性id，有则修改id名字加以区分
                    if (masterFields.contains(itemField)){
                        itemField.setInputOrgId(itemField.getId());

                        Field upField = FieldUtil.getFieldById(masterFields,itemField.getId());

                        if (upField!=null){
                            String newId = itemField.getId()+"_productLevel";
                            upField.setId(newId);
                            FieldUtil.renameDependFieldId(upField,itemField.getId(),newId,masterFields);
                        }

                    }
                    masterFields.add(itemField);
                }
            }

            //1. 先只根据id删除
            for (MtCommPropActionDefModel actionDefModel:removeByIdList){

                Field delField = FieldUtil.getFieldById(masterFields,actionDefModel.getPropId());
                if (delField != null){
                    removeFields.add(delField);
                    FieldUtil.removeFieldById(masterFields,actionDefModel.getPropId());
                }

            }

            //2. 先根据id删除,再根据Name删除Field
            for (MtCommPropActionDefModel actionDefModel:removeByIdAndNameList){

                List<Field> tarFields = FieldUtil.getFieldByName(masterFields,actionDefModel.getPropName());

                for (Iterator<Field> it=tarFields.iterator();it.hasNext();){

                    Field field = it.next();

                    removeFields.add(field);

                    FieldUtil.removeFieldById(masterFields,field.getId());

                }

                Field delField = FieldUtil.getFieldById(masterFields,actionDefModel.getPropId());
                if (delField != null){
                    removeFields.add(delField);
                    FieldUtil.removeFieldById(masterFields,actionDefModel.getPropId());
                }

            }

            //3. 更新需要更新parent field
            for (MtCommPropActionDefModel actionDefModel:updateParentByIdList){
                Field updField = FieldUtil.getFieldById(masterFields,actionDefModel.getPlatformPropRefId());
                updateField(masterFields, actionDefModel, updField);
            }

            //4. 更新需要更新的 sub field
            for (MtCommPropActionDefModel actionDefModel:updateSubByIdList){
                Field updField = FieldUtil.getFieldById(masterFields,actionDefModel.getPlatformPropRefId());
                updateField(masterFields, actionDefModel, updField);
            }

            //5. 添加该添加的field
            for (MtCommPropActionDefModel actionDefModel:addByIdList){

                Field isRmField = FieldUtil.getFieldById(masterFields,actionDefModel.getPropId());

                if (isRmField!=null && actionDefModel.getDefModels().size()==0){
                    removeFields.add(isRmField);
                    FieldUtil.removeFieldById(masterFields,actionDefModel.getPropId());
                }

                FieldTypeEnum fieldType = FieldTypeEnum.getEnum(actionDefModel.getPropType());
                Field newField = FieldTypeEnum.createField(fieldType);

                newField.setInputLevel(0);
                newField.setId(actionDefModel.getPropId());
                newField.setName(actionDefModel.getPropName());
                actionDefModel.getRuleMode().setFieldComProperties(newField);

                if (StringUtils.isEmpty(actionDefModel.getParentPropId())){
                    if (actionDefModel.getDefModels().size()>0){

                        List<MtCommPropActionDefModel> children = actionDefModel.getDefModels();
                        for (MtCommPropActionDefModel model:children){
                            FieldTypeEnum subFieldType = FieldTypeEnum.getEnum(model.getPropType());
                            Field newSubField = FieldTypeEnum.createField(subFieldType);
                            newSubField.setId(model.getPropId());
                            newSubField.setName(model.getPropName());
                            newSubField.setInputLevel(0);
                            model.getRuleMode().setFieldComProperties(newSubField);
                            if (newField instanceof ComplexField){
                                ComplexField complexField = (ComplexField)newField;
                                complexField.getFieldList().add(newSubField);
                            }else if(newField instanceof MultiComplexField){
                                MultiComplexField complexField = (MultiComplexField)newField;
                                complexField.getFieldList().add(newSubField);
                            }
                        }
                        if (FieldUtil.getFieldById(masterFields,actionDefModel.getPropId())==null){
                            masterFields.add(newField);
                        }


                    }else {
                        masterFields.add(newField);
                    }

                }else {
                    Field parentField = FieldUtil.getFieldById(masterFields,actionDefModel.getParentPropId());
                    if (parentField!=null){
                        if (parentField instanceof ComplexField){
                            ComplexField complexField = (ComplexField)parentField;
                            if(complexField.getFieldList()!=null)
                                complexField.getFieldList().add(newField);
                        }else if(parentField instanceof MultiComplexField){
                            MultiComplexField complexField = (MultiComplexField)parentField;
                            if(complexField.getFieldList()!=null)
                                complexField.getFieldList().add(newField);
                        }
                    }

                }

            }

            //构建主数据对象并持久化
            CmsMtCategorySchemaModel masterModel = new CmsMtCategorySchemaModel();
            masterModel.setCatId(StringUtils.encodeBase64(schemaModel.getCatFullPath()));
            masterModel.setCatFullPath(schemaModel.getCatFullPath());
            masterModel.setFields(masterFields);
            masterModel.setCreater(this.JOB_NAME);
            masterModel.setModifier(this.JOB_NAME);

            index++;

            logger.info("生成第"+index+"/"+schemaIds.size()+"个的主数据Schema，类目id为 "+masterModel.getCatId());
            //保存主数据schema
            cmsMtCategorySchemaDao.insert(masterModel);

            //save the fields which was deleted
            CmsMtPlatformRemoveFieldsModel removeHistoryModel = new CmsMtPlatformRemoveFieldsModel();
            removeHistoryModel.setCatId(StringUtils.encodeBase64(schemaModel.getCatFullPath()));
            removeHistoryModel.setCatFullPath(schemaModel.getCatFullPath());
            removeHistoryModel.setFields(removeFields);
            removeHistoryModel.setCreater(this.JOB_NAME);
            removeHistoryModel.setModifier(this.JOB_NAME);

            cmsMtPlatformFieldsRemoveHistoryDao.insert(removeHistoryModel);

        }
    }

    private void updateField(List<Field> masterFields, MtCommPropActionDefModel actionDefModel, Field updField) {

        if (updField != null){

            updField.setId(actionDefModel.getPropId());
            updField.setName(actionDefModel.getPropName());
            updField.setInputOrgId(actionDefModel.getPlatformPropRefId());

            actionDefModel.getRuleMode().setFieldComProperties(updField);

            FieldUtil.renameDependFieldId(updField,actionDefModel.getPropId(),actionDefModel.getPlatformPropRefId(),masterFields);
        }
    }


}
