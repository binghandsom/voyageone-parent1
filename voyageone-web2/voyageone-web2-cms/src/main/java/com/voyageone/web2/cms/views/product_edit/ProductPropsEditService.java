package com.voyageone.web2.cms.views.product_edit;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.web2.cms.bean.FeedAttributesBean;
import com.voyageone.web2.cms.bean.ProductInfoBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-12-16.
 */
@Service
public class ProductPropsEditService {

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private CmsProductService cmsProductService;


    public ProductInfoBean getProductInfo(String channelId, int prodId) throws BusinessException{

        ProductInfoBean productInfo = new ProductInfoBean();

        FeedAttributesBean feedAttributes = new FeedAttributesBean();

        List<Field> skuFields = new ArrayList<>();

        CmsBtProductModel productValueModel = cmsProductService.getProductById(channelId,prodId);

        if (productValueModel == null){

            //product 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId +" product id: " +prodId+" 对应的产品信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }

        // 获取product 对应的 schema
        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(productValueModel.getCatId());

        if (schemaModel ==null){
            // product 对应的schema信息不存在时的异常处理.
            String errMsg = "category id: " + productValueModel.getCatId() +"对应的类目信息不存在！";
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }

        List<Field> schemaFields = schemaModel.getFields();

        Field skuField = schemaModel.getSku();

        skuFields.add(skuField);

        Map valueFields =  productValueModel.getFields();

        List<Map<String,Object>> skuMaps = new ArrayList<>();

        List<CmsBtProductModel_Sku> valueSkus = productValueModel.getSkus();

        for (CmsBtProductModel_Sku model_sku:valueSkus){
            skuMaps.add(model_sku);
        }

        Map<String,Object> skuMap = new HashMap<>();

        skuMap.put(skuField.getId(),skuMaps);

        //设置一般属性的值
        FieldUtil.setFieldsValueFromMap(schemaFields,valueFields);

        //设置sku field的值
        FieldUtil.setFieldsValueFromMap(skuFields,skuMap);

        //设置feed属性值
        feedAttributes.setOrgAtts(productValueModel.getFeed().getOrgAtts());
        feedAttributes.setCnAtts(productValueModel.getFeed().getCnAtts());
        feedAttributes.setCustomIds(productValueModel.getFeed().getCustomIds());

        productInfo.setMasterFields(schemaFields);
        productInfo.setChannelId(channelId);
        productInfo.setProductId(prodId);
        productInfo.setCategoryId(schemaModel.getCatId());
        productInfo.setCategoryFullPath(schemaModel.getCatFullPath());
        productInfo.setSkuFields(skuField);
        productInfo.setFeedAttributes(feedAttributes);

        return productInfo;
    }

}
