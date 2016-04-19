package com.voyageone.common.util;

import com.voyageone.common.util.CamelUtil;

import java.util.HashMap;

/**
 * Created by dell on 2016/3/30.
 */
public class MapCamel extends HashMap<String,Object> {
    @Override
    public Object put(String key, Object value) {
        return super.put(CamelUtil.underlineToCamel(key), value);
    }
}
