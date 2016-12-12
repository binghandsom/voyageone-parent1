package com.voyageone.components.onestop.service;


import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;

import java.lang.reflect.Type;

/**
 *
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/21 19:03
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OAuthClientJSONResponse extends OAuthResourceResponse {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
    public <T> T getBodyAs(Class<T> clz) {
        Preconditions.checkArgument(clz!=null,"class must not null");
        return gson.fromJson(getBody(), clz);
    }

    public <T> T getBodyAs(Type type) {
        Preconditions.checkArgument(type!=null,"class must not null");
        return gson.fromJson(getBody(), type);
    }
}
