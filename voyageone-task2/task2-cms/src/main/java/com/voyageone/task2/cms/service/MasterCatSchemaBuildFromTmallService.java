package com.voyageone.task2.cms.service;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ActionType;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.enums.FieldValueTypeEnum;
import com.voyageone.common.masterdate.schema.exception.TopSchemaException;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.label.Label;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CommonPropActionDefBean;
import com.voyageone.service.dao.cms.mongo.CmsMtCategorySchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCommonSchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformFieldsRemoveHistoryDao;
import com.voyageone.service.daoext.cms.CmsMtCommonPropDaoExt;
import com.voyageone.service.impl.cms.CategoryTreeService;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformRemoveFieldsModel;
import com.voyageone.task2.base.BaseCronTaskService;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lewis on 15-12-3.
 */
@Service
public class MasterCatSchemaBuildFromTmallService extends BaseCronTaskService implements MasterCategorySchemaBuildService{

    private final static String JOB_NAME = "buildMasterSchemaFromPlatformTask";
    @Autowired
    private CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;
    @Autowired
    private CmsMtCommonPropDaoExt cmsMtCommonPropDaoExt;
    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;
    @Autowired
    private CmsMtPlatformFieldsRemoveHistoryDao cmsMtPlatformFieldsRemoveHistoryDao;
    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;
    @Autowired
    private CategoryTreeService categoryTreeService;


    private Map<String, CommonPropActionDefBean> allDefModelsMap = new HashMap<>();

    //Field字段排序方法
    private static void fieldsSort(List<Field> masterFields){
        Collections.sort(masterFields,(a, b) ->{
                //1.b为true, B前置
                if(b.getRuleByName("requiredRule")!=null&&!StringUtils.isNullOrBlank2(b.getRuleByName("requiredRule").getValue())&&b.getRuleByName("requiredRule").getValue().equals("true"))
                    return 1;
                return -1;
        });
    }

    //Sku Field字段排序方法
    private static void skusSort(List<Field> masterFields){
        Map<String,Integer> sortIndex=new HashMap<>();
        //排序
        Arrays.asList(new String[]{"skuCode:1","size:2","qty:3","priceMsrp:4","priceRetail:5","priceSale:6","skuCarts:7","barcode:8"}).forEach(a->{
           sortIndex.put(a.split(":")[0],Integer.parseInt(a.split(":")[1]));
        });
        Collections.sort(masterFields, (a,b) -> {
                //1.a为空b非空 b前置 2.a>b b前置
                Integer aIndex=sortIndex.get(a.getId());
                Integer bIndex=sortIndex.get(b.getId());
                if((aIndex==null&&bIndex!=null)||(aIndex!=null&&bIndex!=null&&aIndex>bIndex))
                    return 1;
                return -1;
        });
    }

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

        $info(JOB_NAME + " start...");

        List<String> cartIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.cart_id);

        // 循环所有店铺
        if (cartIdList != null && cartIdList.size() > 0) {
            cartIdList.forEach(this::buildMasterCatSchema);
        }

        $info(JOB_NAME + " finished...");

    }

    public void buildMasterCatSchema(String cartId) throws TopSchemaException {

        int index = 0;

        //是否保存共通属性到
        Boolean isSaveComProps =true;

        //删除原有数据
//        cmsMtCategorySchemaDao.deleteAll();

        List<JSONObject> schemaIds = cmsMtPlatformCategorySchemaDao.selectAllSchemaKeys(Integer.parseInt(cartId));

        List<CommonPropActionDefBean> allDefModels = cmsMtCommonPropDaoExt.selectActionModelList();

        List<CommonPropActionDefBean> removelist =new ArrayList<>();

        List<CommonPropActionDefBean> addList =new ArrayList<>();

        List<CommonPropActionDefBean> updateList =new ArrayList<>();

        List<CommonPropActionDefBean> comPropList =new ArrayList<>();

        List<Field> comCategorySchema = new ArrayList<>();

        //先根据action type 分组
        for (CommonPropActionDefBean actionDefModel:allDefModels){

            allDefModelsMap.put(actionDefModel.getPropId(),actionDefModel);

            if (actionDefModel.getIsCom() == 1){
                comPropList.add(actionDefModel);
            }

            ActionType actionType = ActionType.valueOf(actionDefModel.getActionType());

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
            CmsMtPlatformCategorySchemaModel schemaModel =
                    cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModelById(
                            id,
                            Integer.parseInt(cartId));
            if (schemaModel != null){
                if(cartId.equalsIgnoreCase(CartEnums.Cart.JGJ.getId()) || cartId.equalsIgnoreCase(CartEnums.Cart.JGY.getId())) schemaModel.setCatFullPath("jd_"+ schemaModel.getCatFullPath());
                if(isExist(schemaModel.getCatFullPath())) continue;
                if (Integer.parseInt(cartId) == schemaModel.getCartId()) {

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
                    for (CommonPropActionDefBean actionDefModel : removelist) {

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
                    for (CommonPropActionDefBean actionDefModel : updateList) {

                        Field updField = FieldUtil.getFieldById(masterFields, actionDefModel.getPlatformPropRefId());

                        if (!"sku".equals(actionDefModel.getPropId()) && !"darwin_sku".equals(actionDefModel.getPropId())){

                            if (updField == null){
                                FieldTypeEnum type = FieldTypeEnum.getEnum(actionDefModel.getPropType());
                                updField = FieldTypeEnum.createField(type);
                                updField.setId(actionDefModel.getPropId());
                                updField.setName(actionDefModel.getPropName());
                                setValueType(actionDefModel, updField);

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
                    masterModel.setCatId(StringUtils.generCatId(schemaModel.getCatFullPath()));
                    masterModel.setCatFullPath(schemaModel.getCatFullPath());
                    FieldUtil.replaceFieldIdDot(masterFields);
                    Field sku = FieldUtil.getFieldById(masterFields, "sku");
                    if(sku instanceof MultiComplexField)
                        skusSort(((MultiComplexField) sku).getFields());
                    FieldUtil.removeFieldById(masterFields, "sku");
                    masterModel.setSku(sku);

                    for (CommonPropActionDefBean comModel:comPropList){
                        Field comField = FieldUtil.getFieldById(masterFields,comModel.getPropId());
                        comCategorySchema.add(comField);
                        FieldUtil.removeFieldById(masterFields,comModel.getPropId());
                    }
                    fieldsSort(masterFields);
                    masterModel.setFields(masterFields);
                    masterModel.setCreater(JOB_NAME);
                    masterModel.setModifier(JOB_NAME);

                    if (isSaveComProps){
//                        cmsMtCommonSchemaDao.deleteAll();
                        if(cmsMtCommonSchemaDao.count() == 0){
                            CmsMtCommonSchemaModel comSchemaModel = new CmsMtCommonSchemaModel();
                            comSchemaModel.setFields(comCategorySchema);
                            cmsMtCommonSchemaDao.insert(comSchemaModel);
                        }
                        isSaveComProps = false;
                    }

                    index++;

                    $info("生成第" + index + "/" + schemaIds.size() + "个的主数据Schema，类目id为 " + masterModel.getCatId()+"   "+masterModel.getCatFullPath());

                    //保存主数据schema
                    cmsMtCategorySchemaDao.insert(masterModel);

                    //生成类目
                    categoryTreeService.addCategory(masterModel.getCatFullPath(),getTaskName());

                    //save the fields which was deleted
                    CmsMtPlatformRemoveFieldsModel removeHistoryModel = new CmsMtPlatformRemoveFieldsModel();
                    removeHistoryModel.setCatId(StringUtils.generCatId(schemaModel.getCatFullPath()));
                    removeHistoryModel.setCatFullPath(schemaModel.getCatFullPath());
                    removeHistoryModel.setFields(removeFields);
                    removeHistoryModel.setCreater(JOB_NAME);
                    removeHistoryModel.setModifier(JOB_NAME);

                    cmsMtPlatformFieldsRemoveHistoryDao.insert(removeHistoryModel);
                }
            }

        }
    }

    private void setValueType(CommonPropActionDefBean actionDefModel, Field updField) {
        if (!StringUtil.isEmpty(actionDefModel.getValueType())){
            FieldValueTypeEnum valueType = FieldValueTypeEnum.getEnum(actionDefModel.getValueType());
            updField.setFieldValueType(valueType);
        }
    }

    private void addField(List<CommonPropActionDefBean> addList, List<Field> masterFields, List<Field> removeFields) {
        for (CommonPropActionDefBean actionDefModel : addList) {

            Field isRmField = FieldUtil.getFieldById(masterFields, actionDefModel.getPropId());

            if (isRmField != null && StringUtil.isEmpty(actionDefModel.getParentPropId())) {
                removeFields.add(isRmField);
                FieldUtil.removeFieldById(masterFields, actionDefModel.getPropId());
            }

            FieldTypeEnum fieldType = FieldTypeEnum.getEnum(actionDefModel.getPropType());
            Field thisField = FieldTypeEnum.createField(fieldType);

            thisField.setInputLevel(0);
            thisField.setId(actionDefModel.getPropId());
            thisField.setName(actionDefModel.getPropName());
            actionDefModel.getRuleMode().setFieldComProperties(thisField);
            //设定field默认值.
            setFieldDefaultValue(actionDefModel,thisField);
            setValueType(actionDefModel, thisField);

            if(StringUtils.isEmpty(actionDefModel.getParentPropId())){

                masterFields.add(thisField);
            }else {
                Field parentField = FieldUtil.getFieldById(masterFields,actionDefModel.getParentPropId());

                List<Field> subFields = new ArrayList<>();

                if (parentField == null){

                    CommonPropActionDefBean pModel = allDefModelsMap.get(actionDefModel.getParentPropId());

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

    private void updateField(List<Field> masterFields, CommonPropActionDefBean actionDefModel, Field updField) {


        updField.setId(actionDefModel.getPropId());
        updField.setName(actionDefModel.getPropName());
        updField.setInputOrgId(actionDefModel.getPlatformPropRefId());
        setValueType(actionDefModel, updField);

        actionDefModel.getRuleMode().setFieldComProperties(updField);

        FieldUtil.renameDependFieldId(updField,actionDefModel.getPropId(),actionDefModel.getPlatformPropRefId(),masterFields);
    }

    private void setFieldDefaultValue(CommonPropActionDefBean defModel, Field field){

        FieldTypeEnum type = FieldTypeEnum.getEnum(defModel.getPropType());

        if (!StringUtil.isEmpty(defModel.getDefaultValue()) && type != null) {
            switch (type) {
                case LABEL:
                    LabelField labelField = (LabelField) field;
                    Label label = new Label();
                    label.setValue(defModel.getDefaultValue());
                    labelField.add(label);
                    break;
                case INPUT:
                    InputField inputField = (InputField) field;
                    inputField.setDefaultValue(defModel.getDefaultValue());
                    break;
                case SINGLECHECK:
                    SingleCheckField singleCheckField = (SingleCheckField) field;
                    singleCheckField.setDefaultValue(defModel.getDefaultValue());
                    break;
                default:
                    break;
            }
        }

    }

    private Boolean isExist(String catFullPath){
        String parmat = String.format("{'catId':'%s'}", StringUtils.generCatId(catFullPath));
        return cmsMtCategorySchemaDao.getCategoryCount(parmat) > 0;
    }

}
