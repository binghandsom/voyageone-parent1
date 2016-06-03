(function() {

    /*
     * 已知的 rule 有如下:
     *   requiredRule
     *   valueTypeRule
     *   minValueRule
     *   maxValueRule
     *   disableRule
     *   minInputNumRule
     *   maxInputNumRule
     *   regexRule
     *   tipRule
     *   devTipRule
     *   minLengthRule
     *   maxLengthRule
     *   maxTargetSizeRule
     *   minImageSizeRule
     *   maxImageSizeRule
     *   readOnlyRule
     */

    /**
     * 条件判断的操作符号。
     * 使用对象(引用类型)作为值, 用来方便后续判断。
     */
    var SYMBOLS = {
        NOT_CONTAINS: { key: 'not contains' },
        NOT_EQUALS: { key: '!=' },
        EQUALS: { key: '==' }
    };

    // 使用 key 再次构建, 便于查找
    SYMBOLS['not contains'] = SYMBOLS.NOT_CONTAINS;
    SYMBOLS['!='] = SYMBOLS.NOT_EQUALS;
    SYMBOLS['=='] = SYMBOLS.EQUALS;

    /**
     * 包装第三方的 find 方法。
     */
    function find(obj, predicate) {
        // 如果后续要更换, 便于更换。
        // 如果需要自己实现, 只要修改这里即可。
        return _.find(obj, predicate);
    }

    /**
     * 获取字段的正确 id。
     * 由于后端的问题。schema 的 field id 中的 '.' 会被转换成 '->'。
     * 所以查找时需要进行转换查找。
     */
    function getFieldId(field) {
        // 使用特殊的 $schema 来记录只有 schema directive 使用的信息
        if (!field.$schema)
            field.$schema = {
                id: field.id.replace(/->/g, '.')
            };
        return field.$schema.id
    }

    /**
     * 在完整的 schema 结构里找目标字段。schema 可以是数组, 也可以是对象(map)
     */
    function searchField(fieldId, schema) {

        // 由于后端的问题。schema 的 field id 中的 '.' 会被转换成 '->'
        // 所以查找时需要进行转换查找

        var result = null;

        // 使用 find 方法可以通过返回值打断。
        // js 原生和 angular, underscore 提供的 each 都不支持打断。
        find(schema, function (field) {

            if (getFieldId(field) === fieldId) {
                result = field;
                return true;
            }

            if (field.fields && field.fields.length) {
                result = searchField(fieldId, field.fields);
                if (result)
                    return true;
            }

            return false;
        });

        return result;
    }

    /**
     * 依赖型规则。
     * 用于记录依赖的相关信息。便于后续计算。
     * 使用明确的类型(class), 便于后续判断(instanceOf)。
     */
    function DependentRule(rule, schema) {

        this.dependExpressList = getDependExpressList(rule).map(function (dependExpress) {

            var field = searchField(dependExpress.fieldId, schema);

            var symbol = SYMBOLS[dependExpress.symbol];

            if (!symbol)
                console.error('没有找到可用符号: ' + dependExpress.symbol);

            return {
                field: field,
                value: dependExpress.value,
                symbol: symbol
            }
        });

        this.value = rule.value;
        this.origin = rule;
    }

    /**
     * 获取规则的依赖条件
     */
    function getDependExpressList(rule) {
        var depends = rule.dependGroup;
        return (depends ? depends.dependExpressList : null);
    }

    /**
     * 规则是否包含依赖条件
     */
    function hasDepend(rule) {
        var dependExpressList = getDependExpressList(rule);
        return !!dependExpressList && !!dependExpressList.length;
    }

    function doRule(field, schema) {

        // 没啥可用的信息就算了
        if (!field || !field.rules)
            return;

        // 没有规则好处理, 果断算了
        if (!field.rules.length)
            return;

        // 规则的简单结果
        var rules = {};

        field.rules.forEach(function (rule) {

            if (!hasDepend(rule)) {
                // 如果不需要监视, 则就是固定值。
                // 就不需要怎么处理, 记下来下一个即可
                rules[rule.name] = rule.value;
            } else {
                // 如果有需要记录的信息, 则转换依赖条件, 并保存值
                rules[rule.name] = new DependentRule(rule, schema);
            }

        });

        return rules;
    }




    var schemaInput = {};


    angular.module('voyageone.angular.directives').directive('schemaInput', function ($compile) {
        return {
            restrict: 'E',
            scope: {
                field: '='
            },
            link: function (scope, elem, attrs) {
                // 页面初始化时, directive 接收到的 field 可能没有值
                // 因为 ajax 还没有返回
                // 所以需要 watch 监控
                // 当拿到之后, 就不需要了。所以保留结果等待销毁
                var disposeWatcher = scope.$watch('field', function (field) {
                    if (!field)
                        return;

                    // 拿到 field 之后, 默认就不再变更了。所以这个 watcher 就可以再见了
                    disposeWatcher();
                    disposeWatcher = null;

                    // 默认的, 根据 field 的 type 属性决定使用什么 schema 标签
                    // 所以当你使用 schema-input 时, 我们默认认为 field 的 type 就是 INPUT
                    // 这一点在未来可以进行报错处理

                    // 现在要根据 rules 的 valueTypeRule 来决定使用什么类型的控件
                    // 如果无法决定, 则默认使用 text
                    var valueType = null;
                    var inputType = null;

                    if (field.rules) {

                        var valueTypeRule = field.rules.find(rule => rule.name === 'valueTypeRule');

                        if (valueTypeRule)
                            valueType = valueTypeRule.value;
                    }

                    if (!valueType)
                        valueType = 'text';

                    switch (valueType) {
                        case 'text':
                        case 'html':
                            inputType = 'text';
                            break;
                        case 'textarea':
                            inputType = 'textarea';
                            break;
                        case 'integer':
                        case 'long':
                        case 'decimal':
                            inputType = 'number';
                            break;
                        case 'date':
                            inputType = 'date';
                            break;
                        case 'time':
                            inputType = 'time';
                            break;
                        case 'url':
                            inputType = 'url';
                            break;
                    }

                    var input = $('<input>').attr({
                        'ng-model': 'field.value',
                        'class': 'form-control',
                        'type': inputType
                    });

                    elem.append($compile(input[0])(scope));
                });
            }
        }
    });

    /*
     .component('schemaInput', {
     template: '<input ng-model="$ctrl.field.value" class="form-control" ng-type="$ctrl.type" ng-required="" ng-pattern="" ng-maxlength="" ng-minlength="" ng-maxvalue="" ng-minvalue="">',
     bindings: {

     },
     controller: class SchemaInputController {
     private field;

     get type() {
     // 尝试查找值类型规则
     var valueTypeRule = this.field.rules.find(rule => rule.name === 'valueTypeRule');

     // 如果找不到, 默认就给 text。
     if (!valueTypeRule)
     return 'text';

     // 如果有的话
     switch (valueTypeRule.value) {

     }

     // 不能匹配的默认使用 text
     return 'text';
     }
     }
     }
     */
})();