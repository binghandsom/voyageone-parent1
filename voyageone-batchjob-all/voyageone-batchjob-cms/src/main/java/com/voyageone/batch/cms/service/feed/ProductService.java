package com.voyageone.batch.cms.service.feed;

import com.google.gson.Gson;
import com.voyageone.batch.cms.bean.ProductsFeedAttribute;
import com.voyageone.batch.cms.bean.ProductsFeedInsert;
import com.voyageone.batch.cms.bean.ProductsFeedUpdate;
import com.voyageone.batch.cms.bean.WsdlResponseBean;
import com.voyageone.batch.cms.utils.WebServiceUtil;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.Feed;

import java.util.HashMap;
import java.util.Map;

/**
 * 产品信息服务调用接口
 * <p>
 * Created by Jonas on 10/13/15.
 */
public class ProductService {

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
    public ProductService(ChannelConfigEnums.Channel channel) {
        this.channel = channel;
    }

    private String getUrl(String action) {
        return Codes.getCodeName("WEB_SERVIES_URL_FEED", action);
    }

    private WsdlResponseBean post(String action, Object dataBody) throws Exception {

        String channel_id = channel.getId();

        Map<String, Object> param = new HashMap<>();

        Map<String, String> authMap = new HashMap<>();
        authMap.put("appKey", Feed.getVal1(channel_id, FeedEnums.Name.webServiesAppKey));
        authMap.put("appSecret", Feed.getVal1(channel_id, FeedEnums.Name.webServiesAppSecret));
        authMap.put("sessionKey", Feed.getVal1(channel_id, FeedEnums.Name.webServiesSessionKey));

        param.put("authentication", authMap);
        param.put("dataBody", dataBody);

        String jsonParam = gson.toJson(param);

        String response = WebServiceUtil.postByJsonStr(getUrl(action), jsonParam);

        return gson.fromJson(response, WsdlResponseBean.class);
    }

    /**
     * 插入商品信息
     *
     * @param productsFeed 接口参数
     * @return 接口的响应内容
     * @throws Exception
     */
    public WsdlResponseBean insert(ProductsFeedInsert productsFeed) throws Exception {
        return post(INSERT, productsFeed);
    }

    /**
     * 更新商品信息
     *
     * @param productsFeed 接口参数
     * @return 接口的响应内容
     * @throws Exception
     */
    public WsdlResponseBean update(ProductsFeedUpdate productsFeed) throws Exception {
        return post(UPDATE, productsFeed);
    }

    /**
     * ???
     *
     * @param productsFeed 接口参数
     * @return 接口的响应内容
     * @throws Exception
     */
    public WsdlResponseBean attribute(ProductsFeedAttribute productsFeed) throws Exception {
        return post(ATTRIBUTE, productsFeed);
    }
}
