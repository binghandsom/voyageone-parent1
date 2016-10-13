/**
 * @description 主品牌设置
 * @author piao
 * @version V2.8.0
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
],function(cms){

    cms.controller('masterBrandApplicationController',(function(masterBrandApplicationService, popups){

        function masterBrandApplicationController(popups){
            this.popups = popups;
            this.masterBrandApplicationService   = masterBrandApplicationService;
        }

        masterBrandApplicationController.prototype.init = function(){
            var self = this;

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