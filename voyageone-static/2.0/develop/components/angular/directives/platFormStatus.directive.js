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
angular.module('voyageone.angular.directives').directive('platformStatus', function () {

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

            if(this.statusData.numberId){
                switch (+this.statusData.cartId){
                    case 23:
                        /**天猫*/
                        this.statusData.detailUrl = "https://detail.tmall.hk/hk/item.htm?id="+this.statusData.cartId;
                        break;
                    case 26:
                        /**京东*/
                        this.statusData.detailUrl = "http://ware.shop.jd.com/onSaleWare/onSaleWare_viewProduct.action?wareId="+this.statusData.cartId;
                        break;
                    case 27:
                        /**聚美*/
                        this.statusData.detailUrl = "http://item.jumeiglobal.com/"+this.statusData.cartId+".html";
                        break;
                }
            }
        };

        return {
            restrict: 'E',
            scope: {data:"=data"},
            controller: StatusController,
            controllerAs: 'ctrl',
            template: "<span class='plateform-status' ng-if='ctrl.statusData.status != \"Approved\"'>"
                        +"<span class='label' ng-class='{\"pending\": ctrl.statusData.status == \"Pending\",\"ready\":ctrl.statusData.status == \"Ready\" }' title='{{ctrl.statusData.status}}'>"
                            +"<span style='color:black'>{{ctrl.statusData.cartName}}</span>"
                        +"</span>"
                     +"</span>"
                     +"<span class='plateform-status' ng-if='ctrl.statusData.status == \"Approved\"'>"
                        +"<span class='label' ng-class='{\"waiting-publish\": ctrl.statusData.pStatus == \"WaitingPublish\",\"in-stock\": ctrl.statusData.pStatus == \"InStock\",\"on-sale\":ctrl.statusData.pStatus == \"OnSale\",\"error\":ctrl.statusData.pStatus == \"Error\"}'  title='{{ctrl.statusData.status}}'>"
                            +"<span ng-if='ctrl.statusData.numberId'><a ng-href='{{ctrl.statusData.detailUrl}}' target='_blank' style='color: #000;text-decoration: none;'>{{ctrl.statusData.cartName}}</a></span>"
                            +"<span ng-if='!ctrl.statusData.numberId'>{{ctrl.statusData.cartName}}</span>"
                        +"</span>"
                     +"</span>",
            link: function ($scope) {
                $scope.ctrl.init();
            }
        }
    });
