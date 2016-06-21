/**
 * Created by pwj on 2016/4/19.
 */
define([
    'underscore',
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (_ , angularAMD) {
    angularAMD.controller('popConfirmStoreOppCtl', function ($scope,context,$storeOpService,notify,alert) {
        $scope.pop = {
            header : context.header,
            upLoadFlag: context.upLoadFlag,
            command:""
        }
        $scope.doStore = doStore;

        /**
         * 1:重新上新所有商品,2:重新导入所有Feed商品（清空共通属性）,0:重新导入所有Feed商品（不清空共通属性）,3:价格同步
         */
        function doStore(){
            if($scope.pop.command !== 'EXECUTE')
            {
                alert("该操作为不可恢复操作，请输入大写EXECUTE以确认！");
                return;
            }

          switch(context.upLoadFlag)
            {
                case 1:
                    $storeOpService.rePublist().then(function(resp){
                        if(resp.data == true)
                            notify.success("操作成功！");
                        $scope.$dismiss();
                    });
                    break;
                case 3:
                    $storeOpService.rePublistPrice().then(function(resp){
                        if(resp.data == true)
                            notify.success("操作成功！");
                        $scope.$dismiss();
                    });
                    break;
                default:
                    var cleanCommonProperties = "0";
                    if(context.upLoadFlag == 2)
                        cleanCommonProperties = "1";
                    $storeOpService.reUpload(cleanCommonProperties).then(function(resp){
                        if(resp.data == true)
                            notify.success("操作成功！");
                        $scope.$dismiss();
                    });
            }
        }
    });
});