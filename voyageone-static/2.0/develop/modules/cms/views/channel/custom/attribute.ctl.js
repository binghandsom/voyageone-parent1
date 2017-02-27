/**
 * @description 自定义属性管理
 * @author Piao
 */
define([
    'cms'
], function (cms) {

    cms.controller('attributeController', (function () {

        function CustomAttributeCtl($routeParams, $localStorage, popups, attributeService, attributeService2, notify, confirm) {
            this.$routeParams = $routeParams;
            this.popups = popups;
            this.attributeService = attributeService;
            this.attributeService2 = attributeService2;
            this.channelInfo = $localStorage.user;
            this.notify = notify;
            this.confirm = confirm;
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

            popups.openAddAttribute({
                type: 'add'
            });
        };

        CustomAttributeCtl.prototype.delete = function () {
            var self = this;

            self.confirm('您确定要删除该属性？').then(function () {
                alert('delete');
                /*                self.attributeService2({

                 }).then(function(){
                 self.notify('删除成功！');
                 });*/
            });
        };

        /**
         * 单个点击checkbox操作
         * @param entity
         */
        CustomAttributeCtl.prototype.updateEntity = function (entity) {
            var self = this,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath || self.vm.catPath == 0 ? '':self.vm.catPath;

            self.attributeService2.doSetCustomshIsDispPlay({
                orgChannelId: channelInfo.channel,
                cat: catPath,
                entity: entity
            }).then(function (res) {
                console.log(res);
                self.notify.success('更新成功！');
            });
        };

        CustomAttributeCtl.prototype.sort = function (entity) {
            var self = this,
                sortArr = [];

            sortArr = self.en

            self.attributeService2.doSetSort({}).then(function (res) {
            });
        };

        return CustomAttributeCtl;

    })());

});
