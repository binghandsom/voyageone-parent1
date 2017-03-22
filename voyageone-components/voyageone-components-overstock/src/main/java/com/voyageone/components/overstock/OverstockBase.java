package com.voyageone.components.overstock;

import com.overstock.mp.mpc.externalclient.Client;
import com.overstock.mp.mpc.externalclient.Credentials;
import com.overstock.mp.mpc.externalclient.api.ClientFactory;
import com.overstock.mp.mpc.externalclient.api.Result;
import com.overstock.mp.mpc.externalclient.model.EventStatusType;
import com.overstock.mp.mpc.externalclient.model.EventTypeType;
import com.overstock.mp.mpc.externalclient.model.EventsType;
import com.voyageone.common.configs.ThirdPartyConfigs;
import com.voyageone.components.ComponentBase;
import org.springframework.util.StringUtils;

/**
 * @author aooer 2016/6/7.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class OverstockBase extends ComponentBase {

    private ClientFactory client;

    private Credentials credentials;

    /**
     * 获取client工厂
     *
     * @return clientFactory
     */
    protected ClientFactory getClientFactory() {
        return StringUtils.isEmpty(client) ? Client.withBaseUrl(getV("api_url")) : client;
    }

    /**
     * 获取证书
     *
     * @return 证书
     */
    protected Credentials getCredentials() {
        return StringUtils.isEmpty(credentials) ? getClientFactory().createCredentials(getV("api_username"), getV("api_password"), getV("api_channelkey")) : credentials;
    }

    /**
     * 获取第三方配置
     *
     * @param key key
     * @return key
     */
    private String getV(String key) {
////        if (key.equals("api_url")) return "https://mpc-sandbox.test.overstock.com/api/v1";
//        if (key.equals("api_url")) return "https://mpc-sandbox.overstock.com/api/v1";
//        if (key.equals("api_username")) return "voyageone";
////        if (key.equals("api_password")) return "&zbrbqroxxl1Vtj";
//        if (key.equals("api_password")) return "password";
//        if (key.equals("api_channelkey")) return "VOYAGEONE";
        return ThirdPartyConfigs.getVal1("024", key);
    }

    /**
     * query for many event
     *
     * @return 结果集
     * @throws Exception
     */
    protected Result<EventsType> queryingForNewEvents(EventTypeType type) throws Exception {
        return getClientFactory().forEvents()
                .getMany()
                .withStatus(EventStatusType.NEW)
                .withType(type)
                .build()
                .execute(getCredentials());
    }

    protected Result<EventsType> queryingForNewEvents(EventTypeType type, Integer limit) throws Exception {
        return getClientFactory().forEvents()
                .getMany()
                .withStatus(EventStatusType.NEW)
                .withType(type)
                .withLimit(limit)
                .build()
                .execute(getCredentials());
    }
}
