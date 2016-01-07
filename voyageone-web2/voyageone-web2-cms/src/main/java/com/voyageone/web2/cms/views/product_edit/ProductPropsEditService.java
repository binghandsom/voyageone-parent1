package com.voyageone.web2.cms.views.product_edit;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCommonSchemaDao;
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

    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;



    public ProductInfoBean getProductInfo(String channelId, int prodId) throws BusinessException{

        ProductInfoBean productInfo = new ProductInfoBean();

        FeedAttributesBean feedAttributes = new FeedAttributesBean();


        // 获取product data.
        CmsBtProductModel productValueModel = getProductModel(channelId, prodId);

        // 获取product 对应的 schema
        CmsMtCategorySchemaModel categorySchemaModel = getCmsMtCategorySchemaModel(productValueModel);

        // 获取共通schema.
        CmsMtComSchemaModel comSchemaModel = getComSchemaModel();

        // 获取master schema.
        List<Field> masterSchemaFields = categorySchemaModel.getFields();

        //添加共通schema.
        masterSchemaFields.addAll(comSchemaModel.getFields());

        Map masterSchemaValue =  productValueModel.getFields();

        //填充master schema
        FieldUtil.setFieldsValueFromMap(masterSchemaFields,masterSchemaValue);

        //获取sku schema.
        List<Field> skuSchemaFields = this.buildSkuSchema(categorySchemaModel);

        //获取sku schemaValue
        Map<String, Object> skuSchemaValue = buildSkuSchemaValue(productValueModel, categorySchemaModel);

        //填充sku schema.
        FieldUtil.setFieldsValueFromMap(skuSchemaFields,skuSchemaValue);

        //设置feed属性值
        feedAttributes.setOrgAtts(productValueModel.getFeed().getOrgAtts());
        feedAttributes.setCnAtts(productValueModel.getFeed().getCnAtts());
        feedAttributes.setCustomIds(productValueModel.getFeed().getCustomIds());

        productInfo.setMasterFields(masterSchemaFields);
        productInfo.setChannelId(channelId);
        productInfo.setProductId(prodId);
        productInfo.setCategoryId(categorySchemaModel.getCatId());
        productInfo.setCategoryFullPath(categorySchemaModel.getCatFullPath());
        productInfo.setSkuFields(skuSchemaFields.get(0));
        productInfo.setFeedAttributes(feedAttributes);

        return productInfo;
    }

    /**
     * 构建sku schemaValue.
     * @param productValueModel
     * @param categorySchemaModel
     * @return
     */
    private Map<String, Object> buildSkuSchemaValue(CmsBtProductModel productValueModel, CmsMtCategorySchemaModel categorySchemaModel) {
        List<Map<String,Object>> skuValueModel = new ArrayList<>();

        List<CmsBtProductModel_Sku> valueSkus = productValueModel.getSkus();

        for (CmsBtProductModel_Sku model_sku:valueSkus){
            skuValueModel.add(model_sku);
        }

        Map<String,Object> skuSchemaValue = new HashMap<>();

        skuSchemaValue.put(categorySchemaModel.getSku().getId(),skuValueModel);

        return skuSchemaValue;
    }


    /**
     * 构建sku schema.
     * @param categorySchemaModel
     * @return
     */
    private List<Field> buildSkuSchema(CmsMtCategorySchemaModel categorySchemaModel){

        List<Field> skuSchema = new ArrayList<>();
        Field skuField = categorySchemaModel.getSku();
        skuSchema.add(skuField);

        return skuSchema;
    }

    /**
     *
     * @param productValueModel
     * @return
     */
    private CmsMtCategorySchemaModel getCmsMtCategorySchemaModel(CmsBtProductModel productValueModel) {

        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(productValueModel.getCatId());

        if (schemaModel == null){
            // product 对应的schema信息不存在时的异常处理.
            String errMsg = "category id: " + productValueModel.getCatId() +"对应的类目信息不存在！";
            logger.error(errMsg);
            throw new BusinessException(errMsg);
        }

        return schemaModel;
    }

    /**
     *
     * @param channelId
     * @param prodId
     * @return
     */
    private CmsBtProductModel getProductModel(String channelId, int prodId) {

        CmsBtProductModel productValueModel = cmsProductService.getProductById(channelId,prodId);

        if (productValueModel == null){

            //product 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId +" product id: " +prodId+" 对应的产品信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }

        return productValueModel;
    }

    /**
     *
     * @return
     */
    private CmsMtComSchemaModel getComSchemaModel() {
        CmsMtComSchemaModel comSchemaModel = cmsMtCommonSchemaDao.getComSchema();

        if (comSchemaModel == null){

            //common schema 不存在时异常处理.
            String errMsg = "共通schema（cms_mt_common_schema）的信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }

        return comSchemaModel;
    }

    // TODO 设置field的options
    private void fillFieldOptions(){

    }

    /**
     * 获取图片信息. TODO
     */
    private void getPictures(){

    }

    /**
     * 获取feed数据. TODO
     */
    private void getFeedInfo(){

    }
}
