/**
 * @Description: 根据键名显示对应的值
 * @Date:    2016/04/22
 * @User:    JiangJusheng
 * @Version: 2.0.0
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/enums/FeedStatus'
], function (cms, Carts, FeedStatus) {
    'use strict';
    return cms.directive("keyValue", function($translate) {
        return {
            restrict: "E",
            require: '^ngModel',
            scope: {
                ngModel: '='
            },
            template : '<div>{{dispValue}}</div>',
            link: function(scope, elem, attrs) {

                scope.$watch('scope.ngModel',function () {
                    getValue();
                });

                function getValue(){
                    if (scope.ngModel == undefined) {
                        return;
                    }
                    if (attrs.type == 'carts') {
                        if (scope.ngModel.length == 0) {
                            return;
                        }
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
                    } else if (attrs.type == 'feedStatus') {
                        var strDisp = FeedStatus.valueOf(scope.ngModel).name;
                        scope.dispValue = $translate.instant(strDisp);
                    }
                }


            }
        };
    });
})