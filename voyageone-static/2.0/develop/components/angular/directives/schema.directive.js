(function () {
    /**
     * @Description:
     *
     * @User: linanbin
     * @Version: 2.0.0, 15/12/24
     */
    var fieldTypes = {
        INPUT: "INPUT",
        DATE: "DATE",
        DATETIME: "DATETIME",
        TEXTAREA: "TEXTAREA",
        SINGLE_CHECK: "SINGLECHECK",
        // 在complex中的显示select,以外的默认显示singlecheck,如果用户觉得select显示不方便,就将该field的type改成radio
        RADIO: "RADIO",
        MULTI_INPUT: "MULTIINPUT",
        // 没有被使用
        MULTI_CHECK: "MULTICHECK",
        COMPLEX: "COMPLEX",
        // TODO
        MULTI_COMPLEX: "MULTICOMPLEX",
        LABEL: "LABEL"
    }, ruleTypes = {
        VALUE_TYPE_RULE: "valueTypeRule",
        REQUIRED_RULE: "requiredRule",
        DISABLE_RULE: "disableRule",
        READ_ONLY_RULE: "readOnlyRule",
        REGEX_RULE: "regexRule",
        SET_RULE: "setRule",
        // TODO 暂时不知道怎么用
        TIP_RULE: "tipRule",
        // TODO 不需要处理
        DEV_TIP_RULE: "devTipRule",
        // 不需要处理
        MIN_LENGTH_RULE: "minLengthRule",
        MAX_LENGTH_RULE: "maxLengthRule",
        MIN_VALUE_RULE: "minValueRule",
        MAX_VALUE_RULE: "maxValueRule",
        MIN_INPUT_NUM_RULE: "minInputNumRule",
        MAX_INPUT_NUM_RULE: "maxInputNumRule",
        MIN_DECIMAL_DIGITS_RULE: "minDecimalDigitsRule",
        // TODO
        MAX_DECIMAL_DIGITS_RULE: "maxDecimalDigitsRule",
        // TODO
        MIN_TARGET_SIZE_RULE: "minTargetSizeRule",
        // TODO
        MAX_TARGET_SIZE_RULE: "maxTargetSizeRule",
        // TODO
        MIN_IMAGE_SIZE_RULE: "minImageSizeRule",
        // TODO
        MAX_IMAGE_SIZE_RULE: "maxImageSizeRule"
    }, valueTypes = {
        TEXT: "text",
        DECIMAL: "decimal",
        INTEGER: "integer",
        LONG: "long",
        DATE: "date",
        TIME: "time",
        URL: "url",
        TEXTAREA: "textarea",
        HTML: "html"
    };
    var SchemaHeader, Schema;
    SchemaHeader = function (config) {
        this.config = config || {
                isRequired: false,
                isMultiComplex: false,
                isComplex: false,
                tipMsg: []
            };
    };
    SchemaHeader.prototype = {
        isRequired: function (value) {
            return value !== undefined ? this.config.isRequired = value : this.config.isRequired;
        },
        isComplex: function (value) {
            return value !== undefined ? this.config.isComplex = value : this.config.isComplex;
        },
        isMultiComplex: function (value) {
            return value !== undefined ? this.config.isMultiComplex = value : this.config.isMultiComplex;
        },
        tipMsg: function (value) {
            return value !== undefined ? this.config.tipMsg.push(value) : this.config.tipMsg;
        }
    };
    Schema = function (config) {
        this.config = config || {
                type: null,
                name: null,
                rowNum: null,
                isRequired: false,
                checkValues: [],
                tipMsg: [],
                html: [],
                notShowEdit: true
            };
    };
    Schema.prototype = {
        type: function (value) {
            return value !== undefined ? this.config.type = value : this.config.type;
        },
        name: function (value) {
            return value !== undefined ? this.config.name = value : this.config.name;
        },
        html: function (value) {
            return value !== undefined ? this.config.html.push(value) : this.config.html.join(" ");
        },
        isRequired: function (value) {
            return value !== undefined ? this.config.isRequired = value : this.config.isRequired;
        },
        rowNum: function (value) {
            return value !== undefined ? this.config.rowNum = value : this.config.rowNum;
        },
        tipMsg: function (value) {
            return value !== undefined ? this.config.tipMsg.push(value) : this.config.tipMsg;
        },
        notShowEdit: function (value) {
            return value !== undefined ? this.config.notShowEdit = value : this.config.notShowEdit;
        }
    };
    angular.module("voyageone.angular.directives.schema", []).directive("schemaHeader", function (templates) {
        return {
            restrict: "E",
            replace: true,
            transclude: true,
            templateUrl: templates.schema.header.url,
            scope: {
                $data: "=data"
            },
            link: function (scope) {
                var header = new SchemaHeader();
                var field = scope.$data;
                // 标记特殊类型的 Field
                switch (field.type) {
                    case fieldTypes.MULTI_COMPLEX:
                        header.isMultiComplex(true);
                        break;

                    case fieldTypes.COMPLEX:
                        header.isComplex(true);
                        break;
                }
                // 标记提供了显示支持的规则
                angular.forEach(field.rules, function (rule) {
                    switch (rule.name) {
                        case ruleTypes.REQUIRED_RULE:
                            header.isRequired("true" == rule.value);
                            break;

                        case ruleTypes.TIP_RULE:
                            header.tipMsg(rule.value);
                            break;
                    }
                });
                scope.header = angular.copy(header.config);
                /**
                 * 设置multi complex添加一条新记录
                 * @param data
                 */
                scope.addField = function (data) {
                    var newFieldMap = {};
                    angular.forEach(data.fields, function (field) {
                        newFieldMap[field.id] = field;
                    });
                    data.complexValues.push({
                        fieldMap: angular.copy(newFieldMap)
                    });
                };
            }
        };
    }).directive("schemaItem", function ($compile, templates) {
        var schemas = templates.schema;
        return {
            restrict: "E",
            require: ["^?form"],
            scope: {
                $data: "=data",
                $hastip: "=hastip",
                $complex: "=complex",
                $notShowEdit: "=notShowEdit"
            },
            controller: function () {
            },
            link: function (scope, element) {
                // 监视配置变动
                scope.$watch("$data", refresh);
                scope.$watch("validForm.$valid", function ($valid) {
                    scope.$data.$valid = $valid;
                });
                function refresh() {
                    var schema = new Schema();
                    var field = scope.$data;
                    // 设置空间name
                    schema.name(field.id);
                    // 设置edit是否显示
                    schema.notShowEdit(scope.$notShowEdit == undefined ? false : scope.$notShowEdit);
                    schema.type(field.type);
                    switch (field.type) {
                        case fieldTypes.RADIO:
                            if (scope.$complex) schema.type(fieldTypes.SINGLE_CHECK);
                            break;

                        case fieldTypes.MULTI_CHECK:
                            field.options.forEach(function (option) {
                                option.selected = field.values.some(function (value) {
                                    return value.value === option.value;
                                });
                            });
                            break;

                        case fieldTypes.MULTI_COMPLEX:
                            field.complexValues = _resetMultiComplex(field);
                            break;

                        case fieldTypes.COMPLEX:
                            _resetComplex(field);
                            break;
                    }
                    angular.forEach(field.rules, function (rule) {
                        switch (rule.name) {
                            case ruleTypes.VALUE_TYPE_RULE:
                                _valueTypeRule(rule);
                                break;

                            case ruleTypes.REQUIRED_RULE:
                                _requiredRule(rule);
                                break;

                            case ruleTypes.DISABLE_RULE:
                                _disableRule(rule);
                                break;

                            case ruleTypes.READ_ONLY_RULE:
                                _readOnlyRule(rule);
                                break;

                            case ruleTypes.REGEX_RULE:
                                _regexRule(rule);
                                break;

                            case ruleTypes.TIP_RULE:
                                _tipRule(rule);
                                break;

                            case ruleTypes.MIN_LENGTH_RULE:
                                _minLengthRule(rule);
                                break;

                            case ruleTypes.MAX_LENGTH_RULE:
                                _maxLengthRule(rule);
                                break;

                            case ruleTypes.MIN_VALUE_RULE:
                                _minValueRule(rule);
                                break;

                            case ruleTypes.MAX_VALUE_RULE:
                                _maxValueRule(rule);
                                break;

                            case ruleTypes.MIN_INPUT_NUM_RULE:
                                _minInputNumRule(rule);
                                break;

                            case ruleTypes.MAX_INPUT_NUM_RULE:
                                _maxInputNumRule(rule);
                                break;
                        }
                    });
                    getTemplate().getHtml().then(function (html) {
                        if (schema.tipMsg() != null && scope.$hastip) return schemas.multiComplex_tip.getHtml().then(function (tipHtml) {
                            compileTemplate(html + tipHtml);
                        });
                        compileTemplate(html);
                    });
                    function compileTemplate(html) {
                        // 如果有验证属性, 追加这些属性, 并包裹 ng-form
                        var validAttrs = schema.html();
                        if (validAttrs) {
                            html = html.replace(/validators/g, validAttrs);
                            // 包裹 ng-form, 用于启用 angular 的验证功能
                            html = '<ng-form name="validForm">' + html + "</ng-form>";
                            // 追加错误信息的显示
                            html += '<div ng-repeat="(k, v) in validForm.$error">{{k}}</div>';
                        }
                        scope.schema = angular.copy(schema.config);
                        element.html($compile(html)(scope));
                        // 如果有验证, 这里将正确返回
                        // 否则, validForm 将是 null
                        field.form = scope.validForm;
                    }

                    /**
                     * 当多选(checkbox/multi_check) field 的值变更时, 同步更新 values 数组
                     * @param option 当前变更的目标选项
                     */
                    scope.changed = function (option) {
                        var values = field.values;
                        // 首次只需一个
                        if (!values && option.selected) {
                            field.values = [{
                                id: null,
                                value: option.value
                            }];
                            return;
                        }
                        // 提前计算 index
                        var index = values.findIndex(function (valWrap) {
                            return valWrap.value === option.value;
                        });
                        // 选中并且没有, 就添加
                        // 没选中并且有, 就删除
                        if (option.selected && index < 0) values.push({
                            id: null,
                            value: option.value
                        }); else if (!option.selected && index > -1) values.splice(index, 1);
                    };
                    /**
                     * 设置multi complex删除该条记录
                     * @param index
                     */
                    scope.delField = function (index) {
                        if(field.complexValues.length == 1){
                            for(var attr in field.complexValues[0].fieldMap){
                                if(attr.indexOf("image") >= 0)
                                    field.complexValues[0].fieldMap[attr].value = "";
                            }
                        }
                        else
                            field.complexValues.splice(index, 1);
                    };
                    function getTemplate() {
                        switch (schema.type()) {
                            case fieldTypes.INPUT:
                                return schemas.input;

                            case fieldTypes.DATE:
                                return schemas.date;

                            case fieldTypes.DATETIME:
                                return schemas.datetime;

                            case fieldTypes.TEXTAREA:
                                return schemas.textarea;

                            case fieldTypes.SINGLE_CHECK:
                                return schemas.select;

                            case fieldTypes.RADIO:
                                return schemas.radio;

                            case fieldTypes.MULTI_CHECK:
                                return schemas.checkbox;

                            case fieldTypes.LABEL:
                                return schemas.label;

                            case fieldTypes.MULTI_COMPLEX:
                                return schemas.multiComplex;

                            case fieldTypes.COMPLEX:
                                return scope.$complex ? schemas.multi_in_complex : schemas.complex;

                            default:
                                return null;
                        }
                    }

                    /**
                     * 重新设置 multi complex 的一些属性
                     * @param data 一个 multi complex 类型的 field
                     * @private
                     */
                    function _resetMultiComplex(data) {
                        var tempValues = [];
                        var tempFieldMap;
                        data.complexValues.forEach(function (value) {
                            tempFieldMap = {};
                            data.fields.forEach(function (field) {
                                var tempField = angular.copy(field);
                                var defValue = value.fieldMap[field.id];
                                if (defValue) switch (field.type) {
                                    case fieldTypes.INPUT:
                                    case fieldTypes.LABEL:
                                    case fieldTypes.DATE:
                                    case fieldTypes.DATETIME:
                                    case fieldTypes.TEXTAREA:
                                    case fieldTypes.SINGLE_CHECK:
                                    case fieldTypes.RADIO:
                                        tempField.value = defValue.value;
                                        break;

                                    case fieldTypes.MULTI_INPUT:
                                    case fieldTypes.MULTI_CHECK:
                                        tempField.values = defValue.values;
                                        break;

                                    case fieldTypes.COMPLEX:
                                        tempField.complexValue = defValue.complexValue;
                                        break;

                                    case fieldTypes.MULTI_COMPLEX:
                                        tempField.complexValues = defValue.complexValues;
                                        break;
                                }
                                tempFieldMap[field.id] = tempField;
                            });
                            tempValues.push({
                                fieldMap: tempFieldMap
                            });
                        });
                        // 如果values为空,默认添加空白行
                        if (_.isEmpty(data.complexValues)) {
                            var newFieldMap = {};
                            data.fields.forEach(function (field) {
                                newFieldMap[field.id] = field;
                            });
                            tempValues.push({
                                fieldMap: newFieldMap
                            });
                        }
                        return tempValues;
                    }

                    /**
                     * 为 data 的所有子 field 设置 value
                     * @param data 一个 complex 类型 field
                     * @private
                     */
                    function _resetComplex(data) {
                        if (!data || !data.fields || !data.fields.length) return;
                        data.fields.forEach(function (field) {
                            var defValue;
                            switch (field.type) {
                                case fieldTypes.INPUT:
                                case fieldTypes.LABEL:
                                case fieldTypes.DATE:
                                case fieldTypes.DATETIME:
                                case fieldTypes.TEXTAREA:
                                case fieldTypes.SINGLE_CHECK:
                                case fieldTypes.RADIO:
                                    if (!_.isEmpty(data.complexValue.fieldMap)) defValue = data.complexValue.fieldMap[field.id]; else defValue = data.defaultComplexValue.fieldMap[field.id];
                                    if (defValue) field.value = defValue.value;
                                    break;

                                case fieldTypes.MULTI_INPUT:
                                case fieldTypes.MULTI_CHECK:
                                    if (!_.isEmpty(data.complexValue.fieldMap)) field.values = data.complexValue.fieldMap[field.id].values; else field.values = data.defaultComplexValue.fieldMap[field.id].values;
                                    break;

                                case fieldTypes.COMPLEX:
                                    if (!_.isEmpty(data.complexValue.fieldMap) && !_.isUndefined(data.complexValue.fieldMap[field.id])) {
                                        field.complexValue = data.complexValue.fieldMap[field.id].complexValue;
                                    } else if (!_.isEmpty(data.defaultComplexValue.fieldMap) && !_.isUndefined(data.defaultComplexValue.fieldMap[field.id])) {
                                        field.complexValue = data.defaultComplexValue.fieldMap[field.id].complexValue;
                                    }
                                    break;

                                case fieldTypes.MULTI_COMPLEX:
                                    if (!_.isEmpty(data.complexValue.fieldMap) && !_.isUndefined(data.complexValue.fieldMap[field.id])) {
                                        field.complexValues = data.complexValue.fieldMap[field.id].complexValues;
                                    } else if (!_.isEmpty(data.defaultComplexValue.fieldMap) && !_.isUndefined(data.defaultComplexValue.fieldMap[field.id])) {
                                        field.complexValues = data.defaultComplexValue.fieldMap[field.id].complexValues;
                                    }
                                    break;
                            }
                        });
                    }

                    /**
                     * 处理valueTypeRule
                     * @param valueTypeRule
                     * @private
                     */
                    function _valueTypeRule(valueTypeRule) {
                        switch (valueTypeRule.value) {
                            case valueTypes.TEXT:
                            case valueTypes.DECIMAL:
                            case valueTypes.INTEGER:
                            case valueTypes.LONG:
                                schema.type(fieldTypes.INPUT);
                                schema.html('type="text"');
                                break;

                            case valueTypes.DATE:
                                schema.type(fieldTypes.DATE);
                                break;

                            case valueTypes.TIME:
                                schema.type(fieldTypes.DATETIME);
                                break;

                            case valueTypes.URL:
                                schema.type(fieldTypes.INPUT);
                                schema.html('type="url"');
                                break;

                            case valueTypes.TEXTAREA:
                                schema.type(fieldTypes.TEXTAREA);
                                schema.rowNum(4);
                                break;

                            case valueTypes.HTML:
                                schema.type(fieldTypes.TEXTAREA);
                                schema.rowNum(10);
                                break;
                        }
                    }

                    /**
                     * 检查必填属性, 并添加必填验证标识
                     * @param requiredRule
                     * @private
                     */
                    function _requiredRule(requiredRule) {
                        // 如果是普通类型, 就使用默认的 required 标识
                        // 如果是多选框(checkbox),需要使用定制的 ng-required
                        if (requiredRule.value !== "true") return;
                        schema.isRequired(true);
                        schema.html(field.type === fieldTypes.MULTI_CHECK ? 'ng-required="!$data.values.length"' : "required");
                    }

                    /**
                     * 处理disableRule
                     * @param disableRule
                     * @private
                     */
                    function _disableRule(disableRule) {
                        if ("true" == disableRule.value && disableRule.dependGroup == null) {
                            schema.html('ng-disabled="true"');
                        }
                    }

                    /**
                     * 处理readOnlyRule
                     * @param readOnlyRule
                     * @private
                     */
                    function _readOnlyRule(readOnlyRule) {
                        if ("true" == readOnlyRule.value) {
                            schema.html("readonly");
                        }
                    }

                    /**
                     * 处理regexRule
                     * @param regexRule
                     * @private
                     */
                    function _regexRule(regexRule) {
                        schema.html('ng-pattern="/' + regexRule.value + '/"');
                    }

                    /**
                     * 处理tipRule
                     * @param tipRule
                     * @private
                     */
                    function _tipRule(tipRule) {
                        schema.tipMsg(tipRule.value);
                    }

                    /**
                     * 处理minLengthRule
                     * @param minLengthRule
                     * @private
                     */
                    function _minLengthRule(minLengthRule) {
                        var value = isNaN(parseInt(minLengthRule.value)) ? 0 : minLengthRule.value;
                        if ("not include" === minLengthRule.exProperty) value = value > 0 ? value - 1 : 0;
                        if ("character" == minLengthRule.unit) schema.html('ng-minlength="' + value + '"'); else schema.html('ng-char-minlength="' + value + '"');
                    }

                    /**
                     * 处理maxLengthRule
                     * @param maxLengthRule
                     * @private
                     */
                    function _maxLengthRule(maxLengthRule) {
                        var value = isNaN(parseInt(maxLengthRule.value)) ? 0 : maxLengthRule.value;
                        if ("not include" === maxLengthRule.exProperty) value = value > 0 ? value - 1 : 0;
                        if ("character" == maxLengthRule.unit) schema.html('ng-maxlength="' + value + '"'); else schema.html('ng-char-maxlength="' + value + '"');
                    }

                    /**
                     * 处理minValueRule
                     * @param minValueRule
                     * @private
                     */
                    function _minValueRule(minValueRule) {
                        var value = isNaN(parseFloat(minValueRule.value)) ? 0 : parseFloat(minValueRule.value);
                        if ("not include" === minValueRule.exProperty) value = value > 0 ? value - .01 : 0;
                        schema.html('ng-minvalue="' + value + '"');
                    }

                    /**
                     * 处理maxValueRule
                     * @param maxValueRule
                     * @private
                     */
                    function _maxValueRule(maxValueRule) {
                        var value = isNaN(parseFloat(maxValueRule.value)) ? 0 : parseFloat(maxValueRule.value);
                        if ("not include" === maxValueRule.exProperty) value = value > 0 ? value - .01 : 0;
                        schema.html('ng-maxvalue="' + value + '"');
                    }

                    /**
                     * 处理minInputNumRule
                     * @param minInputNumRule
                     * @private
                     */
                    function _minInputNumRule(minInputNumRule) {
                        var value = isNaN(parseInt(minInputNumRule.value)) ? 0 : parseInt(minInputNumRule.value);
                        if ("not include" === minInputNumRule.exProperty) value = value > 0 ? value - 1 : 0;
                        schema.html('ng-mininputnum="' + value + '"');
                    }

                    /**
                     * 处理maxInputNumRule
                     * @param maxInputNumRule
                     * @private
                     */
                    function _maxInputNumRule(maxInputNumRule) {
                        var value = isNaN(parseInt(maxInputNumRule.value)) ? 0 : parseInt(maxInputNumRule.value);
                        if ("not include" === maxInputNumRule.exProperty) value = value > 0 ? value - 1 : 0;
                        schema.html('ng-maxinputnum="' + value + '"');
                    }
                }
            }
        };
    });
})();