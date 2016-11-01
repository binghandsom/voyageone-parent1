define([
 'cms',
 './joinPromotion.dev'
],function(cms){

    cms.controller('joinPromotionCtl',(function(){

        function JoinPromotionCtl(addProductToPromotionService2){
            this.addProductToPromotionService = addProductToPromotionService2;
        }

        /**
         * cartId isSelAll codeList    addProductToPromotionService.init
         */
        JoinPromotionCtl.prototype.init = function(){
            var self = this;

            self.addProductToPromotionService.init().then(function(res){
                console.log("res",res);
            });
        };

        return JoinPromotionCtl;

    })());

});
