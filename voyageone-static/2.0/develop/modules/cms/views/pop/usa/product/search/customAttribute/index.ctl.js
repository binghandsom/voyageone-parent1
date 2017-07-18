define([
    'cms'
],function (cms) {

    cms.controller('usaCustomAttributeController',class usaCustomAttributeController{

        constructor(advanceSearch){
            this.advanceSearch = advanceSearch;

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
               }
            });
        }

    });

});