/**
 * @Description:
 * 引入对上传框插件 fileStyle 的指令支持 基于Bootstrap Filestyle
 * @Date:    2015-11-19 17:35:22
 * @User:    Jonas
 * @Version: 2.0.0
 */
angular.module("voyageone.angular.directives").directive("fileStyle", function () {

    function FileStyleController($scope,$element){
        this.scope = $scope;
        this.element = $element;
    }

    FileStyleController.prototype.init = function(attrs){
        var options;

        if(attrs.fileStyle != null && attrs.fileStyle != "")
            options = eval("(" + attrs.fileStyle + ")");

        this.element.filestyle(options);
    };

    return {
        restrict: "A",
        scope:true,
        controller: FileStyleController,
        controllerAs: 'styleCtrl',
        link: function($scope,$element,$attrs){
            $scope.styleCtrl.init($attrs);
        }
    };
});
