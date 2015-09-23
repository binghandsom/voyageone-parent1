/**
 * @User: Jonas
 * @Date: 2015-04-09 14:07:14
 * @Version: 0.0.1
 */

define([
    "modules/wms/wms.module",
    "modules/wms/upc/upc.service",
    "components/directives/enterClick",
    "components/directives/dialogs/dialogs"
], function (wms) {
    return wms.controller("wmsUpcManageController", [
        "$scope",
        "userService",
        "upcService",
        "notify",
        "vAlert",
        wmsUpcManageController
    ]);

    function wmsUpcManageController($scope, userService, upcService, notify, alert) {
        var vm = $scope.vm = {};

        var copy, editModeWatcher;

        vm.mode = {
            create: 0,  // 创建模式。只能填写并创建商品，不能搜索，不能绑定
            edit: 1,    // 编辑模式。可编辑商品，可绑定，不可搜索
            bind: 2,    // 绑定模式。可绑定，不可编辑。不可搜索。默认创建完，会切换到绑定模式，在商品属性被修改时，会切换到编辑模式。非创建模式下，都可以编辑绑定
            search: 3,  // 搜索模式。不可编辑，不可绑定。
            curr: 3     // 初始模式为：搜索模式
        };

        vm.channels = [];

        vm.search = {channel: null, code: ""};

        vm.product = {channel_id: null, code: ""};

        vm.itemDetails = [];

        vm.itemDetailContexts = [];

        vm.sizes = [];

        vm.productTypes = [];

        $scope.init = init;
        $scope.getProduct = getProduct;
        $scope.currChannelName = currChannelName;
        $scope.changeSizes = changeSizes;
        $scope.editable = editable;
        $scope.saveProduct = saveProduct;
        $scope.saveItemDetail = saveItemDetail;
        $scope.resetItemDetail = resetItemDetail;
        $scope.needSaveItemDetail = needSaveItemDetail;
        $scope.isMode = isMode;
        $scope.isNotFoundSizes = isNotFoundSizes;
        $scope.isNoType = isNoType;
        $scope.closeProduct = closeProduct;

        function init() {

            var disposer = $scope.$watch(function() { return userService.getChannels(); }, function(newObj) {

                if (!newObj || !newObj.length) return;

                vm.channels = newObj;

                vm.search.channel = vm.product.channel_id = vm.channels[0].id;

                reqGetAllProductTypes();

                disposer();
            }, true);
        }

        function closeProduct() {
            // 重置所有内容
            vm.product = makeCopy({
                channel_id: vm.channels[0].id,
                code: ""
            });

            vm.search.code = "";

            vm.itemDetails = [];

            vm.sizes = [];

            vm.itemDetailContexts = [];

            // 切换回搜索模式
            vm.mode.curr = vm.mode.search;

            // 结束 watcher
            if (editModeWatcher) {
                editModeWatcher();
                editModeWatcher = null;
            }
        }

        function makeCopy(prod) {
            copy = _.extend({}, prod);
            return prod;
        }

        function isNoType() {
            if (isMode(vm.mode.create)) return true;

            return !vm.product.product_type_id;
        }

        function isNotFoundSizes() {
            if (isMode(vm.mode.create)) return false;

            return vm.product.product_type_id && (!vm.itemDetailContexts || !vm.itemDetailContexts.length);
        }

        function isMode() {
            return _.contains(arguments, vm.mode.curr);
        }

        function needSaveItemDetail(context) {
            return !context.context || context.bean.barcode != context.context;
        }

        function saveItemDetail(context) {
            reqSaveItemDetail(context.bean).then(function (bean) {
                notify({message: "WMS_NOTIFY_OP_SUCCESS", status: "success"});

                context.bean = bean;
                context.context = bean.barcode;
            });
        }

        function reqSaveItemDetail(bean) {
            return upcService.doSaveItemDetail(bean);
        }

        function resetItemDetail(context) {
            context.bean.barcode = context.context;
        }

        function saveProduct() {
            reqSaveProduct();
        }

        function editable() {
            return !!vm.product.code;
        }

        function changeSizes() {
            if (isMode(vm.mode.create)) return;

            getSizes();
        }

        function currChannelName() {
            var channel = _.find(vm.channels, function (c) {
                return c.id === vm.product.channel_id;
            });

            return channel ? channel.name : "";
        }

        function getProduct() {

            if (!vm.search.code) {
                notify({message: "WMS_NOTIFY_UPC_CODE", status: "warning"});
                return;
            }

            reqGetProduct().then(setProduct);
        }

        function reqGetProduct() {
            return upcService.doGetProduct(
                vm.search.channel,
                vm.search.code
            );
        }

        function getSizes() {
            var channel = vm.product.channel_id,
                type_id = vm.product.product_type_id;

            if (!type_id) {
                // 如果类型不存在，则清空现有的内容
                vm.itemDetailContexts = [];
                return;
            }

            reqGetSizes(channel, type_id);
        }

        function reqGetSizes(channel, type_id) {
            upcService.doGetAllSize(
                channel, type_id
            )
                .then(function (list) {
                    vm.sizes = list;

                    // 当获取全部信息后，转换信息到 ItemDetail
                    flushItemDetailGroup();
                });
        }

        function setProduct(res) {
            if (res) {
                vm.product = makeCopy(res.product);

                vm.itemDetails = res.itemDetails;

                // 已存在的商品，默认是进入绑定模式。当修改属性后，则变更为编辑模式
                vm.mode.curr = vm.mode.bind;

                getSizes();

                // 进入 bind 之后，创建 watcher
                // 通过 watcher 进入 edit
                createWatcher();
            } else {
                // 新增模式
                vm.product = makeCopy({
                    channel_id: vm.search.channel,
                    code: vm.search.code
                });

                vm.itemDetails = [];

                vm.sizes = [];

                vm.itemDetailContexts = [];

                // 切换模式
                vm.mode.curr = vm.mode.create;
            }
        }

        function reqGetAllProductTypes() {
            upcService.doGetAllProductTypes(vm.product.channel_id).then(function (list) {
                vm.productTypes = list;
            });
        }

        function reqSaveProduct() {

            if (!Number(vm.product.msrp)) {
                alert("WMS_ALERT_UPC_MSRP");
                return;
            }

            if (!Number(vm.product.product_type_id)) {
                alert("WMS_ALERT_UPC_PRODUCT");
                return;
            }

            if (vm.product.msrp < 1) {
                alert("WMS_ALERT_UPC_MSRPGREATTHEN0");
                return;
            }

            upcService.doSaveProduct(vm.product).then(function (bean) {

                vm.product = makeCopy(bean);

                // 如果是创建，需要刷新尺码视图
                if (isMode(vm.mode.create)) {

                    getSizes();

                    createWatcher();
                }

                // 保存成功后，切换模式！！！此处注意顺序
                vm.mode.curr = vm.mode.bind;

                notify({message: "WMS_NOTIFY_OP_SUCCESS", status: "success"});
            });
        }

        function flushItemDetailGroup() {

            // 预先根据所有 size，创建默认数据的数组。
            var contexts = _.map(vm.sizes, function (size) {
                var sku = vm.product.code + "-" + size;

                return {
                    bean: newItemDetail(size),
                    context: null,
                    out: false // 是否是其他尺码组的内容
                };
            });

            // 遍历查询到数据，填充到上一步创建的数组中。
            _.each(vm.itemDetails, function (itemDetail) {

                // 查找是否有相应的位置
                var context = _.find(contexts, function (context) {
                    return context.bean.sku == itemDetail.sku;
                });

                if (context) {
                    // 如果有位置，则替换数据
                    context.bean = itemDetail;
                    context.context = itemDetail.barcode;
                } else {
                    // 没有，则说明该 Item Detail 的数据，不属于当前所选的 Product Type 的尺码组里
                    // 那么，补充到数组里，并用特殊外观显示
                    contexts.push({
                        bean: itemDetail,
                        context: itemDetail.barcode,
                        out: true
                    });
                }
            });

            // 最后反映到画面上
            vm.itemDetailContexts = contexts;

            function newItemDetail(size) {
                return {
                    order_channel_id: vm.product.channel_id,
                    sku: vm.product.code + "-" + size,
                    itemcode: vm.product.code,
                    size: size
                };
            }

        }

        /**
         * 创建 watcher 监视 product，来切换左侧编辑块内，保存按钮的显示状态
         */
        function createWatcher() {

            function hasChange(prod) {
                var f = false;

                _.each(["name", "product_type_id", "color", "msrp"], function (key) {
                    // continue
                    if (prod[key] == null) return;
                    // break
                    if (prod[key] != copy[key]) {
                        f = true;
                        return false;
                    }
                });

                return f;
            }

            if (editModeWatcher) {
                editModeWatcher();
            }

            editModeWatcher = $scope.$watch("vm.product", function (a, b) {
                if (a == b) return;

                // 当检测到值变化时，就切换到编辑模式
                vm.mode.curr = hasChange(a) ? vm.mode.edit : vm.mode.bind;
            }, true);
        }
    }
});