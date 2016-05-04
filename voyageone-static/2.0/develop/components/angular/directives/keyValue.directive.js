(function() {
    /**
     * @Description: 根据键名显示对应的值
     * @Date:    2016/04/22
     * @User:    JiangJusheng
     * @Version: 2.0.0
     */
    angular.module("voyageone.angular.directives.keyValue", []).directive("keyValue", function() {
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
                    var strDisp = ""; // TODO -- 怎么转换键值？引入enum Carts
                    scope.ngModel.forEach(function (cartObj) {
                        if (cartObj.cartId != 0 && cartObj.cartId != 1) {
                            if (strDisp.length == 0) {
                                strDisp = strDisp + cartObj.cartId;
                            } else {
                                strDisp = strDisp + ', ' + cartObj.cartId;
                            }
                        }
                    });
                    scope.dispValue = strDisp;
                }
            }
        };
    });
})();