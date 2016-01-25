/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/24
 */

angular.module('voyageone.angular.directives.schema', [])
    // 定义fieldTypes
    .constant('fieldTypes', {
        INPUT: "INPUT",
        DATE: "DATE",
        DATETIME: "DATETIME",
        TEXTAREA: "TEXTAREA",
        SINGLE_CHECK: "SINGLECHECK",// 在complex中的显示select,以外的默认显示singlecheck,如果用户觉得select显示不方便,就将该field的type改成radio
        RADIO: "RADIO",
        MULTI_INPUT: "MULTIINPUT", // 没有被使用
        MULTI_CHECK: "MULTICHECK",
        COMPLEX: "COMPLEX", // TODO
        MULTI_COMPLEX: "MULTICOMPLEX",
        LABEL: "LABEL"  // 可以不显示
    })
    // 定义ruleTypes
    .constant('ruleTypes', {
        VALUE_TYPE_RULE: "valueTypeRule",
        REQUIRED_RULE: "requiredRule",
        DISABLE_RULE: "disableRule",
        READ_ONLY_RULE: "readOnlyRule",
        REGEX_RULE: "regexRule",
        SET_RULE: "setRule", // TODO 暂时不知道怎么用
        TIP_RULE: "tipRule", // TODO 不需要处理
        DEV_TIP_RULE: "devTipRule", // 不需要处理
        MIN_LENGTH_RULE: "minLengthRule",
        MAX_LENGTH_RULE: "maxLengthRule",
        MIN_VALUE_RULE: "minValueRule",
        MAX_VALUE_RULE: "maxValueRule",
        MIN_INPUT_NUM_RULE: "minInputNumRule",
        MAX_INPUT_NUM_RULE: "maxInputNumRule",
        MIN_DECIMAL_DIGITS_RULE: "minDecimalDigitsRule", // TODO
        MAX_DECIMAL_DIGITS_RULE: "maxDecimalDigitsRule", // TODO
        MIN_TARGET_SIZE_RULE: "minTargetSizeRule", // TODO
        MAX_TARGET_SIZE_RULE: "maxTargetSizeRule", // TODO
        MIN_IMAGE_SIZE_RULE: "minImageSizeRule", // TODO
        MAX_IMAGE_SIZE_RULE: "maxImageSizeRule" // TODO
    })
    // 定义valueTypes
    .constant('valueTypes', {
        TEXT: "text",
        DECIMAL: "decimal",
        INTEGER: "integer",
        LONG: "long",
        DATE: "date",
        TIME: "time",
        URL: "url",
        TEXTAREA: "textarea",
        HTML: "html"
    })

    .directive('schemaHeader', function ($templateCache, schemaHeaderFactory, fieldTypes, ruleTypes, valueTypes) {

        // 定义header
        var templateKey_header = "voyageone.angular.directives.schemaHeader.tpl.html";
        if (!$templateCache.get(templateKey_header)) {$templateCache.put(templateKey_header
            , '<div class="form-group">' +
              '  <label class="col-sm-2 control-label" ng-class="{\'vo_reqfield\': showHtmlData.isRequired}" ng-bind="$$data.name"></label>' +
              '  <div class="col-sm-8" ng-class="{\'modal-open\' : showHtmlData.isMultiComplex, \'hierarchy_main\': showHtmlData.isComplex}" ng-transclude></div>' +
              '  <div class="col-sm-2" ng-if="showHtmlData.isMultiComplex"><button class="btn btn-success" ng-click="addField($$data)"><i class="fa fa-plus"></i>{{\'BTN_COM_ADD\' | translate}}</button></div>' +
              '  <div class="row" ng-repeat="tipMsg in showHtmlData.tipMsg"><div class="col-sm-8 col-sm-offset-2 text-warnings"><i class="icon fa fa-bell-o"></i>&nbsp;{{tipMsg}}</div></div>' +
              '</div>');}

        return {
            restrict: "E",
            replace: true,
            transclude: true,
            templateUrl: templateKey_header,
            scope: {
                $$data: "=data"
            },
            link: function (scope) {
                var schemaHeader = new schemaHeaderFactory();

                _returnType (scope.$$data.type);
                _operateRule (scope.$$data.rules);
                scope.showHtmlData = angular.copy(schemaHeader.schemaHearInfo);

                /**
                 * 设置multi complex添加一条新记录
                 * @param data
                 */
                scope.addField= function (data) {
                    var newFieldMap = {};
                    angular.forEach(data.fields, function (field) {
                        newFieldMap[field.id] = field;
                        //eval("newFieldMap." + field.id + "=field");
                    });

                    data.complexValues.push({fieldMap: angular.copy(newFieldMap)});
                };

                /**
                 * 返回需要展示的页面样式
                 * @param type
                 * @param valueTypeRule
                 * @private
                 */
                function _returnType (type) {

                    switch (type) {
                        case fieldTypes.MULTI_COMPLEX:
                            schemaHeader.isMultiComplex(true);
                            break;
                        case fieldTypes.COMPLEX:
                            schemaHeader.isComplex(true);
                            break;
                    }
                }

                /**
                 * 处理rules
                 * @param rules
                 * @private
                 */
                function _operateRule (rules) {
                    angular.forEach(rules, function (rule) {
                        switch (rule.name) {
                            case ruleTypes.REQUIRED_RULE:
                                _requiredRule(rule);
                                break;
                            case ruleTypes.TIP_RULE:
                                _tipRule(rule);
                                break;
                        }
                    })

                }

                /**
                 * 处理requiredRule
                 * @param requiredRule
                 * @private
                 */
                function _requiredRule (requiredRule) {
                    if ("true" == requiredRule.value) {
                        schemaHeader.isRequired(true);
                    }
                }

                /**
                * 处理tipRule
                * @param tipRule
                * @private
                */
                function _tipRule (tipRule) {
                    schemaHeader.tipMsg(tipRule.value);
                }
            }
        }
    })

    .directive('schemaItem', function ($templateCache, $compile, schemaFactory, fieldTypes, ruleTypes, valueTypes) {

        // label
        var templateKey_label = "voyageone.angular.directives.schemaLabel.tpl.html";
        if (!$templateCache.get(templateKey_label)) {$templateCache.put(templateKey_label,
            '<input style="min-width: 150px; max-width: 250px;" type="text" readonly ng-model="vm.$$data.value" class="form-control inherited">');}

        // input
        var templateKey_input = "voyageone.angular.directives.schemaInput.tpl.html";
        if (!$templateCache.get(templateKey_input)) {$templateCache.put(templateKey_input,
            '<input style="min-width: 150px; max-width: 250px;" ng-model="vm.$$data.value" class="form-control inherited" replaceInfo>');}

        // data
        var templateKey_date = "voyageone.angular.directives.schemaDate.tpl.html";
        if (!$templateCache.get(templateKey_date)) {$templateCache.put(templateKey_date,
            '<div class="input-group" style="width: 180px;" ng-controller="datePickerCtrl"><input replaceInfo type="text" class="form-control" datepicker-popup="{{formatDate}}" ng-model="$parent.vm.$$data.value" date-model-format="{{formatDate}}" is-open="opened" datepicker-options="dateOptions" close-text="Close" /><span class="input-group-btn"><button replaceInfo type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button></span></div>');}

        // datetime
        var templateKey_datetime = "voyageone.angular.directives.schemaDatetime.tpl.html";
        if (!$templateCache.get(templateKey_datetime)) {$templateCache.put(templateKey_datetime,
            '<div class="input-group" style="width: 180px;" ng-controller="datePickerCtrl"><input replaceInfo type="text" class="form-control" datepicker-popup="{{formatDateTime}}" ng-model="$parent.vm.$$data.value" date-model-format="{{formatDateTime}}" is-open="opened" datepicker-options="dateOptions" close-text="Close" /><span class="input-group-btn"><button replaceInfo type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button></span></div>');}

        // textarea
        var templateKey_textarea = "voyageone.angular.directives.schemaTextarea.tpl.html";
        if (!$templateCache.get(templateKey_textarea)) {$templateCache.put(templateKey_textarea,
            '<textarea style="min-width: 150px; max-width: 650px;" class="form-control no-resize" ng-model="vm.$$data.value" rows="{{showHtmlData.rowNum}}" replaceInfo></textarea>');}

        // single check-select
        var templateKey_select = "voyageone.angular.directives.schemaSelect.tpl.html";
        if (!$templateCache.get(templateKey_select)) {$templateCache.put(templateKey_select,
            '<select style="min-width: 150px; max-width: 250px;" replaceInfo class="form-control" ng-model="vm.$$data.value.value" ng-options="option.value as option.displayName for option in vm.$$data.options"> <option value="">{{\'TXT_SELECT_NO_VALUE\' | translate}}</option></select>');}

        // single check-radio
        var templateKey_radio = "voyageone.angular.directives.schemaRadio.tpl.html";
        if (!$templateCache.get(templateKey_radio)) {$templateCache.put(templateKey_radio,
            '<label class="checkbox-inline c-radio" ng-repeat="option in vm.$$data.options"><input name="{{vm.$$data.id}}" type="radio" ng-value="option.value" ng-model="vm.$$data.value.value"><span class="fa fa-check"></span> {{option.displayName}}</label>');}

        // multi check-checkbox
        var templateKey_checkbox = "voyageone.angular.directives.schemaCheckbox.tpl.html";
        if (!$templateCache.get(templateKey_checkbox)) {$templateCache.put(templateKey_checkbox,
            '<label class="checkbox-inline c-checkbox" ng-repeat="option in vm.$$data.options"><input type="checkbox" ng-value="option.value" ng-click="checkboxValue(option.value)" ng-checked="isSelected(option.value)"><span class="fa fa-check"></span> {{option.displayName}}</label>');}

        // multi complex
        var templateKey_multiComplex = "voyageone.angular.directives.schemaMultiComplex.tpl.html";
        if (!$templateCache.get(templateKey_multiComplex)) {$templateCache.put(templateKey_multiComplex,
            '<table class="table text-center">' +
            '<thead><tr>' +
                //'<schema-header ng-repeat="field in vm.$$data.fields" data="field" is-complex="true"></schema-header>' +
            '<th ng-repeat="field in vm.$$data.fields" ng-class="{\'vo_reqfield\': showHtmlData.isRequired}" class="text-center" style="min-width: 180px;">{{field.name}}</th>' +
            '<th ng-if="!showHtmlData.notShowEdit" style="min-width: 60px;" class="text-center" translate="TXT_COM_EDIT"></th>' +
            '</tr></thead>' +
            '<tbody><tr ng-repeat="value in vm.$$data.complexValues">' +
            '<td class="text-left" ng-repeat="field in value.fieldMap"><div class="tableLayer"><p ng-if="field.type != \'COMPLEX\'">&nbsp;</p><p><schema-item data="field" hastip="true" complex="true"></schema-item></p></div></td>' +
            '<td ng-if="!showHtmlData.notShowEdit" style="min-width: 60px;"><button title="{\'BTN_COM_DELETE\' | translate}" class="btn btn-danger btn-xs" ng-click="delField($index)"><i class="fa  fa-trash-o"></i></button></td>' +
            '</tr></tbody>' +
            '</table>');}

        // complex
        var templateKey_complex = "voyageone.angular.directives.schemaComplex.tpl.html";
        if (!$templateCache.get(templateKey_complex)) {$templateCache.put(templateKey_complex,
            '<schema-header ng-repeat="field in vm.$$data.fields" data="field"><schema-item data="field" ></schema-item></schema-header>');}

        // complex
        var templateKey_multi_in_complex = "voyageone.angular.directives.schemaMultiInComplex.tpl.html";
        if (!$templateCache.get(templateKey_multi_in_complex)) {$templateCache.put(templateKey_multi_in_complex,
            '<div ng-repeat="field in vm.$$data.fields"><p ng-bind="field.name"></p><p><schema-item data="field" hastip="true" complex="true"></schema-item></p></div>');}

        // multi complex tip
        var templateKey_multiComplex_tip = "voyageone.angular.directives.schemaMultiComplexTip.tpl.html";
        if (!$templateCache.get(templateKey_multiComplex_tip)) {$templateCache.put(templateKey_multiComplex_tip,
            '<div class="text-warnings" ng-repeat="tipMsg in showHtmlData.tipMsg"><br><i class="icon fa fa-bell-o"></i>&nbsp;{{tipMsg}}</div>');}

        return {
            restrict: "E",
            require: ['^?form'],
            replace: true,
            bindToController: true,
            controllerAs: "vm",
            controller: function () {},
            scope: {
                $$data: "=data",
                $$hastip: "=hastip",
                $$complex: "=complex",
                $$notShowEdit: "=notShowEdit"
            },
            link: function (scope, element, ctrl, attr) {

                // 监视配置变动
                scope.$watch('vm.$$data', function () {
                    refresh ();
                });

                function refresh () {

                    var schema = new schemaFactory();
                    scope.vm.$$from = ctrl;

                    // 设置空间name
                    schema.name(scope.vm.$$data.id);

                    // 设置edit是否显示
                    schema.notShowEdit(scope.vm.$$notShowEdit == undefined ? false : scope.vm.$$notShowEdit);

                    _returnType (scope.vm.$$data.type);
                    _operateRule (scope.vm.$$data.rules);

                    var tempHtml = "";
                    // 拼接body
                    switch (schema.type()) {
                        case fieldTypes.INPUT:
                            tempHtml = $templateCache.get(templateKey_input).replace("replaceInfo", schema.html());
                            break;
                        case fieldTypes.DATE:
                            tempHtml = $templateCache.get(templateKey_date).replace("replaceInfo", schema.html()).replace("replaceInfo", schema.html());
                            break;
                        case fieldTypes.DATETIME:
                            tempHtml = $templateCache.get(templateKey_datetime).replace("replaceInfo", schema.html()).replace("replaceInfo", schema.html());
                            break;
                        case fieldTypes.TEXTAREA:
                            tempHtml = $templateCache.get(templateKey_textarea).replace("replaceInfo", schema.html());
                            break;
                        case fieldTypes.SINGLE_CHECK:
                            tempHtml = $templateCache.get(templateKey_select).replace("replaceInfo", schema.html());
                            break;
                        case fieldTypes.RADIO:
                            tempHtml = $templateCache.get(templateKey_radio).replace("replaceInfo", schema.html());
                            break;
                        case fieldTypes.MULTI_CHECK:
                            tempHtml = $templateCache.get(templateKey_checkbox).replace("replaceInfo", schema.html());
                            break;
                        case fieldTypes.LABEL:
                            tempHtml = $templateCache.get(templateKey_label);
                            break;
                        case fieldTypes.MULTI_COMPLEX:
                            tempHtml = $templateCache.get(templateKey_multiComplex);
                            break;
                        case fieldTypes.COMPLEX:
                            tempHtml = scope.vm.$$complex ? $templateCache.get(templateKey_multi_in_complex) : $templateCache.get(templateKey_complex);
                            break;
                    }

                    // 添加规则说明
                    if (schema.tipMsg() != null && scope.vm.$$hastip) {
                        tempHtml += $templateCache.get(templateKey_multiComplex_tip);
                    }
                    scope.showHtmlData = angular.copy(schema.schemaInfo());
                    element.html($compile(tempHtml)(scope));

                    /**
                     * 设置checkbox被选中的value值处理
                     * @param value
                     */
                    scope.checkboxValue = function (value) {
                        if (_.contains(scope.showHtmlData.checkValues, value)) {
                            scope.showHtmlData.checkValues.splice(_.indexOf(scope.showHtmlData.checkValues, value), 1);
                        } else {
                            scope.showHtmlData.checkValues.push(value);
                        }
                        scope.vm.$$data.values = [];
                        angular.forEach(scope.showHtmlData.checkValues, function (obj) {
                            scope.vm.$$data.values.push({id: null, value: obj});
                        })
                    };

                    /**
                     * 判断是否被选中
                     * @param value
                     */
                    scope.isSelected = function (value) {
                        return _.contains(scope.showHtmlData.checkValues, value)
                    };

                    /**
                     * 设置multi complex删除该条记录
                     * @param index
                     */
                    scope.delField = function (index) {
                        scope.vm.$$data.complexValues.splice(index, 1);
                    };

                    /**
                     * 返回需要展示的页面样式
                     * @param type
                     * @param valueTypeRule
                     * @private
                     */
                    function _returnType (type) {
                        schema.type(type);
                        switch (type) {
                            case fieldTypes.RADIO:
                                if (scope.vm.$$complex)
                                    schema.type(fieldTypes.SINGLE_CHECK);
                                break;
                            case fieldTypes.MULTI_CHECK:
                                _setCheckValues(scope.vm.$$data.values);
                                break;
                            case fieldTypes.MULTI_COMPLEX:
                                scope.vm.$$data.complexValues = _resetMultiComplex(scope.vm.$$data);
                                break;
                            case fieldTypes.COMPLEX:
                                _resetComplex(scope.vm.$$data);
                                break;
                        }
                    }

                    /**
                     * 设置checkvalues
                     * @param values
                     * @private
                     */
                    function _setCheckValues (values) {
                        if (values != undefined && values != null) {
                            angular.forEach(values, function (obj) {
                                schema.checkValues(obj.value);
                            })
                        }
                    }

                    /**
                     * 重新设置multicomplex的一些属性
                     * @param fields
                     * @private
                     */
                    function _resetMultiComplex (data) {
                        var tempValues = [];
                        angular.forEach(data.complexValues, function (value) {
                            var tempFieldMap = {};
                            angular.forEach(data.fields, function (field) {
                                var tempField = angular.copy(field);
                                if (value.fieldMap[field.id] != undefined) {
                                    switch (field.type) {
                                        case fieldTypes.INPUT:
                                        case fieldTypes.LABEL:
                                        case fieldTypes.DATE:
                                        case fieldTypes.DATETIME:
                                        case fieldTypes.TEXTAREA:
                                        case fieldTypes.SINGLE_CHECK:
                                        case fieldTypes.RADIO:
                                            tempField.value = value.fieldMap[field.id].value;
                                            break;
                                        case fieldTypes.MULTI_INPUT:
                                        case fieldTypes.MULTI_CHECK:
                                            tempField.values = value.fieldMap[field.id].values;
                                            break;
                                        case fieldTypes.COMPLEX:
                                            tempField.complexValue = value.fieldMap[field.id].complexValue;
                                            break;
                                        case fieldTypes.MULTI_COMPLEX:
                                            tempField.complexValues = value.fieldMap[field.id].complexValues;
                                            break;
                                    }
                                }
                                tempFieldMap[field.id] = tempField;
                            });
                            tempValues.push({fieldMap: angular.copy(tempFieldMap)});
                        });

                        // 如果values为空,默认添加空白行
                        if (_.isEmpty(data.complexValues)) {
                            var newFieldMap = {};
                            angular.forEach(data.fields, function (field) {
                                newFieldMap[field.id] = field;
                                //eval("newFieldMap." + field.id + "=field");
                            });

                            tempValues.push({fieldMap: angular.copy(newFieldMap)});
                        }

                        return tempValues;
                    }

                    /**
                     * 重新
                     * @param data
                     * @private
                     */
                    function _resetComplex (data) {
                        angular.forEach(data.fields, function (field) {
                            switch (field.type) {
                                case fieldTypes.INPUT:
                                case fieldTypes.LABEL:
                                case fieldTypes.DATE:
                                case fieldTypes.DATETIME:
                                case fieldTypes.TEXTAREA:
                                case fieldTypes.SINGLE_CHECK:
                                case fieldTypes.RADIO:
                                    if (!_.isEmpty(data.complexValue.fieldMap))
                                        field.value = data.complexValue.fieldMap[field.id].value;
                                    else
                                        field.value = data.defaultComplexValue.fieldMap[field.id].value;
                                    break;
                                case fieldTypes.MULTI_INPUT:
                                case fieldTypes.MULTI_CHECK:
                                    if (!_.isEmpty(data.complexValue.fieldMap))
                                        field.values = data.complexValue.fieldMap[field.id].values;
                                    else
                                        field.values = data.defaultComplexValue.fieldMap[field.id].values;
                                    break;
                                case fieldTypes.COMPLEX:
                                    if (!_.isEmpty(data.complexValue.fieldMap))
                                        field.complexValue = data.complexValue.fieldMap[field.id].complexValue;
                                    else
                                        field.complexValue = data.defaultComplexValue.fieldMap[field.id].complexValue;
                                    break;
                                case fieldTypes.MULTI_COMPLEX:
                                    if (!_.isEmpty(data.complexValue.fieldMap))
                                        field.complexValues = data.complexValue.fieldMap[field.id].complexValues;
                                    else
                                        field.complexValues = data.defaultComplexValue.fieldMap[field.id].complexValues;
                                    break;
                            }
                        });
                    }

                    /**
                     * 处理rules
                     * @param rules
                     * @private
                     */
                    function _operateRule (rules) {
                        angular.forEach(rules, function (rule) {
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
                        })
                    }

                    /**
                     * 处理valueTypeRule
                     * @param valueTypeRule
                     * @private
                     */
                    function _valueTypeRule (valueTypeRule) {
                        switch (valueTypeRule.value) {
                            case valueTypes.TEXT:
                            case valueTypes.DECIMAL:
                            case valueTypes.INTEGER:
                            case valueTypes.LONG:
                                schema.type(fieldTypes.INPUT);
                                schema.html("type=\"text\"");
                                break;
                            case valueTypes.DATE:
                                schema.type(fieldTypes.DATE);
                                break;
                            case valueTypes.TIME:
                                schema.type(fieldTypes.DATETIME);
                                break;
                            case valueTypes.URL:
                                schema.type(fieldTypes.INPUT);
                                schema.html("type=\"url\"");
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
                     * 处理requiredRule
                     * @param requiredRule
                     * @private
                     */
                    function _requiredRule (requiredRule) {
                        if ("true" == requiredRule.value) {
                            schema.isRequired(true);
                            schema.html("required");
                        }
                    }

                    /**
                     * 处理disableRule
                     * @param disableRule
                     * @private
                     */
                    function _disableRule (disableRule) {
                        if ("true" == disableRule.value
                            && disableRule.dependGroup == null) {
                            schema.html("ng-disabled=\"true\"");
                        }
                    }

                    /**
                     * 处理readOnlyRule
                     * @param readOnlyRule
                     * @private
                     */
                    function _readOnlyRule (readOnlyRule) {
                        if ("true" == readOnlyRule.value) {
                            schema.html("readonly");
                        }
                    }

                    /**
                     * 处理regexRule
                     * @param regexRule
                     * @private
                     */
                    function _regexRule (regexRule) {
                        schema.html("ng-pattern=\"/" + regexRule.value +"/\"");
                    }

                    /**
                     * 处理tipRule
                     * @param tipRule
                     * @private
                     */
                    function _tipRule (tipRule) {
                        schema.tipMsg(tipRule.value);
                    }

                    /**
                     * 处理minLengthRule
                     * @param minLengthRule
                     * @private
                     */
                    function _minLengthRule (minLengthRule) {
                        var value = isNaN(parseInt(minLengthRule.value)) ? 0 : minLengthRule.value;

                        if ("not include" === minLengthRule.exProperty)
                            value = (value > 0) ? value -1 : 0;

                        if ("character" == minLengthRule.unit)
                            schema.html("ng-minlength=\"" + value +"\"");
                        else
                            schema.html("ng-char-minlength=\"" + value +"\"");
                    }

                    /**
                     * 处理maxLengthRule
                     * @param maxLengthRule
                     * @private
                     */
                    function _maxLengthRule (maxLengthRule) {
                        var value = isNaN(parseInt(maxLengthRule.value)) ? 0 : maxLengthRule.value;

                        if ("not include" === maxLengthRule.exProperty)
                            value = (value > 0) ? value -1 : 0;

                        if ("character" == maxLengthRule.unit)
                            schema.html("ng-maxlength=\"" + value +"\"");
                        else
                            schema.html("ng-char-maxlength=\"" + value +"\"");
                    }

                    /**
                     * 处理minValueRule
                     * @param minValueRule
                     * @private
                     */
                    function _minValueRule (minValueRule) {
                        var value = isNaN(parseFloat(minValueRule.value)) ? 0 : parseFloat(minValueRule.value);

                        if ("not include" === minValueRule.exProperty)
                            value = (value > 0) ? value - 0.01 : 0;

                        schema.html("ng-minvalue=\"" + value +"\"");
                    }

                    /**
                     * 处理maxValueRule
                     * @param maxValueRule
                     * @private
                     */
                    function _maxValueRule (maxValueRule) {
                        var value = isNaN(parseFloat(maxValueRule.value)) ? 0 : parseFloat(maxValueRule.value);

                        if ("not include" === maxValueRule.exProperty)
                            value = (value > 0) ? value - 0.01 : 0;

                        schema.html("ng-maxvalue=\"" + value +"\"");

                    }

                    /**
                     * 处理minInputNumRule
                     * @param minInputNumRule
                     * @private
                     */
                    function _minInputNumRule (minInputNumRule) {
                        var value = isNaN(parseInt(minInputNumRule.value)) ? 0 : parseInt(minInputNumRule.value);

                        if ("not include" === minInputNumRule.exProperty)
                            value = (value > 0) ? value - 1 : 0;

                        schema.html("ng-mininputnum=\"" + value +"\"");
                    }

                    /**
                     * 处理maxInputNumRule
                     * @param maxInputNumRule
                     * @private
                     */
                    function _maxInputNumRule (maxInputNumRule) {
                        var value = isNaN(parseInt(maxInputNumRule.value)) ? 0 : parseInt(maxInputNumRule.value);

                        if ("not include" === maxInputNumRule.exProperty)
                            value = (value > 0) ? value - 1 : 0;

                        schema.html("ng-maxinputnum=\"" + value +"\"");
                    }

                    /**
                     * 如果checkbox被选中,返回被选中的value.
                     * eg.[{new: true, pending: false, approved: true}] -> [new, approved]
                     * @param object
                     * @returns {*}
                     */
                    //function _returnKey(object) {
                    //    var result = [];
                    //    angular.forEach(object, function(value, index) {
                    //        if (value) result.push(index);
                    //    });
                    //    return result;
                    //}
                }
            }
        }
    });
