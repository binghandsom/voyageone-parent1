/**
 * Notify 的可用参数
 * {
 *    // whether to hide the notification on click
 *    clickToHide: true,
 *    // whether to auto-hide the notification
 *    autoHide: true,
 *    // if autoHide, hide after milliseconds
 *    autoHideDelay: 5000,
 *    // show the arrow pointing at the element
 *    arrowShow: true,
 *    // arrow size in pixels
 *    arrowSize: 5,
 *    // position defines the notification position though uses the defaults below
 *    position: '...',
 *    // default positions
 *    elementPosition: 'bottom left',
 *    globalPosition: 'top right',
 *    // default style
 *    style: 'bootstrap',
 *    // default class (string or [string])
 *    className: 'error',
 *    // show animation
 *    showAnimation: 'slideDown',
 *    // show animation duration
 *    showDuration: 400,
 *    // hide animation
 *    hideAnimation: 'slideUp',
 *    // hide animation duration
 *    hideDuration: 200,
 *    // padding between element and notification
 *    gap: 2
 * }
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 2.0.0
 */
angular.module("vo.factories").factory("notify", function ($filter) {
    /**
     * @ngdoc function
     * @name voNotify
     * @description
     * 自动关闭的弹出提示框
     */
    function notify(options) {
        if (!options) return;
        if (_.isString(options)) options = {
            message: options
        };
        if (!_.isObject(options)) return;
        var values;
        // 为 translate 的格式化提供支持，检测类型并转换
        if (_.isObject(options.message)) {
            values = options.message.values;
            options.message = options.message.id;
        }
        options.message = $filter("translate")(options.message, values);
        return $.notify(options.message, options);
    }

    notify.success = function (message) {
        return notify({
            message: message,
            className: "success"
        });
    };
    notify.warning = function (message) {
        return notify({
            message: message,
            className: "warning"
        });
    };
    notify.danger = function (message) {
        return notify({
            message: message,
            className: "danger"
        });
    };
    return notify;
});
