/**
 * @author tony-piao
 * feed产品概述
 */
define([
    'cms'
],function(cms) {
    cms.directive("feedSchema", function (productDetailService,notify,alert) {
        return {
            restrict: "E",
            templateUrl : "views/product/feed.component.tpl.html",
            scope: {productInfo: "=productInfo"},
            link: function (scope) {
                scope.pageAnchor = pageAnchor;
                scope.updateFeedInfo = updateFeedInfo;

                /**
                 * feed更新操作  updateProductAtts
                 */

                function updateFeedInfo(){
                    if(scope.feedFrom.$invalid){
                        return alert("保存失败，请查看已匹配属性是否填写正确！");
                    }

                    productDetailService.updateProductAtts({prodId:scope.productInfo.productId,feedInfo:scope.productInfo.feedInfo}).then(function(){
                        notify.success("更新成功!");
                    },function(){
                        alert("更新失败！");
                    });
                }
                /**
                 * 右侧导航栏
                 * @param index div的index
                 * @param speed 导航速度 ms为单位
                 */
                function pageAnchor(index,speed){
                    var offsetTop = 0;
                    if(index != 1)
                        offsetTop = ($("#feed"+index).offset().top);
                    $("body").animate({ scrollTop:  offsetTop-70}, speed);
                }

            }
        };
    });
});
