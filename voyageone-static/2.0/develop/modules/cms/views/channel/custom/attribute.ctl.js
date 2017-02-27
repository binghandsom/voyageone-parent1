/**
 * @description 自定义属性管理
 * @author Piao
 */
define([
    'cms'
], function (cms) {

    cms.controller('attributeController', (function () {

        function CustomAttributeCtl($routeParams, $localStorage, popups, attributeService,attributeService2, notify) {
            this.$routeParams = $routeParams;
            this.popups = popups;
            this.attributeService = attributeService;
            this.attributeService2 = attributeService2;
            this.channelInfo = $localStorage.user;
            this.notify = notify;
            this.vm = {
                catPath: $routeParams.catPath
            };
        }

        CustomAttributeCtl.prototype.init = function () {
            var self = this,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath ? self.vm.catPath : '';

            self.attributeService2.search({
                orgChannelId: channelInfo.channel,
                cat: catPath
            }).then(function (res) {
                self.Attributes = res.data.entitys;
            });

        };

        CustomAttributeCtl.prototype.popCategoryMapping = function () {
            var self = this, popups = self.popups;

            self.attributeService.getCatTree().then(function (res) {
                if (!res.data.categoryTree || !res.data.categoryTree.length) {
                    self.notify.danger("数据还未准备完毕");
                    return;
                }

                popups.popupNewCategory({
                    categories: res.data.categoryTree,
                    from: self.vm.catPath == null ? "" : self.vm.catPath
                }).then(function (context) {


                });
            });
        };

        CustomAttributeCtl.prototype.popAddAttribute = function () {
            var self = this, popups = self.popups;

            popups.openAddAttribute();
        };

        return CustomAttributeCtl;

    })());

});
