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
    minbytelength: 'INVALID_MINLENGTH',
    maxbytelength: 'INVALID_MAXLENGTH',
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
angular.module("voyageone.angular.directives")

    .directive('voMessage', function ($translate) {
        return {
            restrict: "E",
            template: '{{$message}}',
            require: '^^form',
            scope: {
                'target': '='
            },
            link: function (scope, elem, attrs, formController) {

                function show(message) {
                    scope.$message = message;
                    elem.fadeIn();
                }

                function hide() {
                    elem.fadeOut();
                }

                var formName;

                // 初始化时保持隐藏
                elem.hide();

                formName = formController.$name;

                // 对指定 form 下字段的错误信息进行监视
                // 如果有变动, 就显示第一个错误的提示信息
                scope.$watch('target.$error',

                    function ($error) {

                        if (!$error) return;

                        // 取所有错误的 angular 错误名称, 如 required
                        var errorKeys = Object.keys($error);

                        var elementName = scope.target.$name;

                        // 这一步可能获取的并不准确
                        // 因为元素的 name 有可能重复
                        var targetElement = $('[name="' + formName + '"] [name="' + elementName + '"]');

                        // 如果有友好名称的话, 就用友好的
                        var translateParam = {field: targetElement.attr('title') || elementName};

                        // 取第一个
                        var error = errorKeys[0];

                        // 如果没有错误就不用继续处理错误信息了
                        if (!error) {
                            hide();
                            return;
                        }

                        // 如果是长度类的检查, 那么为翻译提供长度参数
                        if (['maxlength', 'minlength', 'maxbytelength', 'minbytelength', 'max', 'min', 'pattern'].indexOf(error) > -1) {
                            translateParam.value = targetElement.attr(error);
                        }

                        // 取错误的翻译 Key, 如 required -> INVALID_REQUIRED, 参加上面的 var errorTypes
                        $translate(errorTypes[error], translateParam).then(show, show);

                    }, true);

            }
        }
    });
