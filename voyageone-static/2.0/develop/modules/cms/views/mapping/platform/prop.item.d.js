/**
 * @ngdoc
 * @directive
 * @name platformProp
 */

define([
    'cms'
], function (cms) {
    'use strict';
    return cms.directive('platformProp', function () {

        return {
            restrict: 'A',
            // 地址基于地址栏定位, 启动的画面变动, 这里也要跟随变动
            templateUrl: 'views/mapping/platform/prop.item.d.html',
            scope: {
                property: '=platformProp',
                popup: '&platformPropPopup'
            }
        }
    });
});