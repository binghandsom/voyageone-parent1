/**
 * @description:
 * 提供"滚动到"和"滚动到顶部"功能
 *
 * @example: <some-element scroll-to="#cssSelector">something</>
 * @example: <some-element scroll-to="#cssSelector, 200">something</>
 * @example: <some-element scroll-to="300, 200">something</>
 * @example: <some-element scroll-to="#cssSelector, 200, -35">something</>
 * @example: <a href="javascript:void(0)" go-top="200">xxx</a>
 * @user:    tony-piao, jonas
 * @version: 0.2.8
 * @since    0.2.0
 */
angular.module("voyageone.angular.directives")
    .directive("scrollTo", function () {
        return {
            restrict: "A",
            scope: false,
            link: function (scope, element, attr) {
                var option = attr.scrollTo;
                if (!option)
                    return;
                option = option.split(',');
                option[1] = parseInt(option[1]) || 200;
                option[2] = parseInt(option[2]) || 0;

                element.on("click", function () {
                    var option0;
                    if (option[0]) {
                        option0 = $(option[0]);
                        if (option0.length) {
                            option0 = option0.offset().top;
                        } else {
                            option0 = parseInt(option[0]) || 0;
                        }
                    } else {
                        option0 = 0;
                    }
                    $("body").animate({scrollTop: option0 + option[2]}, option[1]);
                    return false;
                });
            }
        };
    })
    .directive("goTop", function () {
        return {
            restrict: "A",
            scope: false,
            link: function (scope, element, attrs) {
                var speed = +attrs.goTop;
                $(element).on("click", function () {
                    $("body").animate({scrollTop: 0}, speed);
                    return false;
                });
            }
        };
    });