/**
 * @description 自定义属性管理
 * @author Piao
 */
define([
    'cms'
], function (cms) {

    cms.controller('attributeController', (function () {

        function CustomAttributeCtl($routeParams, $localStorage, popups, notify, alert, confirm, $location, cRoutes,
                                    systemCategoryService, attributeService2) {
            this.$routeParams = $routeParams;
            this.popups = popups;
            this.systemCategoryService = systemCategoryService;
            this.attributeService2 = attributeService2;
            this.channelInfo = angular.copy($localStorage.user);
            this.notify = notify;
            this.alert = alert;
            this.confirm = confirm;
            this.$location = $location;
            this.cRoutes = cRoutes;
            this.vm = {
                catPath: $routeParams.catPath || $routeParams.catPath == 0 ? '' : $routeParams.catPath
            };
        }

        CustomAttributeCtl.prototype.TYPE = {
            1: '共通属性',
            2: '主类目属性',
            3: '供货商属性',
            4: '自定义属性'
        };

        CustomAttributeCtl.prototype.ATTRIBUTETYPE = {
            1: '自动翻译',
            2: '匹配Master详情内属性',
            3: '固定值'
        };

        CustomAttributeCtl.prototype.init = function () {
            var self = this;

            self.attributeService2.init().then(function (res) {
                self.channelList = res.data.channelList;
                self.commonFields = res.data.commonFields;
            });

            self.search();
        };

        CustomAttributeCtl.prototype.clear = function () {
            var self = this;

            self.vm.catPath = '';
        };

        CustomAttributeCtl.prototype.search = function () {
            var self = this,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath;

            //liking旗舰店
            if(channelInfo.channel == 928)
                channelInfo.channel = '';

            self.attributeService2.search({
                orgChannelId: channelInfo.channel,
                cat: catPath
            }).then(function (res) {
                self.attributes = res.data.entitys;
                self.attributesTrue = _.filter(self.attributes, function (element) {
                    return element.checked
                });
                self.attributesFalse = _.filter(self.attributes, function (element) {
                    return !element.checked
                });
            });

        };

        CustomAttributeCtl.prototype.popCategoryMapping = function () {
            var self = this, popups = self.popups;

            self.systemCategoryService.getNewsCategoryList().then(function (res) {
                if (!res.data || !res.data.length) {
                    self.notify.danger("数据还未准备完毕");
                    return;
                }

                popups.popupCategoryNew({
                    categories: res.data,
                    from: self.vm.catPath == null ? "" : self.vm.catPath
                }).then(function (context) {
                    self.vm.catPath = context.selected.catPath;
                    self.search();
                });
            });
        };

        /**
         * 添加
         */
        CustomAttributeCtl.prototype.popAddAttribute = function () {
            var self = this, popups = self.popups,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath;

            popups.openAddAttribute({
                nameEnArr: _.pluck(self.attributes, 'nameEn'),
                orgChannelId: channelInfo.channel,
                cat: catPath,
                commonFields: self.commonFields
            }).then(function () {
                self.search();
            });
        };

        /**
         * 修改
         */
        CustomAttributeCtl.prototype.popEditAttribute = function (entity) {
            var self = this, popups = self.popups,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath;

            popups.openAddAttribute({
                entity: entity,
                orgChannelId: channelInfo.channel,
                cat: catPath,
                commonFields: self.commonFields
            }).then(function () {
                self.search();
            });
        };

        CustomAttributeCtl.prototype.delete = function (entity) {
            var self = this,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath;

            self.confirm('您确定要删除该属性？').then(function () {

                self.attributeService2.delete({
                    orgChannelId: channelInfo.channel,
                    cat: catPath,
                    entity: entity
                }).then(function () {
                    self.notify.success('删除成功！');
                    self.search();
                });
            });
        };

        /**
         * 单个点击checkbox操作
         * @param entity
         */
        CustomAttributeCtl.prototype.updateEntity = function (entity) {
            var self = this,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath;

            if (!entity.nameCn) {
                self.alert('该属性没有中文名称，请编辑中文属性！');
                entity.checked = !entity.checked;
                return;
            }

            self.attributeService2.doSetCustomshIsDispPlay({
                orgChannelId: channelInfo.channel,
                cat: catPath,
                entity: entity
            }).then(function () {
                self.notify.success('更新成功！');
                self.search();
            });
        };

        CustomAttributeCtl.prototype.moveKeys = {
            up: 'up',
            toTop: 'toTop',
            down: 'down',
            toDown: 'toDown'
        };

        CustomAttributeCtl.prototype.moveAttr = function (index, moveKey) {
            var self = this, moveKeys = self.moveKeys,
                channelInfo = self.channelInfo,
                catPath = self.vm.catPath,
                Attributes = self.attributesTrue, temp;

            switch (moveKey) {
                case moveKeys.up:
                    if (index === 0)
                        return;
                    temp = Attributes[index];
                    Attributes[index] = Attributes[index - 1];
                    Attributes[index - 1] = temp;
                    break;
                case moveKeys.toTop:
                    if (index === 0)
                        return;
                    temp = Attributes.splice(index, 1);
                    Attributes.splice(0, 0, temp[0]);
                    break;
                case moveKeys.down:
                    if (index === Attributes.length - 1)
                        return;
                    temp = Attributes[index];
                    Attributes[index] = Attributes[index + 1];
                    Attributes[index + 1] = temp;
                    break;
                case moveKeys.toDown:
                    if (index === Attributes.length - 1)
                        return;
                    temp = Attributes.splice(index, 1);
                    Attributes.push(temp[0]);
                    break;
            }

            //调用排序接口
            self.callSort({
                orgChannelId: channelInfo.channel,
                cat: catPath,
                sort: _.pluck(angular.copy(Attributes), 'nameEn')
            });
        };

        CustomAttributeCtl.prototype.callSort = function (upEntity) {
            var self = this;

            self.attributeService2.doSetSort(upEntity).then(function () {
                self.notify.success('排序完毕！');
                self.search();
            });
        };

        CustomAttributeCtl.prototype.linkValue = function (entity) {
            var self = this;

            sessionStorage.setItem(entity.nameEn, angular.toJson({
                catPath: self.vm.catPath,
                entity: entity,
                nameEnArr: _.pluck(self.attributes, 'nameEn')
            }));

            self.$location.path(self.cRoutes.marketing_setting_common_custom_translate_list.url + entity.nameEn);
        };

        CustomAttributeCtl.prototype.changeChannel = function(){
            var self = this;

            self.search();
        };

        return CustomAttributeCtl;

    })());

});
