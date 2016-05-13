/**
 * @Description: 根据键名显示对应的值
 * @Date:    2016/04/22
 * @User:    JiangJusheng
 * @Version: 2.0.0
 */
define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, Carts) {
    'use strict';
    return cms.directive("keyValue", function() {
        return {
            restrict: "E",
            require: '^ngModel',
            scope: {
                ngModel: '='
            },
            template : '<div>{{dispValue}}</div>',
            link: function(scope, elem, attrs) {
                if (scope.ngModel == undefined || scope.ngModel.length == 0) {
                    return;
                }
                if (attrs.type == 'carts') {
                    var strDisp = "";
                    scope.ngModel.forEach(function (cartObj) {
                        if (cartObj.cartId != 0 && cartObj.cartId != 1) {
                            var strValue = Carts.valueOf(cartObj.cartId);
                            if (strValue == undefined) {
                                // Carts中没有定义时直接输出cartId
                                strValue = cartObj.cartId;
                            } else {
                                strValue = strValue.name;
                            }
                            if (strDisp.length == 0) {
                                strDisp = strDisp + strValue;
                            } else {
                                strDisp = strDisp + ', ' + strValue;
                            }
                        }
                    });
                    scope.dispValue = strDisp;
                }
            }
        };
    });
})