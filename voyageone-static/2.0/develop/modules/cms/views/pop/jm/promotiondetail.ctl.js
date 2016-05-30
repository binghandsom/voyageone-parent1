
/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {
    angularAMD.controller('popPromotionDetailCtl', function ($scope,jmPromotionService,alert,context,confirm,$translate) {
        $scope.vm = {"jmMasterBrandList":[]};
        $scope.editModel = {};
        $scope.datePicker = [];
        $scope.initialize  = function () {
            console.log(context);
            if(context.id)
            {
                jmPromotionService.getEditModel(context.id).then(function (res) {
                    $scope.editModel = res.data;
                });
            }
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
            });
        };
        $scope.addTag = function () {
            if ($scope.editModel.tagList) {
                $scope.editModel.tagList.push({"id": "", "channelId": "", "tagName": "",active:1});
            } else {
                $scope.editModel.tagList = [{"id": "", "channelId": "", "tagName": "",active:1}];
            }
        };
        $scope.getTagList=function()
        {
            var tagList = _.filter( $scope.editModel.tagList, function(tag){ return tag.active==1; });
            return tagList||[];
        }
        $scope.delTag = function (tag) {
            confirm($translate.instant('TXT_MSG_DELETE_ITEM')).result
                .then(function () {
                  tag.active=0;
                });
        };
        $scope.ok = function() {
            $scope.editModel.tagList= _.filter( $scope.editModel.tagList, function(tag){ return tag.tagName!=""; });
            jmPromotionService.saveModel($scope.editModel).then(function (res) {
                    $scope.$close();
                if(context.search) {
                    context.search();
                }
            }, function (res) {
            })
        }
    });
});