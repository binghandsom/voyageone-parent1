package com.voyageone.common.configs;

import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.FeedEnums;
import com.voyageone.common.configs.beans.FeedBean;

/**
 * Created by jonasvlag on 16/7/20.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public class FeedsHelper {

    private static void put(String channel, String name, String cfg_val1) {

        FeedBean feedBean = new FeedBean() {
            {
                setId(0);
                setOrder_channel_id(channel);
                setCfg_name(name);
                setCfg_val1(cfg_val1);
            }
        };

        Feeds.put(feedBean);
    }

    public static void put(ChannelConfigEnums.Channel channel, FeedEnums.Name name, String cfg_val1) {
        put(channel.getId(), name.name(), cfg_val1);
    }
}
