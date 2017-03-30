define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageDetailCtl', function ($scope, $rootScope, context) {
        $scope.vm = {
            picInfo: context
        };

        $scope.initialize = initialize;
        $scope.goDetail = goDetail;

        function initialize() {
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
                picObj.mainPic = $rootScope.imageUrl.replace("%s", picObj.mainPic);//"http://image.sneakerhead.com/is/image/sneakerhead/" + ;
                var picList = picObj.picList;

                //框架foreach无法改变值，所以用js源生循环
                for (var i = 0, length = picList.length; i < length; i++) {
                    for (var j = 0, length2 = picList[i].length; j < length2; j++) {
                        picList[i][j] = $rootScope.imageUrl.replace("%s", picList[i][j]);
                    }
                }
            }
        }

        /**
         * 直接跳转到图片的url
         */
        function goDetail() {
            var vm = $scope.vm,
                args,
                url = !vm.currentImage ? vm.picInfo.mainPic : vm.currentImage;

            args = url.split("/");

            if(args.length == 0)
                return;

            window.open("http://image.voyageone.com.cn/is/image/sneakerhead/✓?wid=2200&hei=2200".replace("✓", args[args.length - 1]));
        }

    });
});
