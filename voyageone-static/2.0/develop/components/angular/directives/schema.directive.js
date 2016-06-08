define(function (require) {
    /*
     * !! 因为需要异步依赖枚举, 所以需要使用 require 在必要时引入
     */

    var FIELD_TYPES;

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
     * 获取字段是否包含那种类似 required 的验证类的规则, tip 就不是。
     */
    function hasValidateRule(field) {

        // 额。。。这种肯定没有
        if (!field || !field.rules)
            return false;

        // 额。。。这种肯定也他喵的没有
        if (!field.rules.length)
            return false;

        return field.rules.some(function (rule) {
            return rule.name.indexOf('tip') < 0;
        });
    }

    /**
     * 根据值类型规则返回相应的 input type
     */
    function getInputType(valueTypeRule) {

        var type = 'text';

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

        return type;
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

    angular.module('voyageone.angular.directives')
        .directive('schema', function () {
            return {
                restrict: 'E',
                transclude: true,
                scope: true,
                controllerAs: '$ctrl',
                link: function ($scope, $element, $attrs, ctrl, transclude) {
                    // 自己处理 transclude 来保证内部的 vo-message 可以访问到上层 scope 的 formController
                    transclude($scope, function (clone) {
                        $element.append(clone);
                    });
                },
                controller: function SchemaController($scope, $attrs) {

                    var self = this;
                    var disposeDataWatcher;

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
        .directive('schemaField', function ($compile) {
            return {
                restrict: 'E',
                require: ['?^schema', '?^form'],
                scope: true,
                link: function ($scope, elem, attr, controllers) {

                    // schema-field
                    // ---
                    // 如果 SchemaController 存在, 即提供依赖类型的规则验证。但仅支持 disableRule, 如果想开启类似必填这些的验证, 需要在外层提供 form
                    // ---
                    // 如果 SchemaController 不存在, 只存在 FormController。那么只提供类似必填类的并且非依赖的验证, 所有依赖类型的都将被忽略。同时这里决定是否有 vo-message 的存在
                    // ---
                    // 如果啥都没有, 就不开验证, 后续的 rule 就都不用解析了。

                    var disposeWatcher = null;
                    var schemaController = controllers[0];
                    var formController = controllers[1];
                    var config;

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

                                    compile(field, schema);

                                    disposeWatcher();
                                    disposeWatcher = null;
                                });
                            } else {
                                // 如果外面没有, 意思就是不要提供依赖验证支持。那就不用在折腾了

                                compile(field, null);

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
                                compile(field, schema);
                            }

                            disposeWatcher();
                            disposeWatcher = null;
                        });
                        return;
                    }

                    // 如果两个都没设置, 或者没有外层 schema 那就....
                    elem.text('请提供 field 或者 field-id 属性。');

                    function compile(field, schema) {

                        var innerElement, fieldElementName = 'field_' + random();

                        var rules = $scope.$rules = doRule(field, schema);

                        $scope.field = field;

                        if (!FIELD_TYPES) FIELD_TYPES = require('modules/cms/enums/FieldTypes');

                        config = {
                            // 是否显示名称
                            showName: !!schemaController,
                            // 解析依赖规则
                            doDep: !!schemaController,
                            // 解析验证规则
                            doValid: !!formController && hasValidateRule(field)
                        };

                        // 创建输入元素
                        // 根据需要处理规则
                        elem.append(innerElement = createElement(field, fieldElementName, rules));

                        // 根据需要创建 vo-message
                        if (config.doValid) {
                            var formName = formController.$name;
                            var voMessage = angular.element('<vo-message target="' + formName + '.' + fieldElementName + '"></vo-message>');
                            elem.append(voMessage);
                        }

                        // 最终编译
                        $compile(elem.children())($scope);
                    }

                    function createElement(field, name, rules) {

                        var innerElement;

                        switch (field.type) {
                            case FIELD_TYPES.input:

                                var type = getInputType(rules.valueTypeRule);
                                var regexRule = rules.regexRule;

                                if (type === 'textarea') {
                                    innerElement = angular.element('<textarea class="form-control">');
                                } else {
                                    innerElement = angular.element('<input class="form-control">').attr('type', type);
                                }

                                innerElement.attr('name', name);

                                innerElement.attr('ng-model', 'field.$value');

                                bindBoolRule(innerElement, rules.requiredRule, 'requiredRule', 'required');
                                bindBoolRule(innerElement, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                                bindLengthRule(innerElement, rules.minLengthRule, 'minLengthRule', 'minlength');
                                bindLengthRule(innerElement, rules.maxLengthRule, 'maxLengthRule', 'maxlength');

                                bindTipRule(innerElement, rules.tipRule);

                                // 处理正则规则
                                if (regexRule) {

                                    if (regexRule instanceof DependentRule) {
                                        // 如果是依赖类型
                                        // 则如果需要, 则赋值正则, 否则为空。为空时将总是验证通过(即不验证)
                                        innerElement.attr('ng-pattern', 'rules.regexRule.getRegex()');

                                    } else if (regexRule !== 'yyyy-MM-dd') {
                                        // 如果是日期格式验证就不需要了
                                        // type=date 时 angular 会验证的
                                        innerElement.attr('pattern', regexRule);
                                    }
                                }

                                break;
                            case FIELD_TYPES.singleCheck:

                                innerElement = angular.element('<select class="form-control">');

                                innerElement.attr('ng-options', 'option.value as option.displayName for option in field.options');

                                innerElement.attr('name', name);

                                innerElement.attr('ng-model', 'field.$value');

                                bindBoolRule(innerElement, rules.requiredRule, 'requiredRule', 'required');
                                bindBoolRule(innerElement, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                                bindTipRule(innerElement, rules.tipRule);

                                break;
                            case FIELD_TYPES.multiCheck:

                                var requiredRule = rules.requiredRule;

                                innerElement = [];

                                // 创建用于记录每个多选框选中状态的对象
                                $scope.selected = [];

                                // 通过事件触发 update 来操作 field 的 values 数组
                                $scope.update = function (index) {

                                    var selectedValue = field.options[index].value;
                                    var selectedIndex = field.$value.indexOf(selectedValue);

                                    if ($scope.selected[index]) {
                                        // 选中
                                        if (selectedIndex < 0)
                                            field.$value.push(selectedValue);
                                    } else {
                                        // 没选中
                                        if (selectedIndex > -1)
                                            field.$value.splice(selectedIndex, 1);
                                    }

                                };

                                field.$value = [];

                                field.options.forEach(function (option, index) {

                                    var label = angular.element('<label></label>'),
                                        checkbox = angular.element('<input type="checkbox">');

                                    checkbox.attr('ng-model', 'selected[' + index + ']');

                                    checkbox.attr('name', name);

                                    checkbox.attr('ng-change', 'update(' + index + ')');

                                    // checkbox 的必填比较特殊
                                    if (requiredRule) {
                                        if (requiredRule instanceof DependentRule) {
                                            checkbox.attr('ng-required', 'rules.requiredRule.checked() && !field.$value.length');
                                        } else {
                                            checkbox.attr('ng-required', '!field.$value.length');
                                        }
                                    }

                                    bindBoolRule(checkbox, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                                    bindTipRule(checkbox, rules.tipRule);

                                    label.append(checkbox, '&nbsp;', option.displayName);

                                    innerElement.push(label);
                                });

                                break;
                            case FIELD_TYPES.complex:
                                break;
                            case FIELD_TYPES.multiComplex:
                                break;
                            default:
                                console.error('不支持其他类型');
                                return null;
                        }

                        return innerElement;
                    }
                }
            };
        })

        /********************************************************************************************************************************************/

        .directive('schemaMultiCheck', function ($compile) {

            return schemaFieldDirectiveFactory(function (field, schema, $scope, elem, attr, controllers) {

                var checkboxes = [], elementName = 'field_name_' + random(), rules = doRule(field, schema);

                $scope.rules = rules;

                // 创建用于记录每个多选框选中状态的对象
                $scope.selected = [];

                // 通过事件触发 update 来操作 field 的 values 数组
                $scope.update = function (index) {

                    var selectedValue = field.options[index].value;
                    var selectedIndex = field.$value.indexOf(selectedValue);

                    if ($scope.selected[index]) {
                        // 选中
                        if (selectedIndex < 0)
                            field.$value.push(selectedValue);
                    } else {
                        // 没选中
                        if (selectedIndex > -1)
                            field.$value.splice(selectedIndex, 1);
                    }

                };

                field.$value = [];

                field.options.forEach(function (option, index) {

                    var label = angular.element('<label></label>'),
                        checkbox = angular.element('<input type="checkbox">');

                    var requiredRule = rules.requiredRule;

                    checkbox.attr('name', elementName);
                    checkbox.attr('ng-model', 'selected[' + index + ']');
                    checkbox.attr('ng-change', 'update(' + index + ')');



                    label.append(checkbox, '&nbsp;', option.displayName);

                    checkboxes.push(label);
                });

                compileAndLink($compile, $scope, elem, checkboxes, elementName, field, controllers);

            });

        });

    /********************************************************************************************************************************************/
});