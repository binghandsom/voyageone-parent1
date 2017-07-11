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
            this.updateModel = context.updateModel; // 是否是更新CmsFeedModel对象
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
                }
            });
        }

        approve() {
            // self.itemDetailService.
            let self = this;
            let checkCarts = _.filter(self.platforms, item => {
               return item.checked;
            });

            let approveInfo = {};
            angular.forEach(checkCarts, item => {
                let _tmp ={};
                _tmp[item.cartId] = item.day;
                _.extend(approveInfo, _tmp);
            });
            let params = {
                sel_all:self.sel_all,
                codeList:self.codeList,
                approveInfo:approveInfo,
                searchMap:{}
            };
            if (self.updateModel) {
                self.$modalInstance.close({success:true,approveInfo:approveInfo});
            } else {
                self.itemDetailService.approve(params).then((res) => {
                    self.$modalInstance.close({success:true});
                });
            }
        }
    });

});