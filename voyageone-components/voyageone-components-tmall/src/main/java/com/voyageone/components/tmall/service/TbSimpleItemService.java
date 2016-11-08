package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.request.TmallItemSimpleschemaAddRequest;
import com.taobao.api.request.TmallItemSimpleschemaUpdateRequest;
import com.taobao.api.request.TmallItemUpdateSimpleschemaGetRequest;
import com.taobao.api.response.TmallItemSimpleschemaAddResponse;
import com.taobao.api.response.TmallItemSimpleschemaUpdateResponse;
import com.taobao.api.response.TmallItemUpdateSimpleschemaGetResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import com.voyageone.components.tmall.exceptions.GetUpdateSchemaFailException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 天猫官网同购上新接口调用（简化方式发布商品）
 * Created by desmond on 8/25/2016.
 *
 * @version 2.6.0
 * @since 2.6.0
 */
@Component
public class TbSimpleItemService extends TbBase {
    /**
     * 天猫官网同购新增Item
     * 淘宝接口名：tmall.item.simpleschema.add
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=25455
     *
     * @param shopBean  店铺
     * @param schemaXml xml
     * @return numIId
     */
    public String addSimpleItem(ShopBean shopBean, String schemaXml) throws ApiException {
        logger.info("url:" + shopBean.getApp_url());
        logger.info("appkey:" + shopBean.getAppKey());
        logger.info("secret:" + shopBean.getAppSecret());
        logger.info("session:" + shopBean.getSessionKey());
        logger.info("xml:" + schemaXml);

        TmallItemSimpleschemaAddRequest request = new TmallItemSimpleschemaAddRequest();
        request.setSchemaXmlFields(schemaXml);

        TmallItemSimpleschemaAddResponse response = reqTaobaoApi(shopBean, request);

        // 调用淘宝API未成功或者subCode不为空的时候(errorCode是英文错误，subCode是中文错误)
        if (!response.isSuccess() || !StringUtils.isEmpty(response.getSubCode())) {
            logger.error("addSimpleItem [SubMsg = " + response.getSubMsg() + "]");
            return "ERROR:" + response.getErrorCode() + ":" + response.getSubCode() + ":" + response.getSubMsg();
        }

        return response.getResult();
    }

    /**
     * 天猫官网同购更新Item
     * 淘宝接口名：tmall.item.simpleschema.update
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=26072
     *
     * @param shopBean  店铺
     * @param numIId    numIId
     * @param schemaXml xml
     * @return numIId
     */
    public String updateSimpleItem(ShopBean shopBean, Long numIId, String schemaXml) throws ApiException {
        logger.info("url:" + shopBean.getApp_url());
        logger.info("appkey:" + shopBean.getAppKey());
        logger.info("secret:" + shopBean.getAppSecret());
        logger.info("session:" + shopBean.getSessionKey());
        logger.info("numIId:" + numIId);
        logger.info("xml:" + schemaXml);

        TmallItemSimpleschemaUpdateRequest request = new TmallItemSimpleschemaUpdateRequest();

        request.setItemId(numIId);
        request.setSchemaXmlFields(schemaXml);

        TmallItemSimpleschemaUpdateResponse response = reqTaobaoApi(shopBean, request);

        // 调用淘宝API未成功或者subCode不为空的时候(errorCode是英文错误，subCode是中文错误)
        if (!response.isSuccess() || !StringUtils.isEmpty(response.getErrorCode())) {
            logger.error("updateSimpleItem [SubMsg = " + response.getSubMsg() + "]");
            return "ERROR:" + response.getErrorCode() + ":" + response.getSubCode() + ":" + response.getSubMsg();
        }

        return response.getUpdateItemResult();
    }

    /**
     * 天猫官网同购更新Item
     * 淘宝接口名：tmall.item.simpleschema.update
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=26072
     *
     * @param shopBean     店铺
     * @param tbItemSchema 商品 Schema
     * @return numIId，或以 "ERROR:" 开头的错误 Message
     */
    public String updateSimpleItem(ShopBean shopBean, TbItemSchema tbItemSchema) throws TopSchemaException, ApiException {

        String xmlData = SchemaWriter.writeParamXmlString(tbItemSchema.getFields());

        return updateSimpleItem(shopBean, tbItemSchema.getNum_iid(), xmlData);
    }

    /**
     * 天猫官网同购编辑商品的get接口
     * 淘宝接口名：tmall.item.update.simpleschema.get
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=26232
     *
     * @param shopBean 店铺
     * @param numIId   商品id
     * @return numIId
     */
    public TbItemSchema getSimpleItem(ShopBean shopBean, Long numIId) throws ApiException, GetUpdateSchemaFailException, TopSchemaException {

        TmallItemUpdateSimpleschemaGetRequest request = new TmallItemUpdateSimpleschemaGetRequest();
        request.setItemId(numIId);

        TmallItemUpdateSimpleschemaGetResponse response = reqTaobaoApi(shopBean, request);

        String result = response.getResult();

        // 调用淘宝API未成功或者subCode不为空的时候(errorCode是英文错误，subCode是中文错误)
        if (!response.isSuccess() || !StringUtils.isEmpty(response.getSubCode()) || StringUtils.isEmpty(result))
            throw new GetUpdateSchemaFailException(response);

        List<Field> fields = SchemaReader.readXmlForList(result);

        return new TbItemSchema(numIId, fields, true);
    }
}
