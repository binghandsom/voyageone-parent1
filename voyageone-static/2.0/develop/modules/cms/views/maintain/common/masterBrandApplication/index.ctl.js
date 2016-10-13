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

        function MainBrandController(popups){
            this.popups = popups;
        }

        MainBrandController.prototype.init = function(){
            var self = this;
            this.masterBrandApplicationService   = masterBrandApplicationService;
        };

        MainBrandController.prototype.popBrandAudit = function(){
            var self = this,
                popups = self.popups;

            popups.openBrandAudit({}).then(function(context){

            });
        };

        MainBrandController.prototype.popBrandEdit = function(){
            var self = this,
                popups = self.popups;

            popups.openMbrandEdit({}).then(function(context){

            });

        };

        return MainBrandController;

    })());

});