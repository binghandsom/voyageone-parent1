/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function attributeValueController($scope, $routeParams, attributeValueService, attributeService, $translate,notify) {
        $scope.vm = {
            searchInfo: {
                cat_path: $routeParams.catPath,
                catPathTxt: $routeParams.catPath,
                sts : "2",
                propName: "",
                propValue: ""
            },
            path:"",
            categoryList:[],
            resultData: [],
            valuesPageOption: {curr: 1, total: 0, fetch: search}
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.openAddAttributeValue = openAddAttributeValue;
        $scope.save = save;

        function initialize () {
            attributeService.getCatList().then(function (res){
                // 给画面的categorylist 赋值
                $scope.vm.categoryList = res.data.categoryList;
                if ($scope.vm.searchInfo.catPathTxt == '0') {
                    $scope.vm.searchInfo.catPathTxt = '共通属性';
                }
                // 获取页面一览
                $scope.search();
            })
        }

        /**
         * 清空检索条件
         */
        function clear(){
            $scope.vm.searchInfo={
                cat_path: null,
                catPathTxt: null,
                sts: "2",
                propName: "",
                propValue: ""
            };
        }

        /**
         * 检索数据
         */
        function search(page){
            $scope.vm.valuesPageOption.curr = !page ? $scope.vm.valuesPageOption.curr : page;
            $scope.vm.searchInfo.skip = $scope.vm.valuesPageOption.curr;
            $scope.vm.searchInfo.limit = $scope.vm.valuesPageOption.size;
            if ($scope.vm.searchInfo.catPathTxt == '共通属性') {
                $scope.vm.searchInfo.cat_path = '0';
            } else {
                $scope.vm.searchInfo.cat_path = $scope.vm.searchInfo.catPathTxt;
            }
            attributeValueService.init($scope.vm.searchInfo)
                .then(function (res) {
                    $scope.vm.valuesPageOption.total = res.data.total;
                    $scope.vm.resultData = res.data.resultData;
                })
        }

        /**
         * 保存数据
         */
        function save(item){
            var changeData = {
                value_id : item.value_id,
                value_translation: item.value_translation
            };
            attributeValueService.save(changeData)
                .then(function () {
                    notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                    $scope.search();
                });
        }

        /**
         * 打开添加自定义属性popup -- newAttribute
         * @param openAddNewValue
         */
        function openAddAttributeValue (openAddNewValue) {
            var params = $scope.vm.searchInfo;
            if (params.catPathTxt == '共通属性') {
                params.cat_path = '0';
            } else {
                params.cat_path = params.catPathTxt;
            }
            params.skip = 0;
            params.limit = 0;
            attributeValueService.init(params)
                .then(function (res) {
                    // 要先过滤数据，合并相同属性名
                    var valList = res.data.resultData;
                    var valMapNew = {};
                    for (var i = 0, l = valList.length; i < l; i++) {
                        var nowObj = valList[i];
                        valMapNew[nowObj.prop_id] = nowObj;
                    }
                    var valListNew = _.toArray(valMapNew);
                    openAddNewValue({
                        from: $scope.vm.searchInfo.cat_path,
                        categoryList: $scope.vm.categoryList,
                        valueList: valListNew
                    }).then( function () {
                            $scope.search();
                        }
                    );
                })


        }

    }

    attributeValueController.$inject = ['$scope', '$routeParams', 'attributeValueService', 'attributeService','$translate','notify'];
    return attributeValueController;
});
