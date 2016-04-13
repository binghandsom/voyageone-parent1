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
    function Cart(name, id, desc) {
        this.name = name;
        this.id = id;
        this.desc = desc;
    }

    return {
        /**
         * 天猫
         */
        TM: new Cart('TM', 20, '天猫'),

        /**
         * 淘宝
         */
        TB: new Cart('TB', 21, '淘宝'),

        /**
         * 线下
         */
        OF: new Cart('OF', 22, '线下'),

        /**
         * 天猫国际
         */
        TG: new Cart('TG', 23, '天猫国际'),

        /**
         * 京东
         */
        JD: new Cart('JD', 24, '京东'),

        /**
         * 独立域名
         */
        CN: new Cart('CN', 25, '独立域名'),

        /**
         * 京东国际
         */
        JG: new Cart('JG', 26, '京东国际'),

        /**
         * 聚美优品
         */
        JM: new Cart('JM', 27, '聚美优品'),

        /**
         * Mini Mall
         */
        MM: new Cart('MM', 28, 'Mini Mall'),

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