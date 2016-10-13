/**
 * @description 主品牌设置
 * @author piao
 * @version V2.8.0
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
],function(cms){

    cms.controller('masterBrandApplicationController',(function(){

        function masterBrandApplicationController(masterBrandApplicationService,popups){
            this.popups = popups;
            this.masterBrandApplicationService = masterBrandApplicationService;
            this.searchInfo = {
                statusList :[],
                masterBrandCn:'',
                masterBrandEn:'',
                channelId:'',
                feedBrand:'',
                cartBrandName:''
            }
        }

        masterBrandApplicationController.prototype.init = function(){
            var self = this;

            self.search();

        };

        masterBrandApplicationController.prototype.search = function(){
            var self = this,

                masterBrandApplicationService = self.masterBrandApplicationService;

            masterBrandApplicationService.search({
                statusList :['0'],
                masterBrandEn:'',
                masterBrandCn:'',
                channelId:'',
                feedBrand:'',
                cartBrandName:''
            }).then(function(res){
                console.log("res",res);
            });

        };


        masterBrandApplicationController.prototype.popMasterBrandCheck = function(){
            var self = this,
                popups = self.popups;

            popups.openMasterBrandCheck({}).then(function(context){

            });
        };

        masterBrandApplicationController.prototype.popMasterBrandEdit = function(){
            var self = this,
                popups = self.popups;

            popups.openMasterBrandEdit({}).then(function(context){

            });

        };


        return masterBrandApplicationController;

    })());

});