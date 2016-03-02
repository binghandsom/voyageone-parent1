/**
 * Created by linanbin on 15/12/7.
 */

define([
    'modules/cms/controller/popup.ctl'
], function () {

    function attributeController($scope, $routeParams, attributeService) {
        $scope.vm = {
            saveInfo: {
                cat_path:"",
                unsplitFlg:""
            },
            sameAttr: "",
            unvalList: [],
            valList: [],
        };

        $scope.initialize = initialize;
        $scope.save = save;

        /**
         * 初始化数据:
         * unsplitFlg默认值缺省，接收两个List
         * 初始化属性已翻译及未翻译列表
         */
        $scope.flg = false;
        function initialize () {
            console.log(999);
            $scope.vm.unsplitFlg="";
            //
            //if($scope.flg){
            //}else{
            //    $scope.vm.cat_path="0";
            //$scope.vm.cat_path= ;
            //
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
        }
    }

    attributeController.$inject = ['$scope', '$routeParams', 'attributeService'];
    return attributeController;
});
