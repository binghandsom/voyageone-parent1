define([
    'cms'
],function (cms) {

    cms.controller('usaCustomAttributeController',class usaCustomAttributeController{

        constructor(context,advanceSearch,$uibModalInstance,notify,$rootScope){
            this.context = context;
            this.$rootScope = $rootScope;
            this.advanceSearch = advanceSearch;
            this.$uibModalInstance = $uibModalInstance;
            this.notify = notify;

            this.customColumns = {
                commonProps:[],
                selCommonProps:[],
                platformAttributes:[],
                selPlatformAttributes:[],
                platformSales:[],
                selPlatformSales:[]
            };
            // Platform Sales
            this.saleInfo = {
                beginTime:"",
                endTime:"",
                selCartObj:{},
                selCarts:[]
            };

            this.init();
        }

        init() {
            let self = this;
            this.advanceSearch.getCustomColumns().then(res => {
               if (res.data) {
                   self.commonProps = res.data.commonProps == null ? [] : res.data.commonProps;
                   self.selCommonProps = res.data.selCommonProps == null ? [] : res.data.selCommonProps;
                   self.platformAttributes = res.data.platformAttributes == null ? [] : res.data.platformAttributes;
                   self.selPlatformAttributes = res.data.selPlatformAttributes == null ? [] : res.data.selPlatformAttributes;
                   self.platformSales = res.data.platformSales == null ? [] : res.data.platformSales;
                   self.selPlatformSales = res.data.selPlatformSales == null ? [] : res.data.selPlatformSales;

                   // 处理勾选
                   _.each(self.commonProps, item => {
                       let selOne = _.find(self.selCommonProps, selItem => {
                           return item.propId === selItem;
                       });
                       if (!!selOne) {
                           _.extend(item, {checked:true});
                       }
                   });
                   _.each(self.platformAttributes, item => {
                       let selOne = _.find(self.selPlatformAttributes, selItem => {
                           let flag = item.cartId == selItem.cartId && item.value == selItem.value;
                           if (!flag) {
                               let index = item.value.indexOf(selItem.value);
                               if (index != -1) {
                                   flag = true;
                               }
                           }
                           return flag;
                       });
                       if (selOne) {
                           _.extend(item, {checked:true});
                       }
                   });

                   _.each(self.platformSales, platformSale => {
                        _.extend(platformSale, {value: parseInt(platformSale.cartId)});
                   });

                   if (_.size(self.selPlatformSales) > 0) {
                       let platformSaleOne = self.selPlatformSales[0];
                       self.saleInfo.beginTime = platformSaleOne.beginTime;
                       self.saleInfo.endTime = platformSaleOne.endTime;

                       // 处理已勾选的平台
                       _.each(self.selPlatformSales, platformSale => {
                           self.saleInfo.selCartObj[platformSale.cartId] = true;
                           self.saleInfo.selCarts.push(platformSale.cartId);
                       });
                   }

               }
            });
        }

        // 勾选或取消勾选Platform sale 平台
        selSaleCart() {
            let self = this;
            let selCartObj = _.pick(self.saleInfo.selCartObj, function (value, key, object) {
                return value;
            });
            self.saleInfo.selCarts = _.keys(selCartObj);
        }

        // 保存
        save() {
            // 处理Common Attributes
            let self = this;

            let selCommonProps = [];
            _.each(self.commonProps, item => {
                if (item.checked) {
                    selCommonProps.push(item.propId);
                }
            });
            // 处理Platform Attributes
            let selPlatformAttributes = {};
            _.each(self.platformAttributes, item => {
                if (item.checked) {
                    let cartId = item.cartId;
                    let cartVal = item.value;
                    let currCartAttrs = selPlatformAttributes[cartId];
                    if (selPlatformAttributes[cartId]) {
                        selPlatformAttributes[cartId] = currCartAttrs.concat(cartVal.split(","));
                    } else {
                        selPlatformAttributes[cartId] = cartVal.split(",");
                    }
                }
            });
            // 处理Platform Sales
            let selPlatformSales =[];
            if (self.saleInfo.beginTime && self.saleInfo.endTime && _.size(self.saleInfo.selCarts) > 0) {
                _.each(self.saleInfo.selCarts, selCart => {
                    let tempPlatSale = {
                        cartId:selCart,
                        beginTime:self.saleInfo.beginTime,
                        endTime:self.saleInfo.endTime
                    };
                    selPlatformSales.push(tempPlatSale);
                });
            }

            //去除遮罩
            self.advanceSearch.saveCustomColumns({
                selCommonProps:selCommonProps,
                selPlatformAttributes:selPlatformAttributes,
                selPlatformSales:selPlatformSales
            },{
                autoBlock: false
            }).then(res => {
                //self.context.customAttributeResult = res.data;
                self.$rootScope.customAttributeResult = res.data;
                self.notify.success("save success");
            });

            self.$uibModalInstance.close(self.context.customAttributeResult);
        }

    });

});