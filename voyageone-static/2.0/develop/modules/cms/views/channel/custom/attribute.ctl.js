/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function attributeController($scope, $routeParams, attributeService, feedMappingService, notify, confirm, $translate, $location) {
        this.feedMappingService = feedMappingService;
        $scope.vm = {
            saveInfo: {
                cat_path:"",
                unsplitFlg:""
            },
            sameAttr: "",
            unvalList: [],
            valList: []
        };

        $scope.initialize = initialize;
        $scope.save = save;
        $scope.openCategoryMapping = openCategoryMapping;
        $scope.bindCategory = bindCategory;
        $scope.openAddAttributeValue = openAddAttributeValue;

        /**
         * 初始化数据:
         * unsplitFlg默认值缺省，接收两个List
         * 初始化属性已翻译及未翻译列表
         */
        $scope.flg = false;
        function initialize () {
            $scope.vm.unsplitFlg="";
            //var data = ;
            $scope.flg = $routeParams.catPath == "0" ? true : false;

            attributeService.init({cat_path:$routeParams.catPath})
                .then(function (res) {
                    $scope.vm.sameAttr = res.data.sameAttr;
                    $scope.vm.unvalList = res.data.unvalList;
                    $scope.vm.valList = res.data.valList;

                    //if($scope.flg){
                    //    $scope.flg = false;
                    //}else{
                    //    $scope.flg = true;
                    //}
                });

        }

        //$scope.onclick = onclick;
        //function onclick () {
        //    initialize ()
        //}

        //移除已翻译列表中的一条数据，并插入到未翻译列表中
        $scope.remove = function(item){
            $scope.index = $scope.vm.valList.indexOf(item);
            $scope.i = $scope.vm.valList.splice($scope.index,1);
            $scope.vm.unvalList.push($scope.i[0]);
        };

        //移除未翻译列表中的一条数据，并插入到已翻译列表中
        //且为其添加一个key:prop_translation,值为空
        $scope.addVal = function(inx){
            $scope.i = $scope.vm.unvalList.splice(inx,1);

            if(!$scope.i[0].prop_translation){

                $scope.i[0].prop_translation = "";
            }
            $scope.vm.valList.push($scope.i[0]);
        };

        //保存已翻译和未翻译数据
        function save(){
            var nData = {};
            nData.saveInfo = $scope.vm.saveInfo;
            nData.valList = $scope.vm.valList;
            nData.unvalList = $scope.vm.unvalList;
            nData.sameAttr = $scope.vm.sameAttr;
            attributeService.save(nData);
            //attributeService.save(nData)
            //    .then(function(res){
            //
            //    });
        }

        //页面跳转
        $scope.getTranslation = function (catPath){
            $location.path("/channel/custom/value" + catPath);
        };

        //打开类目选择popup
        function openCategoryMapping (popupNewCategory) {

            //feedMappingService.getMainCategories()
            attributeService.getCatTree()
                .then(function (res) {

                    popupNewCategory({

                        categories: res.data,
                        from: null
                    }).then( function (res) {
                            bindCategory (res.selected.catPath)
                        }
                    );

                });
        }
        //类目选择跳转至类目页面
        function bindCategory (catPath) {
            $location.path("/channel/custom/" + catPath);
        }

        //打开添加自定义属性popup -- newAttribute
        function openAddAttributeValue (openAddAttribute) {

            var attributeList = angular.copy($scope.vm.unvalList);
            openAddAttribute({
                from: attributeList.concat($scope.vm.valList)
            }).then( function (res) {
                    delNewAttribute (res)
                }
            );
        }

        function delNewAttribute (nData) {
            if (_.isEmpty(nData.value_translation)) {
                $scope.vm.unvalList.push({"prop_original":nData.prop_original, "cat_path":"0"})
            } else {
                $scope.vm.valList.push({"prop_original":nData.prop_original, "prop_translation": nData.prop_translation, "cat_path":"0"})
            }

            $scope.save();
        }
    }

    attributeController.$inject = ['$scope', '$routeParams', 'attributeService', 'feedMappingService', 'notify', 'confirm', '$translate', '$location'];
    return attributeController;
});
