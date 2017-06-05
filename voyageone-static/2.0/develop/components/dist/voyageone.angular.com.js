

/*****************************/

/**
 * angular component head file。
 * 声明各个组件的父模块
 * 
 * create by Jonas on 2016-06-01 14:00:39
 */

// 各级子模块, 子系统可以单独引入

angular.module('voyageone.angular.controllers', []);
angular.module('voyageone.angular.directives', []);
angular.module('voyageone.angular.factories', []);
angular.module('voyageone.angular.services', []);
angular.module('voyageone.angular.filter', []);

// 总模块, 供子系统一次性引入
angular.module('voyageone.angular', [
    'voyageone.angular.controllers',
    'voyageone.angular.directives',
    'voyageone.angular.factories',
    'voyageone.angular.services',
    'voyageone.angular.filter'
]);

/*****************************/

/**
 * @Description:
 * select tables items
 * @User: linanbin
 * @Version: 2.0.0, 15/12/16
 */
angular.module("voyageone.angular.controllers").controller("selectRowsCtrl", function ($scope) {
    $scope.selectAll = selectAll;
    $scope.selectOne = selectOne;
    $scope.isAllSelected = isAllSelected;
    /**
     * 全部选中当前页数据
     * @param objectList
     */
    function selectAll(objectList, id) {
        objectList.selAllFlag = !objectList.selAllFlag;
        if (!id) {
            id = "id";
        }
        // 循环处理全选中的数据
        angular.forEach(objectList.currPageRows, function (object) {
            // 单签页面所有产品选中flag被标示
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

    /**
     * 选中单条数据
     * @param currentId
     * @param objectList
     */
    function selectOne(currentId, objectList, id) {
        if (!id) {
            id = "id";
        }
        if (objectList.hasOwnProperty("selList")) {
            angular.forEach(objectList.currPageRows, function (object) {
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
        angular.forEach(objectList.currPageRows, function (object) {
            if (tempList && tempList.indexOf(object[id]) == -1) {
                objectList.selAllFlag = false;
            }
        });
    }

    /**
     * 判断当前页是否为全选中
     * @param objectList
     * @param id
     */
    function isAllSelected(objectList, id) {
        if (!id) {
            id = "id";
        }
        if (objectList != undefined) {
            objectList.selAllFlag = true;
            var tempList = _.pluck(objectList.selList, id);
            angular.forEach(objectList.currPageRows, function (object) {
                if (tempList && tempList.indexOf(object[id]) == -1) {
                    objectList.selAllFlag = false;
                }
            });
            return objectList.selAllFlag;
        }
        return false;
    }
});


/*****************************/

/**
 * @Description:
 * 显示html的popover的共同方法
 * @User: linanbin
 * @Version: 2.0.0, 15/12/14
 */
angular.module("voyageone.angular.controllers").controller("showPopoverCtrl", function ($scope,$searchAdvanceService2,$promotionHistoryService) {

    $scope.templateAction = {
        "promotionDetailPopover":{
            templateUrl: 'promotionDetailTemplate.html',
            title: 'Title'
        },
        "advanceSkuPopover":{
            templateUrl: 'advanceSkuTemplate.html',
            title: 'Title'
        }
    };

    $scope.showInfo = showInfo;
    $scope.popoverAdvanceSku = popoverAdvanceSku;
    $scope.popoverPromotionDetail = popoverPromotionDetail;
    $scope.getCartQty = getCartQty;

    function showInfo(values) {
        if (values == undefined || values == '') {
            return '';
        }
        var tempHtml = "";
        if (values instanceof Array) {
            angular.forEach(values, function (data, index) {
                tempHtml += data;
                if (index !== values.length) {
                    tempHtml += "<br>";
                }
            });
        }
        else if(values.isUseComplexTemplate == true){
            $scope.dynamicPopover = {
                type: values.type,
                value1: values.value,
                value2: values.value2,
                value3: values.value3,
                templateUrl: 'dynamicPopoverTemplate.html'
            };
        }else {
            tempHtml += values;
        }
        return tempHtml;
    }

    /**
     * 高级检索   显示sku
     */
    function popoverAdvanceSku(code, skus , entity){

        if(entity.isOpen){
            entity.isOpen = false;
            return;
        }
        entity.isOpen = true;

        $searchAdvanceService2.getSkuInventory(code).then(function(resp) {
            var skuDetails = [],
                skuInventories = resp.data;
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

    /**
     * 高级线索   显示活动详情
     */
    function popoverPromotionDetail(code,entity){

        if(entity.isOpen){
            entity.isOpen = false;
            return;
        }
        entity.isOpen = true;

        $promotionHistoryService.getUnduePromotion({code: code}).then(function(resp) {
            $scope.promotionDetail = resp.data;
        });

    }

    /**
     * code对应平台的库存
     * @param code
     * @param codeCartQty
     */
    function getCartQty(codeCartQty){
        $scope.codeCartQty = codeCartQty;
    }
});


/*****************************/

/**
 * @Date: 2016-06-22 15:59:47
 * @User: Jonas
 */
(function () {
    /**
     * 抄自聚美后台 js
     */
    function sizeof(str) {
        if (str == undefined || str == null) {
            return 0;
        }
        var regex = str.match(/[^\x00-\xff]/g);
        return (str.length + (!regex ? 0 : regex.length));
    }

    /**
     * 第三方函数包装
     */
    function getByteLength(str) {
        return sizeof(str);
    }

    /**
     * directive 创建包装
     */
    function makeByteLength(attrName, checkLength) {
        return function () {
            return {
                restrict: "A",
                require: 'ngModel',
                scope: false,
                link: function (scope, element, attrs, ngModelController) {
                    var length = attrs[attrName];
                    if (!length)
                        return;

                    if(checkLength(getByteLength(scope.field.value), length)){
                        ngModelController.$setValidity(attrName,true);
                    }else{
                        ngModelController.$setValidity(attrName,false);
                    }

                    ngModelController.$parsers.push(function (viewValue) {
                        ngModelController.$setValidity(attrName, checkLength(getByteLength(viewValue), length));
                        return viewValue;
                    });

                }
            };
        };
    }

    angular.module("voyageone.angular.directives")
        .directive("maxbytelength", makeByteLength("maxbytelength", function (byteLength, maxLength) {
            return byteLength <= maxLength;
        }))
        .directive("minbytelength", makeByteLength("minbytelength", function (byteLength, minLength) {
            return byteLength >= minLength;
        }));
}());

/*****************************/

/**
 * @Date: 2015-11-19 15:13:02
 * @User: Jonas
 * @Version: 2.0.0
 */
angular.module("voyageone.angular.directives").directive("dateModelFormat", function ($filter) {
    return {
        restrict: "A",
        require: "ngModel",
        link: function (scope, elem, attrs, ngModel) {
            ngModel.$parsers.push(function (viewValue) {
                return $filter("date")(viewValue, attrs.dateModelFormat || "yyyy-MM-dd HH:mm:ss");
            });
        }
    };
});


/*****************************/

/**
 * 编辑产品originalTitleCn
 * @param productInfo 产品信息
 * @author piao
 */
angular.module("voyageone.angular.directives").directive("editTitle", function () {

    function EditTitleController($scope, $attrs, $element, notify, $translate,
                                 productDetailService) {
        this.$attrs = $attrs;
        this.$scope = $scope;
        this.$element = $element;
        this.notify = notify;
        this.$translate = $translate;
        this.productDetailService = productDetailService;
    }

    EditTitleController.prototype.init = function () {
        var self = this,
            _products = {
                originalTitleCn: angular.copy(self.$scope.data.common.fields.originalTitleCn),
                prodId: angular.copy(self.$scope.data.prodId)
            };

        self.productInfo = _products;

        self.dynamicPopover = {
            title: '产品名称 ',
            templateUrl: 'myPopoverTemplate.html'
        };
    };

    EditTitleController.prototype.save = function () {
        var self = this,
            productInfo = self.productInfo;

        var prodId = productInfo.prodId,
            originalTitleCn = productInfo.originalTitleCn.replace(/[.\n]/g, '');

        //如果值未改变
        if(angular.equals(originalTitleCn,self.$scope.data.common.fields.originalTitleCn)){
            self.isOpen = false;
            return;
        }

        if (prodId && originalTitleCn) {
            self.productDetailService.updateOriginalTitleCn({
                                                                prodId: prodId,
                                                                originalTitleCn: originalTitleCn
                                                            }).then(function () {
                self.isOpen = false;
                self.$scope.data.common.fields.originalTitleCn = originalTitleCn;
                self.notify.success(self.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            });
        }

    };

    return {
        restrict: 'E',
        scope: {
            data: "=data"
        },
        controller: ['$scope', '$attrs', '$element', 'notify', '$translate', 'productDetailService',
                     EditTitleController],
        controllerAs: 'ctrl',
        template: '<div ng-init="ctrl.init()">'
                  + '<script type="text/ng-template" id="myPopoverTemplate.html">'
                  + '<div class="form-group">'
                  + '<textarea class="form-control no-resize" style="min-height: 70px;width: 200px" ng-model="ctrl.productInfo.originalTitleCn"></textarea>'
                  + '</div>'
                  + '<div class="form-group pull-right">'
                  + '<button class="btn btn-success" ng-click="ctrl.save()"><i class="fa fa-save"></i></button>'
                  + '</div>'
                  + '</script>'
                  + '<button uib-popover-template="ctrl.dynamicPopover.templateUrl" popover-title="{{ctrl.dynamicPopover.title}}" popover-is-open="ctrl.isOpen" type="button" class="btn btn-default" title="{{ctrl.$scope.data.common.fields.originalTitleCn}}" ng-if="ctrl.$scope.data.common.fields.originalTitleCn">{{ctrl.$scope.data.common.fields.originalTitleCn  | limitTo: 25}}</button>'
                  + '</div>'

    }
});


/*****************************/

/**
 * @Date: 2015-11-19 15:13:02
 * @User: Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.directives").directive("enterClick", function () {
    return {
        restrict: "A",
        link: function (scope, elem, attr) {
            angular.element(elem).on("keyup", function (e) {
                if (e.keyCode !== 13) return;
                var selectExp = attr.enterClick;
                var targetElem, handler = function () {
                    targetElem.triggerHandler("click");
                };
                try {
                    targetElem = document.querySelector(selectExp);
                } catch (e) {
                    targetElem = null;
                }
                if (!targetElem) {
                    // 如果取不到元素，则尝试作为表达式执行
                    handler = function () {
                        scope.$eval(selectExp);
                    };
                } else {
                    targetElem = angular.element(targetElem);
                    // 如果元素存在，但是是禁用状态的，放弃执行
                    if (targetElem.attr("disabled")) return;
                }
                handler();
            });
        }
    };
});


/*****************************/

/**
 * @Description:
 * 比较两个值是否相等
 * @Date:    2017-01-10 17:35:22
 * @User:    edward
 * @Version: 2.10.0
 */
angular.module("voyageone.angular.directives").directive("equalTo", function () {
    return {
        restrict: "A",
        require: "ngModel",
        scope: {
            equalTo:"="
        },
        link: function (scope, ele, attrs, ctrl) {

            var target = attrs["equalTo"];//获取自定义指令属性键值

            if (target) {//判断键是否存在
                scope.$watch("equalTo", function () {//存在启动监听其值
                    ctrl.$validate()//每次改变手动调用验证
                });

                ctrl.$validators.equalTo = function (viewVale) {//自定义验证器内容

                    return scope.equalTo == viewVale;//是否等于passwordConfirm的值
                };
            }
        }
    }
});

/*****************************/

/**
 * @Description:
 * 引入对上传框插件 fileStyle 的指令支持 基于Bootstrap Filestyle
 * @Date:    2015-11-19 17:35:22
 * @User:    Jonas
 * @Version: 2.0.0
 */
angular.module("voyageone.angular.directives").directive("fileStyle", function () {

    function FileStyleController($scope,$element){
        this.scope = $scope;
        this.element = $element;
    }

    FileStyleController.prototype.init = function(attrs){
        var options;

        if(attrs.fileStyle != null && attrs.fileStyle != "")
            options = eval("(" + attrs.fileStyle + ")");

        this.element.filestyle(options);
    };

    return {
        restrict: "A",
        scope:true,
        controller: FileStyleController,
        controllerAs: 'styleCtrl',
        link: function($scope,$element,$attrs){
            $scope.styleCtrl.init($attrs);
        }
    };
});


/*****************************/

/**
 * @Description:
 * table中无数据范围的数据
 * @User: linanbin
 * @Version: 2.0.0, 15/12/11
 */
angular.module("voyageone.angular.directives").directive("ifNoRows", function ($templateCache, $compile) {
    var tempNoDataKey = "voyageone.angular.directives.ifNoRows.tpl.html";
    // 没有数据显示警告
    if (!$templateCache.get(tempNoDataKey)) {
        $templateCache.put(tempNoDataKey, '<div class="text-center text-hs" id="noData"><h4 class="text-vo"><i class="icon fa fa-warning"></i>&nbsp;<span translate="TXT_ALERT"></span></h4><span translate="TXT_MSG_NO_DATE"></span></dv>');
    }
    return {
        restrict: "A",
        replace: false,
        scope: {
            $$data: "@ifNoRows"
        },
        link: function (scope, element) {
            scope.$parent.$watch(scope.$$data, function () {
                // 如果数据不存在则显示警告信息
                if (scope.$parent.$eval(scope.$$data) == 0) {
                    element.find("#noData").remove();
                    element.append($compile($templateCache.get(tempNoDataKey))(scope));
                } else {
                    element.find("#noData").remove();
                }
            });
        }
    };
});


/*****************************/

/**
 * @Description: 用于替换成cms2中可显示的图片url
 *
 * @User: linanbin
 * @Version: 2.0.0, 16/5/12
 */
angular.module("voyageone.angular.directives").directive("image", function () {
    return {
        restrict: "A",
        scope: {
            image: "@"
        },
        link: function (scope, element, attrs) {
            attrs.$observe('image', function () {
                if (scope.image != null && scope.image != "" && scope.$root.imageUrl != undefined)
                    element[0].src = scope.$root.imageUrl.replace('%s', scope.image);
            });
        }
    };
});


/*****************************/

angular.module("voyageone.angular.directives").directive("input", function () {
    return {
        restrict: "E",
        require: ['?ngModel'],
        link: function (scope, element, attr) {

            var type = attr.type;

            if (!type)
                return;

            type = type.toLowerCase();

            if (type !== 'number')
                return;

            element.on('keypress', function (event) {

                var charCode = event.charCode;
                var lastInputIsPoint = element.data('lastInputIsPoint');

                if (charCode !== 0 && charCode !== 46 && (charCode < 48 || charCode > 57)) {
                    event.preventDefault();
                    return;
                }

                if (charCode === 46) {

                    if (lastInputIsPoint || this.value.indexOf('.') > -1) {
                        event.preventDefault();
                        return;
                    }
                    element.data('lastInputIsPoint', true);
                    return;
                }

                element.data('lastInputIsPoint', false);
            });
        }
    };
}).directive("scale", function () {
    return {
        restrict: "A",
        require: ['ngModel'],
        link: function (scope, element, attr, ctrls) {

            var type = attr.type;
            var ngModelController = ctrls[0];

            if (!type)
                return;

            type = type.toLowerCase();

            if (type !== 'number')
                return;

            //默认为2位
            var scale , _length;

            var _numArr =  attr.scale.split(",");

            if(_numArr.length !== 2){

                console.warn("scale格式为{ 位数 },{ 精度 } 默认值=》位数：15位，精度为小数点2位。");

                /**设置默认值 长度为15  小数点精度为2位*/
                _length = 15;
                scale = 2;

            }else{

                _length = _numArr[0];
                scale = _numArr[1];

            }

            element.on('keyup', function () {

                var regex;

                if(scale != 0)
                    regex = new RegExp("^\\d+(\\.\\d{1," + scale + "})?$");
                else
                    regex = new RegExp("^\\d+$");


                if (regex.test(this.value))
                    return;

                ngModelController.$setViewValue(this.value.substr(0, this.value.length - 1));
                ngModelController.$render();

            }).on("keypress",function(event){

                var _value = angular.copy(this.value);

                if(_value.toString().length >= _length){
                    event.preventDefault();
                }

            });
        }
    };
});

/*****************************/

/**
 * 错误类型, 直接通过 form 验证所得的 $error 获取相应的 key, 并通过翻译获取信息结果
 */
var errorTypes = {
    email: 'INVALID_EMAIL',
    url: 'INVALID_URL',
    date: 'INVALID_DATE',
    datetimelocal: 'INVALID_DATETIMELOCAL',
    color: 'INVALID_COLOR',
    range: 'INVALID_RANGE',
    month: 'INVALID_MONTH',
    time: 'INVALID_TIME',
    week: 'INVALID_WEEK',
    number: 'INVALID_NUMBER',
    required: 'INVALID_REQUIRED',
    maxlength: 'INVALID_MAXLENGTH',
    minlength: 'INVALID_MINLENGTH',
    minbytelength: 'INVALID_MINLENGTH',
    maxbytelength: 'INVALID_MAXLENGTH',
    max: 'INVALID_MAX',
    min: 'INVALID_MIN',
    pattern: 'INVALID_PATTERN',
    equalTo: "INVALID_NOT_EQUAL"
};

/**
 * @Description:
 * 对 Angular 的 Form 验证提供统一的信息输出支持。
 *
 * @Date:    2016-05-18 16:22:46
 * @User:    Jonas
 * @Version: 1.0.0
 */
angular.module("voyageone.angular.directives")

    .directive('voMessage', function ($translate) {
        return {
            restrict: "E",
            template: '{{$message}}',
            require: '^^form',
            scope: {
                'target': '='
            },
            link: function (scope, elem, attrs, formController) {

                function show(message) {
                    scope.$message = message;
                    elem.fadeIn();
                }

                function hide() {
                    elem.fadeOut();
                }

                var formName;

                // 初始化时保持隐藏
                elem.hide();

                formName = formController.$name;

                // 对指定 form 下字段的错误信息进行监视
                // 如果有变动, 就显示第一个错误的提示信息
                scope.$watch('target.$error',

                    function ($error) {

                        if (!$error) return;

                        // 取所有错误的 angular 错误名称, 如 required
                        var errorKeys = Object.keys($error);

                        var elementName = scope.target.$name;

                        // 这一步可能获取的并不准确
                        // 因为元素的 name 有可能重复
                        var targetElement = $('[name="' + formName + '"] [name="' + elementName + '"]');

                        // 如果有友好名称的话, 就用友好的
                        var translateParam = {field: targetElement.attr('title') || elementName};

                        // 取第一个
                        var error = errorKeys[0];

                        // 如果没有错误就不用继续处理错误信息了
                        if (!error) {
                            hide();
                            return;
                        }

                        // 尝试获取用户定义的错误提示信息
                        if (attrs[error]) {
                            // 如果用户自定义了相关错误的信息
                            // 就显示自定义信息
                            show(attrs[error]);
                        } else {
                            // 如果用户没有设定提示信息，那么就自己根据参数生成
                            if (['maxlength', 'minlength', 'maxbytelength', 'minbytelength', 'max', 'min', 'pattern', 'equalTo'].indexOf(error) > -1) {
                                if (!(translateParam.value = targetElement.attr(error)) && 'pattern' === error)
                                    translateParam.value = targetElement.attr('ng-pattern');
                            }

                            // 取错误的翻译 Key, 如 required -> INVALID_REQUIRED, 参加上面的 var errorTypes
                            $translate(errorTypes[error], translateParam).then(show, show);
                        }

                    }, true);

            }
        }
    });


/*****************************/

/**
 * @Description:
 * 用于菜单目录二级展开
 * @User:    Jonas
 * @Version: 0.2.0, 2015-12-07
 */
angular.module("voyageone.angular.directives").directive("uiNav", function () {
    return {
        restrict: "AC",
        link: function (scope, el) {
            var _window = $(window), _mb = 768, wrap = $(".app-aside"), next, backdrop = ".dropdown-backdrop";
            // unfolded
            el.on("click", "a", function (e) {
                next && next.trigger("mouseleave.nav");
                var _this = $(this);
                _this.parent().siblings(".active").toggleClass("active");
                _this.next().is("ul") && _this.parent().toggleClass("active") && e.preventDefault();
                // mobile
                _this.next().is("ul") || _window.width() < _mb && $(".app-aside").removeClass("show off-screen");
            });
            // folded & fixed
            el.on("mouseenter", "a", function (e) {
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
                next.on("mouseleave.nav", function (e) {
                    $(backdrop).remove();
                    next.appendTo(_this.parent());
                    next.off("mouseleave.nav").css("top", "auto").css("bottom", "auto");
                    _this.parent().removeClass("active");
                });
                $(".smart").length && $('<div class="dropdown-backdrop"/>').insertAfter(".app-aside").on("click", function (next) {
                    next && next.trigger("mouseleave.nav");
                });
            });
            wrap.on("mouseleave", function (e) {
                next && next.trigger("mouseleave.nav");
                $("> .nav", wrap).remove();
            });
        }
    };
});


/*****************************/

/**
 * The ng-thumb directive
 * @author: piao wenjie
 * @version: 2.3.0, 2016-7-1
 */
'use strict';
angular.module('voyageone.angular.directives').directive('ngThumb', ['$window', function($window) {

        var helper = {
            support: !!($window.FileReader && $window.CanvasRenderingContext2D),
            isFile: function(item) {
                return angular.isObject(item) && item instanceof $window.File;
            },
            isImage: function(file) {
                var type =  '|' + file.type.slice(file.type.lastIndexOf('/') + 1) + '|';
                return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
            }
        };

        return {
            restrict: 'A',
            link: function(scope, element, attributes) {
                if (!helper.support) return;

                var params = scope.$eval(attributes.ngThumb);

                if (!helper.isImage(params.file)) return;

                var fileReader = new FileReader();

                fileReader.readAsDataURL(params.file);

                fileReader.onload = function (event) {
                    scope.$apply(function () {
                        attributes.$set('src', event.target.result);
                    });
                };


            }
        };
    }]);



/*****************************/

/**
 * @Description:
 * 用于显示过多内容的text,基于bootstrap的popover;
 * content暂时只支持字符串
 * @User:    tony-piao
 * @Version: 0.1.0, 2016-4-20
 */

angular.module("voyageone.angular.directives").directive("popoverText", function () {
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
        link: function (scope, element) {

            var content = scope.content,
                size = scope.size;
                var li = $(element).find("li");

            if (content.length > scope.size)
                li.html(content.substr(0, size) + '...');
            else
                li.html(content).css({cursor: 'default'});
        }
    };
});


/*****************************/

/**
 * @description 价格显示directive
 *              输入一组价格，显示出 min ~ max 价格区间
 * @author Piao
 */
(function () {

    function _priceScale(prices) {
        if (!prices || prices.length === 0) {
            console.warn('directive:price=> 请输入要显示的价格');
            return;
        }

        if (prices.length === 1)
            return prices[0];

        var min = _.min(prices),
            max  = _.max(prices),
            compiled = _.template("<%= min %> ~ <%= max %>");

        if (min === max)
            return min;
        else
            return compiled({min: min, max: max});
    }

    angular.module("voyageone.angular.directives").directive("price", function () {
        return {
            restrict: "E",
            scope: {
                prices: "=prices"
            },
            link: function (scope, element) {
                element.html(_priceScale(scope.prices));
            }
        };
    }).directive("clientMsrpPrice", function ($compile) {
        return {
            restrict: "E",
            scope: {
                data: "=data"
            },
            link: function (scope, element) {

                var skuList = scope.data,
                    final = [],rangArr = [],
                    buttonPopover = angular.element('<button  type="button">');

                buttonPopover.attr('ng-controller','showPopoverCtrl');
                buttonPopover.attr('popover-title','客户建议零售价');
                buttonPopover.attr('popover-placement','left');
                buttonPopover.addClass('btn btn-default btn-xs');

                if (!skuList){
                    console.warn('没有提供sku数据！');
                    return;
                }


                if (skuList instanceof Array) {

                    angular.forEach(skuList, function (element) {

                        var str = element.skuCode + ' : ' + element.clientMsrpPrice,
                            cmcf = element.clientMsrpPriceChgFlg,
                            labelStr = '';

                        if (cmcf && cmcf != 0 && !/^\w{1}0%$/.test(cmcf)) {

                            if (cmcf.indexOf('U') >= 0) {
                                labelStr += '<label class="text-u-red font-bold">&nbsp;(↑' + cmcf.substring(1) + ')</label>';
                            } else {
                                labelStr += '<label class="text-u-green font-bold">&nbsp;(↓' + cmcf.substring(1) + ')</label>';
                            }
                            //记录标识涨幅的label标签
                            rangArr.push(labelStr);
                        }

                        final.push(str + labelStr);

                    });

                } else {
                    console.warn('传入的数据结构应该是数组！');
                }

                buttonPopover.attr('popover-html', 'showInfo(' + JSON.stringify(final) + ')');

                if(rangArr[0])
                    buttonPopover.html(_priceScale(_.pluck(skuList, 'clientMsrpPrice')) + rangArr[0]);
                else
                    buttonPopover.html(_priceScale(_.pluck(skuList, 'clientMsrpPrice')));

                element.html($compile(buttonPopover)(scope.$new()));
            }
        }
    });

})();

/*****************************/

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

/*****************************/

/**
 * @description:
 * 提供"滚动到"和"滚动到顶部"功能
 *
 * @example: <some-element scroll-to="#cssSelector">something</>
 * @example: <some-element scroll-to="#cssSelector, 200">something</>
 * @example: <some-element scroll-to="300, 200">something</>
 * @example: <some-element scroll-to="#cssSelector, 200, -35">something</>
 * @example: <a href="javascript:void(0)" go-top="200">xxx</a>
 * @user:    tony-piao, jonas
 * @version: 0.2.8
 * @since    0.2.0
 */
angular.module("voyageone.angular.directives")
    .directive("scrollTo", function () {
        return {
            restrict: "A",
            scope: false,
            link: function (scope, element, attr) {
                var option = attr.scrollTo;
                if (!option)
                    return;
                option = option.split(',');
                option[1] = parseInt(option[1]) || 200;
                option[2] = parseInt(option[2]) || 0;

                element.on("click", function () {
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
                    $("body").animate({scrollTop: option0 + option[2]}, option[1]);
                    return false;
                });
            }
        };
    })
    .directive("goTop", function () {
        return {
            restrict: "A",
            scope: false,
            link: function (scope, element, attrs) {
                var speed = +attrs.goTop;
                $(element).on("click", function () {
                    $("body").animate({scrollTop: 0}, speed);
                    return false;
                });
            }
        };
    });

/*****************************/

/**
 * @description:
 * 为 jQuery 的插件 stickUp 提供 angular 风格包装
 *
 * @example: <sticky>....</sticky>
 * @user:    jonas
 * @version: 0.2.8
 */
angular.module("voyageone.angular.directives").directive("sticky", function () {
    return {
        restrict: "E",
        scope: false,
        link: function stickyPostLink(scope, element, attr) {
            var $document = $(document);
            var top = parseInt(element.css('top'));
            var topFix = parseInt(attr.topFix) || 0;
            $document.on('scroll', function () {
                var scrollTop = parseInt($document.scrollTop());
                if (scrollTop > top + topFix) {
                    element.css('top', scrollTop - topFix + 'px');
                } else {
                    element.css('top', top + 'px');
                }
            });
        }
    };
});


/*****************************/

/**
 * @Description:
 * 可以在textarea当中使用tab键，
 * @example: <textarea class="form-control no-resize" rows="10" placeholder="请输入导入文字" tab-in-textarea></textarea>
 * @User:    tony-piao
 * @Version: 0.1.0, 2016-5-5
 */
angular.module("voyageone.angular.directives").directive("tabInTextarea", function () {
    return {
        restrict: "A",
        link: function (scope, element) {
            $(element).keydown(function (e) {
                if (e.keyCode === 9) { // tab was pressed
                    // get caret position/selection
                    var start = this.selectionStart;
                    var end = this.selectionEnd;

                    var $this = $(this);
                    var value = $this.val();

                    // set textarea value to: text before caret + tab + text after caret
                    $this.val(value.substring(0, start)
                        + "\t"
                        + value.substring(end));

                    // put caret at right position again (add one for the tab)
                    this.selectionStart = this.selectionEnd = start + 1;

                    // prevent the focus lose
                    e.preventDefault();
                }
            });
        }
    };
});


/*****************************/

/**
 * @Description:
 * 用于分页
 * @User:    Edward
 * @Version: 0.2.0, 2015-12-08
 */
angular.module("voyageone.angular.directives").directive("vpagination", function ($templateCache, $compile, vpagination) {
    var templateKey = "voyageone.angular.directives.pagination.tpl.html";
    var templateKeyNoData = "voyageone.angular.directives.paginationNoData.tpl.html";
    // 有数据分页样式
    if (!$templateCache.get(templateKey)) {
        // 这个 html 是经过压缩的 html , 如果需要修改分页的 html 结构, 请去 vpagination.directive.html 修改, 修改后压缩并粘贴覆盖这里的代码
        $templateCache.put(templateKey, '<div class="row"><div class="col-sm-3"><span translate="TXT_SHOWING_NO"></span><span>&nbsp;</span><input class="text-center" type="text" ng-model="curr.pageNo"><span>&nbsp;</span><span>/</span><span>&nbsp;</span><span ng-bind="totalPages"></span><span>&nbsp;</span><span translate="TXT_PAGE"></span><span>&nbsp;</span><button class="btn btn-xs btn-default" type="button" ng-click="goPage(curr.pageNo)" translate="BTN_GO"></button></div><div class="col-sm-4 text-center"><span translate="TXT_PAGER_SIZE"></span><span>&nbsp;</span><select ng-change="changePerPage(perpages.selectedOption)" ng-options="option for option in perpages.availableOptions" ng-model="perpages.selectedOption"></select><span>&nbsp;</span><span translate="TXT_SHOWING"></span><span>&nbsp;</span><span ng-bind="curr.start"></span><span>&nbsp;</span><span>-</span><span>&nbsp;</span><span ng-bind="curr.end"></span><span>&nbsp;</span><span translate="TXT_OF"></span><span>&nbsp;</span><span ng-bind="totalItems"></span><span>&nbsp;</span><span translate="TXT_ITEMS"></span></div><div class="col-sm-5 text-right"><ul class="pagination"><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(1)" ng-disabled="curr.isFirst">&laquo;</a></li><li ng-class="{disabled: curr.isFirst}"><a href ng-click="goPage(curr.pageNo - 1)" ng-disabled="curr.isFirst">&lsaquo;</a></li><li ng-if="curr.isShowStart"><a href>...</a></li><li ng-repeat="page in curr.pages track by $index" ng-class="{active: isCurr(page)}"><a href ng-click="goPage(page)">{{page}}</a></li><li ng-if="curr.isShowEnd"><a href>...</a></li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(curr.pageNo + 1)" ng-disabled="curr.isLast">&rsaquo;</a></li><li ng-class="{disabled: curr.isLast}"><a href ng-click="goPage(totalPages)" ng-disabled="curr.isLast">&raquo;</a></li></ul></div></div>');
    }
    // 无数据分页样式
    if (!$templateCache.get(templateKeyNoData)) {
        // 这里同上
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
        link: function (scope, element) {
            // 获取用户的config配置
            var userConfigName = scope.$$configNameForA || scope.$$configNameForE;
            var userConfig = scope.$parent.$eval(userConfigName);
            // 将用户配置覆盖到默认配置后，在重新覆盖到用户配置上，用于补全配置属性
            var userWithDefConfig = angular.extend({}, defConfig, userConfig);
            scope.config = angular.extend(userConfig, userWithDefConfig);
            scope.config.setPageIndex = function (pageIndex) {
                if (scope.config.curr == pageIndex) {
                    scope.config.fetch(scope.config.curr, scope.config.size);
                }
                else {
                    scope.goPage(pageIndex);
                }
            };
            var p = new vpagination(scope.config);
            // 监视配置变动
            scope.$parent.$watch(userConfigName, function () {
                refresh();
            }, true);
            /**
             * 跳转到指定页
             * @param num
             */
            scope.goPage = function (num) {
                p.goPage(isNaN(Number(num)) ? 1 : Number(num));
            };
            /**
             * 判断是否是当前页
             * @param num
             * @returns {*|boolean}
             */
            scope.isCurr = function (num) {
                return p.isCurr(num);
            };
            function refresh() {
                // 获取总页数
                scope.totalPages = p.getPageCount();
                // 获取总items数
                scope.totalItems = p.getTotal();
                // 获取当前页的信息
                scope.curr = p.getCurr();
                // 获取每页数量
                scope.config.size = p.getSize();

                scope.perpages = {
                    availableOptions: [10, 20, 50, 100],
                    selectedOption: scope.config.size
                };
                // 根据总数量显示不同的分页样式
                var tempHtml;
                if (p.getTotal() == 0) {
                    tempHtml = $compile($templateCache.get(templateKeyNoData))(scope);
                } else {
                    tempHtml = $compile($templateCache.get(templateKey))(scope);
                }
                element.html(tempHtml);
            }

            scope.changePerPage = function (perpage) {
                scope.config.size = parseInt(perpage);
                //当改变页数时，切换到第一页
                scope.config.curr = 1;
                p.goPage(parseInt(scope.config.curr));
            }
        }
    };
});


/*****************************/

/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 15/12/25
 */
angular.module("voyageone.angular.directives").directive("ngCharMaxlength", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var maxlength = -1;
            attr.$observe("ngCharMaxlength", function (value) {
                var intVal = parseInt(value);
                maxlength = isNaN(intVal) ? -1 : intVal;
                ctrl.$validate();
            });
            ctrl.$validators.maxlength = function (modelValue, viewValue) {
                return maxlength < 0 || ctrl.$isEmpty(viewValue) || getByteLength(viewValue) <= maxlength;
            };
        }
    };
    /**
     * 取得字段的字节长度.
     * @param value
     * @returns {number}
     */
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
}).directive("ngCharMinlength", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var minlength = -1;
            attr.$observe("ngCharMinlength", function (value) {
                var intVal = parseInt(value);
                minlength = isNaN(intVal) ? -1 : intVal;
                ctrl.$validate();
            });
            ctrl.$validators.minlength = function (modelValue, viewValue) {
                return minlength < 0 || ctrl.$isEmpty(viewValue) || getByteLength(viewValue) >= minlength;
            };
        }
    };
    /**
     * 取得字段的字节长度.
     * @param value
     * @returns {number}
     */
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
}).directive("ngMaxvalue", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var maxvalue = -1;
            attr.$observe("ngMaxvalue", function (value) {
                if (/^(\d{4})\/(\d{1,2})\/(\d{1,2})$/.test(value)) maxvalue = new Date(value); else if (/^(\d+)(\.[0-9]{0,2})?$/.test(value)) maxvalue = isNaN(parseFloat(value)) ? -1 : parseFloat(value); else if (/^(\d+)$/.test(value)) maxvalue = isNaN(parseInt(value)) ? -1 : parseInt(value); else maxvalue = -1;
                ctrl.$validate();
            });
            ctrl.$validators.maxvalue = function (modelValue, viewValue) {
                return maxvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue <= maxvalue;
            };
        }
    };
}).directive("ngMinvalue", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var minvalue = -1;
            attr.$observe("ngMinvalue", function (value) {
                if (/^(\d{4})\/(\d{1,2})\/(\d{1,2})$/.test(value)) minvalue = new Date(value); else if (/^(\d+)(\.[0-9]{0,2})?$/.test(value)) minvalue = isNaN(parseFloat(value)) ? -1 : parseFloat(value); else if (/^(\d+)$/.test(value)) minvalue = isNaN(parseInt(value)) ? -1 : parseInt(value); else minvalue = -1;
                ctrl.$validate();
            });
            ctrl.$validators.minvalue = function (modelValue, viewValue) {
                return minvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue >= minvalue;
            };
        }
    };
}).directive("ngMaxinputnum", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var maxvalue = -1;
            attr.$observe("ngMaxinputvalue", function (value) {
                maxvalue = isNaN(parseInt(value)) ? -1 : parseInt(value);
                ctrl.$validate();
            });
            ctrl.$validators.maxinputnum = function (modelValue, viewValue) {
                return maxvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue.length <= maxvalue;
            };
        }
    };
}).directive("ngMininputnum", function () {
    return {
        restrict: "A",
        require: "?ngModel",
        link: function (scope, elm, attr, ctrl) {
            if (!ctrl) return;
            var minvalue = -1;
            attr.$observe("ngMininputnum", function (value) {
                minvalue = isNaN(parseInt(value)) ? -1 : parseInt(value);
                ctrl.$validate();
            });
            ctrl.$validators.mininputnum = function (modelValue, viewValue) {
                return minvalue < 0 || ctrl.$isEmpty(viewValue) || viewValue.length >= minvalue;
            };
        }
    };
});


/*****************************/

/**
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 2.0.0
 */
angular.module("voyageone.angular.factories").factory("$dialogs", function ($uibModal, $filter, $templateCache) {

    var templateName = "voyageone.angular.factories.dialogs.tpl.html";

    var template = '<div class="vo_modal vo-dialogs"><div class="modal-header"><button type="button" class="close" data-dismiss="modal" aria-label="Close" ng-click="close()"><span aria-hidden="true"><i ng-click="close()" class="fa fa-close"></i></span></button><h5 class="modal-title"><i class="fa fa-exclamation-triangle"></i>&nbsp;<span ng-bind-html="title"></span></h5></div><div class="modal-body"><div class="text-left" ng-bind-html="content"></div></div><div class="modal-footer"><button class="btn btn-default btn-sm" ng-if="!isAlert" ng-click="close()" translate="BTN_CANCEL"></button><button class="btn btn-sm {{::isAlert?\'btn-default\':\'btn-vo\'}}" ng-click="ok()" translate="BTN_OK"></button></div></div>';

    $templateCache.put(templateName, template);

    function tran(translationId, values) {
        return $filter("translate")(translationId, values);
    }

    return function (options) {
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
            controller: ["$scope", function (scope) {
                _.extend(scope, options);
            }],
            size: "md"
        });
        options.close = function () {
            modalInstance.dismiss("close");
        };
        options.ok = function () {
            modalInstance.close("");
        };
        return modalInstance;
    };

}).factory("alert", function ($dialogs) {
    return function (content, title) {
        return $dialogs({
            title: title || "TXT_ALERT",
            content: content,
            isAlert: true
        }).result;
    };
}).factory("confirm", function vConfirm($dialogs) {
    return function (content, title) {
        return $dialogs({
            title: title || "TXT_CONFIRM",
            content: content,
            isAlert: false
        }).result;
    };
});


/*****************************/

/**
 * @Date:    2015-11-16 20:51:05
 * @User:    Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.factories").factory("interceptorFactory", function () {
    // 未知的系统错误
    var UNKNOWN_CODE = "5";
    // 和 JAVA 同步,系统通知前端自动跳转的特殊代码
    var CODE_SYS_REDIRECT = "SYS_REDIRECT";
    // 和 JAVA 同步,回话过期的信息
    var MSG_TIMEOUT = "300001";
    var MSG_LOCKED = "A003";
    var MSG_MANYFAILS = "A004";
    var MSG_MISSAUTHENTICATION = "A005";
    var MSG_CHANGEPASS = "A006";
    var MSG_LOGINAGAIN = "A001";
    var CODE_SEL_CHANNEL = "SYS_0";

    /**
     * 对系统自动跳转的响应,执行跳转
     * @param {{code:string,redirectTo:string}} res
     * @returns {boolean}
     */
    function autoRedirect(res) {
        if (res.code != CODE_SYS_REDIRECT && res.code != CODE_SEL_CHANNEL) {
            return false;
        }
        // 如果跳转数据异常,则默认跳转登陆页
        location.href = res.redirectTo || "/login.html";
        return true;
    }

    /**
     * 对会话超时和未登录进行特殊处理
     * @param {{code:string}} res
     * @returns {boolean}
     */
    function sessionTimeout(res) {
        if (res.code != MSG_TIMEOUT) {
            return false;
        }
        // 会话超时,默认跳转到登陆页
        location.href = "/login.html";
        return true;
    }

    /**
     * 对会话超时和未登录进行特殊处理(Admin)
     */
    //function adminSessionTimeout(res) {
    //    if (res.code != MSG_MISSAUTHENTICATION) {
    //        return false;
    //    }
    //    // 会话超时,默认跳转到登陆页
    //    location.href = "/adminLogin.html";
    //    return true;
    //}

    //function adminResetPassword(res) {
    //    if (res.code != MSG_CHANGEPASS) {
    //        return false;
    //    }
    //    // 密码输入错误,默认跳转到重置密码界面
    //    location.href = "/adminResetPass.html";
    //    return true;
    //}

    function adminReLogin(res) {
        if (res.code != MSG_LOGINAGAIN) {
            return false;
        }
        // 密码输入错误,默认跳转到重置密码界面
        location.href = "/";
        return true;
    }

    /**
     * 处理位置的异常
     * @param response
     */
    function unknownException(response) {
        if (response.data.code !== UNKNOWN_CODE) {
            return;
        }
        window.$$lastUnknow = response;
        console.error("Server throw unknown exceptio. Message:", response.data.message);
    }

    return {
        request: function (config) {
            return config;
        },
        /**
         * 响应拦截
         * @param {{config:object, data:*, headers:function, status:number, statusText:string}} res
         * @returns {*}
         */
        response: function (res) {
            var result = res.data;
            // 特殊处理部分内容
           // if (autoRedirect(result) || sessionTimeout(result) || adminSessionTimeout(result) || adminResetPassword(result)||adminReLogin(result)) {
            if (autoRedirect(result) || sessionTimeout(result)||adminReLogin(result)) {
                return res;
            }
            unknownException(res);
            return res;
        },
        requestError: function (config) {
            return config;
        },
        responseError: function (res) {
        }
    };
}).config(function ($httpProvider) {
    $httpProvider.interceptors.push("interceptorFactory");
});


/*****************************/

/**
 * Notify 的可用参数
 * {
 *    // whether to hide the notification on click
 *    clickToHide: true,
 *    // whether to auto-hide the notification
 *    autoHide: true,
 *    // if autoHide, hide after milliseconds
 *    autoHideDelay: 5000,
 *    // show the arrow pointing at the element
 *    arrowShow: true,
 *    // arrow size in pixels
 *    arrowSize: 5,
 *    // position defines the notification position though uses the defaults below
 *    position: '...',
 *    // default positions
 *    elementPosition: 'bottom left',
 *    globalPosition: 'top right',
 *    // default style
 *    style: 'bootstrap',
 *    // default class (string or [string])
 *    className: 'error',
 *    // show animation
 *    showAnimation: 'slideDown',
 *    // show animation duration
 *    showDuration: 400,
 *    // hide animation
 *    hideAnimation: 'slideUp',
 *    // hide animation duration
 *    hideDuration: 200,
 *    // padding between element and notification
 *    gap: 2
 * }
 * @User: Jonas
 * @Date: 2015-3-31 14:39:26
 * @Version: 2.0.0
 */
angular.module("voyageone.angular.factories").factory("notify", function ($filter) {

    var notifyStyle = {
        'noticeTip':{
            html: "<div><span data-notify-text/></div>",
            classes: {
                base: {
                    "min-width":'150px',
                    "background-color": "#ee903d",
                    "padding": "5px",
                    "color": "white",
                    "border":"1px solid #ee903d"
                },
                superBlue: {
                    "color": "white",
                    "background-color": "blue"
                }
            }
        }
    };

    /**
     * @ngdoc function
     * @name voNotify
     * @description
     * 自动关闭的弹出提示框
     */
    function notify(options) {
        if (!options) return;
        if (_.isString(options)) options = {
            message: options
        };
        if (!_.isObject(options)) return;
        var values;
        // 为 translate 的格式化提供支持，检测类型并转换
        if (_.isObject(options.message)) {
            values = options.message.values;
            options.message = options.message.id;
        }
        options.message = $filter("translate")(options.message, values);

        if(options.type === 'noticeTip'){
            $.notify.addStyle('noticeTip', notifyStyle.noticeTip);

            options.style = 'noticeTip';
            _.extend(options,options.opts);

            return $.notify(options.jqObj,options.message, options);
        }

        return $.notify(options.message, options);
    }

    notify.success = function (message) {
        return notify({
            message: message,
            className: "success"
        });
    };
    notify.warning = function (message) {
        return notify({
            message: message,
            className: "warning"
        });
    };
    notify.danger = function (message) {
        return notify({
            message: message,
            className: "danger"
        });
    };

    notify.noticeTip = function(jqObj,message,options){
        return notify({
            jqObj:jqObj,
            type:'noticeTip',
            message: message,
            opts:options
        });
    };

    return notify;
});


/*****************************/

/**
 * @ngdoc
 * @factory
 * @name pppAutoImpl
 * @description
 * 根据定义自动生成方法实现. 注意! 依赖 ui-bootstrap
 */
angular.module("voyageone.angular.factories").factory("pppAutoImpl", function ($q, $modal) {
    return function (declares, viewBaseUrl, jsBaseUrl) {
        if (!declares.$$$ || !declares.$$$.impl) declares.$$$ = {
            impl: declarePopupMethods(declares, viewBaseUrl, jsBaseUrl, "")
        };
        return declares.$$$.impl;
    };
    function declarePopupMethods(declares, viewBaseUrl, jsBaseUrl, popupBaseKey) {
        var impl = {};
        if (popupBaseKey) popupBaseKey += "/";
        _.each(declares, function (declare, parentDir) {
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
            impl[declare.popupKey] = function (context) {
                if (context) options.resolve = {
                    context: function () {
                        return context;
                    }
                };
                var defer = $q.defer();
                require([options.controllerUrl], function () {
                    defer.resolve($modal.open(options).result);
                });
                return defer.promise;
            };
        });
        return impl;
    }

    function getControllerName(key) {
        return key.replace(/\.(\w)/g, function (m, m1) {
                return m1.toUpperCase();
            }).replace(/^(\w)/, function (m, m1) {
                return m1.toLowerCase();
            }) + "PopupController";
    }
});


/*****************************/

/**
 * @Description:
 *
 * @User: linanbin
 * @Version: 2.0.0, 16/1/11
 */
angular.module("voyageone.angular.factories").factory("selectRowsFactory", function () {
    return function (config) {
        var _selectRowsInfo = config ? config : {
            selAllFlag: false,
            currPageRows: [],
            // [{id: "12345"}], [{id: "123456", name: "test1"}]
            selFlag: [],
            // [{"12345": true}, {"12346": false}]
            selList: []
        };
        this.selAllFlag = function (value) {
            return value !== undefined ? _selectRowsInfo.selAllFlag = value : _selectRowsInfo.selAllFlag;
        };
        this.clearCurrPageRows = function () {
            _selectRowsInfo.currPageRows = [];
        };
        this.currPageRows = function (value) {
            return value !== undefined ? _selectRowsInfo.currPageRows.push(value) : _selectRowsInfo.currPageRows;
        };
        this.selFlag = function (value) {
            return value !== undefined ? _selectRowsInfo.selFlag.push(value) : _selectRowsInfo.selFlag;
        };
        this.selList = function (value) {
            return value !== undefined ? _selectRowsInfo.selList.push(value) : _selectRowsInfo.selList;
        };
        this.clearSelectedList = function () {
            _selectRowsInfo.selList = [];
            _selectRowsInfo.selFlag = [];
        };
        this.selectRowsInfo = _selectRowsInfo;
    };
});


/*****************************/

/**
 * @User: Edward
 * @Version: 2.0.0, 2015-12-09
 */
angular.module("voyageone.angular.factories").factory("vpagination", function () {
    /**
     * 创建一个分页服务
     * @param {{ curr: number, size: number, total: number, fetch: function }} config 配置
     */
    return function (config) {
        var _pages, _lastTotal = 0, _showPages = [], defaultPage = config.size;
        /**默认page为20，当改变时触发分页，add by pwj*/
        /**
         * 返回总件数
         * @returns {*}
         */
        this.getTotal = function () {
            return config.total;
        };

        /**
         * 获取每页数量
         * @returns {*}
         */
        this.getSize = function () {
            return config.size;
        };

        /**
         * 返回当前页的开始和结束号
         * @returns {{start: number, end: number}}
         */
        this.getCurr = function () {
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
        // 跳转到指定页
        this.goPage = load;
        // 返回总页数
        this.getPageCount = getPages;
        // 是否是当前页
        this.isCurr = isCurr;

        /**
         * 跳转到指定页
         * @param {number} page 页号
         */
        function load(page) {
            page = page || config.curr;
            if (page < 1 || page > getPages() || isCurr(page)) return;
            config.curr = page;
            //保留上次每页条数
            defaultPage = config.size;
            config.fetch(config.curr, config.size);
        }

        /**
         * 初始化page列表
         * @returns {Array}
         */
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
            // 按照指定数量创建按钮
            for (var i = minPage; i <= maxPage; i++) {
                //scope.pages.push({num: 1, active: "", show: false});
                _showPages.push(i);
            }
            return _showPages;
        }

        /**
         * 获取当前总页数
         * @returns {number}
         */
        function getPages() {
            if (_lastTotal != config.total || config.size !== defaultPage) {
                _pages = parseInt(config.total / config.size) + (config.total % config.size > 0 ? 1 : 0);
                _lastTotal = config.total;
            }
            return _pages;
        }

        /**
         * 返回当前页的起始号
         * @returns {number}
         */
        function getCurrStartItems() {
            return (config.curr - 1) * config.size + 1;
        }

        /**
         * 返回当前页的结束号
         * @returns {number}
         */
        function getCurrEndItems() {
            var currEndItems = config.curr * config.size;
            return currEndItems <= config.total ? currEndItems : config.total;
        }

        /**
         * 是否是最后一页
         * @returns {boolean}
         */
        function isLast() {
            return config.curr == getPages();
        }

        /**
         * 是否是第一页
         * @returns {boolean}
         */
        function isFirst() {
            return config.curr == 1;
        }

        /**
         * 是否是当前页
         * @param page 页码
         * @returns {boolean}
         */
        function isCurr(page) {
            return config.curr == page && config.size === defaultPage;
        }

        function curr() {
            return config.curr;
        }

        /**
         * 是否显示开始...项目
         * @returns {boolean}
         */
        function isShowStart() {
            _showPages = createShowPages();
            return _showPages[0] > 1;
        }

        /**
         * 是否显示结束...项目
         * @returns {boolean}
         */
        function isShowEnd() {
            _showPages = createShowPages();
            return _showPages[_showPages.length - 1] < _pages;
        }
    };
});


/*****************************/

/**
 * @description 格林威治时间转换为当地时区时间
 */
angular.module("voyageone.angular.filter").filter("gmtDate", function ($filter) {

    return function (input, format) {

        var miliTimes;

        if (!input) {
            return '';
        }

        switch (typeof input) {
            case 'string':
                input = new Date(input);
                miliTimes = input.getTime() + new Date().getTimezoneOffset() * 60 * 1000 * (-1);
                break;
            case 'number':
                miliTimes = new Date(input);
                break;
            default:
                console.error("传入了未知类型数据！！！");
        }

        return $filter('date')(new Date(miliTimes), format);

    };

});


/*****************************/

/**
 * Created by sofia on 2016/7/22.
 */

angular.module("voyageone.angular.filter").filter("stringCutter", function() {
    return function (value, wordWise, max, tail) {
        if (!value) return '';

        max = parseInt(max, 10);
        if (!max) return value;
        if (value.length <= max) return value;

        value = value.substr(0, max);
        if (wordWise) {
            var lastSpace = value.lastIndexOf(' ');
            if (lastSpace != -1) {
                value = value.substr(0, lastSpace);
            }
        }
        return value + (tail || ' …');
    };
});

/*****************************/

/**
 * @description
 *
 * 自动创建基于地址定义的数据访问 service.
 * 传入的定义必须是 {object}, 并且至少有 root 属性
 *
 * @User: Jonas
 * @Date: 2015-12-10 19:32:37
 * @Version: 2.0.0
 */

angular.module("voyageone.angular.vresources", []).provider("$vresources", function ($provide) {

    function getActionUrl(root, action) {
        return root + (root.lastIndexOf("/") === root.length - 1 ? "" : "/") + action;
    }

    function actionHashcode(md5, root, actionName, args, cacheWith) {
        var argsJson = angular.toJson(args);
        var otherKeyJson = !cacheWith ? "" : angular.toJson(cacheWith);
        var md5Arg = root + actionName + argsJson + otherKeyJson;
        return md5.createHash(md5Arg);
    }

    /**
     * 闭包声明一个数据访问的 Service
     * @param {string} name Service 的名称
     * @param {object} actions 方法和地址定义
     * @param {object} cacheKey 额外的可用缓存关键字
     */
    function closureDataService(name, actions, cacheKey) {

        var _ServiceClass, root = actions.root;

        if (!actions) {
            return;
        }
        if (typeof actions !== "object") {
            console.log("Failed to new DataResource: [" + actions + "] is not a object");
            return;
        }
        if (!root) {
            console.log("Failed to new DataResource: no root prop" + angular.toJson(actions));
            return;
        }

        _ServiceClass = function (ajax, $sessionStorage, $localStorage, md5, $q) {
            this._a = ajax;
            this._sc = $sessionStorage;
            this._lc = $localStorage;
            this._5 = md5;
            this._q = $q;
            this._c = {};
        };

        _.each(actions, function (option, actionName) {

            var _url, _root, _resolve, _reject, _cacheFlag, _cacheWith;

            if (_.isString(option))
                _url = option;
            else if (_.isObject(option)) {
                _url = option.url;
                _resolve = option.then;
                _root = option.root;
                _cacheFlag = option.cache;
                _cacheWith = option.cacheWith;

                if (!_.isArray(_cacheWith))
                    _cacheWith = null;
                else {
                    var __cacheWith = _cacheWith.map(function (cacheKeyName) {
                        return cacheKey[cacheKeyName];
                    }).filter(function (cacheKeyValue) {
                        return !!cacheKeyValue;
                    });

                    if (__cacheWith.length !== _cacheWith.length)
                        _cacheFlag = 0;
                    else
                        _cacheWith = __cacheWith;
                }
            }

            if (!_url) {
                console.error('URL is undefined', option);
                return;
            }

            if (_root === false)
                _root = "";
            else if (_root === null || _root === undefined || _root === true)
                _root = root;

            if (_.isArray(_resolve)) {
                _reject = _resolve[1];
                _resolve = _resolve[0]
            }

            if (!_.isFunction(_resolve))
                _resolve = function (res) {
                    return res;
                };

            if (!_cacheFlag || _cacheFlag > 3)
                _cacheFlag = 0;

            _url = getActionUrl(_root, _url);

            _ServiceClass.prototype[actionName] = _cacheFlag === 0 ? function (args, option) {
                return this._a.post(_url, args, option).then(_resolve, _reject);
            } : function (args, option) {
                var deferred, result;
                var session = this._sc,
                    local = this._lc,
                    hash = actionHashcode(this._5, root, actionName, args, _cacheWith),
                    promise = this._c[hash];
                if (promise)
                    return promise;
                deferred = this._q.defer();
                promise = deferred.promise;
                this._c[hash] = promise;

                result = _cacheFlag === 2 ? session[hash] : (_cacheFlag === 3 ? local[hash] : null);

                if (result !== null && result !== undefined)
                    deferred.resolve(result);
                else
                    this._a.post(_url, args, option).then(function (res) {
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
                    }, function (res) {
                        result = _reject(res);
                        deferred.reject(result);
                    });

                return promise;
            };
        });

        $provide.service(name, ['ajaxService', '$sessionStorage', '$localStorage', 'md5', '$q', _ServiceClass]);
    }

    this.$get = function () {
        return {
            register: function (name, actions, cacheKey) {
                if (!actions) return;
                if (typeof actions !== "object") return;
                // 如果有 root 这个属性,就创建 service
                if (actions.root) {
                    closureDataService(name, actions, cacheKey);
                    return;
                }
                // 否则继续访问子属性
                for (var childName in actions) {
                    // 额外的检查
                    if (actions.hasOwnProperty(childName)) {
                        this.register(childName, actions[childName], cacheKey);
                    }
                }
            }
        };
    };
});

/*****************************/

/**
 * @Date:    2015-11-16 18:48:29
 * @User:    Jonas
 * @Version: 0.2.1
 */
angular.module("voyageone.angular.services").service("$ajax", $Ajax).service("ajaxService", AjaxService).config(['$httpProvider', function ($httpProvider) {
    $httpProvider.defaults.headers.common = {
        'X-Requested-With': 'XMLHttpRequest'
    };
}]);

function $Ajax($http, $q, blockUI, $timeout) {
    this.$http = $http;
    this.$q = $q;
    this.blockUI = blockUI;
    this.$timeout = $timeout;
}

$Ajax.prototype.post = function (url, data, option) {
    var defer = this.$q.defer(),
        blockUI = this.blockUI,
        $timeout = this.$timeout,
        cancelBlock = null;

    option = option || {
            autoBlock: true,
            blockDelay: 1000
        };

    var autoBlock = option.autoBlock,
        blockDelay = option.blockDelay;

    if (autoBlock) {
        cancelBlock = (function (blockPromise) {
            return function () {
                $timeout.cancel(blockPromise);
                blockUI.stop();
            };
        })($timeout(function () {
            blockUI.start();
        }, blockDelay));
    }

    if (data === undefined) {
        data = {};
    }

    this.$http.post(url, data).then(function (response) {
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
    }, function (response) {
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

AjaxService.prototype.post = function (url, data, option) {
    var defer = this.$q.defer();

    this.$ajax.post(url, data, option).then(function (res) {
        // 成功
        defer.resolve(res);
        return res;
    }, function (_this) {
        // 失败
        return function (res) {
            _this.messageService.show(res);
            defer.reject(res);
            return res;
        };
    }(this));

    return defer.promise;
};



/*****************************/

/**
 * @Date:    2015-11-16 20:30:37
 * @User:    Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.services").service("cookieService", CookieService);

var keys = {
    language: "voyageone.user.language",
    company: "voyageone.user.company",
    channel: "voyageone.user.channel",
    application: "voyageone.user.application"
};

function makeProps(key) {
    return function (val) {
        if (arguments.length === 1) {
            return this.set(key, val);
        } else if (arguments.length > 1) {
            return this.set(key, arguments);
        }
        return this.get(key);
    };
}

/**
 * cookie 模型, 用来包装传入的数据
 * @param key
 * @param value
 */
function Cookie(key, value) {
    this.key = key;
    this.value = value;
}

function CookieService($cookies) {
    this.$cookies = $cookies;
}

CookieService.prototype.get = function (key) {
    var result = this.$cookies.get(key);
    if (result === undefined || result === null)
        return '';

    // 为了兼容老数据
    // 不是以 { 起始的认为不是 json, 直接返回
    if (result.indexOf('{') !== 0)
        return result;

    // 否则转换输出
    var item = JSON.parse(result);
    return item.value;
};

CookieService.prototype.set = function (key, value) {
    // 统一使用 Cookie 类包装后存储
    var item = new Cookie(key, value);
    return this.$cookies.put(key, angular.toJson(item));
};

CookieService.prototype.removeAll = function () {
    this.$cookies.remove(keys.language);
    this.$cookies.remove(keys.company);
    this.$cookies.remove(keys.channel);
    this.$cookies.remove(keys.application);
};

CookieService.prototype.language = makeProps(keys.language);

CookieService.prototype.company = makeProps(keys.company);

CookieService.prototype.channel = makeProps(keys.channel);

CookieService.prototype.application = makeProps(keys.application);


/*****************************/

/**
 * 对后台的信息进行自动处理
 * @Date:    2015-11-19 14:47:23
 * @User:    Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.services").service("messageService", MessageService);

// 同步,DisplayType 枚举
var DISPLAY_TYPES = {
    /**
     * 弹出提示
     */
    ALERT: 1,
    /**
     * 顶端弹出自动关闭
     */
    NOTIFY: 2,
    /**
     * 右下弹出自动关闭
     */
    POP: 3,
    /**
     * 用户自定义处理
     */
    CUSTOM: 4
};

function MessageService(alert, confirm, notify) {
    this.alert = alert;
    this.confirm = confirm;
    this.notify = notify;
}

MessageService.prototype = {
    /**
     * 根据类型自动显示信息
     * @param {{displayType:Number, message:String}} res
     */
    show: function (res) {
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


/*****************************/

/**
 * @Date:    2015-11-19 14:26:43
 * @User:    Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.services").service("permissionService", PermissionService);

function PermissionService($rootScope) {
    this.$rootScope = $rootScope;
    this.permissions = [];
}

PermissionService.prototype = {
    /**
     * set the action permissions.
     * @param permissions
     */
    setPermissions: function (permissions) {
        this.permissions = permissions;
        this.$rootScope.$broadcast("permissionsChanged");
    },
    /**
     * check the permission has been in action permissions.
     * @param permission
     * @returns {boolean|*}
     */
    has: function (permission) {
        return _.contains(this.permissions, permission.trim());
    }
};


/*****************************/

/**
 * @Date:    2015-11-19 14:35:25
 * @User:    Jonas
 * @Version: 0.2.0
 */
angular.module("voyageone.angular.services").service("translateService", TranslateService);

function TranslateService($translate) {
    this.$translate = $translate;
}

TranslateService.prototype = {
    
    languages: {
        en: "en",
        zh: "zh"
    },
    
    /**
     * set the web side language type.
     */
    setLanguage: function (language) {
        if (!_.contains(this.languages, language)) {
            language = this.getBrowserLanguage();
        }
        this.$translate.use(language);
        return language;
    },
    
    /**
     * get the browser language type.
     * @returns {string}
     */
    getBrowserLanguage: function () {
        var currentLang = navigator.language;
        if (!currentLang) currentLang = navigator.browserLanguage;
        return currentLang.substr(0, 2);
    }
};
