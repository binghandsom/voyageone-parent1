define([
 'cms'   
],function (cms) {

    cms.controller('batchApproveController',class batchApproveController{

        constructor(context, alert,$location,notify,confirm,$modalInstance,itemDetailService,commonService) {
            this.alert = alert;
            this.notify = notify;
            this.confirm = confirm;
            this.$modalInstance = $modalInstance;
            this.itemDetailService = itemDetailService;
            this.commonService = commonService;

            this.sel_all = context.sel_all; // 是否检索全量
            this.codeList = !context.codeList ? [] : context.codeList;

            this.price = {
                msrp:context.msrp,
                price:context.price
            };

            this.platforms = [];

            this.init();
        }

        init() {
            let self = this;
            self.commonService.getChannelCarts().then((res) => {
                if (res.data) {
                    self.platforms = res.data;
                    angular.forEach(self.platforms, function (cartOjb) {
                        _.extend(cartOjb, {cartId:parseInt(cartOjb.value), day:0});
                    });
                    console.log(self.platforms);
                }
            });
        }

        approve() {
            // self.itemDetailService.
            let self = this;
            let approveInfo = [];
            angular.forEach(self.platforms, function (platform) {
                approveInfo.push({cartId:platform.cartId,approve:platform.checked,day:platform.day})
            });
            let params = {
                sel_all:self.sel_all,
                codeList:self.codeList,
                approveItems:approveInfo,
                searchMap:{}
            };
            self.itemDetailService.approve(params).then((res) => {
                if (res.data) {
                    console.log(res.data);
                    self.$modalInstance.close({success:true});
                }
            });
        }

        complete() {
            let self = this;


        }

    });

});