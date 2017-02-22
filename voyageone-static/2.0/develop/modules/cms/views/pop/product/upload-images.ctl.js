/**
 * @description 产品详情页 商品图片上传
 * @author Piao
 */
define([
    'cms'
],function(cms){

    cms.controller('uploadImagesController',(function(){

        function UploadImagesCtl(context){
            this.context = context;
        }

        UploadImagesCtl.prototype.init = function(){

        };

        return UploadImagesCtl;

    })());

});
