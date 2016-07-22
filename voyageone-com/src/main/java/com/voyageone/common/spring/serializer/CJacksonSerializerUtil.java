package com.voyageone.common.spring.serializer;

import com.fasterxml.jackson.core.JsonGenerator;


public abstract class CJacksonSerializerUtil {

    static boolean isCustom(JsonGenerator jgen) {
        Object outputTarget = jgen.getOutputTarget();
        return outputTarget != null && outputTarget instanceof CustomServletOutputStream && ((CustomServletOutputStream) outputTarget).isCustom();
    }
}
