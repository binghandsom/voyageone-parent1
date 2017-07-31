(function($) {
    if (!$) {
        console.warn("jQuery is not defined");
        return;
    }
    function newId() {
        return "DOWN" + Math.random().toString().substr(2, 5);
    }
    function createIframe(newId) {
        return $("<iframe>").attr({
            id: newId,
            name: newId,
            style: "display:none"
        });
    }
    $.download = {
        post: function post(url, param, callback, context) {
            var id = newId();
            var $form = $("<form>").attr({
                action: url,
                method: "POST",
                style: "display:none",
                target: id
            });
            var $hiddens = $.map(param, function(val, name) {
                return $('<input type="hidden">').attr({
                    id: name,
                    name: name,
                    value: val
                });
            });
            var $frame = createIframe(id);
            $("body").append($form.append($hiddens), $frame);
            $frame.on("load", function() {
                var responseContent = $frame.contents().find("body").text();
                if (callback) callback(responseContent, param, context);
                $form.remove();
                $frame.remove();
            });
            $form.submit();
        },
        get: function get(url, param, callback, context) {
            var id = newId();
            var $frame = createIframe(id);
            $("body").append($frame);
            $frame.on("load", function() {
                var responseContent = $frame.contents().find("body").text();
                if (callback) callback(responseContent, param, context);
                $frame.remove();
            });
            $frame[0].contentWindow.href = url + "?" + $.param(param);
        }
    };
})(window["jQuery"]);

angular.module("voyageone.angular.controllers", []);

angular.module("voyageone.angular.directives", []);

angular.module("voyageone.angular.factories", []);

angular.module("voyageone.angular.services", []);

angular.module("voyageone.angular.filter", []);

angular.module("voyageone.angular", [ "voyageone.angular.controllers", "voyageone.angular.directives", "voyageone.angular.factories", "voyageone.angular.services", "voyageone.angular.filter" ]);

angular.module("voyageone.angular.controllers").controller("selectRowsCtrl", [ "$scope", function($scope) {
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
                if (tempList) {
                    if (objectList.selAllFlag && tempList.indexOf(object[id]) < 0) {
                        objectList.selList.push(object);
                    } else if (!objectList.selAllFlag && tempList.indexOf(object[id]) > -1) {
                        objectList.selList.splice(tempList.indexOf(object[id]), 1);
                    }
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
                    if (tempList && tempList.indexOf(object[id]) > -1) {
                        objectList.selList.splice(tempList.indexOf(object[id]), 1);
                    } else {
                        objectList.selList.push(object);
                    }
                }
            });
        }
        objectList.selAllFlag = true;
        var tempList = _.pluck(objectList.selList, id);
        angular.forEach(objectList.currPageRows, function(object) {
            if (tempList && tempList.indexOf(object[id]) == -1) {
                objectList.selAllFlag = false;
            }
        });
    }
    function isAllSelected(objectList, id) {
        if (!id) {
            id = "id";
        }
        if (objectList) {
            objectList.selAllFlag = true;
            var tempList = _.pluck(objectList.selList, id);
            if (objectList.currPageRows && objectList.currPageRows.length === 0) {
                return false;
            }
            angular.forEach(objectList.currPageRows, function(object) {
                if (tempList && tempList.indexOf(object[id]) == -1) {
                    objectList.selAllFlag = false;
                }
            });
            return objectList.selAllFlag;
        }
        return false;
    }
} ]);

angular.module("voyageone.angular.controllers").controller("showPopoverCtrl", [ "$scope", "$searchAdvanceService2", "$promotionHistoryService", function($scope, $searchAdvanceService2, $promotionHistoryService) {
    $scope.templateAction = {
        promotionDetailPopover: {
            templateUrl: "promotionDetailTemplate.html",
            title: "Title"
        },
        advanceSkuPopover: {
            templateUrl: "advanceSkuTemplate.html",
            title: "Title"
        }
    };
    $scope.showInfo = showInfo;
    $scope.popoverAdvanceSku = popoverAdvanceSku;
    $scope.popoverPromotionDetail = popoverPromotionDetail;
    $scope.getCartQty = getCartQty;
    function showInfo(values) {
        if (values == undefined || values == "") {
            return "";
        }
        var tempHtml = "";
        if (values instanceof Array) {
            angular.forEach(values, function(data, index) {
                tempHtml += data;
                if (index !== values.length) {
                    tempHtml += "<br>";
                }
            });
        } else if (values.isUseComplexTemplate == true) {
            $scope.dynamicPopover = {
                type: values.type,
                value1: values.value,
                value2: values.value2,
                value3: values.value3,
                templateUrl: "dynamicPopoverTemplate.html"
            };
        } else {
            tempHtml += values;
        }
        return tempHtml;
    }
    function popoverAdvanceSku(code, skus, entity) {
        if (entity.isOpen) {
            entity.isOpen = false;
            return;
        }
        entity.isOpen = true;
        $searchAdvanceService2.getSkuInventory(code).then(function(resp) {
            var skuDetails = [], skuInventories = resp.data;
            _.forEach(skus, function(sku) {
                var inventory = 0;
                _.forEach(skuInventories, function(skuInventory) {
                    if (skuInventory.sku.toLowerCase() == sku.skuCode.toLowerCase()) {
                        inventory = skuInventory.qtyChina;
                        return false;
                    }
                });
                skuDetails.push({
                    skuCode: sku.skuCode,
                    size: sku.size,
                    inventory: inventory
                });
            });
            $scope.advanceSku = skuDetails;
        });
    }
    function popoverPromotionDetail(code, entity) {
        if (entity.isOpen) {
            entity.isOpen = false;
            return;
        }
        entity.isOpen = true;
        $promotionHistoryService.getUnduePromotion({
            code: code
        }).then(function(resp) {
            $scope.promotionDetail = resp.data;
        });
    }
    function getCartQty(codeCartQty) {
        $scope.codeCartQty = codeCartQty;
    }
} ]);

angular.module("voyageone.angular.directives").directive("autoComplete", function() {
    return {
        restrict: "A",
        require: "ngModel",
        scope: {
            matchArrays: "@autoComplete"
        },
        link: function link(scope, element, attrs, ngModelCtl) {
            scope.$parent.$watch(scope.matchArrays, function(newValue) {
                var _copyArray = angular.copy(newValue);
                element.autocomplete({
                    lookup: _.map(_copyArray, function(str) {
                        return {
                            value: str
                        };
                    }),
                    minChars: 0,
                    onSelect: function onSelect(suggestion) {
                        ngModelCtl.$setViewValue(suggestion.value);
                    }
                }).focus(function() {
                    element.autocomplete("search");
                });
            });
        }
    };
});

(function() {
    function sizeof(str) {
        if (str == undefined || str == null) {
            return 0;
        }
        var regex = str.match(/[^\x00-\xff]/g);
        return str.length + (!regex ? 0 : regex.length);
    }
    function getByteLength(str) {
        return sizeof(str);
    }
    function makeByteLength(attrName, checkLength) {
        return function() {
            return {
                restrict: "A",
                require: "ngModel",
                scope: false,
                link: function link(scope, element, attrs, ngModelController) {
                    var length = attrs[attrName];
                    if (!length) return;
                    if (checkLength(getByteLength(scope.field.value), length)) {
                        ngModelController.$setValidity(attrName, true);
                    } else {
                        ngModelController.$setValidity(attrName, false);
                    }
                    ngModelController.$parsers.push(function(viewValue) {
                        ngModelController.$setValidity(attrName, checkLength(getByteLength(viewValue), length));
                        return viewValue;
                    });
                }
            };
        };
    }
    angular.module("voyageone.angular.directives").directive("maxbytelength", makeByteLength("maxbytelength", function(byteLength, maxLength) {
        return byteLength <= maxLength;
    })).directive("minbytelength", makeByteLength("minbytelength", function(byteLength, minLength) {
        return byteLength >= minLength;
    }));
})();

angular.module("voyageone.angular.directives").directive("dateModelFormat", [ "$filter", function($filter) {
    return {
        restrict: "A",
        require: "ngModel",
        link: function link(scope, elem, attrs, ngModel) {
            ngModel.$parsers.push(function(viewValue) {
                return $filter("date")(viewValue, attrs.dateModelFormat || "yyyy-MM-dd HH:mm:ss");
            });
        }
    };
} ]);

angular.module("voyageone.angular.directives").directive("editTitle", function() {
    function EditTitleController($scope, $attrs, $element, notify, $translate, productDetailService) {
        this.$attrs = $attrs;
        this.$scope = $scope;
        this.$element = $element;
        this.notify = notify;
        this.$translate = $translate;
        this.productDetailService = productDetailService;
    }
    EditTitleController.prototype.init = function() {
        var self = this, _products = {
            originalTitleCn: angular.copy(self.$scope.data.common.fields.originalTitleCn),
            prodId: angular.copy(self.$scope.data.prodId)
        };
        self.productInfo = _products;
        self.dynamicPopover = {
            title: "产品名称 ",
            templateUrl: "myPopoverTemplate.html"
        };
    };
    EditTitleController.prototype.save = function() {
        var self = this, productInfo = self.productInfo;
        var prodId = productInfo.prodId, originalTitleCn = productInfo.originalTitleCn.replace(/[.\n]/g, "");
        if (angular.equals(originalTitleCn, self.$scope.data.common.fields.originalTitleCn)) {
            self.isOpen = false;
            return;
        }
        if (prodId && originalTitleCn) {
            self.productDetailService.updateOriginalTitleCn({
                prodId: prodId,
                originalTitleCn: originalTitleCn
            }).then(function() {
                self.isOpen = false;
                self.$scope.data.common.fields.originalTitleCn = originalTitleCn;
                self.notify.success(self.$translate.instant("TXT_MSG_UPDATE_SUCCESS"));
            });
        }
    };
    return {
        restrict: "E",
        scope: {
            data: "=data"
        },
        controller: [ "$scope", "$attrs", "$element", "notify", "$translate", "productDetailService", EditTitleController ],
        controllerAs: "ctrl",
        template: '<div ng-init="ctrl.init()">' + '<script type="text/ng-template" id="myPopoverTemplate.html">' + '<div class="form-group">' + '<textarea class="form-control no-resize" style="min-height: 70px;width: 200px" ng-model="ctrl.productInfo.originalTitleCn"></textarea>' + "</div>" + '<div class="form-group pull-right">' + '<button class="btn btn-success" ng-click="ctrl.save()"><i class="fa fa-save"></i></button>' + "</div>" + "<\/script>" + '<button uib-popover-template="ctrl.dynamicPopover.templateUrl" popover-title="{{ctrl.dynamicPopover.title}}" popover-is-open="ctrl.isOpen" type="button" class="btn btn-default" title="{{ctrl.$scope.data.common.fields.originalTitleCn}}" ng-if="ctrl.$scope.data.common.fields.originalTitleCn">{{ctrl.$scope.data.common.fields.originalTitleCn  | limitTo: 25}}</button>' + "</div>"
    };
});

angular.module("voyageone.angular.directives").directive("enterClick", function() {
    return {
        restrict: "A",
        link: function link(scope, elem, attr) {
            angular.element(elem).on("keyup", function(e) {
                if (e.keyCode !== 13) return;
                var selectExp = attr.enterClick;
                var targetElem, handler = function handler() {
                    targetElem.triggerHandler("click");
                };
                try {
                    targetElem = document.querySelector(selectExp);
                } catch (e) {
                    targetElem = null;
                }
                if (!targetElem) {
                    handler = function handler() {
                        scope.$eval(selectExp);
                    };
                } else {
                    targetElem = angular.element(targetElem);
                    if (targetElem.attr("disabled")) return;
                }
                handler();
            });
        }
    };
});

angular.module("voyageone.angular.directives").directive("equalTo", function() {
    return {
        restrict: "A",
        require: "ngModel",
        scope: {
            equalTo: "="
        },
        link: function link(scope, ele, attrs, ctrl) {
            var target = attrs["equalTo"];
            if (target) {
                scope.$watch("equalTo", function() {
                    ctrl.$validate();
                });
                ctrl.$validators.equalTo = function(viewVale) {
                    return scope.equalTo == viewVale;
                };
            }
        }
    };
});

angular.module("voyageone.angular.directives").directive("fileStyle", function() {
    FileStyleController.$inject = [ "$scope", "$element" ];
    function FileStyleController($scope, $element) {
        this.scope = $scope;
        this.element = $element;
    }
    FileStyleController.prototype.init = function(attrs) {
        var options;
        if (attrs.fileStyle != null && attrs.fileStyle != "") options = eval("(" + attrs.fileStyle + ")");
        this.element.filestyle(options);
    };
    return {
        restrict: "A",
        scope: true,
        controller: FileStyleController,
        controllerAs: "styleCtrl",
        link: function link($scope, $element, $attrs) {
            $scope.styleCtrl.init($attrs);
        }
    };
});

angular.module("voyageone.angular.directives").directive("ifNoRows", [ "$templateCache", "$compile", function($templateCache, $compile) {
    var tempNoDataKey = "voyageone.angular.directives.ifNoRows.tpl.html";
    if (!$templateCache.get(tempNoDataKey)) {
        $templateCache.put(tempNoDataKey, '<div class="text-center text-hs" id="noData"><h4 class="text-vo"><i class="icon fa fa-warning"></i>&nbsp;<span translate="TXT_ALERT"></span></h4><span translate="TXT_MSG_NO_DATE"></span></dv>');
    }
    return {
        restrict: "A",
        replace: false,
        scope: {
            $$data: "@ifNoRows"
        },
        link: function link(scope, element) {
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

var _createClass = function() {
    function defineProperties(target, props) {
        for (var i = 0; i < props.length; i++) {
            var descriptor = props[i];
            descriptor.enumerable = descriptor.enumerable || false;
            descriptor.configurable = true;
            if ("value" in descriptor) descriptor.writable = true;
            Object.defineProperty(target, descriptor.key, descriptor);
        }
    }
    return function(Constructor, protoProps, staticProps) {
        if (protoProps) defineProperties(Constructor.prototype, protoProps);
        if (staticProps) defineProperties(Constructor, staticProps);
        return Constructor;
    };
}();

function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
        throw new TypeError("Cannot call a class as a function");
    }
}

var imageDirectiveService = [ "$localStorage", function() {
    function ImageDirectiveService($localStorage) {
        _classCallCheck(this, ImageDirectiveService);
        this.imageUrlTemplate = "http://localhost:2080/{channel}/is/image/sneakerhead/{image_name}";
        this.userInfo = $localStorage.user;
        this.selectedChannel = this.userInfo.channel;
    }
    _createClass(ImageDirectiveService, [ {
        key: "getImageUrlByName",
        value: function getImageUrlByName(name) {
            var channel = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : this.selectedChannel;
            return this.imageUrlTemplate.replace("{channel}", channel).replace("{image_name}", name);
        }
    } ]);
    return ImageDirectiveService;
}() ];

var imgByName = [ "imageDirectiveService", function imgByNameFactory(imageDirectiveService) {
    var srcBindTemplate = imageDirectiveService.getImageUrlByName("{{name}}{{::s7Options?'?'+s7Options:''}}", "{{channel || selectedChannel}}");
    return {
        restrict: "E",
        replace: true,
        template: '<img data-by="img-by-name" ng-src="' + srcBindTemplate + '">',
        scope: {
            name: "=",
            channel: "=",
            s7Options: "@"
        },
        link: function link($scope) {
            $scope.selectedChannel = imageDirectiveService.selectedChannel;
        }
    };
} ];

var aProductImg = [ "imageDirectiveService", function aProductImgFactory(imageDirectiveService) {
    var hrefBindTemplate = imageDirectiveService.getImageUrlByName("{{name}}?{{::r}}&{{::s7Options}}", "{{channel || selectedChannel}}");
    var textBindTemplate = imageDirectiveService.getImageUrlByName("{{name}}?{{::s7Options}}", "{{channel || selectedChannel}}");
    return {
        restrict: "E",
        replace: true,
        transclude: true,
        scope: {
            name: "=",
            channel: "=",
            s7Options: "@"
        },
        template: '<a ng-href="' + hrefBindTemplate + '"><ng-transclude>' + textBindTemplate + "</ng-transclude></a>",
        link: function link($scope) {
            $scope.r = Math.random();
            $scope.selectedChannel = imageDirectiveService.selectedChannel;
        }
    };
} ];

angular.module("voyageone.angular.directives").service("imageDirectiveService", imageDirectiveService).directive("imgByName", imgByName).directive("aProductImg", aProductImg);

angular.module("voyageone.angular.directives").directive("input", function() {
    return {
        restrict: "E",
        require: [ "?ngModel" ],
        link: function link(scope, element, attr) {
            var type = attr.type;
            if (!type) return;
            type = type.toLowerCase();
            if (type !== "number") return;
            element.on("keypress", function(event) {
                var charCode = event.charCode;
                var lastInputIsPoint = element.data("lastInputIsPoint");
                if (charCode !== 0 && charCode !== 46 && (charCode < 48 || charCode > 57)) {
                    event.preventDefault();
                    return;
                }
                if (charCode === 46) {
                    if (lastInputIsPoint || this.value.indexOf(".") > -1) {
                        event.preventDefault();
                        return;
                    }
                    element.data("lastInputIsPoint", true);
                    return;
                }
                element.data("lastInputIsPoint", false);
            });
        }
    };
}).directive("scale", function() {
    return {
        restrict: "A",
        require: [ "ngModel" ],
        link: function link(scope, element, attr, ctrls) {
            var type = attr.type;
            var ngModelController = ctrls[0];
            if (!type) return;
            type = type.toLowerCase();
            if (type !== "number") return;
            var scale, _length;
            var _numArr = attr.scale.split(",");
            if (_numArr.length !== 2) {
                console.warn("scale格式为{ 位数 },{ 精度 } 默认值=》位数：15位，精度为小数点2位。");
                _length = 15;
                scale = 2;
            } else {
                _length = _numArr[0];
                scale = _numArr[1];
            }
            element.on("keyup", function() {
                var regex;
                if (scale != 0) regex = new RegExp("^\\d+(\\.\\d{1," + scale + "})?$"); else regex = new RegExp("^\\d+$");
                if (regex.test(this.value)) return;
                ngModelController.$setViewValue(this.value.substr(0, this.value.length - 1));
                ngModelController.$render();
            }).on("keypress", function(event) {
                var _value = angular.copy(this.value);
                if (_value.toString().length >= _length) {
                    event.preventDefault();
                }
            });
        }
    };
});

var errorTypes = {
    email: "INVALID_EMAIL",
    url: "INVALID_URL",
    date: "INVALID_DATE",
    datetimelocal: "INVALID_DATETIMELOCAL",
    color: "INVALID_COLOR",
    range: "INVALID_RANGE",
    month: "INVALID_MONTH",
    time: "INVALID_TIME",
    week: "INVALID_WEEK",
    number: "INVALID_NUMBER",
    required: "INVALID_REQUIRED",
    maxlength: "INVALID_MAXLENGTH",
    minlength: "INVALID_MINLENGTH",
    minbytelength: "INVALID_MINLENGTH",
    maxbytelength: "INVALID_MAXLENGTH",
    max: "INVALID_MAX",
    min: "INVALID_MIN",
    pattern: "INVALID_PATTERN",
    equalTo: "INVALID_NOT_EQUAL"
};

angular.module("voyageone.angular.directives").directive("voMessage", [ "$translate", function($translate) {
    return {
        restrict: "E",
        template: "{{$message}}",
        require: "^^form",
        scope: {
            target: "="
        },
        link: function link(scope, elem, attrs, formController) {
            function show(message) {
                scope.$message = message;
                elem.fadeIn();
            }
            function hide() {
                elem.fadeOut();
            }
            var formName;
            elem.hide();
            formName = formController.$name;
            scope.$watch("target.$error", function($error) {
                if (!$error) return;
                var errorKeys = Object.keys($error);
                var elementName = scope.target.$name;
                var targetElement = $('[name="' + formName + '"] [name="' + elementName + '"]');
                var translateParam = {
                    field: targetElement.attr("title") || elementName
                };
                var error = errorKeys[0];
                if (!error) {
                    hide();
                    return;
                }
                if (attrs[error]) {
                    show(attrs[error]);
                } else {
                    if ([ "maxlength", "minlength", "maxbytelength", "minbytelength", "max", "min", "pattern", "equalTo" ].indexOf(error) > -1) {
                        if (!(translateParam.value = targetElement.attr(error)) && "pattern" === error) translateParam.value = targetElement.attr("ng-pattern");
                    }
                    $translate(errorTypes[error], translateParam).then(show, show);
                }
            }, true);
        }
    };
} ]);

angular.module("voyageone.angular.directives").directive("uiNav", function() {
    return {
        restrict: "AC",
        link: function link(scope, el) {
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

angular.module("voyageone.angular.directives").directive("ngEnter", function() {
    return function(scope, element, attrs) {
        element.bind("keydown keypress", function(event) {
            if (event.which === 13) {
                scope.$apply(function() {
                    scope.$eval(attrs.ngEnter);
                });
                event.preventDefault();
            }
        });
    };
});

"use strict";

angular.module("voyageone.angular.directives").directive("ngThumb", [ "$window", function($window) {
    var helper = {
        support: !!($window.FileReader && $window.CanvasRenderingContext2D),
        isFile: function isFile(item) {
            return angular.isObject(item) && item instanceof $window.File;
        },
        isImage: function isImage(file) {
            var type = "|" + file.type.slice(file.type.lastIndexOf("/") + 1) + "|";
            return "|jpg|png|jpeg|bmp|gif|".indexOf(type) !== -1;
        }
    };
    return {
        restrict: "A",
        link: function link(scope, element, attributes) {
            if (!helper.support) return;
            var params = scope.$eval(attributes.ngThumb);
            if (!helper.isImage(params.file)) return;
            var fileReader = new FileReader();
            fileReader.readAsDataURL(params.file);
            fileReader.onload = function(event) {
                scope.$apply(function() {
                    attributes.$set("src", event.target.result);
                });
            };
        }
    };
} ]);

angular.module("voyageone.angular.directives").directive("popoverText", function() {
    return {
        restrict: "AE",
        transclude: true,
        template: '<small popover-html="content" popover-placement="{{::direct}}"><div class="table_main"><ul><li class="cur-p"></li></ul></div></small>',
        replace: true,
        scope: {
            content: "=content",
            direct: "@direct",
            size: "@size"
        },
        link: function link(scope, element) {
            var content = scope.content, size = scope.size;
            var li = $(element).find("li");
            if (content.length > scope.size) li.html(content.substr(0, size) + "..."); else li.html(content).css({
                cursor: "default"
            });
        }
    };
});

(function() {
    function _priceScale(prices) {
        if (!prices || prices.length === 0) {
            console.warn("directive:price=> 请输入要显示的价格");
            return;
        }
        if (prices.length === 1) return prices[0];
        var min = _.min(prices), max = _.max(prices), compiled = _.template("<%= min %> ~ <%= max %>");
        if (min === max) return min; else return compiled({
            min: min,
            max: max
        });
    }
    angular.module("voyageone.angular.directives").directive("price", function() {
        return {
            restrict: "E",
            scope: {
                prices: "=prices"
            },
            link: function link(scope, element) {
                element.html(_priceScale(scope.prices));
            }
        };
    }).directive("clientMsrpPrice", [ "$compile", function($compile) {
        return {
            restrict: "E",
            scope: {
                data: "=data"
            },
            link: function link(scope, element) {
                var skuList = scope.data, final = [], rangArr = [], buttonPopover = angular.element('<button  type="button">');
                buttonPopover.attr("ng-controller", "showPopoverCtrl");
                buttonPopover.attr("popover-title", "客户建议零售价");
                buttonPopover.attr("popover-placement", "left");
                buttonPopover.addClass("btn btn-default btn-xs");
                if (!skuList) {
                    console.warn("没有提供sku数据！");
                    return;
                }
                if (skuList instanceof Array) {
                    angular.forEach(skuList, function(element) {
                        var str = element.skuCode + " : " + element.clientMsrpPrice, cmcf = element.clientMsrpPriceChgFlg, labelStr = "";
                        if (cmcf && cmcf != 0 && !/^\w{1}0%$/.test(cmcf)) {
                            if (cmcf.indexOf("U") >= 0) {
                                labelStr += '<label class="text-u-red font-bold">&nbsp;(↑' + cmcf.substring(1) + ")</label>";
                            } else {
                                labelStr += '<label class="text-u-green font-bold">&nbsp;(↓' + cmcf.substring(1) + ")</label>";
                            }
                            rangArr.push(labelStr);
                        }
                        final.push(str + labelStr);
                    });
                } else {
                    console.warn("传入的数据结构应该是数组！");
                }
                buttonPopover.attr("popover-html", "showInfo(" + JSON.stringify(final) + ")");
                if (rangArr[0]) buttonPopover.html(_priceScale(_.pluck(skuList, "clientMsrpPrice")) + rangArr[0]); else buttonPopover.html(_priceScale(_.pluck(skuList, "clientMsrpPrice")));
                element.html($compile(buttonPopover)(scope.$new()));
            }
        };
    } ]);
})();

(function() {
    var FIELD_TYPES = {
        INPUT: "INPUT",
        MULTI_INPUT: "MULTIINPUT",
        SINGLE_CHECK: "SINGLECHECK",
        MULTI_CHECK: "MULTICHECK",
        COMPLEX: "COMPLEX",
        MULTI_COMPLEX: "MULTICOMPLEX",
        LABEL: "LABEL"
    };
    var find, findIndex, each, any, all, exists, is;
    var VALUE_TYPES = {
        TEXT: "text",
        TEXTAREA: "textarea",
        INTEGER: "integer",
        LONG: "long",
        DECIMAL: "decimal",
        DATE: "date",
        TIME: "time",
        URL: "url",
        HTML: "html"
    };
    var SYMBOLS = {
        NOT_CONTAINS: {
            key: "not contains"
        },
        NOT_EQUALS: {
            key: "!="
        },
        EQUALS: {
            key: "=="
        }
    };
    SYMBOLS["not contains"] = SYMBOLS.NOT_CONTAINS;
    SYMBOLS["!="] = SYMBOLS.NOT_EQUALS;
    SYMBOLS["=="] = SYMBOLS.EQUALS;
    (function() {
        is = {};
        exists = function exists(target) {
            return target !== null && target !== undefined && target !== "";
        };
        if (!window._) console.warn("Please import underscore !!!"); else {
            find = _.find;
            any = _.some;
            all = _.every;
            each = _.each;
            findIndex = _.findIndex;
            is.string = _.isString;
            is.array = _.isArray;
        }
    })();
    function random(length) {
        return Math.random().toString().substr(2, length || 6);
    }
    function getFieldId(field) {
        if (!field.$schema) field.$schema = {
            id: field.id.replace(/->/g, ".")
        };
        return field.$schema.id;
    }
    function searchField(fieldId, schema) {
        var result = null;
        find(schema, function(field) {
            if (getFieldId(field) === fieldId) {
                result = field;
                return true;
            }
            if (field.fields && field.fields.length) {
                result = searchField(fieldId, field.fields);
                if (result) return true;
            }
            return false;
        });
        return result;
    }
    function hasDepend(rule) {
        var dependExpressList = rule && rule.dependGroup ? rule.dependGroup.dependExpressList : null;
        return !!dependExpressList && !!dependExpressList.length;
    }
    function getRules(field, schema) {
        var withSchema = !!schema;
        var rules = field.$rules;
        if (rules) {
            if (!withSchema || rules.$withSchema) return rules;
        } else {
            rules = {};
        }
        if (!field || !field.rules) return rules;
        if (!field.rules.length) return rules;
        each(field.rules, function(rule) {
            if (!hasDepend(rule)) {
                var newRule;
                if (rule.value === "false") return;
                newRule = {};
                newRule.__proto__ = rule;
                if (rule.value === "true") newRule.value = true; else if (/^-?\d+(\.\d+)?$/.test(rule.value)) newRule.value = parseFloat(rule.value);
                rules[rule.name] = newRule;
            } else if (schema) {
                rules[rule.name] = new DependentRule(rule, field, schema);
            }
        });
        rules.$withSchema = withSchema;
        field.$rules = rules;
        return rules;
    }
    function hasValidateRule(field) {
        if (!field || !field.rules) return false;
        if (!field.rules.length) return false;
        return any(field.rules, function(rule) {
            return rule.name === "valueTypeRule" && rule.value !== VALUE_TYPES.TEXT && rule.value !== VALUE_TYPES.TEXTAREA && rule.value !== VALUE_TYPES.HTML || rule.name !== "tipRule" && rule.name !== "devTipRule" && rule.name !== "disableRule";
        });
    }
    function getInputType(valueTypeRule) {
        var type = "text";
        if (!valueTypeRule) return type;
        switch (valueTypeRule.value) {
          case VALUE_TYPES.TEXT:
            type = "text";
            break;

          case VALUE_TYPES.HTML:
          case VALUE_TYPES.TEXTAREA:
            type = "textarea";
            break;

          case VALUE_TYPES.INTEGER:
          case VALUE_TYPES.LONG:
            type = "number";
            break;

          case VALUE_TYPES.DECIMAL:
            type = "number";
            break;

          case VALUE_TYPES.DATE:
            type = "date";
            break;

          case VALUE_TYPES.TIME:
            type = "datetime-local";
            break;

          case VALUE_TYPES.URL:
            type = "url";
            break;
        }
        return type;
    }
    function getInputValue(value, field, valueTypeRule) {
        var parsedValue = null;
        var valueType = field.fieldValueType;
        if (!exists(value)) return null;
        switch (valueType) {
          case "INT":
          case "DOUBLE":
            parsedValue = parseFloat(value);
            return isNaN(parsedValue) ? null : parsedValue;
        }
        if (!valueTypeRule) return value;
        switch (valueTypeRule.value) {
          case VALUE_TYPES.TEXT:
          case VALUE_TYPES.HTML:
          case VALUE_TYPES.TEXTAREA:
          case VALUE_TYPES.URL:
            return value;

          case VALUE_TYPES.INTEGER:
          case VALUE_TYPES.LONG:
          case VALUE_TYPES.DECIMAL:
            parsedValue = parseFloat(value);
            if (isNaN(parsedValue)) return null;
            return parsedValue;

          case VALUE_TYPES.DATE:
          case VALUE_TYPES.TIME:
            if (!value) return null;
            parsedValue = new Date(value);
            if (isNaN(parsedValue.getDate())) throw "日期(时间)格式不正确";
            return parsedValue;
        }
        return value;
    }
    function bindLengthRule(element, rule, name, attr, ngAttr) {
        if (!rule) return;
        if (rule instanceof DependentRule) {
            element.attr(ngAttr || "ng-" + attr, "rules." + name + ".getLength()");
        } else {
            element.attr(attr, rule.value);
        }
    }
    function bindInputLengthRule(element, rules) {
        var minRule = rules.minLengthRule, maxRule = rules.maxLengthRule;
        if (minRule) {
            if (isByteUnit(minRule)) bindLengthRule(element, minRule, "minLengthRule", "minbytelength", "minbytelength"); else bindLengthRule(element, minRule, "minLengthRule", "minlength");
        }
        if (maxRule) {
            if (isByteUnit(maxRule)) bindLengthRule(element, maxRule, "maxLengthRule", "maxbytelength", "maxbytelength"); else bindLengthRule(element, maxRule, "maxLengthRule", "maxlength");
        }
    }
    function bindBoolRule(element, rule, name, attr) {
        if (!rule) return;
        if (rule instanceof DependentRule) {
            element.attr("ng-" + attr, "rules." + name + ".checked()");
        } else if (rule) {
            element.attr(attr, true);
        }
    }
    function bindTipRule(container, rules) {
        each(rules, function(content, key) {
            if (key.indexOf("$") === 0) return;
            if (key.indexOf("Rule") > 0 && key !== "tipRule") return;
            var contentContainer = angular.element('<s-tip ng-if="showTip">');
            container.append(contentContainer);
            if ("url" in content && !!content.url) {
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
        var result = tryGetDefault("defaultValue");
        if (!exists(result)) {
            result = tryGetDefault("defaultValues");
            if (!exists(result) || !result.length) return;
        }
        if (exists(options) && options.length) {
            result = !is.array(result) ? find(options, function(o) {
                return o.value == result;
            }).displayName : result.map(function(r) {
                return find(options, function(o) {
                    return o.value == r;
                }).displayName;
            });
        }
        var contentContainer = angular.element("<s-tip>");
        contentContainer.text("该字段包含默认值: " + angular.toJson(result));
        container.append(contentContainer);
    }
    function resetValue(valueObj, fieldObj) {
        [ "value", "values", "complexValue", "complexValues" ].some(function(key) {
            if (key in valueObj) {
                fieldObj[key] = valueObj[key];
                return true;
            }
            return false;
        });
    }
    function isByteUnit(rule) {
        return rule.unit === "byte";
    }
    function switchPrivateValue(field, valueKeys, fromPrivate) {
        if (field.type === FIELD_TYPES.COMPLEX) {
            each(field.fields, function(childField) {
                switchPrivateValue(childField, valueKeys, fromPrivate);
            });
            return;
        }
        if (fromPrivate) {
            any(valueKeys, function(key) {
                var privateKey = "$" + key;
                var privateValueObj = field[privateKey];
                if (privateKey in field) {
                    if (exists(privateValueObj)) {
                        field[key] = privateValueObj;
                    }
                    field[privateKey] = null;
                    return true;
                }
                return false;
            });
        } else {
            any(valueKeys, function(key) {
                var valueObj = field[key];
                if (key in field) {
                    if (exists(valueObj)) {
                        field["$" + key] = valueObj;
                    }
                    field[key] = null;
                    return true;
                }
                return false;
            });
        }
    }
    function ComplexValue(fields) {
        this.$id = "$ComplexValue" + random();
        this.fieldMap = {};
        this.fieldKeySet = [];
        ComplexValue.Caches[this.$id] = this;
        this.putAll(fields);
    }
    ComplexValue.Caches = {};
    ComplexValue.prototype.get = function(field) {
        return this.fieldMap[field.id || field];
    };
    ComplexValue.prototype.put = function(field) {
        this.fieldKeySet.push(field.id);
        this.fieldMap[field.id] = field;
        field.$parentComplexValueId = this.$id;
        return this;
    };
    ComplexValue.prototype.putAll = function(fields) {
        var self = this;
        each(fields, function(f) {
            self.put(angular.copy(f));
        });
        return self;
    };
    ComplexValue.prototype.copyFrom = function(originComplexValue, field) {
        var self = this;
        var fieldMap = originComplexValue.fieldMap || (originComplexValue.fieldMap = {});
        each(field.fields, function(childField) {
            var mapItem = fieldMap[childField.id];
            if (!mapItem) {
                mapItem = angular.copy(childField);
            } else {
                mapItem.rules = childField.rules;
                mapItem.name = childField.name;
                mapItem.options = childField.options;
                mapItem.fieldValueType = childField.fieldValueType;
            }
            self.put(mapItem);
        });
        return self;
    };
    function DependentRule(rule, field, schema) {
        var dependGroup = rule.dependGroup, dependExpressList = dependGroup.dependExpressList, operator = dependGroup.operator;
        this.$key = "$Depend" + random();
        this.checker = operator === "or" ? any : all;
        this.dependExpressList = dependExpressList.map(function(dependExpress) {
            var field = searchField(dependExpress.fieldId, schema);
            if (!field) {
                console.warn("cant find field for dep rule. -> " + dependExpress.fieldId);
            }
            var symbol = SYMBOLS[dependExpress.symbol];
            if (!symbol) console.warn("没有找到可用符号: " + dependExpress.symbol);
            return {
                field: field,
                value: dependExpress.value,
                symbol: symbol
            };
        });
        this.value = rule.value;
        this.origin = rule;
        DependentRule.fieldCache[this.$fieldId = field.$name || field.id] = field;
    }
    DependentRule.fieldCache = {};
    DependentRule.prototype.checked = function() {
        var self = this, dependExpressList = self.dependExpressList, currRule = self.origin, currentField = DependentRule.fieldCache[self.$fieldId], forceFail = false;
        var result = self.checker(dependExpressList, function(express) {
            var parentDisableRule, parentComplexValue;
            var targetFieldInComplex = null;
            var targetField = express.field;
            if (currentField.$parentComplexValueId) {
                parentComplexValue = ComplexValue.Caches[currentField.$parentComplexValueId];
                if (parentComplexValue) {
                    targetFieldInComplex = parentComplexValue.get(targetField);
                    if (targetFieldInComplex) targetField = targetFieldInComplex;
                }
            }
            if (!targetField) return !(forceFail = true);
            if (currRule.name === "disableRule" && !!(parentDisableRule = getRules(targetField).disableRule)) {
                if (parentDisableRule.checked()) return true;
            }
            var value = targetField.values || targetField.value;
            if (value && "value" in value) value = value.value;
            if (value === null) return !(forceFail = true);
            switch (express.symbol) {
              case SYMBOLS.EQUALS:
                return value == express.value;

              case SYMBOLS.NOT_EQUALS:
                return value != express.value;

              case SYMBOLS.NOT_CONTAINS:
                return !!value && !any(value, function(valueObj) {
                    return valueObj.value == express.value;
                });

              default:
                return false;
            }
        });
        return forceFail ? false : result;
    };
    DependentRule.prototype.getRegex = function() {
        return this.checked() ? this.value : null;
    };
    DependentRule.prototype.getLength = function() {
        return this.checked() ? this.value : null;
    };
    function FieldRepeater(fieldList, schema, parentScope, targetElement, $compile) {
        this.fieldList = fieldList;
        this.schema = schema;
        this.repeaterScope = parentScope.$new();
        this.targetElement = targetElement;
        this.$compile = $compile;
    }
    FieldRepeater.prototype.renderOne = function(field) {
        var self = this, schema = self.schema, parentScope = self.repeaterScope, targetElement = self.targetElement, $compile = self.$compile;
        var rules, disableRule, disabledExpression, fieldElement, fieldScope;
        if (field.isDisplay == "0") return;
        field.$name = "Field" + random();
        rules = getRules(field, schema);
        disableRule = rules.disableRule;
        if (disableRule && !(disableRule instanceof DependentRule) && disableRule.value === true) return;
        fieldElement = angular.element("<s-field>");
        fieldScope = parentScope.$new();
        fieldScope.field = field;
        fieldElement.attr("field", "field");
        if (disableRule instanceof DependentRule) {
            fieldScope.disabled = disableRule;
            disabledExpression = "!disabled.checked()";
            fieldElement.attr("ng-if", disabledExpression);
            fieldScope.$watch(disabledExpression, function(enabled) {
                switchPrivateValue(field, [ "value", "values", "complexValues" ], enabled);
            });
        }
        targetElement.append(fieldElement);
        $compile(fieldElement)(fieldScope);
    };
    FieldRepeater.prototype.renderList = function() {
        var self = this, fieldList = self.fieldList;
        each(fieldList, function(field) {
            self.renderOne(field);
        });
    };
    FieldRepeater.prototype.destroy = function() {
        var self = this, parentScope = self.repeaterScope, targetElement = self.targetElement;
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
    angular.module("voyageone.angular.directives").directive("schema", [ "$compile", function($compile) {
        SchemaController.$inject = [ "$scope" ];
        function SchemaController($scope) {
            this.$scope = $scope;
        }
        SchemaController.prototype.getSchema = function() {
            return this.schema;
        };
        SchemaController.prototype.$setSchema = function(data) {
            this.schema = data;
        };
        SchemaController.prototype.$render = function($element) {
            var controller = this, $scope = controller.$scope, schema = controller.getSchema(), fieldRepeater = controller.fieldRepeater;
            if (fieldRepeater) {
                fieldRepeater.destroy();
                controller.fieldRepeater = null;
            }
            if (!schema || !schema.length) return;
            fieldRepeater = new FieldRepeater(schema, schema, $scope, $element, $compile);
            controller.fieldRepeater = fieldRepeater;
            fieldRepeater.renderList();
        };
        return {
            restrict: "E",
            scope: true,
            controllerAs: "schemaController",
            link: function link($scope, $element, $attrs) {
                $scope.$watch($attrs.data, function(data) {
                    var schemaController = $scope.schemaController;
                    schemaController.$setSchema(data);
                    schemaController.$render($element);
                });
            },
            controller: SchemaController
        };
    } ]).directive("sField", [ "$compile", function($compile) {
        SchemaFieldController.$inject = [ "$scope", "$element" ];
        function SchemaFieldController($scope, $element) {
            this.originScope = $scope;
            this.$element = $element;
        }
        SchemaFieldController.prototype.render = function() {
            var controller = this, $element, formController, showName, parentScope, $scope, field, container, hasValidate, rules, innerElement, isSimple;
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
            isSimple = field.type != FIELD_TYPES.COMPLEX && field.type != FIELD_TYPES.MULTI_COMPLEX;
            if (showName) container.append(angular.element("<s-header>"));
            each(rules, function(content, key) {
                if (key.indexOf("$") === 0) return;
                if (key.indexOf("Rule") > 0 && key !== "tipRule") return;
                var contentContainer = angular.element('<s-tip-new ng-click="showTip =! showTip">');
                container.append(contentContainer);
            });
            innerElement = angular.element('<div class="s-wrapper">');
            container.append(innerElement);
            container = innerElement;
            innerElement = angular.element("<s-container>");
            container.append(innerElement);
            if (hasValidate && isSimple) {
                var formName = formController.$name;
                var voMessage = angular.element('<vo-message target="' + formName + "." + field.$name + '"></vo-message>');
                container.append(voMessage);
            }
            bindDefaultValueTip(container, field);
            bindTipRule(container, rules);
            $compile($element.contents())($scope);
        };
        SchemaFieldController.prototype.remove = function(complexValue) {
            var $scope = this.$scope;
            var list = $scope.$complexValues;
            var index = list.indexOf(complexValue);
            list.splice(index, 1);
        };
        SchemaFieldController.prototype.getField = function() {
            return this.field;
        };
        SchemaFieldController.prototype.setField = function(field) {
            this.field = field;
        };
        SchemaFieldController.prototype.destroy = function() {
            var controller = this, $element = controller.$element, $scope = controller.$scope;
            if ($element) $element.empty();
            if ($scope) {
                $scope.$destroy();
                controller.$scope = null;
            }
        };
        return {
            restrict: "E",
            require: [ "^^?form" ],
            scope: true,
            controllerAs: "schemaFieldController",
            link: function link($scope, $element, $attrs, requiredControllers) {
                var controller = $scope.schemaFieldController;
                controller.formController = requiredControllers[0];
                if (!$attrs.field) {
                    $element.text("请提供 field 属性。");
                    return;
                }
                controller.showName = !$attrs.showName || $attrs.showName === "true";
                controller.canAdd = $attrs.add !== "false";
                $scope.$watch($attrs.field, function(field) {
                    controller.setField(field);
                    if (!field) return;
                    controller.render();
                });
            },
            controller: SchemaFieldController
        };
    } ]).directive("sHeader", function() {
        return {
            restrict: "E",
            require: [ "^^sField" ],
            scope: false,
            link: function link(scope, element, attrs, requiredControllers) {
                var schemaFieldController = requiredControllers[0];
                var field = schemaFieldController.getField(), rules = getRules(field), required = rules.requiredRule, requiredClass = "s-required";
                switch (field.type) {
                  case FIELD_TYPES.INPUT:
                  case FIELD_TYPES.SINGLE_CHECK:
                  case FIELD_TYPES.MULTI_CHECK:
                    element.addClass("simple");
                    break;

                  case FIELD_TYPES.COMPLEX:
                    element.addClass("complex");
                    break;

                  case FIELD_TYPES.MULTI_COMPLEX:
                    element.addClass("complex multi");
                    break;
                }
                element.text(field.name || field.id);
                if (required) {
                    if (required instanceof DependentRule) {
                        scope.$watch(function() {
                            return required.checked();
                        }, function(required) {
                            element[required ? "addClass" : "removeClass"](requiredClass);
                        });
                    } else {
                        element.addClass(requiredClass);
                    }
                }
            }
        };
    }).directive("sContainer", [ "$compile", "$filter", function($compile, $filter) {
        return {
            restrict: "E",
            scope: false,
            require: [ "^^sField" ],
            link: function link(scope, element, attrs, requiredControllers) {
                var schemaFieldController = requiredControllers[0];
                var innerElement;
                var field = schemaFieldController.getField(), rules = getRules(field), name = field.$name;
                scope.field = field;
                scope.rules = rules;
                switch (field.type) {
                  case FIELD_TYPES.INPUT:
                    (function createInputElements() {
                        var regexRule = rules.regexRule, valueTypeRule = rules.valueTypeRule, requiredRule = rules.requiredRule, readOnlyRule = rules.readOnlyRule, type = getInputType(valueTypeRule), _value, isDate = type.indexOf("date") > -1;
                        if (type === "textarea") {
                            innerElement = angular.element('<textarea class="form-control">');
                            if (valueTypeRule === VALUE_TYPES.HTML) innerElement.addClass("s-html");
                        } else {
                            innerElement = angular.element('<input class="form-control">').attr("type", type);
                        }
                        innerElement.attr("name", name);
                        bindBoolRule(innerElement, readOnlyRule, "readOnlyRule", "readonly");
                        bindBoolRule(innerElement, requiredRule, "requiredRule", "required");
                        bindInputLengthRule(innerElement, rules);
                        if (regexRule) {
                            if (regexRule instanceof DependentRule) {
                                innerElement.attr("ng-pattern", "rules.regexRule.getRegex()");
                            } else if (regexRule.value !== "yyyy-MM-dd") {
                                innerElement.attr("pattern", regexRule.value);
                            }
                        }
                        innerElement.attr("title", field.name || field.id);
                        innerElement.attr("ng-trim", false);
                        _value = field.value;
                        field.value = getInputValue(_value, field, valueTypeRule);
                        if (!field.value && exists(field.defaultValue)) {
                            _value = field.defaultValue;
                            field.value = getInputValue(_value, field, valueTypeRule);
                        }
                        if (isDate) {
                            scope.dateValue = field.value;
                            field.value = _value;
                            innerElement.attr("ng-model", "dateValue");
                            scope.$watch("dateValue", function(newDate) {
                                field.value = $filter("date")(newDate, type === "date" ? "yyyy-MM-dd" : "yyyy-MM-dd hh:mm:ss");
                            });
                        } else {
                            innerElement.attr("ng-model", "field.value");
                        }
                        if ((!readOnlyRule || readOnlyRule instanceof DependentRule) && isDate) {
                            var inputGroup = angular.element('<div class="input-group">');
                            var inputGroupBtn = angular.element('<span class="input-group-btn"><button type="button" class="btn btn-default" ng-click="$opened = !$opened"><i class="glyphicon glyphicon-calendar"></i></button>');
                            innerElement.attr("uib-datepicker-popup", "");
                            innerElement.attr("is-open", "$opened");
                            if (readOnlyRule instanceof DependentRule) {
                                inputGroupBtn.attr("ng-if", "!rules.readOnlyRule.checked()");
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
                        var requiredRule = rules.requiredRule, options = field.options, valueObject = field.value;
                        options = options.map(function(option) {
                            var newOption = {};
                            newOption.__proto__ = option;
                            newOption.value = getInputValue(option.value, field);
                            return newOption;
                        });
                        if (!valueObject) {
                            valueObject = {
                                value: null
                            };
                        } else if (exists(valueObject.value)) {
                            valueObject.value = getInputValue(valueObject.value, field);
                            foundValueObject = _.find(options, function(optionItem) {
                                return optionItem.value == valueObject.value;
                            });
                            valueObject.value = foundValueObject ? foundValueObject.value : valueObject.value = null;
                        }
                        if (!exists(valueObject.value) && exists(field.defaultValue)) {
                            valueObject.value = getInputValue(field.defaultValue, field);
                        }
                        if (!requiredRule) {
                            options.unshift(nullValueObj = {
                                displayName: "Select...",
                                value: null
                            });
                            if (!valueObject.value) {
                                valueObject = nullValueObj;
                            }
                        }
                        scope.$options = options;
                        field.value = valueObject;
                        innerElement = angular.element('<select class="form-control" chosen>');
                        innerElement.attr("ng-options", "option.value as option.displayName for option in $options");
                        innerElement.attr("name", name);
                        innerElement.attr("ng-model", "field.value.value");
                        innerElement.attr("width", '"100%"');
                        innerElement.attr("search-contains", true);
                        innerElement.attr("title", field.name || field.id);
                        bindBoolRule(innerElement, requiredRule, "requiredRule", "required");
                        bindBoolRule(innerElement, rules.readOnlyRule, "readOnlyRule", "readonly");
                    })();
                    break;

                  case FIELD_TYPES.MULTI_CHECK:
                    (function createCheckboxElements() {
                        var selected, valueStringList;
                        var requiredRule = rules.requiredRule;
                        var defaultValues = field.defaultValues;
                        innerElement = [];
                        selected = scope.selected = [];
                        scope.update = function(index) {
                            var selectedValue = field.options[index].value;
                            var selectedIndex = findIndex(field.values, function(valueObj) {
                                return valueObj.value == selectedValue;
                            });
                            if (scope.selected[index]) {
                                if (selectedIndex < 0) field.values.push({
                                    value: selectedValue
                                });
                            } else {
                                if (selectedIndex > -1) field.values.splice(selectedIndex, 1);
                            }
                        };
                        if (!field.values) field.values = [];
                        valueStringList = field.values.map(function(valueObj) {
                            return valueObj.value = getInputValue(valueObj.value, field).toString();
                        });
                        each(field.options, function(option, index) {
                            var label = angular.element("<label></label>"), checkbox = angular.element('<input type="checkbox">');
                            checkbox.attr("ng-model", "selected[" + index + "]");
                            checkbox.attr("name", name);
                            checkbox.attr("title", field.name || field.id);
                            checkbox.attr("ng-change", "update(" + index + ")");
                            if (requiredRule) {
                                if (requiredRule instanceof DependentRule) {
                                    checkbox.attr("ng-required", "rules.requiredRule.checked() && !field.values.length");
                                } else {
                                    checkbox.attr("ng-required", "!field.values.length");
                                }
                            }
                            bindBoolRule(checkbox, rules.readOnlyRule, "readOnlyRule", "readonly");
                            if (valueStringList.length) {
                                selected[index] = !(valueStringList.indexOf(option.value) < 0);
                            } else if (requiredRule && !!defaultValues.length) {
                                selected[index] = !(defaultValues.indexOf(option.value) < 0);
                            }
                            label.append(checkbox, "&nbsp;", option.displayName);
                            innerElement.push(label);
                        });
                    })();
                    break;

                  case FIELD_TYPES.COMPLEX:
                    var fieldValueMap, complexValue = field.complexValue;
                    if (complexValue) {
                        fieldValueMap = complexValue.fieldMap;
                        if (fieldValueMap) {
                            each(field.fields, function(childField) {
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
                    var complexValues = field.complexValues;
                    if (!complexValues) complexValues = [];
                    if (!complexValues.length) {
                        complexValues.push(new ComplexValue(field.fields));
                    } else {
                        complexValues = complexValues.map(function(complexValueObj) {
                            return new ComplexValue().copyFrom(complexValueObj, field);
                        });
                    }
                    field.complexValues = complexValues;
                    scope.$complexValues = complexValues;
                    innerElement = angular.element('<s-complex multi="true" fields="complexValue.fieldMap">');
                    innerElement.attr("ng-repeat", "complexValue in $complexValues");
                    if (schemaFieldController.canAdd) {
                        element.append(angular.element("<s-toolbar>"));
                    }
                    break;

                  default:
                    element.text("不支持的类型");
                    break;
                }
                if (innerElement instanceof Array) each(innerElement, function(childElement) {
                    element.append(childElement);
                }); else element.append(innerElement);
                $compile(element.contents())(scope);
            }
        };
    } ]).directive("sComplex", [ "$compile", function($compile) {
        SchemaComplexController.$inject = [ "$scope", "$attrs" ];
        function SchemaComplexController($scope, $attrs) {
            this.$scope = $scope;
            this.fields = $scope.$eval($attrs.fields);
        }
        SchemaComplexController.prototype.$render = function(schema, isMulti, $element) {
            var controller = this, fields = controller.fields, $scope = controller.$scope, repeater = controller.repeater;
            if (repeater) {
                repeater.destroy();
                controller.repeater = null;
            }
            if (!fields) return;
            repeater = new FieldRepeater(fields, schema, $scope, $element, $compile);
            controller.repeater = repeater;
            repeater.renderList();
            if (isMulti && $scope.schemaFieldController.canAdd) {
                var toolbox = angular.element("<s-toolbox>");
                $element.append(toolbox);
                $compile(toolbox)($scope);
            }
        };
        return {
            restrict: "E",
            scope: true,
            require: [ "^^?schema", "^^sField" ],
            controllerAs: "schemaComplexController",
            link: function link($scope, $element, $attrs, requiredControllers) {
                var controller = $scope.schemaComplexController;
                var isMulti = $attrs.multi === "true";
                var schemaController = requiredControllers[0];
                if (schemaController) {
                    controller.$render(schemaController.getSchema(), isMulti, $element);
                } else {
                    controller.$render(null, isMulti, $element);
                }
            },
            controller: SchemaComplexController
        };
    } ]).directive("sToolbar", function() {
        return {
            restrict: "E",
            require: [ "^^sField" ],
            template: '<button class="btn btn-schema btn-success" ng-click="$newComplexValue()"><i class="fa fa-plus"></i></button>',
            scope: false,
            link: function link($scope) {
                $scope.$newComplexValue = function() {
                    $scope.$complexValues.push(new ComplexValue($scope.field.fields));
                };
            }
        };
    }).directive("sToolbox", function() {
        return {
            restrict: "E",
            require: [ "^^sField", "^^sComplex" ],
            template: '<button class="btn btn-schema btn-danger" ng-click="schemaFieldController.remove(complexValue)" ng-if="$complexValues.length > 1"><i class="fa fa-trash-o"></i></button>',
            scope: false
        };
    });
})();

angular.module("voyageone.angular.directives").directive("scrollTo", function() {
    return {
        restrict: "A",
        scope: false,
        link: function link(scope, element, attr) {
            var option = attr.scrollTo;
            if (!option) return;
            option = option.split(",");
            option[1] = parseInt(option[1]) || 200;
            option[2] = parseInt(option[2]) || 0;
            element.on("click", function() {
                var option0;
                if (option[0]) {
                    option0 = $(option[0]);
                    if (option0.length) {
                        option0 = option0.offset().top;
                    } else {
                        option0 = parseInt(option[0]) || 0;
                    }
                } else {
                    option0 = 0;
                }
                $("body").animate({
                    scrollTop: option0 + option[2]
                }, option[1]);
                return false;
            });
        }
    };
}).directive("goTop", function() {
    return {
        restrict: "A",
        scope: false,
        link: function link(scope, element, attrs) {
            var speed = +attrs.goTop;
            $(element).on("click", function() {
                $("body").animate({
                    scrollTop: 0
                }, speed);
                return false;
            });
        }
    };
});

angular.module("voyageone.angular.directives").directive("sticky", function() {
    return {
        restrict: "E",
        scope: false,
        link: function stickyPostLink(scope, element, attr) {
            var $document = $(document);
            var top = parseInt(element.css("top"));
            var topFix = parseInt(attr.topFix) || 0;
            $document.on("scroll", function() {
                var scrollTop = parseInt($document.scrollTop());
                if (scrollTop > top + topFix) {
                    element.css("top", scrollTop - topFix + "px");
                } else {
                    element.css("top", top + "px");
                }
            });
        }
    };
});

angular.module("voyageone.angular.directives").directive("tabInTextarea", function() {
    return {
        restrict: "A",
        link: function link(scope, element) {
            $(element).keydown(function(e) {
                if (e.keyCode === 9) {
                    var start = this.selectionStart;
                    var end = this.selectionEnd;
                    var $this = $(this);
                    var value = $this.val();
                    $this.val(value.substring(0, start) + "\t" + value.substring(end));
                    this.selectionStart = this.selectionEnd = start + 1;
                    e.preventDefault();
                }
            });
        }
    };
});

angular.module("voyageone.angular.directives").directive("vpagination", [ "$templateCache", "$compile", "vpagination", function($templateCache, $compile, vpagination) {
    var templateKey = "voyageone.angular.directives.pagination.tpl.html";
    var templateKeyNoData = "voyageone.angular.directives.paginationNoData.tpl.html";
    if (!$templateCache.get(templateKey)) {
        $templateCache.put(templateKey, '<div class="row"><div class="col-sm-3"><span translate="TXT_SHOWING_NO"></span><span>&nbsp;</span><input class="text-center" type="text" ng-model="curr.pageNo"><span>&nbsp;</span><span>/</span><span>&nbsp;</span><span ng-bind="totalPages"></span><span>&nbsp;</span><span translate="TXT_PAGE"></span><span>&nbsp;</span><button class="btn btn-xs btn-default" type="button" ng-click="goPage(curr.pageNo)" translate="BTN_GO"></button></div><div class="col-sm-4 text-center"><span translate="TXT_PAGER_SIZE"></span><span>&nbsp;</span><select ng-change="changePerPage(perpages.selectedOption)" ng-options="option for option in perpages.availableOptions" ng-model="perpages.selectedOption"></select><span>&nbsp;</span><span translate="TXT_SHOWING"></span><span>&nbsp;</span><span ng-bind="curr.start"></span><span>&nbsp;</span><span>-</span><span>&nbsp;</span><span ng-bind="curr.end"></span><span>&nbsp;</span><span translate="TXT_OF"></span><span>&nbsp;</span><span ng-bind="totalItems"></span><span>&nbsp;</span><span translate="TXT_ITEMS"></span></div><div class="col-sm-5 text-right"><ul class="pagination"><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(1)" ng-disabled="curr.isFirst">&laquo;</a></li><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(curr.pageNo - 1)" ng-disabled="curr.isFirst">&lsaquo;</a></li><li ng-if="curr.isShowStart"><a href>...</a></li><li ng-repeat="page in curr.pages track by $index" ng-class="{active: isCurr(page)}"><a href ng-click="goPage(page)">{{page}}</a></li><li ng-if="curr.isShowEnd"><a href>...</a></li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(curr.pageNo + 1)" ng-disabled="curr.isLast">&rsaquo;</a></li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(totalPages)" ng-disabled="curr.isLast">&raquo;</a></li></ul></div></div>');
    }
    if (!$templateCache.get(templateKeyNoData)) {
        $templateCache.put(templateKeyNoData, '<div class="text-center">&nbsp;<span translate="TXT_SHOWING"></span>&nbsp;<span>0&nbsp;-&nbsp;0</span>&nbsp;<span translate="TXT_OF"></span>&nbsp;<span>0</span>&nbsp;<span translate="TXT_ITEMS"></span></div>');
    }
    var defConfig = {
        curr: 1,
        total: 0,
        size: 10,
        showPageNo: 5,
        fetch: null
    };
    return {
        restrict: "AE",
        replace: false,
        scope: {
            $$configNameForA: "@vpagination",
            $$configNameForE: "@config"
        },
        link: function link(scope, element) {
            var userConfigName = scope.$$configNameForA || scope.$$configNameForE;
            var userConfig = scope.$parent.$eval(userConfigName);
            var userWithDefConfig = angular.extend({}, defConfig, userConfig);
            scope.config = angular.extend(userConfig, userWithDefConfig);
            scope.config.setPageIndex = function(pageIndex) {
                if (scope.config.curr == pageIndex) {
                    scope.config.fetch(scope.config.curr, scope.config.size);
                } else {
                    scope.goPage(pageIndex);
                }
            };
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
                scope.config.size = p.getSize();
                scope.perpages = {
                    availableOptions: [ 10, 20, 50, 100 ],
                    selectedOption: scope.config.size
                };
                var tempHtml;
                if (p.getTotal() == 0) {
                    tempHtml = $compile($templateCache.get(templateKeyNoData))(scope);
                } else {
                    tempHtml = $compile($templateCache.get(templateKey))(scope);
                }
                element.html(tempHtml);
            }
            scope.changePerPage = function(perpage) {
                scope.config.size = parseInt(perpage);
                scope.config.curr = 1;
                p.goPage(parseInt(scope.config.curr));
            };
        }
    };
} ]);

angular.module("voyageone.angular.directives").directive("ngCharMaxlength", function() {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function link(scope, elm, attr, ctrl) {
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
        link: function link(scope, elm, attr, ctrl) {
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
        link: function link(scope, elm, attr, ctrl) {
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
        link: function link(scope, elm, attr, ctrl) {
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
        link: function link(scope, elm, attr, ctrl) {
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
        link: function link(scope, elm, attr, ctrl) {
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

angular.module("voyageone.angular.factories").factory("$dialogs", [ "$uibModal", "$filter", "$templateCache", function($uibModal, $filter, $templateCache) {
    var templateName = "voyageone.angular.factories.dialogs.tpl.html";
    var template = '<div class="vo_modal vo-dialogs"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="close()"><span aria-hidden="true"><i ng-click="close()" class="fa fa-close"></i></span></button><h5 class="modal-title"><i class="fa fa-exclamation-triangle"></i>&nbsp;<span ng-bind-html="title"></span></h5></div><div class="modal-body"><div class="text-left" ng-bind-html="content"></div></div><div class="modal-footer"><button class="btn btn-default btn-sm" ng-if="!isAlert" ng-click="close()" translate="BTN_CANCEL"></button><button class="btn btn-sm {{::isAlert?\'btn-default\':\'btn-vo\'}}" ng-click="ok()" translate="BTN_OK"></button></div></div>';
    $templateCache.put(templateName, template);
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
        var modalInstance = $uibModal.open({
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
            title: title || "TXT_ALERT",
            content: content,
            isAlert: true
        }).result;
    };
} ]).factory("confirm", [ "$dialogs", function vConfirm($dialogs) {
    return function(content, title) {
        return $dialogs({
            title: title || "TXT_CONFIRM",
            content: content,
            isAlert: false
        }).result;
    };
} ]);

angular.module("voyageone.angular.factories").factory("interceptorFactory", function() {
    var UNKNOWN_CODE = "5";
    var CODE_SYS_REDIRECT = "SYS_REDIRECT";
    var MSG_TIMEOUT = "300001";
    var MSG_LOCKED = "A003";
    var MSG_MANYFAILS = "A004";
    var MSG_MISSAUTHENTICATION = "A005";
    var MSG_CHANGEPASS = "A006";
    var MSG_LOGINAGAIN = "A001";
    var CODE_SEL_CHANNEL = "SYS_0";
    function autoRedirect(res) {
        if (res.code != CODE_SYS_REDIRECT && res.code != CODE_SEL_CHANNEL) {
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
    function adminReLogin(res) {
        if (res.code != MSG_LOGINAGAIN) {
            return false;
        }
        location.href = "/";
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
        request: function request(config) {
            return config;
        },
        response: function response(res) {
            var result = res.data;
            if (autoRedirect(result) || sessionTimeout(result) || adminReLogin(result)) {
                return res;
            }
            unknownException(res);
            return res;
        },
        requestError: function requestError(config) {
            return config;
        },
        responseError: function responseError(res) {}
    };
}).config([ "$httpProvider", function($httpProvider) {
    $httpProvider.interceptors.push("interceptorFactory");
} ]);

angular.module("voyageone.angular.factories").factory("notify", [ "$filter", function($filter) {
    var notifyStyle = {
        noticeTip: {
            html: "<div><span data-notify-text/></div>",
            classes: {
                base: {
                    "min-width": "150px",
                    "background-color": "#ee903d",
                    padding: "5px",
                    color: "white",
                    border: "1px solid #ee903d"
                },
                superBlue: {
                    color: "white",
                    "background-color": "blue"
                }
            }
        }
    };
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
        if (options.type === "noticeTip") {
            $.notify.addStyle("noticeTip", notifyStyle.noticeTip);
            options.style = "noticeTip";
            _.extend(options, options.opts);
            return $.notify(options.jqObj, options.message, options);
        }
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
    notify.noticeTip = function(jqObj, message, options) {
        return notify({
            jqObj: jqObj,
            type: "noticeTip",
            message: message,
            opts: options
        });
    };
    return notify;
} ]);

angular.module("voyageone.angular.factories").factory("pppAutoImpl", [ "$q", "$modal", function($q, $modal) {
    return function(declares, viewBaseUrl, jsBaseUrl) {
        if (!declares.$$$ || !declares.$$$.impl) declares.$$$ = {
            impl: declarePopupMethods(declares, viewBaseUrl, jsBaseUrl, "")
        };
        return declares.$$$.impl;
    };
    function declarePopupMethods(declares, viewBaseUrl, jsBaseUrl, popupBaseKey) {
        var impl = {};
        if (popupBaseKey) popupBaseKey += "/";
        _.each(declares, function(declare, parentDir) {
            if (!declare.popupKey) {
                if (_.isObject(declare) || _.isArray(declare)) _.extend(impl, declarePopupMethods(declare, viewBaseUrl, jsBaseUrl, popupBaseKey + parentDir, $q, $modal));
                return;
            }
            var options = _.clone(declare.options) || {};
            var pathBase = "/" + popupBaseKey;
            if (_.isString(parentDir)) pathBase += parentDir + "/";
            pathBase += declare.popupKey;
            options.templateUrl = viewBaseUrl + pathBase + ".tpl.html";
            options.controllerUrl = jsBaseUrl + pathBase + ".ctl";
            if (declare.controllerAs || declare.controller) options.controller = getControllerName(declare.popupKey);
            if (declare.controllerAs) options.controller += " as " + (_.isString(declare.controllerAs) ? declare.controllerAs : "ctrl");
            impl[declare.popupKey] = function(_context) {
                if (_context) options.resolve = {
                    context: function context() {
                        return _context;
                    }
                };
                var defer = $q.defer();
                require([ options.controllerUrl ], function() {
                    defer.resolve($modal.open(options).result);
                });
                return defer.promise;
            };
        });
        return impl;
    }
    function getControllerName(key) {
        return key.replace(/\.(\w)/g, function(m, m1) {
            return m1.toUpperCase();
        }).replace(/^(\w)/, function(m, m1) {
            return m1.toLowerCase();
        }) + "PopupController";
    }
} ]);

angular.module("voyageone.angular.factories").factory("selectRowsFactory", function() {
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
        this.clearSelectedList = function() {
            _selectRowsInfo.selList = [];
            _selectRowsInfo.selFlag = [];
        };
        this.selectRowsInfo = _selectRowsInfo;
    };
});

angular.module("voyageone.angular.factories").factory("vpagination", function() {
    return function(config) {
        var _pages, _lastTotal = 0, _showPages = [], defaultPage = config.size;
        this.getTotal = function() {
            return config.total;
        };
        this.getSize = function() {
            return config.size;
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
            defaultPage = config.size;
            config.fetch(config.curr, config.size);
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
            if (_lastTotal != config.total || config.size !== defaultPage) {
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
            return config.curr == page && config.size === defaultPage;
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

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

angular.module("voyageone.angular.filter").filter("gmtDate", [ "$filter", function($filter) {
    return function(input, format) {
        var miliTimes;
        if (!input) {
            return "";
        }
        switch (typeof input === "undefined" ? "undefined" : _typeof(input)) {
          case "string":
            input = new Date(input);
            miliTimes = input.getTime() + new Date().getTimezoneOffset() * 60 * 1e3 * -1;
            break;

          case "number":
            miliTimes = new Date(input);
            break;

          default:
            console.error("传入了未知类型数据！！！");
        }
        return $filter("date")(new Date(miliTimes), format);
    };
} ]);

angular.module("voyageone.angular.filter").filter("stringCutter", function() {
    return function(value, wordWise, max, tail) {
        if (!value) return "";
        max = parseInt(max, 10);
        if (!max) return value;
        if (value.length <= max) return value;
        value = value.substr(0, max);
        if (wordWise) {
            var lastSpace = value.lastIndexOf(" ");
            if (lastSpace != -1) {
                value = value.substr(0, lastSpace);
            }
        }
        return value + (tail || " …");
    };
});

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function(obj) {
    return typeof obj;
} : function(obj) {
    return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
};

angular.module("voyageone.angular.vresources", []).provider("$vresources", [ "$provide", function($provide) {
    function getActionUrl(root, action) {
        return root + (root.lastIndexOf("/") === root.length - 1 ? "" : "/") + action;
    }
    function actionHashcode(md5, root, actionName, args, cacheWith) {
        var argsJson = angular.toJson(args);
        var otherKeyJson = !cacheWith ? "" : angular.toJson(cacheWith);
        var md5Arg = root + actionName + argsJson + otherKeyJson;
        return md5.createHash(md5Arg);
    }
    function closureDataService(name, actions, cacheKey) {
        var _ServiceClass, root = actions.root;
        if (!actions) {
            return;
        }
        if ((typeof actions === "undefined" ? "undefined" : _typeof(actions)) !== "object") {
            console.log("Failed to new DataResource: [" + actions + "] is not a object");
            return;
        }
        if (!root) {
            console.log("Failed to new DataResource: no root prop" + angular.toJson(actions));
            return;
        }
        _ServiceClass = function _ServiceClass(ajax, $sessionStorage, $localStorage, md5, $q) {
            this._a = ajax;
            this._sc = $sessionStorage;
            this._lc = $localStorage;
            this._5 = md5;
            this._q = $q;
            this._c = {};
        };
        _.each(actions, function(option, actionName) {
            var _url, _root, _resolve, _reject, _cacheFlag, _cacheWith;
            if (_.isString(option)) _url = option; else if (_.isObject(option)) {
                _url = option.url;
                _resolve = option.then;
                _root = option.root;
                _cacheFlag = option.cache;
                _cacheWith = option.cacheWith;
                if (!_.isArray(_cacheWith)) _cacheWith = null; else {
                    var __cacheWith = _cacheWith.map(function(cacheKeyName) {
                        return cacheKey[cacheKeyName];
                    }).filter(function(cacheKeyValue) {
                        return !!cacheKeyValue;
                    });
                    if (__cacheWith.length !== _cacheWith.length) _cacheFlag = 0; else _cacheWith = __cacheWith;
                }
            }
            if (!_url) {
                console.error("URL is undefined", option);
                return;
            }
            if (_root === false) _root = ""; else if (_root === null || _root === undefined || _root === true) _root = root;
            if (_.isArray(_resolve)) {
                _reject = _resolve[1];
                _resolve = _resolve[0];
            }
            if (!_.isFunction(_resolve)) _resolve = function _resolve(res) {
                return res;
            };
            if (!_cacheFlag || _cacheFlag > 3) _cacheFlag = 0;
            _url = getActionUrl(_root, _url);
            _ServiceClass.prototype[actionName] = _cacheFlag === 0 ? function(args, option) {
                return this._a.post(_url, args, option).then(_resolve, _reject);
            } : function(args, option) {
                var deferred, result;
                var session = this._sc, local = this._lc, hash = actionHashcode(this._5, root, actionName, args, _cacheWith), promise = this._c[hash];
                if (promise) return promise;
                deferred = this._q.defer();
                promise = deferred.promise;
                this._c[hash] = promise;
                result = _cacheFlag === 2 ? session[hash] : _cacheFlag === 3 ? local[hash] : null;
                if (result !== null && result !== undefined) deferred.resolve(result); else this._a.post(_url, args, option).then(function(res) {
                    result = _resolve(res);
                    switch (_cacheFlag) {
                      case 2:
                        session[hash] = result;
                        break;

                      case 3:
                        local[hash] = result;
                        break;
                    }
                    deferred.resolve(result);
                }, function(res) {
                    result = _reject(res);
                    deferred.reject(result);
                });
                return promise;
            };
        });
        $provide.service(name, [ "ajaxService", "$sessionStorage", "$localStorage", "md5", "$q", _ServiceClass ]);
    }
    this.$get = function() {
        return {
            register: function register(name, actions, cacheKey) {
                if (!actions) return;
                if ((typeof actions === "undefined" ? "undefined" : _typeof(actions)) !== "object") return;
                if (actions.root) {
                    closureDataService(name, actions, cacheKey);
                    return;
                }
                for (var childName in actions) {
                    if (actions.hasOwnProperty(childName)) {
                        this.register(childName, actions[childName], cacheKey);
                    }
                }
            }
        };
    };
} ]);

$Ajax.$inject = [ "$http", "$q", "blockUI", "$timeout" ];

AjaxService.$inject = [ "$q", "$ajax", "messageService" ];

angular.module("voyageone.angular.services").service("$ajax", $Ajax).service("ajaxService", AjaxService).config([ "$httpProvider", function($httpProvider) {
    $httpProvider.defaults.headers.common = {
        "X-Requested-With": "XMLHttpRequest12"
    };
} ]);

function $Ajax($http, $q, blockUI, $timeout) {
    this.$http = $http;
    this.$q = $q;
    this.blockUI = blockUI;
    this.$timeout = $timeout;
}

$Ajax.prototype.post = function(url, data, option) {
    var defer = this.$q.defer(), blockUI = this.blockUI, $timeout = this.$timeout, cancelBlock = null;
    option = option || {
        autoBlock: true,
        blockDelay: 1e3
    };
    var autoBlock = option.autoBlock, blockDelay = option.blockDelay;
    if (autoBlock) {
        cancelBlock = function(blockPromise) {
            return function() {
                $timeout.cancel(blockPromise);
                blockUI.stop();
            };
        }($timeout(function() {
            blockUI.start();
        }, blockDelay));
    }
    if (data === undefined) {
        data = {};
    }
    this.$http.post(url, data).then(function(response) {
        var res = response.data;
        if (cancelBlock) cancelBlock();
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
        if (cancelBlock) cancelBlock();
        defer.reject(null, response);
    });
    return defer.promise;
};

function AjaxService($q, $ajax, messageService) {
    this.$q = $q;
    this.$ajax = $ajax;
    this.messageService = messageService;
}

AjaxService.prototype.post = function(url, data, option) {
    var defer = this.$q.defer();
    this.$ajax.post(url, data, option).then(function(res) {
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

CookieService.$inject = [ "$cookies" ];

angular.module("voyageone.angular.services").service("cookieService", CookieService);

var keys = {
    language: "voyageone.user.language",
    company: "voyageone.user.company",
    channel: "voyageone.user.channel",
    application: "voyageone.user.application"
};

function makeProps(key) {
    return function(val) {
        if (arguments.length === 1) {
            return this.set(key, val);
        } else if (arguments.length > 1) {
            return this.set(key, arguments);
        }
        return this.get(key);
    };
}

function Cookie(key, value) {
    this.key = key;
    this.value = value;
}

function CookieService($cookies) {
    this.$cookies = $cookies;
}

CookieService.prototype.get = function(key) {
    var result = this.$cookies.get(key);
    if (result === undefined || result === null) return "";
    if (result.indexOf("{") !== 0) return result;
    var item = JSON.parse(result);
    return item.value;
};

CookieService.prototype.set = function(key, value) {
    var item = new Cookie(key, value);
    return this.$cookies.put(key, angular.toJson(item));
};

CookieService.prototype.removeAll = function() {
    this.$cookies.remove(keys.language);
    this.$cookies.remove(keys.company);
    this.$cookies.remove(keys.channel);
    this.$cookies.remove(keys.application);
};

CookieService.prototype.language = makeProps(keys.language);

CookieService.prototype.company = makeProps(keys.company);

CookieService.prototype.channel = makeProps(keys.channel);

CookieService.prototype.application = makeProps(keys.application);

MessageService.$inject = [ "alert", "confirm", "notify" ];

angular.module("voyageone.angular.services").service("messageService", MessageService);

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

MessageService.prototype = {
    show: function show(res) {
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

PermissionService.$inject = [ "$rootScope" ];

angular.module("voyageone.angular.services").service("permissionService", PermissionService);

function PermissionService($rootScope) {
    this.$rootScope = $rootScope;
    this.permissions = [];
}

PermissionService.prototype = {
    setPermissions: function setPermissions(permissions) {
        this.permissions = permissions;
        this.$rootScope.$broadcast("permissionsChanged");
    },
    has: function has(permission) {
        return _.contains(this.permissions, permission.trim());
    }
};

TranslateService.$inject = [ "$translate" ];

angular.module("voyageone.angular.services").service("translateService", TranslateService);

function TranslateService($translate) {
    this.$translate = $translate;
}

TranslateService.prototype = {
    languages: {
        en: "en",
        zh: "zh"
    },
    setLanguage: function setLanguage(language) {
        if (!_.contains(this.languages, language)) {
            language = this.getBrowserLanguage();
        }
        this.$translate.use(language);
        return language;
    },
    getBrowserLanguage: function getBrowserLanguage() {
        var currentLang = navigator.language;
        if (!currentLang) currentLang = navigator.browserLanguage;
        return currentLang.substr(0, 2);
    }
};