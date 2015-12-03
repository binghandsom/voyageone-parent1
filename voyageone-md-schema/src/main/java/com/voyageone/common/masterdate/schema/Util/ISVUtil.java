package com.voyageone.common.masterdate.schema.Util;

import com.voyageone.common.masterdate.schema.value.Value;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ISVUtil {
    public ISVUtil() {
    }

    public static List<Value> getValueListFromString(List<String> values) {
        if(values == null) {
            return null;
        } else {
            ArrayList result = new ArrayList();
            Iterator i$ = values.iterator();

            while(i$.hasNext()) {
                String v = (String)i$.next();
                Value value = new Value();
                value.setValue(v);
                result.add(value);
            }

            return result;
        }
    }
}
