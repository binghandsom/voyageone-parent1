/**
 * @description 京东详情页
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
        var self = this;
        self.$scope = $scope;
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
        self.flag = false;
        self.vm = {
            productDetails: null,
            productCode: "",
            mastData: null,
            platform: null,
            status: "Pending",
            skuTemp: {},
            checkFlag: self.$scope.cartInfo.value == 24 ? {translate: 0, category: 0, attribute: 0} : {
                    translate: 0,
                    tax: 0,
                    category: 0,
                    attribute: 0
                },
            resultFlag: 0,
            sellerCats: [],
            productUrl: "",
            preStatus: null,
            noMaterMsg: null,
            sxImageTemplates:[]
        };
        self.tempImageTemplate = "请输入...";
        self.panelShow = true;
    }
    /*xu*/
    SpJdController.prototype.change = function(){
        var self = this;
        if(self.tempImageTemplate != "" && self.tempImageTemplate != "请输入..."){
            self.vm.platform.fields.mainImageTemplate = self.tempImageTemplate;
        }
        if (self.tempImageTemplate == "") {
            self.flag = true;
        } else {
            self.flag = false;
        }
    };
    SpJdController.prototype.change1 = function(){
        var self = this;
        if(self.tempImageTemplate != "" && self.tempImageTemplate != "请输入..."){
            self.vm.platform.fields.mainImageTemplate = self.tempImageTemplate;
        }
    };

    SpJdController.prototype.init = function (element) {
        var self = this,
            check = self.vm.checkFlag,
            $scope = self.$scope;

        self.element = element;

        //监控税号和翻译状态和锁定状态
        var checkFlag = $scope.$watch("productInfo.checkFlag", function () {
            check.translate = $scope.productInfo.translateStatus;
            if ($scope.cartInfo.value != 24)
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
            vm.sxImageTemplates = resp.data.sxImageTemplates;
            vm.publishEnabled = resp.data.channelConfig.publishEnabledChannels.length > 0;

            if (vm.platform) {
                if (vm.platform.noMain)
                    vm.noMaterMsg = "该商品的没有设置主商品，请先设置主商品：" + vm.platform.mainCode;

                vm.status = vm.platform.status == null ? vm.status : vm.platform.status;
                vm.checkFlag.category = vm.platform.pCatPath == null ? 0 : 1;
                vm.platform.pStatus = vm.platform.pStatus == null ? "" : vm.platform.pStatus;
                vm.sellerCats = vm.platform.sellerCats == null ? [] : vm.platform.sellerCats;
            }

            vm.tempSkuNo = _.find(vm.platform.skus, function (mSku) {
                return mSku.jdSkuId != "" && mSku.isSale
            }).jdSkuId;

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
    SpJdController.prototype.categoryMapping = function () {
        var self = this,
            productDetailService = self.productDetailService,
            $scope = self.$scope;

        if (self.vm.platform.pNumIId) {
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
                        self.vm.checkFlag.category = 1;

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
            /**清空原来店铺类分类*/
            self.vm.sellerCats = [];
            self.vm.sellerCats = context.sellerCats;
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
     * @description 部分属性上新
     */
    SpJdController.prototype.loadAttribute = function(){
        var self = this;

        self.popups.openLoadAttribute({
            attribute: ['description', 'title', 'seller_cids']
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
    SpJdController.prototype.saveValid = function (mark) {
        var self = this, masterBrand;

        if (mark == "ready" || self.vm.status == "Ready" || self.vm.status == "Approved") {
            if (!self.validSchema()) {
                self.alert("请输入必填属性，或者输入的属性格式不正确");
                return false;
            }

            if (self.vm.platform.pBrandName == null) {
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

    SpJdController.prototype.saveProductAction = function (mark) {
        var self = this,
            popups = self.popups,
            productDetailService = self.productDetailService;

        self.vm.preStatus = angular.copy(self.vm.status);

        //判断页面头部4个状态
        if (mark != "temporary")
            self.vm.status = productDetailService.bulbAdjust(self.vm.status, self.vm.checkFlag);

        //有效性判断
        if (mark != "temporary" && !self.saveValid(mark))
            return;

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
    SpJdController.prototype.callSave = function (mark) {
        var self = this,
            productDetailService = self.productDetailService,
            $translate = self.$translate,
            updateInfo = {
                prodId: self.$scope.productInfo.productId,
                platform: self.vm.platform,
                type: mark
            };

        if(self.approveAttr)
            _.extend(updateInfo,{
                platformWorkloadAttributes:self.approveAttr
            });

        productDetailService.updateProductPlatformChk(updateInfo).then(function (resp) {
            self.vm.platform.modified = resp.data.modified;
            self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));

            /**生成共通部分，商品状态*/
            self.productDetailService.createPstatus(self.element.find("#platform-status"),
                self.$scope.$new(),
                self.vm.platform
            );
        }, function (resp) {
            if (resp.code != "4000091" && resp.code != "4000092" && resp.code != "4000094") {
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

    /**商品智能上新*/
    SpJdController.prototype.publishProduct = function () {
        var self = this,
            platform = self.vm.platform,
            cartId = self.vm.platform.cartId;

        // liking店不需要检测平台类目是否填写完毕
        if ([28, 29].indexOf(cartId) < 0 && self.vm.checkFlag.category == 0) {
            self.alert('请检查类目是否设置完毕！');
            return;
        }

        self.confirm('您是否确定要智能上新').then(function () {

            self.vm.preStatus = angular.copy(self.vm.status);
            //设置智能上新状态,如果pStatus已经存在则保留原来状态
            platform.pStatus = platform.pStatus == "" ? "WaitingPublish" : platform.pStatus;
            platform.status = self.vm.status = 'Approved';

            self.callSave('intel');
        });

    };

    /**
     *  商品品牌选择
     */
    SpJdController.prototype.choseBrand = function () {

        var self = this,
            $scope = self.$scope,
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
            $scope = self.$scope,
            upEntity = {
                cartId: $scope.cartInfo.value,
                prodId: $scope.productInfo.productId,
                platform: self.vm.platform
            };

        self.confirm("您是否确认要刷新sku价格").then(function () {
            self.productDetailService.updateSkuPrice(_.extend(upEntity, {priceCheck: true})).then(function () {
                self.notify.success("TXT_MSG_UPDATE_SUCCESS");
            }, function (res) {
                if (res.code != "4000094")
                    return;

                self.confirm(res.message + "是否强制保存").then(function () {
                    self.productDetailService.updateSkuPrice(_.extend(upEntity, {priceCheck: false})).then(function () {
                        self.notify.success("TXT_MSG_UPDATE_SUCCESS");
                    });

                });
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
        var self = this,
            $translate = self.$translate,
            vm = self.vm;

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
            self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            self.getPlatformData();
        });
    };

    SpJdController.prototype.openSwitchMainPop = function () {
        var self = this,
            $translate = self.$translate;

        self.popups.openSwitchMain({
            cartId: self.$scope.cartInfo.value,
            productCode: self.vm.mastData.productCode
        }).then(function () {
            self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
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
                self.vm.sellerCats = self.vm.platform.sellerCats == null ? [] : self.vm.platform.sellerCats;
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

    /**
     * @description 判断Ready和Approved的button激活状态
     */
    SpJdController.prototype.btnDisabled = function () {
        return _.every(this.vm.checkFlag, function (ele) {
            return ele == true ? 1 : 0;
        });
    };

    /**
     * 产品详情上下架
     */
    SpJdController.prototype.upperAndLowerFrame = function (mark) {
        var self = this,
            $translate = self.$translate,
            msg = mark === 'ToOnSale' ? '上架' : '下架';

        self.confirm('您是否执行' + msg + '操作？').then(function () {
            self.productDetailService.upperLowerFrame({
                cartId: self.$scope.cartInfo.value,
                productCode: self.vm.mastData.productCode,
                pStatus: mark
            }).then(function () {
                self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                self.getPlatformData();
            });
        });
    };
    /**
     * 操作区域图片上传按钮
     */
    SpJdController.prototype.popUploadImg = function () {
        var self = this,
            popup = self.popups;

        self.vm.platform['images1'] = self.$scope.productInfo.masterField['images1'];

        popup.openUploadImages({
            cartId: self.$scope.cartInfo.value,
            productId: self.$scope.productInfo.productId,
            platform: self.vm.platform,
            showArr: ['image1', 'image6', 'image7', 'image4', 'image5']
        }).then(function (platform) {
            self.vm.platform = platform;

            //暂存
            self.saveProduct('temporary');
        });
    };

    /**
     * 锁平台
     */
    SpJdController.prototype.platFormLock = function () {
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

    /**
     * group info 显示更多图片
     */
    SpJdController.prototype.moreCode = function () {
        var self = this;

        self.moreCodeFlg = !self.moreCodeFlg;
    };

    SpJdController.prototype.canMoreCode = function () {
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

    SpJdController.prototype.popEditGroup = function () {
        var self = this,
            $translate = self.$translate,
            $scope = self.$scope;

        var _mainProduct = _.find(self.vm.mastData.images, function (item) {
            return item.isMain;
        });

        if (!_mainProduct) {
            self.alert('未找到对应主商品信息！');
            return;
        }

        self.popups.openEditGroup({
            mainProdId: _mainProduct.prodId,
            cartId: $scope.cartInfo.value,
            masterField: $scope.productInfo.masterField
        }).then(function (resp) {
            self.vm.platform.modified = resp.data.modified;
            self.notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));

            self.getPlatformData();
        });
    };

    cms.directive('jdSubPage', function () {
        return {
            restrict: 'E',
            controller: ['$scope', 'productDetailService', '$translate', 'notify', 'confirm', '$compile', 'alert', 'popups', '$fieldEditService', '$document', '$templateRequest', SpJdController],
            controllerAs: 'ctrl',
            scope: {
                productInfo: "=productInfo",
                cartInfo: "=cartInfo"
            },
            templateUrl: 'views/product/subpage/jd.sub-page.tpl.html',
            link: function ($scope, element) {
                $scope.ctrl.init(element);
            }
        }
    })

});
