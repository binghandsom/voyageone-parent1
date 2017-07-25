/**
 * @description 批量修改价格
 * @author piao
 */
define([
    'cms'
], function (cms) {

    cms.controller('batchPriceController', class BatchPriceController {

        constructor(confirm, advanceSearch, $modalInstance, context) {
            let self = this;
            this.advanceSearch = advanceSearch;
            this.confirm = confirm;
            this.$modalInstance = $modalInstance;
            this.context = context;
            this.usPlatformName = "";
            //设置修改参数
            self.paraMap = {
                //在当前时间之后多少天执行操作,默认为0;
                days:0,
                //"list":上架,"deList":下架
                activeStatus: "",
                //平台id,默认为0
                cartId:"",
                selAll:"",
                queryMap: {},
                codeList: {}
            };
            self.usPlatformName = self.context.usPlatformName;
            //亚马逊平台的日期默认设置为45
            if(self.context.cartId == 5){
                self.paraMap.days = 45;
            }
        }

        //上架和下架操作
        listOrDelist(value) {
            let self = this;
            self.paraMap.selAll = self.context.selAll + "";
            self.paraMap.queryMap = self.context.queryMap;
            self.paraMap.codeList = self.context.codeList;
            self.paraMap.activeStatus = self.context.activeStatus;
            self.paraMap.cartId = self.context.cartId + "";
            self.paraMap.days += "";
            if(self.paraMap.codeList.length == 0){
                self.alert("please choose at least one!!!");
                self.$modalInstance.close({success: value,type:1});
                return;
            }
            self.advanceSearch.listOrDelist(self.paraMap).then((res) => {
                //"1",需要清除勾选状态,"0"不需要清除勾选状态
                self.$modalInstance.close({success: value,type:1});
            });
        }

    })

});