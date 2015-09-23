/**
 * @Name:    popWangWangId.js
 * @Date:    2015/5/9
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {

    var mainApp = require ('components/app');
    var _ = require ('underscore');

    mainApp.directive ('popWangwang', ['$window', function ($window) {

        return {
            restrict: 'A',
            scope: {
                "wangwangId": '=',
                "sourceOrderId": "="
            },
            link: function (scope, element) {

                $ (element).addClass ("carousel");

                var wangWangUrl = "http://www.taobao.com/webww/?ver=1&touid=cntaobao{0}&siteid=cntaobao&status=1&portalId=&gid=$bizOrder.itemID:{1}&itemsId=";
                var wangWangImgUrl = "http://amos.im.alisoft.com/online.aw?v=2&uid={0}&site=cntaobao&s=2&charset=utf-8";

                // 旺旺图片加载
                scope.$watch ("wangwangId", function () {
                    if (!_.isEmpty (scope.wangwangId)) {                	
                        var imgPath = wangWangImgUrl.replace ("{0}", scope.wangwangId);
                        $ (element).attr ("src", imgPath);
                    }
                });

                element[0].onclick = function () {

                    if (!_.isEmpty (scope.wangwangId)
                        && !_.isEmpty (scope.sourceOrderId)) {
                        var realPage = wangWangUrl.replace ("{0}", scope.wangwangId).replace ("{1}", scope.sourceOrderId);
                        $window.open (realPage);
                    }
                }
            }
        };
    }]);
    
    mainApp.directive ('popHref', ['$window', function ($window) {

        return {
            restrict: 'A',
            scope: {
                "popHref": '='
            },
            link: function (scope, element) {

                $ (element).addClass ("carousel");

                element[0].onclick = function () {

                    if (!_.isEmpty (scope.popHref)) {
                        $window.open (scope.popHref);
                    }
                }
            }
        };

    }]);
});
