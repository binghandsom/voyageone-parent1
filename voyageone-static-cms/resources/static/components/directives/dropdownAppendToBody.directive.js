/**
 * @Name:    dropdownAppendToBody
 * @Date:    2015-05-13 10:52:08
 *
 * @User:    Jonas
 * @Version: 0.0.1
 * @description: UI-BootStrap 中 Dropdown 的自定义补丁。为项目中不支持的 dropdown-append-to-body 提供支持
 */

define(["components/app"], function (app) {
    app.directive("dropdownAppendToBody", [
        "$document", "$position",
        function ($document, $position) {
            return {
                restrict: "A",
                require: '?^dropdown',
                link: function (scope, elem, attr, controller) {

                    var position = attr.dropdownAppendToBody || 'bottom-left';

                    controller.dropdownMenu = elem.find("ul.dropdown-menu");

                    $document.find('body').append(controller.dropdownMenu);

                    elem.on('$destroy', function () {
                        controller.dropdownMenu.remove();
                    });

                    scope.$watch(function () {
                        return controller.isOpen();
                    }, function (isOpen) {
                        var pos = $position.positionElements(elem, controller.dropdownMenu, position, true);
                        controller.dropdownMenu.css({
                            top: pos.top + 'px',
                            left: pos.left + 'px',
                            display: isOpen ? 'block' : 'none'
                        });
                    });
                }
            }
        }
    ]);
});

