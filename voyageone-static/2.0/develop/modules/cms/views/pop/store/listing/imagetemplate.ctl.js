/**
 * Created by 123 on 2016/4/29.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageTemplateCtl', function ($scope,imageTemplateService,context, $routeParams) {
        $scope.vm = {"platformList":[],
            "brandNameList":[],
            "productTypeList":[],
            "sizeTypeList":[]
        };
        $scope.model = {};
        $scope.datePicker = [];
        $scope.initialize  = function () {

                imageTemplateService.init().then(function (res) {
                    $scope.vm.platformList = res.data.platformList;
                    $scope.vm.brandNameList = res.data.brandNameList;
                    $scope.vm.productTypeList = res.data.productTypeList;
                    $scope.vm.sizeTypeList = res.data.sizeTypeList;
                    //   $scope.search();
                    if(context)
                    {
                        imageTemplateService.get(context.imageTemplateId).then(function (res) {
                            $scope.model=res.data;
                            $scope.model.cartId += "";
                            $scope.model.viewType+="";
                            $scope.model.imageTemplateType+="";
                        })
                    }
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

