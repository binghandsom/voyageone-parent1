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
            //cms对应状态
            this.statusDes = {
                "WaitingPublish":"等待上新",
                "InStock":"在库",
                "OnSale":"在售"
            },
            //平台对应状态
            this.pStatusDes = {
                "InStock":"未上架",
                "OnSale":"已上架"
            }
        }

        StatusController.prototype.init = function(){
            this.statusData = this.$scope.data;
            //对Error进行处理
            if(!this.statusData)
                return;

            if(this.statusData.pPublishError)
                this.statusData.pStatus = "Error";

            var _numberId = this.statusData.numberId,
                _cartId = +this.statusData.cartId;

            this.cartEntity = carts.valueOf(_cartId);

            if(_numberId){

                if(_cartId != 27){
                    this.statusData.detailUrl = this.cartEntity.pUrl + _numberId;
                }else{
                    this.statusData.detailUrl =this.cartEntity.pUrl + _numberId + ".html";
                }

            }

            /**由于cms的商品状态和平台的商品状态可能不一致
             * 条件1：两者状态相同时只显示cms的商品状态
             * 条件2：两者状态不一致时两者状态都要显示*/
            this.pStatusDesc = this.statusDes[this.statusData.pStatus];
            if(this.statusData.pReallyStatus && this.statusData.pStatus != this.statusData.pReallyStatus){
                this.pReallyStatusDesc = this.pStatusDes[this.statusData.pReallyStatus];
            }

        };

        return {
            restrict: 'E',
            scope: {data:"=data"},
            controller: StatusController,
            controllerAs: 'ctrl',
            template: "<span class='plateform-status' ng-if='ctrl.statusData.status != \"Approved\"'>"
                    + "<span class='label' ng-class='{\"pending\": ctrl.statusData.status == \"Pending\",\"ready\":ctrl.statusData.status == \"Ready\" }' title='{{ctrl.statusData.status}}'>"
                    + "<span style='color:black'>{{ctrl.cartEntity.name}}</span>"
                    + "</span>"
                    + "</span>"
                    + "<span class='plateform-status' ng-if='ctrl.statusData.status == \"Approved\"'>"
                    + "<span class='label' ng-class='{\"waiting-publish\": ctrl.statusData.pStatus == \"WaitingPublish\",\"in-stock\": ctrl.statusData.pStatus == \"InStock\",\"on-sale\":ctrl.statusData.pStatus == \"OnSale\",\"error\":ctrl.statusData.pStatus == \"Error\"}'  title='{{ctrl.statusData.pStatus}}'>"
                    + "<span ng-if='ctrl.statusData.numberId'><a ng-href='{{ctrl.statusData.detailUrl}}' target='_blank' style='color: #000;text-decoration: none;'>{{ctrl.cartEntity.name}}:{{ctrl.pStatusDesc}}{{ctrl.pReallyStatusDesc?'-'+ctrl.pReallyStatusDesc:''}}</a></span>"
                    + "<span ng-if='!ctrl.statusData.numberId'>{{ctrl.cartEntity.name}}:{{ctrl.pStatusDesc}}{{ctrl.pReallyStatusDesc?'-'+ctrl.pReallyStatusDesc:''}}</span>"
                    + "</span>"
                    + "</span>",
            link: function ($scope) {
                $scope.ctrl.init();
            }
        }
    });
});
