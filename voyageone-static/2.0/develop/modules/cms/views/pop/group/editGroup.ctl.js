/**
 * @description group编辑
 *
 * @author Piao
 */
define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, carts) {

    cms.controller('editGroupController', (function () {

        function EditGroupCtl(context, $scope, $uibModalInstance, productDetailService, $translate, notify, confirm, $compile, alert, popups, $fieldEditService, $document, $templateRequest) {
            var self = this;
            self.context = context;
            self.$scope = $scope;
            self.$uibModalInstance = $uibModalInstance;
            self.productDetailService = productDetailService;
            self.$translate = $translate;
            self.notify = notify;
            self.confirm = confirm;
            self.$compile = $compile;
            self.alert = alert;
            self.popups = popups;
            self.$fieldEditService = $fieldEditService;
            self.$document = $document;
            self.$templateRequest = $templateRequest;
            self.vm = {
                productDetails: null,
                productCode: "",
                mastData: null,
                platform: null,
                status: "Pending",
                skuTemp: {},
                resultFlag: 0,
                sellerCats: [],
                productUrl: "",
                preStatus: null,
                noMaterMsg: null
            };
            self.panelShow = true;
        }

        EditGroupCtl.prototype.init = function () {
            this.getPlatformData();
        };

        /**
         * 构造平台数据
         */
        EditGroupCtl.prototype.getPlatformData = function () {

            var self = this,
                vm = self.vm;

            self.productDetailService.getProductPlatform({
                cartId: self.context.cartId,
                prodId: self.context.mainProdId
            }).then(function (resp) {
                vm.mastData = resp.data.mastData;
                vm.platform = resp.data.platform;
                vm.publishEnabled = resp.data.channelConfig.publishEnabledChannels.length > 0;

                if (vm.platform) {
                    if (vm.platform.noMain)
                        vm.noMaterMsg = "该商品的没有设置主商品，请先设置主商品：" + vm.platform.mainCode;

                    vm.status = vm.platform.status == null ? vm.status : vm.platform.status;
                    vm.platform.pStatus = vm.platform.pStatus == null ? "" : vm.platform.pStatus;
                    vm.sellerCats = vm.platform.sellerCats == null ? [] : vm.platform.sellerCats;
                }

                if (vm.platform.schemaFields && vm.platform.schemaFields.product)
                    self.initBrand(vm.platform.schemaFields.product, vm.platform.pBrandId);

                self.autoSyncPriceMsrp = resp.data.autoSyncPriceMsrp;
                self.autoSyncPriceSale = resp.data.autoSyncPriceSale;

                /**生成共通部分，商品状态*/
                self.productDetailService.createPstatus(angular.element("#group-platform-status"),
                    self.$scope.$new(),
                    self.vm.platform
                );
            });

            vm.productUrl = carts.valueOf(Number(self.context.cartId)).pUrl;

        };


        /**
         @description 类目popup
         * @param productInfo
         */
        EditGroupCtl.prototype.categoryMapping = function () {
            var self = this,
                productDetailService = self.productDetailService;

            productDetailService.getPlatformCategories({cartId: self.context.cartId})
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
                            cartId: self.context.cartId,
                            prodId: self.context.mainProdId,
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
         */
        EditGroupCtl.prototype.openSellerCat = function () {
            var self = this, selectedIds = {};

            self.vm.sellerCats.forEach(function (element) {
                selectedIds[element.cId] = true;
            });

            self.popups.openAddChannelCategoryEdit([{
                code: self.vm.mastData.productCode,
                sellerCats: self.vm.sellerCats,
                cartId: self.context.cartId,
                selectedIds: selectedIds,
                plateSchema: true
            }]).then(function (context) {
                /**清空原来店铺类分类*/
                self.vm.sellerCats = [];
                self.vm.sellerCats = context.sellerCats;
            });
        };

        /**
         *  商品品牌选择
         */
        EditGroupCtl.prototype.choseBrand = function () {

            var self = this,
                platform = self.vm.platform;

            self.popups.openPlatformMappingSetting({
                cartId: self.context.cartId,
                cartName: carts.valueOf(Number(self.context.cartId)).desc,
                masterName: self.context.masterField.brand,
                pBrandId: platform.pBrandId ? platform.pBrandId : null
            }).then(function (context) {
                self.vm.platform.pBrandName = context.pBrand;
                if (platform.schemaFields && platform.schemaFields.product)
                    self.initBrand(platform.schemaFields.product, context.brandId);
            });

        };

        /**
         * @description 更新操作
         * @param mark:记录是否为ready状态,temporary:暂存
         */
        EditGroupCtl.prototype.saveProduct = function (mark) {
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
         * @description 部分属性上新
         */
        EditGroupCtl.prototype.loadAttribute = function () {
            var self = this;

            self.popups.openLoadAttribute({
                attribute: ['description', 'title', 'item_images', 'seller_cids', 'sell_points', 'wireless_desc']
            }).then(function (res) {
                self.approveAttr = null;
                self.approveAttr = res;

                self.saveProduct();
            });

        };

        /**
         * @description 保存前判断数据的有效性
         * @param mark 标识字段
         */
        EditGroupCtl.prototype.saveValid = function (mark) {
            var self = this, masterBrand;

            if (mark === "ready" || self.vm.status === "Ready" || self.vm.status === "Approved") {
                if (!self.validSchema()) {
                    self.alert("请输入必填属性，或者输入的属性格式不正确");
                    return false;
                }

                if (!self.vm.platform.pBrandName) {
                    masterBrand = self.$scope.productInfo.masterField.brand;
                    self.vm.status = self.vm.preStatus;
                    self.alert("该商品的品牌【" + masterBrand + "】没有与平台品牌建立关联，点击左侧的【品牌】按钮，或者在【店铺管理=>平台品牌设置页面】进行设置");
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

        EditGroupCtl.prototype.saveProductAction = function (mark) {
            var self = this,
                popups = self.popups,
                productDetailService = self.productDetailService;

            self.vm.preStatus = angular.copy(self.vm.status);

            //有效性判断
            if (mark !== "temporary" && !self.saveValid(mark))
                return;

            //判断页面头部状态
            if (mark !== "temporary")
                self.vm.status = productDetailService.bulbAdjust(self.vm.status, self.vm.checkFlag);

            /**构造调用接口上行参数*/
            productDetailService.platformUpEntity({cartId: self.$scope.cartInfo.value, mark: mark}, self.vm);

            if (mark === "temporary") {
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

                        productDetailService.checkCategory({
                            cartId: self.$scope.vm.platform.cartId,
                            pCatPath: self.$scope.vm.platform.pCatPath
                        }).then(function (resp) {
                            if (resp.data === false) {
                                confirm("当前类目没有申请 是否还需要保存？如果选择[确定]，那么状态会返回[待编辑]。请联系IT人员处理平台类目").then(function () {
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
                });
            } else {
                return self.callSave();
            }
        };

        /**调用服务器接口*/
        EditGroupCtl.prototype.callSave = function (mark) {
            var self = this,
                productDetailService = self.productDetailService,
                $translate = self.$translate,
                updateInfo = {
                    prodId: self.$scope.productInfo.productId,
                    platform: self.vm.platform,
                    type: mark
                };

            if (self.approveAttr)
                _.extend(updateInfo, {
                    platformWorkloadAttributes: self.approveAttr
                });

            /**判断价格*/
            productDetailService.updateProductPlatformChk(updateInfo).then(function (resp) {
                self.vm.platform.modified = resp.data.modified;
                if (mark !== 'intel')
                    self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));

                /**生成共通部分，商品状态*/
                self.productDetailService.createPstatus(angular.element("#group-platform-status"),
                    self.$scope.$new(),
                    self.vm.platform
                );

            }, function (resp) {
                if (resp.code !== "4000091" && resp.code !== "4000092" && resp.code !== "4000094") {
                    self.vm.status = self.vm.preStatus;
                    return;
                }

                self.confirm(resp.message + "是否强制保存").then(function () {
                    productDetailService.updateProductPlatform(updateInfo).then(function (resp) {
                        self.vm.platform.modified = resp.data.modified;
                        self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));

                        /**生成共通部分，商品状态*/
                        self.productDetailService.createPstatus(angular.element("#group-platform-status"),
                            self.$scope.$new(),
                            self.vm.platform
                        );
                    });
                }, function () {
                    if (mark !== 'temporary')
                        self.vm.status = self.vm.preStatus;
                });
            });

        };


        /**当shema的品牌为空时，设置平台共通的品牌*/
        EditGroupCtl.prototype.initBrand = function (product, brandId) {

            var brandField;

            if (!product)
                return;

            brandField = this.productDetailService.searchField("品牌", product);

            if (!brandField)
                return;

            if (!brandField.value.value)
                brandField.value.value = brandId;
        };

        /**
         * @description 判断Ready和Approved的button激活状态
         */
        EditGroupCtl.prototype.btnDisabled = function () {
            return _.every(this.vm.checkFlag, function (ele) {
                return ele == true ? 1 : 0;
            });
        };

        /**
         * 操作区域图片上传按钮
         */
        EditGroupCtl.prototype.popUploadImg = function () {
            var self = this,
                popup = self.popups;

            self.vm.platform['images1'] = self.context.masterField['images1'];

            popup.openUploadImages({
                cartId: self.context.cartId,
                productId: self.context.mainProdId,
                platform: self.vm.platform,
                showArr: ['image1', 'image6', 'image7', 'image2', 'image3', 'image4', 'image5']
            }).then(function (platform) {
                self.vm.platform = platform;

                //暂存
                self.saveProduct('temporary');
            });
        };


        /**
         * group info 显示更多图片
         */
        EditGroupCtl.prototype.moreCode = function () {
            var self = this;

            self.moreCodeFlg = !self.moreCodeFlg;
        };

        EditGroupCtl.prototype.canMoreCode = function () {
            var self = this;

            if (!self.vm.mastData || !self.vm.mastData.images || self.vm.mastData.images.length == 0)
                return false;

            return _.some(self.vm.mastData.images, function (element) {
                return element.qty == 0
                    && !element.isMain
                    && self.$scope.productInfo
                    && element.productCode != self.$scope.productInfo.masterField.code;
            });
        };

        /**
         * group 保存操作
         */
        EditGroupCtl.prototype.saveGroup = function(){

        };

        return EditGroupCtl;

    })());

});
