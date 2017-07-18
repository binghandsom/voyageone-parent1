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
            let self = this;
            let commonProps = _.filter(self.commonProps, item => {
                return item.checked;
            });
            let platformAttributes = _.filter(self.platformAttributes, item => {
                return item.checked;
            });
            let platformSales = _.filter(self.platformSales, item => {
                return item.checked;
            });

            let selCommonProps = [];
            _.each(commonProps, item => {
                selCommonProps.push(item.propId);
            });
            let selPlatformAttributes = [];
            _.each(platformAttributes, item => {
               selPlatformAttributes.push(item.value);
            });
            let selPlatformSales =[];
            _.each(platformSales, item => {
               let map = {cartId:item.cartId,beginTime:item.beginTime,endTime:item.endTime};
               selPlatformSales.push(map);
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