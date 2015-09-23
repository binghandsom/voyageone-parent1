package com.voyageone.common.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.request.TmallItemSchemaUpdateRequest;
import com.taobao.api.request.TmallItemUpdateSchemaGetRequest;
import com.taobao.api.response.TmallItemSchemaUpdateResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.top.schema.exception.TopSchemaException;
import com.taobao.top.schema.factory.SchemaReader;
import com.taobao.top.schema.factory.SchemaWriter;
import com.taobao.top.schema.field.Field;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.components.tmall.exceptions.GetUpdateSchemaFailException;
import com.voyageone.common.configs.beans.ShopBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 淘宝平台，商品更新接口调用
 * Created by Jonas on 7/16/15.
 */
@Component
public class TbItemService extends TbBase {
    /**
     * 淘宝接口名：tmall.item.update.schema.get。
     * 文档地址：http://open.taobao.com/apidoc/api.htm?spm=a219a.7386789.1998342952.14.0uzLoC&path=scopeId:11430-apiId:23435
     *
     * @param shopBean  店铺
     * @param num_iid   商品
     * @return 查询到的所有字段
     * @throws ApiException
     * @throws TopSchemaException
     */
    public TbItemSchema getUpdateSchema(ShopBean shopBean, long num_iid) throws ApiException, TopSchemaException, GetUpdateSchemaFailException {

        TmallItemUpdateSchemaGetRequest request = new TmallItemUpdateSchemaGetRequest();

        request.setItemId(num_iid);

        TmallItemUpdateSchemaGetResponse res = reqTaobaoApi(shopBean, request);

        if (!res.isSuccess() || !StringUtils.isEmpty(res.getSubCode())) throw new GetUpdateSchemaFailException(res);

        // 转换 Schema Xml 到 Field Bean 集合
        List<Field> fields = SchemaReader.readXmlForList(res.getUpdateItemResult());

        return new TbItemSchema(num_iid, fields);
    }

    /**
     * 全量更新淘宝的商品。
     *
     * 淘宝接口名：tmall.item.schema.update
     * 文档地址：http://open.taobao.com/apidoc/api.htm?spm=a219a.7386789.1998342952.66.rW9rTR&path=cid:4-apiId:23434
     */
    public TmallItemSchemaUpdateResponse updateFields(ShopBean shopBean, TbItemSchema tbItemSchema) throws ApiException, TopSchemaException {

        TmallItemSchemaUpdateRequest req = new TmallItemSchemaUpdateRequest();

        req.setItemId(tbItemSchema.getNum_iid());

        String xmlData = SchemaWriter.writeParamXmlString(tbItemSchema.getFields());

        req.setXmlData(xmlData);

        return reqTaobaoApi(shopBean, req);
    }
}
