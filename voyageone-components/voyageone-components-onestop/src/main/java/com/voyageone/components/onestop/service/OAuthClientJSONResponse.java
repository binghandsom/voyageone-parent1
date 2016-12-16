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
    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) ->
                    Date.from(OffsetDateTime.parse(json.getAsString()).toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant()))
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

    public static void main(String[] args) {
//        OffsetDateTime odt = ;
//        Date from = ;
//        System.out.println(from);
//        System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2016-11-11T11:47:20.107-08:00"));
    }

    public <T> T getBodyAs(Class<T> clz) {
        Preconditions.checkArgument(clz!=null,"class must not null");
        return gson.fromJson(getBody(), clz);
    }

    public <T> T getBodyAs(Type type) {

        Preconditions.checkArgument(type!=null,"class must not null");
        return gson.fromJson(getBody(), type);
    }
}
