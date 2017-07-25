/**
 * @author piao
 * @description 价格一览
 * @version V2.9.0
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
], function (cms) {

    class PriceTabController {

        constructor($scope, detailDataService, notify) {
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
            this.manyMsrp = "";
            this.manySalePrice = "";
            this.manyUsMsrp = "";
            this.manyUsNetPrice = "";

            this.flag = true;
            this.notify = notify;
        }

        init() {
            this.getData();
        }

        getData() {
            let self = this;
            self.detailDataService.getAllPlatformsPrice(self.productInfo.productId).then(res => {
                self.usPriceList = res.data.allUsPriceList;
                self.priceList = res.data.allPriceList;

                _.each(self.usPriceList, item => {
                    item.flag = true;
                });

                _.each(self.priceList, item => {
                    item.flag = true;
                });
            });
        }

        //修改价格
        save(cartId, platform) {
            let self = this;
            self.saveParam.cartId = cartId + "";
            self.saveParam.prodId = self.productInfo.productId + "";
            self.saveParam.clientMsrpPrice = priceMsrpSt + "";
            self.saveParam.clientRetailPrice = priceRetailSt + "";
            self.detailDataService.updateOnePrice([self.saveParam]).then(res =>{
                self.success('Update Success');
                self.getData();
            });
            //刷新页面
        }
        saveAll(){
            let self = this;
            let lists = [];
            //美国平台
            if(self.manyUsMsrp != "" || self.manyUsNetPrice != ""){
                _.each(self.usPriceList, function (value,key) {
                    let map = {
                        cartId: key + "",
                        prodId:self.productInfo.productId + "",
                        clientMsrpPrice:self.manyUsMsrp ? self.manyUsMsrp +"":"",
                        clientRetailPrice:self.manyUsNetPrice?self.manyUsNetPrice+"":""
                    };
                    lists.push(map);
                });
            }
            //中国平台
            if(self.manyMsrp != "" || self.manySalePrice != ""){
                _.each(self.priceList, function (value,key) {
                    let map = {
                        cartId: key + "",
                        prodId:self.productInfo.productId + "",
                        clientMsrpPrice:self.manyMsrp ? self.manyMsrp +"":"",
                        clientRetailPrice:self.manySalePrice?self.manySalePrice+"":""
                    }
                    lists.push(map);
                })
            }
            self.detailDataService.updateOnePrice(lists).then(res =>{
                self.manyMsrp = "";
                self.manySalePrice = "";
                self.manyUsMsrp = "";
                self.manyUsNetPrice = "";
                self.success('Update Success');
                self.getData();
            });
        }

        //批量编辑
       /* changeAll(type,value){
            let self = this;
            if(type == "manyMsrp"){
                _.each(self.usPriceList, item => {
                    item.priceMsrpSt = value;
                    item.priceMsrpEd = value;
                });
            }
            if(type == "manySalePrice"){
                _.each(self.usPriceList, item => {
                    item.priceRetailSt = value;
                    item.priceRetailEd = value;
                });
            }
            if(type == "manyUsMsrp"){
                _.each(self.priceList, item => {
                    item.priceMsrpSt = value;
                    item.priceMsrpEd = value;
                });
            }
            if(type == "manyUsNetPrice"){
                _.each(self.priceList, item => {
                    item.priceRetailSt = value;
                    item.priceRetailEd = value;
                });
            }
        }*/


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