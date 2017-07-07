define([
    'cms'
], function (cms) {

    cms.controller('newItemController', class newItemController {

        constructor(popups, itemDetailService) {
            this.popups = popups;
            this.feeds = {};
            this.feedListTotal = 0;
            this.paraMap = {};
            this.paraMap.status = "";
            this.paraMap.isApprove = null;
            this.status=[false,false,false];
            this.isApprove=[false,false];
            this.paraMap.pageNum = 1;
            this.paraMap.pageSize = 10;
            this.itemDetailService = itemDetailService;
            this.init();


        }
        init() {
            let self = this;
        }
        getList(){
            let self = this;
            if(self.status[0] == true){
                self.paraMap.status += "new_";
            }
            if(self.status[1] == true){
                self.paraMap.status += "pending_";
            }
            if(self.status[2] == true){
                self.paraMap.status += "ready_";
            }
            if(self.isApprove[0] == true && self.isApprove[1] == false){
                self.paraMap.isApprove ="Yes";
            }
            if(self.isApprove[1] == true && self.isApprove[0] == false){
                self.paraMap.isApprove ="No";
            }
            self.itemDetailService.list(self.paraMap).then(resp => {
                self.feeds = resp.data.feedList;
                self.feedListTotal = resp.data.feedListTotal;

            });

        }

        clear(){
            let self = this;
           self.paraMap = {};
            self.status=[false,false,false];
            self.isApprove=[false,false];
            self.paraMap.pageNum = 1;
            self.paraMap.pageSize = 10;

        }

        popBatchApprove() {
            let self = this;

            self.popups.openBatchApprove();
        }

    });

});