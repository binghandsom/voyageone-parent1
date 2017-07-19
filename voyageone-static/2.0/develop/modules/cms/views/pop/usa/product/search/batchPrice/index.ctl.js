/**
 * @description 批量修改价格
 * @author piao
 */
define([
    'cms'
], function (cms) {

    cms.controller('batchPriceController', class BatchPriceController {

        constructor(confirm, advanceSearch, $modalInstance, context,alert) {
            let self = this;
            this.alert = alert;
            this.advanceSearch = advanceSearch;
            this.confirm = confirm;
            this.$modalInstance = $modalInstance;
            this.context = context;
            //设置修改参数
            self.paraMap = {
                //被修改的价格类型clientMsrpPrice,clientRetailPrice
                changedPriceType: "",
                //clientMsrpPrice,clientRetailPrice,fixedValue,如果选择了fixedValue,操作类型默认为=,不能修改
                basePriceType: "",
                //操作类型+-*/=,平台点进去才能选=?
                optionType: "",
                value: "",
                //是否取整"1":是,"0":否,默认为取整
                flag: "1",
                //默认不全选,"1"为全选,"0"为不全选
                selAll:"",
                queryMap: {},
                codeList: {},
                cartId: ""
            };
        }

        //修改价格
        updatePrice(value) {
            let self = this;
            //如果选择固定值,默认操作为=
            if (self.paraMap.basePriceType == "fixedValue") {
                self.paraMap.optionType = "=";
            }
            self.paraMap.selAll = self.context.selAll + "";
            self.paraMap.queryMap = self.context.queryMap;
            self.paraMap.codeList = self.context.codeList;
            self.paraMap.cartId = self.context.cartId + "";
            self.paraMap.value += "";
            if(self.paraMap.codeList.length == 0){
                self.alert("please choose at least one!!!");
                self.$modalInstance.close({success: value});
                return;
            }
            if(self.paraMap.changedPriceType == ""||self.paraMap.basePriceType == "" ||self.paraMap.optionType == "" || self.paraMap.value == ""){
                self.alert("some value is empty!!!");
                self.$modalInstance.close({success: value});
                return;
            }else {
                self.advanceSearch.updatePrice(self.paraMap).then((res) => {
                    //"1",需要清除勾选状态,"0"不需要清除勾选状态
                    self.$modalInstance.close({success: value});
                });
            }
        }

    })

});