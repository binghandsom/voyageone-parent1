define([
 'cms'   
],function (cms) {

    cms.controller('batchApproveController',class batchApproveController{

        constructor(context, alert,$location,notify,confirm,itemDetailService) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.itemDetailService = itemDetailService;

            this.sel_all = context.sel_all; // 是否检索全量
            this.codeList = !context.codeList ? [] : context.codeList;

            this.platforms = [
                {cartId:23,name:"TM"},
                {cartId:27,name:"JM"}
            ];
            this.usPlatforms = [
                {cartId:1,name:"U.S.Official"},
                {cartId:2,name:"Amazon"},
            ];

            this.price = {
                msrp:context.msrp,
                price:context.price
            };
        }

        approve() {
            self.itemDetailService.
        }

    });

});