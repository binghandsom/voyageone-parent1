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
                if ($rootScope.imageUrl == null || $rootScope.imageUrl == undefined) {
                    $rootScope.imageUrl = '';
                }
                picObj.mainPic =  $rootScope.imageUrl.replace("%s", picObj.mainPic) + ".jpg";//"http://image.sneakerhead.com/is/image/sneakerhead/" + ;
                var picList = picObj.picList;

                //框架foreach无法改变值，所以用js源生循环
                for(var i=0,length=picList.length;i<length;i++){
                     for(var j=0,length2=picList[i].length;j<length2;j++){
                         picList[i][j] = $rootScope.imageUrl.replace("%s", picList[i][j]) + ".jpg";
                     }
                 }
            }
        };
    });
});
