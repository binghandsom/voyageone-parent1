package com.voyageone.web2.cms.views.product_edit;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedInfoDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.cms.service.dao.mongodb.CmsMtCommonSchemaDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.web2.cms.bean.CustomAttributesBean;
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

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    public ProductInfoBean getProductInfo(String channelId, int prodId) throws BusinessException{

        ProductInfoBean productInfo = new ProductInfoBean();

        //自定义属性.
        CustomAttributesBean customAttributes = new CustomAttributesBean();

        // 获取product data.
        CmsBtProductModel productValueModel = getProductModel(channelId, prodId);

        //商品各种状态.
        ProductInfoBean.ProductStatus productStatus = productInfo.getProductStautsInstance();
        productStatus.setStatus(productValueModel.getFields().getStatus());
        productStatus.setEditStatus(productValueModel.getFields().getEditStatus());
        productStatus.setTranslateStatus(productValueModel.getFields().getTranslateStatus());

        //获取商品图片信息.
        List<CmsBtProductModel_Field_Image> productImages = productValueModel.getFields().getImages(CmsBtProductConstants.FieldImageType.PRODUCT_IMAGE);

        // 获取feed方数据.
        CmsBtFeedInfoModel feedInfoModel = getCmsBtFeedInfoModel(channelId, prodId, productValueModel);

        // 获取product 对应的 schema
        CmsMtCategorySchemaModel categorySchemaModel = getCmsMtCategorySchemaModel(productValueModel);

        // 获取共通schema.
        CmsMtComSchemaModel comSchemaModel = getComSchemaModel();

        // 获取master schema.
        List<Field> masterSchemaFields = categorySchemaModel.getFields();

        // 向主数据schema 添加共通schema.
        masterSchemaFields.addAll(comSchemaModel.getFields());

        //获取主数据的值.
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
        customAttributes.setOrgAtts(productValueModel.getFeed().getOrgAtts());
        customAttributes.setCnAtts(productValueModel.getFeed().getCnAtts());
        customAttributes.setCustomIds(productValueModel.getFeed().getCustomIds());

        productInfo.setMasterFields(masterSchemaFields);
        productInfo.setChannelId(channelId);
        productInfo.setProductId(prodId);
        productInfo.setCategoryId(categorySchemaModel.getCatId());
        productInfo.setCategoryFullPath(categorySchemaModel.getCatFullPath());
        productInfo.setSkuFields(skuSchemaFields.get(0));
        productInfo.setCustomAttributes(customAttributes);
        productInfo.setFeedInfoModel(feedInfoModel);
        productInfo.setProductImages(productImages);
        productInfo.setProductStatus(productStatus);

        return productInfo;
    }

    /**
     * 
     * @param channelId
     * @param prodId
     * @param productValueModel
     * @return
     */
    private CmsBtFeedInfoModel getCmsBtFeedInfoModel(String channelId, int prodId, CmsBtProductModel productValueModel) {

        CmsBtFeedInfoModel feedInfoModel = cmsBtFeedInfoDao.selectProductByCode(channelId,productValueModel.getFields().getCode());

        if (feedInfoModel == null){
            //feed 信息不存在时异常处理.
            String errMsg = "channel id: " + channelId +" product id: " +prodId+" 对应的品牌方信息不存在！";

            logger.error(errMsg);

            throw new BusinessException(errMsg);
        }
        return feedInfoModel;
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

}
