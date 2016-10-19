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

    JmSortProductPopupController.prototype.sortFields = {
        stock: 'jmPromotionProduct.quantity',
        xiaoliang: 3,
        price: 'jmPromotionProduct.dealPrice'
    };

    JmSortProductPopupController.prototype.loadProduct = function loadProduct() {
        var self = this,
            spDataService = self.spDataService;

        spDataService.getPromotionProducts(self.tagId).then(function (productList) {
            self.productList = productList.map(function (item, index) {
                item.defaultSort = index;
                return item;
            });
        });
    };

    JmSortProductPopupController.prototype.getSortName = function (field) {
        var name = this['sortName:' + field];
        return name ? ('-' + name) : name;
    };

    JmSortProductPopupController.prototype.sortBy = function (field) {
        var self = this,
            sortFunction, sortName,
            sortKey = 'sortBy:' + field,
            sort = self[sortKey];

        switch (sort) {
            case true:
                sort = false;
                sortName = "desc";
                sortFunction = function (a, b) {
                    return eval('b.' + field + ' - ' + 'a.' + field);
                };
                break;
            case false:
                sort = null;
                sortName = "";
                sortFunction = function (a, b) {
                    return a.defaultSort - b.defaultSort;
                };
                break;
            case null:
            default:
                sort = true;
                sortName = "asc";
                sortFunction = function (a, b) {
                    return eval('a.' + field + ' - ' + 'b.' + field);
                };
                break;
        }

        _.each(self.sortFields, function (v) {
            if (v === field)
                return;
            self['sortName:' + v] = '';
            self['sortBy:' + v] = null;
        });
        self[sortKey] = sort;
        self['sortName:' + field] = sortName;
        self.productList.sort(sortFunction);
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