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

        constructor($scope, detailDataService, notify,alert) {
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
                clientRetailPrice:"",
                isSale:"",
                days:null
            };
            this.manyMsrp = "";
            this.manySalePrice = "";
            this.manyUsMsrp = "";
            this.manyUsNetPrice = "";

            this.flag = true;
            this.notify = notify;
            this.alert = alert;
        }

        broadcast(eventName,context){
            let self = this;

            self.$scope.$parent.$broadcast(eventName, context);
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
                    if(item.isSale == "1" || item.isSale == null ){
                        item.tempIsSale = true;
                    }else {
                        item.tempIsSale = false;
                    }
                });
                _.each(self.priceList, item => {
                    item.flag = true;
                    if(item.isSale == "1" || item.isSale == null ){
                        item.tempIsSale = true;
                    }else {
                        item.tempIsSale = false;
                    }
                });
            });
        }
        //修改价格
        save(cartId, priceMsrpSt,priceRetailSt,isSale,days) {
            let self = this;
            if((priceMsrpSt == null || priceMsrpSt == "0") || (priceRetailSt == null || priceRetailSt == "0")){
                self.alert('value can not to be 0 or empty!');
                return;
            }else{
                self.saveParam.cartId = cartId + "";
                self.saveParam.prodId = self.productInfo.productId + "";
                self.saveParam.clientMsrpPrice = priceMsrpSt + "";
                self.saveParam.clientRetailPrice = priceRetailSt + "";
                self.saveParam.isSale = isSale == true?"1":"0";
                self.saveParam.days = days;
                self.detailDataService.updateOnePrice([self.saveParam]).then(() =>{
                    self.notify.success('Update Success');
                    self.getData();

                    //广播到相应平台
                    self.broadcast('price.save',{cartId:cartId});
                });
            }
        }

        //批量修改价格
        saveAll(){
            let self = this;
            if(self.manyUsMsrp == "0" || self.manyUsNetPrice == "0"|| self.manyMsrp == "0" ||self.manySalePrice == "0" ){
                self.alert('value can not to be 0!');
                return;
            }
            let lists = [];
            //美国平台
            self.manyUsMsrp = self.manyUsMsrp == null ?"":self.manyUsMsrp;
            self.manyUsNetPrice = self.manyUsNetPrice == null ?"":self.manyUsNetPrice;
            self.manyMsrp = self.manyMsrp == null ?"":self.manyMsrp;
            self.manySalePrice = self.manySalePrice == null ?"":self.manySalePrice;
            if(self.manyUsMsrp == "" && self.manyUsNetPrice == ""&& self.manyMsrp == "" &&self.manySalePrice == "" ){
                self.alert('value can not to be empty!');
                return;
            }
            if(self.manyUsMsrp != ""  || self.manyUsNetPrice != ""){
                _.each(self.usPriceList, function (value,key) {
                    let map = {
                        cartId: key + "",
                        prodId:self.productInfo.productId + "",
                        clientMsrpPrice:self.manyUsMsrp ? self.manyUsMsrp +"":"",
                        clientRetailPrice:self.manyUsNetPrice?self.manyUsNetPrice+"":"",
                    };
                    lists.push(map);
                });
            }
            //中国平台 manyMsrp manySalePrice
            if(self.manyMsrp != "" || self.manySalePrice != ""){
                _.each(self.priceList, function (value,key) {
                    let map = {
                        cartId: key + "",
                        prodId:self.productInfo.productId + "",
                        clientMsrpPrice:self.manyMsrp ? self.manyMsrp +"":"",
                        clientRetailPrice:self.manySalePrice?self.manySalePrice+"":"",
                    }
                    lists.push(map);
                })
            }
            self.detailDataService.updateOnePrice(lists).then(() =>{
                self.manyMsrp = "";
                self.manySalePrice = "";
                self.manyUsMsrp = "";
                self.manyUsNetPrice = "";
                self.notify.success('Update Success');
                self.getData();

                //广播到相应平台
                self.broadcast('price.save',{cartId:0});
            });
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