/**
 * 拓展高级检索JS端controller
 */
define([
    'cms',
    'modules/cms/directives/platFormStatus.directive'
], function (cms) {

    cms.controller('adSearchAppendCtl', (function () {

        function AdSearchAppendCtl($scope, notify, confirm) {
            //高级检索中的scope
            this.parentScope = $scope.$parent;
            this.notify = notify;
            this.columnArrow = {};
            this.confirm = confirm;
        }

        AdSearchAppendCtl.prototype.columnOrder = function (columnName, cartId) {
            var self = this,
                column,
                columnArrow = self.columnArrow;

            if (cartId) {
                columnName = columnName.replace('✓', cartId);
            }

            _.forEach(columnArrow, function (value, key) {
                if (key != columnName)
                    columnArrow[key] = null;
            });

            column = columnArrow[columnName];

            if (!column) {
                column = {};
                column.mark = 'unsorted';
                column.count = 0;
            }

            column.count++;

            //偶数升序，奇数降序
            if (column.count % 2 === 0)
                column.mark = 'sort-up';
            else
                column.mark = 'sort-desc';

            columnArrow[columnName] = column;

            self.searchByOrder(columnName, column.mark);
        };

        AdSearchAppendCtl.prototype.searchByOrder = function (columnName, sortOneType) {
            var self = this,
                parentScope = self.parentScope,
                searchInfo = parentScope.vm.searchInfo;

            searchInfo.sortOneName = columnName;
            searchInfo.sortOneType = sortOneType == 'sort-up' ? '1' : '-1';

            if (parentScope.vm.currTab == 'product')
                parentScope.search();
            else
                parentScope.getGroupList();
        };

        AdSearchAppendCtl.prototype.getArrowName = function (columnName, cartId) {
            var self = this,
                columnArrow = self.columnArrow;

            if (cartId) {
                columnName = columnName.replace('✓', cartId);
            }

            if (!columnArrow || !columnArrow[columnName])
                return 'unsorted';

            return columnArrow[columnName].mark;
        };

        AdSearchAppendCtl.prototype.orderStep1 = function () {
            var self = this,
                parentScope = self.parentScope,
                searchInfo = parentScope.vm.searchInfo,
                column,
                notify = self.notify,
                columnArrow = self.columnArrow;

            if (!searchInfo.sortOneName) {
                searchInfo.sortOneName = 0;
                notify.warning("Warning： 请选择排序条件1");
                return;
            }

            _.forEach(columnArrow, function (value, key) {
                columnArrow[key] = null;
            });

            column = {};
            column.mark = searchInfo.sortOneType == 1 ? 'sort-up' : 'sort-desc';
            column.count = searchInfo.sortOneType == 1 ? 2 : 1;

            columnArrow[searchInfo.sortOneName] = column;
        };

        /**
         * 平台级锁定
         */
        AdSearchAppendCtl.prototype.platFormLock = function (cartId) {
            var self = this, parentScope = self.parentScope;

            parentScope._chkProductSel(parseInt(cartId), function (cartId, _selProdList) {

                self.confirm("是否同步商品下架？").then(function () {
                    console.log('所选产品', _selProdList);
                });

            });
        };

        return AdSearchAppendCtl;

    })());

});