(function () {

    /**
     * 以下代码包含下面这些自定义标签:
     *  schema
     *  schema-field
     *  schema-field-name
     *  schema-complex-name
     *  schema-complex
     *  schema-input-container
     *  schema-field-tip
     *  schema-input-toolbar
     *  schema-complex-toolbox
     *
     * 后续如有增加新的自定义标签, 请在这里追加。方便控制外观自定义。
     *
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

    var FIELD_TYPES = {
        "INPUT": "INPUT",
        "MULTI_INPUT": "MULTIINPUT",
        "SINGLE_CHECK": "SINGLECHECK",
        "MULTI_CHECK": "MULTICHECK",
        "COMPLEX": "COMPLEX",
        "MULTI_COMPLEX": "MULTICOMPLEX",
        "LABEL": "LABEL"
    };

    var find, findIndex, each, any, all, exists, is;

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

    (function () {

        // 解耦包装帮主函数
        // 便于后续脱离第三方库时, 进行自定义实现

        is = {};

        exists = function (target) {
            return target !== null && target !== undefined;
        };

        if (!window._)
            console.warn('Please import underscore !!!');
        else {
            find = _.find;
            any = _.some;
            all = _.every;
            each = _.each;
            findIndex = _.findIndex;

            is.string = _.isString;
            is.array = _.isArray;
        }
    })();

    /**
     * 获取随机数字
     */
    function random(length) {
        return Math.random().toString().substr(2, length || 6);
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
                if (rule.value === 'false')
                // 除了 disable/readonly/required 这类值回事布尔外, 其他的一定都不是
                // 所以固定值总是 false 的也不用继续处理了
                    return;

                if (rule.value === 'true')
                    rules[rule.name] = true;
                else if (/^-?\d+(\.\d+)?$/.test(rule.value))
                // 尝试简单的数字检查, 如果是就转换
                    rules[rule.name] = parseFloat(rule.value);
                else if ('url' in rule)
                // 如果是有 url 的就把完整的记下来
                    rules[rule.name] = rule;
                else
                    rules[rule.name] = rule.value;


            } else if (schema) {
                // 如果有需要记录的信息, 则转换依赖条件, 并保存值
                rules[rule.name] = new DependentRule(rule, field, schema);
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
                type = 'datetime-local';
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
    function getInputValue(value, field, valueTypeRule) {

        var parsedValue = null;
        var valueType = field.fieldValueType;

        if (!exists(value))
            return null;

        // 如果字段上有具体的值类型声明
        // 就使用支持的类型进行转换
        switch (valueType) {
            case 'INT':
            case 'DOUBLE':
                // 因为 js 的数字只有 number 类型, 所以 parseInt 和 parseFloat 所输出的是否带有小数点, 只取决于输入值
                // 所以这里不用特意为 int 使用 parseInt, 因为即使传入 1.0, parseFloat 输出的依然是 1
                parsedValue = parseFloat(value);
                return isNaN(parsedValue) ? null : parsedValue;
        }

        // 否则, 按 valueTypeRule 来转换
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
            case VALUE_TYPES.DECIMAL:

                // 这里的逻辑同上一段
                parsedValue = parseFloat(value);
                if (isNaN(parsedValue))
                    return null;
                return parsedValue;

            case VALUE_TYPES.DATE:
            case VALUE_TYPES.TIME:

                if (!value)
                    return null;

                parsedValue = new Date(value);

                if (isNaN(parsedValue.getDate()))
                    throw '日期(时间)格式不正确';

                return parsedValue;
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
            element.attr('ng-' + attr, '$rules.' + name + '.getLength()');
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
            element.attr('ng-' + attr, '$rules.' + name + '.checked()');
        } else if (rule) {
            element.attr(attr, true);
        }
    }

    /**
     * tip 只是简单的显示, 默认应该不会是依赖规则。如果某天真的是了... 请修改这里
     * 2016-06-19 18:01:39 追加认为默认值也需要作为 tip 显示出来
     */
    function bindTipRule(container, rules) {

        // 除了 tipRule 和 devTipRule 外, 天猫还有其他非固定名称的 rule, 形如: 45690217-1。
        // 一般这种 rule 都是关系型 rule, 无法进行逻辑控制
        // 所以也作为 tip 显示

        each(rules, function (content, key) {
            if (key === 'tipRule' || key.indexOf('Rule') < 0) {

                var contentContainer = angular.element('<schema-field-tip>');
                container.append(contentContainer);

                // 有的 tip 中有 url 属性, 有的话, 就增加 a 标签
                if (!is.string(content)) {

                    if ('url' in content && !!content.url) {
                        var aTag = angular.element('<a href="' + content.url + '" target="_blank">');
                        aTag.text(content.value);
                        contentContainer.append(aTag);
                    } else {
                        contentContainer.text(content.value);
                    }
                }
                else {
                    contentContainer.text(content);
                }
            }
        });
    }

    function bindDefaultValueTip(container, field) {

        function tryGetDefault(defaultPropName) {
            var value = field[defaultPropName];
            if (defaultPropName in field && exists(value)) {
                return value;
            }
            return null;
        }

        var options = field.options;

        var result = tryGetDefault('defaultValue');
        if (!exists(result)) {
            result = tryGetDefault('defaultValues');
            if (!exists(result) || !result.length) return;
        }

        if (exists(options) && options.length) {
            result = !is.array(result) ? find(options, function(o) {
                return o.value == result;
            }).displayName : result.map(function (r) {
                return find(options, function (o) {
                    return o.value == r;
                }).displayName;
            });
        }

        var contentContainer = angular.element('<schema-field-tip>');
        contentContainer.text('该字段包含默认值: ' + angular.toJson(result));
        container.append(contentContainer);
    }

    function resetValue(valueObj, fieldObj) {

        ['value', 'values', 'complexValue', 'complexValues'].some(function (key) {

            if (key in valueObj) {
                fieldObj[key] = valueObj[key];
                return true;
            }

            return false;
        });
    }

    /**
     * 切换字段上的属性所存储的位置。专属!供 disableRule 切换时调用。参见 schema-field 中 $render 里的切换逻辑
     * @param {object} field
     * @param {Array.<String>} valueKeys 需要切换的属性名
     * @param {bool} fromPrivate 标识切换方向, true = from private, false = to private
     */
    function switchPrivateValue(field, valueKeys, fromPrivate) {

        if (field.type === FIELD_TYPES.COMPLEX) {
            each(field.fields, function (childField) {
                switchPrivateValue(childField, valueKeys, fromPrivate);
            });
            return;
        }

        if (fromPrivate) {
            // 如果启用了就尝试移回去
            any(valueKeys, function (key) {

                var privateKey = '$' + key;

                var privateValueObj = field[privateKey];

                if (privateKey in field) {
                    // 值有就保存, 木有就不保存
                    if (exists(privateValueObj)) {
                        field[key] = privateValueObj;
                    }
                    field[privateKey] = null;
                    // 只要找到其中一个属性就不用继续了
                    return true;
                }

                return false;
            })
        } else {
            // 禁用了就保存值到缓存字段
            // 并移除现有的值
            any(valueKeys, function (key) {

                var valueObj = field[key];

                if (key in field) {
                    // 值有就保存, 木有就不保存
                    if (exists(valueObj)) {
                        field['$' + key] = valueObj;
                    }
                    field[key] = null;
                    // 只要找到其中一个属性就不用继续了
                    return true;
                }

                return false;
            });
        }
    }

    /**
     * @class 为 multiComplex 字段的 values 提供包装
     */
    function ComplexValue(fields) {
        // 生成 id 用来标记缓存
        this.$id = '$ComplexValue' + random();
        // 追加属性
        this.fieldMap = {};
        this.fieldKeySet = [];
        // 存入缓存
        ComplexValue.Caches[this.$id] = this;
        // 如果 fields 存在, 就自动生成相应的字段
        this.putAll(fields);
    }

    ComplexValue.Caches = {};

    ComplexValue.prototype.get = function (field) {
        return this.fieldMap[field.id || field];
    };

    ComplexValue.prototype.put = function (field) {
        this.fieldKeySet.push(field.id);
        this.fieldMap[field.id] = field;
        // 为 field 记录当前 value 组的 id, 便于在依赖计算时查找
        field.$parentComplexValueId = this.$id;

        return this;
    };

    ComplexValue.prototype.putAll = function (fields) {
        var self = this;

        each(fields, function (f) {
            self.put(angular.copy(f));
        });

        return self;
    };

    ComplexValue.prototype.copyFrom = function (originComplexValue, field) {
        var self = this;
        var fieldMap = originComplexValue.fieldMap || (originComplexValue.fieldMap = {});

        each(field.fields, function (childField) {

            var mapItem = fieldMap[childField.id];

            if (!mapItem) {
                // 没有, 创建新的进行字段补全
                // 否则画面上显示的都不是全部的字段
                mapItem = angular.copy(childField);
            } else {
                // 如果已经存在, 只要补全属性就可以了
                // 这里没有使用 angular.copy 完整的 field 来覆盖 complexValues 内的 field。
                // 是为了减少可能存在的影响。
                // 只选择把后续需要的属性进行了赋值(引用)
                mapItem.rules = childField.rules;
                mapItem.name = childField.name;
                mapItem.options = childField.options;
                mapItem.fieldValueType = childField.fieldValueType;
            }

            self.put(mapItem);
        });

        return self;
    };

    /**
     * @class 依赖型规则
     * 用于记录依赖的相关信息。便于后续计算。
     * 使用明确的类型(class), 便于后续判断(instanceOf)。
     */
    function DependentRule(rule, field, schema) {

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

        // 因为 field 上回存储 $rules 便于访问
        // 如果这里直接把 field 存在 this 上, 就会造成递归访问, 无法将数据 JSON 化
        // 所以需要通过第三方存储来保存相互的关系
        this.$fieldId = field.id;
        DependentRule.fieldCache[field.id] = field;
    }

    DependentRule.fieldCache = {};

    /**
     * 获取依赖结果
     */
    DependentRule.prototype.checked = function () {

        var self = this,
            dependExpressList = self.dependExpressList,
            currRule = self.origin,
            currentField = DependentRule.fieldCache[self.$fieldId],
            forceTrue = false;

        var result = all(dependExpressList, function (express) {

            // 每一个表达式的计算, 都只支持简单处理
            // 如果后续需要, 请继续扩展

            var parentDisableRule, parentComplexValue;

            // 当前字段可能是一个 multiComplex 下的字段
            // 所以如果是的话, 那么它依赖的字段可能和它同属一个 multiComplex
            // 因为每一组 multiComplex 的 complexValue 都是 clone 出来的
            // 所以 express.field 所记录的 field 并不包含真正的值
            // 所以!
            // 如果字段包含 $parentComplexValueId 那么就先从 valueMap 判断是否有这个字段
            // 如果没有就原始字段, 有的话就用同组的 complexValueMap 中的字段

            var targetFieldInComplex = null;
            var targetField = express.field;

            if (currentField.$parentComplexValueId) {
                parentComplexValue = ComplexValue.Caches[currentField.$parentComplexValueId];
                if (parentComplexValue) {
                    targetFieldInComplex = parentComplexValue.get(targetField);
                    if (targetFieldInComplex)
                        targetField = targetFieldInComplex;
                }
            }

            // 如果依赖的目标字段不存在, 则规则强制生效
            if (!targetField)
                return !(forceTrue = true);

            if (currRule.name === 'disableRule' && !!(parentDisableRule = targetField.$rules.disableRule)) {
                // 如果当前要检查的就是 disableRule
                // 并且父级也有 disableRule
                // 那么如果父级不显示, 子级自然不能显示
                if (parentDisableRule.checked())
                    return true;
            }

            // 严格的逻辑应该根据 switch 判断 field 类型来取值
            // 这里只进行简单处理, 逐个尝试获取值内容

            var value = targetField.values || targetField.value;

            if (value && 'value' in value)
                value = value.value;

            // 如果最终获取的值内容为空, 那么认为计算失败, 则规则强制生效
            if (value === null)
                return !(forceTrue = true);

            switch (express.symbol) {
                case SYMBOLS.EQUALS:
                    return value == express.value;
                case SYMBOLS.NOT_EQUALS:
                    return value != express.value;
                case SYMBOLS.NOT_CONTAINS:
                    return !!value && !any(value, function (valueObj) {
                            return valueObj.value == express.value;
                        });
                default:
                    return false;
            }
        });

        return forceTrue || result;
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
                    $scope = controller.$scope,
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
                function createElement(field, name) {

                    var innerElement;

                    switch (field.type) {
                        case FIELD_TYPES.INPUT:
                            (function createInputElements() {

                                var regexRule = rules.regexRule,
                                    valueTypeRule = rules.valueTypeRule,
                                    requiredRule = rules.requiredRule,
                                    readOnlyRule = rules.readOnlyRule,
                                    type = getInputType(valueTypeRule);

                                if (type === 'textarea') {
                                    innerElement = angular.element('<textarea class="form-control">');
                                    // 如果是 html 就加个特殊样式用来便于外观控制
                                    if (valueTypeRule === VALUE_TYPES.HTML)
                                        innerElement.addClass('schema-field-html');
                                } else {
                                    innerElement = angular.element('<input class="form-control">').attr('type', type);
                                }

                                innerElement.attr('name', name);
                                innerElement.attr('ng-model', 'field.value');

                                bindBoolRule(innerElement, readOnlyRule, 'readOnlyRule', 'readonly');
                                bindBoolRule(innerElement, requiredRule, 'requiredRule', 'required');

                                bindLengthRule(innerElement, rules.minLengthRule, 'minLengthRule', 'minlength');
                                bindLengthRule(innerElement, rules.maxLengthRule, 'maxLengthRule', 'maxlength');

                                // 处理正则规则
                                if (regexRule) {

                                    if (regexRule instanceof DependentRule) {
                                        // 如果是依赖类型
                                        // 则如果需要, 则赋值正则, 否则为空。为空时将总是验证通过(即不验证)
                                        innerElement.attr('ng-pattern', '$rules.regexRule.getRegex()');

                                    } else if (regexRule !== 'yyyy-MM-dd') {
                                        // 如果是日期格式验证就不需要了
                                        // type=date 时 angular 会验证的
                                        innerElement.attr('pattern', regexRule);
                                    }
                                }

                                innerElement.attr('title', field.name || field.id);

                                // 根据类型转换值类型, 并填值
                                field.value = getInputValue(field.value, field, valueTypeRule);

                                // 没有填值, 并且有默认值, 那么就使用默认值
                                // 之所以不和上面的转换赋值合并, 是因为 getInputValue 有可能转换返回 null
                                // 所以这里要单独判断
                                if (!exists(field.value) && exists(field.defaultValue))
                                    field.value = getInputValue(field.defaultValue, field, valueTypeRule);

                                if ((!readOnlyRule || readOnlyRule instanceof DependentRule) && type.indexOf('date') > -1) {
                                    // 日期类型的输入框要追加一个按钮, 用来触发 popup picker
                                    // 并且 readonly 时, 要把这个按钮隐藏掉
                                    var inputGroup = angular.element('<div class="input-group">');
                                    var inputGroupBtn = angular.element('<span class="input-group-btn"><button type="button" class="btn btn-default" ng-click="$opened = !$opened"><i class="glyphicon glyphicon-calendar"></i></button>');

                                    innerElement.attr('uib-datepicker-popup', '');
                                    innerElement.attr('date-model-format', (type === 'date' ? 'yyyy-MM-dd' : 'yyyy-MM-dd hh:mm:ss'));
                                    innerElement.attr('is-open', '$opened');

                                    if (readOnlyRule instanceof DependentRule) {
                                        inputGroupBtn.attr('ng-if', '!$rules.readOnlyRule.checked()')
                                    }

                                    inputGroup.append(innerElement);
                                    inputGroup.append(inputGroupBtn);

                                    innerElement = inputGroup;
                                }
                            })();
                            break;
                        case FIELD_TYPES.SINGLE_CHECK:
                            (function createSelectElements() {

                                var nullValueObj;

                                var requiredRule = rules.requiredRule;

                                var options = field.options;

                                innerElement = angular.element('<select class="form-control">');
                                innerElement.attr('ng-options', 'option.value as option.displayName for option in $options');
                                innerElement.attr('name', name);
                                innerElement.attr('ng-model', 'field.value.value');

                                bindBoolRule(innerElement, requiredRule, 'requiredRule', 'required');
                                bindBoolRule(innerElement, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                                if (!field.value)
                                    field.value = {value: null};
                                else {
                                    // 如果 value 的值是一些原始值类型, 如数字那么可能需要转换处理
                                    // 所以这一步做额外的处理
                                    field.value.value = getInputValue(field.value.value, field);
                                }

                                // 处理默认值, 判断基本同 input 类型, 参见 input 中的注释
                                if (!exists(field.value.value) && exists(field.defaultValue)) {
                                    field.value.value = getInputValue(field.defaultValue, field);
                                }

                                if (!requiredRule) {
                                    // 非必填, 就创建空选项
                                    // 但是并不能直接修改 field 上的 options, 否则会导致后端!!爆炸!!
                                    // 所以要克隆新的出来使用
                                    options = angular.copy(options);
                                    options.unshift(nullValueObj = {
                                        displayName: '',
                                        value: null
                                    });

                                    // 如果当前的选中值也木有, 就用这个默认的
                                    if (!field.value.value) {
                                        field.value = nullValueObj;
                                    }
                                }

                                // 最终保存到 $scope 上, 供页面绑定使用
                                $scope.$options = options;

                                innerElement.attr('title', field.name || field.id);
                            })();
                            break;
                        case FIELD_TYPES.MULTI_CHECK:
                            (function createCheckboxElements() {

                                var selected, valueStringList;
                                var requiredRule = rules.requiredRule;
                                var defaultValues = field.defaultValues;

                                innerElement = [];

                                // 创建用于记录每个多选框选中状态的对象
                                selected = $scope.selected = [];

                                // 通过事件触发 update 来操作 field 的 values 数组
                                $scope.update = function (index) {

                                    // 获取选中值
                                    var selectedValue = field.options[index].value;

                                    // 获取选中的值, 在选中值集合里的位置
                                    var selectedIndex = findIndex(field.values, function (valueObj) {
                                        return valueObj.value == selectedValue;
                                    });

                                    if ($scope.selected[index]) {
                                        // 当前选中选中, 并且不在集合中的
                                        if (selectedIndex < 0)
                                            field.values.push({value: selectedValue});
                                    } else {
                                        // 没选中, 并且在集合中的
                                        if (selectedIndex > -1)
                                            field.values.splice(selectedIndex, 1);
                                    }
                                };

                                if (!field.values)
                                    field.values = [];

                                // 先把 values 里的选中值取出, 便于后续判断
                                valueStringList = field.values.map(function (valueObj) {
                                    // 如果 value 的值是一些原始值类型, 如数字那么可能需要转换处理
                                    // 所以这一步做额外的处理
                                    return (valueObj.value = getInputValue(valueObj.value, field).toString());
                                });

                                each(field.options, function (option, index) {

                                    var label = angular.element('<label></label>'),
                                        checkbox = angular.element('<input type="checkbox">');

                                    checkbox.attr('ng-model', 'selected[' + index + ']');

                                    checkbox.attr('name', name);

                                    checkbox.attr('title', field.name || field.id);

                                    checkbox.attr('ng-change', 'update(' + index + ')');

                                    // checkbox 的必填比较特殊
                                    if (requiredRule) {
                                        if (requiredRule instanceof DependentRule) {
                                            checkbox.attr('ng-required', '$rules.requiredRule.checked() && !field.values.length');
                                        } else {
                                            checkbox.attr('ng-required', '!field.values.length');
                                        }
                                    }

                                    bindBoolRule(checkbox, rules.readOnlyRule, 'readOnlyRule', 'readonly');

                                    // 如果有原值, 就使用原值
                                    // 如果没有, 看下是不是必填字段
                                    // 如果是必填字段, 看看是不是有默认值
                                    // 如果有就把默认值放上去
                                    if (valueStringList.length) {
                                        selected[index] = !(valueStringList.indexOf(option.value) < 0);
                                    } else if (requiredRule === true && !!defaultValues.length) {
                                        selected[index] = !(defaultValues.indexOf(option.value) < 0);
                                    }

                                    label.append(checkbox, '&nbsp;', option.displayName);

                                    innerElement.push(label);
                                });
                            })();
                            break;
                        case FIELD_TYPES.COMPLEX:

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

                            innerElement = angular.element('<schema-complex fields="$fields">');

                            break;
                        case FIELD_TYPES.MULTI_COMPLEX:

                            // multiComplex 字段, 其值不同于 complex 字段, 是存在于 complexValues 中。
                            // 存在 complexValues 中, 每一组的 fieldMap 的 field 的 value 中。
                            // 所以需要根据每个 complexValues 来创建 container

                            var complexValues = field.complexValues || (field.complexValues = []);

                            if (!complexValues.length) {
                                // 如果获取的值里没有内容, 就创建一套默认
                                complexValues.push(new ComplexValue(field.fields));
                            } else {
                                // 包装原有的 value, 便于后续使用
                                complexValues = complexValues.map(function (complexValueObj) {
                                    return new ComplexValue().copyFrom(complexValueObj, field);
                                });
                            }

                            $scope.$complexValues = complexValues;

                            innerElement = angular.element('<schema-complex multi="true" fields="complexValue.fieldMap">');

                            innerElement.attr('ng-repeat', 'complexValue in $complexValues');

                            // multiComplex 字段, 有多个 complexValue
                            // 所以需要创建工具栏和按钮, 来新建 complexValue
                            $scope.$newComplexValue = function () {
                                complexValues.push(new ComplexValue(field.fields));
                            };

                            if (controller.canAdd) {
                                var toolbar = angular.element('<schema-input-toolbar>');
                                toolbar.append('<button class="btn btn-schema btn-success" ng-click="$newComplexValue()"><i class="fa fa-plus"></i></button>');
                                container.append(toolbar);
                            }

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
                        case FIELD_TYPES.INPUT:
                        case FIELD_TYPES.SINGLE_CHECK:
                        case FIELD_TYPES.MULTI_CHECK:
                            innerElement = angular.element('<schema-field-name>');
                            break;
                        case FIELD_TYPES.COMPLEX:
                        case FIELD_TYPES.MULTI_COMPLEX:
                            innerElement = angular.element('<schema-complex-name>');
                            break;
                        default:
                            console.error('不支持其他类型');
                            return null;
                    }

                    innerElement.attr('for', name);
                    innerElement.text(field.name || field.id);

                    if (rules.requiredRule) {
                        // 如果这个字段是需要必填的
                        // 就加个红星
                        // 如果是依赖型的必填, 那就给红星 class 加成 ng-class
                        if (rules.requiredRule instanceof DependentRule) {
                            innerElement.attr('ng-class', '{"schema-field-required":$rules.requiredRule.checked()}');
                        } else {
                            innerElement.addClass('schema-field-required');
                        }
                    }

                    if (hasDisableRule)
                        innerElement.attr('ng-if', '!$rules.disableRule.checked()');

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

                var rules = doRule(field, schema),
                    disableRule = rules.disableRule,
                    hasDisableRule = (disableRule && disableRule instanceof DependentRule);

                var innerElement, nameElement, isSimple, enabledExpression;

                // 放到 scope 上, 供画面绑定使用
                // 创建元素时, ngModel 会直接指向到 field.value 或 values 等
                $scope.field = field;
                $scope.$rules = rules;

                // 把处理好的规则保存到字段上, 便于依赖型的规则可以递归查询
                field.$rules = rules;

                isSimple = (field.type != FIELD_TYPES.COMPLEX && field.type != FIELD_TYPES.MULTI_COMPLEX);

                if (showName) {
                    // 如果需要显示名称
                    // 就创建专用的名称元素
                    nameElement = createNameElement(field, fieldElementName);
                    container.append(nameElement);
                }

                // 创建一个专门的输入元素容器便于控制外观
                innerElement = angular.element('<schema-input-container>');
                container.append(innerElement);
                container = innerElement;

                if (field.type === FIELD_TYPES.MULTI_COMPLEX)
                    container.attr('multi', true);

                if (hasDisableRule) {

                    enabledExpression = '!$rules.disableRule.checked()';

                    container.attr('ng-if', enabledExpression);

                    // 还要为 disableRule 处理值得移除和补回
                    $scope.$watch(enabledExpression, function (enabled) {
                        switchPrivateValue(field, ['value', 'values', 'complexValues'], enabled);
                    });
                }

                // 创建输入元素
                // 根据需要处理规则
                innerElement = createElement(field, fieldElementName);

                if (innerElement instanceof Array)
                    each(innerElement, function (element) {
                        container.append(element);
                    });
                else
                    container.append(innerElement);

                bindDefaultValueTip(container, field);
                bindTipRule(container, rules);

                // 根据需要创建 vo-message
                if (hasValidate && isSimple) {

                    var formName = formController.$name;

                    var voMessage = angular.element('<vo-message target="' + formName + '.' + fieldElementName + '"></vo-message>');

                    container.append(voMessage);
                }

                // 最终编译
                $compile($element.contents())($scope);
            };

            SchemaFieldController.prototype.remove = function (complexValue) {
                var $scope = this.$scope;
                var list = $scope.$complexValues;
                var index = list.indexOf(complexValue);
                list.splice(index, 1);
            };

            return {
                restrict: 'E',
                require: ['^^?schema', '^^?form', '^^?schemaComplex'],
                scope: true,
                controllerAs: '$ctrl',
                link: function ($scope, $element, $attrs, controllers) {

                    var controller = $scope.$ctrl,
                        schemaController = controllers[0],
                        parentController = controllers[2];

                    controller.formController = controllers[1];
                    controller.showName = (!$attrs.showName || $attrs.showName === 'true');
                    controller.canAdd = $attrs.add !== 'false';

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

        .directive('schemaComplex', function ($compile) {

            return {
                restrict: 'E',
                scope: true,
                require: '^^schemaField',
                controllerAs: '$ctrl',
                link: function ($scope, $element, $attrs, schemaFieldController) {

                    var isMulti = ($attrs.multi === 'true');

                    var fields = $scope.$eval($attrs.fields);

                    $scope.$ctrl.fields = fields;

                    each(fields, function (field) {

                        var child = angular.element('<schema-field field-id="' + field.id + '">');

                        $element.append(child);
                    });

                    if (isMulti) {
                        $scope.$remove = function (complexValue) {
                            schemaFieldController.remove(complexValue);
                        };
                        var toolbox = angular.element('<schema-complex-toolbox>');
                        toolbox.append('<button class="btn btn-schema btn-danger" ng-click="$remove(complexValue)" ng-if="$complexValues.length > 1"><i class="fa fa-trash-o"></i></button>');
                        $element.append(toolbox);
                    }

                    $compile($element.contents())($scope);
                },
                controller: function SchemaComplexController() {
                }
            };
        });
}());