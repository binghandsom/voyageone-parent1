/**
 * Created by sofia on 5/6/2016.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function () {
    function cmsCatController($scope, $routeParams, $translate, categorySettingService, platformMappingService, cookieService, confirm, notify, alert) {

        $scope.vm = {
            curMCatId: '',
            curMCatPath: '',
            curPCatId: '',
            curPCatPath: '',
            curMCatLvl: -1,
            curPCatLvl: -1,
            selMIdxList: [-1,-1,-1,-1,-1],
            selPIdxList: [-1,-1,-1,-1,-1],
            searchMKeys: ['','','','',''],
            searchPKeys: ['','','','',''],
            mCatListOrg: [[],[],[],[],[]],
            pCatListOrg: [[],[],[],[],[]],
            mCatList: [[],[],[],[],[]],
            pCatList: [[],[],[],[],[]]
        };

        $scope.initialize = initialize;
        // 获取初始化数据
        function initialize() {
            if ($routeParams.type == "1") {
                // 由属性合并画面而来
                // 画面显示用的数据从cookie中取得
                var _catvmdata = cookieService.get("_catvmdata");
                _.extend($scope.vm, _catvmdata);
            }
            var para = {catLevel: 0};
            var catObj = null;
            categorySettingService.getMasterSubCategoryList(para)
                .then(function (res) {
                    $scope.vm.mCatListOrg[0] = res.data.catList;
                    $scope.vm.mCatList[0] = res.data.catList;

                    if ($routeParams.type == "1") {
                        // 重新显示数据
                        research(0, 0);
                        // 显示主数据，确定类目级别以及是否有过滤查询
                        if ($scope.vm.curMCatLvl > 0) {
                            regetMasterData(para, 0);
                        }
                    }
                });
            platformMappingService.getCarts().then(function (res) {
                $scope.vm.carts = res.data;
            });

            if ($routeParams.type == "1") {
                // 重新显示数据
                // 显示平台数据
                var para2 = {catLevel: 0, cartId: $scope.vm.selCart};
                categorySettingService.getPlatformSubCategoryList(para2)
                    .then(function (res) {
                        $scope.vm.pCatListOrg[0] = res.data.catList;
                        $scope.vm.pCatList[0] = res.data.catList;
                        research(1, 0);
                        if ($scope.vm.curPCatLvl > 0) {
                            regetPlatformData(para2, 0);
                        }
                    });
            }
        };

        function regetMasterData(para, idx) {
            if (idx > $scope.vm.curMCatLvl) {
                return;
            }
            para.catLevel = idx;
            var catObj = $scope.vm.mCatList[idx][$scope.vm.selMIdxList[idx]];
            if (catObj == undefined) {
                return;
            }
            if (catObj.isParent == 0) {
                return;
            }
            if (idx == 0) {
                para.rootCatId = '';
            } else {
                para.rootCatId = $scope.vm.mCatList[0][$scope.vm.selMIdxList[0]].catId;
            }
            para.catId = catObj.catId;

            categorySettingService.getMasterSubCategoryList(para)
                .then(function (res) {
                    $scope.vm.mCatListOrg[idx + 1] = res.data.catList;
                    $scope.vm.mCatList[idx + 1] = res.data.catList;
                    research(0, idx + 1);
                    regetMasterData(para, idx + 1);
                });
        }

        function regetPlatformData(para, idx) {
            if (idx > $scope.vm.curPCatLvl) {
                return;
            }
            para.catLevel = idx;
            var catObj = $scope.vm.pCatList[idx][$scope.vm.selPIdxList[idx]];
            if (catObj == undefined) {
                return;
            }
            if (catObj.isParent == 0) {
                return;
            }
            if (idx == 0) {
                para.rootCatId = '';
            } else {
                para.rootCatId = $scope.vm.pCatList[0][$scope.vm.selPIdxList[0]].catId;
            }
            para.catId = catObj.catId;

            categorySettingService.getPlatformSubCategoryList(para)
                .then(function (res) {
                    $scope.vm.pCatListOrg[idx + 1] = res.data.catList;
                    $scope.vm.pCatList[idx + 1] = res.data.catList;
                    research(1, idx + 1);
                    regetPlatformData(para, idx + 1);
                });
        }

        // 查询类目（类目名称）,只查询本地缓存
        $scope.search = function(catType, catLvl) {
            var nowCatList = null;
            if (catType == 0) {
                // 主数据类目查询
                var qryCatName = $scope.vm.searchMKeys[catLvl];
                var rsList = [];
                if (qryCatName == '') {
                    // 如果输入为空，则重置为最初查询的数据
                    rsList = $scope.vm.mCatListOrg[catLvl];
                    if (rsList == null || rsList == undefined || rsList.length == 0) {
                        return;
                    }
                } else {
                    nowCatList = $scope.vm.mCatList[catLvl];
                    if (nowCatList == null || nowCatList == undefined || nowCatList.length == 0) {
                        return;
                    }
                    for (idx in nowCatList) {
                        var catObj = nowCatList[idx];
                        if (catObj.catName.indexOf(qryCatName) >= 0) {
                            rsList.push(catObj);
                        }
                    }
                }

                // 重置当前选择类目
                if (catLvl > 0) {
                    $scope.vm.curMCatId = $scope.vm.mCatList[catLvl - 1][$scope.vm.selMIdxList[catLvl - 1]].catId;
                    $scope.vm.curMCatPath = $scope.vm.mCatList[catLvl - 1][$scope.vm.selMIdxList[catLvl - 1]].catPath;
                } else {
                    $scope.vm.curMCatId = '';
                    $scope.vm.curMCatPath = '';
                }
                $scope.vm.selMIdxList[catLvl] = -1;

                // 设置类目数据
                $scope.vm.mCatList[catLvl] = rsList;
                clearInfo(0, catLvl, 1);

            } else {
                // 平台类目查询
                var qryCatName = $scope.vm.searchPKeys[catLvl];
                var rsList = [];
                if (qryCatName == '') {
                    // 如果输入为空，则重置为最初查询的数据
                    rsList = $scope.vm.pCatListOrg[catLvl];
                    if (rsList == null || rsList == undefined || rsList.length == 0) {
                        return;
                    }
                } else {
                    nowCatList = $scope.vm.pCatList[catLvl];
                    if (nowCatList == null || nowCatList == undefined || nowCatList.length == 0) {
                        return;
                    }
                    for (idx in nowCatList) {
                        var catObj = nowCatList[idx];
                        if (catObj.catName.indexOf(qryCatName) >= 0) {
                            rsList.push(catObj);
                        }
                    }
                }

                // 重置当前选择类目
                if (catLvl > 0) {
                    $scope.vm.curPCatId = $scope.vm.pCatList[catLvl - 1][$scope.vm.selPIdxList[catLvl - 1]].catId;
                    $scope.vm.curPCatPath = $scope.vm.pCatList[catLvl - 1][$scope.vm.selPIdxList[catLvl - 1]].catPath;
                } else {
                    $scope.vm.curPCatId = '';
                    $scope.vm.curPCatPath = '';
                }
                $scope.vm.selPIdxList[catLvl] = -1;

                // 设置类目数据
                $scope.vm.pCatList[catLvl] = rsList;
                clearInfo(1, catLvl, 1);
            }
        };

        // 设置当前选中的类目，从后台查询子类目
        $scope.catTarget = function(catType, catLvl, $index) {
            var para = {catLevel: catLvl};
            if (catType == 0) {
                // 主数据类目查询
                $scope.vm.curMCatLvl = catLvl;
                $scope.vm.curMCatId = $scope.vm.mCatList[catLvl][$index].catId;
                $scope.vm.curMCatPath = $scope.vm.mCatList[catLvl][$index].catPath;
                $scope.vm.selMIdxList[catLvl] = $index;
                if ($scope.vm.mCatList[catLvl][$index].isParent == 0) {
                    clearInfo(0, catLvl, 1);
                    return;
                }

                para.rootCatId = $scope.vm.mCatList[0][$scope.vm.selMIdxList[0]].catId;
                if (catLvl == 0) {
                    para.rootCatId = '';
                }
                para.catId = $scope.vm.mCatList[catLvl][$index].catId;
                categorySettingService.getMasterSubCategoryList(para)
                    .then(function (res) {
                        $scope.vm.mCatListOrg[catLvl + 1] = res.data.catList;
                        $scope.vm.mCatList[catLvl + 1] = res.data.catList;
                        $scope.vm.searchMKeys[catLvl + 1] = '';
                        clearInfo(0, catLvl, 2);
                    })
            } else {
                // 平台类目查询
                $scope.vm.curPCatLvl = catLvl;
                $scope.vm.curPCatId = $scope.vm.pCatList[catLvl][$index].catId;
                $scope.vm.curPCatPath = $scope.vm.pCatList[catLvl][$index].catPath;
                $scope.vm.selPIdxList[catLvl] = $index;
                if ($scope.vm.pCatList[catLvl][$index].isParent == 0) {
                    clearInfo(1, catLvl, 1);
                    return;
                }

                para.rootCatId = $scope.vm.pCatList[0][$scope.vm.selPIdxList[0]].catId;
                if (catLvl == 0) {
                    para.rootCatId = '';
                }
                para.catId = $scope.vm.pCatList[catLvl][$index].catId;
                para.cartId = $scope.vm.selCart;
                categorySettingService.getPlatformSubCategoryList(para)
                    .then(function (res) {
                        $scope.vm.pCatListOrg[catLvl + 1] = res.data.catList;
                        $scope.vm.pCatList[catLvl + 1] = res.data.catList;
                        $scope.vm.searchPKeys[catLvl + 1] = '';
                        clearInfo(1, catLvl, 2);
                    })
            }
        };

        function clearInfo(catType, catLvl, begIdx) {
            if (catType == 0) {
                for (var i = begIdx; i < 5; i++) {
                    if (catLvl + i < 5) {
                        $scope.vm.mCatListOrg[catLvl + i] = [];
                        $scope.vm.mCatList[catLvl + i] = [];
                        $scope.vm.searchMKeys[catLvl + i] = '';
                    }
                }
            } else {
                for (var i = begIdx; i < 5; i++) {
                    if (catLvl + i < 5) {
                        $scope.vm.pCatListOrg[catLvl + i] = [];
                        $scope.vm.pCatList[catLvl + i] = [];
                        $scope.vm.searchPKeys[catLvl + i] = '';
                    }
                }
            }
        };

        // 查询类目（类目名称）,只查询本地缓存，只在画面切换时使用
        function research(catType, catLvl) {
            var nowCatList = null;
            if (catType == 0) {
                // 主数据类目查询
                var qryCatName = $scope.vm.searchMKeys[catLvl];
                if (qryCatName != '') {
                    var rsList = [];
                    nowCatList = $scope.vm.mCatListOrg[catLvl];
                    if (nowCatList == null || nowCatList == undefined || nowCatList.length == 0) {
                        return;
                    }
                    for (idx in nowCatList) {
                        var catObj = nowCatList[idx];
                        if (catObj.catName.indexOf(qryCatName) >= 0) {
                            rsList.push(catObj);
                        }
                    }
                    // 设置类目数据
                    $scope.vm.mCatList[catLvl] = rsList;
                }
            } else {
                // 平台类目查询
                var qryCatName = $scope.vm.searchPKeys[catLvl];
                if (qryCatName != '') {
                    var rsList = [];
                    nowCatList = $scope.vm.pCatListOrg[catLvl];
                    if (nowCatList == null || nowCatList == undefined || nowCatList.length == 0) {
                        return;
                    }
                    for (idx in nowCatList) {
                        var catObj = nowCatList[idx];
                        if (catObj.catName.indexOf(qryCatName) >= 0) {
                            rsList.push(catObj);
                        }
                    }
                    // 设置类目数据
                    $scope.vm.pCatList[catLvl] = rsList;
                }
            }
        };

        // 切换平台类目
        $scope.loadCategories = function() {
            $scope.vm.curPCatId = '';
            $scope.vm.curPCatPath = '';
            $scope.vm.curPCatLvl = -1;
            $scope.vm.selPIdxList = [-1,-1,-1,-1,-1];
            $scope.vm.searchPKeys = ['','','','',''];
            $scope.vm.pCatListOrg = [[],[],[],[],[]];
            $scope.vm.pCatList = [[],[],[],[],[]];

            var para = {catLevel: 0, cartId: $scope.vm.selCart};
            categorySettingService.getPlatformSubCategoryList(para)
                .then(function (res) {
                    $scope.vm.pCatListOrg[0] = res.data.catList;
                    $scope.vm.pCatList[0] = res.data.catList;
                })
        };

        // 合并类目属性
        $scope.compAttr = function() {
            // 先检查是否选择了类目
            if ($scope.vm.curMCatId == undefined || $scope.vm.curMCatId == '') {
                alert("请选择主数据类目");
                return;
            }
            if ($scope.vm.curPCatId == undefined || $scope.vm.curPCatId == '') {
                alert("请选择平台类目");
                return;
            }
            // 检查是否是叶子类目，即该类目不能再有子类目
            // 主数据类目
            var catObj = $scope.vm.mCatList[$scope.vm.curMCatLvl][$scope.vm.selMIdxList[$scope.vm.curMCatLvl]];
            if (catObj.isParent == 1) {
                alert("请选择主数据叶子类目");
                return;
            }

            // 平台类目
            catObj = $scope.vm.pCatList[$scope.vm.curPCatLvl][$scope.vm.selPIdxList[$scope.vm.curPCatLvl]];
            if (catObj.isParent == 1) {
                alert("请选择平台叶子类目");
                return;
            }

            // 画面跳转
            var _catvmdata = {};
            _.extend(_catvmdata, $scope.vm);
            delete _catvmdata.carts;
            delete _catvmdata.mCatListOrg;
            delete _catvmdata.pCatListOrg;
            delete _catvmdata.mCatList;
            delete _catvmdata.pCatList;
            cookieService.set("_catvmdata", _catvmdata);
            window.location.href = "#/system/categorysetting_catunion";
        };
    };

    cmsCatController.$inject = ['$scope', '$routeParams', '$translate', 'categorySettingService', 'platformMappingService', 'cookieService', 'confirm', 'notify', 'alert'];
    return cmsCatController;
});