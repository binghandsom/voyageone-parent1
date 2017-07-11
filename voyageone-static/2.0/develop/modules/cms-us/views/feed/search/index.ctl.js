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
                approvePricing: null
            };
            self.updateMap = {};
            //权限控制,默认为最低权限1:new,2:Pending,3:Ready

            self.flag = self.$rootScope.auth.selfAuth;
            self.columnArrow = {};
            self.status = [false, false, false];
            self.approvePricing = [false, false];
            self.itemDetailService = itemDetailService;
            self.paging = {
                curr: 1, total: 0, fetch: function () {
                    self.getList();
                }
            };

            //设置状态的默认选中
            if (self.flag == 1) {
                self.status[0] = true;
            }
            if (self.flag == 2) {
                self.status[1] = true;
            }
            if (self.flag == 3) {
                self.status[2] = true;
            }


        }

        getList() {

            let self = this;
            self.paraMap.status = '';

            if (self.status[0] === true) {
                self.paraMap.status += "New_";
            }
            if (self.status[1] === true) {
                self.paraMap.status += "Pending_";
            }
            if (self.status[2] === true) {
                self.paraMap.status += "Ready_";
            }
            if (self.approvePricing[0] === true && self.approvePricing[1] == false) {
                self.paraMap.approvePricing = "1";
            }
            if (self.approvePricing[1] === true && self.approvePricing[0] === false) {
                self.paraMap.approvePricing = "0";
            }

            self.itemDetailService.list(_.extend(self.paraMap, self.paging)).then(resp => {
                self.feeds = resp.data.feedList;
                self.feedListTotal = resp.data.feedListTotal;
                self.paging.total = resp.data.feedListTotal;
                //对页面显示的价格区间进行转化
                if (self.feeds != null) {
                    angular.forEach(self.feeds, function (feed) {
                        var skus = feed.skus;
                        var arr1 = [];
                        var arr2 = [];
                        angular.forEach(skus, function (value) {
                            arr1.push(value.priceMsrp);
                            arr2.push(value.priceNet);
                        })
                        arr1.sort();
                        arr2.sort();
                        feed.priceMsrps = [arr1[0], arr1[arr1.length - 1]];
                        feed.priceNets = [arr2[0], arr2[arr2.length - 1]];


                    })
                }

            });

        }

        updateOne(feed, key, value) {
            let self = this;
            let requestMap = {};
            requestMap.code = feed.code;
            requestMap[key] = value;
            //requestMap.value = value;
            self.itemDetailService.updateOne(requestMap).then(resp => {
                feed.editMsrp = false;
                feed.editRetai = false;
            });
        }

        clear() {
            let self = this,
                columnArrow = self.columnArrow;

            self.paraMap = {};
            self.paraMap = {
                status: "",
                approvePricing: null
            };
            self.status = [false, false, false];
            self.isApprove = [false, false];
            self.paging.curr = 1;
            self.paging.total = 0;

            if (columnArrow) {
                _.forEach(columnArrow, function (value, key) {
                    columnArrow[key] = null;
                });
            }

        }

        popBatchApprove() {
            let self = this,
                codeList = [];

            codeList = _.chain(self.feeds).filter(item => {
                return item.check;
            }).pluck('code').value();

            if (codeList.length === 0) {
                self.alert('Please select at least one！');
                return false;
            }

            self.popups.openBatchApprove({
                sel_all: self.totalItems,
                codeList: codeList
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

        searchByOrder(columnName, sortType) {
            let self = this,
                paraMap = self.paraMap;

            paraMap.sortName = self.getFinalColumn(columnName, sortType);
            console.log('paraMap.sortName',paraMap.sortName);
            paraMap.sortType = sortType == 'sort-up' ? '1' : '-1';

            self.getList();

        }

        getFinalColumn(columnName, sortType) {
            if (/^priceClient*/.test(columnName)) {
                return sortType == 'sort-up' ? `${columnName}Min` : `${columnName}Max`;
            } else {
                return columnName;
            }
        }

    });

});