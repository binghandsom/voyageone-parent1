/**
 * @author Jonas
 * @date 2015-04-28 11:44:59
 * @version 0.0.2
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {

    wms.service("transferService", [
        'ajaxService',
        'wmsActions',
        "$q", function (http, actions, $q) {

            /**
             * 根据条件搜索 transfer
             * @param po PO# 单据号
             * @param status 单据状态
             * @param from 单据日期
             * @param to 单据日期
             * @param page 当前页
             */
            this.searchTransfer = function (po, store, status, from, to, page, size) {
                return $q(function (resolve) {
                    http.ajaxPost({
                        po: po,
                        store: store,
                        status: status,
                        from: from,
                        to: to,
                        page: page,
                        size: size
                    }, actions.transfer.search)

                        .then(function (res) {
                            resolve({
                                rows: res.data.rows,
                                data: res.data.data
                            });
                        })
                });
            };

            /**
             * 删除 Transfer
             * @param transfer_id {number}
             * @param modified {*}
             */
            this.deleteTransfer = function (transfer_id, modified) {
                return http.ajaxPost({
                        transferId: transfer_id.toString(),
                        modified: modified
                    },
                    actions.transfer.delete)

                    .then(function (res) {
                        return res.data;
                    }, function (res) {
                        return res.resultInfo;
                    })
            };

            /**
             * 获取一个 Transfer 和它的所有 package
             * @resolve {transfer:object, packages:object[]}
             * @param transfer_id {number}
             */
            this.getTransfer = function (transfer_id) {
                if (!_.isNumber(transfer_id)) {
                    transfer_id = parseInt(transfer_id);
                }

                return $q(function (resolve) {
                    http.ajaxPost({
                        transferId: transfer_id.toString()
                    }, actions.transfer.get)
                        .then(function (res) {
                            resolve({
                                transfer: res.data.transfer,
                                packages: res.data.packages,
                                map_target: res.data.map_target
                            });
                        });
                });
            };

            /**
             * 保存 transfer 信息（不变更状态），Package 相关不从这里处理
             */
            this.saveTransfer = function (transfer, map_transfer_name) {
                return http.ajaxPost({
                        transfer: transfer,
                        context_name: map_transfer_name
                    },
                    actions.transfer.save)

                    .then(function (res) {
                        return res.data;
                    });
            };

            /**
             * 保存并提交（变更状态） transfer，Package 相关不从这里处理
             */
            this.submitTransfer = function (transfer, map_transfer_name) {
                return http.ajaxPost({
                        transfer: transfer,
                        context_name: map_transfer_name
                    },
                    actions.transfer.submit)

                    .then(function (res) {
                        return res.data;
                    });
            };

            /**
             * 获取所有的仓库 和 Type （Edit 页面）
             */
            this.getConfigs = function (transfer_id) {
                if (!_.isNumber(transfer_id)) {
                    transfer_id = parseInt(transfer_id);
                }

                return http.ajaxPost({
                    transferId: transfer_id.toString()
                }, actions.transfer.config.all);
            };

            /**
             * 获取 Package 的所有 Items
             * @param package_id
             */
            this.selectPackageItems = function (package_id) {
                return http.ajaxPost({
                    package_id: package_id.toString()
                }, actions.transfer.package.item.select).then(function (res) {
                    return res.data; // res.data = PackageItems[]
                });
            };

            /**
             * 请求服务器，尝试检索 Code 对应的 SKU，并添加指定数量的 Item 到 Package
             * @resolve {TransferItem}
             * @param package_id {string|number}
             * @param code {string}
             * @param num {string|number}
             * @returns {Promise}
             */
            this.tryAddItem = function (package_id, code, num) {
                if (!_.isNumber(package_id)) package_id = parseInt(package_id);

                if (!_.isNumber(num)) num = parseInt(num);

                return $q(function (resolve) {
                    http.ajaxPost({
                        package_id: package_id.toString(),
                        barcode: code,
                        num: num
                    }, actions.transfer.package.item.add).then(function (res) {
                        resolve(res.data);
                    });
                });
            };

            /**
             * 删除一个 Package
             * @resolve {boolean}
             * @param package_id {string}
             * @returns {Promise}
             */
            this.deletePackage = function (package_id, modified) {
                return $q(function (resolve) {
                    http.ajaxPost({
                        package_id: package_id.toString(),
                        modified: modified
                    }, actions.transfer.package.delete).then(function (res) {
                        resolve(res.data);
                    });
                });
            };

            /**
             * 重新打开一个 Package
             * @resolve {boolean}
             * @param package_id {string}
             * @returns {Promise}
             */
            this.reOpenPackage = function (package_id, modified) {
                return $q(function (resolve) {
                    http.ajaxPost({
                        package_id: package_id.toString(),
                        modified: modified
                    }, actions.transfer.package.reopen).then(function (res) {
                        resolve(res.data);
                    });
                });
            };

            /**
             * 请求服务器，在 Transfer 下创建一个指定名称的 Package
             * @resolve {Package}
             * @param transfer_id {number}
             * @param package_name {string}
             * @returns {Promise}
             */
            this.createPackage = function (transfer_id, package_name) {
                return http.ajaxPost({
                    transferId: transfer_id.toString(),
                    packageName: package_name
                }, actions.transfer.package.create).then(function (res) {
                    return res.data;
                });
            };

            /**
             * @resolve {boolean}
             * @param package_id {string|number}
             * @param modified {string}
             * @returns {Promise}
             */
            this.doClosePackage = function (package_id, modified) {
                return http.ajaxPost({
                    package_id: package_id.toString(),
                    modified: modified
                }, actions.transfer.package.close).then(function (res) {
                    return res.data; // isSuccess
                });
            };

            this.doDeleteItem = function (package_id, code, num) {
                if (!_.isNumber(package_id)) package_id = parseInt(package_id);

                if (!_.isNumber(num)) num = parseInt(num);

                return http.ajaxPost({
                    package_id: package_id.toString(),
                    barcode: code,
                    num: -num
                }, actions.transfer.package.item.add).then(function (res) {
                    return res.data;
                });
            };

            this.doCompare = function (transfer_id) {
                return http.ajaxPost({
                        transfer_id: transfer_id
                    },
                    actions.transfer.package.item.compare);
            };

            /**
             * 一览页面初始化
             */
            this.doListInit = function (data, scope) {
                return http.ajaxPost(data, actions.transfer.init, scope);
            };

            /**
             * 比较transfer的数量
             */
            this.compareTransfer = function (transfer) {
                return http.ajaxPost({
                        transfer: transfer
                    },
                    actions.transfer.compare)

                    .then(function (res) {
                        return res.data;
                    });
            };

        }]);
});