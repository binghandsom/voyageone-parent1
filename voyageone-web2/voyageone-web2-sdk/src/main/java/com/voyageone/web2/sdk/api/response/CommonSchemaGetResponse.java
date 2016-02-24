package com.voyageone.web2.sdk.api.response;

import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CommonSchemaGetResponse extends VoApiListResponse {

    private List<Field> fields;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
