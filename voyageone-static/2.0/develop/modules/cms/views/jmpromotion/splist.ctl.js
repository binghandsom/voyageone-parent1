
define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {

    function indexController($scope, promotionService, jmPromotionService, promotionDetailService,alert, confirm, $translate, cActions, notify, $location, cRoutes, cookieService, $filter) {

        $scope.vm = {"promotionList": [], "platformTypeList": [], "promotionStatus": [{"name":"Open","value":1},{"name":"Close","value":0}],"promotionIdList": [],status: {open: true}};
        $scope.searchInfo = {};
        $scope.datePicker = [];
        $scope.currentChannelId = cookieService.channel();
        $scope.dataPageOption = {curr: 1, total: 0, size: 10, fetch: goPage.bind(this)};

        $scope.initialize = function () {
            jmPromotionService.init().then(function (res) {
                $scope.vm.jmMasterBrandList = res.data.jmMasterBrandList;
                $scope.search();
            });
        };

        $scope.clear = function () {
            $scope.searchInfo = {};
        };

        $scope.search = function () {
            var pageParameter = getPageParameter();
            $scope.dataPageOption.setPageIndex(1);//查询第一页
            //获取页数量
            jmPromotionService.getJmPromCount(pageParameter).then(function (res) {
                $scope.dataPageOption.total = res.data;
            });
        };

        //跳转指定页
        function goPage(pageIndex, pageRowCount) {
            var pageParameter = getPageParameter();
            pageParameter.pageIndex= pageIndex;
            pageParameter.pageRowCount = pageRowCount;
            jmPromotionService.getJmPromList(pageParameter).then(function (res) {
                $scope.vm.promotionList = res.data;//_.where(res.data, {isAllPromotion: 0});
                _.each($scope.vm.promotionList, function(item) {
                    item.prePeriodStart = _formatToStr(item.prePeriodStart);
                    item.activityStart = _formatToStr(item.activityStart);
                    item.activityEnd = _formatToStr(item.activityEnd);
                    item.signupDeadline = _formatToStr(item.signupDeadline);
                });
            })
        }

        //获取分页参数及其条件
        function getPageParameter() {
            var pageParameter = {};
            $scope.searchInfo.cartId = 27;
            pageParameter.parameters = angular.copy($scope.searchInfo);
            return pageParameter;
        }

        function _formatToStr(dateStr) {
            if (dateStr) {
                return dateStr.substr(0, 10);
            } else {
                return '';
            }
        }

        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_PROMOTION_DELETE').replace("%s",data.name)).then(function () {
                var index = _.indexOf($scope.vm.promotionList, data);
                promotionService.deleteByPromotionId(data.id).then(function(res) {
                    if(res.data.result) {
                        $scope.vm.promotionList.splice(index, 1);
                        $scope.dataPageOption.total = $scope.vm.promotionList.length;
                    } else {
                      alert(res.data.msg);
                    }
                });
            })
        };

    };

    indexController.$inject = ['$scope', 'promotionService', 'jmPromotionService', 'promotionDetailService',"alert", 'confirm', '$translate', 'cActions','notify','$location','cRoutes', 'cookieService', '$filter'];

    return indexController;
});