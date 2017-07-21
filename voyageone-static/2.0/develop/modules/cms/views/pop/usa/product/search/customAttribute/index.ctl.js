define([
    'cms'
],function (cms) {

    cms.controller('usaCustomAttributeController',class usaCustomAttributeController{

        constructor(advanceSearch,$modalInstance,notify){
            this.advanceSearch = advanceSearch;
            this.$modalInstance = $modalInstance;
            this.notify = notify;

            this.customColumns = {
                commonProps:[],
                selCommonProps:[],
                platformAttributes:[],
                selPlatformAttributes:[],
                platformSales:[],
                selPlatformSales:[]
            };

            this.init();
        }

        init() {
            let self = this;
            this.advanceSearch.getCustomColumns().then(res => {
               if (res.data) {
                   console.log(res.data);
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
                           return item.value == selItem;
                       });
                       if (selOne) {
                           _.extend(item, {checked:true});
                       }
                   });
                   _.each(self.platformSales, item => {
                       let selOne = _.find(self.selPlatformSales, selItem => {
                           return item.cartId == selItem.cartId;
                       });
                       if (selOne) {
                           _.extend(item, {checked:true,beginTime:selOne.beginTime,endTime:selOne.endTime});
                       }
                   });
               }
            });
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
                    if (selPlatformAttributes[cartId]) {
                        selPlatformAttributes[cartId].concat(cartVal.split(","));
                    } else {
                        selPlatformAttributes[cartId] = cartVal.split(",");
                    }
                }
            });
            // 处理Platform Sales
            let selPlatformSales =[];
            _.each(self.platformSales, item => {
                if (item.checked) {
                    let map = {cartId:item.cartId,beginTime:item.beginTime,endTime:item.endTime};
                    selPlatformSales.push(map);
                }
            });

            let params = {
                selCommonProps:selCommonProps,
                selPlatformAttributes:selPlatformAttributes,
                selPlatformSales:selPlatformSales
            };
            self.advanceSearch.saveCustomColumns(params).then(res => {
                self.$modalInstance.close(res.data);
                self.notify.success("save success");
            });
        }

    });

});