define([
 'cms',
 './joinPromotion.data'
],function(cms,promotionData){

    cms.service('addProductToPromotionService2',function($q){

        this.init = init;

        function init(){
            var defer = $q.defer();

            defer.resolve(promotionData);

            return defer.promise;
        }

    });

});
