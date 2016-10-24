define([
'cms'
],function(cms){

    cms.controller('imageSuitCtl',(function(){

        function ImageSuitCtl(context,JmPromotionImagesService,alert,$uibModalInstance){
            this.context = context;
            this.$uibModalInstance = $uibModalInstance;
            this.JmPromotionImagesService = JmPromotionImagesService;
            this.alert = alert;
            this.selected = {};
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
                selectedImg,
                selectList = _.map(self.selected,function(value,key){
                    if(value)
                        return key;
                });

            if(selectList.length != 1)
                alert("请选择一个套图，且只能选择一个");

            selectedImg = _.find(self.dataList,function(item){
                return item.promotionId = selectList[0];
            });

            $uibModalInstance.close(selectedImg);

        };

        return ImageSuitCtl;

    })());

});