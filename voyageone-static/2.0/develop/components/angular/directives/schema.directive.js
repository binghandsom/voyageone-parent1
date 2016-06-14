define(function (require) {
    /*
     * !! 因为需要异步依赖枚举, 所以需要使用 require 在必要时引入
     */

    /**
     * 以下代码包含下面这些自定义标签:
     *  schema
     *  schema-field
     *  schema-field-name
     *  schema-complex-name
     *  schema-complex-container
     *  schema-disable-container
     *  schema-field-tip
     *
     * 后续如有增加新的自定义标签, 请在这里追加。方便控制外观自定义。
     */

    var FIELD_TYPES;

    /*
     * 已知的 rule 有如下:
     *   requiredRule
     *   readOnlyRule
     *   disableRule
     *   regexRule
     *   valueTypeRule
     *   tipRule
     *   devTipRule
     *   minValueRule
     *   maxValueRule
     *   minInputNumRule
     *   maxInputNumRule
     *   minLengthRule
     *   maxLengthRule
     *   maxTargetSizeRule
     *   minImageSizeRule
     *   maxImageSizeRule
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
     * 解耦包装 find 方法
     */
    function find(obj, predicate) {
        return _.find(obj, predicate);
    }

    /**
     * 解耦包装 some 方法
     */
    function any(obj, predicate) {
        return _.some(obj, predicate);
    }

    /**
     * 解耦包装 every 方法
     */
    function all(obj, predicate) {
        return _.every(obj, predicate);
    }

    /**
     * 解耦包装 each 方法
     */
    function each(obj, iteratee) {
        return _.each(obj, iteratee);
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

        // 规则的简单结果
        var rules = {};

        // 没啥可用的信息就算了
        if (!field || !field.rules)
            return rules;

        // 没有规则好处理, 果断算了
        if (!field.rules.length)
            return rules;

        each(field.rules, function (rule) {

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

        return any(field.rules, function (rule) {

            // 以下规则都不需要追加 vo-message

            return (rule.name === 'valueTypeRule'
                && rule.value !== VALUE_TYPES.TEXT
                && rule.value !== VALUE_TYPES.TEXTAREA
                && rule.value !== VALUE_TYPES.HTML)
                || (rule.name !== 'tipRule'
                && rule.name !== 'devTipRule'
                && rule.name !== 'disableRule');
        });
    }

    /**
     * 根据值类型规则返回相应的 input type
     */
    function getInputType(valueTypeRule) {

        var type = 'text';

        if (!valueTypeRule)
            return type;

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

        return type;
    }

    /**
     * 根据 valueTypeRule 转换值类型
     */
    function getInputValue(value, valueTypeRule) {

        if (!valueTypeRule)
            return value;

        switch (valueTypeRule) {
            case VALUE_TYPES.TEXT:
            case VALUE_TYPES.HTML:
            case VALUE_TYPES.TEXTAREA:
            case VALUE_TYPES.URL:
                return value;
            case VALUE_TYPES.INTEGER:
            case VALUE_TYPES.LONG:
                return parseInt(value);
            case VALUE_TYPES.DECIMAL:
                return parseFloat(value);
            case VALUE_TYPES.DATE:
                return new Date(value);
            case VALUE_TYPES.TIME:
                return new Date(value);
        }

        // default
        return value;
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
     * tip 只是简单的显示, 默认应该不会是依赖规则。如果某天真的是了... 请修改这里
     */
    function bindTipRule(container, rule) {
        if (!rule) return;

        var content = rule;

        var contentContainer = angular.element('<schema-field-tip>');

        contentContainer.text(content);

        container.append(contentContainer);
    }

    function getFieldKeySet(fields) {
        return fields.map(function (f) {
            return f.id;
        });
    }

    function getFieldMap(fields) {
        var map = {};
        each(fields, function (f) {
            map[f.id] = angular.copy(f);
        });
        return map;
    }

    function createComplexValue(fields) {
        return {
            fieldKeySet: getFieldKeySet(fields),
            fieldMap: getFieldMap(fields)
        };
    }

    function resetValue(valueObj, fieldObj) {

        ['value', 'values', 'complexValue', 'complexValues'].some(function (key) {
            if (valueObj[key] && !fieldObj[key]) {
                fieldObj[key] = valueObj[key];
                return true;
            }
            return false;
        });
    }

    /**
     * @class 依赖型规则
     * 用于记录依赖的相关信息。便于后续计算。
     * 使用明确的类型(class), 便于后续判断(instanceOf)。
     */
    function DependentRule(rule, schema) {

        this.dependExpressList = getDependExpressList(rule).map(function (dependExpress) {

            var field = searchField(dependExpress.fieldId, schema);

            if (!field) {
                console.warn('cant find field for dep rule. -> ' + dependExpress.fieldId);
            }

            var symbol = SYMBOLS[dependExpress.symbol];

            if (!symbol)
                console.warn('没有找到可用符号: ' + dependExpress.symbol);

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

        var self = this;
        var dependExpressList = self.dependExpressList;

        return all(dependExpressList, function (express) {

            // 每一个表达式的计算, 都只支持简单处理
            // 如果后续需要, 请继续扩展

            var field = express.field;

            if (!field)
                return false;

            var value = field.$value;

            switch (express.symbol) {
                case SYMBOLS.EQUALS:
                    return value == express.value;
                case SYMBOLS.NOT_EQUALS:
                    return value != express.value;
                case SYMBOLS.NOT_CONTAINS:
                    return !!value && value.indexOf(express.value) < 0;
                default:
                    return false;
            }
        });
    };

    /**
     * 根据依赖结果返回正则值
     */
    DependentRule.prototype.getRegex = function () {
        return this.checked() ? this.value : null;
    };

    /**
     * 根据依赖结果返回 maxlength 或 minlength 的 length 值
     */
    DependentRule.prototype.getLength = function () {
        return this.checked() ? this.value : null;
    };

    angular.module('voyageone.angular.directives')

        .directive('schema', function ($compile) {

            function SchemaController($scope, $attrs, $q) {

                var controller = this;

                controller.$attrs = $attrs;
                controller.$scope = $scope;
                controller.$q = $q;
            }

            SchemaController.prototype.$watchSchema = function () {

                var controller = this,
                    $attrs = controller.$attrs,
                    $scope = controller.$scope,
                    $q = controller.$q;

                var deferred = $q.defer();

                var disposeDataWatcher = $scope.$watch($attrs.data, function (data) {

                    if (!data)
                        return;

                    deferred.resolve(controller.schema = data);

                    disposeDataWatcher();
                    disposeDataWatcher = null;
                });

                return deferred.promise;
            };

            SchemaController.prototype.$render = function ($element) {

                var controller = this,
                    $scope = controller.$scope;
                schema = controller.schema;

                each(schema, function (field) {

                    var fieldElement = angular.element('<schema-field>');

                    fieldElement.attr('field-id', field.id);

                    $element.append(fieldElement);
                });

                $compile($element.contents())($scope);
            };

            return {
                restrict: 'E',
                transclude: true,
                scope: true,
                controllerAs: '$ctrl',
                link: function ($scope, $element, $attrs, ctrl, transclude) {

                    var controller = $scope.$ctrl;

                    // 自己处理 transclude 来保证内部的 vo-message 可以访问到上层 scope 的 formController
                    // 处理之前, 先等待 schema 数据
                    controller.$watchSchema().then(function () {

                        // 拿到 schema 之后
                        // 如果 schema directive 内部有 field 子元素
                        // 就直接用子元素
                        // 否则需要展开所有字段

                        transclude($scope, function (clone) {

                            var fieldElements = clone.filter('schema-field');

                            var hasFieldElements = !!fieldElements.length;

                            if (!hasFieldElements) {
                                // 如果元素内为空, 或内部没有元素, 就展开所有 field
                                controller.$render($element);
                                return;
                            }

                            $element.append(fieldElements);
                        });

                    });

                },
                controller: SchemaController
            }
        })

        .directive('schemaField', function ($compile, $q) {

            function SchemaFieldController($scope, $element) {
                this.$scope = $scope;
                this.$element = $element;
            }

            SchemaFieldController.prototype.$render = function (field, schema) {

                /**
                 * 元素创建过程
                 */
                function createElement(field, name, rules) {

                    var innerElement;

                    switch (field.type) {
                        case FIELD_TYPES.input:

                            var regexRule = rules.regexRule;
                            var valueTypeRule = rules.valueTypeRule;
                            var type = getInputType(valueTypeRule);

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

                            if (!field.$value)
                                field.$value = getInputValue(field.value, valueTypeRule);

                            break;
                        case FIELD_TYPES.singleCheck:

                            innerElement = angular.element('<select class="form-control">');

                            innerElement.attr('ng-options', 'option.value as option.displayName for option in field.options');

                            innerElement.attr('name', name);

                            innerElement.attr('ng-model', 'field.$value');

                            bindBoolRule(innerElement, rules.requiredRule, 'requiredRule', 'required');
                            bindBoolRule(innerElement, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                            if (!field.$value && field.value)
                                field.$value = field.value.value;

                            break;
                        case FIELD_TYPES.multiCheck:

                            var selected, $value;
                            var requiredRule = rules.requiredRule;

                            innerElement = [];

                            // 创建用于记录每个多选框选中状态的对象
                            selected = $scope.selected = [];

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

                            if (!field.$value) {

                                if (field.values && field.values.length) {
                                    field.$value = field.values.map(function (val) {
                                        return val.value;
                                    });
                                } else {
                                    field.$value = [];
                                }
                            }

                            $value = field.$value;

                            each(field.options, function (option, index) {

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

                                selected[index] = !($value.indexOf(option.value) < 0);

                                label.append(checkbox, '&nbsp;', option.displayName);

                                innerElement.push(label);
                            });

                            break;
                        case FIELD_TYPES.complex:

                            // complex 字段, 每个 field 的值都是存在其 value 上的。
                            // 所以直接使用 fields 属性即可。
                            // 还原 complexValue 中的原始值到 field 上

                            var fieldValueMap,
                                complexValue = field.complexValue;

                            if (complexValue) {
                                fieldValueMap = complexValue.fieldMap;

                                if (fieldValueMap) {
                                    each(field.fields, function (childField) {

                                        var valueObj = fieldValueMap[childField.id];

                                        if (!valueObj) return;

                                        resetValue(valueObj, childField);
                                    });
                                }
                            }

                            $scope.$fields = field.fields;

                            innerElement = angular.element('<schema-complex-container fields="$fields">');

                            break;
                        case FIELD_TYPES.multiComplex:

                            // multiComplex 字段, 其值不同于 complex 字段, 是存在于 complexValues 中。
                            // 存在 complexValues 中, 每一组的 fieldMap 的 field 的 value 中。
                            // 所以需要根据每个 complexValues 来创建 container

                            var complexValues = field.complexValues || (field.complexValues = []);

                            if (!complexValues.length) {
                                // 如果获取的值里没有内容, 就创建一套默认
                                complexValues.push(createComplexValue(field.fields));
                            } else {
                                // 否则就为 complexValues 里的 field 补全属性
                                // 如果有遗漏 field 就补全 field
                                each(complexValues, function (complexValue) {
                                    // 这里没有使用 angular.copy 完整的 field 来覆盖 complexValues 内的 field。
                                    // 是为了减少可能存在的影响。
                                    // 只选择把后续需要的属性进行了赋值(引用)
                                    var fieldKeySet = complexValue.fieldKeySet || (complexValue.fieldKeySet = []);
                                    var fieldMap = complexValue.fieldMap || (complexValue.fieldMap = {});

                                    each(field.fields, function (field) {
                                        // 如果 keySet 里没有这个字段的 key 就补上
                                        if (fieldKeySet.indexOf(field.id) < 0)
                                            fieldKeySet.push(field.id);

                                        var mapItem = fieldMap[field.id];

                                        if (!mapItem) {
                                            // 如果这个字段也没在 map 里, 就新创建一个
                                            fieldMap[field.id] = angular.copy(field);
                                        } else {
                                            // 如果已经存在, 只要补全属性就可以了
                                            mapItem.rules = field.rules;
                                            mapItem.name = field.name;
                                        }
                                    });
                                });
                            }

                            $scope.$complexValues = complexValues;

                            innerElement = angular.element('<schema-complex-container multi="true" fields="complexValue.fieldMap">');

                            innerElement.attr('ng-repeat', 'complexValue in $complexValues');

                            // multiComplex 字段, 有多个 complexValue
                            // 所以需要创建工具栏和按钮, 来新建 complexValue
                            $scope.$newComplexValue = function () {
                                complexValues.push(createComplexValue(field.fields));
                            };

                            container.append('<button ng-click="$newComplexValue()">新增</button>');

                            break;
                        default:
                            console.error('不支持其他类型');
                            return null;
                    }

                    return innerElement;
                }

                /**
                 * 名称显示元素创建过程
                 */
                function createNameElement(field, name) {

                    var innerElement;

                    switch (field.type) {
                        case FIELD_TYPES.input:
                        case FIELD_TYPES.singleCheck:
                        case FIELD_TYPES.multiCheck:
                            innerElement = angular.element('<schema-field-name>');
                            break;
                        case FIELD_TYPES.complex:
                        case FIELD_TYPES.multiComplex:
                            innerElement = angular.element('<schema-complex-name>');
                            break;
                        default:
                            console.error('不支持其他类型');
                            return null;
                    }

                    innerElement.attr('for', name);
                    innerElement.text(field.name || field.id);

                    return innerElement;
                }

                var controller = this,
                    $element = controller.$element,
                    $scope = controller.$scope,
                    formController = controller.formController,
                    showName = controller.showName;

                var container = $element,
                    fieldElementName = 'field_' + random(),
                    hasValidate = !!formController && hasValidateRule(field);

                var rules = $scope.$rules = doRule(field, schema),
                    disableRule = rules.disableRule;

                var innerElement, nameElement, isSimple;

                // 放到 scope 上, 供画面绑定使用
                // 创建元素时, ngModel 会直接指向到 field.$value
                $scope.field = field;

                if (!FIELD_TYPES) FIELD_TYPES = require('modules/cms/enums/FieldTypes');

                isSimple = (field.type != FIELD_TYPES.complex && field.type != FIELD_TYPES.multiComplex);

                if (disableRule && disableRule instanceof DependentRule) {

                    var ngIfContainer = angular.element('<div class="schema-disable-container">');

                    ngIfContainer.attr('ng-if', '!$rules.disableRule.checked()');

                    container.append(ngIfContainer);

                    container = ngIfContainer;
                }

                if (showName) {
                    // 如果需要显示名称
                    // 就创建专用的名称元素
                    nameElement = createNameElement(field, fieldElementName);
                    container.append(nameElement);
                }

                // 创建输入元素
                // 根据需要处理规则
                innerElement = createElement(field, fieldElementName, rules);

                if (innerElement instanceof Array)
                    each(innerElement, function (element) {
                        container.append(element);
                    });
                else
                    container.append(innerElement);

                bindTipRule(container, rules.tipRule);

                // 根据需要创建 vo-message
                if (hasValidate && isSimple) {

                    var formName = formController.$name;

                    var voMessage = angular.element('<vo-message target="' + formName + '.' + fieldElementName + '"></vo-message>');

                    container.append(voMessage);
                }

                // 最终编译
                $compile($element.contents())($scope);
            };

            return {
                restrict: 'E',
                require: ['^^?schema', '^^?form', '^^?schemaComplexContainer'],
                scope: true,
                controllerAs: '$ctrl',
                link: function ($scope, $element, $attrs, controllers) {

                    var controller = $scope.$ctrl,
                        schemaController = controllers[0],
                        parentController = controllers[2];

                    controller.formController = controllers[1];
                    controller.showName = (!$attrs.showName || $attrs.showName === 'true');

                    // 如果为 field 设置了什么, 就尝试获取 field 上的内容
                    if ($attrs.field) {

                        watchField().then(function (field) {

                            if (schemaController) {

                                watchSchema().then(function (schema) {

                                    tryRender(field, schema);

                                });

                            } else {

                                tryRender(field, null);

                            }

                        });

                    } else if ($attrs.fieldId) {
                        // 否则就尝试根据 fieldId 并配合外层的 schema 来获取 field。

                        // 但是没有外层 schema 的话。就只能...
                        if (!schemaController && !parentController) {
                            $element.text('如果设置了 field-id 就必须在外层提供 schema。但好像并没有。');
                            return;
                        }

                        if (parentController) {

                            var fieldFromParent = find(parentController.fields, function (field) {
                                return field.id === $attrs.fieldId;
                            });

                            if (!schemaController) {
                                tryRender(fieldFromParent, null);
                                return;
                            }

                            watchSchema().then(function (schema) {
                                tryRender(fieldFromParent, schema);
                            });

                        } else {
                            watchSchema().then(function (schema) {

                                var fieldFromSchema = find(schema, function (field) {
                                    return field.id === $attrs.fieldId;
                                });

                                tryRender(fieldFromSchema, schema);
                            });
                        }
                    } else {

                        // 如果两个都没设置, 或者没有外层 schema 那就....
                        $element.text('请提供 field 或者 field-id 属性。');
                    }

                    function watchSchema() {

                        return $q(function (resolve) {

                            // 如果有父级就从父级查找
                            var disposeWatcher = $scope.$watch(function () {

                                return schemaController.schema;

                            }, function (schema) {

                                if (!schema)
                                    return;

                                resolve(schema);

                                disposeWatcher();
                                disposeWatcher = null;
                            });
                        });
                    }

                    function watchField() {

                        return $q(function (resolve) {

                            var disposeWatcher = $scope.$watch($attrs.field, function (field) {

                                if (!field)
                                    return;

                                resolve(field);

                                disposeWatcher();
                                disposeWatcher = null;
                            });
                        });
                    }

                    /**
                     * 元素创建与编译前的检查
                     */
                    function tryRender(field, schema) {

                        if (!field)
                            $element.text('在 schema 上没有找到目标属性。');
                        else
                            controller.$render(field, schema);
                    }
                },
                controller: SchemaFieldController
            };
        })

        .directive('schemaComplexContainer', function ($compile) {

            return {
                restrict: 'E',
                scope: true,
                require: '^^schemaField',
                controllerAs: '$ctrl',
                link: function ($scope, $element, $attrs) {

                    var isMulti = ($attrs.multi === 'true');

                    var fields = $scope.$eval($attrs.fields);

                    $scope.$ctrl.fields = fields;

                    each(fields, function (field) {

                        var child = angular.element('<schema-field field-id="' + field.id + '">');

                        $element.append(child);
                    });

                    $compile($element.contents())($scope);
                },
                controller: function SchemaComplexController() {
                }
            };
        });
});