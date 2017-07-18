define([
    'cms'
],function (cms) {

    cms.controller('usaCustomAttributeController',class usaCustomAttributeController{

        constructor(advanceSearch){
            this.advanceSearch = advanceSearch;
            this.init();
        }

        init() {
            let self = this;
            this.advanceSearch.getCustomColumns().then(res => {
               if (res.data) {
                   console.log(res.data);
               }
            });
        }

    });

});