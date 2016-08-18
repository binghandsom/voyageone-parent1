/**
 * Created by 123 on 2016/4/13.
 */
define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {


    function valueChannelController($scope, $valueChannelService,$translate, confirm,notify) {



        $scope.hsCodeInfo="";
        $scope.addHsCode = addHsCode;
        function addHsCode(typeId){
            $valueChannelService.addHsCodes({"typeId":typeId,"hsCodes":$scope.hsCodeInfo}).then(function(){
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                $scope.hsCodeInfo = "";
            })
        }
    }

    valueChannelController.$inject = ['$scope', "$valueChannelService",  '$translate', 'confirm','notify'];
    return valueChannelController;
});
