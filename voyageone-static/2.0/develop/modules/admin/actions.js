define(function () {

    // 缓存作用域
    var CACHE = {
        NONE: 0,
        ONCE: 1,
        SESSION: 2,
        LOCAL: 3
    };

    // 作为额外缓存关键字的关键字名称
    var KEY = {
        USERNAME: 'username',
        CHANNEL: 'channel'
    };

    /**
     * 生产一个配置好的 action 配置对象
     * @param {string} action Action 名称
     * @param {Array} [cacheWith] 缓存时, 额外追加的缓存关键字, 参见 KEY 对象提供的字段
     * @returns {{url: *, cache: number, cacheWith: *}}
     */
    function session(action, cacheWith) {
        return {
            url: action,
            cache: CACHE.SESSION,
            cacheWith: cacheWith
        };
    }

    return {
        "admin": {
            /** 渠道信息 */
            "channel": {
                "channelService": {
                    "root": "/admin/channel/self",
                    "getAllChannel": 'getAllChannel',
                    "getAllCompany": 'getAllCompany',
                    "addChannel": "addChannel",
                    "updateChannel": "updateChannel",
                    "deleteChannel": "deleteChannel",
                    "searchChannelByPage": "searchChannelByPage",
                    "generateSecretKey": "generateSecretKey",
                    "generateSessionKey": "generateSessionKey",
                    "searchChannelConfigByPage": "searchChannelConfigByPage",
                    "addChannelConfig": "addChannelConfig",
                    "updateChannelConfig": "updateChannelConfig",
                    "deleteChannelConfig": "deleteChannelConfig"
                },
                "channelAttributeService": {
                    "root": "/admin/channel/attribute",
                    "searchChannelAttributeByPage": 'searchChannelAttributeByPage',
                    "addChannelAttribute": 'addChannelAttribute',
                    "updateChannelAttribute": "updateChannelAttribute",
                    "deleteChannelAttribute": "deleteChannelAttribute"
                },
                "thirdPartyConfigService": {
                    "root": "/admin/channel/thirdParty",
                    "searchThirdPartyConfigByPage": 'searchThirdPartyConfigByPage',
                    "addThirdPartyConfig": 'addThirdPartyConfig',
                    "updateThirdPartyConfig": "updateThirdPartyConfig",
                    "deleteThirdPartyConfig": "deleteThirdPartyConfig"
                }
            },
            /** Cart信息 */
            "cart": {
                "AdminCartService": {
                    "root": "/admin/cart/self",
                    "getAllCart": "getAllCart",
                    "getCartByIds": "getCartByIds",
                    "searchCartByPage": "searchCartByPage",
                    "getAllPlatform": "getAllPlatform",
                    "addCart": "addCart",
                    "updateCart": "updateCart",
                    "deleteCart": "deleteCart",
                    "deleteStore": "deleteStore"
                }
            },
            /** 统一属性配置 */
            "config": {
                "AdminChannelService": {
                    "root": "/admin/system/config"
                }
            },
            /** 仓库信息 */
            "store": {
                "storeService": {
                    "root": "/admin/store/self",
                    "searchStoreByPage": "searchStoreByPage",
                    "searchStoreConfigByPage": "searchStoreConfigByPage",
                    "getAllStore": "getAllStore",
                    "addStore": "addStore",
                    "updateStore": "updateStore",
                    "deleteStore": "deleteStore",
                    "addStoreConfig": "addStoreConfig",
                    "updateStoreConfig": "updateStoreConfig",
                    "deleteStoreConfig": "deleteStoreConfig"
                }
            },
            /** 统一属性配置 */
            "type": {
                "typeService": {
                    "root": "/admin/system/type",
                    "searchTypeByPage": "searchTypeByPage",
                    "addType": "addType",
                    "updateType": "updateType",
                    "deleteType": "deleteType",
                    "getAllType": "getAllType"
                }
            }
        }
    };
});