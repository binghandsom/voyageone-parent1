/**
 * Created by 123 on 2016/5/4.
 */
define([
    'angularAMD',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (angularAMD, _) {

    angularAMD.controller('popImageTemplatePreviewCtl', function ($scope,imageTemplateService,context,$routeParams) {
        //$scope.vm = {templateParameterList:[]};
        $scope.vm = {templateParameterList:[]};
        $scope.templateContent="";
        $scope.initialize = function () {
            if (context) {
                $scope.templateContent=context
            }
          //"getTemplateParameter":"getTemplateParameter",
          //"getDownloadUrl":"getDownloadUrl"
            imageTemplateService.getTemplateParameter( $scope.templateContent).then(function (res) {
                angular.forEach(res.data, function (value) {
                    $scope.vm.templateParameterList.push({value:value});//[index]=value;
                });
                console.log( $scope.vm.templateParameterList);
            })
        };
        $scope.openPreview=function()
        {
            var templateParameter=[];
            angular.forEach($scope.vm.templateParameterList,function(kv){
                templateParameter.push(kv.value);
            });
            console.log(templateParameter);
            imageTemplateService.getDownloadUrl({templateContent:$scope.templateContent,templateParameter:templateParameter}).then(function (res) {
                window.open(res.data,"Preview image"+Math.random());
                console.log( res.data);
            })

        }
    });
});