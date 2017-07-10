define([
    'cms'
], function (cms) {

    cms.controller('feedSearchController', class FeedSearchController {

        constructor($rootScope, popups, itemDetailService) {
            let self = this;

            self.$rootScope = $rootScope;
            console.log('auth',self.$rootScope.auth);
            self.popups = popups;
            self.feedListTotal = 0;
            self.paraMap = {
                status: "",
                isApprove: null
            };
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
            let self = this;
            self.paraMap = {};
            self.status = [false, false, false];
            self.isApprove = [false, false];
            self.paging = {curr: 1, total: 0};

        }

        popBatchApprove() {
            let self = this;

            self.popups.openBatchApprove();
        }

    });

});