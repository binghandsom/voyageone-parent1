/**
 * @author piao
 * @date 2016-10-08
 * @version V2.8.0
 */
define([
    'cms'
],function(cms){

    return cms.controller('setSkuPriceCtl',(function(){

        function SetSkuPriceController(context,promotionDetailService){
            this.context = context;
            this.promotionDetailService = promotionDetailService;
        }

        SetSkuPriceController.prototype.init = function(){
            var self = this,
                promotionDetailService = self.promotionDetailService,
                context = self.context;

            promotionDetailService.getPromotionSkuList({productId:context.productId,promotionId:context.promotionId}).then(function(res){
                self.dataList = res.data;
            });
        };

        return SetSkuPriceController;

    })());

});