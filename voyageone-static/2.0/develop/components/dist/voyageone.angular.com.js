define(function() {
  angular.module("voyageone.angular.directives.dateModelFormat", []).directive("dateModelFormat", [ "$filter", function($filter) {
    return {
      restrict: "A",
      require: "ngModel",
      link: function(scope, elem, attrs, ngModel) {
        ngModel.$parsers.push(function(viewValue) {
          return $filter("date")(viewValue, attrs.dateModelFormat || "yyyy-MM-dd HH:mm:ss");
        });
      }
    };
  } ]);
  angular.module("voyageone.angular.directives.enterClick", []).directive("enterClick", function() {
    return {
      restrict: "A",
      link: function(scope, elem, attr) {
        $(elem).keyup(function(e) {
          if (e.keyCode !== 13) return;
          var selectExp = attr.enterClick;
          var targetElem, handler = function() {
            targetElem.click();
          };
          try {
            targetElem = angular.element(selectExp);
          } catch (e) {
            targetElem = null;
          }
          if (!targetElem || !targetElem.length) {
            handler = function() {
              scope.$eval(selectExp);
            };
          } else if (targetElem.attr("disabled")) {
            return;
          }
          handler();
        });
      }
    };
  });
  angular.module("voyageone.angular.directives.fileStyle", []).directive("fileStyle", function() {
    return {
      restrict: "A",
      controller: [ "$scope", "$element", function($scope, $element) {
        var options = $element.data();
        options.classInput = $element.data("classinput") || options.classInput;
        if ($element.filestyle) {
          $element.filestyle(options);
        }
      } ]
    };
  });
  angular.module("voyageone.angular.directives.ifNoRows", []).directive("ifNoRows", [ "$templateCache", "$compile", function($templateCache, $compile) {
    var tempNoDataKey = "voyageone.angular.directives.ifNoRows.tpl.html";
    if (!$templateCache.get(tempNoDataKey)) {
      $templateCache.put(tempNoDataKey, '<div class="text-center text-hs" id="noData">\n' + '    <h4 class="text-vo"><i class="icon fa fa-warning"></i>&nbsp;{{\'TXT_COM_WARNING\' | translate}}</h4>\n' + "{{'TXT_COM_MSG_NO_DATE' | translate}}" + "</dv>");
    }
    return {
      restrict: "A",
      replace: false,
      scope: {
        $$data: "@ifNoRows"
      },
      link: function(scope, element) {
        scope.$parent.$watch(scope.$$data, function() {
          if (scope.$parent.$eval(scope.$$data) == 0) {
            element.find("#noData").remove();
            element.append($compile($templateCache.get(tempNoDataKey))(scope));
          } else {
            element.find("#noData").remove();
          }
        });
      }
    };
  } ]);
  angular.module("voyageone.angular.directives.uiNav", []).directive("uiNav", function() {
    return {
      restrict: "AC",
      link: function(scope, el) {
        var _window = $(window), _mb = 768, wrap = $(".app-aside"), next, backdrop = ".dropdown-backdrop";
        el.on("click", "a", function(e) {
          next && next.trigger("mouseleave.nav");
          var _this = $(this);
          _this.parent().siblings(".active").toggleClass("active");
          _this.next().is("ul") && _this.parent().toggleClass("active") && e.preventDefault();
          _this.next().is("ul") || _window.width() < _mb && $(".app-aside").removeClass("show off-screen");
        });
        el.on("mouseenter", "a", function(e) {
          next && next.trigger("mouseleave.nav");
          $("> .nav", wrap).remove();
          if (!$(".app-aside-fixed.app-aside-folded").length || _window.width() < _mb || $(".app-aside-dock").length) return;
          var _this = $(e.target), top, w_h = $(window).height(), offset = 50, min = 150;
          !_this.is("a") && (_this = _this.closest("a"));
          if (_this.next().is("ul")) {
            next = _this.next();
          } else {
            return;
          }
          _this.parent().addClass("active");
          top = _this.parent().position().top + offset;
          next.css("top", top);
          if (top + next.height() > w_h) {
            next.css("bottom", 0);
          }
          if (top + min > w_h) {
            next.css("bottom", w_h - top - offset).css("top", "auto");
          }
          next.appendTo(wrap);
          next.on("mouseleave.nav", function(e) {
            $(backdrop).remove();
            next.appendTo(_this.parent());
            next.off("mouseleave.nav").css("top", "auto").css("bottom", "auto");
            _this.parent().removeClass("active");
          });
          $(".smart").length && $('<div class="dropdown-backdrop"/>').insertAfter(".app-aside").on("click", function(next) {
            next && next.trigger("mouseleave.nav");
          });
        });
        wrap.on("mouseleave", function(e) {
          next && next.trigger("mouseleave.nav");
          $("> .nav", wrap).remove();
        });
      }
    };
  });
  angular.module("voyageone.angular.directives.schema", []).constant("fieldTypes", {
    INPUT: "INPUT",
    DATE: "DATE",
    DATETIME: "DATETIME",
    TEXTAREA: "TEXTAREA",
    SINGLE_CHECK: "SINGLECHECK",
    RADIO: "RADIO",
    MULTI_INPUT: "MULTIINPUT",
    MULTI_CHECK: "MULTICHECK",
    COMPLEX: "COMPLEX",
    MULTI_COMPLEX: "MULTICOMPLEX",
    LABEL: "LABEL"
  }).constant("ruleTypes", {
    VALUE_TYPE_RULE: "valueTypeRule",
    REQUIRED_RULE: "requiredRule",
    DISABLE_RULE: "disableRule",
    READ_ONLY_RULE: "readOnlyRule",
    REGEX_RULE: "regexRule",
    SET_RULE: "setRule",
    TIP_RULE: "tipRule",
    DEV_TIP_RULE: "devTipRule",
    MIN_LENGTH_RULE: "minLengthRule",
    MAX_LENGTH_RULE: "maxLengthRule",
    MIN_VALUE_RULE: "minValueRule",
    MAX_VALUE_RULE: "maxValueRule",
    MIN_INPUT_NUM_RULE: "minInputNumRule",
    MAX_INPUT_NUM_RULE: "maxInputNumRule",
    MIN_DECIMAL_DIGITS_RULE: "minDecimalDigitsRule",
    MAX_DECIMAL_DIGITS_RULE: "maxDecimalDigitsRule",
    MIN_TARGET_SIZE_RULE: "minTargetSizeRule",
    MAX_TARGET_SIZE_RULE: "maxTargetSizeRule",
    MIN_IMAGE_SIZE_RULE: "minImageSizeRule",
    MAX_IMAGE_SIZE_RULE: "maxImageSizeRule"
  }).constant("valueTypes", {
    TEXT: "text",
    DECIMAL: "decimal",
    INTEGER: "integer",
    LONG: "long",
    DATE: "date",
    TIME: "time",
    URL: "url",
    TEXTAREA: "textarea",
    HTML: "html"
  }).directive("schemaHeader", [ "$templateCache", "schemaHeaderFactory", "fieldTypes", "ruleTypes", "valueTypes", function($templateCache, schemaHeaderFactory, fieldTypes, ruleTypes, valueTypes) {
    var templateKey_header = "voyageone.angular.directives.schemaHeader.tpl.html";
    if (!$templateCache.get(templateKey_header)) {
      $templateCache.put(templateKey_header, '<div class="form-group">' + '  <label class="col-sm-2 control-label" ng-class="{\'vo_reqfield\': showHtmlData.isRequired}" ng-bind="$$data.name"></label>' + "  <div class=\"col-sm-8\" ng-class=\"{'modal-open' : showHtmlData.isMultiComplex, 'hierarchy_main': showHtmlData.isComplex}\" ng-transclude></div>" + '  <div class="col-sm-2" ng-if="showHtmlData.isMultiComplex"><button class="btn btn-success" ng-click="addField($$data)"><i class="fa fa-plus"></i>{{\'BTN_COM_ADD\' | translate}}</button></div>' + '  <div class="row" ng-repeat="tipMsg in showHtmlData.tipMsg"><div class="col-sm-8 col-sm-offset-2 text-warnings"><i class="icon fa fa-bell-o"></i>&nbsp;{{tipMsg}}</div></div>' + "</div>");
    }
    return {
      restrict: "E",
      replace: true,
      transclude: true,
      templateUrl: templateKey_header,
      scope: {
        $$data: "=data"
      },
      link: function(scope) {
        var schemaHeader = new schemaHeaderFactory();
        _returnType(scope.$$data.type);
        _operateRule(scope.$$data.rules);
        scope.showHtmlData = angular.copy(schemaHeader.schemaHearInfo);
        scope.addField = function(data) {
          var newFieldMap = {};
          angular.forEach(data.fields, function(field) {
            eval("newFieldMap." + field.id + "=field");
          });
          data.complexValues.push({
            fieldMap: angular.copy(newFieldMap)
          });
        };
        function _returnType(type) {
          switch (type) {
           case fieldTypes.MULTI_COMPLEX:
            schemaHeader.isMultiComplex(true);
            break;

           case fieldTypes.COMPLEX:
            schemaHeader.isComplex(true);
            break;
          }
        }
        function _operateRule(rules) {
          angular.forEach(rules, function(rule) {
            switch (rule.name) {
             case ruleTypes.REQUIRED_RULE:
              _requiredRule(rule);
              break;

             case ruleTypes.TIP_RULE:
              _tipRule(rule);
              break;
            }
          });
        }
        function _requiredRule(requiredRule) {
          if ("true" == requiredRule.value) {
            schemaHeader.isRequired(true);
          }
        }
        function _tipRule(tipRule) {
          schemaHeader.tipMsg(tipRule.value);
        }
      }
    };
  } ]).directive("schemaItem", [ "$templateCache", "$compile", "schemaFactory", "fieldTypes", "ruleTypes", "valueTypes", function($templateCache, $compile, schemaFactory, fieldTypes, ruleTypes, valueTypes) {
    var templateKey_label = "voyageone.angular.directives.schemaLabel.tpl.html";
    if (!$templateCache.get(templateKey_label)) {
      $templateCache.put(templateKey_label, '<input style="min-width: 150px; max-width: 250px;" type="text" readonly ng-model="vm.$$data.value" class="form-control inherited">');
    }
    var templateKey_input = "voyageone.angular.directives.schemaInput.tpl.html";
    if (!$templateCache.get(templateKey_input)) {
      $templateCache.put(templateKey_input, '<input style="min-width: 150px; max-width: 250px;" ng-model="vm.$$data.value" class="form-control inherited" replaceInfo>');
    }
    var templateKey_date = "voyageone.angular.directives.schemaDate.tpl.html";
    if (!$templateCache.get(templateKey_date)) {
      $templateCache.put(templateKey_date, '<div class="input-group" style="width: 180px;" ng-controller="datePickerCtrl"><input replaceInfo type="text" class="form-control" datepicker-popup="{{formatDate}}" ng-model="$parent.vm.$$data.value" date-model-format="{{formatDate}}" is-open="opened" datepicker-options="dateOptions" close-text="Close" /><span class="input-group-btn"><button replaceInfo type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button></span></div>');
    }
    var templateKey_datetime = "voyageone.angular.directives.schemaDatetime.tpl.html";
    if (!$templateCache.get(templateKey_datetime)) {
      $templateCache.put(templateKey_datetime, '<div class="input-group" style="width: 180px;" ng-controller="datePickerCtrl"><input replaceInfo type="text" class="form-control" datepicker-popup="{{formatDateTime}}" ng-model="$parent.vm.$$data.value" date-model-format="{{formatDateTime}}" is-open="opened" datepicker-options="dateOptions" close-text="Close" /><span class="input-group-btn"><button replaceInfo type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button></span></div>');
    }
    var templateKey_textarea = "voyageone.angular.directives.schemaTextarea.tpl.html";
    if (!$templateCache.get(templateKey_textarea)) {
      $templateCache.put(templateKey_textarea, '<textarea style="min-width: 150px; max-width: 650px;" class="form-control no-resize" ng-model="vm.$$data.value" rows="{{showHtmlData.rowNum}}" replaceInfo></textarea>');
    }
    var templateKey_select = "voyageone.angular.directives.schemaSelect.tpl.html";
    if (!$templateCache.get(templateKey_select)) {
      $templateCache.put(templateKey_select, '<select style="min-width: 150px; max-width: 250px;" replaceInfo class="form-control" ng-model="vm.$$data.value.value" ng-options="option.value as option.displayName for option in vm.$$data.options"> <option value="">{{\'TXT_SELECT_NO_VALUE\' | translate}}</option></select>');
    }
    var templateKey_radio = "voyageone.angular.directives.schemaRadio.tpl.html";
    if (!$templateCache.get(templateKey_radio)) {
      $templateCache.put(templateKey_radio, '<label class="checkbox-inline c-radio" ng-repeat="option in vm.$$data.options"><input name="{{vm.$$data.id}}" type="radio" ng-value="option.value" ng-model="vm.$$data.value.value"><span class="fa fa-check"></span> {{option.displayName}}</label>');
    }
    var templateKey_checkbox = "voyageone.angular.directives.schemaCheckbox.tpl.html";
    if (!$templateCache.get(templateKey_checkbox)) {
      $templateCache.put(templateKey_checkbox, '<label class="checkbox-inline c-checkbox" ng-repeat="option in vm.$$data.options"><input type="checkbox" ng-value="option.value" ng-click="checkboxValue(option.value)" ng-checked="isSelected(option.value)"><span class="fa fa-check"></span> {{option.displayName}}</label>');
    }
    var templateKey_multiComplex = "voyageone.angular.directives.schemaMultiComplex.tpl.html";
    if (!$templateCache.get(templateKey_multiComplex)) {
      $templateCache.put(templateKey_multiComplex, '<table class="table text-center">' + "<thead><tr>" + '<th ng-repeat="field in vm.$$data.fields" ng-class="{\'vo_reqfield\': showHtmlData.isRequired}" class="text-center" style="min-width: 180px;">{{field.name}}</th>' + '<th style="min-width: 60px;" class="text-center" translate="TXT_COM_EDIT"></th>' + "</tr></thead>" + '<tbody><tr ng-repeat="value in vm.$$data.complexValues">' + '<td class="text-left" ng-repeat="field in value.fieldMap"><schema-item data="field" hastip="true" complex="true"></schema-item></td>' + '<td style="min-width: 60px;"><button title="{\'BTN_COM_DELETE\' | translate}" class="btn btn-danger btn-xs" ng-click="delField($index)"><i class="fa  fa-trash-o"></i></button></td>' + "</tr></tbody>" + "</table>");
    }
    var templateKey_complex = "voyageone.angular.directives.schemaComplex.tpl.html";
    if (!$templateCache.get(templateKey_complex)) {
      $templateCache.put(templateKey_complex, '<schema-header ng-repeat="field in vm.$$data.fields" data="field"><schema-item data="field"></schema-item></schema-header>');
    }
    var templateKey_multiComplex_tip = "voyageone.angular.directives.schemaMultiComplexTip.tpl.html";
    if (!$templateCache.get(templateKey_multiComplex_tip)) {
      $templateCache.put(templateKey_multiComplex_tip, '<div class="text-warnings" ng-repeat="tipMsg in showHtmlData.tipMsg"><br><i class="icon fa fa-bell-o"></i>&nbsp;{{tipMsg}}</div>');
    }
    return {
      restrict: "E",
      require: [ "^?form" ],
      replace: true,
      bindToController: true,
      controllerAs: "vm",
      controller: function() {},
      scope: {
        $$data: "=data",
        $$hastip: "=hastip",
        $$complex: "=complex"
      },
      link: function(scope, element, ctrl) {
        var schema = new schemaFactory();
        scope.vm.$$from = ctrl;
        schema.name(scope.vm.$$data.id);
        _returnType(scope.vm.$$data.type);
        _operateRule(scope.vm.$$data.rules);
        var tempHtml = "";
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
          tempHtml = $templateCache.get(templateKey_complex);
          break;
        }
        if (schema.tipMsg() != null && scope.vm.$$hastip) {
          tempHtml += $templateCache.get(templateKey_multiComplex_tip);
        }
        scope.showHtmlData = angular.copy(schema.schemaInfo());
        element.html($compile(tempHtml)(scope));
        scope.checkboxValue = function(value) {
          if (_.contains(scope.showHtmlData.checkValues, value)) {
            scope.showHtmlData.checkValues.splice(_.indexOf(scope.showHtmlData.checkValues, value), 1);
          } else {
            scope.showHtmlData.checkValues.push(value);
          }
          scope.vm.$$data.values = [];
          angular.forEach(scope.showHtmlData.checkValues, function(obj) {
            scope.vm.$$data.values.push({
              id: null,
              value: obj
            });
          });
        };
        scope.isSelected = function(value) {
          return _.contains(scope.showHtmlData.checkValues, value);
        };
        scope.delField = function(index) {
          scope.vm.$$data.complexValues.splice(index, 1);
        };
        function _returnType(type) {
          schema.type(type);
          switch (type) {
           case fieldTypes.RADIO:
            if (scope.vm.$$complex) schema.type(fieldTypes.SINGLE_CHECK);
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
        function _setCheckValues(values) {
          if (values != undefined && values != null) {
            angular.forEach(values, function(obj) {
              schema.checkValues(obj.value);
            });
          }
        }
        function _resetMultiComplex(data) {
          var tempValues = [];
          angular.forEach(data.complexValues, function(value) {
            var tempFieldMap = {};
            angular.forEach(data.fields, function(field) {
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
            tempValues.push({
              fieldMap: angular.copy(tempFieldMap)
            });
          });
          if (_.isEmpty(data.complexValues)) {
            var newFieldMap = {};
            angular.forEach(data.fields, function(field) {
              eval("newFieldMap." + field.id + "=field");
            });
            tempValues.push({
              fieldMap: angular.copy(newFieldMap)
            });
          }
          return tempValues;
        }
        function _resetComplex(data) {
          angular.forEach(data.fields, function(field) {
            switch (field.type) {
             case fieldTypes.INPUT:
             case fieldTypes.LABEL:
             case fieldTypes.DATE:
             case fieldTypes.DATETIME:
             case fieldTypes.TEXTAREA:
             case fieldTypes.SINGLE_CHECK:
             case fieldTypes.RADIO:
              field.value = data.complexValue.fieldMap[field.id].value;
              break;

             case fieldTypes.MULTI_INPUT:
             case fieldTypes.MULTI_CHECK:
              field.values = data.complexValue.fieldMap[field.id].values;
              break;

             case fieldTypes.COMPLEX:
              field.complexValue = data.complexValue.fieldMap[field.id].complexValue;
              break;

             case fieldTypes.MULTI_COMPLEX:
              field.complexValues = data.complexValue.fieldMap[field.id].complexValues;
              break;
            }
          });
        }
        function _operateRule(rules) {
          angular.forEach(rules, function(rule) {
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
        }
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
        function _requiredRule(requiredRule) {
          if ("true" == requiredRule.value) {
            schema.isRequired(true);
            schema.html("required");
          }
        }
        function _disableRule(disableRule) {
          if ("true" == disableRule.value && disableRule.dependGroup == null) {
            schema.html('ng-disabled="true"');
          }
        }
        function _readOnlyRule(readOnlyRule) {
          if ("true" == readOnlyRule.value) {
            schema.html("readonly");
          }
        }
        function _regexRule(regexRule) {
          schema.html('ng-pattern="/' + regexRule.value + '/"');
        }
        function _tipRule(tipRule) {
          schema.tipMsg(tipRule.value);
        }
        function _minLengthRule(minLengthRule) {
          var value = isNaN(parseInt(minLengthRule.value)) ? 0 : minLengthRule.value;
          if ("not include" === minLengthRule.exProperty) value = value > 0 ? value - 1 : 0;
          if ("character" == minLengthRule.unit) schema.html('ng-minlength="' + value + '"'); else schema.html('ng-char-minlength="' + value + '"');
        }
        function _maxLengthRule(maxLengthRule) {
          var value = isNaN(parseInt(maxLengthRule.value)) ? 0 : maxLengthRule.value;
          if ("not include" === maxLengthRule.exProperty) value = value > 0 ? value - 1 : 0;
          if ("character" == maxLengthRule.unit) schema.html('ng-maxlength="' + value + '"'); else schema.html('ng-char-maxlength="' + value + '"');
        }
        function _minValueRule(minValueRule) {
          var value = isNaN(parseFloat(minValueRule.value)) ? 0 : parseFloat(minValueRule.value);
          if ("not include" === minValueRule.exProperty) value = value > 0 ? value - .01 : 0;
          schema.html('ng-minvalue="' + value + '"');
        }
        function _maxValueRule(maxValueRule) {
          var value = isNaN(parseFloat(maxValueRule.value)) ? 0 : parseFloat(maxValueRule.value);
          if ("not include" === maxValueRule.exProperty) value = value > 0 ? value - .01 : 0;
          schema.html('ng-maxvalue="' + value + '"');
        }
        function _minInputNumRule(minInputNumRule) {
          var value = isNaN(parseInt(minInputNumRule.value)) ? 0 : parseInt(minInputNumRule.value);
          if ("not include" === minInputNumRule.exProperty) value = value > 0 ? value - 1 : 0;
          schema.html('ng-mininputnum="' + value + '"');
        }
        function _maxInputNumRule(maxInputNumRule) {
          var value = isNaN(parseInt(maxInputNumRule.value)) ? 0 : parseInt(maxInputNumRule.value);
          if ("not include" === maxInputNumRule.exProperty) value = value > 0 ? value - 1 : 0;
          schema.html('ng-maxinputnum="' + value + '"');
        }
      }
    };
  } ]);
  angular.module("voyageone.angular.directives.voption", []).directive("voption", [ "$templateCache", "$compile", function($templateCache, $compile) {
    var templateKey_select = "voyageone.angular.directives.optionSelect.tpl.html";
    if (!$templateCache.get(templateKey_select)) {
      $templateCache.put(templateKey_select, '<select class="form-control" ng-model="$$data.value.value" ng-options="option.value as option.displayName for option in $$data.options"> <option value="">{{\'TXT_SELECT_NO_VALUE\' | translate}}</option></select>');
    }
    return {
      restrict: "E",
      replace: true,
      scope: {
        $$data: "=data"
      },
      link: function(scope, element) {
        var typeList = {
          SINGLE_CHECK: "SINGLECHECK"
        };
        scope.$watch("$$data", function() {
          refresh();
        }, true);
        function refresh() {
          var tempHtml;
          switch (scope.$$data.type) {
           case typeList.SINGLE_CHECK:
            tempHtml = $compile($templateCache.get(templateKey_select))(scope);
            break;
          }
          element.html(tempHtml);
        }
      }
    };
  } ]);
  angular.module("voyageone.angular.directives.vpagination", []).directive("vpagination", [ "$templateCache", "$compile", "vpagination", function($templateCache, $compile, vpagination) {
    var templateKey = "voyageone.angular.directives.pagination.tpl.html";
    var templateKeyNoData = "voyageone.angular.directives.paginationNoData.tpl.html";
    if (!$templateCache.get(templateKey)) {
      $templateCache.put(templateKey, '<div class="col-sm-2">\n' + '    <div class="page-main form-inline">{{\'TXT_COM_SHOWING_NO\' | translate}}&nbsp;<input class="text-center" type="text" ng-model="curr.pageNo"/>&nbsp;/&nbsp;{{totalPages}}&nbsp;{{\'TXT_COM_PAGE\' | translate}}&nbsp;' + '        <button class="btn btn-xs btn-default" type="button" ng-click="goPage(curr.pageNo)" translate="BTN_GO"></button>\n' + "    </div>\n" + "</div>\n" + '<div class="col-sm-7 text-center">\n' + "    <small class=\"text-muted inline m-t-sm m-b-sm\">{{'TXT_COM_SHOWING' | translate}}&nbsp;{{curr.start}}-{{curr.end}}&nbsp;{{'TXT_COM_OF' | translate}}&nbsp;{{totalItems}}&nbsp{{'TXT_COM_ITEMS' | translate}}</small>\n" + "</div>\n" + '<div class="col-sm-3 text-right text-center-xs"><div>' + '    <ul class="pagination-sm m-t-none m-b pagination ng-isolate-scope ng-valid ng-dirty ng-valid-parse">\n' + '        <li ng-class="{disabled: curr.isFirst ||ngDisabled}" class="pagination-first"><a href ng-click="goPage(1)" ng-disabled="curr.isFirst">&laquo;</a></li>\n' + '        <li ng-class="{disabled: curr.isFirst ||ngDisabled}" class="pagination-prev"><a href ng-click="goPage(curr.pageNo - 1)" ng-disabled="curr.isFirst">&lsaquo;</a></li>\n' + '        <li ng-if="curr.isShowStart" class="disabled" disabled><a href>...</a></li>\n' + '        <li ng-repeat="page in curr.pages track by $index" ng-class="{active: isCurr(page)}" class="pagination-page"><a href ng-click="goPage(page)">{{page}}</a></li>\n' + '        <li ng-if="curr.isShowEnd" class="disabled" disabled><a href>...</a></li>\n' + '        <li ng-class="{disabled: curr.isLast ||ngDisabled}" class="pagination-next"><a href ng-click="goPage(curr.pageNo + 1)" ng-disabled="curr.isLast">&rsaquo;</a></li>\n' + '        <li ng-class="{disabled: curr.isLast ||ngDisabled}" class="pagination-last"><a href ng-click="goPage(totalPages)" ng-disabled="curr.isLast">&raquo;</a></li>\n' + "    </ul>\n" + "</div>");
    }
    if (!$templateCache.get(templateKeyNoData)) {
      $templateCache.put(templateKeyNoData, '<div class="col-sm-7 col-sm-offset-2 text-center">\n' + "    <small class=\"text-muted inline m-t-sm m-b-sm\">{{'TXT_COM_SHOWING' | translate}}&nbsp;0-0&nbsp;{{'TXT_COM_OF' | translate}}&nbsp;0&nbsp{{'TXT_COM_ITEMS' | translate}}</small>\n" + "</div>");
    }
    var defConfig = {
      curr: 1,
      total: 0,
      size: 20,
      showPageNo: 5
    };
    return {
      restrict: "AE",
      replace: false,
      scope: {
        $$configNameForA: "@vpagination",
        $$configNameForE: "@config"
      },
      link: function(scope, element) {
        var userConfigName = scope.$$configNameForA || scope.$$configNameForE;
        var userConfig = scope.$parent.$eval(userConfigName);
        var userWithDefConfig = angular.extend({}, defConfig, userConfig);
        scope.config = angular.extend(userConfig, userWithDefConfig);
        var p = new vpagination(scope.config);
        scope.$parent.$watch(userConfigName, function() {
          refresh();
        }, true);
        scope.goPage = function(num) {
          p.goPage(isNaN(Number(num)) ? 1 : Number(num));
        };
        scope.isCurr = function(num) {
          return p.isCurr(num);
        };
        function refresh() {
          scope.totalPages = p.getPageCount();
          scope.totalItems = p.getTotal();
          scope.curr = p.getCurr();
          var tempHtml;
          if (p.getTotal() == 0) {
            tempHtml = $compile($templateCache.get(templateKeyNoData))(scope);
          } else {
            tempHtml = $compile($templateCache.get(templateKey))(scope);
          }
          element.html(tempHtml);
        }
      }
    };
  } ]);
  angular.module("voyageone.angular.directives.validator", []).directive("ngCharMaxlength", function() {
    return {
      restrict: "A",
      require: "?ngModel",
      link: function(scope, elm, attr, ctrl) {
        if (!ctrl) return;
        var maxlength = -1;
        attr.$observe("ngCharMaxlength", function(value) {
          var intVal = parseInt(value);
          maxlength = isNaN(intVal) ? -1 : intVal;
          ctrl.$validate();
        });
        ctrl.$validators.maxlength = function(modelValue, viewValue) {
          return maxlength < 0 || ctrl.$isEmpty(viewValue) || getByteLength(viewValue) <= maxlength;
        };
      }
    };
    function getByteLength(value) {
      var byteLen = 0, len = value.length;
      if (value) {
        for (var i = 0; i < len; i++) {
          if (value.charCodeAt(i) > 255) {
            byteLen += 2;
          } else {
            byteLen++;
          }
        }
        return byteLen;
      } else {
        return 0;
      }
    }
  }).directive("ngCharMinlength", function() {
    return {
      restrict: "A",
      require: "?ngModel",
      link: function(scope, elm, attr, ctrl) {
        if (!ctrl) return;
        var minlength = -1;
        attr.$observe("ngCharMinlength", function(value) {
          var intVal = parseInt(value);
          minlength = isNaN(intVal) ? -1 : intVal;
          ctrl.$validate();
        });
        ctrl.$validators.minlength = function(modelValue, viewValue) {
          return minlength < 0 || ctrl.$isEmpty(viewValue) || getByteLength(viewValue) >= minlength;
        };
      }
    };
    function getByteLength(value) {
      var byteLen = 0, len = value.length;
      if (value) {
        for (var i = 0; i < len; i++) {
          if (value.charCodeAt(i) > 255) {
            byteLen += 2;
          } else {
            byteLen++;
          }
        }
        return byteLen;
      } else {
        return 0;
      }
    }
  }).directive("ngMaxvalue", function() {
    return {
      restrict: "A",
      require: "?ngModel",
      link: function(scope, elm, attr, ctrl) {
        if (!ctrl) return;
        var maxvalue = -1;
        attr.$observe("ngMaxvalue", function(value) {
          if (/^(\d{4})\/(\d{1,2})\/(\d{1,2})$/.test(value)) maxvalue = new Date(value); else if (/^(\d+)(\.[0-9]{0,2})?$/.test(value)) maxvalue = isNaN(parseFloat(value)) ? -1 : parseFloat(value); else if (/^(\d+)$/.test(value)) maxvalue = isNaN(parseInt(value)) ? -1 : parseInt(value); else maxvalue = -1;
          ctrl.$validate();
        });
        ctrl.$validators.maxvalue = function(modelValue, viewValue) {
          return maxvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue <= maxvalue;
        };
      }
    };
  }).directive("ngMinvalue", function() {
    return {
      restrict: "A",
      require: "?ngModel",
      link: function(scope, elm, attr, ctrl) {
        if (!ctrl) return;
        var minvalue = -1;
        attr.$observe("ngMinvalue", function(value) {
          if (/^(\d{4})\/(\d{1,2})\/(\d{1,2})$/.test(value)) minvalue = new Date(value); else if (/^(\d+)(\.[0-9]{0,2})?$/.test(value)) minvalue = isNaN(parseFloat(value)) ? -1 : parseFloat(value); else if (/^(\d+)$/.test(value)) minvalue = isNaN(parseInt(value)) ? -1 : parseInt(value); else minvalue = -1;
          ctrl.$validate();
        });
        ctrl.$validators.minvalue = function(modelValue, viewValue) {
          return minvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue >= minvalue;
        };
      }
    };
  }).directive("ngMaxinputnum", function() {
    return {
      restrict: "A",
      require: "?ngModel",
      link: function(scope, elm, attr, ctrl) {
        if (!ctrl) return;
        var maxvalue = -1;
        attr.$observe("ngMaxinputvalue", function(value) {
          maxvalue = isNaN(parseInt(value)) ? -1 : parseInt(value);
          ctrl.$validate();
        });
        ctrl.$validators.maxinputnum = function(modelValue, viewValue) {
          return maxvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue.length <= maxvalue;
        };
      }
    };
  }).directive("ngMininputnum", function() {
    return {
      restrict: "A",
      require: "?ngModel",
      link: function(scope, elm, attr, ctrl) {
        if (!ctrl) return;
        var minvalue = -1;
        attr.$observe("ngMininputnum", function(value) {
          minvalue = isNaN(parseInt(value)) ? -1 : parseInt(value);
          ctrl.$validate();
        });
        ctrl.$validators.mininputnum = function(modelValue, viewValue) {
          return minvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue.length >= minvalue;
        };
      }
    };
  });
  angular.module("voyageone.angular.factories.dialogs", []).factory("$dialogs", [ "$modal", "$filter", "$templateCache", function($modal, $filter, $templateCache) {
    var templateName = "voyageone.angular.factories.dialogs.tpl.html";
    $templateCache.put(templateName, '<div class="vo_modal"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="close()"><span aria-hidden="true"><i ng-click="close()" class="fa fa-close"></i></span></button><h4 class="modal-title" ng-bind-html="title"></h4></div><div class="modal-body wrapper-lg"><div class="row"><p ng-bind-html="content"></p></div></div><div class="modal-footer"><button class="btn btn-default btn-sm" ng-if="!isAlert" ng-click="close()" translate="BTN_COM_CANCEL"></button><button class="btn btn-vo btn-sm" ng-click="ok()" translate="BTN_COM_OK"></button></div></div>');
    function tran(translationId, values) {
      return $filter("translate")(translationId, values);
    }
    return function(options) {
      if (!_.isObject(options)) throw "arg type must be object";
      var values;
      if (_.isObject(options.content)) {
        values = options.content.values;
        options.content = options.content.id;
      }
      options.title = tran(options.title);
      options.content = tran(options.content, values);
      var modalInstance = $modal.open({
        templateUrl: templateName,
        controller: [ "$scope", function(scope) {
          _.extend(scope, options);
        } ],
        size: "md"
      });
      options.close = function() {
        modalInstance.dismiss("close");
      };
      options.ok = function() {
        modalInstance.close("");
      };
      return modalInstance;
    };
  } ]).factory("alert", [ "$dialogs", function($dialogs) {
    return function(content, title) {
      return $dialogs({
        title: title || "TXT_COM_ALERT",
        content: content,
        isAlert: true
      });
    };
  } ]).factory("confirm", [ "$dialogs", function vConfirm($dialogs) {
    return function(content, title) {
      return $dialogs({
        title: title || "TXT_COM_CONFIRM",
        content: content,
        isAlert: false
      });
    };
  } ]);
  angular.module("voyageone.angular.factories.interceptor", []).factory("interceptorFactory", InterceptorFactory).config([ "$httpProvider", function($httpProvider) {
    $httpProvider.interceptors.push("interceptorFactory");
  } ]);
  function InterceptorFactory() {
    var UNKNOWN_CODE = "5";
    var CODE_SYS_REDIRECT = "SYS_REDIRECT";
    var MSG_TIMEOUT = "300001";
    function autoRedirect(res) {
      if (res.code != CODE_SYS_REDIRECT) {
        return false;
      }
      location.href = res.redirectTo || "/login.html";
      return true;
    }
    function sessionTimeout(res) {
      if (res.code != MSG_TIMEOUT) {
        return false;
      }
      location.href = "/login.html";
      return true;
    }
    function unknownException(response) {
      if (response.data.code !== UNKNOWN_CODE) {
        return;
      }
      window.$$lastUnknow = response;
      console.error("Server throw unknown exceptio. Message:", response.data.message);
    }
    return {
      request: function(config) {
        return config;
      },
      response: function(res) {
        var result = res.data;
        if (autoRedirect(result) || sessionTimeout(result)) {
          return res;
        }
        unknownException(res);
        return res;
      },
      requestError: function(config) {
        return config;
      },
      responseError: function(res) {}
    };
  }
  angular.module("voyageone.angular.factories.notify", []).factory("notify", [ "$filter", function($filter) {
    function notify(options) {
      if (!options) return;
      if (_.isString(options)) options = {
        message: options
      };
      if (!_.isObject(options)) return;
      var values;
      if (_.isObject(options.message)) {
        values = options.message.values;
        options.message = options.message.id;
      }
      options.message = $filter("translate")(options.message, values);
      return $.notify(options.message, options);
    }
    notify.success = function(message) {
      return notify({
        message: message,
        className: "success"
      });
    };
    notify.warning = function(message) {
      return notify({
        message: message,
        className: "warning"
      });
    };
    notify.danger = function(message) {
      return notify({
        message: message,
        className: "danger"
      });
    };
    return notify;
  } ]);
  angular.module("voyageone.angular.factories.schema", []).factory("schemaHeaderFactory", function() {
    return function(config) {
      var _schemaHeaderInfo = config ? config : {
        isRequired: false,
        isMultiComplex: false,
        isComplex: false,
        tipMsg: []
      };
      this.isRequired = function(value) {
        return value !== undefined ? _schemaHeaderInfo.isRequired = value : _schemaHeaderInfo.isRequired;
      };
      this.isComplex = function(value) {
        return value !== undefined ? _schemaHeaderInfo.isComplex = value : _schemaHeaderInfo.isComplex;
      };
      this.isMultiComplex = function(value) {
        return value !== undefined ? _schemaHeaderInfo.isMultiComplex = value : _schemaHeaderInfo.isMultiComplex;
      };
      this.tipMsg = function(value) {
        return value !== undefined ? _schemaHeaderInfo.tipMsg.push(value) : _schemaHeaderInfo.tipMsg;
      };
      this.schemaHearInfo = _schemaHeaderInfo;
    };
  }).factory("schemaFactory", function() {
    return function(config) {
      var _schemaInfo = config ? config : {
        type: null,
        name: null,
        rowNum: null,
        isRequired: false,
        checkValues: [],
        tipMsg: [],
        html: []
      };
      this.type = function(value) {
        return value !== undefined ? _schemaInfo.type = value : _schemaInfo.type;
      };
      this.name = function(value) {
        return value !== undefined ? _schemaInfo.name = value : _schemaInfo.name;
      };
      this.html = function(value) {
        return value !== undefined ? _schemaInfo.html.push(value) : htmlToString(_schemaInfo.html);
      };
      this.isRequired = function(value) {
        return value !== undefined ? _schemaInfo.isRequired = value : _schemaInfo.isRequired;
      };
      this.rowNum = function(value) {
        return value !== undefined ? _schemaInfo.rowNum = value : _schemaInfo.rowNum;
      };
      this.tipMsg = function(value) {
        return value !== undefined ? _schemaInfo.tipMsg.push(value) : _schemaInfo.tipMsg;
      };
      this.checkValues = function(value) {
        return value !== undefined ? _schemaInfo.checkValues.push(value) : _schemaInfo.checkValues;
      };
      this.schemaInfo = function() {
        return _schemaInfo;
      };
      function htmlToString(htmls) {
        var result = "";
        angular.forEach(htmls, function(html) {
          result += " " + html + " ";
        });
        return result;
      }
    };
  });
  angular.module("voyageone.angular.factories.selectRows", []).factory("selectRowsFactory", function() {
    return function(config) {
      var _selectRowsInfo = config ? config : {
        selAllFlag: false,
        currPageRows: [],
        selFlag: [],
        selList: []
      };
      this.selAllFlag = function(value) {
        return value !== undefined ? _selectRowsInfo.selAllFlag = value : _selectRowsInfo.selAllFlag;
      };
      this.clearCurrPageRows = function() {
        _selectRowsInfo.currPageRows = [];
      };
      this.currPageRows = function(value) {
        return value !== undefined ? _selectRowsInfo.currPageRows.push(value) : _selectRowsInfo.currPageRows;
      };
      this.selFlag = function(value) {
        return value !== undefined ? _selectRowsInfo.selFlag.push(value) : _selectRowsInfo.selFlag;
      };
      this.selList = function(value) {
        return value !== undefined ? _selectRowsInfo.selList.push(value) : _selectRowsInfo.selList;
      };
      this.selectRowsInfo = _selectRowsInfo;
    };
  });
  angular.module("voyageone.angular.factories.vpagination", []).factory("vpagination", function() {
    return function(config) {
      var _pages, _lastTotal = 0, _showPages = [];
      this.getTotal = function() {
        return config.total;
      };
      this.getCurr = function() {
        return {
          pageNo: curr(),
          start: getCurrStartItems(),
          end: getCurrEndItems(),
          isFirst: isFirst(),
          isLast: isLast(),
          pages: createShowPages(),
          isShowStart: isShowStart(),
          isShowEnd: isShowEnd()
        };
      };
      this.goPage = load;
      this.getPageCount = getPages;
      this.isCurr = isCurr;
      function load(page) {
        page = page || config.curr;
        if (page < 1 || page > getPages() || isCurr(page)) return;
        config.curr = page;
        config.fetch(page, config.size);
      }
      function createShowPages() {
        var minPage, maxPage, _showPages = [];
        if (config.curr < config.showPageNo) {
          minPage = 1;
          if (_pages <= config.showPageNo) maxPage = _pages; else maxPage = config.showPageNo;
        } else if (config.curr + 2 > _pages) {
          minPage = _pages + 1 - config.showPageNo;
          maxPage = _pages;
        } else {
          minPage = config.curr + 3 - config.showPageNo;
          maxPage = config.curr + 2;
        }
        for (var i = minPage; i <= maxPage; i++) {
          _showPages.push(i);
        }
        return _showPages;
      }
      function getPages() {
        if (_lastTotal != config.total) {
          _pages = parseInt(config.total / config.size) + (config.total % config.size > 0 ? 1 : 0);
          _lastTotal = config.total;
        }
        return _pages;
      }
      function getCurrStartItems() {
        return (config.curr - 1) * config.size + 1;
      }
      function getCurrEndItems() {
        var currEndItems = config.curr * config.size;
        return currEndItems <= config.total ? currEndItems : config.total;
      }
      function isLast() {
        return config.curr == getPages();
      }
      function isFirst() {
        return config.curr == 1;
      }
      function isCurr(page) {
        return config.curr == page;
      }
      function curr() {
        return config.curr;
      }
      function isShowStart() {
        _showPages = createShowPages();
        return _showPages[0] > 1;
      }
      function isShowEnd() {
        _showPages = createShowPages();
        return _showPages[_showPages.length - 1] < _pages;
      }
    };
  });
  angular.module("voyageone.angular.vresources", []).provider("$vresources", [ "$provide", function($provide) {
    function getActionUrl(root, action) {
      return root + (root.lastIndexOf("/") === root.length - 1 ? "" : "/") + action;
    }
    function closureDataService(name, actions, ajaxService) {
      function DataResource() {
        if (!actions) {
          return;
        }
        if (typeof actions !== "object") {
          console.log("Failed to new DataResource: [" + actions + "] is not a object");
          return;
        }
        if (!actions.root) {
          console.log("Failed to new DataResource: no root prop" + (JSON && JSON.stringify ? ": " + JSON.stringify(actions) : ""));
          return;
        }
        for (var name in actions) {
          if (name === "root") continue;
          if (actions.hasOwnProperty(name)) {
            this[name] = function(actionUrl) {
              return function(data) {
                return ajaxService.post(actionUrl, data);
              };
            }(getActionUrl(actions.root, actions[name]));
          }
        }
      }
      $provide.service(name, DataResource);
    }
    this.$get = [ "ajaxService", function(ajaxService) {
      return {
        register: function(name, actions) {
          if (!actions) return;
          if (typeof actions !== "object") return;
          if (actions.root) {
            closureDataService(name, actions, ajaxService);
            return;
          }
          for (var childName in actions) {
            if (actions.hasOwnProperty(childName)) {
              this.register(childName, actions[childName]);
            }
          }
        }
      };
    } ];
  } ]).run([ "$vresources", "$actions", function($vresources, $actions) {
    $vresources.register(null, $actions);
  } ]);
  angular.module("voyageone.angular.services.ajax", []).service("$ajax", $Ajax).service("ajaxService", AjaxService);
  function $Ajax($http, blockUI, $q) {
    this.$http = $http;
    this.blockUI = blockUI;
    this.$q = $q;
  }
  $Ajax.$inject = [ "$http", "blockUI", "$q" ];
  $Ajax.prototype.post = function(url, data) {
    var defer = this.$q.defer();
    this.$http.post(url, data).then(function(response) {
      var res = response.data;
      if (!res) {
        alert("相应结果不存在?????");
        defer.reject(null);
        return;
      }
      if (res.message || res.code) {
        defer.reject(res);
        return;
      }
      defer.resolve(res);
    }, function(response) {
      defer.reject(null, response);
    });
    return defer.promise;
  };
  function AjaxService($q, $ajax, messageService) {
    this.$q = $q;
    this.$ajax = $ajax;
    this.messageService = messageService;
  }
  AjaxService.$inject = [ "$q", "$ajax", "messageService" ];
  AjaxService.prototype.post = function(url, data) {
    var defer = this.$q.defer();
    this.$ajax.post(url, data).then(function(res) {
      defer.resolve(res);
      return res;
    }, function(_this) {
      return function(res) {
        _this.messageService.show(res);
        defer.reject(res);
        return res;
      };
    }(this));
    return defer.promise;
  };
  angular.module("voyageone.angular.services.cookie", []).service("cookieService", CookieService);
  var keys = {
    language: "voyageone.user.language",
    company: "voyageone.user.company",
    channel: "voyageone.user.channel",
    application: "voyageone.user.application"
  };
  function gentProps(key) {
    return function(val) {
      if (arguments.length === 1) {
        return this.set(key, val);
      } else if (arguments.length > 1) {
        return this.set(key, arguments);
      }
      return this.get(key);
    };
  }
  function CookieService($cookieStore) {
    this.$cookieStore = $cookieStore;
  }
  CookieService.$inject = [ "$cookieStore" ];
  CookieService.prototype.get = function(key) {
    var result = this.$cookieStore.get(key);
    return result == undefined || result == null ? "" : this.$cookieStore.get(key);
  };
  CookieService.prototype.set = function(key, value) {
    return this.$cookieStore.put(key, value);
  };
  CookieService.prototype.language = gentProps(keys.language);
  CookieService.prototype.company = gentProps(keys.company);
  CookieService.prototype.channel = gentProps(keys.channel);
  CookieService.prototype.application = gentProps(keys.application);
  CookieService.prototype.removeAll = function() {
    this.$cookieStore.remove(keys.language);
    this.$cookieStore.remove(keys.company);
    this.$cookieStore.remove(keys.channel);
    this.$cookieStore.remove(keys.application);
  };
  angular.module("voyageone.angular.services.message", []).service("messageService", MessageService);
  var DISPLAY_TYPES = {
    ALERT: 1,
    NOTIFY: 2,
    POP: 3,
    CUSTOM: 4
  };
  function MessageService(alert, confirm, notify) {
    this.alert = alert;
    this.confirm = confirm;
    this.notify = notify;
  }
  MessageService.$inject = [ "alert", "confirm", "notify" ];
  MessageService.prototype = {
    show: function(res) {
      var displayType = res.displayType;
      var message = res.message;
      switch (displayType) {
       case DISPLAY_TYPES.ALERT:
        this.alert(message);
        break;

       case DISPLAY_TYPES.NOTIFY:
        this.notify(message);
        break;

       case DISPLAY_TYPES.POP:
        this.notify({
          message: message,
          position: "right bottom"
        });
        break;
      }
    }
  };
  angular.module("voyageone.angular.services.permission", []).service("permissionService", PermissionService);
  function PermissionService($rootScope) {
    this.$rootScope = $rootScope;
    this.permissions = [];
  }
  PermissionService.$inject = [ "$rootScope" ];
  PermissionService.prototype = {
    setPermissions: function(permissions) {
      this.permissions = permissions;
      this.$rootScope.$broadcast("permissionsChanged");
    },
    has: function(permission) {
      return _.contains(this.permissions, permission.trim());
    }
  };
  angular.module("voyageone.angular.services.translate", []).service("translateService", TranslateService);
  function TranslateService($translate) {
    this.$translate = $translate;
  }
  TranslateService.$inject = [ "$translate" ];
  TranslateService.prototype = {
    languages: {
      en: "en",
      zh: "zh"
    },
    setLanguage: function(language) {
      if (!_.contains(this.languages, language)) {
        language = this.getBrowserLanguage();
      }
      this.$translate.use(language);
      return language;
    },
    getBrowserLanguage: function() {
      var currentLang = navigator.language;
      if (!currentLang) currentLang = navigator.browserLanguage;
      return currentLang.substr(0, 2);
    }
  };
  angular.module("voyageone.angular.controllers.datePicker", []).controller("datePickerCtrl", [ "$scope", function($scope) {
    var vm = this;
    vm.formats = [ "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" ];
    $scope.formatDate = vm.formats[0];
    $scope.formatDateTime = vm.formats[1];
    $scope.open = open;
    function open($event) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope.opened = true;
    }
  } ]);
  angular.module("voyageone.angular.controllers.selectRows", []).controller("selectRowsCtrl", [ "$scope", function($scope) {
    $scope.selectAll = selectAll;
    $scope.selectOne = selectOne;
    $scope.isAllSelected = isAllSelected;
    function selectAll(objectList, id) {
      objectList.selAllFlag = !objectList.selAllFlag;
      if (!id) {
        id = "id";
      }
      angular.forEach(objectList.currPageRows, function(object) {
        objectList.selFlag[object[id]] = objectList.selAllFlag;
        if (objectList.hasOwnProperty("selList")) {
          var tempList = _.pluck(objectList.selList, id);
          if (objectList.selAllFlag && tempList.indexOf(object[id]) < 0) {
            objectList.selList.push(object);
          } else if (!objectList.selAllFlag && tempList.indexOf(object[id]) > -1) {
            objectList.selList.splice(tempList.indexOf(object[id]), 1);
          }
        }
      });
    }
    function selectOne(currentId, objectList, id) {
      if (!id) {
        id = "id";
      }
      if (objectList.hasOwnProperty("selList")) {
        angular.forEach(objectList.currPageRows, function(object) {
          var tempList = _.pluck(objectList.selList, id);
          if (_.isEqual(object[id], currentId)) {
            if (tempList.indexOf(object[id]) > -1) {
              objectList.selList.splice(tempList.indexOf(object[id]), 1);
            } else {
              objectList.selList.push(object);
            }
          }
        });
      }
      objectList.selAllFlag = true;
      tempList = _.pluck(objectList.selList, id);
      angular.forEach(objectList.currPageRows, function(object) {
        if (tempList.indexOf(object[id]) == -1) {
          objectList.selAllFlag = false;
        }
      });
    }
    function isAllSelected(objectList, id) {
      if (!id) {
        id = "id";
      }
      if (objectList != undefined) {
        objectList.selAllFlag = true;
        var tempList = _.pluck(objectList.selList, id);
        angular.forEach(objectList.currPageRows, function(object) {
          if (tempList.indexOf(object[id]) == -1) {
            objectList.selAllFlag = false;
          }
        });
        return objectList.selAllFlag;
      }
      return false;
    }
  } ]);
  angular.module("voyageone.angular.controllers.showPopover", []).controller("showPopoverCtrl", [ "$scope", function($scope) {
    $scope.showInfo = showInfo;
    function showInfo(values) {
      var tempHtml = "";
      angular.forEach(values, function(data, index) {
        tempHtml += data;
        if (index !== values.length) {
          tempHtml += "<br>";
        }
      });
      return tempHtml;
    }
  } ]);
  angular.module("voyageone.angular.controllers", [ "voyageone.angular.controllers.datePicker", "voyageone.angular.controllers.selectRows", "voyageone.angular.controllers.showPopover" ]);
  angular.module("voyageone.angular.directives", [ "voyageone.angular.directives.dateModelFormat", "voyageone.angular.directives.enterClick", "voyageone.angular.directives.fileStyle", "voyageone.angular.directives.ifNoRows", "voyageone.angular.directives.uiNav", "voyageone.angular.directives.schema", "voyageone.angular.directives.voption", "voyageone.angular.directives.vpagination", "voyageone.angular.directives.validator" ]);
  angular.module("voyageone.angular.factories", [ "voyageone.angular.factories.dialogs", "voyageone.angular.factories.interceptor", "voyageone.angular.factories.notify", "voyageone.angular.factories.schema", "voyageone.angular.factories.selectRows", "voyageone.angular.factories.vpagination" ]);
  angular.module("voyageone.angular.services", [ "voyageone.angular.services.ajax", "voyageone.angular.services.cookie", "voyageone.angular.services.message", "voyageone.angular.services.permission", "voyageone.angular.services.translate" ]);
  return angular.module("voyageone.angular", [ "voyageone.angular.controllers", "voyageone.angular.directives", "voyageone.angular.factories", "voyageone.angular.services" ]);
});
//# sourceMappingURL=voyageone.angular.com.js.map
