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

            // 美国各平台状态
            this.platformStatus = {
                Pending:"Pending",
                OnSale:"List",
                InStock:"Delist"
            };
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
        getAllPlatformsPrice(req){
            let self = this,
                defer = self.$q.defer();

            self.$usProductDetailService.getAllPlatformsPrice(req).then(res => {
                defer.resolve(res);
            }, res => {
                defer.reject(res);
            });

            return defer.promise;
        }

        //更新价格
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

        //获取产品平台信息
        getProductPlatform(req){
            let self = this,
                defer = self.$q.defer();

            self.$usProductDetailService.getProductPlatform(req).then(res => {
                defer.resolve(res);
            }, res => {
                defer.reject(res);
            });

            return defer.promise;
        }

        /**
         * SN Tab 平台保存
         */
        updateCommonProductInfo(req){
            let self = this,
                defer = self.$q.defer();

            self.$usProductDetailService.updateCommonProductInfo(req).then(res => {
                defer.resolve(res);
            }, res => {
                defer.reject(res);
            });

            return defer.promise;
        }

        /**全schema中通过name递归查找field*/
        searchField(fieldId, schema) {
            let self = this;

            let result = null;

            _.find(schema, function (field) {

                if (field.id === fieldId) {
                    result = field;
                    return true;
                }

                if (field.fields && field.fields.length) {
                    result = self.searchField(fieldId, field.fields);
                    if (result)
                        return true;
                }

                return false;
            });

            return result;
        }

        /**
         * @description 平台保存
         */
        updateProductPlatform(req){
            let self = this,
                defer = self.$q.defer();

            self.$usProductDetailService.updateProductPlatform(req).then(res => {
                defer.resolve(res);
            }, res => {
                defer.reject(res);
            });

            return defer.promise;
        }

    }

    cms.service('detailDataService', DetailDataService);

});
