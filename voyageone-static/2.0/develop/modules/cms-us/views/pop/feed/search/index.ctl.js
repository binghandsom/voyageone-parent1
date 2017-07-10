define([
 'cms'   
],function (cms) {

    cms.controller('batchApproveController',class batchApproveController{

        constructor(context, alert,$location,notify,confirm) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;

            this.codeList = !context.codeList ? [] : context.codeList;
            console.log(this.codeList);
            this.price = {
                msrp:context.msrp,
                price:context.price
            };
        }

    });

});