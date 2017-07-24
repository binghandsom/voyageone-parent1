/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
], function (cms) {

    class PriceTabController{

        constructor($scope, detailDataService){
            this.$scope = $scope;
            this.detailDataService = detailDataService;
            this.productInfo = $scope.productInfo;
            this.usPriceList = {};
            this.priceList = {};
            //定义修改价格参数
            this.saveParam = {
                cartId:"",
                prodId:"",
                clientMsrpPrice:"",
                clientRetailPrice:""
            };
        }

        init(){
            this.getData();
        }
        getData(){
            let self = this;
            self.detailDataService.getAllPlatformsPrice(self.productInfo.productId).then(res => {
                //中国平台
                self.usPriceList = res.data.allUsPriceList;
                self.priceList = res.data.allPriceList;
            });
        }
        //修改价格
        save(cartId,priceMsrpSt,priceRetailSt){
            let self = this;
            self.saveParam.cartId = cartId + "";
            self.saveParam.prodId = self.productInfo.productId + "";
            self.saveParam.clientMsrpPrice = priceMsrpSt + "";
            self.saveParam.clientRetailPrice = priceRetailSt + "";
            self.detailDataService.updateOnePrice(self.saveParam).then(res =>{

            });
            //刷新页面
            self.getData();

        }

    }

    cms.directive("priceTab", function () {
        return {
            restrict: "E",
            controller: PriceTabController,
            controllerAs: 'ctrl',
            templateUrl: "views/usa/product/detail/price/price.directive.tpl.html",
            scope: {
                productInfo: "=productInfo"
            }
        };
    });
});