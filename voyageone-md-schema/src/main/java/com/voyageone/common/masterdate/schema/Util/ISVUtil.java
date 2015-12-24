package com.voyageone.common.masterdate.schema.util;

import com.voyageone.common.masterdate.schema.value.Value;
import java.util.ArrayList;
import java.util.List;

public class ISVUtil {

    public static List<Value> getValueListFromString(List<String> values) {
        if(values == null) {
            return null;
        } else {
            List<Value> result = new ArrayList<>();

            for (String v : values) {
                Value value = new Value();
                value.setValue(v);
                result.add(value);
            }

            return result;
        }
    }
}
