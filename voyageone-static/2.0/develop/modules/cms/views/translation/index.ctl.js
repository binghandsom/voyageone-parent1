/**
 * Created by Vantis on 2016/06/29.
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    cms.controller('translationManageController', (function () {
        function TranslationManageController($translate, translationService, notify, confirm, alert) {
            this.translate = $translate;
            this.translationService = translationService;
            this.notify = notify;
            this.confirm = confirm;
            this.alert = alert;
            this.searchBtnClicked = false;
            this.vm = {
                searchInfo: {},
                taskInfos: {},
                prodPageOption: {curr: 1, total: 0, fetch: self.searchHistoryTasks},
                sortFieldOptions: [],
                lenInfo: {},
                getTaskInfo: {
                    distributeRule: 1,
                    distributeCount: 10,
                    sortCondition: "",
                    sortRule: ""
                }
            };
        }

        TranslationManageController.prototype = {
            init: function () {
                var self = this;
                self.vm.prodPageOption.curr = 1;
                // 获取初始化数据
                self.translationService.getTasks()
                    .then(function (res) {
                        res = {
                            "code": null,
                            "displayType": null,
                            "message": null,
                            "data": {
                                "taskDetail": {
                                    "prodId": 5931,
                                    "productCode": "SJ9020SZW",
                                    "commonFields": {
                                        "brand": "Jewelry.com",
                                        "productNameEn": "Bangle Bracelet with Swarovski Zirconia in Sterling Silver",
                                        "originalTitleCn": "Jewelry.com 925银方晶锆石 手镯",
                                        "origin": "TH",
                                        "shortDesEn": "Bangle Bracelet with Swarovski Zirconia",
                                        "longDesEn": "A piercingly white stream of Swarovski zirconia illuminates this glamorous bangle bracelet. This piece features round-cut Swarovski zirconia and is crafted in sterling silver. Piece measures 8 inches in circumference.",
                                        "images1": [
                                            {
                                                "image1": "010-SJ9020SZW-1"
                                            }
                                        ],
                                        "clientProductUrl": "http://www.jewelry.com/bangle-bracelet-with-swarovski-zirconia-in-sterling-silver-sku-sj9020szw",
                                        "translator": "will",
                                        "translateStatus": "0",
                                        "translateTime": "2016-06-28 02:57:32"
                                    },
                                    "customProps": [
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "StoneColor",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "White",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Manufacturermodel",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "SJ9020SZW",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "CountryOfOrigin",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "TH",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Gender",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Women's",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Lab Created",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Y",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "产品宝石",
                                            "feedAttrEn": "Stone",
                                            "feedAttrValueCn": "方晶锆石",
                                            "feedAttrValueEn": "Cubic Zirconia",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "isClearance",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "FALSE",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Launch Product",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "FALSE",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "ClaspType",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Hidden Safety Clasp",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "NominalScaleWeight",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "22.098",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "ItemCreationDate",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "2015-04-07",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "TotalMetalWeight",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "19.8000",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MetalType",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Sterling Silver",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MerchantCategory",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Bracelets - Bangles - Cubic Zirconia",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Total Quantity",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "3",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Active",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "TRUE",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "PieceID",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "20370",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MetalColor",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "White",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Visibility",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "4",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "ManufacturingPolicy",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Make-to-Stock",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MSRP",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "2796.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "PrimaryStoneWeight",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "9.39",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "ManufacturerCode",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "9999",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Stone Length",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "3.5",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Stone Width",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "3.5",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "StoneGroupCode",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Cubic Zirconium",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "宝石重量",
                                            "feedAttrEn": "GemstoneTotalWeight",
                                            "feedAttrValueCn": "9.3900",
                                            "feedAttrValueEn": "9.39",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Bracelet Length",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "8",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Age Group",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Adult",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Style",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Bangles",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Gem Creation Method",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Created",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "ItemClassification",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Bracelets",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Popularity",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "3",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "ShapeCut",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Round",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "VisibilityName",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Catalog, Search",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice1",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice0",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "产品材质",
                                            "feedAttrEn": "MetalStamp",
                                            "feedAttrValueCn": "925银",
                                            "feedAttrValueEn": "925",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Bullet Point 2",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Crafted Sterling Silver",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice3",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Bullet Point 3",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Measures 8 Inches in Circumference",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice2",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "Bullet Point 1",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "Round-Cut Swarovski Zirconia",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice9",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice8",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "ActionURL",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "http://www.jewelry.com/bangle-bracelet-with-swarovski-zirconia-in-sterling-silver-sku-sj9020szw",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice5",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice4",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "MagicPrice7",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "0.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "爱德华测试",
                                            "feedAttrEn": "MagicPrice6",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "646.00",
                                            "isfeedAttr": true
                                        },
                                        {
                                            "feedAttrCn": "",
                                            "feedAttrEn": "edward",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "",
                                            "isfeedAttr": false
                                        },
                                        {
                                            "feedAttrCn": "产品品牌",
                                            "feedAttrEn": "brand",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "",
                                            "isfeedAttr": false
                                        },
                                        {
                                            "feedAttrCn": "产品尺寸",
                                            "feedAttrEn": "Description Measure",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "",
                                            "isfeedAttr": false
                                        },
                                        {
                                            "feedAttrCn": "宝石性质",
                                            "feedAttrEn": "GemCreationMethod",
                                            "feedAttrValueCn": "",
                                            "feedAttrValueEn": "",
                                            "isfeedAttr": false
                                        }
                                    ]
                                },
                                "sortFieldOptions": [
                                    {
                                        "type_id": 66,
                                        "type_code": "translateTask",
                                        "value": "quantity",
                                        "name": "库存",
                                        "add_name1": "",
                                        "add_name2": "",
                                        "lang_id": "cn",
                                        "comment": "获取翻译优先顺序"
                                    }
                                ],
                                "taskSummary": {
                                    "unassginedCount": 301,
                                    "imcompeleteCount": 1,
                                    "compeleteCount": 2544,
                                    "userCompeleteCount": 0
                                }
                            },
                            "redirectTo": null
                        };
                        self.vm.data = res.data;
                        // self.vm.taskInfos = res.data.taskInfos;
                        // self.vm.prodPageOption.total = res.data.taskInfos.prodListTotal;
                        // self.vm.sortFieldOptions = res.data.sortFieldOptions;
                        // self.vm.lenInfo = res.data.lenSetInfo;
                    })
            },

            // 分发翻译任务.
            assignTasks: function () {
                var self = this;
                self.translationService.getTasks().then(function (res) {
                    if (res.data.taskInfos.prodListTotal > 0) {
                        alert(self.instant('TXT_MSG_HAVE_UN_TRANSLATED_TASK'));
                    } else if (self.vm.getTaskInfo.distributeCount > 20) {
                        alert("最多不能超过20个任务!")
                    } else if (self.vm.getTaskInfo.distributeCount > self.vm.taskInfos.totalUndoneCount) {
                        alert("获取任务数量不能超过剩余任务数量！")
                    } else {
                        self.translationService.assignTasks(self.vm.getTaskInfo)
                            .then(function (res) {
                                self.vm.taskInfos = res.data;
                                self.vm.prodPageOption.total = res.data.prodListTotal;
                                self.vm.searchInfo = {};
                            })
                    }
                })
            },

            // 暂存当前任务
            saveTask: function (productItem, index) {
                var self = this;
                self.translationService.saveTask(productItem)
                    .then(function (res) {
                        self.vm.taskInfos.productTranslationBeanList[index].modifiedTime = res.data.modifiedTime;
                        self.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                        self.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                        self.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    })
            },

            // 提交当前任务.
            submitTask: function (productItem, index) {
                var self = this;
                self.translationService.submitTask(productItem)
                    .then(function (res) {
                        self.vm.taskInfos.productTranslationBeanList.splice(index, 1);
                        self.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                        self.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                        self.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        self.vm.prodPageOption.total = self.vm.prodPageOption.total - 1;
                    })
            },

            // 从主产品拷贝翻译信息.
            copyFormMainProduct: function (productItem, index) {
                var self = this;
                self.translationService.copyFormMainProduct(productItem)
                    .then(function (res) {
                        self.vm.taskInfos.productTranslationBeanList[index] = res.data;
                    })
            },

            // 查询历史任务.
            searchHistoryTasks: function (page) {
                var self = this;
                self.btnclick = true;
                self.vm.prodPageOption.curr = !page ? self.vm.prodPageOption.curr : page;
                self.vm.searchInfo.pageNum = self.vm.prodPageOption.curr;
                self.vm.searchInfo.pageSize = self.vm.prodPageOption.size;

                self.translationService.searchHistoryTasks(self.vm.searchInfo)
                    .then(function (res) {
                        self.vm.taskInfos.productTranslationBeanList = res.data.productTranslationBeanList;
                        self.vm.prodPageOption.total = res.data.prodListTotal;
                    })
            },

            // 撤销翻译任务.
            cancelTask: function (productItem, index) {
                var self = this;
                self.translationService.cancelTask({prodCode: productItem.productCode})
                    .then(function (res) {
                        self.vm.taskInfos.totalDoneCount = res.data.totalDoneCount;
                        self.vm.taskInfos.totalUndoneCount = res.data.totalUndoneCount;
                        self.vm.taskInfos.userDoneCount = res.data.userDoneCount;
                        self.notify.success(self.translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        productItem.tranSts = 0;
                    })
            },

            // 清空查询条件.
            clearConditions: function () {
                var self = this;
                self.vm.searchInfo = {};
            }
        };

        return TranslationManageController;
    })())
});
