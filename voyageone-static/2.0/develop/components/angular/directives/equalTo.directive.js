/**
 * @Description:
 * 比较两个值是否相等
 * @Date:    2017-01-10 17:35:22
 * @User:    edward
 * @Version: 2.10.0
 */
angular.module("voyageone.angular.directives").directive("equalTo", function () {
    return {
        restrict: "A",
        require: "ngModel",
        scope: {
            equalTo:"="
        },
        link: function (scope, ele, attrs, ctrl) {

            var target = attrs["equalTo"];//获取自定义指令属性键值

            if (target) {//判断键是否存在
                scope.$watch("equalTo", function () {//存在启动监听其值
                    ctrl.$validate()//每次改变手动调用验证
                });

                ctrl.$validators.equalTo = function (viewVale) {//自定义验证器内容

                    return scope.equalTo == viewVale;//是否等于passwordConfirm的值
                };
            }
        }
    }
});