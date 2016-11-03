/**
 * 拓展高级检索JS端controller
 */
define([
    'cms'
], function (cms) {

    cms.controller('adSearchAppendCtl', (function () {

        function AdSearchAppendCtl($scope, $translate) {
            this.parentScope = $scope.$parent;
            this.$translate = $translate;
            this.columnArrow = {};
        }

        AdSearchAppendCtl.prototype.columnOrder = function (columnName) {
            var self = this,
                $translate = self.$translate,
                columnArrow = self.columnArrow;

            columnName = $translate.instant(columnName);

            var column = columnArrow[columnName];

            if(!column){
                column = {};
                column.mark = 'unsorted';
                column.count = 0;
            }

            column.count++;
            if (column.count % 2 === 0)
                column.mark = 'sort-up';
            else
                column.mark = 'sort-desc';

            columnArrow[columnName] = column;

        };

        AdSearchAppendCtl.prototype.getArrowName = function(columnName){
            var self = this,
                $translate = self.$translate,
                name = $translate.instant(columnName),
                columnArrow = self.columnArrow;

            if(!columnArrow || !columnArrow[name])
                return 'unsorted';

            return columnArrow[name].mark;
        };

        return AdSearchAppendCtl;

    })());

});