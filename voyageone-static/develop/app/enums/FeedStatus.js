/**
 * feed 状态
 */
define(['underscore'], function (_) {

    /**
     * @constructor feed 状态
     *
     * @param {number} id   状态
     * @param {string} name 状态名称
     */
    function FeedStatus(id, name) {
        this.id = id;
        this.name = name;
    }

    return {

        New: new FeedStatus(9, 'TXT_FeedStatus_New'),
        WaitingForImport: new FeedStatus(0, 'TXT_FeedStatus_WaitingForImport'),
        FinishImport: new FeedStatus(1, 'TXT_FeedStatus_FinishImport'),
        Error: new FeedStatus(2, 'TXT_FeedStatus_Error'),
        NotImport: new FeedStatus(3, 'TXT_FeedStatus_NotImport'),

        /**
         * 获取枚举
         * @param {number} val
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
            var cart = this[val];
            return (cart instanceof FeedStatus) ? cart : null;
        }
    };
});