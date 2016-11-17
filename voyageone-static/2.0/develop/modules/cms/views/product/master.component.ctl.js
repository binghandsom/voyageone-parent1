/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/directives/platFormStatus.directive'
], function (cms, carts) {
    cms.directive("masterSchema", function (productDetailService,sizeChartService, $rootScope, systemCategoryService, alert, notify, confirm) {
        return {
            restrict: "E",
            templateUrl: "views/product/master.component.tpl.html",
            /**独立的scope对象*/
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            link: function (scope, element) {
                scope.vm = {
                    mastData: null,
                    productComm: null,
                    categoryMark: null,
                    tempImage: {
                        "images1": [],
                        "images2": [],
                        "images3": [],
                        "images4": [],
                        "images5": [],
                        "images6": [],
                        "images7": [],
                        "images8": [],
                        "images9": []
                    },
                    hsCodeOrigin: null,
                    sizeChartList:[],
                    selectSizeChart:null
                };

                initialize();
                scope.masterCategoryMapping = masterCategoryMapping;
                scope.openProImageSetting = openProImageSetting;
                scope.saveProduct = saveProduct;
                scope.pageAnchor = pageAnchor;
                scope.copyCommonProperty = copyCommonProperty;
                scope.goDetail = goDetail;
                
                /**
                 * 获取京东页面初始化数据
                 */
                function initialize() {
                    productDetailService.getCommonProductInfo({
                        cartId: "0",
                        prodId: scope.productInfo.productId
                    }).then(function (resp) {
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.productComm = resp.data.productComm;

                        var _fields = scope.vm.productComm.fields;

                        scope.productInfo.masterField = _fields;

                        /**通知子页面税号状态和翻译状态*/
                        scope.productInfo.checkFlag = new Date().getTime();
                        scope.productInfo.translateStatus = _fields.translateStatus == null ? 0 : +_fields.translateStatus;
                        scope.productInfo.hsCodeStatus = _fields.hsCodeStatus == null ? 0 : +_fields.hsCodeStatus;


                        /**图片显示*/
                        if ($rootScope.imageUrl == undefined) {
                            $rootScope.imageUrl = '';
                        }

                        scope.vm.currentImage = $rootScope.imageUrl.replace('%s', _fields.images1[0].image1);

                        scope.productInfo.feedInfo = scope.vm.mastData.feedInfo;
                        scope.productInfo.lockStatus = scope.vm.mastData.lock == "1" ? true : false;

                        //暂存税号个人
                        scope.vm.hsCodeOrigin = angular.copy(_.find(scope.vm.productComm.schemaFields, function (field) {
                            return field.id === "hsCodePrivate";
                        }));

                        /**主商品提示*/
                        if (!scope.vm.mastData.isMain) {
                            alert("本商品不是平台主商品，如果您需要在天猫或者京东上新，您所修改的信息不会同步到平台上，图片除外。");
                        }
                        var parameterGetProductSizeChartList={};

                        parameterGetProductSizeChartList.brandName=scope.productInfo.masterField.brand;
                        parameterGetProductSizeChartList.productType=scope.productInfo.masterField.productType;
                        parameterGetProductSizeChartList.sizeType=scope.productInfo.masterField.sizeType;

                        console.log(parameterGetProductSizeChartList);

                        sizeChartService.getProductSizeChartList(parameterGetProductSizeChartList).then(function (res) {
                            scope.vm.sizeChartList=res.data;
                        });
                    });
                }
                scope.sizeChartOnchange=function (selectSizeChart) {
                    console.log(scope.vm.selectSizeChart);
                }
                /**
                 @description 类目popup
                 * @param productInfo
                 * @param popupNewCategory popup实例
                 */
                function masterCategoryMapping(popupNewCategory) {
                    systemCategoryService.getNewsCategoryList().then(function (res) {
                        if (!res.data || !res.data.length) {
                            notify.danger("数据还未准备完毕");
                            return;
                        }

                        popupNewCategory({
                            categories: res.data,
                            from: scope.vm.productComm == null ? "" : scope.vm.productComm.catPath
                        }).then(function (context) {
                            //判断类目是否改变
                            scope.vm.categoryMark = scope.vm.productComm.catPath == context.selected.catPath;

                            scope.vm.productComm.catId = context.selected.catId;
                            scope.vm.productComm.catPath = context.selected.catPath;

                        });
                    });
                }

                /**
                 * 添加图片
                 */
                function openProImageSetting(imageType, openImageSetting) {
                    openImageSetting({
                        productId: scope.productInfo.productId,
                        imageType: imageType
                    }).then(function (context) {

                        if (context == null)
                            return;

                        if (context.length == 0)
                            return;

                        scope.vm.productComm.modified = context[context.length - 1].modified;

                        var imgType = null;
                        angular.forEach(context, function (item) {
                            imgType = item.imageType;
                            scope.vm.tempImage[item.imageType].push($rootScope.imageUrl.replace('%s', item.imageName));
                        });

                        _.map(scope.vm.productComm.schemaFields, function (item) {
                            if (item.id == imgType) {
                                item.complexValues.splice(0, item.complexValues.length);
                                angular.forEach(context[context.length - 1].imageSchema[0].complexValues, function (image) {
                                    item.complexValues.push(image);
                                });

                            }
                        });

                    });
                }

                /**
                 * 复制主数据filed
                 * */
                function copyCommonProperty() {

                    confirm("您确定要复制主商品数据吗？").then(function () {
                        productDetailService.copyCommonProperty({prodId: scope.productInfo.productId}).then(function (res) {
                            scope.vm.productComm = res.data.platform;
                        });
                    });
                }

                /**
                 * 更新操作    prodId:'5924',hsCode:'08010000,吊坠,个'
                 */
                function saveProduct(openHsCodeChange) {
                    if (!validSchema()) {
                        alert("保存失败，请查看产品的属性是否填写正确！");
                        focusError();
                        return;
                    }

                    //税号判断
                    var hsCode = _.find(scope.vm.productComm.schemaFields, function (field) {
                        return field.id === "hsCodePrivate";
                    });

                    var _orgHsCode = scope.vm.hsCodeOrigin.value.value;
                    var _hscOde = hsCode.value.value;

                    if (!angular.equals(_hscOde.split(",")[0], _orgHsCode.split(",")[0])) {

                        var _prehsCode = angular.copy(_orgHsCode),
                            results = [];

                        productDetailService.hsCodeChg({
                            prodId: scope.productInfo.productId,
                            hsCode: _hscOde
                        }).then(function (res) {
                            _.each(res.data, function (element, key) {
                                var _hsObject = {cartId: key, cartInfo: carts.valueOf(+key)};
                                _.each(element, function (element, key) {
                                    _.extend(_hsObject, {skuCode: key, prideOld: element[0], priceNew: element[1]});
                                });
                                results.push(_hsObject);
                            });

                            //判断税号价格是否改变
                            var isHsChange = _.every(results, function (element) {
                                return element.prideOld == element.priceNew;
                            });

                            if (!isHsChange) {
                                openHsCodeChange({
                                    prodId: scope.productInfo.productId,
                                    hsCodeOld: _prehsCode,
                                    hsCodeNew: _hscOde,
                                    results: results
                                }).then(function (context) {
                                    if (context === 'confirm') {
                                        callSaveProduct(true);
                                    } else {
                                        hsCode.value.value = _prehsCode;
                                    }
                                });
                            } else {
                                callSaveProduct();
                            }

                        }, function (res) {
                            if (res.displayType != 1)
                                alert("价格计算失败，可能价格公式不合法，请联系IT人员，税号还原为变更前。");
                        });

                    } else {
                        callSaveProduct();
                    }

                }

                /**
                 * 调用保存接口
                 * @param freshSub boolean 标识是否要刷新平台子页面
                 * */
                function callSaveProduct(freshSub) {

                    productDetailService.updateCommonProductInfo({
                        prodId: scope.productInfo.productId,
                        productComm: scope.vm.productComm
                    }).then(function (resp) {
                        scope.vm.productComm.modified = resp.data.modified;
                        scope.productInfo.translateStatus = resp.data.translateStatus == null ? 0 : +resp.data.translateStatus;
                        scope.productInfo.hsCodeStatus = resp.data.hsCodeStatus == null ? 0 : +resp.data.hsCodeStatus;

                        //通知子页面
                        scope.productInfo.checkFlag = new Date().getTime();
                        if (!scope.vm.categoryMark)
                            scope.productInfo.masterCategory = new Date().getTime();

                        if (freshSub)
                            scope.productInfo.masterCategory = new Date().getTime();

                        notify.success("更 新 成 功 ");
                    }, function () {
                        alert("更新失败", "错误提示");
                    });

                }

                function validSchema() {
                    return scope.vm.productComm == null || scope.vm.productComm.schemaFields == null ? false : scope.schemaForm.$valid;
                }

                /**
                 * 右侧导航栏
                 * @param area 区域
                 * @param speed 导航速度 ms为单位
                 */
                function pageAnchor(area, speed) {
                    var offsetTop = 0;
                    if (area != 'master')
                        offsetTop = element.find("#" + area).offset().top;
                    $("body").animate({scrollTop: offsetTop - 100}, speed);
                }

                function focusError() {
                    var firstError = element.find("schema .ng-invalid:first");
                    firstError.focus();
                    firstError.addClass("focus-error");
                }

                function goDetail() {
                    var vm = scope.vm,
                        args = vm.currentImage.split("/");

                    if (args.length == 0)
                        return;

                    window.open("http://image.sneakerhead.com/is/image/sneakerhead/✓?wid=2200&hei=2200".replace("✓", args[args.length - 1]));

                }

                /**全schema中通过name递归查找field*/
                function searchField(fieldName, schema) {

                    var result = null;

                    _.find(schema, function (field) {

                        if (field.name === fieldName) {
                            result = field;
                            return true;
                        }

                        if (field.fields && field.fields.length) {
                            result = searchField(fieldName, field.fields);
                            if (result)
                                return true;
                        }

                        return false;
                    });

                    return result;
                }

            }
        };
    });
});