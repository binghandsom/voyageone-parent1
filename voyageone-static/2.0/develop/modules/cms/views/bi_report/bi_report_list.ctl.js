define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (_) {

    function indexController($scope, alert, confirm, $translate, cActions, notify, $location, cRoutes, cookieService, biReportService, $filter) {
        $scope.datePicker = [];
        $scope.AllfileShow = true;
        $scope.selectFileButton = "全选";
        $scope.fromMinDate = new Date("2015-01-01");
        $scope.fromMaxDate = new Date();
        $scope.toMinDate = new Date("2015-01-01");
        $scope.toMaxDate = new Date();
        $scope.tips=null;
        $scope.vm = {
            downloadTaskList: [],
            searchInfo: {
                channels: [],
                channelCodeList: [],
                fileTypes: [],
                fileTypesTempt: [],
                endDate: undefined,
                staDate: undefined
            },
            "message": "successfully!",
            status: {open: true}
        };
        $scope.fileTypesSource = [
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

        $scope.toDate = function (date) {
            if (!date) return date;
            if (date instanceof Date) return date;
            return new Date(date);
        };

        $scope.getMinDate = function (firstDate, secondDate) {
            if (!firstDate && !secondDate) return new Date();
            if (!firstDate) return secondDate;
            if (!secondDate) return firstDate;
            if (firstDate < secondDate)
                return firstDate;
            return secondDate;
        };

        $scope.getMaxDate = function (firstDate, secondDate) {
            if (!firstDate && !secondDate) return new Date();
            if (!firstDate) return secondDate;
            if (!secondDate) return firstDate;
            if (firstDate < secondDate)
                return secondDate;
            return firstDate;
        };

        $scope.setDateFilterConfig = function () {
        };
        $scope.clear = function () {
            $scope.vm.searchInfo = {};
            $scope.vm.searchInfo.channelCodeList = [];
        };
        $scope.initialize = function () {
            biReportService.init().then(function () {
                $scope.search();
            });
            biReportService.get_channel_list().then(function (res) {
                $scope.channels = res.data;
            })
        };
        //分页
        $scope.dataPageOption = {curr: 1, total: 0, fetch: goPage};
        $scope.search = function () {
            var pageParameter = getPageParameter();
            $scope.dataPageOption.setPageIndex(1);//查询第一页
            //获取页数量,这里需要跨域请求
            biReportService.getCount(pageParameter).then(function (res) {
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
            var files = pageParameter.fileTypesTempt;
            var finalFiles = [];
            for (var i = 0; i < files.length; i++) {
                if ($scope.AllfileShow) {
                    if (false != files[i]  && null != files[i] && undefined != files[i]) {
                        finalFiles.push(files[i]);
                    }
                } else {
                    if (false != files[i]  && null != files[i] && undefined != files[i] &&  files[i] < 4) {
                        finalFiles.push(files[i]);
                    }
                }
            }
            pageParameter.fileTypes = finalFiles;
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
                    "taskId": item.id,
                    "fileName": item.fileName,
                    "exportPath": item.filePath
                },
                _exportFileCallback);

        };
        $scope.createXlsFileTask = function () {
            var parameter = getPageParameter();
            if (parameter.fileTypes.length == 0) {
                alert("请选择文件类型");
                return;
            }
            if (!(validateDate(parameter.staDate) && validateDate(parameter.endDate))) {
                alert("请选择时间！");
                return;
            }
            if (parameter.channelCodeList.length == 0) {
                alert("请选择渠道");
                return;
            }
            confirm("确认生成此文件?").then(function () {
                // var index = _.indexOf($scope.vm.promotionList, data);
                console.log(parameter);
                biReportService.createXlsFileTask(parameter).then(function (res) {
                    var ecd = res.data.ecd;
                    switch (ecd) {
                        case "0":
                            alert($translate.instant('TXT_BI_DOWNLOAD_LIST_FILE_CREATING'));
                            break;
                        case "4400":
                            alert($translate.instant('TXT_BI_DOWNLOAD_LIST_NO_FILEPATH') + res.data.ecd);
                            break;
                        case "4100":
                            alert($translate.instant('TXT_BI_DOWNLOAD_LIST_NO_CONNECTION') + res.data.ecd);
                            break;
                        default:
                            alert($translate.instant('TXT_BI_DOWNLOAD_LIST_ERROR_CODE') + res.data.ecd);
                            break;
                    }
                    $scope.search();
                });
            })
        };
        function validateDate(val) {
            var datePattern = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))-(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$/;
            return datePattern.test(val);
        }

        $scope.del = function (data) {
            confirm($translate.instant('TXT_BI_DOWNLOAD_LIST_SURE_TO_DELETE')).then(function () {
                // var index = _.indexOf($scope.vm.promotionList, data);
                biReportService.deleteTask(data.id).then(function (res) {
                    alert("删除成功！");
                    if (res.data.result) {
                        $scope.search();
                    }
                });
            })
        };
        $scope.selectAllFileTypes = function () {
            if ($scope.selectFileButton == "全选") {
                if ($scope.AllfileShow == true) {
                    $scope.vm.searchInfo.fileTypesTempt = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
                }
                else {
                    $scope.vm.searchInfo.fileTypesTempt = [1, 2, 3];
                }
                $scope.selectFileButton = "全不选";
            }
            else {
                $scope.vm.searchInfo.fileTypesTempt = [];
                $scope.selectFileButton = "全选";
            }

        };
        function compareDate(x, sDate, eDate) {
            if (!(sDate && eDate && x)) return true;
            var compareSDate = getXMonthChange(x, sDate, eDate);
            var compareEDate = stringToDate($scope.vm.searchInfo.endDate);
            return compareEDate < compareSDate;
        }

        function dateToString(date) {
            if (date && date instanceof Date) {
                return $filter("date")(date, "yyyy-MM-dd");
            } else {
                if (date)    return date;
                return "";
            }
        }

        function stringToDate(date) {
            if (date) {
                return new Date(date);
            } else {
                return null;
            }
        }

        function getXMonthChange(xMonth, val) {
            var array = val.split("-");
            var month = parseInt(array[1]) + xMonth;
            var date = new Date();
            var realMonth, realYear;
            var realDay = array [2];
            if (month < 1) {
                realMonth = month + 12;
                realYear = parseInt(array[0]) - 1;
                date.setYear(realYear);
                date.setMonth(realMonth - 1);
                date.setDate(realDay);
                return date;
            } else if (month > 12) {
                realMonth = month - 12;
                realYear = parseInt(array[0]) + 1;
                date.setYear(realYear);
                date.setMonth(realMonth - 1);
                date.setDate(realDay);
                return date;
            } else {
                realMonth = month;
                realYear = array[0];
                date.setYear(realYear);
                date.setMonth(realMonth - 1);
                date.setDate(realDay);
                return date;
            }
        }
        $scope.fileTypeViewManager = function () {
            //获取结束时间和开始时间
            var sDate = $scope.vm.searchInfo.staDate;
            var eDate = $scope.vm.searchInfo.endDate;
            var channelsNumber = $scope.vm.searchInfo.channelCodeList.length;
            var timeMtThreeMonth = compareDate(3, sDate, eDate);
            if ((channelsNumber == 1 && timeMtThreeMonth == true)|| channelsNumber ==0) {
                $scope.AllfileShow = true;
                $scope.tips = null;
            } else{
                $scope.AllfileShow = false;
                $scope.tips=$translate.instant('TXT_BI_DOWNLOAD_LIST_FILE_MAYBE_TOO_BIG_CONTINUE');
            }
            var tempEndDate = $scope.toDate($scope.vm.searchInfo.endDate);
            var tempStaDate = $scope.toDate($scope.vm.searchInfo.staDate);
            $scope.fromMaxDate = $scope.getMinDate(tempEndDate, new Date());
            $scope.toMinDate = $scope.getMaxDate(tempStaDate, new Date("2015-01-01"));
        }
    }

    indexController.$inject = ['$scope', "alert", 'confirm', '$translate', 'cActions', 'notify', '$location', 'cRoutes', 'cookieService', 'biReportService', '$filter'];

    return indexController;
});