/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function attributeValueController($scope, $routeParams, attributeValueService, attributeService, feedMappingService, $translate,notify) {
        $scope.vm = {
            skip:"",
            limit:"",
            total:"",
            path:"",
            categoryList:[],
            resultData: []
        };

        $scope.searchInfo= {};
        $scope.searchInfo.cat_path=$routeParams.catPath;
        $scope.searchInfo.sts = 1;
        $scope.searchInfo.propName ="";
        $scope.searchInfo.propValue ="";

        // 检索页
        $scope.searchInfo.page = 0;
        $scope.searchInfo.offset = 0;
        // 页面大小
        $scope.searchInfo.rows = 0;

        $scope.pageOption = {
            curr: 1,
            size: 10,
            total: 1
        };

        $scope.initialize = initialize;
        $scope.clear = clear;
        $scope.search = search;
        $scope.openAddAttributeValue = openAddAttributeValue;
        $scope.flg = false;

        function initialize () {
            $scope.flg = $routeParams.catPath == "0" ? true : false;
            attributeService.getCatList().then(function (res){
                // 给画面的categorylist 赋值
                $scope.vm.categoryList = res.data.categoryList;
                //$scope.searchInfo.cat_path=$routeParams.catPath;
                //$scope.search();
                attributeValueService.init({cat_path:$routeParams.catPath})
                    .then(function (res) {
                        $scope.vm.total = res.data.total;
                        //$scope.vm.categoryList = res.data.categoryList;
                        $scope.vm.resultData = res.data.resultData;
                    });

            })
        }

        function save(){
            $scope.changeData = {
                value_translation : ""
            };
            attributeValueService.save(changeData)
                .then(function (res) {
                    if(res.code == 0){
                        notify.success($translate.instant('TXT_MSG_UPDATE_SUCCESS'));
                        $scope.$parent.initialize();

                    }else if(res.code == 1){
                        notify($translate.instant('TXT_MSG_PRODUCT_IS_PUBLISHING'));
                    }
                });
            $scope.$close();
        }

        function clear(){
            $scope.searchInfo= {};
            $scope.searchInfo.categorySelected=$routeParams.catPath;
            $scope.searchInfo.sts = 1;
            $scope.searchInfo.propName ="";
            $scope.searchInfo.propValue ="";
        }

        function search(){
            $scope.search.page = $scope.pageOption.curr - 1;
            $scope.search.rows = $scope.pageOption.size;
            $scope.search.offset = $scope.search.page * $scope.search.rows;

            attributeValueService.init({cat_path:$routeParams.catPath},$scope.searchInfo)
                .then(function (res) {
                    $scope.vm.total = res.data.total;
                    $scope.vm.categoryList = res.data.categoryList;
                    $scope.vm.resultData =res.resultData;
                })
        }


        //打开添加自定义属性popup -- newAttribute
        function openAddAttributeValue (openAddNewValue) {

            openAddNewValue({
                from: null
            }).then( function (res) {
                addNewAttribute (res)
                }
            );

            //feedMappingService.getMainCategories()
            //    .then(function (res) {
            //
            //        openAddNewValue({
            //
            //            categories: res.data,
            //            from: null
            //        }).then(function (res) {
            //            addNewAttribute (res)
            //            }
            //        );
            //
            //    });
        }

        function addNewAttribute (nData) {
            if (_.isEmpty(nData.value_translation)) {
                $scope.vm.unvalList.push({"prop_original":nData.prop_original, "cat_path":"0"})
            } else {
                $scope.vm.valList.push({"prop_original":nData.prop_original, "prop_translation": nData.prop_translation, "cat_path":"0"})
            }
        }

    }

    attributeValueController.$inject = ['$scope', '$routeParams', 'attributeValueService', 'attributeService','feedMappingService','$translate','notify'];
    return attributeValueController;
});
