(function () {

    /**
     * 以下代码包含下面这些自定义标签:
     *  schema
     *  s-field
     *  s-header
     *  s-complex
     *  s-container
     *  s-tip
     *  s-toolbar
     *  s-toolbox
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
            return target !== null && target !== undefined && target !== "";
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
     * 规则是否包含依赖条件
     */
    function hasDepend(rule) {
        var dependExpressList = (rule && rule.dependGroup) ? rule.dependGroup.dependExpressList : null;
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
    function getRules(field, schema) {

        var withSchema = !!schema;
        var rules = field.$rules;

        // 存在上次生成的 rule map
        // 并且上次生成的有依赖 schema
        // 那么直接返回
        // 如果上次生成的没有依赖 schema, 并且这次有
        // 那么就需要重新生成

        if (rules) {
            if (!withSchema || rules.$withSchema)
                return rules;
        } else {
            rules = {};
        }

        // 没啥可用的信息就算了
        if (!field || !field.rules)
            return rules;

        // 没有规则好处理, 果断算了
        if (!field.rules.length)
            return rules;

        each(field.rules, function (rule) {
            if (!hasDepend(rule)) {
                var newRule;
                // 除了 disable/readonly/required 这类值是布尔外, 其他的一定都不是
                // 所以固定值总是 false 的也不用继续处理了
                if (rule.value === 'false')
                    return;
                newRule = {};
                newRule.__proto__ = rule;

                if (rule.value === 'true')
                    newRule.value = true;

                // 尝试简单的数字检查, 如果是就转换
                else if (/^-?\d+(\.\d+)?$/.test(rule.value))
                    newRule.value = parseFloat(rule.value);

                rules[rule.name] = newRule;

            } else if (schema) {
                // 如果有需要记录的信息, 则转换依赖条件, 并保存值
                rules[rule.name] = new DependentRule(rule, field, schema);
            }
        });

        rules.$withSchema = withSchema;
        field.$rules = rules;

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

        switch (valueTypeRule.value) {
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

        switch (valueTypeRule.value) {
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
    function bindLengthRule(element, rule, name, attr, ngAttr) {

        if (!rule) return;

        if (rule instanceof DependentRule) {
            element.attr(ngAttr || ('ng-' + attr), 'rules.' + name + '.getLength()');
        } else {
            element.attr(attr, rule.value);
        }
    }

    function bindInputLengthRule(element, rules) {

        var minRule = rules.minLengthRule,
            maxRule = rules.maxLengthRule;

        if (minRule) {
            if (isByteUnit(minRule))
                bindLengthRule(element, minRule, 'minLengthRule', 'minbytelength', 'minbytelength');
            else
                bindLengthRule(element, minRule, 'minLengthRule', 'minlength');
        }

        if (maxRule) {
            if (isByteUnit(maxRule))
                bindLengthRule(element, maxRule, 'maxLengthRule', 'maxbytelength', 'maxbytelength');
            else
                bindLengthRule(element, maxRule, 'maxLengthRule', 'maxlength');
        }
    }

    /**
     * 为 required 和 readonly 规则提供支持
     */
    function bindBoolRule(element, rule, name, attr) {

        if (!rule) return;

        if (rule instanceof DependentRule) {
            element.attr('ng-' + attr, 'rules.' + name + '.checked()');
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

            if (key.indexOf('$') === 0)
                return;

            if (key.indexOf('Rule') > 0 && key !== 'tipRule')
                return;

            var contentContainer = angular.element('<s-tip ng-if="showTip">');
            container.append(contentContainer);

            // 有的 tip 中有 url 属性, 有的话, 就增加 a 标签

            if ('url' in content && !!content.url) {
                var aTag = angular.element('<a href="' + content.url + '" target="_blank">');
                aTag.text(content.value);
                contentContainer.append(aTag);
            } else {
                contentContainer.text(content.value);
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
            result = !is.array(result) ? find(options, function (o) {
                return o.value == result;
            }).displayName : result.map(function (r) {
                return find(options, function (o) {
                    return o.value == r;
                }).displayName;
            });
        }

        var contentContainer = angular.element('<s-tip>');
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

    function isByteUnit(rule) {
        return rule.unit === 'byte';
    }

    /**
     * 切换字段上的属性所存储的位置。专属!供 disableRule 切换时调用。参见 s-field 中 $render 里的切换逻辑
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
     *
     * 因为 multi complex 字段可以有多个 complex value，而每个 complex value 包含多个 field value
     * 所以需要提供一个包装，最主要的是，提供对无值 field 的支持。因为有可能服务器端传递的 complex value 中不包含完整的 fields
     * 这里可以参见 copyFrom 方法
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

        var dependGroup = rule.dependGroup,
            dependExpressList = dependGroup.dependExpressList,
            operator = dependGroup.operator;

        // Debug 信息
        this.$key = '$Depend' + random();

        this.checker = operator === "or" ? any : all;
        this.dependExpressList = dependExpressList.map(function (dependExpress) {

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

        // 如果这里直接把 field 存在 this 上, 就会造成递归访问, 无法将数据 JSON 化
        // 所以需要通过第三方存储来保存相互的关系
        DependentRule.fieldCache[(this.$fieldId = field.$name || field.id)] = field;
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
            forceFail = false;

        var result = self.checker(dependExpressList, function (express) {

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
            // 2016-06-24 12:44:11 为了兼容老版本留下的垃圾数据, 此处临时修改
            // 如果依赖的目标不存在, 则强制规则失效, 并打断 all 判断
            if (!targetField)
                return !(forceFail = true);

            if (currRule.name === 'disableRule' && !!(parentDisableRule = getRules(targetField).disableRule)) {
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
            // 2016-06-24 12:44:11 为了兼容老版本留下的垃圾数据, 此处临时修改
            // 如果最终获取的值内容为空, 则强制规则失效, 并打断 all 判断
            if (value === null)
                return !(forceFail = true);

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

        return (forceFail ? false : result);
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

    function FieldRepeater(fieldList, schema, parentScope, targetElement, $compile) {
        this.fieldList = fieldList;
        this.schema = schema;
        this.repeaterScope = parentScope.$new();
        this.targetElement = targetElement;
        this.$compile = $compile;
    }

    FieldRepeater.prototype.renderOne = function (field) {

        var self = this,
            schema = self.schema,
            parentScope = self.repeaterScope,
            targetElement = self.targetElement,
            $compile = self.$compile;

        // 一次性解析字段的所有 rule
        var rules,
            disableRule,
            disabledExpression,
            fieldElement,
            fieldScope;

        // 2016-07-08 11:11:54
        // 增加对 isDisplay 属性的支持
        // 当该属性为字符串 0 时, 不处理该字段, 否则其他任何值都处理
        if (field.isDisplay == "0")
            return;

        field.$name = 'Field' + random();

        rules = getRules(field, schema);
        disableRule = rules.disableRule;

        // 如果 disableRule 固定为 true 则这个字段就永远不需要处理
        // 如果不为 true, 是一个依赖型 rule 的话, 就需要为字段创建 ng-if 切换控制
        // 如果为 false 或不存在的话, 只需创建单纯的 s-field 即可
        // 不加入 disableRule instanceof DependentRule 判断, 这里也是可以正常运行的
        // 因为原始的 rule 其 value 是字符串型, 所以 === true 会返回 false, 虽然字符串的内容确实是 "true"
        // 但还是加上更靠谱, 所以我在 2016-07-07 21:58:50 补上了这段内容, 吓尿。。。
        if (disableRule && !(disableRule instanceof DependentRule) && disableRule.value === true)
            return;

        fieldElement = angular.element('<s-field>');
        // 创建专有 scope, 通过专有 scope 传递 field 给 element(directive)
        fieldScope = parentScope.$new();
        // 显式注册 attr, 并把 field 保存到 scope 上
        fieldScope.field = field;
        fieldElement.attr('field', 'field');

        if (disableRule instanceof DependentRule) {
            // 为 disableRule 的访问创建简写访问
            // 并注册 ng-if
            // 同时为值切换提供支持

            fieldScope.disabled = disableRule;
            disabledExpression = '!disabled.checked()';

            fieldElement.attr('ng-if', disabledExpression);

            fieldScope.$watch(disabledExpression, function (enabled) {
                switchPrivateValue(field, ['value', 'values', 'complexValues'], enabled);
            });
        }

        targetElement.append(fieldElement);
        $compile(fieldElement)(fieldScope);
    };

    FieldRepeater.prototype.renderList = function () {

        var self = this,
            fieldList = self.fieldList;

        each(fieldList, function (field) {
            self.renderOne(field);
        });
    };

    FieldRepeater.prototype.destroy = function () {

        var self = this,
            parentScope = self.repeaterScope,
            targetElement = self.targetElement;

        if (parentScope) {
            parentScope.$destroy();
            parentScope = null;
            self.repeaterScope = null;
        }

        if (targetElement && targetElement.length) {
            targetElement.empty();
            targetElement = null;
            self.targetElement = null;
        }
    };

    angular.module('voyageone.angular.directives')

        .directive('schema', function ($compile) {

            function SchemaController($scope) {
                this.$scope = $scope;
            }

            SchemaController.prototype.getSchema = function () {
                return this.schema;
            };

            SchemaController.prototype.$setSchema = function (data) {
                this.schema = data;
            };

            SchemaController.prototype.$render = function ($element) {

                var controller = this,
                    $scope = controller.$scope,
                    schema = controller.getSchema(),
                    fieldRepeater = controller.fieldRepeater;

                if (fieldRepeater) {
                    fieldRepeater.destroy();
                    controller.fieldRepeater = null;
                }

                if (!schema || !schema.length)
                    return;

                fieldRepeater = new FieldRepeater(schema, schema, $scope, $element, $compile);

                controller.fieldRepeater = fieldRepeater;

                fieldRepeater.renderList();
            };

            return {
                restrict: 'E',
                scope: true,
                controllerAs: 'schemaController',
                link: function ($scope, $element, $attrs) {
                    $scope.$watch($attrs.data, function (data) {
                        var schemaController = $scope.schemaController;
                        schemaController.$setSchema(data);
                        schemaController.$render($element);
                    });
                },
                controller: SchemaController
            }
        })

        .directive('sField', function ($compile) {

            function SchemaFieldController($scope, $element) {
                this.originScope = $scope;
                this.$element = $element;
            }

            SchemaFieldController.prototype.render = function () {

                var controller = this,
                    $element, formController, showName,
                    parentScope, $scope,
                    field, container, hasValidate,
                    rules, innerElement, isSimple;

                controller.destroy();

                parentScope = controller.originScope;

                $scope = parentScope.$new();

                controller.$scope = $scope;

                $element = controller.$element;
                formController = controller.formController;
                showName = controller.showName;

                field = controller.field;

                container = $element;
                hasValidate = !!formController && hasValidateRule(field);

                rules = getRules(field);

                isSimple = (field.type != FIELD_TYPES.COMPLEX && field.type != FIELD_TYPES.MULTI_COMPLEX);

                if (showName)
                    container.append(angular.element('<s-header>'));

                //sofia
                each(rules, function (content, key) {

                    if (key.indexOf('$') === 0)
                        return;

                    if (key.indexOf('Rule') > 0 && key !== 'tipRule')
                        return;

                    var contentContainer = angular.element('<s-tip-new ng-click="showTip =! showTip">');

                    container.append(contentContainer);
                });

                /**updated by piao 去掉了 style="margin-left:15px"*/
                innerElement = angular.element('<div class="s-wrapper">');
                //sofia
                // 创建一个 div 用来包裹非 name 的所有内容, 便于外观控制
                // innerElement = angular.element('<div class="s-wrapper">');

                container.append(innerElement);
                container = innerElement;

                innerElement = angular.element('<s-container>');
                container.append(innerElement);

                // 根据需要创建 vo-message
                if (hasValidate && isSimple) {
                    var formName = formController.$name;
                    var voMessage = angular.element('<vo-message target="' + formName + '.' + field.$name + '"></vo-message>');
                    container.append(voMessage);
                }

                bindDefaultValueTip(container, field);
                bindTipRule(container, rules);

                // 最终编译
                $compile($element.contents())($scope);
            };

            SchemaFieldController.prototype.remove = function (complexValue) {
                var $scope = this.$scope;
                var list = $scope.$complexValues;
                var index = list.indexOf(complexValue);
                list.splice(index, 1);
            };

            SchemaFieldController.prototype.getField = function () {
                return this.field;
            };

            SchemaFieldController.prototype.setField = function (field) {
                this.field = field;
            };

            SchemaFieldController.prototype.destroy = function () {

                var controller = this,
                    $element = controller.$element,
                    $scope = controller.$scope;

                if ($element)
                    $element.empty();

                if ($scope) {
                    $scope.$destroy();
                    controller.$scope = null;
                }
            };

            return {
                restrict: 'E',
                require: ['^^?form'],
                scope: true,
                controllerAs: 'schemaFieldController',
                link: function ($scope, $element, $attrs, requiredControllers) {

                    var controller = $scope.schemaFieldController;

                    // 保存 formController, 用于在后面检查, 是否需要渲染 vo-message 包括一系列的 form 验证
                    controller.formController = requiredControllers[0];

                    // 如果木有设置, 那说个屁啊
                    if (!$attrs.field) {
                        $element.text('请提供 field 属性。');
                        return;
                    }

                    // 保存配置
                    controller.showName = (!$attrs.showName || $attrs.showName === 'true');
                    controller.canAdd = $attrs.add !== 'false';

                    $scope.$watch($attrs.field, function (field) {

                        controller.setField(field);

                        if (!field)
                            return;

                        controller.render();
                    });
                },
                controller: SchemaFieldController
            };
        })

        .directive('sHeader', function () {
            return {
                restrict: 'E',
                require: ['^^sField'],
                scope: false,
                link: function (scope, element, attrs, requiredControllers) {

                    var schemaFieldController = requiredControllers[0];

                    var field = schemaFieldController.getField(),
                        rules = getRules(field),
                        required = rules.requiredRule,
                        requiredClass = 's-required';

                    switch (field.type) {
                        case FIELD_TYPES.INPUT:
                        case FIELD_TYPES.SINGLE_CHECK:
                        case FIELD_TYPES.MULTI_CHECK:
                            element.addClass('simple');
                            break;
                        case FIELD_TYPES.COMPLEX:
                            element.addClass('complex');
                            break;
                        case FIELD_TYPES.MULTI_COMPLEX:
                            element.addClass('complex multi');
                            break;
                    }

                    element.text(field.name || field.id);

                    if (required) {
                        // 如果这个字段是需要必填的
                        // 就加个红星
                        // 如果是依赖型的必填, 那就要动态变更
                        if (required instanceof DependentRule) {
                            scope.$watch(function () {
                                return required.checked();
                            }, function (required) {
                                element[required ? 'addClass' : 'removeClass'](requiredClass);
                            });
                        } else {
                            element.addClass(requiredClass);
                        }
                    }
                }
            }
        })

        .directive('sContainer', function ($compile, $filter) {
            return {
                restrict: 'E',
                scope: false,
                require: ['^^sField'],
                link: function (scope, element, attrs, requiredControllers) {

                    var schemaFieldController = requiredControllers[0];

                    var innerElement;

                    var field = schemaFieldController.getField(),
                        rules = getRules(field), name = field.$name;

                    scope.field = field;
                    scope.rules = rules;

                    switch (field.type) {
                        case FIELD_TYPES.INPUT:
                            (function createInputElements() {

                                var regexRule = rules.regexRule,
                                    valueTypeRule = rules.valueTypeRule,
                                    requiredRule = rules.requiredRule,
                                    readOnlyRule = rules.readOnlyRule,
                                    type = getInputType(valueTypeRule),
                                    _value,
                                    isDate = type.indexOf('date') > -1;

                                if (type === 'textarea') {
                                    innerElement = angular.element('<textarea class="form-control">');
                                    // 如果是 html 就加个特殊样式用来便于外观控制
                                    if (valueTypeRule === VALUE_TYPES.HTML)
                                        innerElement.addClass('s-html');
                                } else {
                                    innerElement = angular.element('<input class="form-control">').attr('type', type);
                                }

                                innerElement.attr('name', name);

                                bindBoolRule(innerElement, readOnlyRule, 'readOnlyRule', 'readonly');
                                bindBoolRule(innerElement, requiredRule, 'requiredRule', 'required');

                                bindInputLengthRule(innerElement, rules);

                                // 处理正则规则
                                if (regexRule) {

                                    if (regexRule instanceof DependentRule) {
                                        // 如果是依赖类型
                                        // 则如果需要, 则赋值正则, 否则为空。为空时将总是验证通过(即不验证)
                                        innerElement.attr('ng-pattern', 'rules.regexRule.getRegex()');

                                    } else if (regexRule.value !== 'yyyy-MM-dd') {
                                        // 如果是日期格式验证就不需要了
                                        // type=date 时 angular 会验证的
                                        innerElement.attr('pattern', regexRule.value);
                                    }
                                }

                                innerElement.attr('title', field.name || field.id);

                                //ng-trim="false"
                                innerElement.attr('ng-trim',false);

                                // 根据类型转换值类型, 并填值
                                _value = field.value;
                                field.value = getInputValue(_value, field, valueTypeRule);

                                // 没有填值, 并且有默认值, 那么就使用默认值
                                // 之所以不和上面的转换赋值合并, 是因为 getInputValue 有可能转换返回 null
                                // 所以这里要单独判断
                                if (!(field.value) && exists(field.defaultValue)) {
                                    _value = field.defaultValue;
                                    field.value = getInputValue(_value, field, valueTypeRule);
                                }

                                if (isDate) {
                                    // 将转换后的值放在特定的变量上, 供前端绑定
                                    // 将老格式的值还原回字段对象中
                                    // 当强类型的值变动, 就同步更新字段值
                                    scope.dateValue = field.value;
                                    field.value = _value;
                                    innerElement.attr('ng-model', 'dateValue');
                                    scope.$watch('dateValue', function (newDate) {
                                        field.value = $filter('date')(newDate, (type === 'date' ? 'yyyy-MM-dd' : 'yyyy-MM-dd hh:mm:ss'));
                                    });
                                } else {
                                    innerElement.attr('ng-model', 'field.value');
                                }

                                if ((!readOnlyRule || readOnlyRule instanceof DependentRule) && isDate) {
                                    // 日期类型的输入框要追加一个按钮, 用来触发 popup picker
                                    // 并且 readonly 时, 要把这个按钮隐藏掉
                                    var inputGroup = angular.element('<div class="input-group">');
                                    var inputGroupBtn = angular.element('<span class="input-group-btn"><button type="button" class="btn btn-default" ng-click="$opened = !$opened"><i class="glyphicon glyphicon-calendar"></i></button>');

                                    innerElement.attr('uib-datepicker-popup', '');
                                    innerElement.attr('is-open', '$opened');

                                    if (readOnlyRule instanceof DependentRule) {
                                        inputGroupBtn.attr('ng-if', '!rules.readOnlyRule.checked()')
                                    }

                                    inputGroup.append(innerElement);
                                    inputGroup.append(inputGroupBtn);

                                    innerElement = inputGroup;
                                }
                            })();
                            break;
                        case FIELD_TYPES.SINGLE_CHECK:
                            (function createSelectElements() {

                                var nullValueObj, foundValueObject;

                                var requiredRule = rules.requiredRule,
                                    options = field.options,
                                    valueObject = field.value;

                                // 要对 option 匹配, 就需要强类型转换
                                // 但是并不能直接修改 field 上的 options, 否则会导致后端!!爆炸!!
                                // 所以要克隆新的出来使用
                                options = options.map(function (option) {
                                    var newOption = {};
                                    newOption.__proto__ = option;
                                    newOption.value = getInputValue(option.value, field);
                                    return newOption;
                                });

                                if (!valueObject) {
                                    valueObject = {value: null};
                                } else if (exists(valueObject.value)) {
                                    // 如果 value 的值是一些原始值类型, 如数字那么可能需要转换处理
                                    // 所以这一步做额外的处理
                                    valueObject.value = getInputValue(valueObject.value, field);

                                    foundValueObject = _.find(options, function (optionItem) {
                                        return optionItem.value == valueObject.value;
                                    });

                                    valueObject.value = foundValueObject ? foundValueObject.value : valueObject.value = null;
                                }

                                // 处理默认值, 判断基本同 input 类型, 参见 input 中的注释
                                if (!exists(valueObject.value) && exists(field.defaultValue)) {
                                    valueObject.value = getInputValue(field.defaultValue, field);
                                }

                                if (!requiredRule) {
                                    // 非必填, 就创建空选项
                                    options.unshift(nullValueObj = {
                                        displayName: 'Select...',
                                        value: null
                                    });

                                    // 如果当前的选中值也木有, 就用这个默认的
                                    if (!valueObject.value) {
                                        valueObject = nullValueObj;
                                    }
                                }

                                // 最终保存到 $scope 上, 供页面绑定使用
                                scope.$options = options;
                                field.value = valueObject;

                                innerElement = angular.element('<select class="form-control" chosen>');
                                innerElement.attr('ng-options', 'option.value as option.displayName for option in $options');
                                innerElement.attr('name', name);
                                innerElement.attr('ng-model', 'field.value.value');
                                innerElement.attr('width', '"100%"');
                                innerElement.attr('search-contains',true);
                                innerElement.attr('title', field.name || field.id);

                                bindBoolRule(innerElement, requiredRule, 'requiredRule', 'required');
                                bindBoolRule(innerElement, rules.readOnlyRule, 'readOnlyRule', 'readonly');
                            })();
                            break;
                        case FIELD_TYPES.MULTI_CHECK:
                            (function createCheckboxElements() {

                                var selected, valueStringList;
                                var requiredRule = rules.requiredRule;
                                var defaultValues = field.defaultValues;

                                innerElement = [];

                                // 创建用于记录每个多选框选中状态的对象
                                selected = scope.selected = [];

                                // 通过事件触发 update 来操作 field 的 values 数组
                                scope.update = function (index) {

                                    // 获取选中值
                                    var selectedValue = field.options[index].value;

                                    // 获取选中的值, 在选中值集合里的位置
                                    var selectedIndex = findIndex(field.values, function (valueObj) {
                                        return valueObj.value == selectedValue;
                                    });

                                    if (scope.selected[index]) {
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
                                            checkbox.attr('ng-required', 'rules.requiredRule.checked() && !field.values.length');
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
                                    } else if (requiredRule && !!defaultValues.length) {
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

                            scope.$fields = field.fields;

                            innerElement = angular.element('<s-complex fields="$fields">');

                            break;
                        case FIELD_TYPES.MULTI_COMPLEX:

                            // multiComplex 字段, 其值不同于 complex 字段, 是存在于 complexValues 中。
                            // 存在 complexValues 中, 每一组的 fieldMap 的 field 的 value 中。
                            // 所以需要根据每个 complexValues 来创建 container

                            var complexValues = field.complexValues;

                            if (!complexValues) complexValues = [];

                            if (!complexValues.length) {
                                // 如果获取的值里没有内容, 就创建一套默认
                                complexValues.push(new ComplexValue(field.fields));
                            } else {
                                // 包装原有的 value, 便于后续使用
                                complexValues = complexValues.map(function (complexValueObj) {
                                    return new ComplexValue().copyFrom(complexValueObj, field);
                                });
                            }

                            field.complexValues = complexValues;

                            scope.$complexValues = complexValues;

                            innerElement = angular.element('<s-complex multi="true" fields="complexValue.fieldMap">');

                            innerElement.attr('ng-repeat', 'complexValue in $complexValues');

                            if (schemaFieldController.canAdd) {
                                element.append(angular.element('<s-toolbar>'));
                            }

                            break;
                        default:
                            element.text('不支持的类型');
                            break;
                    }

                    if (innerElement instanceof Array)
                        each(innerElement, function (childElement) {
                            element.append(childElement);
                        });
                    else
                        element.append(innerElement);

                    $compile(element.contents())(scope);
                }
            }
        })

        .directive('sComplex', function ($compile) {

            function SchemaComplexController($scope, $attrs) {
                this.$scope = $scope;
                this.fields = $scope.$eval($attrs.fields);
            }

            SchemaComplexController.prototype.$render = function (schema, isMulti, $element) {

                var controller = this,
                    fields = controller.fields,
                    $scope = controller.$scope,
                    repeater = controller.repeater;

                if (repeater) {
                    repeater.destroy();
                    controller.repeater = null;
                }

                if (!fields)
                    return;

                repeater = new FieldRepeater(fields, schema, $scope, $element, $compile);

                controller.repeater = repeater;

                repeater.renderList();

                // 这里偷个懒, 直接在 scope 找
                // schemaFieldController.canAdd
                if (isMulti && $scope.schemaFieldController.canAdd) {
                    var toolbox = angular.element('<s-toolbox>');
                    $element.append(toolbox);
                    $compile(toolbox)($scope);
                }
            };

            return {
                restrict: 'E',
                scope: true,
                require: ['^^?schema', '^^sField'],
                controllerAs: 'schemaComplexController',
                link: function ($scope, $element, $attrs, requiredControllers) {

                    var controller = $scope.schemaComplexController;

                    var isMulti = ($attrs.multi === 'true');

                    var schemaController = requiredControllers[0];

                    if (schemaController) {
                        controller.$render(schemaController.getSchema(), isMulti, $element);
                    } else {
                        controller.$render(null, isMulti, $element);
                    }
                },
                controller: SchemaComplexController
            };
        })

        .directive('sToolbar', function () {
            return {
                restrict: 'E',
                require: ['^^sField'],
                template: '<button class="btn btn-schema btn-success" ng-click="$newComplexValue()"><i class="fa fa-plus"></i></button>',
                scope: false,
                link: function ($scope) {
                    $scope.$newComplexValue = function () {
                        $scope.$complexValues.push(new ComplexValue($scope.field.fields));
                    };
                }
            };
        })

        .directive('sToolbox', function () {
            return {
                restrict: 'E',
                require: ['^^sField', '^^sComplex'],
                template: '<button class="btn btn-schema btn-danger" ng-click="schemaFieldController.remove(complexValue)" ng-if="$complexValues.length > 1"><i class="fa fa-trash-o"></i></button>',
                scope: false
            };
        });
}());