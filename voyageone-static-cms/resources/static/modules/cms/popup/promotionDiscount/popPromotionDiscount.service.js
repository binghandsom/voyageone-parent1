/**
 * @Name:    popup.service.js
 * @Date:    2015/8/21
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {

    var cmsApp = require ('modules/cms/cms.module');
    require('components/services/ajax.service');

    cmsApp.service ('popPromotionDiscountService', ['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            /**
             * 更新promotion的折扣设置.
             * @param data
             * @returns {*}
             */
            this.doUpdatePromotionDiscount = function (data) {
                var defer = $q.defer ();
                ajaxService.ajaxPostWithData (data, cmsAction.cms_edit_promotion_discount_doUpdatePromotionDiscount)
                    .then (function (response) {
                    defer.resolve (response);
                });
                return defer.promise;
            };
        }]);
    return cmsApp;
});