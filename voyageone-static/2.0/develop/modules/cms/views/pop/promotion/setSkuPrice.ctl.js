/**
 * @author piao
 * @date 2016-10-08
 * @version V2.8.0
 */
define([
    'cms'
], function (cms) {

    return cms.controller('setSkuPriceCtl', (function () {

        function SetSkuPriceController(context, promotionDetailService, $uibModalInstance, notify) {
            this.context = context;
            this.promotionDetailService = promotionDetailService;
            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;
        }

        SetSkuPriceController.prototype.init = function () {
            var self = this,
                promotionDetailService = self.promotionDetailService,
                context = self.context;

            promotionDetailService.getPromotionSkuList({
                productCode: context.productCode,
                promotionId: context.promotionId
            }).then(function (res) {
                self.dataList = res.data;
            });
        };

        SetSkuPriceController.prototype.flowPercent = function (promotionPrice, msrpRmb) {

            if (promotionPrice <= 0)
                return '--';

            if (msrpRmb <= 0)
                return '--';

            return (promotionPrice / msrpRmb * 100).toFixed(2);
        };

        SetSkuPriceController.prototype.save = function () {
            var self = this,
                promotionDetailService = self.promotionDetailService,
                $uibModalInstance = self.$uibModalInstance,
                notify = self.notify,
                upEntity;

            upEntity = _.map(self.dataList, function (item) {
                return {'id': item.id, 'promotionPrice': item.promotionPrice};
            });

            promotionDetailService.saveSkuPromotionPrices(upEntity).then(function () {
                notify.success("TXT_SAVE_SUCCESS");
                $uibModalInstance.close();
            });
        };

        return SetSkuPriceController;

    })());

});