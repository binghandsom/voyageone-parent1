(function () {

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
        NOT_CONTAINS: {key: 'not contains'},
        NOT_EQUALS: {key: '!='},
        EQUALS: {key: '=='}
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

    angular.module('voyageone.angular.directives')
        .directive('schema', function () {
            return {
                restrict: 'E',
                scope: {
                    data: '='
                },
                controller: function($scope) {
                    var self = this;
                    var disposeDataWatcher = $scope.$watch('data', function(data) {

                        if (!data)
                            return;

                        self.schema = data;

                        disposeDataWatcher();
                        disposeDataWatcher = null;
                    });
                }
            }
        })
        .directive('schemaInput', function () {
            return {
                restrict: 'E',
                require: '?^schema',
                scope: {
                    field: '=',
                    fieldId: '@'
                },
                link: function($scope, elem, attr, schemaController) {

                    var disposeWatcher = null;
                    
                    // 如果为 field 设置了什么, 就尝试获取 field 上的内容
                    if (attr.field) {
                        disposeWatcher = $scope.$watch('field', function(field){
                            if (!field)
                                return;
                            
                            console.log(field); // TODO
                            
                            disposeWatcher();
                            disposeWatcher = null;
                        });
                        return;
                    }
                    
                    // 否则就尝试根据 fieldId 并配合外层的 schema 来获取 field。
                    if (attr.fieldId) {
                        
                        // 但是没有外层 schema 的话。就只能...
                        if (!schemaController) {
                            elem.text('如果设置了 field-id 就必须在外层提供 schema。但好像并没有。');
                            return;
                        }
                        
                        disposeWatcher = $scope.$watch(function(){
                            return schemaController.schema
                        }, function(schema) {
                            
                            if (!schema)
                                return;
                            
                            var field = find(schema, function(field) {
                                return field.id === $scope.fieldId;
                            });
                            
                            if (!field)
                                elem.text('在 schema 上没有找到目标属性。');
                            
                            console.log(field); // TODO
                            
                            disposeWatcher();
                            disposeWatcher = null;
                        });
                        return;
                    }
                    
                    // 如果两个都没设置, 或者没有外层 schema 那就....
                    elem.text('请提供 field 或者 field-id 属性。');
                }
            }
        });
})();