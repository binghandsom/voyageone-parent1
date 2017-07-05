/**
 * @description 首页

 */
define([
    'cms'
], function (cms) {

    cms.controller('datachartController', class DatachartController {

        constructor($menuService) {
            this.vm = {};
            this.$menuService = $menuService;
        }

        init() {
            let self = this;

            self.$menuService.getHomeSumData().then(function (res) {
                self.vm.sumData = res.data;
            });
        }

        testClick() {
            let self = this;

            console.log('self',self.vm);
        }
    });

});
