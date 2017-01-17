/**
 * 用于ng-repeat的过滤功能
 *
 * controller定义在angular 主入口:app.js
 */

define(function () {

    function RepeatFilter() {

        this.byAddName2 = function (item) {
            var filterAddName2s = ['SN', 'WS', 'AM', 'RX', 'SM'];

            if (!item || !item.add_name2)
                return true;

            return filterAddName2s.indexOf(item.add_name2) < 0;
        }

    }

    return RepeatFilter;

});
