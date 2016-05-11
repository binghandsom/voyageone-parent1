package com.voyageone.common.serialize.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.taobao.top.schema.enums.FieldTypeEnum;
import com.taobao.top.schema.field.Field;

public class TmallFieldDeserializer extends JsonDeserializer<Field> {

    public static final String FIELD_TYPE_NAME = "type";

    @Override
    public Field deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        TokenBuffer tb = new TokenBuffer(p);
        tb.copyCurrentStructure(p);

        Field field = getFieldInstance(tb);
        JsonDeserializer<Object> fieldDeserializer = ctxt.findRootValueDeserializer(ctxt.getTypeFactory().constructType(field.getClass()));
        field = (Field) fieldDeserializer.deserialize(p, ctxt, field);
        return field;
    }

    Field getFieldInstance(TokenBuffer tb) throws IOException {
        String inputType = "";
        JsonParser jp = tb.asParser();
        jp.nextToken();
        do {
            if (FIELD_TYPE_NAME.equals(jp.getCurrentName())) {
                inputType = jp.nextTextValue();
                break;
            }
        } while (jp.nextToken() != JsonToken.END_OBJECT);

        FieldTypeEnum fte = FieldTypeEnum.valueOf(inputType);

        return FieldTypeEnum.createField(fte);
    }
}
