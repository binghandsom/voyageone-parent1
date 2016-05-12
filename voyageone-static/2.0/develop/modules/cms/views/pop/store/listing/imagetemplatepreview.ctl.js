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
        $scope.vm = {templateParameterList:{}};
        $scope.templateContent="";
        $scope.initialize = function () {
            if (context) {
                $scope.templateContent=context
            }
          //"getTemplateParameter":"getTemplateParameter",
          //"getDownloadUrl":"getDownloadUrl"
            imageTemplateService.getTemplateParameter( $scope.templateContent).then(function (res) {

                //$scope.vm.templateParameterList=res.data;

                angular.forEach(res.data, function (value) {
                    $scope.vm.templateParameterList[index]=value;
                });
                //for(var i=0;i<res.data.length;i++)
                //{
                //    $scope.vm.templateParameterList.push({i:res.data[i]});
                //}
                console.log( $scope.vm.templateParameterList);
            })
        };
        $scope.openPreview=function()
        {
            console.log(_.values( $scope.vm.templateParameterList));
        }
    });
});