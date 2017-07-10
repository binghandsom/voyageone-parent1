define([
 'cms'   
],function (cms) {

    cms.controller('batchApproveController',class batchApproveController{

        constructor(context, alert,$location,notify,confirm) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;

            this.sel_all = context.sel_all; // 是否检索全量
            this.codeList = !context.codeList ? [] : context.codeList;

            this.price = {
                msrp:context.msrp,
                price:context.price
            };
        }

    });

});