package com.voyageone.common.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2015-05-28.
 */
@Component
public class TbProductService extends TbBase {

    private static Log logger = LogFactory.getLog(TbProductService.class);

    public String getProductMatchSchema(Long categoryId, ShopBean config) throws ApiException {

        TmallProductMatchSchemaGetRequest request = new TmallProductMatchSchemaGetRequest();
        request.setCategoryId(categoryId);

        TmallProductMatchSchemaGetResponse response = reqTaobaoApi(config, request);
        if (response.getErrorCode() != null)
        {
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
        if (response.getErrorCode() != null)
        {
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

    public TmallItemUpdateSchemaGetResponse doGetWareInfoItem(String itemId, ShopBean config) throws ApiException{

        TmallItemUpdateSchemaGetRequest request = new TmallItemUpdateSchemaGetRequest();
        request.setItemId(Long.parseLong(itemId));
        return reqTaobaoApi(config, request);
    }

    public Boolean isDarwin(Long categoryId, Long brandId, ShopBean config, StringBuffer failCause) throws ApiException
    {
        TmallBrandcatMetadataGetRequest req = new TmallBrandcatMetadataGetRequest();
        req.setCatId(categoryId);
        //req.setBrandId(brandId);
        TmallBrandcatMetadataGetResponse response = reqTaobaoApi(config, req);
        if (response == null)
        {
            failCause.append("访问淘宝超时");
            return null;
        }
        if (response.getErrorCode() != null) {
            failCause.append(response.getMsg());
            return null;
        }
        return response.getBrandCatMetaData().getIsDarwin();
    }
}
