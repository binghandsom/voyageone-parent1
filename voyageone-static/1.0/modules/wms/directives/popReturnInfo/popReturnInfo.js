/**
 * Created by sky on 2015/4/30
 */

define([
    "modules/wms/wms.module",
    "components/directives/getFocus",
    "modules/wms/return/returnService",
    "modules/wms/directives/popReturnInfo/popReturnInfoService",
    "components/directives/enterClick",
    "components/directives/dialogs/dialogs",
    "components/services/printService"
], function (wms) {
    wms.directive("popReturnInfo", [
        "$modal",
        "popReturnInfoService",
        "returnService",
        "vAlert",
        "printService",
        "wmsConstant",
        "$location",
        "userService",
        "$filter",
        popReturnInfo
    ]);

    function popReturnInfo($modal, popReturnInfoService, returnService, alert, printService, wmsConstant, $location, userService, $filter) {

        return {
            restrict: "A",
            link: link
        };

        function link($scope, elem, attr) {

            var orderNumber, m;

            //$scope.search = [];

            //用户是用barCode扫描还是用reservationId
            var scanCondition = $scope.scanCondition = [];

            //点击scan按钮或者捕获enter按键后触发事件
            elem.click(function () {
                //每次重新打开窗口的时候清空扫描条件
                clearScanCondition();
                $scope.returnInfo = [];
                //若是新建session的时候用户必须选择仓库
                if (!$scope.search.sessionId && !$scope.search.storeId) {
                    alert("WMS_PICKUP_STORE_UN_VALID");
                    return;
                }
                orderNumber = attr.popReturnInfo;
                //用户必须输入订单号
                if (!orderNumber) {
                    alert("WMS_MUST_ENTER_ORDERNUMBER");
                    return;
                }
                //获取订单实例，若获取成功的话则创建窗口
                getOrderInfoByOrdNum();
            });

            //产品扫描，默认扫描barCode
            $scope.checkProduct = function () {
                var arr = $scope.orderInfo,
                    searchCondition = scanCondition.resId,
                    object = {};
                if (searchCondition) {
                    object = _.find(arr, function (obj) {
                        return obj.res_id == searchCondition && !obj.selectClass;
                    });
                } else {
                    searchCondition = scanCondition.barCode;
                    object = _.find(arr, function (obj) {
                        return obj.barCode == searchCondition && !obj.selectClass || obj.Upc == searchCondition && !obj.selectClass;
                    });
                }
                //清空扫描条件
                scanCondition.resId = "";
                scanCondition.barCode = "";

                //TODO 若是扫描的barCode或者resId未在该订单里面，那么查询数据库弹出确认框询问否添加到return列表里面
                //TODO 若是确认添加则将查询出来的对象赋值给选中的object
                if(!object){
                    alert("WMS_CAN_NOT_FIND_OBJECT");
                    return;
                }
                if (checkItemStatus(object)) {
                    object.selectClass = "wms_bgcolor";
                    object.returnButtonIsHidden = 'hidden';
                    object.cancelButtonIsHidden = 'dishidden';
                } else {
                    alert("The product you scan may be added by other session or not be closeDay!");
                }
            };

            //点击cancel按钮修改选中眼色为非选中，return和cancel按钮切换显示
            $scope.changeButtonStatus = function (orderInfo) {
                if (orderInfo.returnButtonIsHidden === 'hidden') {
                    orderInfo.returnButtonIsHidden = 'dishidden';
                    orderInfo.cancelButtonIsHidden = 'hidden';
                    orderInfo.selectClass = "";
                } else {
                    orderInfo.returnButtonIsHidden = 'hidden';
                    orderInfo.cancelButtonIsHidden = 'dishidden';
                    orderInfo.selectClass = "wms_bgcolor";
                }
            };

            //点击弹出框的Add按钮
            $scope.addReturnInfo = function () {
                var beSelectObj = _.filter($scope.orderInfo, function (obj) {
                    return obj.selectClass == "wms_bgcolor";
                });

                if (beSelectObj.length == 0) {
                    alert("No object be selected");
                    return;
                }

                //给选中的元素添加用户设置的值
                function setValue(returnSessionId) {
                    _.each(beSelectObj, function (returnInfo) {
                        returnInfo.return_session_id = returnSessionId;
                        returnInfo.reason = $scope.returnInfo.reason;
                        returnInfo.received_from = $scope.returnInfo.received_from;
                        returnInfo.tracking_no = $scope.returnInfo.tracking_no;
                        returnInfo.notes = $scope.returnInfo.notes;
                        returnInfo.store_id = $scope.search.storeId;
                    });
                }

                //将数据插入return表，重新初始化页面，展示新添的数据
                //1、如果是newSession则调用函数创建Session,orderInfoList[0]里面包含一些创建session有用的信息
                if (!$scope.search.sessionId) {
                    popReturnInfoService.createReturnSession($scope.orderInfo[0]).then(function (response) {
                        //$scope.search.sessionId = response.data.returnSessionId;
                        setValue(response.data.returnSessionId);
                        insertReturnInfo(beSelectObj);
                        m.close();
                    });
                } else {
                    setValue($scope.search.sessionId);
                    insertReturnInfo(beSelectObj);
                    m.close();
                }
            };

            //清空扫描条件
            function clearScanCondition(){
                scanCondition.resId = "";
                scanCondition.barCode = "";
            }

            //将选中的信息插入到数据库
            function insertReturnInfo(beSelectObj) {
                popReturnInfoService.insertReturnInfo(beSelectObj).then(function () {
                    returnService.doNewSessionInit({}).then(function (response) {
                        //子scope能看到父scope的东西，但是无法改变，子层的socpe只是父层的一个copy
                        $scope.$parent.processingSessionList = response.data.processingSessionList;
                        returnService.doGetSessionInfo({"id" : $scope.search.sessionId}, $scope).then(function (response) {
                            $scope.vm.returnList = response.data.returnList;
                            //打印当前被添加项
                            popReturnInfoService.getReturnType().then(function (res){
                                $scope.search.sessionId = beSelectObj[0].return_session_id;
                                if(res.data.returnType != "1"){
                                    doPrint(beSelectObj);
                                }
                            });
                        });
                    });
                });
            }

            //打印
            function doPrint(v, label_type){
                //获得当前时间
                var dateFilter = $filter("date");
                var now = new Date();
                var nowStr = dateFilter(now, "yyyy-MM-dd HH:mm:ss");
                v.forEach(function(e){
                    var data = [{"codition" : e.condition,
                                 "modifier" : userService.getUserName(),
                                 "modified" : nowStr,
                                 "location" : e.location_name,
                                 "barcode" : e.barCode,
                                 "sku" : e.sku,
                                 "size" : e.size,
                                 "product" : e.product_name,
                                 "notes" : e.notes,
                                 "label_type" : e.label_type}];
                    var jsonData = JSON.stringify(data);
                    printService.doPrint(wmsConstant.print.business.ReturnLabel, wmsConstant.print.hardware_key.Print_Return, jsonData);
                });
            }

            //扫描订单号获取订单实例
            function getOrderInfoByOrdNum() {
                popReturnInfoService.getOrderInfoByOrdNo(orderNumber).then(function (response) {
                    var orderInfoList = response.data.orderInfo;
                    //找不到对应的订单信息提示
                    if (orderInfoList.length == 0) {
                        alert("WMS_NONEXISTENT_ORDERNUMBER");
                        return;
                    }
                    m = createModal();
                    _.each(orderInfoList, function (obj) {
                        //已经被session添加的物品,或者close_day_flg为0(未closeDay)的物品,则不让操作
                        if (checkItemStatus(obj)) {
                            obj.returnButtonIsHidden = 'dishidden';
                            obj.cancelButtonIsHidden = 'hidden';
                        } else {
                            obj.returnButtonIsHidden = 'hidden';
                            obj.cancelButtonIsHidden = 'hidden';
                            obj.selectClass = "wms_bgcolorGray";
                        }
                        obj.store_id = $scope.search.storeId;
                    });
                    $scope.orderInfo = orderInfoList;
                    //根据storeID设置快递公司下拉框的值
                    popReturnInfoService.getReceivedFromItemByStoreId($scope.search.storeId, $scope).then(function (response){
                       $scope.returnExpress = response.data.receivedFromList;
                        //默认不设置快递公司
                        //$scope.returnInfo.received_from = $scope.returnExpress[0].id;
                        //重置returnReason
                       $scope.returnInfo.reason = $scope.returnReason[0].id;
                    });
                });
            }

            //检查物品否满足return的条件,（未被session添加的sessionId为-1,close_day_flg 为 0 的为未closeDay的）
            function checkItemStatus(obj) {
                return !(obj.return_session_id != "-1" || obj.close_day_flg == "0");
            }

            //创建弹出框
            function createModal() {
                return m = $modal.open({
                    size: 'lg',
                    scope: $scope,
                    templateUrl: "modules/wms/directives/popReturnInfo/popReturnInfo.tpl.html",
                    controller: ["$scope", function (scope) {
                        scope.orderNumber = orderNumber;
                        scope.close = function(){
                            m.dismiss('close');
                        };
                    }]
                });
            }
        }
    }
});