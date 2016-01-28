/**
 * @User: Jonas
 * @Date: 2015/3/22
 * @Version: 0.0.4
 */

define(function () {
    return angular
        .module("wmsModule", ["mainModule"])
        .constant("wmsActions", {
            // wms 的首页数据
            wms_default_index_doInit: "/wms/default/index/doInit",

            transfer: {
                init: "/wms/transfer/list/init",
                search: "/wms/transfer/list/search",
                delete: "/wms/transfer/list/delete",
                get: "/wms/transfer/edit/getTransfer",
                submit: "/wms/transfer/edit/submitTransfer",
                save: "/wms/transfer/edit/saveTransfer",
                download: "/wms/transfer/list/download",
                compare: "/wms/transfer/edit/compareTransfer",

                package: {
                    delete: "/wms/transfer/edit/deletePackage",
                    get: "/wms/transfer/edit/getPackage",
                    create: "/wms/transfer/edit/createPackage",
                    close: "/wms/transfer/edit/closePackage",
                    reopen: "/wms/transfer/edit/reOpenPackage",

                    item: {
                        add: "/wms/transfer/edit/addItem",
                        select: "/wms/transfer/edit/getPackageItems",
                        compare: "/wms/transfer/edit/compare",
                        getSku: "/wms/transfer/edit/getSku"
                    }
                },

                config: {
                    all: "/wms/transfer/edit/getAllConfigs"
                }
            },

            reservation: {
                reservationLog: {
                    search: '/wms/reservation/reservationLog/doReservationLogSearch'
                },

                reservationList: {
                    search: '/wms/reservation/list/doReservationListSearch',
                    init: '/wms/reservation/list/doInit'
                },

                popInventory: {
                    init: '/wms/reservation/popInventory/doPopInventoryInit',
                    search: '/wms/reservation/popInventory/doPopInventorySearch',
                    reset: '/wms/reservation/popInventory/doPopInventoryReset'
                },

                popSkuHisList: {
                    init: '/wms/reservation/popSkuHisList/doPopSkuHisInit',
                    search: '/wms/reservation/popSkuHisList/doPopSkuHisSearch'
                },

                popChangeReservation: {
                    init: '/wms/reservation/popChangeReservation/doInit',
                    change: '/wms/reservation/popChangeReservation/doChange'
                }
            },

            stockTake: {
                sessionList: {
                    init: '/wms/stockTake/list/doSessionListInit',
                    search: '/wms/stockTake/list/doSessionListSearch',
                    newSession: '/wms/stockTake/list/doNewSession',
                    delete: '/wms/stockTake/list/doSessionDelete',
                    close: '/wms/stockTake/list/doSessionStock'
                },

                sectionList: {
                    init: '/wms/stockTake/sectionList/doSectionListInit',
                    newSection: '/wms/stockTake/sectionList/doNewSection',
                    deleteSection: '/wms/stockTake/sectionList/doDeleteSection'
                },

                inventory: {
                    init: '/wms/stockTake/inventory/doInventoryInit',
                    scan: '/wms/stockTake/inventory/doUpcScan',
                    checkSectionStatus: '/wms/stockTake/inventory/doCheckSectionStatus'
                },

                sectionDetail: {
                    init: '/wms/stockTake/sectionDetail/doSectionDetailInit',
                    closeSection: '/wms/stockTake/sectionDetail/doCloseSection',
                    reScan: '/wms/stockTake/sectionDetail/doReScan'
                },

                compare: {
                    init: '/wms/stockTake/compare/doCompareInit',
                    checkBoxValueChange: '/wms/stockTake/compare/doCheckBoxValueChange',
                    checkSessionStatus: '/wms/stockTake/compare/doCheckSessionStatus',
                    sessionDone: '/wms/stockTake/compare/doSessionDone'
                }
            },

            ordReturn: {
                returnList: {
                    init: '/wms/return/list/doInit',
                    search: '/wms/return/list/doReturnListSearch',
                    changeStatus: '/wms/return/list/doChangeStatus'
                },

                newSession: {
                    init: '/wms/return/newSession/doNewSessionInit',
                    getSessionInfo: '/wms/return/newSession/doGetSessionInfo',
                    getOrderInfoByOrdNo: "/wms/return/newSession/getOrderInfoByOrdNo",
                    insertReturnInfo: "/wms/return/newSession/insertReturnInfo",
                    createReturnSession: "/wms/return/newSession/createReturnSession",
                    closeReturnSession: "/wms/return/newSession/closeReturnSession",
                    getReceivedFromItemByStoreId: '/wms/return/newSession/doGetReceivedFromItemByStoreId',
                    getReturnSessionProperties: '/wms/return/newSession/doGetReturnSessionProperties',
                    getReturnType: '/wms/return/newSession/doGetReturnType'
                },

                itemEdit: {
                    init: '/wms/return/itemEdit/doItemEditInit',
                    save: '/wms/return/itemEdit/doItemEditSave',
                    remove: '/wms/return/itemEdit/doReturnInfoRemove'
                },

                sessionList: {
                    init: '/wms/return/sessionList/doSessionListInit',
                    search: '/wms/return/sessionList/doSessionListSearch'
                },

                sessionDetail: {
                    init: '/wms/return/sessionDetail/doSessionDetailInit'
                }
            },

            backOrder: {
                search: '/wms/backOrder/list/doGetBackOrderInfoList',
                delete: '/wms/backOrder/list/doDelBackOrderInfo',
                add: '/wms/backOrder/list/doAddBackOrderInfo',
                popInit: '/wms/backOrder/list/doPopInit',
                listInit: '/wms/backOrder/list/doListInit'
            },

            location: {
                list: {
                    init: '/wms/location/list/init',
                    delete: '/wms/location/list/deleteLocation',
                    search: '/wms/location/list/searchLocation',
                    add: "/wms/location/list/addLocation"
                },
                bind: {
                    init: '/wms/location/bind/init',
                    search: "/wms/location/bind/searchItemLocation",
                    add: "/wms/location/bind/addItemLocation",
                    delete: "/wms/location/bind/deleteItemLocation"
                }
            },

            pickup: {
                list: {
                    init: "/wms/pickup/list/init",
                    search: "/wms/pickup/list/search",
                    scan: "/wms/pickup/list/scan",
                    download: "/wms/pickup/list/download"
                }
            },

            receive: {
                list: {
                    init: "/wms/receive/list/init",
                    search: "/wms/receive/list/search",
                    scan: "/wms/receive/list/scan"
                }
            },

            upc: {
                GET_PRODUCT : "/wms/upc/manage/getProduct",
                SAVE_PRODUCT : "/wms/upc/manage/saveProduct",
                GET_ALL_SIZE : "/wms/upc/manage/getAllSize",
                SAVE_ITEM_DETAIL : "/wms/upc/manage/saveItemDetail",
                GET_ALL_PRODUCT_TYPE : "/wms/upc/manage/getAllProductTypes"
            },

            report: {
                init: '/wms/report/list/doInit'
            }
        }).constant("wmsConstant", {

            print: {
                business: {
                    ReturnLabel: 'ReturnLabel',
                    sf: 'SF',
                    PickUp: 'PickUp',
                    Location:'Location',
                    SKU:'SKU'
                },

                hardware_key: {
                    Print_Return: 'Print_Return',
                    Print_SF: "Print_SF",
                    Print_PickUp: "Print_PickUp",
                    Print_Location: "Print_Location",
                    Print_SKU: "Print_SKU"
                }
            },

            return: {
                typeId: {
                    returnStatus: "17",
                    returnReason: "18",
                    returnExpress: "19",
                    returnCondition: "20",
                    sessionStatus: "23"
                }
            },

            stockTake: {
                typeId: {
                    sessionStatus: "24",
                    stockTakeType: "25"
                }
            }
        });
});