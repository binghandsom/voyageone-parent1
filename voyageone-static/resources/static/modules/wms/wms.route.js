/**
 * @Name:    route.js
 * @Date:    2015/3/22
 * @User:    Jonas
 * @Version: 1.0.0

 * WMS 系统
 */
define(["angularAMD", "components/app", "components/util/commonUtil", "underscore"],

    function (angularAMD, app, util) {

        return app.constant("wmsRouteMaps", {

            wms_default_index: {
                hash: "/wms/default/index",
                page: "modules/wms/default/index.tpl.html",
                controller: "modules/wms/default/wmsIndexController"
            },

            transfer: {
                list: {
                    hash: "/wms/transfer/list",
                    page: "modules/wms/transfer/list.tpl.html",
                    controller: "modules/wms/transfer/transferListCtrl"
                },
                add: {
                    hash: "/wms/transfer/add/:type",
                    page: "modules/wms/transfer/edit.tpl.html",
                    controller: "modules/wms/transfer/transferEditCtrl"
                },
                edit: {
                    hash: "/wms/transfer/edit/:id",
                    page: "modules/wms/transfer/edit.tpl.html",
                    controller: "modules/wms/transfer/transferEditCtrl"
                }
            },

            // UPC管理页面
            upc_manage: {
                hash: "/wms/upc/manage",
                page: "modules/wms/upc/upcmanage.tpl.html",
                controller: "modules/wms/upc/wmsUpcManageController"
            },

            // 退货管理页面
            returns: {
                list: {
                    hash: "/wms/return/list",
                    page: "modules/wms/return/returnList.tpl.html",
                    controller: "modules/wms/return/returnListCtrl"
                },
                sessionList: {
                    hash: "/wms/return/sessionList",
                    page: "modules/wms/return/rtnSessionList.tpl.html",
                    controller: "modules/wms/return/rtnSessionListCtrl"
                },
                sessionDetail: {
                    hash: "/wms/return/sessionDetail",
                    page: "modules/wms/return/rtnSessionDetail.tpl.html",
                    controller: "modules/wms/return/rtnSessionDetailCtrl"
                },
                newSession: {
                    hash: "/wms/return/newSession",
                    page: "modules/wms/return/newRtnSession.tpl.html",
                    controller: "modules/wms/return/newRtnSessionCtrl"
                },
                itemEdit: {
                    hash: "/wms/return/itemEdit",
                    page: "modules/wms/return/rtnItemEdit.tpl.html",
                    controller: "modules/wms/return/rtnItemEditCtrl"
                }
            },

            location: {
                //货架管理
                list: {
                    hash: "/wms/location/list",
                    page: "modules/wms/location/locationList.tpl.html",
                    controller: "modules/wms/location/locationListCtrl"
                },
                // 货架关系管理
                bind: {
                    hash: "/wms/location/bind",
                    page: "modules/wms/location/locationBind.tpl.html",
                    controller: "modules/wms/location/locationBindCtrl"
                }
            },

            // BackOrder管理页面
            backOrder: {
                list:{
                    hash: "/wms/backOrder/list",
                    page: "modules/wms/backorder/list.tpl.html",
                    controller: "modules/wms/backorder/listCtrl"
                }
            },

            // 库存查看页面
            inventorySearch: {
                list: {
                    hash: "/wms/inventory/list",
                    page: "modules/wms/inventory/inventory.tpl.html",
                    controller: "modules/wms/inventory/inventoryCtrl"
                }
            },

            // 拣货
            pickup: {
                list: {
                    hash: "/wms/pickup/list",
                    page: "modules/wms/pickup/pickupList.tpl.html",
                    controller: "modules/wms/pickup/pickupListCtrl"
                }
            },

            // 收货
            receive: {
                list: {
                    hash: "/wms/receive/list",
                    page: "modules/wms/receive/receiveList.tpl.html",
                    controller: "modules/wms/receive/receiveListCtrl"
                }
            },

            // 订单管理
            wms_workload_list: {
                hash: "/wms/reservation/list",
                page: "modules/wms/reservation/reservationList.tpl.html",
                controller: "modules/wms/reservation/reservationListCtrl"
            },

            // 库存盘点
            stockTake: {
                list: {
                    hash: "/wms/stockTake/list",
                    page: "modules/wms/stocktake/sessionList.tpl.html",
                    controller: "modules/wms/stocktake/sessionListCtrl"
                },
                section: {
                    list: {
                        hash: "/wms/stockTake/sectionList",
                        page: "modules/wms/stocktake/sectionList.tpl.html",
                        controller: "modules/wms/stocktake/sectionListCtrl"
                    },
                    detail: {
                        hash: "/wms/stockTake/sectionDetail",
                        page: "modules/wms/stocktake/sectionDetail.tpl.html",
                        controller: "modules/wms/stocktake/sectionDetailCtrl"
                    }
                },
                inventory: {
                    hash: "/wms/stockTake/inventory",
                    page: "modules/wms/stocktake/inventory.tpl.html",
                    controller: "modules/wms/stocktake/inventoryCtrl"
                },
                compare: {
                    hash: "/wms/stockTake/compare",
                    page: "modules/wms/stocktake/compare.tpl.html",
                    controller: "modules/wms/stocktake/compareCtrl"
                }
            },

            packageReceive: {
                receive: {
                    hash: "/wms/packagereceive/receive",
                    page: "modules/wms/packagereceive/packagereceive.tpl.html",
                    controller: "modules/wms/packagereceive/packagereceiveCtrl"
                },
                list: {
                    hash: "/wms/packagereceive/list",
                    page: "modules/wms/packagereceive/packagereceiveList.tpl.html",
                    controller: "modules/wms/packagereceive/packagereceiveListCtrl"
                }
            },

            // 库存隔离，非WMS用，临时创建
            temp_insulate: {
                hash: "/temp/insulate/index",
                page: "modules/wms/temp_insulate/index.tpl.html",
                controller: "modules/wms/temp_insulate/indexCtrl"
            },

            // report 管理页面
            report: {
                list:{
                    hash: "/wms/report/list",
                    page: "modules/wms/report/report.tpl.html",
                    controller: "modules/wms/report/reportCtrl"
                }
            }

        }).config(["$routeProvider", "wmsRouteMaps", function ($routeProvider, routeMaps) {

            function letsRouteIt(mapOrRoute) {
                // 根据是否包含 hash 值，来判断是 map 还是 route
                var map = !mapOrRoute.hash ? mapOrRoute : null,
                    route = !!mapOrRoute.hash ? mapOrRoute : null;

                // 如果是 map，则递归进行绑定
                if (map) {
                    return _.each(map, letsRouteIt);
                }

                // 否则直接绑定
                return $routeProvider.when(
                    route.hash,
                    angularAMD.route({
                        templateUrl: route.page,
                        resolve: {
                            load: ["$q", "$rootScope", function ($q, $rootScope) {
                                return util.loadController($q, $rootScope, route.controller);
                            }]
                        }
                    })
                );
            }

            letsRouteIt(routeMaps);

        }]).config(["uiSelectConfig", function (uiSelectConfig) {

            uiSelectConfig.theme = 'bootstrap';
            uiSelectConfig.resetSearchInput = true;
            uiSelectConfig.appendToBody = true;

        }]);
    });