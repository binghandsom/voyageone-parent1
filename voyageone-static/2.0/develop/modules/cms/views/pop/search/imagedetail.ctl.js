/**
 * Created by 123 on 2016/4/1.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageDetailCtl', function ($scope, $rootScope, context) {
        $scope.vm = {
            picInfo : context
        };
        /**
         * 初始化数据.
         */
        $scope.initialize = function () {
            var picObj = $scope.vm.picInfo;
            if (picObj == undefined) {
                return;
            }
            if ($scope.vm.picInfo.hostUrl == 0) {
                // 直接使用传入的图片url
            } else {
                // 传入的是图片名，必须拼接图片服务器的url
                picObj.mainPic =  $rootScope.imageUrl.replace("%s", picObj.mainPic);//"http://image.sneakerhead.com/is/image/sneakerhead/" + ;

                var picList = picObj.picList;
                _.forEach(picList, function (itemList) {
                    _.forEach(itemList, function (picName) {
                        picName = $rootScope.imageUrl.replace("%s", picName);//"http://image.sneakerhead.com/is/image/sneakerhead/" + picName;
                    });
                });
            }
        };
    });
});
