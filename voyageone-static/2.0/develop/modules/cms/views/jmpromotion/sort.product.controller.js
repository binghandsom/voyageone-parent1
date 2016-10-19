define(['cms'], function (cms) {
    function JmSortProductPopupController(spDataService, $scope, $element, notify) {
        this.destroy = function () {
            $element.remove();
            $scope.$destroy();
        };
        this.tagId = $scope.tagId;
        this.spDataService = spDataService;
        this.notify = notify;
        this.loadProduct();
    }

    JmSortProductPopupController.prototype.loadProduct = function loadProduct() {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotionProducts(self.tagId).then(function (productList) {
            self.productList = productList;
        });
    };

    JmSortProductPopupController.prototype.remove = function (index) {
        this.productList.splice(index, 1);
    };

    JmSortProductPopupController.prototype.save = function () {
        var self = this,
            spDataService = self.spDataService,
            notify = self.notify,
            destroy = self.destroy;

        spDataService.saveProductSort(self.tagId, self.productList).then(function () {
            notify.success('保存成功');
            destroy();
        });
    };

    cms.controller('JmSortProductPopupController', ['spDataService', '$scope', '$element', 'notify', JmSortProductPopupController]);
});