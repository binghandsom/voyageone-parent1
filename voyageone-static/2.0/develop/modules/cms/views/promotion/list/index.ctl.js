
define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {

    function indexController($scope, promotionService, promotionDetailService, confirm, $translate, cActions, notify, $location, cRoutes) {

        $scope.vm = {"promotionList": [], "platformTypeList": [], "promotionStatus": []};
        $scope.searchInfo = {};
        $scope.groupPageOption = {curr: 1, total: 198, size: 30, fetch: $scope.search};
        $scope.datePicker = [];

        $scope.initialize = function () {
            promotionService.init().then(function (res) {
                $scope.vm.platformTypeList = res.data.platformTypeList;
                $scope.vm.promotionStatus = res.data.promotionStatus;
                $scope.search();
            });
        };

        $scope.clear = function () {
            $scope.searchInfo = {};
        };

        $scope.openOtherDownload = function (promotion) {

            $.download.post(cActions.cms.promotion.promotionService.root + "/" + cActions.cms.promotion.promotionService.exportPromotion, {"promotionId": promotion.promotionId,"promotionName":promotion.promotionName});
        };

        $scope.search = function () {
            promotionService.getPromotionList($scope.searchInfo).then(function (res) {
                $scope.vm.promotionList = _.where(res.data, {isAllPromotion: false});
                $scope.groupPageOption.total = $scope.vm.promotionList.size;
            }, function (res) {
            })
        };

        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.promotionName).result.then(function () {
                var index = _.indexOf($scope.vm.promotionList, data);
                data.isActive = false;
                promotionService.delPromotion(data).then(function () {
                    $scope.vm.promotionList.splice(index, 1);
                    $scope.groupPageOption.total = $scope.vm.promotionList.size;
                }, function (res) {
                })
            })

        };

        $scope.teJiaBaoInit = function(promotionId){
            promotionDetailService.teJiaBaoInit(promotionId).then(function (res) {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                $location.path(cRoutes.promotion_task_price.url + promotionId);
            })
        };
    }

    indexController.$inject = ['$scope', 'promotionService', 'promotionDetailService', 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes'];
    return indexController;
});