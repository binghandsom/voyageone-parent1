package com.voyageone.cms.service;

import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import com.voyageone.cms.service.model.CmsMtCategorySchemaWithValueModel;
import com.voyageone.common.masterdate.schema.enums.FieldTypeEnum;
import com.voyageone.common.masterdate.schema.field.*;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.value.ComplexValue;
import com.voyageone.common.masterdate.schema.value.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-15.
 */
@Service
public class CmsMasterBeanConvertService {

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    @Autowired
    private CmsProductService cmsProductService;

    public CmsMtCategorySchemaWithValueModel getViewModels(String channelId,int prodId){

        CmsMtCategorySchemaWithValueModel schemaWithValueModel = new CmsMtCategorySchemaWithValueModel();

        CmsBtProductModel valueModel = cmsProductService.getProductById(channelId,prodId);

        List<Field> skuFields = new ArrayList<>();


        if (valueModel != null){

            CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(valueModel.getCatId());

            Field skuField = null;

            List<Field> schemaFields = null;

            if (schemaModel != null){

                schemaFields = schemaModel.getFields();
                skuField = schemaModel.getSku();
                skuFields.add(skuField);

                Map valueFields =  valueModel.getFields();

                List<Map<String,Object>> skuMaps = new ArrayList<>();

                List<CmsBtProductModel_Sku> valueSkus = valueModel.getSkus();

                for (CmsBtProductModel_Sku model_sku:valueSkus){
                    skuMaps.add(model_sku);
                }

                Map<String,Object> skuMap = new HashMap<>();

                skuMap.put(skuField.getId(),skuMaps);

                //设置一般属性的值
                FieldUtil.setFieldsValueFromMap(schemaFields,valueFields);

                //设置sku field的值
                FieldUtil.setFieldsValueFromMap(skuFields,skuMap);

                schemaWithValueModel.setFields(schemaFields);
                schemaWithValueModel.setChannelId(channelId);
                schemaWithValueModel.setProductId(prodId);
                schemaWithValueModel.setCatId(schemaModel.getCatId());
                schemaWithValueModel.setCatFullPath(schemaModel.getCatFullPath());
            }
        }

        if (schemaWithValueModel.getFields()==null || schemaWithValueModel.getFields().isEmpty()){
            CmsMtCategorySchemaModel testSchemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId("5omL6KGoPueRnuWjq+iFleihqA==");
            schemaWithValueModel.setFields(testSchemaModel.getFields());
            schemaWithValueModel.setChannelId(channelId);
            schemaWithValueModel.setProductId(prodId);
            schemaWithValueModel.setCatId(testSchemaModel.getCatId());
            schemaWithValueModel.setCatFullPath(testSchemaModel.getCatFullPath());
            schemaWithValueModel.setSku(testSchemaModel.getSku());
        }
        return schemaWithValueModel;
    }

}
