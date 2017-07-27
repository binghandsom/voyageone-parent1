define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popImageDetailCtl', function ($scope, $rootScope, imageDirectiveService, context) {
        this.search = context.search;
        this.mainPic = context.mainPic;
        this.currentImage = context.mainPic;

        if (context.hostUrl === 0) {
            // hostUrl 为 0，表示参数值就是图片地址，而不是图片名，所以不转换
            return;
        }

        this.currentImage = imageDirectiveService.getImageUrlByName(this.currentImage);

        if (!context.picList) {
            return;
        }

        this.picList = context.picList.map(function (picNameList) {
            return picNameList.map(function (picName) {
                return picName && imageDirectiveService.getImageUrlByName(picName);
            });
        });

        console.log(this.picList);
        console.log(context.picList)
    });
});
