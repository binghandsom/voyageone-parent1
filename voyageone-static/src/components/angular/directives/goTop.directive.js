/**
 * @Description:
 * 返回页面顶部
 * @example: <a href="javascript:void(0)" go-top = "200">xxx</a>
 * @User:    tony-piao
 * @Version: 0.1.0, 2016-5-24
 */
(function() {
    angular.module("vo.directives.goTop", []).directive("goTop", function() {
        return {
            restrict: "A",
            link: function(scope, element,attrs) {
                 var speed = +attrs.goTop;
                 $(element).on("click",function(){
                     $("body").animate({ scrollTop: 0 }, speed);
                     return false;
                 });
            }
        };
    });
})();