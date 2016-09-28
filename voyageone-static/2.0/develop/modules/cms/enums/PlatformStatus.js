/**
 * 平台状态
 */
define(['underscore'], function (_) {

    /**
     * @constructor 平台状态
     *
     * @param {number} id   状态
     * @param {string} name 状态名称
     */
    function PlatformStatus(id, name) {
        this.id = id;
        this.name = name;
    }

    return {

        OnSale: new PlatformStatus('OnSale', '在售'),
        InStock: new PlatformStatus('InStock', '在库'),
        WaitingPublish: new PlatformStatus('WaitingPublish', '等待上新'),

        /**
         * 显示商品上下架状态
         * @param  val   平台状态
         * @param  val2  实际平台状态
         * @param  val3  商品状态
         */
        getStsTxt: function(val, val2, val3) {
            // 按 name 查找
            if (val3 != 'Approved') {
                return '';
            }
            var stsItem = this[val];
            var stsTxt = (stsItem instanceof PlatformStatus) ? stsItem.name : '';
            if (val2 == 'OnSale' && val2 != val) {
                return stsTxt + '-已上架';
            } else if (val2 == 'InStock' && val2 != val) {
                return stsTxt + '-未上架';
            } else {
                return stsTxt;
            }
        }
    };
});