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
     * 已知的 valueType 包含的值
     */
    var VALUE_TYPES = {
        TEXT: 'text',
        TEXTAREA: 'textarea',
        INTEGER: 'integer',
        LONG: 'long',
        DECIMAL: 'decimal',
        DATE: 'date',
        TIME: 'time',
        URL: 'url',
        HTML: 'html'
    };

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
     * 获取随机数字
     */
    function random(length) {
        return Math.random().toString().substr(2, length || 6);
    }

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
     * 获取依赖结果
     */
    DependentRule.prototype.checked = function () {
        // TODO
    };

    /**
     * 根据依赖结果返回正则值
     */
    DependentRule.prototype.getRegex = function () {
        // TODO
    };

    /**
     * 根据依赖结果返回 maxlength 或 minlength 的 length 值
     */
    DependentRule.prototype.getLength = function () {
        // TODO
    };

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

    /**
     * 将 rules 转为对象。并转换依赖类规则
     * @returns {{
     *   requiredRule : string | Object | null,
     *   valueTypeRule : string | Object | null,
     *   minValueRule : string | Object | null,
     *   maxValueRule : string | Object | null,
     *   disableRule : string | Object | null,
     *   minInputNumRule : string | Object | null,
     *   maxInputNumRule : string | Object | null,
     *   regexRule : string | Object | null,
     *   tipRule : string | Object | null,
     *   devTipRule : string | Object | null,
     *   minLengthRule : string | Object | null,
     *   maxLengthRule : string | Object | null,
     *   maxTargetSizeRule : string | Object | null,
     *   minImageSizeRule : string | Object | null,
     *   maxImageSizeRule : string | Object | null,
     *   readOnlyRule : string | Object | null
     * }}
     */
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
            } else if (schema) {
                // 如果有需要记录的信息, 则转换依赖条件, 并保存值
                rules[rule.name] = new DependentRule(rule, schema);
            }

        });

        return rules;
    }

    /**
     * 为 maxlength 和 minlength 规则提供支持
     */
    function bindLengthRule(element, rule, name, attr) {

        if (!rule) return;

        if (rule instanceof DependentRule) {
            element.attr('ng-' + attr, 'rules.' + name + '.getLength()');
        } else {
            element.attr(attr, rule);
        }
    }

    /**
     * 为 required 和 readonly 规则提供支持
     */
    function bindBoolRule(element, rule, name, attr) {

        if (!rule) return;

        if (rule instanceof DependentRule) {
            element.attr('ng-' + attr, 'rules.' + name + '.checked()');
        } else if (rule === 'true') {
            element.attr(attr, true);
        }
    }

    /**
     * 禁用与启用规则, 只支持依赖类型, 默认其他类型都不支持
     * 因为固定的没有意义
     */
    function bindDisableRule(element, rule) {
        if (rule && rule instanceof DependentRule) {
            element.attr('ng-if', '!rules.disableRule.checked()');
        }
    }

    /**
     * tip 只是简单的显示, 默认应该不会是依赖规则。如果某天真的是了... 请修改这里
     */
    function bindTipRule(element, rule) {
        if (rule) {
            element.attr('title', rule);
        }
    }

    /**
     * 获取字段是否包含那种类似 required 的验证类的规则, tip 就不是。
     */
    function hasValidateRule(field) {

        // 额。。。这种肯定没有
        if (!field || !field.rules)
            return false;

        // 额。。。这种肯定也他喵的没有
        if (!field.rules.length)
            return false;

        var someRuleIsNotTip = field.rules.find(function (rule) {
            return rule.name.indexOf('tip') < 0;
        });

        return !!someRuleIsNotTip;
    }

    /**
     * 在 parent 指定的容器里编译 element, 并根据情况选择是否追加 ng-form
     */
    function compileAndLink($compile, $scope, parent, element, field, controllers) {

        var schemaController = controllers[0];

        var formController = controllers[1];

        var formName, formElement, voMessage, container = parent;

        if (hasValidateRule(field)) {
            // 如果有需要验证的信息, 则追加信息显示

            // 判断是否外层有 form 支持
            if (formController) {
                formName = formController.$name;
            } else if (schemaController) {
                formName = schemaController.formName;
            } else {
                // 如果完全没有 form 提供支持
                // 那么需要自己开
                formName = 'field_form_' + random();
                formElement = angular.element('<ng-form name="' + formName + '"></ng-form>');
                container.append(formElement);
                $compile(formElement)($scope);

                // 后续的元素将追加到自己开的 ngform 里
                container = form;
            }

            voMessage = angular.element('<vo-message target="' + formName + '.' + element.attr('name') + '"></vo-message>');

            container.append(element, voMessage);

            $compile(element)($scope);
            $compile(voMessage)($scope);
        } else {
            // 如果无验证的话, 就不需要信息显示了
            container.append(element);
            $compile(element)($scope);
        }
    }

    /**
     * 统一 field directive 的构建基础部分
     */
    function schemaFieldDirectiveFactory(compile) {
        return {
            restrict: 'E',
            require: ['?^schema', '?^form'],
            scope: true,
            link: function ($scope, elem, attr, controllers) {

                var disposeWatcher = null;
                var schemaController = controllers[0];

                // 如果为 field 设置了什么, 就尝试获取 field 上的内容
                if (attr.field) {
                    disposeWatcher = $scope.$watch(attr.field, function (field) {
                        if (!field)
                            return;

                        // 拿到字段后, 就可以销毁字段检查的 watcher 了
                        disposeWatcher();

                        if (schemaController) {
                            // 外面提供了 schema。那么就要等待 schema 的数据
                            disposeWatcher = $scope.$watch(function () {
                                return schemaController.schema
                            }, function (schema) {

                                $scope.field = field;
                                compile(field, schema, $scope, elem, attr, controllers);

                                disposeWatcher();
                                disposeWatcher = null;
                            });
                        } else {
                            // 如果外面没有, 意思就是不要提供依赖验证支持。那就不用在折腾了
                            $scope.field = field;
                            compile(field, null, $scope, elem, attr, controllers);

                            disposeWatcher = null;
                        }
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

                    disposeWatcher = $scope.$watch(function () {
                        return schemaController.schema
                    }, function (schema) {

                        if (!schema)
                            return;

                        var field = find(schema, function (field) {
                            return field.id === attr.fieldId;
                        });

                        if (!field)
                            elem.text('在 schema 上没有找到目标属性。');
                        else {
                            $scope.field = field;
                            compile(field, schema, $scope, elem, attr, controllers);
                        }

                        disposeWatcher();
                        disposeWatcher = null;
                    });
                    return;
                }

                // 如果两个都没设置, 或者没有外层 schema 那就....
                elem.text('请提供 field 或者 field-id 属性。');
            }
        }
    }

    angular.module('voyageone.angular.directives')
        .directive('schema', function () {
            return {
                restrict: 'E',
                transclude: true,
                template: '<ng-form name="{{$ctrl.formName}}"></ng-form>',
                scope: true,
                controllerAs: '$ctrl',
                link: function ($scope, $element, $attrs, ctrl, transclude) {
                    transclude($scope, function (clone) {
                        $element.find('ng-form').append(clone);
                    });
                },
                controller: function ($scope, $attrs) {

                    var self = this;
                    var disposeDataWatcher;

                    self.formName = 'schema_form_' + random();

                    disposeDataWatcher = $scope.$watch($attrs.data, function (data) {

                        if (!data)
                            return;

                        self.schema = data;

                        disposeDataWatcher();
                        disposeDataWatcher = null;
                    });
                }
            }
        })
        .directive('schemaInput', function ($compile) {
            return schemaFieldDirectiveFactory(function (field, schema, $scope, elem, attr, controllers) {

                var rules = doRule(field, schema);
                var valueTypeRule = rules.valueTypeRule;
                var regexRule = rules.regexRule;

                // var minValueRule = rules.minValueRule;
                // var maxValueRule = rules.maxValueRule;

                // var minInputNumRule = rules.minInputNumRule;
                // var maxInputNumRule = rules.maxInputNumRule;

                // var minImageSizeRule = rules.minImageSizeRule;
                // var maxImageSizeRule = rules.maxImageSizeRule;

                // var maxTargetSizeRule = rules.maxTargetSizeRule;

                var type = 'text';
                var innerElem;

                // 将规则保存在当前 scope 上, 模板上需要时便于绑定
                $scope.rules = rules;

                // 先为 input 检查值类型
                // 如果没有固定规则, 默认 input[type=text]
                // html 使用 jQuery 创建

                if (valueTypeRule) {
                    switch (valueTypeRule) {
                        case VALUE_TYPES.TEXT:
                            type = 'text';
                            break;
                        case VALUE_TYPES.HTML:
                        case VALUE_TYPES.TEXTAREA:
                            type = 'textarea';
                            break;
                        case VALUE_TYPES.INTEGER:
                        case VALUE_TYPES.LONG:
                            type = 'number';
                            break;
                        case VALUE_TYPES.DECIMAL:
                            type = 'number';
                            break;
                        case VALUE_TYPES.DATE:
                            type = 'date';
                            break;
                        case VALUE_TYPES.TIME:
                            type = 'time';
                            break;
                        case VALUE_TYPES.URL:
                            type = 'url';
                            break;
                    }
                }

                if (type === 'textarea') {
                    innerElem = angular.element('<textarea class="form-control">');
                } else {
                    innerElem = angular.element('<input class="form-control">').attr('type', type);
                }

                innerElem.attr('name', 'field_name_' + random());
                innerElem.attr('ng-model', 'field.value');

                bindBoolRule(innerElem, rules.requiredRule, 'requiredRule', 'required');
                bindBoolRule(innerElem, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                bindLengthRule(innerElem, rules.minLengthRule, 'minLengthRule', 'minlength');
                bindLengthRule(innerElem, rules.maxLengthRule, 'maxLengthRule', 'maxlength');

                bindDisableRule(innerElem, rules.disableRule);

                bindTipRule(innerElem, rules.tipRule);

                // 处理正则规则
                if (regexRule) {

                    if (regexRule instanceof DependentRule) {
                        // 如果是依赖类型
                        // 则如果需要, 则赋值正则, 否则为空。为空时将总是验证通过(即不验证)
                        innerElem.attr('ng-pattern', 'rules.regexRule.getRegex()');

                    } else if (regexRule !== 'yyyy-MM-dd') {
                        // 如果是日期格式验证就不需要了
                        // type=date 时 angular 会验证的
                        innerElem.attr('pattern', regexRule);
                    }
                }

                compileAndLink($compile, $scope, elem, innerElem, field, controllers);
            });
        })
        .directive('schemaSingleCheck', function ($compile) {
            return schemaFieldDirectiveFactory(function (field, schema, $scope, elem, attr, controllers) {

                var innerElem, rules = doRule(field, schema);

                $scope.rules = rules;

                innerElem = angular.element('<select class="form-control">');

                innerElem.attr('name', 'field_name_' + random());

                innerElem.attr('ng-model', 'field.value.value');

                innerElem.attr('ng-options', 'option.value as option.displayName for option in field.options');

                bindBoolRule(innerElem, rules.requiredRule, 'requiredRule', 'required');
                bindBoolRule(innerElem, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                bindDisableRule(innerElem, rules.disableRule);

                bindTipRule(innerElem, rules.tipRule);

                compileAndLink($compile, $scope, elem, innerElem, field, controllers)
            });
        })
        .directive('schemaMultiCheck', function ($compile) {

            return schemaFieldDirectiveFactory(function (field, schema, $scope, elem, attr, controllers) {
                
                var checkboxes = [], rules = doRule(field, schema);

                $scope.rules = rules;

                $scope.selected = [];

                $scope.update = function (index) {

                    var selectedValue = field.options[index].value;
                    var selectedIndex = field.value.values.indexOf(selectedValue);

                    if ($scope.selected[index]) {
                        // 选中
                        if (selectedIndex < 0)
                            field.value.values.push(selectedValue);
                    } else {
                        // 没选中
                        if (selectedIndex > -1)
                            field.value.values.splice(selectedIndex, 1);
                    }

                };

                field.options.forEach(function (option, index) {

                    var label = angular.element('<label></label>'),
                        checkbox = angular.element('<input type="checkbox">');

                    checkbox.attr('ng-model', 'selected[' + index + ']');
                    checkbox.attr('ng-change', 'update(' + index + ')');

                    label.append(checkbox, '&nbsp;', option.displayName);

                    checkboxes.push(label);
                });
                
                compileAndLink($compile, $scope, elem, checkboxes, field, controllers);
                
            });
            
        })
        .directive('schemaComplex', function () {
        })
        .directive('schemaMultiComplex', function () {
        });
})();