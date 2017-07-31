/**
 * @author tony-piao
 * 京东 & 聚美 产品概述（schema）
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/directives/platFormStatus.directive',
    'modules/cms/directives/noticeTip.directive',
    'modules/cms/directives/contextMenu.directive'
], function (cms, carts) {

    cms.directive("masterSchema", function (productDetailService, sizeChartService, $rootScope, systemCategoryService,
                                            imageDirectiveService, alert, notify, confirm, $localStorage) {
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
                    hsCodeOrigin: null,
                    sizeChartList: [],
                    selectSizeChart: null,
                    lockStatus: {},
                    channelId: $localStorage.user.channel,
                    panelShow: true,
                    copyImages: [
                        {
                            value: 'image1',
                            name: '品牌方商品图'
                        }, {
                            value: 'image6',
                            name: 'PC端自拍商品图'
                        }, {
                            value: 'image7',
                            name: 'APP端自拍商品图'
                        }, {
                            value: 'image2',
                            name: '包装图'
                        }, {
                            value: 'image3',
                            name: '角度图'
                        }, {
                            value: 'image4',
                            name: 'PC端自定义图'
                        }, {
                            value: 'image5',
                            name: 'APP端自定义图'
                        }, {
                            value: 'image8',
                            name: '吊牌图'
                        }, {
                            value: 'image9',
                            name: '耐久性标签图'
                        }
                    ]
                };

                scope.selectSizeChartChange = function () {
                    var sizeChartId = scope.vm.productComm.sizeChart;
                    scope.vm.selectSizeChart = _.find(scope.vm.sizeChartList, function (f) {
                        return f.sizeChartId == sizeChartId;
                    })

                };

                scope.masterCategoryMapping = masterCategoryMapping;
                scope.openProImageSetting = openProImageSetting;
                scope.saveProduct = saveProduct;
                scope.pageAnchor = pageAnchor;
                scope.copyCommonProperty = copyCommonProperty;
                scope.goDetail = goDetail;
                scope.simpleImgDown = simpleImgDown;
                scope.removeImg = removeImg;
                scope.sortImg = sortImg;
                scope.lockProduct = lockProduct;
                scope.copyToOtherImg = copyToOtherImg;

                initialize();

                /**
                 * 获取京东页面初始化数据
                 */
                function initialize() {
                    productDetailService.getCommonProductInfo({
                        cartId: "0",
                        prodId: scope.productInfo.productId
                    }).then(function (resp) {
                        scope.vm.mastData = resp.data.mastData;
                        scope.vm.productComm = scope.productInfo.productComm = resp.data.productComm;

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

                        //暂存税号个人
                        scope.vm.hsCodeOrigin = angular.copy(_.find(scope.vm.productComm.schemaFields, function (field) {
                            return field.id === "hsCodePrivate";
                        }));

                        //champion不存在主商品图
                        if (_fields.images1 && _fields.images1[0]) {
                            scope.vm.currentImage = _fields.images1[0].image1
                        }

                        scope.productInfo.feedInfo = scope.vm.mastData.feedInfo;
                        scope.productInfo.productCustomIsDisp = scope.vm.mastData.productCustomIsDisp;

                        scope.vm.lockStatus.onOffSwitch1 = scope.vm.mastData.appSwitch == "1" ? true : false;
                        scope.vm.lockStatus.onOffSwitch2 = scope.vm.mastData.translateStatus == "1" ? true : false;
                        scope.vm.lockStatus.onOffSwitch3 = scope.vm.mastData.lock == "1" ? true : false;


                        /**主商品提示*/
                        if (!scope.vm.mastData.isMain) {
                            // alert("本商品不是平台主商品，如果您需要在天猫或者京东上新，您所修改的信息不会同步到平台上，图片除外。");
                        }
                        var parameterGetProductSizeChartList = {};

                        parameterGetProductSizeChartList.brandName = scope.productInfo.masterField.brand;
                        parameterGetProductSizeChartList.productType = scope.productInfo.masterField.productType;
                        parameterGetProductSizeChartList.sizeType = scope.productInfo.masterField.sizeType;

                        sizeChartService.getProductSizeChartList(parameterGetProductSizeChartList).then(function (res) {
                            scope.vm.sizeChartList = res.data;
                            scope.selectSizeChartChange();
                        });
                    });
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
                            var isOverlap = false;

                            //判断类目是否改变
                            scope.vm.categoryMark = scope.vm.productComm.catPath == context.selected.catPath;

                            scope.vm.productComm.catId = context.selected.catId;
                            scope.vm.productComm.catPath = context.selected.catPath;
                            scope.vm.productComm.catPathEn = context.selected.catPathEn;
                            scope.vm.productComm.catConf = "1";
                            if (context.selected.catPath) {
                                var translateStatus = searchField("商品翻译状态", scope.vm.productComm.schemaFields);
                                var temp = searchField("产品名称中文", scope.vm.productComm.schemaFields);
                                if (temp) {
                                    if (!temp.value || translateStatus.value.value != "1") {
                                        var cat = context.selected.catPath.split(">");
                                        var titleCn = scope.vm.productComm.fields.brand + ' ' + context.selected.sizeTypeCn + ' ' + cat[cat.length - 1];
                                        temp.value = titleCn;
                                    }
                                }
                            }
                            confirm("主类目切换是否自动覆盖产品分类，使用人群，税号个人，税号跨境申报？").then(function () {
                                if (context.selected.productTypeEn) {
                                    var productType = searchField("产品分类", scope.vm.productComm.schemaFields);
                                    if (productType) {
                                        productType.value.value = context.selected.productTypeEn;
                                    }
                                }
                                if (context.selected.productTypeEn) {
                                    var productType = searchField("产品分类中文", scope.vm.productComm.schemaFields);
                                    if (productType) {
                                        productType.value.value = context.selected.productTypeCn;
                                    }
                                }
                                if (context.selected.sizeTypeEn) {
                                    var sizeType = searchField("适用人群", scope.vm.productComm.schemaFields);
                                    if (sizeType) {
                                        if (!sizeType.value.value) {
                                            sizeType.value.value = context.selected.sizeTypeEn;
                                        }
                                    }
                                }
                                if (context.selected.sizeTypeEn) {
                                    var sizeType = searchField("适用人群中文", scope.vm.productComm.schemaFields);
                                    if (sizeType) {
                                        if (!sizeType.value.value) {
                                            sizeType.value.value = context.selected.sizeTypeCn;
                                        }
                                    }
                                }
                                if (context.selected.hscodeName8) {
                                    var sizeType = searchField("税号个人（8位）", scope.vm.productComm.schemaFields);
                                    if (sizeType) {
                                        sizeType.value.value = context.selected.hscodeName8;
                                    }
                                }
                                if (context.selected.hscodeName10) {
                                    var sizeType = searchField("税号跨境申报（10位）", scope.vm.productComm.schemaFields);
                                    if (sizeType) {
                                        sizeType.value.value = context.selected.hscodeName10;
                                    }
                                }
                            })

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

                        initialize();

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
                 * 更新操作
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
                                        masterSaveAction(true);
                                    } else {
                                        hsCode.value.value = _prehsCode;
                                    }
                                });
                            } else {
                                masterSaveAction();
                            }

                        }, function (res) {
                            if (res.displayType != 1)
                                alert("价格计算失败，可能价格公式不合法，请联系IT人员，税号还原为变更前。");
                        });

                    } else {
                        masterSaveAction();
                    }

                }

                /**
                 * 保存前判断是否要设置税号
                 * @param flag
                 */
                function masterSaveAction(flag) {
                    if (!scope.vm.lockStatus.onOffSwitch2 && $localStorage.user.channel == 928) {
                        confirm('是否设置翻译状态为翻译?').then(function () {
                            scope.vm.lockStatus.onOffSwitch2 = true;

                            productDetailService.doTranslateStatus({
                                prodId: scope.productInfo.productId,
                                translateStatus: "1"
                            }).then(function () {
                                //改变翻译状态后要设置保存接口的上行参数
                                //翻译状态是schema中的属性
                                scope.productInfo.translateStatus = 1;

                                scope.vm.productComm.fields.translateStatus = '1';
                                var translateStatus = searchField("商品翻译状态", scope.vm.productComm.schemaFields);
                                if (translateStatus) {
                                    translateStatus.value.value = '1';
                                }
                                callSaveProduct(flag);
                            });

                        }, function () {
                            callSaveProduct(flag);
                        });
                    } else {
                        callSaveProduct(flag);
                    }
                }

                /**
                 * 调用保存接口
                 * @param freshSub boolean 标识是否要刷新平台子页面
                 * */
                function callSaveProduct(freshSub) {

                    return productDetailService.updateCommonProductInfo({
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

                    var imageName = args[args.length - 1];
                    var imageUrl = imageDirectiveService.getImageUrlByName(imageName) + '?wid=2200&hei=2200';
                    window.open(imageUrl);
                }

                //
                // var mConfig = {
                //     bigImageUrl: 'http://image.voyageone.com.cn/is/image/sneakerhead/✓?',
                //     newImageUrl: 'http://image.voyageone.com.cn/is/image/sneakerhead/✓?'
                // };

                function simpleImgDown(imgName, templateFlag, $event) {
                    var jq = angular.element,
                        _aTag;

                    var imageUrl = imageDirectiveService.getImageUrlByName(imgName);

                    if (templateFlag == 0) {
                        imageUrl += '?wid=2200&hei=2200';
                    } else {
                        imageUrl += '?fmt=jpg&scl=1&qlt=100';
                    }
                    _aTag = jq('<a download>').attr({'href': imageUrl});

                    jq('body').append(_aTag);
                    _aTag[0].click();
                    _aTag.remove();

                    $event.stopPropagation();
                }

                function removeImg(imagesType, imageName, $event) {
                    var productComm = scope.vm.productComm,
                        pictures = productComm.fields[imagesType],
                        _rmIndex = 0;

                    if (!imagesType)
                        return;

                    confirm("您确认要删除该图片吗？").then(function () {
                        _.each(pictures, function (ele, index) {
                            if (ele[imagesType.replace('images', 'image')] === imageName) {
                                _rmIndex = index;
                            }
                        });

                        pictures.splice(_rmIndex, 1);

                        productDetailService.restoreImg({
                            prodId: scope.productInfo.productId,
                            imagesType: imagesType,
                            images: pictures
                        }).then(function () {
                            initialize();
                            notify.success("删除成功！");
                        });

                    });

                    $event.stopPropagation();
                }

                function sortImg(imagesType, $event) {
                    var imgSource = scope.vm.productComm.fields[imagesType];

                    if (!imagesType || imgSource.length < 2)
                        return;

                    productDetailService.restoreImg({
                        prodId: scope.productInfo.productId,
                        imagesType: imagesType,
                        images: imgSource
                    }).then(function () {
                        initialize();
                        notify.success("排序成功！");
                    });

                    $event.stopPropagation();
                }

                function copyToOtherImg(image, orgImgName, targetImg) {
                    var imageType = Object.keys(image)[0];

                    confirm("您确认要复制到【" + targetImg.name + "】吗?").then(function () {
                        var schemaData = scope.vm.productComm.schemaFields;

                        var orgModel = searchField(orgImgName, schemaData);
                        var targetModel = searchField(targetImg.name, schemaData);

                        var orgComplexValue = _.find(orgModel.complexValues, function (_element) {
                            var _fieldMap = _element.fieldMap;
                            return _fieldMap[Object.keys(_fieldMap)[0]].value === image[imageType];
                        });

                        var jsonStr = angular.toJson(orgComplexValue).replace(/image\d{1}/g, targetImg.value);

                        var isExit = _.some(targetModel.complexValues, function (item) {
                            var _fieldMap = item.fieldMap;
                            return _fieldMap[Object.keys(_fieldMap)[0]].value === image[imageType];
                        });

                        if (isExit) {
                            alert(targetImg.name + "上已经存在该图片。");
                            return;
                        }

                        targetModel.complexValues.push(angular.fromJson(jsonStr));

                        //调用保存接口
                        callSaveProduct().then(function () {
                            initialize();
                        });

                    });
                }

                /**
                 * 导航栏上的状态锁定操作
                 * @param onOffSwitch：锁定的对象
                 */
                function lockProduct(onOffSwitch) {
                    var _status = scope.vm.lockStatus[onOffSwitch],
                        lock = _status ? "1" : "0";

                    confirm("您确定执行该操作吗？").then(function () {

                        switch (onOffSwitch) {
                            case "onOffSwitch1":
                                productDetailService.doAppSwitch({
                                    prodId: scope.productInfo.productId,
                                    appSwitch: lock
                                }).then(function () {
                                    initialize();
                                    notify.success(_status ? "APP端已启用" : "APP端已关闭");
                                });
                                break;
                            case "onOffSwitch2":
                                productDetailService.doTranslateStatus({
                                    prodId: scope.productInfo.productId,
                                    translateStatus: lock
                                }).then(function () {
                                    notify.success(_status ? "翻译已启用" : "翻译已关闭");
                                    initialize();
                                    scope.productInfo.translateStatus = +lock;
                                    //通知子页面
                                    scope.productInfo.checkFlag = new Date().getTime();
                                });
                                break;
                            case "onOffSwitch3":
                                productDetailService.updateLock({
                                    prodId: scope.productInfo.productId,
                                    lock: lock
                                }).then(function () {
                                    notify.success(_status ? "商品已锁定" : "商品已解除锁定");

                                    initialize();
                                    scope.productInfo.masterLock = lock;
                                    //刷新子页面
                                    scope.productInfo.masterCategory = new Date().getTime();
                                });
                                break;
                        }

                    }, function () {
                        scope.vm.lockStatus[onOffSwitch] = !_status;
                    });
                }


                /**
                 * group info 显示更多图片
                 */
                scope.moreCode = function () {
                    scope.moreCodeFlg = !scope.moreCodeFlg;
                };

                scope.canMoreCode = function () {
                    _mastData = scope.vm.mastData;

                    if (!_mastData || !_mastData.images || _mastData.images.length === 0)
                        return false;

                    return _.some(_mastData.images, function (element) {
                        return element.qty == 0
                            && !element.isMain
                            && element.productCode != scope.productInfo.masterField.code;
                    });
                };

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