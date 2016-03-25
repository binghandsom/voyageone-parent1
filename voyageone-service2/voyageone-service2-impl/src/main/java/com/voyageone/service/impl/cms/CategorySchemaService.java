package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.ComplexField;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.field.MultiComplexField;
import com.voyageone.common.masterdate.schema.field.OptionsField;
import com.voyageone.common.masterdate.schema.option.Option;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.service.bean.cms.CmsCategoryInfoBean;
import com.voyageone.service.dao.cms.mongo.CmsMtCategorySchemaDao;
import com.voyageone.service.dao.cms.mongo.CmsMtCommonSchemaDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsMtCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.CmsMtCommonSchemaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * categorySchema Service
 *
 * @author lewis 2016/01/28
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class CategorySchemaService extends BaseService {

    private static final String optionDataSource = "optConfig";

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;

    /**
     * 通过
     * @param categoryId String
     * @return CategorySchemaGetResponse
     */
    public CmsCategoryInfoBean getCategorySchemaByCatId(String categoryId){

        CmsCategoryInfoBean categorySchema = new CmsCategoryInfoBean();
        // 获取product 对应的 schema
        CmsMtCategorySchemaModel categorySchemaModel = getCmsMtCategorySchemaModel(categoryId);

        // 获取共通schema.
        CmsMtCommonSchemaModel comSchemaModel = getComSchemaModel();

        List<Field> comSchemaFields = comSchemaModel.getFields();

        this.fillFieldOptions(comSchemaFields,categoryId);

        // 获取master schema.
        List<Field> masterSchemaFields = categorySchemaModel.getFields();

        // 向主数据schema 添加共通schema.
        masterSchemaFields.addAll(comSchemaFields);

        //没有值的情况下设定complexField、MultiComplexField的默认值.
        setDefaultComplexValues(masterSchemaFields);

        //获取sku schema.
        List<Field> skuSchemaFields = this.buildSkuSchema(categorySchemaModel);

        MultiComplexField skuField = (MultiComplexField)skuSchemaFields.get(0);

        List<Field> subSkuFields = skuField.getFields();

        this.fillFieldOptions(subSkuFields,categoryId);

        //没有值的情况下设定complexField、MultiComplexField的默认值.
        setDefaultComplexValues(skuSchemaFields);

        categorySchema.setMasterFields(masterSchemaFields);
        categorySchema.setCategoryId(categorySchemaModel.getCatId());
        categorySchema.setCategoryFullPath(categorySchemaModel.getCatFullPath());
        categorySchema.setSkuFields(skuField);

        return categorySchema;
    }

    /**
     * 获取 master schema.
     * @param categoryId String
     * @return CmsMtCategorySchemaModel
     */
    private CmsMtCategorySchemaModel getCmsMtCategorySchemaModel(String categoryId) {

        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(categoryId);

        if (schemaModel == null){
            // product 对应的schema信息不存在时的异常处理.
            String errMsg = "category id: " + categoryId +"对应的类目信息不存在！";
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }

        return schemaModel;
    }

    /**
     * 获取common schema.
     * @return CmsMtCommonSchemaModel
     */
    private CmsMtCommonSchemaModel getComSchemaModel() {
        CmsMtCommonSchemaModel comSchemaModel = cmsMtCommonSchemaDao.getComSchema();

        if (comSchemaModel == null){

            //common schema 不存在时异常处理.
            String errMsg = "共通schema（cms_mt_common_schema）的信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }

        return comSchemaModel;
    }

    /**
     * 填充field选项值.
     * @param fields List<Field>
     * @param channelId String
     */
    private void fillFieldOptions(List<Field> fields,String channelId){

        for (Field field : fields) {

            if (optionDataSource.equals(field.getDataSource())) {

                FieldTypeEnum type = field.getType();

                switch (type){
                    case LABEL:
                        break;
                    case INPUT:
                        break;
                    case SINGLECHECK:
                    case MULTICHECK:
                        List<Option> options = TypeChannels.getOptions(field.getId(), channelId);
                        OptionsField optionsField = (OptionsField) field;
                        optionsField.setOptions(options);
                        break;
                    default:
                        break;

                }

            }
        }

    }

    /**
     * complex field值为空时设定默认值.
     * @param fields List<Field>
     */
    private void setDefaultComplexValues(List<Field> fields){

        for (Field fieldItem:fields){

            FieldTypeEnum fieldType = fieldItem.getType();

            switch (fieldType){
                case COMPLEX:
                    ComplexField complexField = (ComplexField)fieldItem;
                    if (complexField.getComplexValue().getFieldMap().isEmpty() && complexField.getDefaultComplexValue().getFieldMap().isEmpty()){

                        ComplexValue defComplexValue = new ComplexValue();
                        Map<String,Field> complexValueMap = new HashMap<>();
                        List<Field> complexFields = complexField.getFields();
                        setDefaultValueFieldMap(complexFields,complexValueMap);
                        defComplexValue.setFieldMap(complexValueMap);
                        complexField.setDefaultValue(defComplexValue);

                    }
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField)fieldItem;
                    if (multiComplexField.getComplexValues().isEmpty() && multiComplexField.getDefaultComplexValues().isEmpty()) {
                        List<Field> complexFields = multiComplexField.getFields();
                        ComplexValue defComplexValue = new ComplexValue();
                        Map<String, Field> complexValueMap = new HashMap<>();
                        setDefaultValueFieldMap(complexFields, complexValueMap);
                        defComplexValue.setFieldMap(complexValueMap);
                        multiComplexField.addDefaultComplexValue(defComplexValue);
                    }
                    break;

                default:
                    break;
            }

        }

    }

    /**
     * 设定Field 的valueFieldMap.
     * @param fields List<Field>
     * @param complexValueMap Map<String,Field>
     */
    private void setDefaultValueFieldMap(List<Field> fields,Map<String, Field> complexValueMap){

        for (Field field:fields){
            FieldTypeEnum type = field.getType();
            switch (type){
                case INPUT:
                case SINGLECHECK:
                case MULTICHECK:
                case MULTIINPUT:
                    complexValueMap.put(field.getId(),field);
                    break;
                case COMPLEX:

                    ComplexField complexField = (ComplexField)field;

                    if (complexField.getComplexValue().getFieldMap().isEmpty() && complexField.getDefaultComplexValue().getFieldMap().isEmpty()){

                        ComplexValue complexValue = new ComplexValue();

                        Map<String,Field> subComplexValueMap = new HashMap<>();

                        List<Field> subFields = complexField.getFields();

                        setDefaultValueFieldMap(subFields,subComplexValueMap);

                        complexValue.setFieldMap(subComplexValueMap);

                        complexField.setDefaultValue(complexValue);

                        complexValueMap.put(complexField.getId(),complexField);
                    }
                    break;
                case MULTICOMPLEX:
                    MultiComplexField multiComplexField = (MultiComplexField)field;
                    if (multiComplexField.getComplexValues().isEmpty() && multiComplexField.getDefaultComplexValues().isEmpty()){
                        ComplexValue complexValue = new ComplexValue();
                        Map<String,Field> subComplexValueMap = new HashMap<>();
                        List<Field> subFields = multiComplexField.getFields();

                        setDefaultValueFieldMap(subFields,subComplexValueMap);

                        multiComplexField.addDefaultComplexValue(complexValue);
                        complexValueMap.put(multiComplexField.getId(),multiComplexField);

                    }
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 构建sku schema.
     * @param categorySchemaModel CmsMtCategorySchemaModel
     * @return List<Field>
     */
    private List<Field> buildSkuSchema(CmsMtCategorySchemaModel categorySchemaModel){

        List<Field> skuSchema = new ArrayList<>();
        Field skuField = categorySchemaModel.getSku();
        skuSchema.add(skuField);

        return skuSchema;
    }

}
