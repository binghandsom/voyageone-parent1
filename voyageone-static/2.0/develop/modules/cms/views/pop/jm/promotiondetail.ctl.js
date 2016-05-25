
/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popPromotionDetailCtl', function ($scope,jmPromotionService,context,confirm,$translate) {
        $scope.vm = {"jmMasterBrandList":[]};
        $scope.model = {};
        $scope.datePicker = [];
        $scope.initialize  = function () {
            if(context)
            {
                $scope.model=context;
            }
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
            });
        };
        $scope.addTag = function () {
            if ($scope.model.tagList) {
                $scope.model.tagList.push({"id": "", "channelId": "", "tagName": ""});
            } else {
                $scope.model.tagList = [{"id": "", "channelId": "", "tagName": ""}];
            }
        };

        $scope.delTag = function (parent, node) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                    var index;
                    index = _.indexOf(parent, node);
                    if (index > -1) {
                        parent.splice(index, 1);
                    }
                });
        };
        $scope.ok = function(){
            //console.log("save");
            //console.log($scope.model);
            if(!$scope.model.id) {
                jmPromotionService.insert($scope.model).then(function (res) {

                    $scope.$close();
                }, function (res) {
                })
            }else{
                jmPromotionService.update($scope.model).then(function (res) {
                    for (key in $scope.promotion) {
                        items[key] = $scope.promotion[key];
                    }
                    $scope.$close();
                }, function (res) {
                })
            }
        }
    });
});