define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/directives/platFormStatus.directive',
    'modules/cms/directives/noticeTip.directive'
], function (cms, carts) {

    var cntConfig = {
        checkArr: ['translate', 'tax', 'category', 'attribute']
    };

    function SpJdController($scope, productDetailService, $translate, notify, confirm, $q, $compile, alert, popups, $fieldEditService) {
        this.$scope = $scope;
        this.productDetailService = productDetailService;
        this.$translate = $translate;
        this.notify = notify;
        this.confirm = confirm;
        this.$q = $q;
        this.$compile = $compile;
        this.alert = alert;
        this.popups = popups;
        this.$fieldEditService = $fieldEditService;
        this.vm = {
            productDetails: null,
            productCode: "",
            mastData: null,
            platform: null,
            status: "Pending",
            skuTemp: {},
            checkFlag: {translate: 0, tax: 0, category: 0, attribute: 0},
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
            check.translate = $scope.productInfo.translateStatus;
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
                vm.status = vm.platform.status == null ? vm.status : vm.platform.status;
                vm.checkFlag.category = vm.platform.pCatPath == null ? 0 : 1;
                vm.platform.pStatus = vm.platform.pStatus == null ? "WaitingPublish" : vm.platform.pStatus;
                vm.sellerCats = vm.platform.sellerCats == null ? [] : vm.platform.sellerCats;
                vm.platform.pStatus = vm.platform.pPublishMessage != null && vm.platform.pPublishMessage != "" ? "Failed" : vm.platform.pStatus;
            }

            _.each(vm.mastData.skus, function (mSku) {
                vm.skuTemp[mSku.skuCode] = mSku;
            });

            if (vm.platform.schemaFields && vm.platform.schemaFields.product)
                self.initBrand(vm.platform.schemaFields.product, vm.platform.pBrandId);

            if ($scope.productInfo.skuBlock) {
                setTimeout(function () {
                    self.pageAnchor('sku', 0);
                }, 1500)
            }

            self.autoSyncPriceMsrp = resp.data.autoSyncPriceMsrp;

        }, function (resp) {
            vm.noMaterMsg = resp.message.indexOf("Server Exception") >= 0 ? null : resp.message;
        });

        vm.productUrl = carts.valueOf(+$scope.cartInfo.value).pUrl;

    };


    /**
     @description 类目popup
     * @param productInfo
     * @param popupNewCategory popup实例
     */
    SpJdController.prototype.jdCategoryMapping = function () {
        var self = this,
            productDetailService = self.productDetailService,
            $scope = self.$scope,
            popups = self.popups;

        if (self.vm.status == 'Approved') {
            self.alert("商品可能已经上线，请先进行该平台的【全Group下线】操作。");
            return;
        }

        productDetailService.getPlatformCategories({cartId: $scope.cartInfo.value})
            .then(function (res) {
                return self.$q(function (resolve, reject) {
                    if (!res.data || !res.data.length) {
                        self.notify.danger("数据还未准备完毕");
                        reject("数据还未准备完毕");
                    } else {
                        resolve(popups.popupNewCategory({
                            from: $scope.vm.platform == null ? "" : $scope.vm.platform.pCatPath,
                            categories: res.data,
                            divType: ">",
                            plateSchema: true
                        }));
                    }
                });
            }).then(function (context) {
            if ($scope.vm.platform != null) {
                if (context.selected.catPath == $scope.vm.platform.pCatPath)
                    return;
                if (($scope.cartInfo.value == 23 || $scope.cartInfo.value == 20) && $scope.vm.status == 'Approved' && $scope.vm.platform.pCatPath) {
                    var c1 = $scope.vm.platform.pCatPath.split(">"),
                        c2 = context.selected.catPath.split(">");
                    if (c1[0] != c2[0]) {
                        self.alert("商品可能已经上线，如果要修改类目一级类目必须与修改前的一样。");
                        return;
                    }
                }
            }

            productDetailService.changePlatformCategory({
                cartId: $scope.cartInfo.value,
                prodId: $scope.productInfo.productId,
                catId: context.selected.catId,
                catPath: context.selected.catPath
            }).then(function (resp) {
                $scope.vm.platform = resp.data.platform;
                $scope.vm.platform.pCatPath = context.selected.catPath;
                $scope.vm.platform.pCatId = context.selected.catId;
                $scope.vm.checkFlag.category = 1;
                $scope.vm.platform.pStatus == 'WaitingPublish';
                $scope.vm.status = "Pending";

            });
        });
    };

    /**
     * @description 店铺内分类popup
     * @param openAddChannelCategoryEdit
     */
    SpJdController.prototype.openSellerCat = function () {
        var self = this,
            popups = self.popups,
            selectedIds = {};

        self.vm.sellerCats.forEach(function (element) {
            selectedIds[element.cId] = true;
        });

        popups.openAddChannelCategoryEdit([{
            "code": self.vm.mastData.productCode,
            "sellerCats": self.vm.sellerCats,
            "cartId": self.$scope.cartInfo.value,
            "selectedIds": selectedIds,
            plateSchema: true
        }]).then(function (context) {
            /**清空原来店铺类分类*/
            self.vm.sellerCats = [];
            self.vm.sellerCats = context.sellerCats;
        });
    };

    /**商品智能上新*/
    SpJdController.prototype.publishProduct = function () {
        var self = this,
            $fieldEditService = self.$fieldEditService;
        //记录原先状态
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

    SpJdController.prototype.saveProductAction = function (mark) {
        var self = this,
            popups = self.popups,
            confirm = self.confirm,
            alert = self.alert,
            productDetailService = self.productDetailService,
            statusFlag;

        if (mark == "ready") {
            if (!self.validSchema()) {
                self.alert("请输入必填属性，或者输入的属性格式不正确");
                return;
            }
        }

        statusFlag = _.filter(self.vm.checkFlag, function (value, key) {
            return cntConfig.checkArr.indexOf(key) >= 0;
        }).every(function (value) {
            return value == true ? 1 : 0;
        });

        self.vm.preStatus = angular.copy(self.vm.status);

        switch (self.vm.status) {
            case "Pending":
                self.vm.status = statusFlag ? "Ready" : self.vm.status;
                break;
            case "Ready":
                self.vm.status = "Approved";
                break;
        }

        if (self.vm.status == "Ready" && self.vm.platform.pBrandName == null && mark != "temporary") {
            var masterBrand = self.$scope.productInfo.masterField.brand;
            self.vm.status = self.vm.preStatus;
            self.alert("该商品的品牌【" + masterBrand + "】没有与平台品牌建立关联，点击左侧的【品牌】按钮，或者在【店铺管理=>平台品牌设置页面】进行设置");
            return;
        }

        if ((self.vm.status == "Ready" || self.vm.status == "Approved") && !self.checkSkuSale() && mark != "temporary") {
            self.vm.status = self.vm.preStatus;
            self.alert("请至少选择一个sku进行发布");
            return;
        }

        /**构造调用接口上行参数*/
        if (self.vm.checkFlag.attribute == 1)
            self.vm.platform.pAttributeStatus = "1";
        else
            self.vm.platform.pAttributeStatus = "0";

        self.vm.platform.status = mark == "temporary" ? "Pending" : self.vm.status;
        self.vm.platform.sellerCats = self.vm.sellerCats;
        self.vm.platform.cartId = +self.$scope.cartInfo.value;

        _.map(self.vm.platform.skus, function (item) {
            item.property = item.property == null ? "OTHER" : item.property;
        });

        if (mark == "temporary") {
            //暂存状态都为 Pending
            self.vm.status = "Pending";
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

                    if (self.vm.platform.cartId != 27) {
                        productDetailService.checkCategory({
                            cartId: self.vm.platform.cartId,
                            pCatPath: self.vm.platform.pCatPath
                        }).then(function (resp) {
                            if (resp.data === false) {
                                self.confirm("当前类目没有申请 是否还需要保存？如果选择[确定]，那么状态会返回[待编辑]。请联系IT人员处理平台类目").then(function () {
                                    self.vm.platform.status = self.vm.status = "Pending";
                                    self.callSave();
                                });
                            } else {
                                self.callSave();
                            }
                        });
                    } else {
                        self.callSave();
                    }
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
        var self = this,
            notify = self.notify,
            confirm = self.confirm,
            productDetailService = self.productDetailService,
            $translate = self.$translate;

        /**判断价格*/
        return self.productDetailService.updateProductPlatformChk({
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

        var self = this,
            $scope = self.$scope,
            platform = self.vm.platform;

        openPlatformMappingSetting({
            cartId: $scope.cartInfo.value,
            cartName: $scope.cartInfo.name,
            masterName: $scope.productInfo.masterField.brand,
            pBrandId: platform.pBrandId ? platform.pBrandId : null
        }).then(function (context) {
            self.vm.platform.pBrandName = context.pBrand;
            if (platform.schemaFields && platform.schemaFields.product)
                self.initBrand(platform.schemaFields.product, context.brandId);
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
     * 重置天猫产品id
     * @returns {*}
     */
    SpJdController.prototype.doResetTmProduct = function () {
        var self = this,
            $scope = self.$scope;

        return self.productDetailService.resetTmProduct({
            cartId: $scope.cartInfo.value,
            productCode: $scope.productInfo.masterField.code
        }).then(function () {
            self.alert("处理完成， 请重新approve一下商品");
        });
    };

    /**
     * 如果autoSyncPriceMsrp='2',Approved或刷新价格时做相应check
     * @returns {boolean}
     */
    SpJdController.prototype.checkPriceMsrp = function () {
        var self = this,
            priceMsrpCheckObj,
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
        var self = this,
            $scope = self.$scope;

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
        var offsetTop = 0,
            element = this.element;

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

        if (!self.vm.platform)
            return false;

        if (!self.vm.platform.skus)
            return false;

        return self.vm.platform.skus.every(function (element) {
            return element.isSale === true;
        });
    };

    /**错误聚焦*/
    SpJdController.prototype.focusError = function () {
        var self = this,
            element = self.element,
            firstError;

        if (!self.validSchema()) {
            firstError = element.find("schema .ng-invalid:first");
            firstError.focus();
            firstError.addClass("focus-error");
        }
    };

    /**当shema的品牌为空时，设置平台共通的品牌*/
    SpJdController.prototype.initBrand = function (product, brandId) {

        var self = this,
            brandField;

        if (!product)
            return;

        if (self.$scope.cartInfo.value != 23)
            return;

        brandField = self.searchField("品牌", product);

        if (!brandField)
            return;

        if (!brandField.value.value)
            brandField.value.value = brandId;
    };

    SpJdController.prototype.searchField = function (fieldName, schema) {

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
    };

    cms.directive('jdSubPage', function () {
        return {
            restrict: 'E',
            controller: ['$scope', 'productDetailService', '$translate', 'notify', 'confirm', '$q', '$compile', 'alert', 'popups', '$fieldEditService', SpJdController],
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/product/jd.sub-page.tpl.html',
            link: function ($scope, element) {
                $scope.ctrl.init(element);
            }
        }
    })

});
