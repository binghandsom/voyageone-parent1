/**
 * @description 首页

 */
define([
    'cms'
], function (cms) {

    cms.controller('datachartController', class DatachartController {

        constructor($menuService,alert) {
            this.vm = {};
            this.$menuService = $menuService;
            this.alert = alert;
        }

        init() {
            let self = this;

            self.$menuService.getHomeSumData().then(function (res) {
                self.vm.sumData = res.data;
            });
        }

        goLink() {
            let self = this;

            self.alert('去哪呀？');
        }
    });

});
