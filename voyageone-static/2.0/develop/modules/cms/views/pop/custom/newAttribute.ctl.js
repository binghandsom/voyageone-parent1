/**
 * Created by linanbin on 15/12/7.
 *
 * @description 添加自定义属性模态框
 * @author by Piao
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function AttrEntity(nameEn, nameCn, attributeType, value) {
        if (!arguments || arguments.length < 4)
            throw new Error('没有填写正确参数！');
        this.nameEn = nameEn;
        this.nameCn = nameCn;
        this.type = 4;
        this.attributeType = attributeType;
        this.value = value;
    }

    cms.controller('popAddAttributeValueCtl', (function () {

        function AttributeValueCtl($modalInstance, context, attributeService2, notify) {
            this.$modalInstance = $modalInstance;
            this.context = context;
            this.attributeService2 = attributeService2;
            this.notify = notify;
            this.vm = {
                prop_original: "",
                prop_translation: "",
                attrType: '',
                type: 4
            };
        }

        AttributeValueCtl.prototype.init = function () {
            var self = this, context = self.context;

            if (context.entity) {
                self.vm.prop_original = context.entity.nameEn;
                self.vm.prop_translation = context.entity.nameCn;
                self.vm.attrType = context.entity.attributeType;
                self.vm.type = context.entity.type;

                if (context.entity.type == 2)
                    self.vm.prop_master_value = context.entity.value;
                else
                    self.vm.prop_fix_value = context.entity.value;
            }

            //匹配Master详情内属性
            self.masterFields = context.commonFields;

        };

        AttributeValueCtl.prototype.changeType = function () {
            var vm = this.vm;

            if (vm.attrType > 1)
                vm.prop_master_value = vm.prop_fix_value = null;
        };

        /**
         * 提交属性追加
         * $modalInstance.close($scope.vm);
         */
        AttributeValueCtl.prototype.ok = function () {
            var self = this, entity,
                vm = self.vm, attrValue;

            switch (Number(vm.attrType)) {
                case 1:
                    attrValue = '';
                    break;
                case 2:
                    attrValue = vm.prop_master_value;
                    break;
                case 3:
                    attrValue = vm.prop_fix_value;
                    break;
            }

            if (vm.type === 'edit') {
                entity = angular.copy(self.context.entity);
                entity.nameCn = vm.prop_translation;
                entity.value = attrValue;
            } else {
                entity = new AttrEntity(vm.prop_original, vm.prop_translation, Number(vm.attrType), attrValue)
            }

            self.attributeService2.doUpdateEntity({
                orgChannelId: self.context.orgChannelId,
                cat: self.context.cat,
                entity: entity
            }).then(function () {
                self.notify.success('添加成功！');
                self.$modalInstance.close();
            });
        };

        return AttributeValueCtl;

    })());

});