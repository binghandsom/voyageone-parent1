/**
 * @description 官网同构详情页
 * @author piao
 */
define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/directives/platFormStatus.directive',
    'modules/cms/directives/noticeTip.directive',
    './approved-example.ctl'
], function (cms, carts) {

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

    function SpJdController($scope, productDetailService, $translate, notify, confirm, $compile, alert, popups, $fieldEditService, $document, $templateRequest) {
        this.$scope = $scope;
        this.productDetailService = productDetailService;
        this.$translate = $translate;
        this.notify = notify;
        this.confirm = confirm;
        this.$compile = $compile;
        this.alert = alert;
        this.popups = popups;
        this.$fieldEditService = $fieldEditService;
        this.$document = $document;
        this.$templateRequest = $templateRequest;
        this.vm = {
            productDetails: null,
            productCode: "",
            mastData: null,
            platform: null,
            status: "Pending",
            skuTemp: {},
            checkFlag: {tax: 0},
            resultFlag: 0,
            sellerCats: [],
            productUrl: "",
            preStatus: null,
            noMaterMsg: null
        }
    }

    SpJdController.prototype.init = function (element) {
        var self = this,
            check = self.vm.checkFlag,
            $scope = self.$scope;

        self.element = element;

        //监控税号和翻译状态
        var checkFlag = $scope.$watch("productInfo.checkFlag", function () {
            check.tax = $scope.productInfo.hsCodeStatus;
        });

        //监控主类目
        var masterCategory = $scope.$watch("productInfo.masterCategory", function () {
            self.getPlatformData();
        });
    };

    /**
     * 构造平台数据
     */
    SpJdController.prototype.getPlatformData = function () {

        var self = this,
            $scope = self.$scope,
            vm = self.vm;

        self.productDetailService.getProductPlatform({
            cartId: $scope.cartInfo.value,
            prodId: $scope.productInfo.productId
        }).then(function (resp) {
            vm.mastData = resp.data.mastData;
            vm.platform = resp.data.platform;
            vm.publishEnabled = resp.data.channelConfig.publishEnabledChannels.length > 0;

            if (vm.platform) {
                if(vm.platform.noMain)
                    vm.noMaterMsg = "该商品的没有设置主商品，请先设置主商品：" + vm.platform.mainCode;

                vm.status = vm.platform.status == null ? vm.status : vm.platform.status;
                vm.platform.pStatus = vm.platform.pStatus == null ? "WaitingPublish" : vm.platform.pStatus;
                vm.sellerCats = vm.platform.sellerCats == null ? [] : vm.platform.sellerCats;
            }

            _.each(vm.mastData.skus, function (mSku) {
                vm.skuTemp[mSku.skuCode] = mSku;
            });

            if ($scope.productInfo.skuBlock) {
                setTimeout(function () {
                    self.pageAnchor('sku', 0);
                }, 1500)
            }

            self.autoSyncPriceMsrp = resp.data.autoSyncPriceMsrp;

        });

        vm.productUrl = carts.valueOf(+$scope.cartInfo.value).pUrl;

    };


    /**
     @description 类目popup
     * @param productInfo
     * @param popupNewCategory popup实例
     */
    SpJdController.prototype.categoryMapping = function () {
        var self = this, $scope = self.$scope,
            productDetailService = self.productDetailService;

        if (self.vm.status == 'Approved') {
            self.alert("商品可能已经上线，请先进行该平台的【全Group下线】操作。");
            return;
        }

        productDetailService.getPlatformCategories({cartId: $scope.cartInfo.value})
            .then(function (res) {
                if (!res.data || !res.data.length) {
                    self.notify.danger("数据还未准备完毕");
                    return;
                }

                self.popups.popupNewCategory({
                    from: self.vm.platform == null ? "" : self.vm.platform.pCatPath,
                    categories: res.data,
                    divType: ">",
                    plateSchema: true
                }).then(function (context) {

                    if (self.vm.platform != null) {
                        if (context.selected.catPath == self.vm.platform.pCatPath)
                            return;
                    }

                    productDetailService.changePlatformCategory({
                        cartId: $scope.cartInfo.value,
                        prodId: $scope.productInfo.productId,
                        catId: context.selected.catId,
                        catPath: context.selected.catPath
                    }).then(function (resp) {
                        self.vm.platform = resp.data.platform;
                        self.vm.platform.pCatPath = context.selected.catPath;
                        self.vm.platform.pCatId = context.selected.catId;
                    });
                });

            })
    };

    /**
     * @description 店铺内分类popup
     * @param openAddChannelCategoryEdit
     */
    SpJdController.prototype.openSellerCat = function () {
        var self = this, selectedIds = {};

        self.vm.sellerCats.forEach(function (element) {
            selectedIds[element.cId] = true;
        });

        self.popups.openAddChannelCategoryEdit([{
            code: self.vm.mastData.productCode,
            sellerCats: self.vm.sellerCats,
            cartId: self.$scope.cartInfo.value,
            selectedIds: selectedIds,
            plateSchema: true
        }]).then(function (context) {
            self.vm.sellerCats = [];
            self.vm.sellerCats = context.sellerCats;
        });
    };

    /**商品智能上新*/
    SpJdController.prototype.publishProduct = function () {
        var self = this,
            $fieldEditService = self.$fieldEditService;

        self.vm.preStatus = angular.copy(self.vm.status);

        self.callSave('intel').then(function (res) {

            if (res) {
                $fieldEditService.intelligentPublish({
                    cartId: self.vm.platform.cartId,
                    productIds: [self.vm.mastData.productCode],
                    isSelectAll: 0
                }).then(function () {
                    self.isPublishSucceed = true;
                    self.alert('已完成商品的智能上新！');
                });
            }

        });

    };

    /**
     * @description 更新操作
     * @param mark:记录是否为ready状态,temporary:暂存
     */
    SpJdController.prototype.saveProduct = function (mark) {
        var self = this;

        if (!self.checkPriceMsrp()) {
            self.confirm("建议售价不能低于指导价和最终售价，是否强制保存？").then(function () {
                self.saveProductAction(mark);
            });
        } else {
            self.saveProductAction(mark);
        }
    };

    /**
     * @description 保存前判断数据的有效性
     * @param mark 标识字段
     */
    SpJdController.prototype.saveValid = function (mark) {
        var self = this;

        if (mark == "ready" || self.vm.status == "Ready" || self.vm.status == "Approved") {

            if (!self.checkSkuSale()) {
                self.vm.status = self.vm.preStatus;
                self.alert("请至少选择一个sku进行发布");
                return false;
            }
        }

        return true;
    };

    SpJdController.prototype.saveProductAction = function (mark) {
        var self = this,
            popups = self.popups,
            productDetailService = self.productDetailService;

        self.vm.preStatus = angular.copy(self.vm.status);

        //有效性判断
        if (!self.saveValid(mark))
            return;

        //判断页面头部状态
        if (mark != "temporary")
            self.vm.status = productDetailService.bulbAdjust(self.vm.status, self.vm.checkFlag);

        /**构造调用接口上行参数*/
        productDetailService.platformUpEntity({cartId: self.$scope.cartInfo.value, mark: mark}, self.vm);

        if (mark == "temporary") {
            self.vm.status = self.vm.platform.status;
            self.callSave("temporary");
            return;
        }

        if (self.vm.status == "Approved") {

            popups.openApproveConfirm(self.vm.platform.skus).then(function (context) {
                if (context) {
                    _.map(self.vm.platform.skus, function (element) {
                        element.confPriceRetail = element.priceRetail;
                    });
                    productDetailService.priceConfirm({
                        productCode: self.$scope.productInfo.masterField.code,
                        platform: self.vm.platform
                    });
                    self.callSave();
                } else {
                    self.callSave();
                }
            });
        } else {
            return self.callSave();
        }


    };

    /**调用服务器接口*/
    SpJdController.prototype.callSave = function (mark) {
        var self = this, notify = self.notify,
            confirm = self.confirm,
            productDetailService = self.productDetailService,
            $translate = self.$translate;

        /**判断价格*/
        return productDetailService.updateProductPlatformChk({
            prodId: self.$scope.productInfo.productId,
            platform: self.vm.platform,
            isUpdate: mark !== 'intel' ? true : false
        }).then(function (resp) {
            self.vm.platform.modified = resp.data.modified;
            if (mark !== 'intel')
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));

            return true;

        }, function (resp) {
            if (resp.code != "4000091" && resp.code != "4000092") {
                self.vm.status = self.vm.preStatus;
                return false;
            }

            return confirm(resp.message + ",是否强制保存").then(function () {
                return productDetailService.updateProductPlatform({
                    prodId: self.$scope.productInfo.productId,
                    platform: self.vm.platform
                }).then(function (resp) {
                    self.vm.platform.modified = resp.data.modified;
                    self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    return true;
                }, function () {
                    return false;
                });
            }, function () {
                if (mark != 'temporary')
                    self.vm.status = selfvm.preStatus;
                return false;
            });
        });

    };

    /**
     *  商品品牌选择
     */
    SpJdController.prototype.choseBrand = function () {

        var self = this, $scope = self.$scope,
            platform = self.vm.platform;

        self.popups.openPlatformMappingSetting({
            cartId: $scope.cartInfo.value,
            cartName: $scope.cartInfo.name,
            masterName: $scope.productInfo.masterField.brand,
            pBrandId: platform.pBrandId ? platform.pBrandId : null
        }).then(function (context) {
            self.vm.platform.pBrandName = context.pBrand;
        });

    };

    /**sku价格刷新*/
    SpJdController.prototype.refreshPrice = function () {
        var self = this;
        if (!self.checkPriceMsrp()) {
            confirm("建议售价不能低于指导价和最终售价，是否强制保存？").then(function () {
                self.updateSkuPrice()
            });
        } else {
            self.updateSkuPrice();
        }
    };

    /**
     * 判断是否一个都没选 true：有打钩    false：没有选择
     */
    SpJdController.prototype.checkSkuSale = function () {
        return this.vm.platform.skus.some(function (element) {
            return element.isSale === true;
        });
    };

    /**
     * 如果autoSyncPriceMsrp='2',Approved或刷新价格时做相应check
     * @returns {boolean}
     */
    SpJdController.prototype.checkPriceMsrp = function () {
        var self = this, priceMsrpCheckObj,
            priceMsrpCheck = true;

        if (self.autoSyncPriceMsrp == "2") {
            priceMsrpCheckObj = _.find(self.vm.platform.skus, function (sku) {
                return (sku.priceMsrp < sku.priceSale) || (sku.priceMsrp < sku.priceRetail);
            });
            priceMsrpCheck = typeof priceMsrpCheckObj == "undefined";
        }

        return priceMsrpCheck;
    };

    /**
     * 刷新价格实际操作
     */
    SpJdController.prototype.updateSkuPrice = function () {
        var self = this, $scope = self.$scope;

        self.confirm("您是否确认要刷新sku价格").then(function () {
            self.productDetailService.updateSkuPrice({
                cartId: $scope.cartInfo.value,
                prodId: $scope.productInfo.productId,
                platform: $scope.vm.platform
            }).then(function () {
                self.alert("TXT_MSG_UPDATE_SUCCESS");
            }, function (res) {
                self.alert(res.message);
            });
        });
    };

    SpJdController.prototype.validSchema = function () {
        return this.vm.platform == null || this.vm.platform.schemaFields == null ? false : this.schemaForm.$valid && this.skuForm.$valid;
    };

    /**
     * 全选操作
     */
    SpJdController.prototype.selectAll = function () {
        var self = this;
        self.vm.platform.skus.forEach(function (element) {
            element.isSale = self.vm.skuFlag;
        });
    };

    /**
     * 右侧导航栏
     * @param area div的index
     * @param speed 导航速度 ms为单位
     */
    SpJdController.prototype.pageAnchor = function (area, speed) {
        var offsetTop = 0, element = this.element;

        if (area != 'master') {
            offsetTop = element.find("#" + area).offset().top;
        }

        $("body").animate({scrollTop: offsetTop - 100}, speed);
    };

    /**
     * 判断是否全部选中
     */
    SpJdController.prototype.allSkuSale = function () {
        var self = this;

        if (!self.vm.platform || !self.vm.platform.skus)
            return false;

        return self.vm.platform.skus.every(function (element) {
            return element.isSale === true;
        });
    };

    /**错误聚焦*/
    SpJdController.prototype.focusError = function () {
        var self = this, firstError,
            element = self.element;

        if (!self.validSchema()) {
            firstError = element.find("schema .ng-invalid:first");
            firstError.focus();
            firstError.addClass("focus-error");
        }
    };

    SpJdController.prototype.openOffLinePop = function (type) {
        var self = this, vm = self.vm;

        if (vm.mastData == null)
            return;

        if (vm.status != 'Approved') {
            self.alert("商品未完成平台上新，无法操作平台下线。");
            return;
        }

        if (vm.mastData.isMain && type != 'group') {
            self.alert("当前商品为主商品，无法单品下线。如果想下线整个商品，请点击【全group下线】按钮");
            return;
        }

        self.popups.openProductOffLine({
            cartId: self.$scope.cartInfo.value,
            productCode: vm.mastData.productCode,
            type: type
        }).then(function () {
            self.getPlatformData();
        });
    };

    SpJdController.prototype.openSwitchMainPop = function () {
        var self = this;

        self.popups.openSwitchMain({
            cartId: self.$scope.cartInfo.value,
            productCode: self.vm.mastData.productCode
        }).then(function () {
            self.getPlatformData();
            self.vm.noMaterMsg = null;
        });
    };

    SpJdController.prototype.copyMainProduct = function () {
        var self = this, $scope = self.$scope,
            productDetailService = self.productDetailService,
            template = _.template("您确定要复制Master数据到<%=cartName%>吗？");

        self.confirm(template({cartName: $scope.cartInfo.name})).then(function () {
            productDetailService.copyProperty({
                prodId: $scope.productInfo.productId,
                cartId: +$scope.cartInfo.value
            }).then(function (res) {
                self.vm.platform = res.data.platform;
            });
        });
    };

    SpJdController.prototype.moveToGroup = function () {

        var self = this, $scope = self.$scope,
            $translate = self.$translate,
            productDetailService = self.productDetailService,
            template = $translate.instant('TXT_CONFIRM_MOVE_SKU', {'cartName': $scope.cartInfo.name});

        window.sessionStorage.setItem('moveCodeInfo', JSON.stringify({
            cartId: $scope.cartInfo.value,
            cartName: $scope.cartInfo.name,
            prodId: $scope.productInfo.productId
        }));

        self.confirm(template).then(function () {
            var newTab = window.open('about:blank');

            productDetailService.moveCodeInitCheck({
                cartId: $scope.cartInfo.value,
                cartName: $scope.cartInfo.name,
                prodId: $scope.productInfo.productId
            }).then(function () {
                newTab.location.href = "#/product/code_move";
            }, function () {
                newTab.close();
            });
        });
    };

    SpJdController.prototype.showExt = function () {
        var self = this, modal, modalChildScope,
            body = self.$document[0].body,
            $compile = self.$compile,
            $templateRequest = self.$templateRequest;

        $templateRequest('/modules/cms/views/product/subpage/approved-example.tpl.html').then(function (html) {
            modal = $(html);
            modalChildScope = self.$scope.$new();

            modal.appendTo(body);
            $compile(modal)(modalChildScope);
        });
    };

    cms.directive('gtSubPage', function () {
        return {
            restrict: 'E',
            controller: ['$scope', 'productDetailService', '$translate', 'notify', 'confirm', '$compile', 'alert', 'popups', '$fieldEditService', '$document', '$templateRequest', SpJdController],
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/product/subpage/gt.sub-page.tpl.html',
            link: function ($scope, element) {
                $scope.ctrl.init(element);
            }
        }
    })

});
