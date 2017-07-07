define(function () {

    // 缓存作用域
    const CACHE = {
        NONE: 0,
        ONCE: 1,
        SESSION: 2,
        LOCAL: 3
    };

    // 作为额外缓存关键字的关键字名称
    const KEY = {
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
        "core": {
            "access": {
                "user": {
                    "logout": "/core/access/user/logout"
                }
            },
            "home": {
                "menu": {
                    "getMenuHeaderInfo": "/core/home/menu/getMenuHeaderInfo",
                    "setLanguage": "/core/home/menu/setLanguage"
                }
            }
        },
        "cms-us": {
            "home": {
                "$menuService": {
                    "root": "/cms/home/menu/",
                    "getCategoryInfo": session('getCategoryInfo', [KEY.CHANNEL]),
                    "getPlatformType": session('getPlatformType', [KEY.USERNAME, KEY.CHANNEL]),
                    "setPlatformType": "setPlatformType",
                    "getHomeSumData": "getHomeSumData",
                    "getCmsConfig": session('getCmsConfig', [KEY.CHANNEL]),
                    getMenuHeaderInfo: "getMenuHeaderInfo"
                },
                "$modifyPassWordService": {
                    "root": "/cms/home/menu/modifyPassword/",
                    "save": "save"
                }
            },
            "search": {
                "$feedSearchService": {
                    "root": "/cms/search/feed/",
                    "init": "init",
                    "search": "search",
                    "updateFeedStatus": "updateFeedStatus",
                    "doExport": "export",
                    "exportSearch": "exportSearch",
                    "download": "download",
                    "updateMainCategory":"updateMainCategory",
                    "batchUpdateMainCategory":"batchUpdateMainCategory"
                }
            },
            "itemDetailService":{
                root:'/cms/us/feed',
                detail:"detail"
            }
        }
    };
});