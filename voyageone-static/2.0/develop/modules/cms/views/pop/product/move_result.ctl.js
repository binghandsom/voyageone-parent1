/**
 * Created by sofia on 2016/10/17.
 */
define([
    'cms'
], function (cms) {

    cms.controller("MoveResultController", (function () {

        function MoveResult(context, $uibModalInstance) {
            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
            this.resultType = this.context == 'Sku' ? 'Sku' : 'Code';
            this.resultBtn = this.context == 'Sku' ? '迁移到新Code产品详情页' : '返回到移动Code的产品详情页';
        }

        MoveResult.prototype.ok = function () {
            this.$uibModalInstance.close();
        };

        return MoveResult;
    })());
});