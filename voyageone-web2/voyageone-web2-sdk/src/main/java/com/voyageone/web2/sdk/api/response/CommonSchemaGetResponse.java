package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
public class CommonSchemaGetResponse extends VoApiListResponse {

    private List<Map<String, Object>> fields;

    public List<Map<String, Object>> getFields() {
        return fields;
    }

    public void setFields(List<Map<String, Object>> fields) {
        this.fields = fields;
    }
}
