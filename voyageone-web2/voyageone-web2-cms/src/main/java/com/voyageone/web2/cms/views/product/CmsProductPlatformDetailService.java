package com.voyageone.web2.cms.views.product;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.masterdate.schema.factory.SchemaReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.common.masterdate.schema.utils.FieldUtil;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.dao.cms.CmsMtBrandsMappingDao;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategorySchemaDao;
import com.voyageone.service.impl.cms.product.ProductGroupService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsMtBrandsMappingModel;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategorySchemaModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2016/6/3.
 * @version 2.0.0
 */
@Service
public class CmsProductPlatformDetailService extends BaseAppService {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductGroupService productGroupService;
    @Autowired
    CmsMtPlatformCategorySchemaDao cmsMtPlatformCategorySchemaDao;

    @Autowired
    CmsMtBrandsMappingDao cmsMtBrandsMappingDao;

    /**
     * 获取产品平台信息
     * @param channelId
     * @param prodId
     * @param cartId
     * @return
     */
    public Map<String, Object> getProductPlatform(String channelId, Long prodId, int cartId) {
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);

        if(platformCart != null && !StringUtil.isEmpty(platformCart.getpCatId())){
            CmsMtPlatformCategorySchemaModel platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(platformCart.getpCatId(), cartId);
            List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
            BaseMongoMap<String, Object> fieldsValue = platformCart.getFields();
            if(fieldsValue != null){
                FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
            }
            platformCart.put("schemaFields",fields);
        }

        // platform 品牌名
        if(StringUtil.isEmpty(platformCart.getpBrandId())){
            Map<String,Object> parm = new HashMap<>();
            parm.put("channelId",channelId);
            parm.put("cartId",cartId);
            parm.put("cmsBrand",cmsBtProduct.getFields().getBrand());
            parm.put("active", 1);
            CmsMtBrandsMappingModel cmsMtBrandsMappingModel = cmsMtBrandsMappingDao.selectOne(parm);
            if(cmsMtBrandsMappingModel != null){
                platformCart.setpBrandIds(cmsMtBrandsMappingModel.getBrandId());
                platformCart.setpBrandName(cmsMtBrandsMappingModel.getCmsBrand());
            }
        }

        return platformCart;
    }

    /**
     * 获取产品的基础数据给平台展示用
     * @param channelId
     * @param prodId
     * @param cartId
     * @return
     */
    public Map<String,Object> getProductMastData(String channelId, Long prodId, int cartId){
        Map<String,Object>mastData = new HashMap<>();
        // 取得产品信息
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        // 取得该商品的所在group的其他商品的图片
        CmsBtProductGroupModel cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getFields().getCode(), cartId);
        if(cmsBtProductGroup == null){
            cmsBtProductGroup = productGroupService.selectProductGroupByCode(channelId, cmsBtProduct.getFields().getCode(), 0);
        }
        List<Map<String,Object>> images = new ArrayList<>();
        final CmsBtProductGroupModel finalCmsBtProductGroup = cmsBtProductGroup;
        cmsBtProductGroup.getProductCodes().forEach(s1 -> {
            CmsBtProductModel product = cmsBtProduct.getFields().getCode().equalsIgnoreCase(s1) ? cmsBtProduct : productService.getProductByCode(channelId, s1);
            if (product != null) {
                Map<String, Object> image = new HashMap<String, Object>();
                image.put("productCode", s1);
                image.put("imageName", product.getFields().getImages1().get(0).get("image"));
                image.put("isMain", finalCmsBtProductGroup.getMainProductCode().equalsIgnoreCase(s1));
                images.add(image);
            }
        });

        mastData.put("productCode", cmsBtProduct.getFields().getCode());
        mastData.put("productName", StringUtil.isEmpty(cmsBtProduct.getFields().getProductNameCn()) ? cmsBtProduct.getFields().getProductNameEn() : cmsBtProduct.getFields().getProductNameCn());
        mastData.put("model",cmsBtProduct.getFields().getModel());
        if(cmsBtProduct.getCommon().getFields() != null){
            mastData.put("translateStatus",cmsBtProduct.getCommon().getFields().getTranslateStatus());
            mastData.put("hsCodeStatus",cmsBtProduct.getCommon().getFields().getHsCodeStatus());
        }
        mastData.put("images",images);
        return mastData;
    }

    /**
     * 平台类目变更
     * @param channelId
     * @param prodId
     * @param cartId
     * @param catId
     * @return
     */
    public Map<String,Object> changePlatformCategory(String channelId, Long prodId, int cartId, String catId){
        CmsBtProductModel cmsBtProduct = productService.getProductById(channelId, prodId);
        CmsBtProductModel_Platform_Cart platformCart = cmsBtProduct.getPlatform(cartId);
        if(platformCart != null && !StringUtil.isEmpty(platformCart.getpCatId())){
            CmsMtPlatformCategorySchemaModel platformCategorySchemaModel = cmsMtPlatformCategorySchemaDao.selectPlatformCatSchemaModel(catId, cartId);
            List<Field> fields = SchemaReader.readXmlForList(platformCategorySchemaModel.getPropsItem());
            BaseMongoMap<String, Object> fieldsValue = platformCart.getFields();
            if(fieldsValue != null){
                FieldUtil.setFieldsValueFromMap(fields, fieldsValue);
            }
            platformCart.put("schemaFields",fields);
        }

        // platform 品牌名
        if(StringUtil.isEmpty(platformCart.getpBrandId())){
            Map<String,Object> parm = new HashMap<>();
            parm.put("channelId",channelId);
            parm.put("cartId",cartId);
            parm.put("cmsBrand",cmsBtProduct.getFields().getBrand());
            parm.put("active", 1);
            CmsMtBrandsMappingModel cmsMtBrandsMappingModel = cmsMtBrandsMappingDao.selectOne(parm);
            if(cmsMtBrandsMappingModel != null){
                platformCart.setpBrandIds(cmsMtBrandsMappingModel.getBrandId());
                platformCart.setpBrandName(cmsMtBrandsMappingModel.getCmsBrand());
            }
        }

        return platformCart;
    }
}
