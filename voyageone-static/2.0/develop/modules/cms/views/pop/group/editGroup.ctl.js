/**
 * @description group编辑
 *              platFormStatus.directive，
 *              noticeTip.directive'在产品详情页引入过
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
                sellerCats: [],
                productUrl: "",
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

                //不显示SKU
                if (vm.platform && vm.platform.schemaFields && vm.platform.schemaFields.item) {
                    vm.platform.schemaFields.item = _.filter(vm.platform.schemaFields.item, function (element) {
                        return element.name !== 'SKU';
                    });
                }

                vm.publishEnabled = resp.data.channelConfig.publishEnabledChannels.length > 0;

                if (vm.platform) {
                    if (vm.platform.noMain)
                        vm.noMaterMsg = "该商品的没有设置主商品，请先设置主商品：" + vm.platform.mainCode;

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
                self.vm.platform.pBrandId = context.brandId;
                self.vm.platform.pBrandName = context.pBrand;
                if (platform.schemaFields && platform.schemaFields.product)
                    self.initBrand(platform.schemaFields.product, context.brandId);
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
                showArr: ['image1','image4', 'image5']
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
                    && self.vm.mastData
                    && element.productCode != self.vm.mastData.productCode;
            });
        };

        /**
         * group 保存操作
         */
        EditGroupCtl.prototype.saveGroup = function () {
            var self = this, vm = self.vm;

            self.confirm('您是否确认要保存产品Group信息？').then(function(){

                //设置店铺内分类
                vm.platform.sellerCats = vm.sellerCats;

                self.productDetailService.updateGroupPlatform({
                    code: vm.mastData.productCode,
                    platform: vm.platform
                }).then(function (res) {
                    self.$uibModalInstance.close(res);
                });
            });

        };

        return EditGroupCtl;

    })());

});
