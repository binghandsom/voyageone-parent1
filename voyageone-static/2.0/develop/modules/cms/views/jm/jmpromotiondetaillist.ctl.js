/**
 * Created by linanbin on 15/12/7.
 */
define([
    'underscore',
    'modules/cms/controller/popup.ctl'
], function () {
    function detailController($scope, popups, cmsBtJmPromotionImportTaskService, cmsBtJmPromotionExportTaskService, jmPromotionDetailService, $routeParams, alert, $translate, confirm, platformMappingService,$filter) {

        $scope.datePicker = [];
        $scope.vm = {
            "promotionId": $routeParams.parentId,
            modelList: [],
            cmsBtJmPromotionImportTaskList: [],
            cmsBtJmPromotionExportTaskList: [],
            tagList: [],
            changeCount:0
        };
        $scope.searchInfo = {cmsBtJmPromotionId: $routeParams.parentId, pCatPath: null, pCatId: null};
        $scope.parentModel = {};
        $scope.modelUpdateDealEndTime = {};
        $scope.modelAllUpdateDealEndTime = {};
        $scope.dataPageOption = {curr: 1, total: 0, fetch: goPage.bind(this)};
        $scope.platformCategoryMapping = platformCategoryMapping;
        $scope.initialize = function () {
            jmPromotionDetailService.init({jmPromotionRowId:$routeParams.parentId}).then(function(res){
                $scope.parentModel = res.data.modelPromotion;
                $scope.vm.tagList = res.data.listTag;
                $scope.vm.changeCount = res.data.changeCount;
                $scope.vm.isBegin=res.data.isBegin;//活动是否开始
                $scope.vm.isEnd=res.data.isEnd;//活动是否结束
                $scope.vm.isUpdateJM=res.data.isUpdateJM;
                $scope.vm.brandList = res.data.brandList;
            });
            $scope.search();
            $scope.modelUpdateDealEndTime.promotionId = $routeParams.parentId;
            $scope.modelUpdateDealEndTime.getSelectedProductIdList = getSelectedProductIdList;
            $scope.modelUpdateDealEndTime.isBatch = true;

        };
        $scope.clear = function () {
            $scope.searchInfo = {cmsBtJmPromotionId: $routeParams.parentId};
            $scope.searchInfo.brand = null;
            $scope.searchInfo.selectedChanged = null;
        };
        $scope.search = function () {
            var data = getSearchInfo();

            goPage(1, 10)
            jmPromotionDetailService.getPromotionProductInfoCountByWhere(data).then(function (res) {
                $scope.dataPageOption.total = res.data;
            }, function (res) {
            });
        };
        function getSearchInfo() {
            loadSearchInfo();
            var data = angular.copy($scope.searchInfo);
            for (var key in data) {
                if (!data[key]) {
                    data[key] = undefined;
                }
            }
            return data;
        }

        function goPage(pageIndex, size) {
            var data = getSearchInfo();
            data.start = (pageIndex - 1) * size;
            data.length = size;
            jmPromotionDetailService.getPromotionProductInfoListByWhere(data).then(function (res) {
                $scope.vm.modelList = res.data;
            }, function (res) {
            })
        }

        var getSelectedProductIdList = function () {
            var productIdList = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    productIdList.push($scope.vm.modelList[i].cmsBtJmProductId);
                }
            }
            return productIdList;
        }
        $scope.jmNewByProductIdListInfo = function () {
            var productIdList = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    productIdList.push($scope.vm.modelList[i].cmsBtJmProductId);
                }
            }
            var parameter = {promotionId: $scope.vm.promotionId, productIdList: productIdList};
            confirm($translate.instant('TXT_Do_You_Want_To_Selected')).then(function () {
                jmPromotionDetailService.jmNewByProductIdListInfo(parameter).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {
                        if ($scope.vm.modelList[i].isChecked) {
                            $scope.vm.modelList[i].synchState = 1;
                            $scope.vm.modelList[i].isChecked = false;
                        }
                    }
                }, function (res) {
                })
            });
        };
        $scope.jmNewUpdateAll = function () {
            confirm($translate.instant('TXT_Do_You_Want_To_Update_ All')).then(function () {
                jmPromotionDetailService.jmNewUpdateAll($scope.vm.promotionId).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {

                        $scope.vm.modelList[i].synchState = 1;

                    }
                }, function (res) {
                })
            })
        };


        $scope.deleteByProductIdList = function () {
            var productIdList = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    productIdList.push($scope.vm.modelList[i].cmsBtJmProductId);
                }
            }
            var parameter = {promotionId: $scope.vm.promotionId, productIdList: productIdList};
            confirm($translate.instant('TXT_MSG_DO_DELETE')).then(function () {
                jmPromotionDetailService.deleteByProductIdList(parameter).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {
                        if ($scope.vm.modelList[i].isChecked) {
                            $scope.vm.modelList.splice(i, 1);
                        }
                    }
                }, function (res) {
                })
            });
        }
        $scope.deleteByPromotionId = function () {
            confirm($translate.instant('TXT_MSG_DO_DELETE')).then(function () {
                jmPromotionDetailService.deleteByPromotionId($scope.vm.promotionId).then(function () {
                    $scope.vm.modelList = [];
                }, function (res) {
                })
            })
        }
        //单条删除    调用批量删除方法
        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.productLongName).then(function () {
                var index = _.indexOf($scope.vm.modelList, data);
                if (data.synchStatus == 2) {
                    alert("该商品已上传，禁止删除!");
                    return
                }
                var parameter={};
                parameter.promotionId = $scope.vm.promotionId;
                parameter.listPromotionProductId =[data.id];
                parameter.listProductCode=[data.productCode];
                jmPromotionDetailService.batchDeleteProduct(parameter).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('TXT_SUCCESS'));
                    }
                    else {
                        alert($translate.instant('TXT_FAIL'));
                    }
                }, function (res) {
                    alert($translate.instant('TXT_FAIL'));
                });
                //jmPromotionDetailService.delete(data.id).then(function () {
                //    $scope.vm.modelList.splice(index, 1);
                //}, function (res) {
                //})
            })
        };

        $scope.updateDealPrice = function (data) {
            jmPromotionDetailService.updateDealPrice({id: data.id, dealPrice: data.dealPrice}).then(function () {
                data.isSave = false;
            }, function (res) {
            })
        };
        $scope.searchImport = function () {
            cmsBtJmPromotionImportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                $scope.vm.cmsBtJmPromotionImportTaskList = res.data;
            }, function (res) {
            })
        };
        $scope.searchExport = function () {
            cmsBtJmPromotionExportTaskService.getByPromotionId($routeParams.parentId).then(function (res) {
                $scope.vm.cmsBtJmPromotionExportTaskList = res.data;
            }, function (res) {
            })
        };
        $scope.addExport = function (templateType) {
            var model = {templateType: templateType, cmsBtJmPromotionId: $scope.vm.promotionId};
            cmsBtJmPromotionExportTaskService.addExport(model).then(function (res) {
                $scope.searchExport();
            }, function (res) {

            });
        };
        $scope.downloadImportExcel = function (id) {
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadExcel", angular.toJson({id: id}));
        };
        $scope.downloadImportErrorExcel = function (id) {
            ExportExcel("/cms/CmsBtJmPromotionImportTask/index/downloadImportErrorExcel", angular.toJson({id: id}));
        };
        $scope.downloadExportExcel = function (id) {
            ///cms/CmsBtJmPromotionExportTask/index/downloadExcel
            ExportExcel("/cms/CmsBtJmPromotionExportTask/index/downloadExcel", angular.toJson({id: id}));
        };


        function ExportExcel(action, source)//导出excel方法
        {
            var Form = document.createElement("FORM");
            document.body.appendChild(Form);
            Form.method = "POST";
            var newElement = $("<input name='source' type='hidden' />")[0];
            Form.appendChild(newElement);
            newElement.value = source;
            var IsExcelElement = $("<input name='IsExcel' type='hidden' />")[0];
            Form.appendChild(IsExcelElement);
            IsExcelElement.value = 1;
            Form.action = action;
            Form.submit();
        };
        function loadSearchInfo() {
            $scope.searchInfo.synchStatusList = [];
            //$scope.searchInfo.errorStatus=undefined;//错误状态
            //$scope.searchInfo.allStatus1=undefined;//处理中
            $scope.searchInfo.hasStatus=undefined;//是否有状态
            if ($scope.searchInfo.synchStatus0) {
                $scope.searchInfo.synchStatusList.push(0)
                $scope.searchInfo.synchStatusList.push(1)
                $scope.searchInfo.hasStatus=1;
            }
            if ($scope.searchInfo.allStatus1) {
                $scope.searchInfo.hasStatus=1;
            }
            if ($scope.searchInfo.synchStatus2) {
                $scope.searchInfo.synchStatusList.push(2)
                $scope.searchInfo.hasStatus=1;
            }
            if ($scope.searchInfo.allErrorStatus) {
                $scope.searchInfo.hasStatus=1;
            }
        }
        $scope.getStatus = function (model) {
            //0:未更新 2:上新成功 3:上传异常    未上传；处理中；上传成功；上传失败

             if (model.synchStatus == 1) {
                return "待上传";
            }
            else if (model.priceStatus== 1) {//model.priceStatus == 1 ||
                return "待更新";
            }
            else if (model.dealEndTimeStatus == 1) {
                return "待延期";
            }
           else if(model.synchStatus==3||model.priceStatus==3 ||model.dealEndTimeStatus==3 || model.stockStatus==3)
            {
                return "上传失败";
            }
            else if (model.synchStatus == 0) {
                return "未上传";
            }
            return "上传成功";
        }
        $scope.getSelectedPromotionProductIdList = function () {
            var listPromotionProductId = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProductId.push($scope.vm.modelList[i].id);
                }
            }
            return listPromotionProductId;
        }
        $scope.getSelectedPromotionProductList = function () {
            var listPromotionProduct = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProduct.push($scope.vm.modelList[i]);
                }
            }
            return listPromotionProduct;
        }
        $scope.getSelectedProductCodeList = function () {
            var listPromotionProductCode = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProductCode.push($scope.vm.modelList[i].productCode);
                }
            }
            return listPromotionProductCode;
        }
        //单条更新 调用批量同步价格
        $scope.updateJM = function (promotionProductId) {
            confirm("您确定要重新上传商品吗?").then(function () {
                var listPromotionProductId = [promotionProductId];
                var parameter = {};
                parameter.promotionId = $scope.vm.promotionId;
                parameter.listPromotionProductId = listPromotionProductId;
                jmPromotionDetailService.batchSynchPrice(parameter).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('请稍后几分钟刷新页面，查看最新上传结果'));
                    }
                    else {
                        alert($translate.instant('TXT_FAIL'));
                    }
                }, function (res) {
                    alert($translate.instant('TXT_FAIL'));
                });
            });
        };
        //批量同步价格
        $scope.batchSynchPrice = function () {
            var listPromotionProductId = $scope.getSelectedPromotionProductIdList();
            if (listPromotionProductId.length == 0) {
                alert("请选择同步价格的商品!");
                return;
            }
            if ($scope.vm.isBegin) {
                confirm("聚美专场已开始预热，价格变更将有极大可能性引起客诉。点击确认继续操作！").then(function () {
                    batchSynchPrice_item(listPromotionProductId);
                });
            }
            else {
                confirm("聚美平台无任何删除功能，专场内一旦有商品完成上传，该商品禁止删除，该专场禁止删除，点击确认继续操作.").then(function () {
                    batchSynchPrice_item(listPromotionProductId);
                });
            }
        };
        function  batchSynchPrice_item(listPromotionProductId) {
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            jmPromotionDetailService.batchSynchPrice(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert("请稍后几分钟刷新页面，查看最新上传结果");
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }
        $scope.synchAllPrice = function () {
            confirm("您确定要重新上传商品吗?").then(function () {
                jmPromotionDetailService.synchAllPrice($scope.vm.promotionId).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('请稍后几分钟刷新页面，查看最新上传结果'));
                    }
                    else {
                        alert(res.data.msg);
                    }
                }, function (res) {
                    alert($translate.instant('TXT_FAIL'));
                });
            });
        }
        //批量上传
        $scope.batchCopyDeal = function () {
            var listPromotionProductId = $scope.getSelectedPromotionProductIdList();
            if (listPromotionProductId.length == 0) {
                alert("请选择上传的商品!");
                return;
            }
            //2.8.2
            if ($scope.vm.isBegin) {
                confirm("聚美专场已开始预热，任何变更都有极大可能性引起客诉，点击确认继续操作.").then(function () {
                    batchCopyDeal_item(listPromotionProductId);
                });
            }
            else {
                confirm("聚美平台无任何删除功能，专场内一旦有商品完成上传，该商品禁止删除，该专场禁止删除。点击确认继续操作.").then(function () {
                    batchCopyDeal_item(listPromotionProductId);
                });
            }
        }
        function batchCopyDeal_item(listPromotionProductId) {
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            jmPromotionDetailService.batchCopyDeal(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('请稍后几分钟刷新页面，查看最新上传结果'));
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
        }
        $scope.copyDealAll = function () {
            confirm("是否全部上传?").then(function () {

                jmPromotionDetailService.copyDealAll($scope.vm.promotionId).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('请稍后几分钟刷新页面，查看最新上传结果'));
                    }
                    else {
                        alert(res.data.msg);
                    }
                }, function (res) {
                    alert($translate.instant('TXT_FAIL'));
                });
            });
        }
        //批量删除
        $scope.batchDeleteProduct = function () {
            //已再售的不删除
            var listPromotionProductId = $scope.getSelectedPromotionProductIdList();
            var listProductCode = $scope.getSelectedProductCodeList();
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            parameter.listProductCode=listProductCode;
            if (listPromotionProductId.length == 0) {
                alert("请选择删除的商品!");
                return;
            }
            // for (var i = 0; i < $scope.vm.modelList.length; i++) {
            //     if ($scope.vm.modelList[i].isChecked && $scope.vm.modelList[i].synchStatus == 2) {
            //         alert("勾选范围内存在商品已完成上传，禁止删除!");
            //         return;
            //     }
            // }
            confirm($translate.instant('TXT_MSG_DO_DELETE')).then(function () {
                jmPromotionDetailService.batchDeleteProduct(parameter).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('TXT_SUCCESS'));
                    }
                    else {
                        alert($translate.instant('TXT_FAIL'));
                    }
                }, function (res) {
                    alert($translate.instant('TXT_FAIL'));
                });
            });
        }

        $scope.deleteAllProduct = function () {//已再售的不删除
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].synchStatus == 2) {
                    alert("该专场内存在商品已完成上传，禁止删除!");
                    return;
                }
            }
            confirm($translate.instant('TXT_MSG_DO_DELETE')).then(function () {
                jmPromotionDetailService.deleteAllProduct($scope.vm.promotionId).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('TXT_SUCCESS'));
                    }
                    else {
                        alert(res.data.msg);
                    }
                }, function (res) {
                    alert($translate.instant('TXT_FAIL'));
                });
            });
        }
        $scope.selectAll = function ($event) {
            var checkbox = $event.target;
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                // if ($scope.vm.modelList[i].isChecked) {
                $scope.vm.modelList[i].isChecked = checkbox.checked;
                //}
            }
        };
        //批量修改价格
        $scope.openPriceModifyWin = function () {
            var listPromotionProduct = $scope.getSelectedPromotionProductList();
            if (listPromotionProduct.length == 0) {
                alert("请选择修改价格的商品!");
                return;
            }
            popups.openPriceModify({search: $scope.search,jmPromotionId:$scope.vm.promotionId ,isBegin: $scope.vm.isBegin,listPromotionProduct: listPromotionProduct})
        }
        $scope.openProductDetailWin = function (object) {

            popups.openJmProductDetail(object).then(function () {
                $scope.search();
            });
        }
        $scope.openJmPromotionProductImportWin = function () {
            popups.openJmPromotionProductImport($scope.parentModel, $scope.selectImport);
        }
        $scope.openJmPromotionDetailWin = function () {
            var parameter={ id : $routeParams.parentId};
            parameter.isBegin= $scope.vm.isBegin;//活动是否开始
            parameter.isEnd= $scope.vm.isEnd;//活动是否结束
            popups.openJmPromotionDetail(parameter).then(function(context){
                $scope.parentModel = context;
            });

        }
        $scope.openDealExtensionWin = function () {
            popups.openDealExtension($scope.parentModel).then(function () {
                $scope.search();
            });;
        }

        $scope.getErrorMsg = function (errorMsg) {
            if (!errorMsg) {
                return "";
            }
            return errorMsg.substr(0, 30) + "...";
        };

        $scope.getDealPrice = function (m) {
            if (m.maxDealPrice == m.minDealPrice)
                return m.maxDealPrice;

            return m.maxDealPrice + "~" + m.minDealPrice;
        };
        $scope.getMarketPrice = function (m) {
            if (m.maxMarketPrice == m.minMarketPrice)
                return m.maxMarketPrice;

            return m.maxMarketPrice + "~" + m.minMarketPrice;
        };
        $scope.getMinMaxPrice = function (minPrice,maxPrice) {
            if (maxPrice== minPrice)
                return maxPrice;

            return minPrice + "~" +maxPrice;
        };
        $scope.changeSelectTag=function(m) {
            var productTagList = [];
            for (var i = 0; i < m.tagNameList.length; i++) {
                var tagName = m.tagNameList[i];
                var tag = _.find($scope.vm.tagList, function (tag) {
                    return tag.tagName == tagName;
                });
                productTagList.push({tagId: tag.id, tagName: tag.tagName});
            }
            var parameter = {};
            parameter.tagList = productTagList;
            parameter.id = m.id;
            jmPromotionDetailService.updatePromotionProductTag(parameter).then(function (res) {
                //   alert($translate.instant('TXT_SUCCESS'));
            }, function (res) {
                alert($translate.instant('TXT_FAIL'));
            });
            //$scope.vm.tagList.
            // alert(m.tagNameList.toString());
        }
        /**
         * popup弹出选择聚美平台数据类目
         * @param popupNewCategory
         */
        function platformCategoryMapping(popupNewCategory) {
            platformMappingService.getPlatformCategories({cartId: 27})
                .then(function (res) {
                    if (!res.data || !res.data.length) {
                        alert("没数据");
                        return null;
                    }
                    return popupNewCategory({
                        from: "",
                        categories: res.data
                    });
                }).then(function (context) {
                $scope.searchInfo.pCatPath = context.selected.catPath;
                $scope.searchInfo.pCatId = context.selected.catId;
            });
        }
    }

    detailController.$inject = ['$scope', 'popups', 'cmsBtJmPromotionImportTaskService', 'cmsBtJmPromotionExportTaskService', 'jmPromotionDetailService', '$routeParams', 'alert', '$translate', 'confirm', 'platformMappingService'];
    return detailController;
});