define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {

    function indexController($scope, alert, confirm, $translate, cActions, notify, $location, cRoutes, cookieService, biReportService) {
        $scope.datePicker = [];
        $scope.exportStatus = ["正在生成", "完成", "失败"];
        $scope.vm =
        {
            downloadTaskList: [],
            searchInfo: {
                fileTypes: []
            },
            "message": "successfully!",
            status: {open: true}
        };
        $scope.channels = [
            {channelId: "007", channelName: "ALL"},
            {channelId: "010", channelName: "Jewelry"},
            {channelId: "012", channelName: "BCBG"},
            {channelId: "014", channelName: "SC"},
            {channelId: "017", channelName: "LV"},
            {channelId: "018", channelName: "Jewelry"},
            {channelId: "024", channelName: "OverStock"},
            {channelId: "030", channelName: "WL"}
        ];
        $scope.fileTypes = [
            {fileTypeCode: "1", fileTypeName: "商铺月报"},
            {fileTypeCode: "2", fileTypeName: "商铺周报"},
            {fileTypeCode: "3", fileTypeName: "商铺日报"},
            {fileTypeCode: "4", fileTypeName: "商品月报"},
            {fileTypeCode: "5", fileTypeName: "商品周报"},
            {fileTypeCode: "6", fileTypeName: "商品日报"},
            {fileTypeCode: "7", fileTypeName: "产品月报"},
            {fileTypeCode: "8", fileTypeName: "产品周报"},
            {fileTypeCode: "9", fileTypeName: "产品日报"},
            {fileTypeCode: "10", fileTypeName: "sku月报"},
            {fileTypeCode: "11", fileTypeName: "sku周报"},
            {fileTypeCode: "12", fileTypeName: "sku日报"}
        ];
        $scope.clear = function () {
            $scope.searchInfo = {};
        };
        $scope.initialize = function () {
            biReportService.init().then(function (res) {
                $scope.search();
            });
        };
        //分页
        $scope.dataPageOption = {curr: 1, total: 0, fetch: goPage};
        $scope.search = function () {
            var pageParameter = getPageParameter();
            $scope.dataPageOption.setPageIndex(1);//查询第一页
            //获取页数量,这里需要跨域请求
            biReportService.getCount(pageParameter).then(function (res) {
                console.log(res.data);
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });
        };
        //跳转指定页
        function goPage(pageIndex, pageRowCount) {
            var pageParameter = getPageParameter();
            pageParameter.pageIndex = pageIndex;
            pageParameter.pageRowCount = pageRowCount;
            biReportService.getPage(pageParameter).then(function (res) {
                loadGridDataSource(res.data);
            }, function (res) {
            })
        }
        //获取分页参数及其条件
        function getPageParameter() {
            var pageParameter = angular.copy($scope.vm.searchInfo);
            pageParameter.fileTypes = _.filter(pageParameter.fileTypes, function (subFileType) {
                return null != subFileType;
            });
            return pageParameter;
        }
        //绑定grid数据源
        function loadGridDataSource(data) {
            $scope.vm.downloadTaskList = data;
        }
        function getDownloadTaskList() {
            biReportService.getDownloadTaskList().then(function (res) {
                $scope.vm.downloadTaskList = res.data.list;
            });
        }
        /* 分页以下*/
        $scope.openOtherDownload = function (item) {
                if (item.taskStatus == 1) {
                    alert("文件创建失败，请重新生成！");
                    return;
                }
                if (item.taskStatus == 2) {
                    alert("文件正在创建，不能下载！");
                    return;
                }
                function _exportFileCallback(res) {
                    var obj = JSON.parse(res);
                    if (obj.code == '4004') {
                        $scope.initialize();
                        alert("此文件不存在");
                    }
                }
            $.download.post(cActions.cms.biReportService.root + cActions.cms.biReportService.biRepDownload,
                {
                    "taskId":item.id,
                    "fileName":item.fileName,
                    "exportPath":item.filePath
                },
                _exportFileCallback);

            };
        $scope.createXlsFileTask = function()
        {
            var parameter=getPageParameter();
            if(parameter.fileTypes == null || parameter.fileTypes.length == 0)
            {
                alert("请选择文件类型");
                return;
            }
            if(!(validateDate(parameter.staDate)&&validateDate(parameter.endDate)))
            {
                alert("请选择时间！");
                return ;
            }
            if(parameter.channelCode == null ||parameter.channelCode == "")
            {
                alert("请选择渠道（品牌）");
                return ;
            }
            biReportService.createXlsFileTask(parameter).then(function (res) {
                if(res.ecd == "0")
                {
                    $scope.initialize();
                }
                else
                {
                    alert("创建文件失败");
                }
            })

        };
        function validateDate(val) {
            var datePattern = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$/;
            return datePattern.test(val);
        }


    }

    indexController.$inject = ['$scope',"alert", 'confirm', '$translate', 'cActions','notify','$location','cRoutes', 'cookieService','biReportService'];

    return indexController;
});