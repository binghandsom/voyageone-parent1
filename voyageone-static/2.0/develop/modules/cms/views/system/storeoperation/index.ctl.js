/**
 * Created by pwj on 2016/4/25.
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
        return cms.controller('storeOperationController', (function(){
            function storeOperationController($storeOpService){
                this.storeOpService = $storeOpService;
                this.searchInfo = {operationType:"",modifier:""};
                this.storePageOption = {curr: 1, total: 0, fetch: this.search.bind(this)}
                this.storeDataList = [];
            }
            storeOperationController.prototype = {
                init:function(){
                    var self = this;
                    //获取可供上新商品总数
                    self.storeOpService.init().then(function(resp){
                        self.uploadCnt = resp.data.uploadCnt;
                    });
                    this.search();
                },
                clear:function(){
                    this.searchInfo = {
                        operationType : null,
                        modifier : null
                    };
                },
                search:function(){
                    var self = this;
                    self.storeOpService.getHistory(self.storePageOption,self.searchInfo).then (function(res) {
                         self.storeDataList = res.data.data;
                         self.storePageOption.total = res.data.total;
                    })
                }
            }
            return storeOperationController;

        })());

});