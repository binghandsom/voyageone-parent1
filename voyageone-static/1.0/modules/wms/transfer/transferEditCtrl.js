/**
 * @User: Jonas
 * @Date: 2015-3-26 13:57:58
 * @Version: 1.0.0
 */

define([
    "modules/wms/wms.module",
    "modules/wms/transfer/transferService",
    "modules/wms/directives/popInputClientSku/popInputClientSku",
    "components/directives/dialogs/dialogs",
    "components/directives/enterClick"
], function (wms) {
    wms.controller("transferEditCtrl", [
        "$scope",
        "$routeParams",
        "$location",
        "transferService",
        "vConfirm",
        "$timeout",
        "vAlert",
        "ngDialog",
        "notify",
        "$window",
        "wmsInputClientSku",
        transferEditCtrl
    ])
        .filter("statusName", function () {
            return function (input) {
                return input == 0 ? "Open" : "Close";
            }
        });

    function transferEditCtrl($scope,
                              $routeParams,
                              $location,
                              transferService,
                              confirm,
                              $timeout,
                              alert,
                              ngDialog,
                              notify,
                              $window,
                              wmsInputClientSku) {
        var channelStores = [];
        var companyStores = [];
        var storesTo = [];

        var vm = $scope.vm = {};

        var addTransferType = keyToNum($routeParams.type);

        // 参数检查
        if (vm.isAddMode && !addTransferType) {
            alert("WMS_ALERT_TRANSFER_EDIT_NO_TYPE").then(backToList);
            return;
        }

        vm.isAddMode = ( $location.path().indexOf("/wms/transfer/add") === 0 );

        vm.transfer_map_target = "";

        vm.transfer_client_shipments = "";

        // 当前的 Transfer
        vm.transfer = {};

        // 正在编辑的 Detail
        vm.package = {};

        // 当前 Transfer 下的 Details
        vm.packages = [];

        // Items 编辑页面，表格内容
        vm.packageItems = [];

        // select 的各种数据源
        vm.selects = {stores: {}};

        // tips 提示数据源
        vm.btnCtrl = {submit:false, save:false};

        // package 编辑中的扫描输入
        vm.packageInput = {name: ""};

        // item 编辑块里面的扫描输入框
        vm.itemInput = {num: 1, code: ""};

        //dorpDown显示控制
        vm.isColose = [];

        //dorpDown显示控制
        vm.isReOpen = [];

        $scope.getCurrTransferName = getCurrTransferName;
        $scope.isPurchaseOrder = isPurchaseOrder;
        $scope.isWithdrawal = isWithdrawal;
        $scope.isTransferClosed = isTransferClosed;
        $scope.isTransferEditable = isTransferEditable;
        $scope.isEditingPackages = isEditingPackages;
        $scope.isEditingPackage = isEditingPackage;
        $scope.backToList = backToList;
        $scope.fromStores = fromStores;
        $scope.toStores = toStores;
        $scope.saveTransfer = saveTransfer;
        $scope.submitTransfer = submitTransfer;
        $scope.deleteTransfer = deleteTransfer;
        $scope.acceptPackage = acceptPackage;
        $scope.printPackage = printPackage;
        $scope.closePackage = closePackage;
        $scope.acceptItem = acceptItem;
        $scope.deleteItem = deleteItem;
        $scope.cancel = cancel;
        $scope.isPackageClosed = isPackageClosed;
        $scope.isTransferIn = isTransferIn;
        $scope.isMapFieldEditable = isMapFieldEditable;
        $scope.compare = compare;
        $scope.editPackage = editPackage;
        $scope.deletePackage = deletePackage;
        $scope.reOpenPackage = reOpenPackage;
        $scope.setVal = setVal;

        init();

        function setVal($index){
            vm.packages.selected = $index;
            vm.isColose = vm.packages[$index].package_status == "1" ? 1 : 0;
            vm.isReOpen = vm.isColose && vm.transfer.transfer_status == 0;
        }

        function init() {
            transferService.getConfigs($routeParams.id).then(function (res) {
                channelStores = res.data.storeList;
                companyStores = res.data.companyStoreList;
                storesTo = res.data.companyStoreToList;
                vm.transfer_client_shipments = res.data.notMatchClientShipmentList;

                if (vm.isAddMode) {
                    vm.transfer = newTransfer();
                    isMapFieldEditable(true);
                    bindStoreSelects();
                } else {
                    reqTransfer($routeParams.id).then(function (res) {
                        vm.transfer = res.transfer;
                        vm.packages = res.packages;
                        vm.transfer_map_target = res.map_target;

                        isMapFieldEditable(!vm.transfer_map_target);
                        bindStoreSelects();
                    });
                }
            });
        }

        // ------------ 绑定方法 ------------ //

        function getCurrTransferName() {
            switch (vm.transfer.transfer_type) {
                case "1":
                    return "Transfer In";
                case "2":
                    return "Transfer Out";
                case "3":
                    return "Purchase Order";
                case "4":
                    return "Withdrawal";
                case "5":
                    return "Refresh";
                default :
                    return null;
            }
        }

        function isPurchaseOrder() {
            return vm.transfer.transfer_type == 3;
        }

        function isWithdrawal() {
            return vm.transfer.transfer_type == 4;
        }

        function isTransferClosed() {
            return vm.transfer.transfer_status == 1;
        }

        function isTransferEditable() {
            return vm.isAddMode;
        }

        function isEditingPackages() {
            return !vm.isAddMode;
        }

        function isEditingPackage() {
            if (vm._isEditingPackage == null) vm._isEditingPackage = false;

            if (arguments.length > 0) vm._isEditingPackage = !!arguments[0];

            vm.package.transfer_package_qty = calPackageQty();

            return vm._isEditingPackage;
        }

        function backToList() {
            $timeout(function () {
                $location.path("/wms/transfer/list");
            }, 200);
        }

        function fromStores() {
            if (arguments.length > 0) vm.selects.stores.from = arguments[0];

            return vm.selects.stores.from;
        }

        function toStores() {
            if (arguments.length > 0) vm.selects.stores.to = arguments[0];

            return vm.selects.stores.to;
        }

        function saveTransfer() {
            if (!checkTransferInput()) {
                return;
            }

            if (isTransferClosed()) {
                alert("WMS_ALERT_TRANSFER_EDIT_CLOSED");
                return;
            }

            reqSaveTransfer();
        }

        function deleteTransfer() {
            if (isTransferClosed()) {
                alert("WMS_ALERT_TRANSFER_EDIT_CLOSED");
                return;
            }

            confirm({id:"WMS_ALERT_TRANSFER_DELETE", values:vm.transfer}).then(reqDeleteTransfer);
        }

        function submitTransfer() {
            if (!checkTransferInput()) {
                return;
            }

            if (isTransferClosed()) {
                alert("WMS_ALERT_TRANSFER_EDIT_CLOSED");
                return;
            }

            reqSubmitTransfer();
        }

        function acceptPackage() {
            if (isTransferClosed()) return;

            if (isEditingPackage()) return;

            var name = vm.packageInput.name;
            var pkg;

            if (name) {
                pkg = findPackage(name);
            }

            if (pkg) {
                editingPackage(pkg);
            } else {
                reqPackage(vm.transfer.transfer_id, name)
                    .then(editingPackage)
            }
        }

        function printPackage() {
            if (vm.transfer.transfer_id < 0) return;

            $window.open("./wms/transfer/list/download?transfer_id="+vm.transfer.transfer_id, '_blank');
        }

        function editPackage() {
            var index = vm.packages.selected;
            if (index < 0 || isEditingPackage()) return;

            editingPackage(vm.packages[index]);
        }

        function closePackage() {
            reqClosePackage(vm.package);
        }

        function acceptItem() {
            var num = vm.itemInput.num;
            var code = vm.itemInput.code;

            // 在转存到变量后，立刻重置，不论对错
            vm.itemInput.num = 1;
            vm.itemInput.code = "";

            // 重置焦点到 code 输入框
            angular.element("#transfer-item-barcode").focus();

            if (!code) {
                notify("WMS_TRANSFER_EDIT_NO_CODE");
                return;
            }

            if (num < 1) {
                notify("WMS_TRANSFER_EDIT_NUM_VALID");
                return;
            }

            transferService

                // 尝试将扫描的商品，实时反映到后台数据库中
                .tryAddItem(vm.package.transfer_package_id, code, num)

                .then(function (sku) {

                    // 没有内容，则放弃继续
                    if (!sku) return;

                    if(sku == 'popupInput') {
                        wmsInputClientSku($scope,vm.package.transfer_package_id, code, num);
                        return;
                    }

                    // 如果返回内容，有值。那么就反映到画面中
                    vm.packageItems.unshift({
                        transfer_sku: sku,
                        transfer_barcode: code,
                        transfer_qty: num
                    });

                });
        }

        function deleteItem(index) {
            var item = vm.packageItems[index];

            if (!item) {
                alert("WMS_TRANSFER_EDIT_NO_ITEM");
                return;
            }

            reqDeleteItem(item).then(function () {
                vm.packageItems.splice(index, 1);
            });
        }

        function cancel() {
            if (isTransferClosed()) {
                backToList();
                return;
            }

            confirm("WMS_TRANSFER_EDIT_CANCEL")
                .then(backToList);
        }

        function isPackageClosed(pkg) {
            pkg = pkg || vm.package;

            return pkg.package_status === '1';
        }

        function isTransferIn() {
            return vm.transfer.transfer_type == 1;
        }

        function isMapFieldEditable(bool) {
            if (_.isBoolean(bool)) vm.$$mapFieldEditable = bool;
            if (_.isUndefined(vm.$$mapFieldEditable)) vm.$$mapFieldEditable = true;

            return vm.$$mapFieldEditable;
        }

        function compare() {
            reqCompare();
        }

        // ------------ 数据处理方法 ------------ //

        function reqCompare() {
            transferService.doCompare(vm.transfer.transfer_id)

                .then(showCompare);
        }

        function deletePackage() {
            var index = vm.packages.selected;
            if (index < 0) return;

            var selected = vm.packages[index];

            if (isPackageClosed(selected)) {
                alert("WMS_TRANSFER_EDIT_PKG_CLOSED");
                return;
            }

            confirm({id:"WMS_TRANSFER_EDIT_PKG_DEL",values:selected})
                .then(function () {
                    transferService

                        .deletePackage(selected.transfer_package_id, selected.modified)

                        .then(function (isSuccess) {
                            if (isSuccess) {
                                vm.packages.splice(index, 1);
                            } else {
                                alert("WMS_NOTIFY_OP_FAIL");
                            }
                        });
                });
        }

        function reOpenPackage() {
            var index = vm.packages.selected;
            if (index < 0) return;

            var selected = vm.packages[index];

            if (isTransferClosed()) {
                alert("WMS_ALERT_TRANSFER_EDIT_CLOSED");
                return;
            }

            confirm({id:"WMS_TRANSFER_EDIT_PKG_REOPEN",values:selected})
                .then(function () {
                    transferService

                        .reOpenPackage(selected.transfer_package_id, selected.modified)

                        .then(function (isSuccess) {
                            if (isSuccess) {
                                vm.packages[index].package_status = "0";
                            } else {
                                alert("WMS_NOTIFY_OP_FAIL");
                            }
                        });
                });
        }

        function reqSaveTransfer() {
            if (vm.btnCtrl.save) {
                return;
            }

            vm.btnCtrl.save = true;

            return transferService.saveTransfer(vm.transfer, vm.transfer_map_target)

                .then(onSuccess, revert);

            function onSuccess(transfer) {
                if (transfer) {

                    notify({message:"WMS_NOTIFY_OP_SUCCESS", status:"success"});

                    vm.transfer = transfer;
                    if (vm.isAddMode) {
                        vm.isAddMode = false;
                    }

                    isMapFieldEditable(false);
                }
                revert();
            }

            function revert() {
                $timeout(function () {
                    vm.btnCtrl.save = false;
                }, 2000);
            }
        }

        function reqSubmitTransfer() {
            if (vm.btnCtrl.submit) {
                return;
            }

            vm.btnCtrl.submit = true;

            return transferService.submitTransfer(vm.transfer, vm.transfer_map_target)

                .then(onSuccess, onFail);

            function onSuccess(transfer) {

                vm.transfer = transfer;

                notify({message:"WMS_NOTIFY_OP_SUCCESS", status:"success"});

                isMapFieldEditable(false);

                revert();
            }

            function onFail(res) {
                // 如果失败原因是内容比对失败了。
                // 显示比对的具体内容
                if (res.messageCode == "600014") compare();

                revert();
            }

            function revert() {
                $timeout(function () {
                    vm.btnCtrl.submit = false;
                }, 2000);
            }
        }

        function reqDeleteTransfer() {
            return transferService.deleteTransfer(vm.transfer.transfer_id, vm.transfer.modified)

                .then(function (isSuccess) {
                    if (isSuccess) {
                        backToList();
                    } else {
                        alert("WMS_NOTIFY_OP_FAIL");
                    }
                });
        }

        function reqTransfer(id) {
            return transferService.getTransfer(id);
        }

        function reqPackage(transfer_id, package_name) {
            return transferService

                .createPackage(transfer_id, package_name)

                .then(function (pkg) {
                    vm.packages.unshift(pkg);
                    // 还原输入框内的名称
                    vm.packageInput.name = "";

                    return pkg;
                });
        }

        function reqPackageItems(transfer_package_id) {
            return transferService.selectPackageItems(transfer_package_id)

                .then(function (itemsArr) {
                    vm.packageItems = itemsArr;
                })
        }

        function reqClosePackage(pkg) {
            return transferService

                .doClosePackage(pkg.transfer_package_id, pkg.modified)

                .then(function (isSuccess) {
                    if (isSuccess) {
                        vm.package.package_status = "1";
                        isEditingPackage(false);
                        $scope.$apply();
                    } else {
                        alert("WMS_NOTIFY_OP_FAIL");
                    }
                });
        }

        function reqDeleteItem(item) {
            return transferService.doDeleteItem(vm.package.transfer_package_id, item.transfer_barcode, item.transfer_qty);
        }

        // ------------ 辅助方法 ------------ //

        function editingPackage(pkg) {
            // 此处不判断 package 的关闭状态，因为需要提供 close 状态下的查看功能

            vm.package = pkg;
            isEditingPackage(true);
            reqPackageItems(pkg.transfer_package_id);
        }

        function findPackage(name) {
            return _.find(vm.packages, function (obj) {
                return obj.transfer_package_name == name;
            });
        }

        function bindStoreSelects() {

            // 根据类型，绑定到执行下拉列表
            switch (vm.transfer.transfer_type) {
                case "1": // in
                    fromStores(channelStores);
                    toStores(companyStores);
                    break;
                case "2": // out
                    fromStores(companyStores);
                    toStores(storesTo);
                    break;
                case "3": // po
                    fromStores(companyStores);
                    toStores(companyStores);
                    break;
                case "4": // return
                    fromStores(companyStores);
                    toStores(companyStores);
                    break;
                default:
                    alert("WMS_TRANSFER_EDIT_NO_TYPE");
                    return;
            }

            // 在绑定之后，如果各仓库下拉的内容，只有一个。则默认选中
            if (!vm.transfer) return;

            if (fromStores().length === 1) vm.transfer.transfer_from_store = fromStores()[0].store_id;

            if (toStores().length === 1) vm.transfer.transfer_to_store = toStores()[0].store_id;

        }

        /**
         * 根据新增类型创建 Transfer
         */
        function newTransfer() {
            return {
                transfer_type: addTransferType,
                po_number: "", // 等待用户输入
                transfer_name: "",
                transfer_from_store: "",
                transfer_to_store: "",
                comment: "",
                client_shipment_id : 0
            };
        }

        function keyToNum(typeKey) {
            typeKey = typeKey ? typeKey.toLowerCase() : "";
            switch (typeKey) {
                case "in":
                    return "1";
                case "out":
                    return "2";
                case "po":
                    return "3";
                case "re":
                    return "4";
                default :
                    return null;
            }
        }

        function isEmpty(str) {
            return str == null || ((typeof str) === "string" && !(str.trim()));
        }

        function checkTransferInput() {
            var currTransfer = vm.transfer;

            // transfer name is empty
            if (!vm.isAddMode && isEmpty(currTransfer.transfer_name)) {
                alert("WMS_TRANSFER_EDIT_NAME_VALID");
                return false;
            }

            // mapping transfer out is empty
            if (isTransferIn() && !vm.transfer_map_target) {
                alert("WMS_TRANSFER_EDIT_MAP_VALID");
                return false;
            }

            // transfer po_number is empty
            if (isPurchaseOrder() && isEmpty(currTransfer.po_number)) {
                alert("WMS_TRANSFER_EDIT_PO_VALID");
                return false;
            }

            // transfer from is empty
            if (!isPurchaseOrder() && isEmpty(currTransfer.transfer_from_store)) {
                alert("WMS_TRANSFER_EDIT_FROM_VALID");
                return false;
            }

            // transfer to is empty
            if (!isWithdrawal() && isEmpty(currTransfer.transfer_to_store)) {
                alert("WMS_TRANSFER_EDIT_TO_VALID");
                return false;
            }

            return true;
        }

        function showCompare(items) {
            items.inItems = _.sortBy(items.inItems, function (i) {
                return i.transfer_sku;
            });
            items.outItems = _.sortBy(items.outItems, function (i) {
                return i.transfer_sku;
            });

            ngDialog.open({
                template: "modules/wms/transfer/edit.compare.tpl.html",
                controller: ["$scope", function(scope) {
                    scope.inName = vm.transfer.transfer_name;
                    scope.outName = vm.transfer_map_target;
                    scope.items = items;
                }]
            });
        }

        function calPackageQty() {

            var packageQty = 0;
            for (i = 0; i < vm.packageItems.length; i++) {
                packageQty = packageQty + parseInt(vm.packageItems[i].transfer_qty);

            }
            return packageQty ;

        }
    }
});