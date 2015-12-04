/**
 * @Name:    popAddToPromotionService.js
 * @Date:    2015/08/24
 * @User:    sky
 * @Version: 1.0.0
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');
    cmsApp.service('popAddToPromotionService',['$q', 'cmsAction', 'ajaxService',
        function ($q, cmsAction, ajaxService) {

            //获取目前有效的活动月份
            this.getActivePromotionMonth = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_promotion_popGetPromMoth, scope);
            };

            //根据月份获取本月有效的promotion
            this.getPromInfo = function (data, scope) {
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_promotion_popGetPromInfo, scope);
            };

            //建立产品和promotion的关系
            this.createPromProductRelation = function(data, scope){
                return ajaxService.ajaxPost(data, cmsAction.cms_edit_promotion_popCreatePromProductRelation, scope);
            }
        }]);
    return cmsApp;
});