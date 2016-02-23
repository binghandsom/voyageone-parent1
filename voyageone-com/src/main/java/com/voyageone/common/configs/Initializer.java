package com.voyageone.common.configs;

import com.voyageone.common.configs.dao.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * 访问
 * Created by Jonas on 4/16/2015.
 */
public class Initializer {

    @Autowired
    private CodeDao codeDao;

    @Autowired
    private TypeDao typeDao;

    @Autowired
    private OrderChannelDao orderChannelDao;

    @Autowired
    private OrderChannelConfigDao orderChannelConfigDao;

    @Autowired
    private PortConfigDao portConfigDao;

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private ShopConfigDao shopConfigDao;

    @Autowired
    private StoreConfigDao storeConfigDao;

    @Autowired
    private ThirdPartConfigDao thirdPartConfigDao;
    
    @Autowired
    ImsCategoryDao imsCategoryDao;

    @Autowired
    FeedDao feedDao;

    @Autowired
    private CarrierDao carrierDao;

    @Autowired
    private TypeChannelDao channelValueDao;

    @Autowired
    private CmsChannelConfigDao cmsChannelConfigDao;


    /**
     * 初始化所有配置类的内容
     */
    public void init() throws IOException {
        // 初始化 Code
        Codes.init(codeDao);
        // 初始化 Type
        Type.init(typeDao);
        // 初始化 Port
        PortConfigs.init(portConfigDao);
        // 初始化 Channel
        ChannelConfigs.init(orderChannelDao, orderChannelConfigDao);
        // 初始化 Shop
        ShopConfigs.init(shopDao, shopConfigDao);
        // 初始化 Store
        StoreConfigs.init(storeConfigDao);
        // 初始化 com_mt_third_party_config
        ThirdPartyConfigs.init(thirdPartConfigDao);
        // 初始化 KV 文件
        Properties.init();
        //初始化类目列表
        ImsCategoryConfigs.init(imsCategoryDao);
        //初始化类目列表
        Feed.init(feedDao);
        // 初始化 Carrier
        CarrierConfigs.init(carrierDao);

        // 初始化 Carrier
        TypeChannel.init(channelValueDao);

        // 初始化 cmsChannelConfig(cms_mt_channel_config)
        CmsChannelConfigs.init(cmsChannelConfigDao);
    }
}
