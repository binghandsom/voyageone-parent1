define([
 'cms'   
],function (cms) {

    cms.controller('batchApproveController',class batchApproveController{

        constructor(popups, $routeParams, itemDetailService, alert,$location,notify,confirm) {
            this.popups = popups;
            this.itemDetailService = itemDetailService;
            this.alert = alert;
            this.$location = $location;
            this.notify = notify;
            this.confirm = confirm;

            this.id = $routeParams.id;
            if (!this.id) {
                this.alert("Feed not exists.");
                return;
            }

            this.feed = {};
            this.brandList = [];
            this.productTypeList = [];
            this.sizeTypeList = [];
            this.materialList = [{value:"wood",name:"wood"}];
            this.originList = [{value:"CN",name:"CN"}];
            this.colorMap = [{value:"Red",name:"Red"}];
            this.setting = {
                weightOrg:"",
                weightOrgUnit:"",
                priceClientMsrp:"",
                priceNet:"",
                priceMsrp:"",
                priceCurrent:"",

                weightOrgUnits:['kg','lb']
            };
            this.topFeedList = []; // 同Model查询结果
            this.imageUrl = "http://image.sneakerhead.com/is/image/sneakerhead/";
            this.init();
        }

    });

});