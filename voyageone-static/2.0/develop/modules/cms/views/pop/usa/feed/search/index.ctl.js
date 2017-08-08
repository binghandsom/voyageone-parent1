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

            this.selAll = context.selAll; // 是否检索全量
            this.codeList = !context.codeList ? [] : context.codeList;
            this.context = context;
            this.platforms = [];

            this.init();
        }

        init() {
            let self = this;
            self.commonService.getChannelCarts().then((res) => {
                if (res.data) {
                    let platforms = res.data;
                    _.each(platforms, cartOjb => {
                        let cartId = parseInt(cartOjb.value);
                        let day = 0;
                        if (cartId == 5) {
                            day = 45;
                        }
                        _.extend(cartOjb, {cartId:cartId, day:day, checked:true});
                    });
                    self.platforms = platforms;
                }
            });
        }

        approve() {
            let self = this;
            let approveInfo = [];
            _.each(self.platforms, platform => {
                let _tmp ={cartId:platform.cartId, day:platform.day, checked:platform.checked};
                approveInfo.push(_tmp);
            });
            let params = {
                selAll:self.selAll,
                codeList:self.codeList,
                approveInfo:approveInfo,
                searchMap:self.context.searchMap
            };
            self.itemDetailService.approve(params).then((res) => {
                self.$modalInstance.close({success:true});
            });
        }
    });

});