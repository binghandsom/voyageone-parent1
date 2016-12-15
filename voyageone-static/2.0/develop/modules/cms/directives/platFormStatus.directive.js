/**
 * @Description: 解析平台状态
 * @example:{  cartId:entity.cartId,
               status:entity.status,
               pStatus:entity.pStatus,
               isMain:entity.pIsMain,
               pPublishError:entity.pPublishError,
               numberId:entity.pNumIId}
 * @User:    tony-piao
 * @Version: 2.1.0, 2016-6-24
 */
define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, carts) {

    var statusDes = {
            "WaitingPublish": "待",
            "InStock": "库",
            "OnSale": "售"
        },
        pStatusDes = {
            "InStock": "(未)",
            "OnSale": "(已)",
            "Out": "(无)"
        },
        outstatusDes = {
            "Pending": "",
            "Ready": "",
            "Approved": ":待"
        };

    return cms.directive("platformStatus", function () {
        function StatusController($scope, $attrs) {
            this.$attrs = $attrs;
            this.$scope = $scope;
            this.statusData = null;
        }

        StatusController.prototype.init = function () {
            this.statusData = this.$scope.data;
            var _numberId = this.statusData.numberId,
                _cartId = +this.statusData.cartId,
                _cartInfo = carts.valueOf(_cartId);

            if (!this.statusData)
                return;

            if (this.statusData.pPublishError)
                this.statusData.approveError = true;

            if (_numberId) {
                if (_cartId != 27) {
                    this.statusData.detailUrl = _cartInfo.pUrl + _numberId;
                } else {
                    this.statusData.detailUrl = _cartInfo.pUrl + _numberId + ".html";
                }

            }

            this._cartInfo = _cartInfo;

            if (this.statusData.pReallyStatus) {
                this.pStatusDesc = statusDes[this.statusData.pReallyStatus];
                this.pReallyStatusDesc = pStatusDes[this.statusData.pReallyStatus];
            } else {
                this.pStatusDesc = outstatusDes[this.statusData.status];
                this.pReallyStatusDesc = pStatusDes['Out'];
            }

        };

        return {
            restrict: 'E',
            scope: {data: "=data"},
            controller: StatusController,
            controllerAs: 'ctrl',
            /** 编辑模板的话请到html页面编辑，
             * 然后压缩拷进来。*/
            template: '<span class="plateform-status" ng-if="!ctrl.statusData.pReallyStatus"><span class="label"  ng-class="{\'pending\': ctrl.statusData.status == \'Pending\',                                     \'ready\':ctrl.statusData.status == \'Ready\' ,                                     \'waiting-publish\':ctrl.statusData.status == \'Approved\'}"           title="{{ctrl.statusData.status}}"><span ng-class="{\'isMain\': ctrl.statusData.isMain,                             \'isCommon\':!ctrl.statusData.isMain}">                {{ctrl._cartInfo.name}}{{ctrl.pStatusDesc}}{{ctrl.statusData.status == \'Approved\'? ctrl.pReallyStatusDesc:""}}</span></span></span><span class="plateform-status" ng-if="ctrl.statusData.pReallyStatus"><span class="label {{ctrl.statusData.approveError ? \'error\' : \'\'}}"           ng-class="{\'in-stock\': ctrl.statusData.pReallyStatus == \'InStock\',\'on-sale\':ctrl.statusData.pReallyStatus == \'OnSale\'}"           title="{{ctrl.statusData.pStatus}}"><span ng-if="ctrl.statusData.numberId"><a ng-href="{{ctrl.statusData.detailUrl}}" target="_blank" ng-class="{\'isMain\': ctrl.statusData.isMain,\'isCommon\':!ctrl.statusData.isMain}">                 {{ctrl._cartInfo.name}}:{{ctrl.pStatusDesc}}{{ctrl.pReallyStatusDesc ? ctrl.pReallyStatusDesc:""}}</a></span><span ng-if="!ctrl.statusData.numberId" ng-class="{\'isMain\': ctrl.statusData.isMain,\'isCommon\':!ctrl.statusData.isMain}">             {{ctrl._cartInfo.name}}:{{ctrl.pStatusDesc}}{{ctrl.pReallyStatusDesc ? ctrl.pReallyStatusDesc:""}}</span></span></span>',
            link: function ($scope) {
                $scope.ctrl.init();
            }
        }
    });
});
