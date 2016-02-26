/**
 * @User: Jerry
 * @Date: 2016-02-23 15:39:01
 * @Version: 0.0.1
 * @modify jerry 20160223
 */

define([
    "modules/wms/wms.module",
    "modules/wms/directives/popChangeReturn/popChangeReturnService",
    "components/directives/dialogs/dialogs"
], function (wms) {

    wms.factory("wmsChangeReturn", [
        "$modal",
        "vAlert",
        "notify",
        "popChangeReturnService",
        function ($modal, alert, notify, popChangeReturnService) {

            //弹出框对象
            var m;

            return function($scope, returnInfo) {
                m = $modal.open({
                    size: 'dialog',
                    scope: $scope,
                    templateUrl: "modules/wms/directives/popChangeReturn/popChangeReturn.tpl.html",
                    title: "Change Return",
                    controller: ["$scope", modalController]
                });

                //弹出框控制器
                function modalController(scope) {

                    //页面变量Map
                    var vm = scope.vm = {};

                    var viewCtl = $scope.viewCtl = {};

                    //检索条件初始化
                    vm.change = {
                        store: "",
                        sku: "",
                        status: "",
                        notes: ""
                    };

                    //关闭窗口
                    scope.close = close;

                    //点击查询按钮
                    scope.doChange = doChange;

                    returnInfo = stringToObject(returnInfo);

                    // 备注初期化
                    vm.change.notes = returnInfo.notes;

                    // 根据检索条件查询相关记录
                    function doChange() {

                        if (!vm.change.notes) {
                            alert("Please Input Notes.");
                            return;
                        }

                        popChangeReturnService.change(
                            returnInfo.return_id,
                            returnInfo.changeKind,
                            vm.change.notes
                        )
                            .then(function (res) {
                                $scope.returnList.selected.notes = res.data.notes;
                                $scope.returnList.selected.modified = res.data.modified;
                                $scope.returnList.selected.modified_local = res.data.modified_local;

                                var op={"message": returnInfo.res_id + " update success !","status":"success"};
                                notify(op);

                                m.close();
                            });

                    }

                    function close() {
                        m.close();
                    }

                    function stringToObject(returnInfo) {
                        if (!_.isObject(returnInfo)) {
                            return JSON.parse(returnInfo)
                        } else {
                            return returnInfo;
                        }
                    }
                }
            };
        }
    ]).directive("popWmsChangeReturn", [
        "wmsChangeReturn",
        popWmsChangeReturn
    ]);

    function popWmsChangeReturn(wmsChangeReturn) {
        return {
            restrict: "A",
            link: link
        };

        function link($scope, elem, attr) {
            //点击插件属性，标记的页面元素时候触发事件
            elem.click(function () {
                wmsChangeReturn($scope, attr.popWmsChangeReturn);
            });
        }
    }

});