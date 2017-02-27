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

    function AttrEntity(nameEn, nameCn, type, checked) {
        if (!arguments || arguments.length < 4)
            throw new Error('没有填写正确参数！');
        this.nameEn = nameEn;
        this.nameCn = nameCn;
        this.type = type;
        this.checked = checked;
    }

    cms.controller('popAddAttributeValueCtl', function ($scope, $modalInstance, context, attributeService2, alert) {

        $scope.vm = {
            prop_original: "",
            prop_translation: "",
            type: context.type
        };

        $scope.changeType = function () {
            var vm = $scope.vm;

            if (vm.attrType > 1)
                vm.prop_master_value = vm.prop_fix_value = null;
        };

        /**
         * 提交属性追加
         * $modalInstance.close($scope.vm);
         */
        $scope.ok = function () {

            var entity = new AttrEntity();

            attributeService2.doUpdateEntity({}).then(function () {
                alert('添加成功！');
                $modalInstance.dismiss();
            });
        };
    });


});