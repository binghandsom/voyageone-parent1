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
            this.updateModel = context.updateModel; // 是否是更新CmsFeedModel对象
            this.codeList = !context.codeList ? [] : context.codeList;
            this.context = context;
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
                    let platforms = res.data;
                    angular.forEach(platforms, function (cartOjb) {
                        _.extend(cartOjb, {cartId:parseInt(cartOjb.value), day:0,checked:true});
                    });
                    // 平台过滤
                    self.platforms = _.filter(platforms, cartObj => {
                        return cartObj.cartId != 0 && ((cartObj.cartId < 20 && cartObj.lang_id) == "en" || (cartObj.cartId >= 20 && cartObj.lang_id == "cn"));
                    })
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
                selAll:self.selAll,
                codeList:self.codeList,
                approveInfo:approveInfo,
                searchMap:self.context.searchMap
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