(function () {

    'use strict';

    /**
     * 错误类型, 直接通过 form 验证所得的 $error 获取相应的 key, 并通过翻译获取信息结果
     */
    var errorTypes = {
        email: 'INVALID_EMAIL',
        url: 'INVALID_URL',
        date: 'INVALID_DATE',
        datetimelocal: 'INVALID_DATETIMELOCAL',
        color: 'INVALID_COLOR',
        range: 'INVALID_RANGE',
        month: 'INVALID_MONTH',
        time: 'INVALID_TIME',
        week: 'INVALID_WEEK',
        number: 'INVALID_NUMBER',
        required: 'INVALID_REQUIRED',
        maxlength: 'INVALID_MAXLENGTH',
        minlength: 'INVALID_MINLENGTH',
        max: 'INVALID_MAX',
        min: 'INVALID_MIN',
        pattern: 'INVALID_PATTERN'
    };

    /**
     * @Description:
     * 对 Angular 的 Form 验证提供统一的信息输出支持。
     *
     * @Date:    2016-05-18 16:22:46
     * @User:    Jonas
     * @Version: 1.0.0
     */
    angular.module("voyageone.angular.directives.message", [])

        .directive('voMessage', function ($translate) {
            return {
                restrict: "E",
                require: '^^form',
                template: '{{$message}}',
                scope: {
                    'target': '='
                },
                link: function (scope, elem, attrs, formController) {

                    var formName = formController.$name;

                    var fieldName = scope.target.$name;

                    // 对指定 form 下字段的错误信息进行监视
                    // 如果有变动, 就显示第一个错误的提示信息
                    scope.$watch(

                        function () {
                            return scope.target.$error;
                        },

                        function ($error) {

                            // 取所有错误的 angular 错误名称, 如 required
                            var errorKeys = Object.keys($error);

                            // 取第一个
                            var error = errorKeys[0];

                            // 如果没有错误就不用继续处理错误信息了
                            if (!error) {
                                hide();
                                return;
                            }

                            // 取错误的翻译 Key, 如 required -> INVALID_REQUIRED, 参加上面的 var errorTypes
                            $translate(errorTypes[error], {field: fieldName, form: formName}).then(function (message) {

                                // 翻译成功就输出翻译后的信息
                                scope.$message = message;

                            }, function (key) {

                                // 找不到就直接输出翻译 Key
                                scope.$message = key;

                            });

                        }, true);
                }
            }
        });
})();