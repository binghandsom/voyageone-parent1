/**
 * 平台枚举
 */
define(['underscore'], function (_) {

    /**
     * @description
     * 同步 com.voyageone.common.configs.Enums.CartEnums.Cart
     *
     * @param {string} name 枚举名称
     * @param {number} id   平台 ID
     * @param {string} desc 平台名称
     * @constructor
     */
    function Cart(name, id, desc, pUrl, platformId) {
        this.name = name;
        this.id = id;
        this.desc = desc;
        this.pUrl = pUrl;
        this.platformId = platformId;
    }

    return {
        /**
         * Master
         */
        MASTER:new Cart('MASTER' , 0 , '主数据','', ''),
        /**
         * Feed
         */
        FEED:new Cart('FEED' , 1 , 'Feed数据','', ''),
        /**
         * Feed
         */
        FEED:new Cart('SN' , 1 , 'Sneakerhead','', '5'),
        /**
         * Feed
         */
        FEED:new Cart('WS' , 3 , 'Sneakerhead WS','', '5'),
        /**
         * Feed
         */
        FEED:new Cart('AM' , 5 , 'Amazon','', '5'),
        /**
         * Feed
         */
        FEED:new Cart('RX' , 7 , 'Sneaker RX','', ''),
        /**
         * Feed
         */
        FEED:new Cart('SM' , 9 , 'Sneakerhead Mobile','', ''),
        /**
         * 天猫
         */
        TM: new Cart('TM', 20, '天猫', 'https://detail.tmall.com/item.htm?id=', '1'),

        /**
         * 淘宝
         */
        TB: new Cart('TB', 21, '淘宝', '', '1'),

        /**
         * 线下
         */
        OF: new Cart('OF', 22, '线下', '', '0'),

        /**
         * 天猫国际
         */
        TG: new Cart('TG', 23, '天猫国际', 'https://detail.tmall.hk/hk/item.htm?id=', '1'),

        /**
         * 京东
         */
        JD: new Cart('JD', 24, '京东', 'https://item.jd.hk/', '2'),

        /**
         * 独立域名
         */
        CN: new Cart('CN', 25, '独立域名', '', '3'),

        /**
         * 京东国际
         */
        JG: new Cart('JG', 26, '京东国际', 'https://item.jd.hk/', '2'),

        /**
         * 聚美优品 (注意：聚美的产品url后缀要添加'.html')
         */
        JM: new Cart('JM', 27, '聚美优品', 'http://item.jumeiglobal.com/', '4'),

        /**
         * 天猫国际官网同购
         */
        TT: new Cart('TT', 30, '天猫国际官网同购', 'https://detail.tmall.hk/hk/item.htm?id=', '1'),
        /**
         * 天猫国际USJOI官网同购
         */
        LTT: new Cart('LTT', 31, '天猫国际USJOI官网同购', 'https://detail.tmall.hk/hk/item.htm?id=', '1'),
        /**
         * Liking官网
         */
        LCN: new Cart('LCN', 32, 'Liking官网', 'http://www.liking.com/prod_', '3'),
        /**
         * 分销
         */
        DT: new Cart('DT', 33, '分销', 'http://fenxiao.voyageone.com.cn/prod-detail?channelId=928&id=', '6'),


        /**
         * 京东匠心界
         */
        USJGJ: new Cart('USJGJ', 928, 'USJOI匠心界', '', '2'),

        /**
         * 京东悦境
         */
        USJGY: new Cart('USJGY', 929, 'USJOI悦境', '', '2'),

        /**
         * 测试USJOI
         */
        USJGT: new Cart('USJGT', 998, 'USJOI测试京东', '', '2'),

        /**
         * 京东匠心界
         */
        JGJ: new Cart('JGJ', 28, '京东匠心界', 'https://item.jd.hk/', '2'),

        /**
         * 京东悦境
         */
        JGY: new Cart('JGY', 29, '京东悦境', 'https://item.jd.hk/', '2'),

        /**
         * 测试USJOI
         */
        JGT: new Cart('JGT', 98, 'USJOI测试', '', '1'),

        /**
         * 考拉
         */
        KAOLA: new Cart('KAOLA',34,'考拉海外购','http://www.kaola.com/product/', '8'),

        /**
         * Mini Mall
         */
        MM: new Cart('MM', 99, 'US Joi', '', ''),

        SN: new Cart('SN', 1, 'Sneakerhead', '', '5'),

        SNWS: new Cart('SNWS', 3, 'Sneakerhead WS', '', ''),

        AMZN: new Cart('AMZN', 5, 'Amazon', '', ''),

        SRX: new Cart('SRX', 7, 'Sneaker RX', '', ''),

        SNM: new Cart('SNM', 9, 'Sneakerhead Mobile', '', ''),

        /**
         * 获取枚举, desc 不支持
         * @param {string|number} val
         */
        valueOf: function(val) {

            // 如果是数字,则默认按 id 查找
            if (_.isNumber(val)) {
                return _.find(this, function(v) {
                    if (_.isFunction(v)) return false;
                    return v.id === val;
                });
            }

            // 否则按 name 查找
            // 暂时不支持按 desc 查找
            var cart = this[val];

            return (cart instanceof Cart) ? cart : null;
        }
    };
});