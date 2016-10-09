/**
 * @author piao
 * @date 2016-10-08
 * @version V2.8.0
 */
define([
    'cms'
],function(cms){

    return cms.controller('setSkuPriceCtl',(function(){

        function SetSkuPriceController(context,promotionDetailService2){
            this.context = context;
            this.promotionDetailService = promotionDetailService2;
        }

        SetSkuPriceController.prototype.init = function(){
            var self = this;

            self.promotionDetailService.getPromotionSkuList().then(function(res){
                self.dataList = res.data;
            });
        };

        return SetSkuPriceController;

    })());

});