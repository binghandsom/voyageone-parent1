package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.components.tmall.TbBase;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2015-05-28.
 */
@Component
public class TbProductService extends TbBase {

	/**
     * 获取[更新产品]的规则的schema
     * @throws ApiException
     */
    public String getProductUpdateSchema(Long productId, ShopBean config, StringBuffer failCause) throws ApiException {
        TmallProductUpdateSchemaGetRequest request = new TmallProductUpdateSchemaGetRequest();
        request.setProductId(productId);

        TmallProductUpdateSchemaGetResponse response = reqTaobaoApi(config, request);
        if (response.getErrorCode() != null) {
            logger.error(response.getSubMsg());
            failCause.append(response.getSubMsg());
        }

        return response.getUpdateProductSchema();
    }

	/**
     * 更新产品
     * @throws ApiException
     */
    public String updateProduct(Long productId, String xmlData, ShopBean config, StringBuffer failCause) throws ApiException {

        TmallProductSchemaUpdateRequest request = new TmallProductSchemaUpdateRequest();
        request.setProductId(productId);
        request.setXmlData(xmlData);

        TmallProductSchemaUpdateResponse response = reqTaobaoApi(config, request);
        if (response.getErrorCode() != null) {
            logger.error(response.getSubMsg());
            failCause.append(response.getSubMsg());
        }

        return response.getUpdateProductResult();
    }

    public String getProductMatchSchema(Long categoryId, ShopBean config) throws ApiException {

        TmallProductMatchSchemaGetRequest request = new TmallProductMatchSchemaGetRequest();
        request.setCategoryId(categoryId);

        TmallProductMatchSchemaGetResponse response = reqTaobaoApi(config, request);
        if (response.getErrorCode() != null) {
            logger.error(response.getSubMsg());
        }

        return response.getMatchResult();
    }

    public String getProductSchema(Long productId, ShopBean config) throws ApiException
    {
        TmallProductSchemaGetRequest request = new TmallProductSchemaGetRequest();
        request.setProductId(productId);

        TmallProductSchemaGetResponse response = reqTaobaoApi(config, request);

        return response.getGetProductResult();
    }

    public String[] matchProduct(Long categoryId, String propValues, ShopBean config) throws ApiException {
        TmallProductSchemaMatchRequest request = new TmallProductSchemaMatchRequest();
        request.setCategoryId(categoryId);
        request.setPropvalues(propValues);

        TmallProductSchemaMatchResponse response = reqTaobaoApi(config, request);
        if (response.getErrorCode() == null)
        {
            String product_ids = response.getMatchResult();
            if (product_ids == null || "".equals(product_ids))
                return null;
            return product_ids.split(",");
        }
        return null;
    }

    public String getAddProductSchema(Long categoryId, Long brandId, ShopBean config) throws ApiException {
        TmallProductAddSchemaGetRequest request = new TmallProductAddSchemaGetRequest();
        request.setCategoryId(categoryId);
        request.setBrandId(brandId);
        TmallProductAddSchemaGetResponse response = reqTaobaoApi(config, request);
        return response.getAddProductRule();
    }

    public String addProduct(Long categoryId, Long brandId, String xmlData, ShopBean config, StringBuffer failCause) throws ApiException {
        TmallProductSchemaAddRequest request = new TmallProductSchemaAddRequest();
        request.setCategoryId(categoryId);
        request.setBrandId(brandId);
        request.setXmlData(xmlData);

        TmallProductSchemaAddResponse response = reqTaobaoApi(config, request);
        if (response.getErrorCode() != null) {
            failCause.append(response.getSubMsg());
        }
        return response.getAddProductResult();
    }

    //tmall.item.schema.add
    public TmallItemSchemaAddResponse addItem(Long categoryId, String productId, String xmlData, ShopBean config) throws ApiException {
        TmallItemSchemaAddRequest request = new TmallItemSchemaAddRequest();
        request.setCategoryId(categoryId);
        request.setProductId(Long.parseLong(productId));
        request.setXmlData(xmlData);

        return reqTaobaoApi(config, request);
    }

    //tmall.item.schema.update
    public TmallItemSchemaUpdateResponse updateItem(String productId, String numId, Long categoryId, String xmlData, ShopBean config) throws ApiException {
        TmallItemSchemaUpdateRequest request = new TmallItemSchemaUpdateRequest();
        request.setCategoryId(categoryId);
        request.setItemId(Long.parseLong(numId));
        request.setProductId(Long.parseLong(productId));
        request.setXmlData(xmlData);

        return reqTaobaoApi(config, request);
    }

    public TmallItemUpdateSchemaGetResponse doGetWareInfoItem(String itemId, ShopBean config) throws ApiException {
        TmallItemUpdateSchemaGetRequest request = new TmallItemUpdateSchemaGetRequest();
        request.setItemId(Long.parseLong(itemId));
        return reqTaobaoApi(config, request);
    }

    /**
     * taobao.item.seller.get (获取单个商品详细信息)
     *
     * @param numIId numIId
     * @param fields 需要返回的商品字段列表,“,”分隔
     */
    public ItemSellerGetResponse doGetItemInfo(String numIId, String fields, ShopBean config) throws ApiException {
        ItemSellerGetRequest request = new ItemSellerGetRequest();
        request.setNumIid(Long.parseLong(numIId));
        request.setFields(fields);
        return reqTaobaoApi(config, request);
    }

    public Boolean isDarwin(Long categoryId, Long brandId, ShopBean config, StringBuffer failCause) throws ApiException {
        TmallBrandcatMetadataGetRequest req = new TmallBrandcatMetadataGetRequest();
        req.setCatId(categoryId);
        //req.setBrandId(brandId);
        TmallBrandcatMetadataGetResponse response = reqTaobaoApi(config, req);
        if (response == null) {
            failCause.append("访问淘宝超时");
            return null;
        }
        if (response.getErrorCode() != null) {
            failCause.append(response.getMsg());
            return null;
        }
        return response.getBrandCatMetaData().getIsDarwin();
    }

    public Boolean delItem(ShopBean config, String numId) throws ApiException {
        ItemDeleteRequest req = new ItemDeleteRequest();
        req.setNumIid(Long.parseLong(numId));
        ItemDeleteResponse response = reqTaobaoApi(config,req);
        if (response.getErrorCode() != null) {
            if(response.getSubMsg().indexOf("该商品已被删除") > -1) return true;
            throw new BusinessException("天猫删除商品失败:" + response.getSubMsg() + "  错误码：" + response.getErrorCode());
        }
        return true;
    }

    /**
     * 天猫增量更新商品规则获取(tmall.item.increment.update.schema.get)
     * http://open.taobao.com/docs/api.htm?apiId=23781
     *
     * @param numId    商品id(必须)
     * @param xmlData 更新的字段(可选) 如果入参xml_data指定了更新的字段，则只返回指定字段的规则（ISV如果功能性很强，如明确更新Title，请拼装好此字段以提升API整体性能）
     */
    public TmallItemIncrementUpdateSchemaGetResponse getItemIncrementUpdateSchema(String numId, String xmlData, ShopBean config) throws ApiException
    {
        TmallItemIncrementUpdateSchemaGetRequest request = new TmallItemIncrementUpdateSchemaGetRequest();
        request.setItemId(Long.parseLong(numId));
        if (!StringUtils.isEmpty(xmlData)) request.setXmlData(xmlData);

        return reqTaobaoApi(config, request);
    }

    /**
     * 天猫根据规则增量更新商品(tmall.item.schema.increment.update)
     * http://open.taobao.com/docs/api.htm?apiId=23782
     *
     * @param numId    需要编辑的商品ID(必须)
     * @param xmlData  更新的字段(必须) 如果入参xml_data指定了更新的字段，则只返回指定字段的规则（ISV如果功能性很强，如明确更新Title，请拼装好此字段以提升API整体性能）
     */
    public TmallItemSchemaIncrementUpdateResponse updateItemSchemaIncrement(String numId, String xmlData, ShopBean config) throws ApiException
    {
        TmallItemSchemaIncrementUpdateRequest request = new TmallItemSchemaIncrementUpdateRequest();
        request.setItemId(Long.parseLong(numId));
        request.setXmlData(xmlData);

        return reqTaobaoApi(config, request);
    }
}
