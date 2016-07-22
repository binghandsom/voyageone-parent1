/**
 * @Description: 解析平台状态
 * @example:{  cartId:entity.cartId,
               cartName:entity.cartName,
               status:entity.status,
               pStatus:entity.pStatus,
               pPublishError:entity.pPublishError,
               numberId:entity.pNumIId}
 * @User:    tony-piao
 * @Version: 2.1.0, 2016-6-24
 */
define([
    'cms',
    'modules/cms/enums/Carts'
], function (cms, carts) {
    'use strict';
    return cms.directive("platformStatus", function() {
        function StatusController($scope, $attrs) {

            this.$attrs = $attrs;
            this.$scope = $scope;
            this.statusData = null;
        }

        StatusController.prototype.init = function(){
            this.statusData = this.$scope.data;
            //对Error进行处理
            if(!this.statusData)
                return;

            if(this.statusData.pPublishError)
                this.statusData.pStatus = "Error";

            var _numberId = this.statusData.numberId;

            if(_numberId){

                var _cartId = +this.statusData.cartId;

                if(_cartId != 27){
                    this.statusData.detailUrl = carts.valueOf(_cartId).pUrl + _numberId;
                }else{
                    this.statusData.detailUrl =carts.valueOf(_cartId).pUrl + _numberId + ".html";
                }

            }
        };

        return {
            restrict: 'E',
            scope: {data:"=data"},
            controller: StatusController,
            controllerAs: 'ctrl',
            template: "<span class='plateform-status' ng-if='ctrl.statusData.status != \"Approved\"'>"
                    + "<span class='label' ng-class='{\"pending\": ctrl.statusData.status == \"Pending\",\"ready\":ctrl.statusData.status == \"Ready\" }' title='{{ctrl.statusData.status}}'>"
                    + "<span style='color:black'>{{ctrl.statusData.cartName}}</span>"
                    + "</span>"
                    + "</span>"
                    + "<span class='plateform-status' ng-if='ctrl.statusData.status == \"Approved\"'>"
                    + "<span class='label' ng-class='{\"waiting-publish\": ctrl.statusData.pStatus == \"WaitingPublish\",\"in-stock\": ctrl.statusData.pStatus == \"InStock\",\"on-sale\":ctrl.statusData.pStatus == \"OnSale\",\"error\":ctrl.statusData.pStatus == \"Error\"}'  title='{{ctrl.statusData.pStatus}}'>"
                    + "<span ng-if='ctrl.statusData.numberId'><a ng-href='{{ctrl.statusData.detailUrl}}' target='_blank' style='color: #000;text-decoration: none;'>{{ctrl.statusData.cartName}}</a></span>"
                    + "<span ng-if='!ctrl.statusData.numberId'>{{ctrl.statusData.cartName}}</span>"
                    + "</span>"
                    + "</span>",
            link: function ($scope) {
                $scope.ctrl.init();
            }
        }
    });
});
