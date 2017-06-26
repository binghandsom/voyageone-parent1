define([
    'cms',
    'modules/cms/enums/Carts',
    'modules/cms/controller/popup.ctl'
], function (cms, carts) {

    function SProductListDirectiveController($scope, popups, $productDetailService, cmsBtJmPromotionExportTaskService, jmPromotionDetailService, spDataService, $routeParams, alert, $translate, confirm, notify, platformMappingService) {
        $scope.datePicker = [];
        $scope.vm = {
            promotionId: $routeParams.jmpromId,
            modelList: [],
            cmsBtJmPromotionImportTaskList: [],
            cmsBtJmPromotionExportTaskList: [],
            tagList: [],
            changeCount: 0,
            productCount: 0,
            productUrl: carts.valueOf(27).pUrl,
            _selall:false,
            columnArrow: {}
        };
        $scope.searchInfo = {cmsBtJmPromotionId: $routeParams.jmpromId, pCatPath: null, pCatId: null};
        $scope.searchInfoBefore = {};
        $scope.parentModel = {};
        $scope.modelUpdateDealEndTime = {};
        $scope.modelAllUpdateDealEndTime = {};
        $scope.dataPageOption = {curr: 1, total: 0, fetch: goPage.bind(this)};
        $scope.platformCategoryMapping = platformCategoryMapping;

        $scope.$on('detail.saved', function () {
            $scope.initialize();
        });

        $scope.initialize = function () {
            jmPromotionDetailService.init({jmPromotionRowId: $routeParams.jmpromId}).then(function (res) {

                $scope.parentModel = res.data.modelPromotion;
                $scope.parentModel.prodSum = $scope.parentModel.prodSum || 0;
                $scope.parentModel.quantitySum = $scope.parentModel.quantitySum || 0;
                $scope.vm.tagList = res.data.listTag;
                $scope.vm.productCount = res.data.productCount || 0;
                $scope.vm.changeCount = res.data.changeCount || 0;
                $scope.vm.isBegin = res.data.isBegin;//活动是否开始
                $scope.vm.isEnd = res.data.isEnd;//活动是否结束
                $scope.vm.isUpdateJM = res.data.isUpdateJM;
                $scope.vm.brandList = res.data.brandList;

                spDataService.passDated = $scope.vm.passDated = $scope.parentModel.passDated;
                $scope.vm.spDataService = spDataService;
            });
            $scope.search();
            $scope.modelUpdateDealEndTime.promotionId = $routeParams.jmpromId;
            $scope.modelUpdateDealEndTime.getSelectedProductIdList = getSelectedProductIdList;
            $scope.modelUpdateDealEndTime.isBatch = true;

            spDataService.getExportInfo = getExportInfo;
        };

        $scope.clear = function () {
            $scope.searchInfo = {cmsBtJmPromotionId: $routeParams.jmpromId};
            $scope.searchInfo.brand = null;
            $scope.searchInfo.selectedChanged = null;
            $scope.vm._selall = false;
        };

        $scope.search = function () {
            $scope.vm._selall = false;
            var data = getSearchInfo(),
                size = $scope.dataPageOption.size ? $scope.dataPageOption.size : 10;

            goPage(1, size);
            jmPromotionDetailService.getPromotionProductInfoCountByWhere(data).then(function (res) {
                $scope.dataPageOption.total = res.data;
            });
        };

        function getSearchInfo() {
            loadSearchInfo();
            var data = angular.copy($scope.searchInfo);
            for (var key in data) {
                if (!data[key]) {
                    if (key == 'stockQty' && data[key] == 0) {
                        data[key] = 0;
                    } else {
                        data[key] = undefined;
                    }
                }
            }
            $scope.searchInfoBefore = angular.copy(data);
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
        };

        $scope.jmNewByProductIdListInfo = function () {
            var productIdList = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    productIdList.push($scope.vm.modelList[i].cmsBtJmProductId);
                }
            }

            confirm($translate.instant('TXT_Do_You_Want_To_Selected')).then(function () {
                jmPromotionDetailService.jmNewByProductIdListInfo({
                    promotionId: $scope.vm.promotionId,
                    productIdList: productIdList
                }).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {
                        if ($scope.vm.modelList[i].isChecked) {
                            $scope.vm.modelList[i].synchState = 1;
                            $scope.vm.modelList[i].isChecked = false;
                        }
                    }
                })
            });
        };

        $scope.jmNewUpdateAll = function () {
            confirm($translate.instant('TXT_Do_You_Want_To_Update_ All')).then(function () {
                jmPromotionDetailService.jmNewUpdateAll($scope.vm.promotionId).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {
                        $scope.vm.modelList[i].synchState = 1;
                    }
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

            confirm($translate.instant('TXT_MSG_DO_DELETE')).then(function () {
                jmPromotionDetailService.deleteByProductIdList({
                    promotionId: $scope.vm.promotionId,
                    productIdList: productIdList
                }).then(function () {
                    for (var i = $scope.vm.modelList.length - 1; i >= 0; i--) {
                        if ($scope.vm.modelList[i].isChecked) {
                            $scope.vm.modelList.splice(i, 1);
                        }
                    }
                })
            });
        };

        $scope.deleteByPromotionId = function () {
            confirm($translate.instant('TXT_MSG_DO_DELETE')).then(function () {
                jmPromotionDetailService.deleteByPromotionId($scope.vm.promotionId).then(function () {
                    $scope.vm.modelList = [];
                })
            })
        };

        /**
         * 单条删除
         * 调用批量删除方法
         */
        $scope.del = function (data) {
            confirm($translate.instant('TXT_MSG_DO_DELETE') + data.productLongName).then(function () {
                if (data.synchStatus == 2) {
                    alert("该商品已上传，禁止删除!");
                    return
                }

                jmPromotionDetailService.batchDeleteProduct({
                    promotionId : $scope.vm.promotionId,
                    listPromotionProductId :[data.id],
                    listProductCode : [data.productCode]
                }).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('TXT_SUCCESS'));
                    }
                    else {
                        alert($translate.instant('TXT_FAIL'));
                    }
                });

            })
        };

        $scope.updateDealPrice = function (data) {
            jmPromotionDetailService.updateDealPrice({id: data.id, dealPrice: data.dealPrice}).then(function () {
                data.isSave = false;
            })
        };

        $scope.addExport = function (templateType) {
            var model = {templateType: templateType, cmsBtJmPromotionId: $scope.vm.promotionId};
            cmsBtJmPromotionExportTaskService.addExport(model).then(function (res) {
                $scope.searchExport();
            });
        };

        $scope.save = function () {
            var errMsg = '';
            if (spDataService.jmPromotionObj.detailStatus != 1) {
                errMsg += '活动信息、  ';
            }
            if (spDataService.jmPromotionObj.shelfStatus != 1) {
                errMsg += '活动货架、  ';
            }
            if (spDataService.jmPromotionObj.imageStatus != 1 && spDataService.jmPromotionObj.imageStatus != 3) {
                errMsg += '活动图片、  ';
            }
            if (spDataService.jmPromotionObj.bayWindowStatus != 1) {
                errMsg += '活动飘窗、  ';
            }
            if (errMsg) {
                errMsg = "以下活动内容还未填写完整：<br>" + errMsg + "<br>请确认是否提交？";
                confirm($translate.instant(errMsg)).then(function () {
                    var stsParam = {'jmPromId': parseInt($scope.vm.promotionId)};
                    stsParam.stepName = 'SessionsUpload';
                    stsParam.stepStatus = 'Success';
                    jmPromotionDetailService.setJmPromotionStepStatus(stsParam).then(function () {
                        spDataService.jmPromotionObj.uploadStatus = 1;
                        notify.success('提交成功');
                    });
                });
            } else {
                var stsParam = {'jmPromId': parseInt($scope.vm.promotionId)};
                stsParam.stepName = 'SessionsUpload';
                stsParam.stepStatus = 'Success';
                jmPromotionDetailService.setJmPromotionStepStatus(stsParam).then(function () {
                    spDataService.jmPromotionObj.uploadStatus = 1;
                    notify.success('提交成功');
                });
            }
        };

        function loadSearchInfo() {
            $scope.searchInfo.synchStatusList = [];

            $scope.searchInfo.hasStatus = undefined;//是否有状态
            if ($scope.searchInfo.synchStatus0) {
                $scope.searchInfo.synchStatusList.push(0)
                $scope.searchInfo.synchStatusList.push(1)
                $scope.searchInfo.hasStatus = 1;
            }
            if ($scope.searchInfo.allStatus1) {
                $scope.searchInfo.hasStatus = 1;
            }
            if ($scope.searchInfo.synchStatus2) {
                $scope.searchInfo.synchStatusList.push(2)
                $scope.searchInfo.hasStatus = 1;
            }
            if ($scope.searchInfo.allErrorStatus) {
                $scope.searchInfo.hasStatus = 1;
            }
            if ($scope.searchInfo.synchStatus_1) {
                $scope.searchInfo.isInJm = 1;
                $scope.searchInfo.hasStatus = 1;
            }
        }

        $scope.getStatus = function (model) {
            //0:未更新 2:上新成功 3:上传异常    未上传；处理中；上传成功；上传失败

            if (model.synchStatus == 1) {
                return "待上传";
            }
            else if (model.priceStatus == 1) {//model.priceStatus == 1 ||
                return "待更新";
            }
            else if (model.dealEndTimeStatus == 1) {
                return "待延期";
            }
            else if (model.synchStatus == 3 || model.priceStatus == 3 || model.dealEndTimeStatus == 3 || model.stockStatus == 3) {
                return "上传失败";
            }
            else if (model.synchStatus == 0) {
                return "未上传";
            }
            return "上传成功";
        };

        $scope.getUpdateStatus = function (model) {
            //0:未更新 1：待更新  2：已经更新 3：更新失败',

            if (model.updateStatus == 1) {
                return "有变更";
            }
            else if (model.updateStatus == 2) {//model.priceStatus == 1 ||
                return "已变更";
            }
            else if (model.updateStatus == 0) {
                return "无变更";
            }
            return "无变更";
        };

        $scope.getSelectedPromotionProductIdList = function () {
            var listPromotionProductId = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProductId.push($scope.vm.modelList[i].id);
                }
            }
            return listPromotionProductId;
        };

        $scope.getSelectedPromotionProductCodeList = function () {
            var listPromotionProductCode = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProductCode.push($scope.vm.modelList[i].productCode);
                }
            }
            return listPromotionProductCode;
        };

        $scope.getSelectedPromotionProductList = function () {
            var listPromotionProduct = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProduct.push($scope.vm.modelList[i]);
                }
            }
            return listPromotionProduct;
        };

        $scope.getSelectedProductCodeList = function () {
            var listPromotionProductCode = [];
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                if ($scope.vm.modelList[i].isChecked) {
                    listPromotionProductCode.push($scope.vm.modelList[i].productCode);
                }
            }
            return listPromotionProductCode;
        };

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
                });
            });
        };

        //批量同步价格
        $scope.batchSynchPrice = function () {
            var listPromotionProductId = $scope.getSelectedPromotionProductIdList();
            if (listPromotionProductId.length == 0 && $scope.vm._selall == false) {
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

        function batchSynchPrice_item(listPromotionProductId) {
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            parameter.searchInfo = $scope.searchInfoBefore;
            parameter.selAll = $scope.vm._selall;
            jmPromotionDetailService.batchSynchPrice(parameter).then(function (res) {
                $scope.search();
                alert("请稍后几分钟刷新页面，查看最新上传结果");
            });
        }

        //批量同步价格
        $scope.batchSynchMallPrice = function () {
            var listPromotionProductCodes = $scope.getSelectedPromotionProductCodeList();
            if (listPromotionProductCodes.length == 0 && $scope.vm._selall == false) {
                alert("请选择同步价格的商品!");
                return;
            }
            if ($scope.vm.isBegin) {
                confirm("聚美专场已开始预热，价格变更将有极大可能性引起客诉。点击确认继续操作！").then(function () {
                    batchSynchMallPrice_item(listPromotionProductCodes);
                });
            }
            else {
                confirm("聚美平台无任何删除功能，专场内一旦有商品完成上传，该商品禁止删除，该专场禁止删除，点击确认继续操作.").then(function () {
                    batchSynchMallPrice_item(listPromotionProductCodes);
                });
            }
        };

        function batchSynchMallPrice_item(listPromotionProductCodes) {
            var parameter = {};
            parameter.jmPromotionId = $scope.vm.promotionId;
            parameter.productCodes = listPromotionProductCodes;
            parameter.searchInfo = $scope.searchInfoBefore;
            parameter.selAll = $scope.vm._selall;
            jmPromotionDetailService.batchSynchMallPrice(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();

                    alert("请稍后几分钟刷新页面，查看最新上传结果");

                }

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
                });
            });
        };

        $scope.synchAllMallPrice = function () {
            confirm("您确定要同步商场价格吗?").then(function () {
                batchSynchMallPrice_item()
            });
        };

        //批量上传
        $scope.batchCopyDeal = function () {
            var listPromotionProductId = $scope.getSelectedPromotionProductIdList();
            if (listPromotionProductId.length == 0 && $scope.vm._selall == false) {
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
        };

        function batchCopyDeal_item(listPromotionProductId) {
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            parameter.searchInfo = $scope.searchInfoBefore;
            parameter.selAll = $scope.vm._selall;
            jmPromotionDetailService.batchCopyDeal(parameter).then(function (res) {
                if (res.data.result) {
                    $scope.search();
                    alert($translate.instant('请稍后几分钟刷新页面，查看最新上传结果'));
                    $scope.vm._selall = false;
                }
                else {
                    alert($translate.instant('TXT_FAIL'));
                }
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
                });
            });
        };

        //批量删除
        $scope.batchDeleteProduct = function () {
            //已再售的不删除
            var listPromotionProductId = $scope.getSelectedPromotionProductIdList();
            var listProductCode = $scope.getSelectedProductCodeList();
            var parameter = {};
            parameter.promotionId = $scope.vm.promotionId;
            parameter.listPromotionProductId = listPromotionProductId;
            parameter.listProductCode = listProductCode;
            parameter.searchInfo = $scope.searchInfoBefore;
            parameter.selAll = $scope.vm._selall;
            if (listPromotionProductId.length == 0 && $scope.vm._selall == false) {
                alert("请选择删除的商品!");
                return;
            }

            confirm($translate.instant('TXT_MSG_DO_DELETE')).then(function () {
                jmPromotionDetailService.batchDeleteProduct(parameter).then(function (res) {
                    if (res.data.result) {
                        $scope.search();
                        alert($translate.instant('TXT_SUCCESS'));
                        $scope.vm._selall = false;
                    }
                    else {
                        alert($translate.instant('TXT_FAIL'));
                    }
                });
            });
        };

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
                });
            });
        };

        $scope.selectAll = function ($event) {
            var checkbox = $event.target;
            for (var i = 0; i < $scope.vm.modelList.length; i++) {
                $scope.vm.modelList[i].isChecked = checkbox.checked;
            }
        };

        //批量修改价格
        $scope.openPriceModifyWin = function () {
            var listPromotionProduct = $scope.getSelectedPromotionProductList();
            if (listPromotionProduct.length == 0 && $scope.vm._selall == false) {
                alert("请选择修改价格的商品!");
                return;
            }
            popups.openPriceModify({
                search: $scope.search,
                jmPromotionId: $scope.vm.promotionId,
                isBegin: $scope.vm.isBegin,
                listPromotionProduct: listPromotionProduct,
                searchInfo : $scope.searchInfoBefore,
                selAll : $scope.vm._selall
            }).then(function () {
                $scope.vm._selall = false;
            })
        };

        $scope.openTagModifyWin = function () {
            var listPromotionProduct = $scope.getSelectedPromotionProductList();
            if (listPromotionProduct.length == 0 && $scope.vm._selall == false) {
                alert("请选择修改价格的商品!");
                return;
            }
            popups.openJMTagModify({
                search: $scope.search,
                tagList: $scope.vm.tagList,
                jmPromotionId: $scope.vm.promotionId,
                isBegin: $scope.vm.isBegin,
                listPromotionProduct: listPromotionProduct,
                searchInfo : $scope.searchInfoBefore,
                selAll : $scope.vm._selall
            })
        };

        $scope.openJmProductDetailWin = function (model) {

            popups.openJmProductDetail({promotionProduct: model}).then(function () {
                $scope.search();
            });
        };

        $scope.openProductDetailWin = function (model) {

            $productDetailService.getProductIdByCode(model.productCode).then(function (res) {
                window.open("#/product/detail/" + res.data);
            });

        };

        $scope.openJmPromotionProductImportWin = function () {
            popups.openJmPromotionProductImport($scope.parentModel, $scope.selectImport);
        };

        $scope.openJmPromotionDetailWin = function () {
            var parameter = {id: $routeParams.jmpromId};
            parameter.isBegin = $scope.vm.isBegin;//活动是否开始
            parameter.isEnd = $scope.vm.isEnd;//活动是否结束
            popups.openJmPromotionDetail(parameter).then(function (context) {
                $scope.parentModel = context;
            });

        };

        $scope.openDealExtensionWin = function () {
            popups.openDealExtension($scope.parentModel).then(function () {
                $scope.search();
            });
        };

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

        $scope.getMinMaxPrice = function (minPrice, maxPrice) {
            if (maxPrice == minPrice)
                return maxPrice;

            return minPrice + "~" + maxPrice;
        };

        $scope.changeSelectTag = function (m, oldTagNameList) {
            //获取删除的tag
            var delTagNameList = _.filter(oldTagNameList, function (tagName) {
                return !_.contains(m.tagNameList, tagName);
            });
            //获取新增的tag
            var addTagNameList = _.filter(m.tagNameList, function (tagName) {
                return !_.contains(oldTagNameList, tagName);
            });

            var productTagList = [];
            _.each(delTagNameList, function (tagName) {
                var tag = _.find($scope.vm.tagList, function (tag) {
                    return tag.tagName == tagName;
                });
                productTagList.push({tagId: tag.id, tagName: tag.tagName, checked: 0});
            });
            _.each(addTagNameList, function (tagName) {
                var tag = _.find($scope.vm.tagList, function (tag) {
                    return tag.tagName == tagName;
                });
                productTagList.push({tagId: tag.id, tagName: tag.tagName, checked: 2});
            });

            var parameter = {};
            parameter.tagList = productTagList;
            parameter.id = m.id;
            jmPromotionDetailService.updatePromotionProductTag(parameter).then(function (res) {
                //   alert($translate.instant('TXT_SUCCESS'));
            });

        };

        //刷新价格
        $scope.refreshPrice = function () {
            jmPromotionDetailService.refreshPrice($scope.vm.promotionId).then(function (res) {
                alert($translate.instant('TXT_SUCCESS'));
            });

        };

        //更新备注
        $scope.updateRemark = function (item) {
            // alert(item.remark);
            var parameter = {jmPromotionProductId: item.id, remark: item.remark};
            jmPromotionDetailService.updateRemark(parameter).then(function (res) {
                //   alert($translate.instant('TXT_SUCCESS'));
            });
        };

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

        function getExportInfo() {
            var selCodeList = [];

            _.each($scope.vm.modelList, function (element) {
                if (element.isChecked)
                    selCodeList.push(element.productCode);
            });

            return {
                searchInfo: getSearchInfo(),
                selCodeList: selCodeList
            };
        }

        /**
         * 检索列排序
         * */
        $scope.columnOrder = function (columnName) {
            var column, columnArrow = $scope.vm.columnArrow;
            _.forEach(columnArrow, function (value, key) {
                if (key != columnName)
                    columnArrow[key] = null;
            });
            column = columnArrow[columnName];
            if (!column) {
                column = {};
                column.mark = 'unsorted';
                column.count = null;
            }
            column.count = !column.count;
            //偶数升序，奇数降序
            if (column.count)
                column.mark = 'sort-desc';
            else
                column.mark = 'sort-up';
            columnArrow[columnName] = column;

            searchByOrder(columnName, column.mark);
        };

        function searchByOrder(columnName, sortOneType) {
            $scope.searchInfo.sortOneName = columnName;
            $scope.searchInfo.sortOneType = sortOneType == 'sort-up' ? 'asc' : 'desc';
            $scope.search();
        }

        $scope.getArrowName = function (columnName, cartId) {
            var columnArrow = $scope.vm.columnArrow;

            if (cartId) {
                columnName = columnName.replace('✓', cartId);
            }

            if (!columnArrow || !columnArrow[columnName])
                return 'unsorted';

            return columnArrow[columnName].mark;
        };
    }


    cms.directive('spProductList', [function spProductListDirectiveFactory() {
        return {
            scope: {},
            controller: ['$scope', 'popups', '$productDetailService', 'cmsBtJmPromotionExportTaskService', 'jmPromotionDetailService', 'spDataService', '$routeParams', 'alert', '$translate', 'confirm', 'notify', 'platformMappingService', SProductListDirectiveController],
            templateUrl: '/modules/cms/views/jmpromotion/sp.product-list.directive.html'
        }
    }]);
});