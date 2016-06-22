/**
 * Created by linanbin on 15/12/7.
 */
define([
    'cms'
], function (cms) {

    return cms.controller('popJmProductDetailController', (function () {

        function JmProductDetailController($translate, $modalInstance, jmPromotionDetailService, notify, confirm, alert, context) {
            this.translate = $translate;
            this.modalInstance = $modalInstance;
            this.jmPromotionDetailService = jmPromotionDetailService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
            this.context = context;
            this.masterData = {};
            this.model={};
        }
        JmProductDetailController.prototype = {
            initialize: function () {
                var self = this;
                self.jmPromotionDetailService.getProductView(self.context.promotionProductId).then(function(res){
                    self.model=res.data;
                });
                //self.jmPromotionDetailService.getProductMasterData().then(function (res) {
                //    self.masterData = res.data;
                //    self.jmPromotionDetailService.getProductDetail(self.context).then(function (res) {
                //        self.productImage = res.data.productImage;
                //        self.productInfo = res.data.productInfo;
                //        self.productPromotionInfo = res.data.productPromotionInfo;
                //        self.skuList = res.data.skuList;
                //    })
                //})
            },
            saveProductInfo: function () {
                var self = this;
                self.jmPromotionDetailService.updateProductDetail(self.productInfo).then(function () {
                    self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                })
            },
            saveDealInfo: function () {
                var self = this;
                self.jmPromotionDetailService.updatePromotionProductDetail({
                    'cmsBtJmProductModel': self.productInfo,
                    'cmsBtJmPromotionProductModel': self.productPromotionInfo
                }).then(function () {
                    self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                })
            },
            saveSku: function (skuInfo, index) {
                var self = this;
                if (!skuInfo.cmsBtJmPromotionSkuModel.id) {
                    skuInfo.cmsBtJmPromotionSkuModel.cmsBtJmSkuId = skuInfo.cmsBtJmSkuModel.id;
                    skuInfo.cmsBtJmPromotionSkuModel.cmsBtJmPromotionId = self.context.cmsBtJmPromotionId;
                    skuInfo.cmsBtJmPromotionSkuModel.cmsBtJmProductId = self.productPromotionInfo.cmsBtJmProductId;
                    skuInfo.cmsBtJmPromotionSkuModel.skuCode = skuInfo.cmsBtJmSkuModel.skuCode;
                    skuInfo.cmsBtJmPromotionSkuModel.jmSize = skuInfo.cmsBtJmSkuModel.jmSize;
                }
                self.jmPromotionDetailService.updateSkuDetail(skuInfo).then(function (res) {
                    self.skuList[index] = res.data;
                    self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                })
            },
            updateDealPrice:function(skuInfo,index){
                var self = this;
                var parameter={};
                parameter.promotionSkuId=skuInfo.id;
                parameter.dealPrice=skuInfo.dealPrice;
                parameter.marketPrice=skuInfo.marketPrice;
                self.jmPromotionDetailService.updateDealPrice(parameter).then(function (res) {
                    skuInfo.isSave=false;
                    self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                })
            },
            deleteSku: function (promotionSkuInfo, index) {
                var self = this;
                self.confirm(this.translate.instant('TXT_MSG_DELETE_ITEM')).result
                    .then(function () {
                        self.jmPromotionDetailService.deletePromotionSku(promotionSkuInfo).then(function () {
                            self.skuList[index].cmsBtJmPromotionSkuModel = {};
                            self.notify.success(self.translate.instant('TXT_MSG_DELETE_SUCCESS'));
                        })
                    });
            },
            close: function () {
                this.modalInstance.close('');
            }
        };
        return JmProductDetailController;
    })())
});