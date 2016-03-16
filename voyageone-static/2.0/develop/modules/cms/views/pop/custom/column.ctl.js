/**
 * Created by linanbin on 15/12/7.
 */
define([
    'angularAMD'
], function (angularAMD) {

    angularAMD.controller('popCustomColumnCtl', function ($scope,$searchAdvanceService) {
        $scope.cus = {
            customProps:[],
            commonProps:[]
        };
        $scope.cusData = {
            customProps:[],
            commonProps:[]
        };
        $scope.initialize = initialize;
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
            console.log($scope.cusData.commonProps);
        };

        $scope.updateSelection = function($event,item,flg){
            var checkbox = $event.target;
            var action = (checkbox.checked?'add':'remove');

            if(flg){
                if(action == 'add'){
                    $scope.cusData.customProps.push(item);
                }
                if(action == 'remove'){
                    var idx_cus = $scope.cusData.customProps.indexOf(item);
                    $scope.cusData.customProps.splice(idx_cus,1);
                }
            }else{
                if(action == 'add'){
                    $scope.cusData.commonProps.push(item);
                }
                if(action == 'remove'){
                    var idx_com = $scope.cusData.commonProps.indexOf(item);
                    $scope.cusData.commonProps.splice(idx_com,1);
                }
            }
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