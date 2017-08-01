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
                changedPriceType: "clientRetailPrice",
                //clientMsrpPrice,clientRetailPrice,fixedValue,如果选择了fixedValue,操作类型默认为=,不能修改
                basePriceType: "clientRetailPrice",
                //操作类型+-*/=,平台点进去才能选=?
                optionType: "*",
                /*value:null,*/
                //是否取整"1":是,"0":否,默认为取整
                flag: "1",
                //默认不全选,"1"为全选,"0"为不全选
                selAll:"",
                queryMap: {},
                codeList: [],
                cartId: ""
            };
        }

        //修改价格
        updatePrice(value) {
            let self = this;
            let paraMap1 = angular.copy(self.paraMap);

            //如果选择固定值,默认操作为=
            if (paraMap1.basePriceType == "fixedValue") {
                paraMap1.optionType = "=";
            }
            paraMap1.selAll = self.context.selAll + "";
            paraMap1.queryMap = self.context.queryMap;
            paraMap1.codeList = self.context.codeList;
            paraMap1.cartId = self.context.cartId + "";
            paraMap1.value += "";

            if(paraMap1.changedPriceType == ""||paraMap1.basePriceType == "" ||paraMap1.optionType == "" || paraMap1.value == ""){
                self.alert("some value is empty!!!");
                self.$modalInstance.close({success: value,type:0});
                return;
            }else {
                self.advanceSearch.updatePrice(paraMap1).then((res) => {
                    //"1",需要清除勾选状态,"0"不需要清除勾选状态
                    self.$modalInstance.close({success: value,type:1});
                });
            }
        }

    })

});