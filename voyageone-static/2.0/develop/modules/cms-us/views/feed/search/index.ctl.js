define([
    'cms'
], function (cms) {

    cms.controller('feedSearchController', class FeedSearchController {

        constructor($rootScope, popups, itemDetailService, alert) {
            let self = this;

            self.$rootScope = $rootScope;
            self.alert = alert;
            self.popups = popups;
            self.feedListTotal = 0;
            self.paraMap = {
                status: "",
                isApprove: null
            };
            self.columnArrow = {};
            self.status = [false, false, false];
            self.isApprove = [false, false];
            self.itemDetailService = itemDetailService;
            self.paging = {
                curr: 1, total: 0, fetch: function () {
                    self.getList();
                }
            };

        }

        getList() {
            let self = this;

            if (self.status[0] === true) {
                self.paraMap.status += "new_";
            }
            if (self.status[1] === true) {
                self.paraMap.status += "pending_";
            }
            if (self.status[2] === true) {
                self.paraMap.status += "ready_";
            }
            if (self.isApprove[0] === true && self.isApprove[1] == false) {
                self.paraMap.isApprove = "Yes";
            }
            if (self.isApprove[1] === true && self.isApprove[0] === false) {
                self.paraMap.isApprove = "No";
            }

            self.itemDetailService.list(_.extend(self.paraMap, self.paging)).then(resp => {
                self.feeds = resp.data.feedList;
                self.feedListTotal = resp.data.feedListTotal;
                self.paging.total = resp.data.feedListTotal;
            });

        }

        clear() {
            let self = this,
                columnArrow = self.columnArrow;

            self.paraMap = {};
            self.status = [false, false, false];
            self.isApprove = [false, false];
            self.paging = {curr: 1, total: 0};

            if (columnArrow) {
                _.forEach(columnArrow, function (value, key) {
                    columnArrow[key] = null;
                });
            }

        }

        popBatchApprove() {
            let self = this,
                codeList;

            codeList = _.chain(self.feeds).filter(item => {
                return item.check;
            }).pluck('code').value();

            if (codeList.length === 0) {
                self.alert('Please select at least one！');
                return false;
            }

            self.popups.openBatchApprove({
                sel_all:self.totalItems,
                codeList:codeList
            });
        }

        selectAll() {
            let self = this;

            _.each(self.feeds, feed => {
                feed.check = self.isAll;
            });
        }

        columnOrder(columnName) {
            let self = this, column,
                columnArrow = self.columnArrow;


            _.forEach(columnArrow, function (value, key) {
                if (key != columnName)
                    columnArrow[key] = null;
            });

            column = columnArrow[columnName];

            if (!column) {
                column = {};
                column.mark = 'unsorted';
                column.count = null;
            }

            column.count = !column.count;

            //偶数升序，奇数降序
            if (column.count)
                column.mark = 'sort-desc';
            else
                column.mark = 'sort-up';

            columnArrow[columnName] = column;

            self.searchByOrder(columnName, column.mark);
        };

        getArrowName(columnName) {
            let self = this,
                columnArrow = self.columnArrow;

            if (!columnArrow || !columnArrow[columnName])
                return 'unsorted';

            return columnArrow[columnName].mark;
        };

        searchByOrder(columnName, sortOneType) {
            let self = this,
                paraMap = self.paraMap;

            paraMap.sortName = columnName;
            paraMap.sortType = sortOneType == 'sort-up' ? '1' : '-1';

            self.getList();

        }

    });

});