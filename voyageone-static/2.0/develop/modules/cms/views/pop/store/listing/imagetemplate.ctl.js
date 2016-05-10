/**
 * Created by 123 on 2016/4/29.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageTemplateCtl', function ($scope,imageTemplateService, $routeParams) {
        $scope.vm = {"platformList":[],
            "brandNameList":[],
            "productTypeList":[],
            "sizeTypeList":[]
        };
        $scope.model = {};
        $scope.datePicker = [];
        $scope.initialize  = function () {
            //if(context)
            //{
            //    $scope.model=context;
            //}
                imageTemplateService.init().then(function (res) {
                    $scope.vm.platformList = res.data.platformList;
                    $scope.vm.brandNameList = res.data.brandNameList;
                    $scope.vm.productTypeList = res.data.productTypeList;
                    $scope.vm.sizeTypeList = res.data.sizeTypeList;
                    //   $scope.search();
                })
        };
        $scope.ok = function(){
                imageTemplateService.save($scope.model).then(function (res) {

                    $scope.$close();
                }, function (res) {
                })

        }

    });
});

