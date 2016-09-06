package com.voyageone.components.tmall.service;

import com.taobao.api.ApiException;
import com.taobao.api.request.TmallItemSimpleschemaAddRequest;
import com.taobao.api.request.TmallItemSimpleschemaUpdateRequest;
import com.taobao.api.response.TmallItemSimpleschemaAddResponse;
import com.taobao.api.response.TmallItemSimpleschemaUpdateResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.TbBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 天猫官网同购上新接口调用（简化方式发布商品）
 * Created by desmond on 8/25/2016.
 */
@Component
public class TbSimpleItemService extends TbBase {

    /**
     * 天猫官网同购新增Item
     * 淘宝接口名：tmall.item.simpleschema.add
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=25455
     *
     * @param shopBean 店铺
     * @param schemaXml xml
     * @return numIId
     * @throws ApiException
     */
    public String addSimpleItem(ShopBean shopBean, String schemaXml) throws ApiException {

        TmallItemSimpleschemaAddRequest request = new TmallItemSimpleschemaAddRequest();
        request.setSchemaXmlFields(schemaXml);

        TmallItemSimpleschemaAddResponse response = reqTaobaoApi(shopBean, request);

        // 调用淘宝API未成功或者subCode不为空的时候(errorCode是英文错误，subCode是中文错误)
        if (!response.isSuccess() || !StringUtils.isEmpty(response.getSubCode())) {
            logger.error("addSimpleItem [SubMsg = " + response.getSubMsg() + "]");
            return "ERROR:"+ response.getErrorCode() + ":" + response.getSubCode() + ":" + response.getSubMsg();
        }

        return response.getResult();
    }

    /**
     * 天猫官网同购更新Item
     * 淘宝接口名：tmall.item.simpleschema.update
     * 文档地址：http://open.taobao.com/docs/api.htm?apiId=26072
     *
     * @param shopBean 店铺
     * @param numIId numIId
     * @param schemaXml xml
     * @return numIId
     * @throws ApiException
     */
    public String updateSimpleItem(ShopBean shopBean, Long numIId, String schemaXml) throws ApiException {

        TmallItemSimpleschemaUpdateRequest request = new TmallItemSimpleschemaUpdateRequest();

        request.setItemId(numIId);
        request.setSchemaXmlFields(schemaXml);

        TmallItemSimpleschemaUpdateResponse response = reqTaobaoApi(shopBean, request);

        // 调用淘宝API未成功或者subCode不为空的时候(errorCode是英文错误，subCode是中文错误)
        if (!response.isSuccess() || !StringUtils.isEmpty(response.getErrorCode())) {
            logger.error("updateSimpleItem [SubMsg = " + response.getSubMsg() + "]");
            return "ERROR:"+ response.getErrorCode() + ":" + response.getSubCode() + ":" + response.getSubMsg();
        }

        return response.getUpdateItemResult();
    }

}
