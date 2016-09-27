package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.domain.UpdateSkuPrice;
import com.taobao.api.request.ItemSkusGetRequest;
import com.taobao.api.request.TmallItemPriceUpdateRequest;
import com.taobao.api.request.TmallItemSchemaUpdateRequest;
import com.taobao.api.request.TmallItemUpdateSchemaGetRequest;
import com.taobao.api.response.ItemSkusGetResponse;
import com.taobao.api.response.TmallItemPriceUpdateResponse;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 淘宝平台，商品更新接口调用
 * Created by Jonas on 7/16/15.
 */
@Component
public class TbItemService extends TbBase {
    /**
     * 淘宝接口名：tmall.item.update.schema.get
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=23435
     *
     * @param shopBean 店铺
     * @param num_iid  商品
     * @return 查询到的所有字段
     */
    public TbItemSchema getUpdateSchema(ShopBean shopBean, long num_iid) throws ApiException, TopSchemaException, GetUpdateSchemaFailException {

        TmallItemUpdateSchemaGetRequest request = new TmallItemUpdateSchemaGetRequest();

        request.setItemId(num_iid);

        TmallItemUpdateSchemaGetResponse res = reqTaobaoApi(shopBean, request);

        if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) throw new GetUpdateSchemaFailException(res);

        // 转换 Schema Xml 到 Field bean 集合
        List<Field> fields = SchemaReader.readXmlForList(res.getUpdateItemResult());

        return new TbItemSchema(num_iid, fields, false);
    }

    /**
     * 全量更新淘宝的商品。
     * <p>
     * 淘宝接口名：tmall.item.schema.update
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=23434
     */
    public TmallItemSchemaUpdateResponse updateFields(ShopBean shopBean, TbItemSchema tbItemSchema) throws ApiException, TopSchemaException {

        TmallItemSchemaUpdateRequest req = new TmallItemSchemaUpdateRequest();

        req.setItemId(tbItemSchema.getNum_iid());

        String xmlData = SchemaWriter.writeParamXmlString(tbItemSchema.getFields());

        req.setXmlData(xmlData);

        return reqTaobaoApi(shopBean, req);
    }

    /**
     * 根据商品ID列表获取SKU信息
     */
    public ItemSkusGetResponse getSkuInfo(ShopBean shopBean, String numIid, String fields) throws ApiException {

        logger.info("根据商品ID列表获取SKU信息 " + numIid);
        ItemSkusGetRequest req = new ItemSkusGetRequest();
        req.setFields(fields);
        req.setNumIids(numIid);

        ItemSkusGetResponse response = reqTaobaoApi(shopBean, req);
        if (response.getErrorCode() != null) {
            logger.error(response.getSubMsg());
        }
        return reqTaobaoApi(shopBean, req);
    }

    /**
     * 更新商品SKU的价格（天猫商品/SKU价格更新接口 tmall.item.price.update）
     * numIid参数为必须，
     * prodPrice，skuPriceList为可选，且互斥，即这两个只有一个有值
     */
    public TmallItemPriceUpdateResponse updateSkuPrice(ShopBean shopBean, String numIid, Double prodPrice, List<UpdateSkuPrice> skuPriceList) throws ApiException {
        logger.info("更新商品SKU的价格 " + numIid);
        TmallItemPriceUpdateRequest req = new TmallItemPriceUpdateRequest();
        req.setItemId(NumberUtils.toLong(numIid));
        if (prodPrice != null) {
            req.setItemPrice(prodPrice.toString());
        }
        if (skuPriceList != null && skuPriceList.size() > 0) {
            req.setSkuPrices(skuPriceList);
        }

        TmallItemPriceUpdateResponse response = reqTaobaoApi(shopBean, req);
        if (response == null) {
            return null;
        }
        if (response.getErrorCode() != null) {
            logger.error(response.getSubMsg());
        }
        logger.info("更新商品SKU的价格 结果：" + response.getPriceUpdateResult());
        return response;
    }
}
