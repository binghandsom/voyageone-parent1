/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popCustomColumnCtl', function ($scope,$searchAdvanceService) {
        $scope.cus = {
            customProps:[],
            commonProps:[],
            feed_prop_original:"",
            propId:""
        };
        $scope.cusData = {
            customProps:[],
            commonProps:[]
        };
        $scope.initialize = initialize;
        //$scope.isSelect = isSelect;
        /**
         * 初始化数据.
         */
        function initialize () {
            $searchAdvanceService.getCustColumnsInfo()
                .then(function (res) {
                $scope.cus.customProps = res.data.customProps;
                $scope.cus.commonProps = res.data.commonProps;
            })
        }

        /**
         * 确定返回，数据传回前端及用户自定义保存
         */


        $scope.ok = function(){

            console.log($scope.cusData.customProps);
        };

        var updateSelected = function(action,item,inx){
            if(action == 'add'){
                $scope.cusData.customProps.push(item);
            }
            if(action == 'remove'){

                $scope.cusData.customProps.splice(inx,1);
            }
        };

        $scope.updateSelection = function($event,item,inx){
            var checkbox = $event.target;
            var action = (checkbox.checked?'add':'remove');

            updateSelected(action,item,inx);
        };

        /**
         * check 是否选中
         * To do 需要添加一个checkedValue属性
         */
        /*$scope.isSelected = function(sel){
            return sel;
        };*/

    });

});