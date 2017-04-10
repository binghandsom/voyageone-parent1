/**
 * @description MQ:sku-code 错误列表
 * @author piao
 */
define([
    'cms'
],function(cms){

    cms.controller('mqSkuCodeErrorController',(function(){

        function MqSkuCodeErrorCtl(context, $uibModalInstance){
            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
        }

        MqSkuCodeErrorCtl.prototype.init = function(){
            var self = this;

            self.msgList = this.context.msg;
        };

        return MqSkuCodeErrorCtl;

    })());

});