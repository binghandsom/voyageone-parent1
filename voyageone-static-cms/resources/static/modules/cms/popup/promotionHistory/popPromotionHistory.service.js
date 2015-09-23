/**
 * @Name:    popPromotionHistoryService.js
 * @Date:    2015/09/07
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');
    cmsApp.service('popPromotionHistoryService',['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            //根据产品Id和cartId查询对应的promotion
            this.searchPromotionHistory = function (data, scope) {
                var defer = $q.defer();
                ajaxService.ajaxPostWithData(data, cmsAction.cms_edit_product_doGetPromotionHistory).then(function (res){
                    defer.resolve(res.data);
                });
                return defer.promise;
            };
        }]);
    return cmsApp;
});