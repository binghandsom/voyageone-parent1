package com.voyageone.batch.cms.service;

import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.dao.mongo.CmsMtPlatformFieldsRemoveHistoryDao;
import com.voyageone.batch.cms.model.mongo.CmsMtPlatformRemoveFieldsModel;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.cms.service.dao.CmsMtCommonPropDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsMtPlatformCategorySchemaModel;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategorySchemaDao;
import com.voyageone.cms.service.model.MtCommPropActionDefModel;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ActionType;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.util.StringUtils;
import net.minidev.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    Map<String,MtCommPropActionDefModel> allDefModelsMap = new HashMap<>();

//    @Autowired
//    private CmsMtCommonPropDefDao cmsMtCommonPropDefDao;

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

        //是否保存共通属性到
        Boolean isSaveComProps =true;

        //删除原有数据
        cmsMtCategorySchemaDao.deleteAll();

        List<JSONObject> schemaIds = cmsMtPlatformCategorySchemaDao.getAllSchemaKeys();

        List<MtCommPropActionDefModel> allDefModels = cmsMtCommonPropDao.getActionModelList();

        List<MtCommPropActionDefModel> removelist =new ArrayList<>();

        List<MtCommPropActionDefModel> addList =new ArrayList<>();

        List<MtCommPropActionDefModel> updateList =new ArrayList<>();

        //先根据action type 分组
        for (MtCommPropActionDefModel actionDefModel:allDefModels){

            allDefModelsMap.put(actionDefModel.getPropId(),actionDefModel);

            ActionType actionType = ActionType.valueOf(Integer.valueOf(actionDefModel.getActionType()));
            if(actionType != null){
                switch (actionType){
                    case ADD:
                        addList.add(actionDefModel);
                        break;
                    case REMOVE:
                        removelist.add(actionDefModel);
                        break;
                    case UPDATE:
                        updateList.add(actionDefModel);
                        break;
                }
            }
        }

        for (JSONObject schemaId:schemaIds) {
            String id = schemaId.get("_id").toString();
            CmsMtPlatformCategorySchemaModel schemaModel = cmsMtPlatformCategorySchemaDao.getPlatformCatSchemaModelById(id);
            if (CartEnums.Cart.TG == CartEnums.Cart.getValueByID(schemaModel.getCartId().toString())) {

                String itemSchema = schemaModel.getPropsItem();
                String productSchema = schemaModel.getPropsProduct();
                List<Field> masterFields = new ArrayList<>();
                List<Field> removeFields = new ArrayList<>();


                List<Field> itemFields = null;
                List<Field> productFields = null;
                //取得商品fields from item schema
                if (!StringUtils.isEmpty(itemSchema))
                    itemFields = SchemaReader.readXmlForList(itemSchema);

                //取得产品fields from item schema
                if (!StringUtils.isEmpty(productSchema))
                    productFields = SchemaReader.readXmlForList(productSchema);


                //判断产品和商品中是否有相同的属性id，有则修改id名字加以区分
                if (productFields != null) {
                    for (Field proField : productFields) {
                        proField.setInputLevel(1);

                        masterFields.add(proField);
                    }
                }

                if (itemFields != null) {
                    for (Field itemField : itemFields) {
                        itemField.setInputLevel(2);
                        //判断产品和商品中是否有相同的属性id，有则修改id名字加以区分
                        if (masterFields.contains(itemField)) {
                            itemField.setInputOrgId(itemField.getId());

                            Field upField = FieldUtil.getFieldById(masterFields, itemField.getId());

                            if (upField != null) {
                                String newId = itemField.getId() + "_productLevel";
                                upField.setId(newId);
                                FieldUtil.renameDependFieldId(upField, itemField.getId(), newId, masterFields);
                            }

                        }
                        masterFields.add(itemField);
                    }
                }

                //1. 先删除
                for (MtCommPropActionDefModel actionDefModel : removelist) {

                    Field removeByIdField = FieldUtil.getFieldById(masterFields, actionDefModel.getPropId());
                    if (removeByIdField != null) {
                        removeFields.add(removeByIdField);
                        FieldUtil.removeFieldById(masterFields, actionDefModel.getPropId());
                    }

                    List<Field> removeByNameFields = FieldUtil.getFieldByName(masterFields,actionDefModel.getPropName());
                    for (Field byNameField:removeByNameFields){
                        removeFields.add(byNameField);
                        FieldUtil.removeFieldById(masterFields,byNameField.getId());
                    }

                }

                Field skuField = FieldUtil.getFieldById(masterFields,"sku");

                Field darwinSkuField = FieldUtil.getFieldById(masterFields,"darwin_sku");

                //5. 添加sku field
                if (skuField == null && darwinSkuField == null){
                    skuField = FieldTypeEnum.createField(FieldTypeEnum.MULTICOMPLEX);
                    skuField.setId("sku");
                    skuField.setName("SKU");
                    skuField.setFieldRequired();
                    skuField.setInputLevel(0);
                    skuField.setIsDisplay(1);
                    masterFields.add(skuField);
                }

                //6. 更新达尔文sku
                if (darwinSkuField != null){
                    updateField(masterFields, allDefModelsMap.get("sku"), darwinSkuField);
                }


                //4. 更新需要更新的 sub field
                for (MtCommPropActionDefModel actionDefModel : updateList) {

                    Field updField = FieldUtil.getFieldById(masterFields, actionDefModel.getPlatformPropRefId());

                        if (!"sku".equals(actionDefModel.getPropId()) && !"darwin_sku".equals(actionDefModel.getPropId())){

                            if (updField == null){
                                    FieldTypeEnum type = FieldTypeEnum.getEnum(actionDefModel.getPropType());
                                    updField = FieldTypeEnum.createField(type);
                                    updField.setId(actionDefModel.getPropId());
                                    updField.setName(actionDefModel.getPropName());

                                    actionDefModel.getRuleMode().setFieldComProperties(updField);

                                    if (StringUtils.isEmpty(actionDefModel.getParentPropId())){
                                        masterFields.add(updField);
                                    } else {
                                        Field parentField = FieldUtil.getFieldById(masterFields,actionDefModel.getParentPropId());
                                        if (parentField != null){

                                            MultiComplexField complexField = (MultiComplexField)parentField;

                                            List<Field> subSkuFields = complexField.getFields();

                                            subSkuFields.add(updField);
                                        }

                                    }

                            } else {
                                updateField(masterFields, actionDefModel, updField);
                            }

                        }

                }

                //6. 添加sub field
                addField(addList, masterFields, removeFields);

                //构建主数据对象并持久化
                CmsMtCategorySchemaModel masterModel = new CmsMtCategorySchemaModel();
                masterModel.setCatId(StringUtils.getMd5(schemaModel.getCatFullPath()));
                masterModel.setCatFullPath(schemaModel.getCatFullPath());
                FieldUtil.replaceFieldIdDot(masterFields);
                Field sku = FieldUtil.getFieldById(masterFields, "sku");
                FieldUtil.removeFieldById(masterFields, "sku");
                masterModel.setSku(sku);
                masterModel.setFields(masterFields);
                masterModel.setCreater(this.JOB_NAME);
                masterModel.setModifier(this.JOB_NAME);

                index++;

                logger.info("生成第" + index + "/" + schemaIds.size() + "个的主数据Schema，类目id为 " + masterModel.getCatId());
                //保存主数据schema
                cmsMtCategorySchemaDao.insert(masterModel);

                //保存共通属性（TODO 只保存一次，临时性的,将来会有专门的页面生成这部分数据）
                //            if (isSaveComProps)
                //            {
                //                Field itemStatus = FieldUtil.getFieldById(masterFields, "item_status");
                //                Field startTime = FieldUtil.getFieldById(masterFields, "start_time");
                //                Field hsCodeCrop = FieldUtil.getFieldById(masterFields, "hsCodeCrop");
                //                Field hsCodePrivate = FieldUtil.getFieldById(masterFields, "hsCodePrivate");
                //
                //                List<CmsMtCommonPropDefModel> comPropModels = new ArrayList<>();
                //                CmsMtCommonPropDefModel itemStatusModel = new CmsMtCommonPropDefModel();
                //                CmsMtCommonPropDefModel startTimeModel = new CmsMtCommonPropDefModel();
                //                CmsMtCommonPropDefModel hsCodeCropModel = new CmsMtCommonPropDefModel();
                //                CmsMtCommonPropDefModel hsCodePrivateModel = new CmsMtCommonPropDefModel();
                //
                //                itemStatusModel.setField(itemStatus);
                //                startTimeModel.setField(startTime);
                //                hsCodeCropModel.setField(hsCodeCrop);
                //                hsCodePrivateModel.setField(hsCodePrivate);
                //
                //                comPropModels.add(itemStatusModel);
                //                comPropModels.add(startTimeModel);
                //                comPropModels.add(hsCodeCropModel);
                //                comPropModels.add(hsCodePrivateModel);
                //
                //                cmsMtCommonPropDefDao.insertWithList(comPropModels);
                //
                //                isSaveComProps = false;
                //
                //            }

                //save the fields which was deleted
                CmsMtPlatformRemoveFieldsModel removeHistoryModel = new CmsMtPlatformRemoveFieldsModel();
                removeHistoryModel.setCatId(StringUtils.getMd5(schemaModel.getCatFullPath()));
                removeHistoryModel.setCatFullPath(schemaModel.getCatFullPath());
                removeHistoryModel.setFields(removeFields);
                removeHistoryModel.setCreater(this.JOB_NAME);
                removeHistoryModel.setModifier(this.JOB_NAME);

                cmsMtPlatformFieldsRemoveHistoryDao.insert(removeHistoryModel);
            }
        }
    }

    private void addField(List<MtCommPropActionDefModel> addList, List<Field> masterFields, List<Field> removeFields) {
        for (MtCommPropActionDefModel actionDefModel : addList) {

            Field isRmField = FieldUtil.getFieldById(masterFields, actionDefModel.getPropId());

            if (isRmField != null && actionDefModel.getDefModels().size() == 0) {
                removeFields.add(isRmField);
                FieldUtil.removeFieldById(masterFields, actionDefModel.getPropId());
            }

            FieldTypeEnum fieldType = FieldTypeEnum.getEnum(actionDefModel.getPropType());
            Field thisField = FieldTypeEnum.createField(fieldType);

            thisField.setInputLevel(0);
            thisField.setId(actionDefModel.getPropId());
            thisField.setName(actionDefModel.getPropName());
            actionDefModel.getRuleMode().setFieldComProperties(thisField);
            if(StringUtils.isEmpty(actionDefModel.getParentPropId())){
                masterFields.add(thisField);
            }else {
                Field parentField = FieldUtil.getFieldById(masterFields,actionDefModel.getParentPropId());

                List<Field> subFields = null;

                if (parentField == null){

                    MtCommPropActionDefModel pModel = allDefModelsMap.get(actionDefModel.getParentPropId());

                    FieldTypeEnum pType = FieldTypeEnum.getEnum(pModel.getPropType());

                    //添加父结点
                    parentField = FieldTypeEnum.createField(pType);

                    if (parentField instanceof ComplexField){
                        subFields = ((ComplexField) parentField).getFields();

                    }else if (parentField instanceof MultiComplexField){
                        subFields = ((MultiComplexField) parentField).getFields();
                    }

                    subFields.add(thisField);

                    masterFields.add(parentField);

                } else {
                    if (parentField instanceof ComplexField){
                        subFields = ((ComplexField) parentField).getFields();

                    }else if (parentField instanceof MultiComplexField){
                        subFields = ((MultiComplexField) parentField).getFields();
                    }

                    subFields.add(thisField);
                }

            }


        }
    }

    private void updateField(List<Field> masterFields, MtCommPropActionDefModel actionDefModel, Field updField) {


        updField.setId(actionDefModel.getPropId());
        updField.setName(actionDefModel.getPropName());
        updField.setInputOrgId(actionDefModel.getPlatformPropRefId());

        actionDefModel.getRuleMode().setFieldComProperties(updField);

        FieldUtil.renameDependFieldId(updField,actionDefModel.getPropId(),actionDefModel.getPlatformPropRefId(),masterFields);
    }


}
