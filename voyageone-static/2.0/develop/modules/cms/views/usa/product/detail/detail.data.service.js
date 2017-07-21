/**
 * @description 由于angular service为单例模式
 *              该service产品详情页统一ajax入口
 */
define([
    'cms'
], function (cms) {

    class DetailDataService {
        constructor($usProductDetailService, $q, $productDetailService) {
            this.$usProductDetailService = $usProductDetailService;
            this.$productDetailService = $productDetailService;
            this.$q = $q;
        }

        getProductInfo(upEntity) {
            let self = this,
                defer = self.$q.defer();

            self.$usProductDetailService.getProductInfo(upEntity).then(res => {
                defer.resolve(res);
            }, res => {
                defer.reject(res);
            });

            return defer.promise;
        }

        // 取得SKU的库存信息
        getSkuStockInfo(req) {
            let self = this,
                defer = self.$q.defer();

            self.$productDetailService.getSkuStockInfo(req)
                .then(function (resp) {
                    defer.resolve(resp);
                }, function (resp) {
                    defer.reject(resp)
                });

            return defer.promise;
        }

        //获取价格信息
        updateOnePrice(req){
            let self = this,
                defer = self.$q.defer();

            self.$usProductDetailService.updateOnePrice(req).then(res => {
                defer.resolve(res);
            }, res => {
                defer.reject(res);
            });

            return defer.promise;
        }

    }

    cms.service('detailDataService', DetailDataService);

});
