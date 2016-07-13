
define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {

    function indexController($scope, promotionService, promotionDetailService, confirm, $translate, cActions, notify, $location, cRoutes, cookieService) {

        $scope.vm = {"promotionList": [], "platformTypeList": [], "promotionStatus": [{"name":"Open","value":0},{"name":"Close","value":1}],"promotionIdList": [],status: {open: true}};
        $scope.searchInfo = {};
        $scope.groupPageOption = {curr: 1, total: 0, fetch: $scope.search};
        $scope.datePicker = [];
        $scope.currentChannelId = cookieService.channel();
        $scope.initialize = function () {
            promotionService.init().then(function (res) {
                $scope.vm.platformTypeList = res.data.platformTypeList;
                $scope.search();
            });
        };

        $scope.clear = function () {
            $scope.searchInfo = {};
        };

        $scope.openOtherDownload = function (promotion) {

            $.download.post(cActions.cms.promotion.promotionService.root + "/" + cActions.cms.promotion.promotionService.exportPromotion, {"promotionId": promotion.id,"promotionName":promotion.promotionName});
        };
        $scope.dataPageOption = {curr: 1, total: 0, fetch: goPage.bind(this)};
        $scope.search = function () {
            var pageParameter=getPageParameter();
            $scope.dataPageOption.setPageIndex(1);//查询第一页

            //获取页数量
            promotionService.getCount(pageParameter).then(function (res) {
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });
        };
        function getPageParameter() {
            var pageParameter={};
            pageParameter.parameters=angular.copy($scope.searchInfo);;
            return pageParameter;
        }
        function goPage(pageIndex, pageRowCount) {
            var pageParameter=getPageParameter();
            pageParameter.pageIndex= pageIndex;
            pageParameter.pageRowCount = pageRowCount;
            promotionService.getPage(pageParameter).then(function (res) {
                $scope.vm.promotionList = _.where(res.data, {isAllPromotion: 0});
                _.each($scope.vm.promotionList,function(item){
                    if(item.prePeriodStart) item.prePeriodStart = new Date(item.prePeriodStart);
                    if(item.prePeriodEnd) item.prePeriodEnd = new Date(item.prePeriodEnd);
                    if(item.activityStart) item.activityStart = new Date(item.activityStart);
                    if(item.activityEnd) item.activityEnd = new Date(item.activityEnd);
                    if(item.preSaleStart) item.preSaleStart = new Date(item.preSaleStart);
                    if(item.preSaleEnd) item.preSaleEnd = new Date(item.preSaleEnd);
                });
                $scope.groupPageOption.total = $scope.vm.promotionList.length;
            }, function (res) {
            })
        }

        //$scope.search = function () {
        //    promotionService.getPromotionList($scope.searchInfo).then(function (res) {
        //        $scope.vm.promotionList = _.where(res.data, {isAllPromotion: 0});
        //        _.each($scope.vm.promotionList,function(item){
        //            if(item.prePeriodStart) item.prePeriodStart = new Date(item.prePeriodStart);
        //            if(item.prePeriodEnd) item.prePeriodEnd = new Date(item.prePeriodEnd);
        //            if(item.activityStart) item.activityStart = new Date(item.activityStart);
        //            if(item.activityEnd) item.activityEnd = new Date(item.activityEnd);
        //            if(item.preSaleStart) item.preSaleStart = new Date(item.preSaleStart);
        //            if(item.preSaleEnd) item.preSaleEnd = new Date(item.preSaleEnd);
        //        });
        //        $scope.groupPageOption.total = $scope.vm.promotionList.length;
        //    })
        //};

        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_PROMOTION_DELETE').replace("%s",data.promotionName)).result.then(function () {
                var index = _.indexOf($scope.vm.promotionList, data);
                promotionService.delPromotion(data).then(function () {
                    $scope.vm.promotionList.splice(index, 1);
                    $scope.groupPageOption.total = $scope.vm.promotionList.size;
                })
            })

        };

        $scope.teJiaBaoInit = function(promotionId){
            promotionDetailService.teJiaBaoInit(promotionId).then(function () {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                $location.path(cRoutes.promotion_task_price.url + promotionId);
            })
        };
    };

    indexController.$inject = ['$scope', 'promotionService', 'promotionDetailService', 'confirm', '$translate', 'cActions','notify','$location','cRoutes', 'cookieService'];

    return indexController;
});