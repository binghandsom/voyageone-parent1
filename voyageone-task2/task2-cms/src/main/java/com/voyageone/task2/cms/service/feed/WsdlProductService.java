package com.voyageone.task2.cms.service.feed;

import com.google.gson.Gson;
import com.voyageone.task2.cms.bean.WsdlResponseBean;
import com.voyageone.task2.cms.utils.WebServiceUtil;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feeds;
import com.voyageone.task2.cms.bean.ProductsFeedAttribute;
import com.voyageone.task2.cms.bean.ProductsFeedInsert;
import com.voyageone.task2.cms.bean.ProductsFeedUpdate;

import java.util.HashMap;
import java.util.Map;

/**
 * 产品信息服务调用接口
 * Created by Jonas on 10/13/15.
 */
public class WsdlProductService {

    private static final String INSERT = "01";

    private static final String UPDATE = "02";

    private static final String ATTRIBUTE = "03";

    private Gson gson = new Gson();

    private ChannelConfigEnums.Channel channel;

    /**
     * 产品信息服务调用接口
     *
     * @param channel 渠道
     */
    public WsdlProductService(ChannelConfigEnums.Channel channel) {
        this.channel = channel;
    }

    private String getUrl(String action) {
        return Codes.getCodeName("WEB_SERVIES_URL_FEED", action);
    }

    private String post(String action, Object dataBody) throws Exception {

        String channel_id = channel.getId();

        Map<String, Object> param = new HashMap<>();

        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppKey));
        authMap.put("appSecret", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesAppSecret));
        authMap.put("sessionKey", Feeds.getVal1(channel_id, FeedEnums.Name.webServiesSessionKey));

        param.put("authentication", authMap);
        param.put("dataBody", dataBody);

        String jsonParam = gson.toJson(param);

        return WebServiceUtil.postByJsonStr(getUrl(action), jsonParam);
    }

    /**
     * 插入商品信息
     *
     * @param productsFeed 接口参数
     * @return 接口的响应内容
     * @throws Exception
     */
    public WsdlProductInsertResponse insert(ProductsFeedInsert productsFeed) throws Exception {

        String response = post(INSERT, productsFeed);

        return new Gson().fromJson(response, WsdlProductInsertResponse.class);
    }

    /**
     * 更新商品信息
     *
     * @param productsFeed 接口参数
     * @return 接口的响应内容
     * @throws Exception
     */
    public WsdlProductUpdateResponse update(ProductsFeedUpdate productsFeed) throws Exception {

        String response = post(UPDATE, productsFeed);

        return new Gson().fromJson(response, WsdlProductUpdateResponse.class);
    }

    /**
     * 插入或更新商品的具体属性
     *
     * @param productsFeed 接口参数
     * @return 接口的响应内容
     * @throws Exception
     */
    public WsdlResponseBean attribute(ProductsFeedAttribute productsFeed) throws Exception {

        String response = post(ATTRIBUTE, productsFeed);

        return new Gson().fromJson(response, WsdlProductUpdateResponse.class);
    }
    public WsdlResponseBean attribute(Map productsFeed) throws Exception {

        String response = post(ATTRIBUTE, productsFeed);

        return new Gson().fromJson(response, WsdlProductUpdateResponse.class);
    }
}
