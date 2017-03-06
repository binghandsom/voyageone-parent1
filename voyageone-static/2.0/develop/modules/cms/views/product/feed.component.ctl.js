/**
 * @author tony-piao
 * feed产品概述
 */
define([
    'cms'
], function (cms) {
    cms.directive("feedSchema", function () {

        function feedComponentCtl($scope, $attrs, $element, productDetailService, notify, alert) {
            this.$scope = $scope;
            this.$attrs = $attrs;
            this.$element = $element;
            this.productDetailService = productDetailService;
            this.notify = notify;
            this.alert = alert;
        }

        feedComponentCtl.prototype.updateFeedInfo = function () {
            var self = this,
                productDetailService = self.productDetailService,
                $scope = self.$scope;

            productDetailService.updateProductAtts({
                prodId: $scope.productInfo.productId,
                feedInfo: $scope.productInfo.feedInfo
            }).then(function () {
                self.notify.success("更新成功!");
            }, function () {
                self.alert("更新失败！");
            });
        };

        /**
         * 右侧导航栏
         * @param index div的index
         * @param speed 导航速度 ms为单位
         */
        feedComponentCtl.prototype.pageAnchor = function (area, speed) {
            var offsetTop = 0,
                element = this.$element;

            if (area != "attribute")
                offsetTop = element.find("#" + area).offset().top;
            $("body").animate({scrollTop: offsetTop - 70}, speed);
        };

        feedComponentCtl.prototype.focusError = function () {
            var firstError = this.$element.find("input.ng-invalid:first");
            firstError.focus();
            firstError.addClass("focus-error");
        };

        return {
            restrict: "E",
            controller: feedComponentCtl,
            controllerAs: 'ctrl',
            templateUrl: "views/product/feed.component.tpl.html",
            scope: {productInfo: "=productInfo"}
        };
    });
});
