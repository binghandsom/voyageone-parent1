define([
    'cms'
], function (cms) {

    cms.controller('loadAttributeController', (function () {

        function loadAttributeCtl(context, $uibModalInstance) {
            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
        }

        loadAttributeCtl.prototype.init = function () {
            console.log('部分属性上传！');
        };

        loadAttributeCtl.prototype.ok = function () {
            this.$uibModalInstance.close('young king yong boss!');
        };

        return loadAttributeCtl;
    })());

});