define([
'cms'
],function(cms){

    cms.controller('imageSuitCtl',(function(){

        function ImageSuitCtl(context,JmPromotionImagesService,alert,$uibModalInstance){
            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
            this.JmPromotionImagesService = JmPromotionImagesService;
            this.alert = alert;
        }

        ImageSuitCtl.prototype.init = function(){
            var self = this,
                context = self.context,
                JmPromotionImagesService = self.JmPromotionImagesService;

            JmPromotionImagesService.getImageForSuit({brand:context.brand}).then(function(res){
                self.dataList  = res.data;
            });
        };

        ImageSuitCtl.prototype.chose = function(){
            var self  = this,
                alert = self.alert,
                $uibModalInstance = self.$uibModalInstance,
                selectedImg;

            if(!self.selected){
                alert("请选择一个套图!");
                return;
            }

            selectedImg = _.find(self.dataList,function(item){
                return item.promotionId == self.selected;
            });

            $uibModalInstance.close(selectedImg);

        };

        return ImageSuitCtl;

    })());

});