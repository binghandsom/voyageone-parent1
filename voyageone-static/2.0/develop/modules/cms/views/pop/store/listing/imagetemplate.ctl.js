/**
 * Created by 123 on 2016/4/29.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    angularAMD.controller('popImageTemplateCtl', function ($scope,imageTemplateService,alert,context, $routeParams) {
        $scope.vm = {"platformList":[],
            "brandNameList":[],
            "productTypeList":[],
            "sizeTypeList":[],
            "imageTemplateList":[]
        };
        $scope.model = {};
        $scope.datePicker = [];
        $scope.initialize  = function () {

                imageTemplateService.init().then(function (res) {
                    $scope.vm.platformList = res.data.platformList;
                    $scope.vm.brandNameList = res.data.brandNameList;
                    $scope.vm.productTypeList = res.data.productTypeList;
                    $scope.vm.sizeTypeList = res.data.sizeTypeList;
                    $scope.vm.imageTemplateList=res.data.imageTemplateList;
                    //   $scope.search();
                    if(context.model)
                    {
                        imageTemplateService.get(context.model.imageTemplateId).then(function (res) {
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
                    console.log(res);
                      if(res.data.result) {
                          $scope.$close();
                          context.search();
                      }
                      else
                      {
                         // self.alert({id: 'TXT_MSG_INVALID_FEILD', values: {fields: invalidNames.join(', ')}});
                          return alert(res.data.msg);
                      }
                }, function (res) {
                })

        }

    });
});

