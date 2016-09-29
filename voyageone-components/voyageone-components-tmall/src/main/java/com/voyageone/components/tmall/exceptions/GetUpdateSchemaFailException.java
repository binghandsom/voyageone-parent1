package com.voyageone.components.tmall.exceptions;

import com.taobao.api.TaobaoResponse;
import com.taobao.api.response.TmallItemUpdateSchemaGetResponse;
import com.taobao.api.response.TmallItemUpdateSimpleschemaGetResponse;

/**
 * 调用淘宝 api: taobao.item.update.schema.get / tmall.item.update.simpleschema.get 失败
 * <p>
 * Created by Jonas on 8/11/15.
 *
 * @version 2.6.0
 * @since 2.0.0
 */
public class GetUpdateSchemaFailException extends Exception {

    private TaobaoResponse taobaoResponse;

    public GetUpdateSchemaFailException(TmallItemUpdateSchemaGetResponse tmallItemUpdateSchemaGetResponse) {
        super(tmallItemUpdateSchemaGetResponse.getSubMsg());
        this.taobaoResponse = tmallItemUpdateSchemaGetResponse;
    }

    /**
     * @since 2.6.0
     * @param tmallItemUpdateSimpleschemaGetResponse 同购接口获取的 schema 响应结果
     */
    public GetUpdateSchemaFailException(TmallItemUpdateSimpleschemaGetResponse tmallItemUpdateSimpleschemaGetResponse) {
        super("ERROR:" + tmallItemUpdateSimpleschemaGetResponse.getErrorCode() + ":" +
                tmallItemUpdateSimpleschemaGetResponse.getSubCode() + ":" +
                tmallItemUpdateSimpleschemaGetResponse.getSubMsg());
        this.taobaoResponse = tmallItemUpdateSimpleschemaGetResponse;
    }

    public String getSubMsg() {
        return taobaoResponse.getSubMsg();
    }

    public String getSubCode() {
        return taobaoResponse.getSubCode();
    }
}
