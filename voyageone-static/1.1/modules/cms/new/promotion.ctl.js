/**
 * @Name:    promotion.ctl.js
 * @Date:    2015/8/13
 *
 * @User:    Eric
 * @Version: 1.0.0
 */


define(function (require) {
    var cmsApp = require('modules/cms/cms.module');
    require('modules/cms/edit/edit.service');
    require ('modules/cms/new/new.service');

    cmsApp.controller('newPromotionController', ['$scope', '$location', 'commonService', 'newPromotionService',  'userService','notify','cmsRoute',
        function ($scope, $location, commonService, newPromotionService, userService, notify, cmsRoute) {

            var _ = require('underscore');
            var commonUtil = require('components/util/commonUtil');
            $scope.promotionInfo = {};

           /**
            * 创建Promotion信息,如果创建成功跳转到对应的画面中.
            */
           $scope.doSaveNewPromotionInfo = function () {

               var promotionInfo = angular.copy($scope.promotionInfo);
               promotionInfo.channelId = userService.getSelChannel();
               promotionInfo.preheatDateStart = commonUtil.doFormatDate(promotionInfo.preheatDateStart);
               promotionInfo.preheatDateEnd = commonUtil.doFormatDate(promotionInfo.preheatDateEnd);
               promotionInfo.effectiveDateStart = commonUtil.doFormatDate(promotionInfo.effectiveDateStart);
               promotionInfo.effectiveDateEnd = commonUtil.doFormatDate(promotionInfo.effectiveDateEnd);

               newPromotionService.doSavePromotionInfo (promotionInfo).then (function (data) {
                   commonService.doGetCMSMasterInfo(userService.getSelChannel(), false);
                   notify.success("CMS_TXT_MSG_UPDATE_SUCCESS");
                   $location.path(commonUtil.returnReallyPathByMoreParam(cmsRoute.cms_edit_promotion.hash, ['3', data.promotionId]));
               });
           };

            /**
             * 清除选中的cartId.
             */
            $scope.clearCartId = function () {
                $scope.promotionInfo.cartId = '';
            };

            /**
             * 取消画面输入.
             */
            $scope.undoSaveNewPromotionInfo = function () {
                $scope.promotionInfo = {};
            }
        }])
});