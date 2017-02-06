
define([
    '' +
    '',
    'modules/cms/controller/popup.ctl',
    'modules/cms/views/search/advanceSearch.ctl',
    'underscore',
    'modules/cms/directives/keyValue.directive',
    'modules/cms/service/search.advance2.service',
    'modules/cms/service/product.detail.service'
], function (angularAMD) {

    function indexController($scope, promotionService, promotionDetailService,alert, confirm, $translate, cActions, notify, $location, cRoutes, cookieService,biReportService,searchAdvanceService2,$scope, $routeParams, searchAdvanceService2, $searchAdvanceService2, $fieldEditService, productDetailService, systemCategoryService, $addChannelCategoryService, confirm, $translate, notify, alert, sellerCatService, platformMappingService, attributeService, $sessionStorage, cActions, popups, $q, shelvesService) {
        //$scope.vm = {"promotionList": [], "platformTypeList": [], "promotionStatus": [{"name":"Open","value":1},{"name":"Close","value":0}],"promotionIdList": [],status: {open: true}};
        //$scope.groupPageOption = {curr: 1, total: 0, fetch: $scope.search};
        $scope.datePicker = [];
        $scope.exportStatus = ["正在生成", "完成", "失败"];
        $scope.vm=
        {
            searchInfo:
            {

            },
            "downloadList":[],
            "message":"successfully!",
            status: {open: true}
        };
        $scope.sites = [
            {site : "Google", url : "http://www.google.com"},
            {site : "Runoob", url : "http://www.runoob.com"},
            {site : "Taobao", url : "http://www.taobao.com"}
        ];
        $scope.channels=[
            {channelId : "007", channelName :"ALL"},
            {channelId : "010", channelName :"Jewelry"},
            {channelId : "012", channelName :"BCBG"},
            {channelId : "014", channelName :"SC"},
            {channelId : "017", channelName :"LV"},
            {channelId : "018", channelName :"Jewelry"},
            {channelId : "024", channelName :"OverStock"},
            {channelId : "030", channelName :"WL"}
        ];
        // $scope.currentChannelId = cookieService.channel();
        // $scope.vm={"testBeanList":[],"message":"successfully!",status: {open: true}};
        $scope.exportFile = exportFile;
       /* $scope.initialize = function () {
            promotionService.init().then(function (res) {
                $scope.vm.platformTypeList = res.data.platformTypeList;
                $scope.search();
            });
        };*/
       $scope.showCode=function () {
           log.info(vm.searchInfo.channel);
           alert(vm.searchInfo.channel);
       }
        $scope.clear = function () {
            $scope.searchInfo = {};
        };
        $scope.downloadExcel=function(data) {
            /* $scope.time=$scope.time+1;*/
            alert("hello,"+data);
        };
        $scope.search = function () {
            var pageParameter=getPageParameter();
            $scope.dataPageOption.setPageIndex(1);//查询第一页
            //获取页数量
            promotionService.getCount(pageParameter).then(function (res) {
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });
        };
        $scope.test2 = function() {
            alert($scope.vm.searchInfo.channelStart+"------"+$scope.vm.searchInfo.channel);
        };

        $scope.biRepDownload = function () {
            //$scope.time=$scope.time+1;
            var pageParameter=getPageParameter();
            //alert("hello!");
            biReportService.biRepDownload(pageParameter).then(function (res) {
                //$scope.time=$scope.time+2;
                if(res.data.testBeanList.length==0)
                {
                    alert("testBeanList lenght is 0");
                }
                else {
                    /*  alert("hello,world!");*/
                    $scope.vm.testBeanList = res.data.testBeanList;
                }
                // $scope.search();
            });
        };
        $scope.bireportDownload=function () {
            var parameters=getPageParameter();
                function _exportFileCallback(res) {
                    var obj = JSON.parse(res);
                    if (obj.code == '4004') {
                        alert("此文件不存在");
                    }
                }
                $.download.post(cActions.cms.biReportService.root + cActions.cms.biReportService.createXlsFile,
                    {"nameCn":$scope.vm.searchInfo.channel,
                        "staDate":$scope.vm.searchInfo.channelStart,
                        "endDate":$scope.vm.searchInfo.channelEnd
                    },
                    _exportFileCallback);
        };
        // 下载已创建完成的数据文件
        $scope.SearchopenDownload = function (fileName, status) {
            if (status == -1) {
                alert("文件已经过期，请重新下载");
                return;
            }
            function _exportFileCallback(res) {
                var obj = JSON.parse(res);
                if (obj.code == '4004') {
                    alert("此文件不存在");
                }
            }

            $.download.post(cActions.cms.search.$searchAdvanceService2.root + cActions.cms.search.$searchAdvanceService2.exportDownload,
                {"fileName": fileName
                },
                _exportFileCallback);
        };


        $scope.openDownload = function (fileName, status) {

            if (status == -1) {
                alert("文件已经过期，请重新下载");
                return;
            }
            function _exportFileCallback(res) {
                if(res==null)
                {
                    alert("res is null");
                }
                var obj = JSON.parse(res);
                if (obj.code == '4004') {
                    alert("此文件不存在");
                }
            }

            $.download.post(cActions.cms.search.$searchAdvanceService2.root + cActions.cms.search.$searchAdvanceService2.exportDownload, {}, _exportFileCallback);
        };
        /**
         * 数据导出
         */
        function exportFile(fileType) {
            var msg = '';
            if (fileType == 6) {
                msg = '即将导出bi_report搜索结果，请确认。' + msg;
            }else
            {
                msg="no fileType!"
            }
            confirm(msg).then(function () {
                $scope.vm.searchInfo.fileType = fileType;
                searchAdvanceService2.exportFile($scope.vm.searchInfo).then(function (res) {
                    var ecd = res.data.ecd;
                    if (ecd == undefined || ecd == '4003') {
                        alert("创建文件时出错。");
                    } else if (ecd == '4002') {
                        alert("未选择导出文件类型。");
                    } else if (ecd == '4004') {
                        alert("已经有一个任务还没有执行完毕。请稍后再导出");
                    } else if (ecd == '0') {
                        notify.success($translate.instant('TXT_SUBMIT_SUCCESS'));
                    }
                });
            });
        }
     /*   $scope.search = function () {
            var pageParameter=getPageParameter();
            $scope.dataPageOption.setPageIndex(1);//查询第一页
            //获取页数量
            promotionService.test(pageParameter).then(function (res) {
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });
            /!*promotionService.getCount(pageParameter).then(function (res) {
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });*!/
        };*/

        $scope.initialize = function () {
            getDownloadTaskList();
        };
        function getDownloadTaskList() {
          /*  var data = {
                code: $scope.vm.code,
                cartId: $scope.vm.cartId,
                offset: ($scope.vm.pageOption.curr - 1) * $scope.vm.pageOption.size,
                rows: $scope.vm.pageOption.size
            };*/
            biReportService.getDownloadTaskList().then(function (res) {
                $scope.vm.downloadTaskList = res.data.list;
               /* $scope.vm.pageOption.total = res.data.total;
                $scope.vm.cartList = res.data.cartList*/
            });
        }











        $scope.openOtherDownload = function (promotion) {

            $scope.download.post(cActions.cms.promotion.promotionService.root + "/" + cActions.cms.promotion.promotionService.exportPromotion, {"promotionId": promotion.id,"promotionName":promotion.promotionName});
        };
        $scope.dataPageOption = {curr: 1, total: 0, fetch: goPage.bind(this)};

        //跳转指定页
        function goPage(pageIndex, pageRowCount) {
            var pageParameter=getPageParameter();
            pageParameter.pageIndex= pageIndex;
            pageParameter.pageRowCount = pageRowCount;
            promotionService.getPage(pageParameter).then(function (res) {
                loadGridDataSource(res.data);
            }, function (res) {
            })
        }
        //获取分页参数及其条件
        function getPageParameter() {
            var pageParameter={};
            pageParameter.parameters=angular.copy($scope.vm.searchInfo);;
            return pageParameter;
        }


        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_PROMOTION_DELETE').replace("%s",data.promotionName)).then(function () {
                var index = _.indexOf($scope.vm.promotionList, data);
                promotionService.deleteByPromotionId(data.id).then(function(res){
                    if(res.data.result) {
                        $scope.vm.promotionList.splice(index, 1);
                        $scope.groupPageOption.total = $scope.vm.promotionList.size;
                    }
                    else
                    {
                        alert(res.data.msg);
                    }
                });
            })
        };
        $scope.setOpenPromotionStatus=function(data)
        {
            confirm($translate.instant("是否打开活动%s").replace("%s",data.promotionName)).then(function () {
                promotionService.setPromotionStatus({promotionId:data.id,promotionStatus:1}).then(function(res){
                    data.promotionStatus=1;
                });
            });
        }
        $scope.setClosePromotionStatus=function(data)
        {
            confirm($translate.instant("是否关闭活动%s").replace("%s",data.promotionName)).then(function () {
                promotionService.setPromotionStatus({promotionId:data.id,promotionStatus:0}).then(function(res){
                    data.promotionStatus=0;
                });
            });
        }
        $scope.teJiaBaoInit = function(promotionId){
            promotionDetailService.teJiaBaoInit(promotionId).then(function () {
                notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                $location.path(cRoutes.promotion_task_price.url + promotionId);
            })
        };
    };

    indexController.$inject = ['$scope', 'promotionService', 'promotionDetailService',"alert", 'confirm', '$translate', 'cActions','notify','$location','cRoutes', 'cookieService','biReportService','searchAdvanceService2','$scope', '$routeParams', 'searchAdvanceService2', '$searchAdvanceService2', '$fieldEditService', '$productDetailService', 'systemCategoryService', '$addChannelCategoryService', 'confirm', '$translate', 'notify', 'alert', 'sellerCatService', 'platformMappingService', 'attributeService', '$sessionStorage', 'cActions', 'popups', '$q', 'shelvesService'];

    return indexController;
});