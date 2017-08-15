/**
 * @description feed search page
 */
define([
    'cms'
], function (cms) {

    cms.controller('feedSearchController', class FeedSearchController {

        constructor(popups, itemDetailService, alert,$sessionStorage,notify,confirm) {
            let self = this;

            self.$sessionStorage = $sessionStorage;
            self.notify = notify;
            self.alert = alert;
            self.confirm = confirm;
            self.popups = popups;
            self.feedListTotal = 0;
            self.paraMap = {
                status: [],
                approvePricing: [],
                sortName:"created",
                sortType:"-1"

            };
            self.updateMap = {};
            self.totalItems = false;
            //权限控制,默认为最低权限1:new,2:Pending,3:Ready

            self.flag = self.$sessionStorage.auth.selfAuth;
            self.columnArrow = {};
            self.status = [false, false, false];
            self.approvePricing = [false, false];
            self.itemDetailService = itemDetailService;
            self.paging = {
                curr: 1,size:10, total: 0, fetch: function () {
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
        init(){
            let self = this;
            self.columnArrow={"created":{"count":true,"mark":"sort-desc"}};
            self.getList();
        }

        getListSearch(){
            let self = this;
            self.isAll = false;
            angular.forEach(self.feeds, function (feed) {
                feed.check = false;
            });
            self.totalItems = false;
            self.paging.curr=1;
            self.getList();
        }

        /**
         * 获取检索列表
         */
        getList() {
            let self = this,
                _upEntity = angular.copy(self.paraMap);

            _upEntity.status = [];
            _upEntity.approvePricing = [];
            if (self.status[0] === true) {
                _upEntity.status.push("New");
            }
            if (self.status[1] === true) {
                _upEntity.status.push("Pending");
            }
            if (self.status[2] === true) {
                _upEntity.status.push("Ready");
            }
            //根据当前页面权限设置默认状态值
            if (_upEntity.status.length == 0) {
                //状态值为空
                if (self.flag == 1) {
                    //new 权限
                    _upEntity.status.push("New");
                }
                if (self.flag == 2) {
                    //Pending 权限
                    _upEntity.status.push("New");
                    _upEntity.status.push("Pending");
                }
                if (self.flag == 3) {
                    //Ready 权限
                    _upEntity.status.push("New");
                    _upEntity.status.push("Pending");
                    _upEntity.status.push("Ready");
                }
            }

            if (self.approvePricing[0] === true)
            {
                _upEntity.approvePricing.push("1");
            }
            if (self.approvePricing[1] === true)
            {
                _upEntity.approvePricing.push("0");
            }
            if(_upEntity.lastReceivedOnStart != null){
                _upEntity.lastReceivedOnStart += " 00:00:00";
            }
            if(_upEntity.lastReceivedOnEnd != null){
                _upEntity.lastReceivedOnEnd += " 23:59:59";
            }
            if(_upEntity.createdStart != null){
                _upEntity.createdStart += " 00:00:00";
            }
            if(_upEntity.createdEnd != null){
                _upEntity.createdEnd += " 23:59:59";
            }

            self.itemDetailService.list(_.extend(_upEntity, self.paging)).then(resp => {
                self.feeds = resp.data.feedList;
                self.feedListTotal = resp.data.feedListTotal;
                self.paging.total = resp.data.feedListTotal;
            });

        }

        updateOne(feed, key, value) {
            let self = this,
                requestMap = {};

            if(value == null || value == ''){
                self.alert("value can not be  empty or 0!!!");
                feed.editMsrp = feed.editRetai = false;
                return;
            }

            requestMap.code = feed.code;
            requestMap[key] = value + '';

            self.itemDetailService.updateOne(requestMap).then(() => {
                self.notify.success('update success!');

                feed.editMsrp = false;
                feed.editRetai = false;
            });

        }

        // 修改价格, 对应feed.usPlatforms.P8.XX
        updatePrice(feed, key, value, cartId) {
            let self = this,
                requestMap = {};

            if (!value) {
                value = 0;
            }
            /*if(value == null || value <= 0){
                self.alert("Price must be greater than 0!");
                feed.editMsrp = feed.editRetai = false;
                return;
            }*/

            requestMap.code = feed.code;
            requestMap["cartId"] = cartId;
            requestMap[key] = value + '';

            self.itemDetailService.updateOne(requestMap).then(() => {
                self.notify.success('update success!');
                feed.usPlatforms.P8[key] = value;

                feed.editMsrp = false;
                feed.editRetai = false;
            });
        }

        clear() {
            let self = this,
                columnArrow = self.columnArrow;
            self.paraMap = {};
            self.paraMap = {
                status: [],
                approvePricing: [],
                sortName:"created",
                sortType:"-1"
            };
            self.status = [true, false, false];
            self.approvePricing = [false, false];
            self.paging.curr = 1;
           // self.paging.total = 0;

            if (columnArrow) {
                _.forEach(columnArrow, function (value, key) {
                    if(key == "created"){
                        columnArrow["created"] = {'count':true,'mark':'sort-desc'};
                    }else{
                        columnArrow[key] = null;
                    }
                });
            }
        }

        popBatchApprove() {
            let self = this,
                codeList = [];

            codeList = _.chain(self.feeds).filter(item => {
                return item.check;
            }).pluck('code').value();

            if (codeList.length === 0 && self.totalItems == false) {
                self.alert('Please select at least one！');
                return false;
            }

            self.popups.openBatchApprove({
                selAll:self.totalItems,
                codeList:codeList,
                searchMap: self.paraMap
            }).then((res) =>{
                if (res.success) {
                    self.isAll = false;
                    angular.forEach(self.feeds, function (feed) {
                        feed.check = false;
                    })
                    self.totalItems = false;
                    self.getList();
                }
            });
        }

        // 批量Approve Pricing
        batchApprovePricing() {
            let self = this,
                codeList = [];

            codeList = _.chain(self.feeds).filter(item => {
                return item.check;
            }).pluck('code').value();

            if (codeList.length === 0 && self.totalItems == false) {
                self.alert('Please select at least one！');
                return false;
            }

            self.confirm("Make sure of approving pricing of the selected products").then(confirmed => {
                self.itemDetailService.bulkApprovePricing(
                    {
                        selAll:self.totalItems,
                        codeList:codeList,
                        searchMap: self.paraMap
                    }
                ).then(res => {
                    self.isAll = false;
                    angular.forEach(self.feeds, function (feed) {
                        feed.check = false;
                    })
                    self.totalItems = false;
                    self.getList();
                });
            });
        }

        popBatchApproveOne(code) {
            let self = this,
                codeList = [code];

            self.popups.openBatchApprove({
                selAll:false,
                codeList:codeList
                //searchMap: self.paraMap
            }).then((res) =>{
                if (res.success) {
                    self.isAll = false;
                    angular.forEach(self.feeds, function (feed) {
                        feed.check = false;
                    })
                    self.totalItems = false;
                    self.getList();
                }
            });
        }

        selectAll() {
            let self = this;

            _.each(self.feeds, feed => {
                if(feed.status != 'New')
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

        canApprovePrice(feed){
            let self = this;
            //进行校验,no到yes
            if(!feed.approvePricing || feed.approvePricing != "1"){

                if(!feed.usPlatforms || !feed.usPlatforms.P8 || feed.usPlatforms.P8.priceClientMsrp <= 0
                   || !feed.usPlatforms.P8.priceClientRetail || feed.usPlatforms.P8.priceClientRetail <= 0){
                    //价格为0时不能修改
                    self.alert("Msrp($) or price($) is 0, can not change!!!");
                    return;
                }

                feed.approvePricing = "1";
                self.updateOne(feed,'approvePricing', feed.approvePricing);
            }else{
                feed.approvePricing = "0";
                self.updateOne(feed,'approvePricing', feed.approvePricing);
            }
        }
    });

});