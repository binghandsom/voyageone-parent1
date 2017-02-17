define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {

    function indexController($scope, alert, confirm, $translate, cActions, notify, $location, cRoutes, cookieService, biReportService) {
        $scope.datePicker = [];
        $scope.selectFileButton="全选";
        $scope.vm =
        {
            downloadTaskList: [],
            searchInfo: {
                channels : [],
                channelCodeList:[],
                fileTypes: []
            },
            "message": "successfully!",
            status: {open: true}
        };
        // $scope.channels = [];
        $scope.fileTypes = [
            {fileTypeCode: "1", fileTypeName: "店铺月报"},
            {fileTypeCode: "2", fileTypeName: "店铺周报"},
            {fileTypeCode: "3", fileTypeName: "店铺日报"},
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
                $scope.vm.searchInfo={};
                $scope.vm.searchInfo.channelCodeList=[];
        };
        $scope.initialize = function () {
            $scope.vm.minDate = new Date(2015,1,1);
            $scope.vm.maxDate = new Date();
            biReportService.init().then(function (res) {
                $scope.search();
            });
            biReportService.get_channel_list().then(function (res) {
                $scope.channels=res.data;
            })
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
                return (null != subFileType || false != subFileType || true !=subFileType);
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
                if (item.taskStatus != 3) {
                    alert("文件无法下载，可能是尚未生成，或者已失效");
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
            if( parameter.fileTypes.length == 0)
            {
                alert("请选择文件类型");
                return;
            }
            if(!(validateDate(parameter.staDate)&&validateDate(parameter.endDate)))
            {
                alert("请选择时间！");
                return ;
            }
            if(parameter.channelCodeList.length == 0)
            {
                alert("请选择渠道");
                return ;
            }
            confirm("确认生成此文件?").then(function () {
                // var index = _.indexOf($scope.vm.promotionList, data);
                biReportService.createXlsFileTask(parameter).then(function (res) {
                    var ecd = res.data.ecd ;
                    switch (ecd)
                    {
                        case "0":
                            alert("文件正在生成！");
                            break;
                        case "4400":
                            alert("无法找到文件路径！错误代码：" + res.data.ecd );
                            break;
                        case "4100":
                            alert("无法连接远程API！错误代码："+ res.data.ecd );
                            break;
                        default:
                            alert("生成文件失败，错误代码:" + res.data.ecd );
                            break;
                    }
                    $scope.initialize();
                });
            })
        };
        function validateDate(val) {
            var datePattern = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$/;
            return datePattern.test(val);
        }

        $scope.del = function (data) {
            confirm("确认删除此文件？").then(function () {
                // var index = _.indexOf($scope.vm.promotionList, data);
                biReportService.deleteTask(data.id).then(function(res){
                    if(res.data.result) {
                        alert("删除成功！");
                        $scope.search();
                    }
                    else
                    {
                        alert("删除成功！");
                    }
                });
            })
        };
        $scope.selectAllFileTypes = function () {
            if($scope.selectFileButton == "全选")
            {
                if( $scope.vm.searchInfo.channelCodeList.length <= 1)
                {
                    $scope.vm.searchInfo.fileTypes = [1,2,3,4,5,6,7,8,9,10,11,12];
                }
                else {
                    $scope.vm.searchInfo.fileTypes = [1,2,3];
                }
                $scope.selectFileButton = "全不选";
            }
            else {
                $scope.vm.searchInfo.fileTypes = [];
                $scope.selectFileButton = "全选";
            }

        }
    }

    indexController.$inject = ['$scope',"alert", 'confirm', '$translate', 'cActions','notify','$location','cRoutes', 'cookieService','biReportService'];

    return indexController;
});