/**
 * @description 天猫详情页
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

    function SpLgController($scope, productDetailService, $translate, notify, confirm, $compile, alert, popups, $document, $templateRequest) {
        var self = this;
        self.$scope = $scope;
        self.productDetailService = productDetailService;
        self.$translate = $translate;
        self.notify = notify;
        self.confirm = confirm;
        self.$compile = $compile;
        self.alert = alert;
        self.popups = popups;
        self.$document = $document;
        self.$templateRequest = $templateRequest;
        self.vm = {
            productDetails: null,
            productCode: "",
            mastData: null,
            platform: null,
            status: "Pending",
            skuTemp: {},
            checkFlag: { attribute: 0 },
            resultFlag: 0,
            productUrl: "",
            preStatus: null,
            noMaterMsg: null
        };
        self.panelShow = true;
    }

    SpLgController.prototype.init = function (element) {
        this.element = element;
        this.getPlatformData();
    };

    /**
     * 构造平台数据
     */
    SpLgController.prototype.getPlatformData = function () {

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
                if (vm.platform.noMain)
                    vm.noMaterMsg = "该商品的没有设置主商品，请先设置主商品：" + vm.platform.mainCode;

                vm.status = vm.platform.status == null ? vm.status : vm.platform.status;
                vm.platform.pStatus = vm.platform.pStatus == null ? "" : vm.platform.pStatus;
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
            self.autoSyncPriceSale = resp.data.autoSyncPriceSale;

            /**生成共通部分，商品状态*/
            self.productDetailService.createPstatus(self.element.find("#platform-status"),
                self.$scope.$new(),
                self.vm.platform
            );
        });

        vm.productUrl = carts.valueOf(+$scope.cartInfo.value).pUrl;

    };

    /**
     * @description 更新操作
     * @param mark:记录是否为ready状态,temporary:暂存
     */
    SpLgController.prototype.saveProduct = function (mark) {
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
    SpLgController.prototype.saveValid = function (mark) {
        var self = this, masterBrand;

        if (mark == "ready" || self.vm.status == "Ready" || self.vm.status == "Approved") {
            if (!self.validSchema()) {
                self.alert("请输入必填属性，或者输入的属性格式不正确");
                return false;
            }

            if (!self.checkSkuSale()) {
                self.vm.status = self.vm.preStatus;
                self.alert("请至少选择一个sku进行发布");
                return false;
            }
        }

        return true;
    };

    SpLgController.prototype.saveProductAction = function (mark) {
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
            self.callSave();
        }
    };

    /**调用服务器接口*/
    SpLgController.prototype.callSave = function (mark) {
        var self = this,
            productDetailService = self.productDetailService,
            $translate = self.$translate,
            updateInfo = {
                prodId: self.$scope.productInfo.productId,
                platform: self.vm.platform,
                type: mark
            };

        /**判断价格*/
        productDetailService.updateProductPlatformChk(updateInfo).then(function (resp) {
            self.vm.platform.modified = resp.data.modified;
            self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));

            /**生成共通部分，商品状态*/
            self.productDetailService.createPstatus(self.element.find("#platform-status"),
                self.$scope.$new(),
                self.vm.platform
            );
        }, function (resp) {
            if (resp.code != "4000091" && resp.code != "4000092") {
                self.vm.status = self.vm.preStatus;
                return;
            }

            self.confirm(resp.message + "是否强制保存").then(function () {
                productDetailService.updateProductPlatform(updateInfo).then(function (resp) {
                    self.vm.platform.modified = resp.data.modified;
                    self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));

                    /**生成共通部分，商品状态*/
                    self.productDetailService.createPstatus(self.element.find("#platform-status"),
                        self.$scope.$new(),
                        self.vm.platform
                    );
                });
            }, function () {
                if (mark != 'temporary')
                    self.vm.status = self.vm.preStatus;
            });
        });

    };

    /**
     * 判断是否一个都没选 true：有打钩    false：没有选择
     */
    SpLgController.prototype.checkSkuSale = function () {
        return this.vm.platform.skus.some(function (element) {
            return element.isSale === true;
        });
    };

    /**
     * 如果autoSyncPriceMsrp='2',Approved或刷新价格时做相应check
     * @returns {boolean}
     */
    SpLgController.prototype.checkPriceMsrp = function () {
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


    SpLgController.prototype.validSchema = function () {
        return this.vm.platform == null || this.schemaForm.$valid && this.skuForm.$valid;
    };

    /**
     * 全选操作
     */
    SpLgController.prototype.selectAll = function () {
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
    SpLgController.prototype.pageAnchor = function (area, speed) {
        var offsetTop = 0, element = this.element;

        if (area != 'master') {
            offsetTop = element.find("#" + area).offset().top;
        }

        $("body").animate({scrollTop: offsetTop - 100}, speed);
    };

    /**
     * 判断是否全部选中
     */
    SpLgController.prototype.allSkuSale = function () {
        var self = this;

        if (!self.vm.platform || !self.vm.platform.skus)
            return false;

        return self.vm.platform.skus.every(function (element) {
            return element.isSale === true;
        });
    };

    /**错误聚焦*/
    SpLgController.prototype.focusError = function () {
        var self = this, firstError,
            element = self.element;

        if (!self.validSchema()) {
            firstError = element.find("schema .ng-invalid:first");
            firstError.focus();
            firstError.addClass("focus-error");
        }
    };

    /**
     * 保存按钮提示功能
     */
    SpLgController.prototype.showExt = function () {
        var self = this,
            body = self.$document[0].body,
            $compile = self.$compile,
            modal, modalChildScope,
            $templateRequest = self.$templateRequest;

        $templateRequest('/modules/cms/views/product/subpage/approved-example.tpl.html').then(function (html) {
            modal = $(html);
            modalChildScope = self.$scope.$new();

            modal.appendTo(body);
            $compile(modal)(modalChildScope);
        });
    };

    /**
     * @description 判断Ready和Approved的button激活状态
     */
    SpLgController.prototype.btnDisabled = function () {
        return _.every(this.vm.checkFlag, function (ele) {
            return ele == true ? 1 : 0;
        });
    };

    /**
     * 锁平台
     */
    SpLgController.prototype.platFormLock = function () {
        var self = this, notify = self.notify,
            lock = angular.copy(self.vm.platform.lock);

        self.productDetailService.lockPlatForm({
            cartId: self.$scope.cartInfo.value,
            prodId: self.$scope.productInfo.productId,
            lock: Number(lock)
        }).then(function (res) {
            notify.success(res);
        }, function (res) {
            if (!res)
                self.vm.platform.lock = lock === '1' ? '0' : '1';
        });

    };

    cms.directive('lgSubPage', function () {
        return {
            restrict: 'E',
            controller: ['$scope', 'productDetailService', '$translate', 'notify', 'confirm', '$compile', 'alert', 'popups', '$document', '$templateRequest', SpLgController],
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/product/subpage/lg.sub-page.tpl.html',
            link: function ($scope, element) {
                $scope.ctrl.init(element);
            }
        }
    })

});
