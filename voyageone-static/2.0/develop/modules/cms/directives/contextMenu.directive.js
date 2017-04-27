/**
 * @description 鼠标右键出现菜单
 */
define([
    'cms'
], function (cms) {

    cms.run(['$templateCache', function ($templateCache) {
        $templateCache.put('contextmenu.default.html', '<ul><li ng-repeat="menu in menus" ng-click="menu.fun(menu.text)">{{menu.text}}' +
            '<ul ng-if="menu.submenu"><li ng-repeat="submenu in menu.submenu" ng-click="submenu.fun(submenu.text)">{{submenu.text}}</li></ul>' +
            '</li></ul>')

    }]).directive('contextMenu', ["$document", "$compile", "$rootScope", "jayaContextMenu", function ($document, $compile, $rootScope, jayaContextMenu) {
        return {
            restrict: 'A',
            link: function (scope, ele) {
                ele.bind('contextmenu', function () {
                    return false
                });
                ele.bind('click', function () {
                    jayaContextMenu.close();
                })
            }
        }
    }])

        .directive('mouseOptions',
            ["$document", "$compile", "$rootScope", "jayaContextMenu",
                function ($document, $compile, $rootScope, jayaContextMenu) {
                    return {
                        restrict: 'A',
                        scope: {mouseOptions: "=mouseOptions"},
                        link: function (scope, ele) {

                            ele.bind('contextmenu', function (e) {
                                window.event.returnValue = false;
                                return jayaContextMenu.open({
                                    clientX: e.clientX,
                                    clientY: e.clientY,
                                    winWidth: window.innerWidth || document.documentElement.clientWidth || document.body.clientWidht,
                                    winHeight: window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight,
                                    menus: scope.mouseOptions
                                });
                            })
                        }
                    }
                }])


        .factory('jayaContextMenu',
            ['$rootScope', '$document', '$compile',
                function ($rootScope, $document, $compile) {
                    var menu, leftOrRight, topOrBottom, menuScope;
                    var body = $document.find('body').eq(0);

                    var menuOptions = {
                        type: 'default'
                    };

                    $document.find('body').bind('mousedown', function (e) {

                        if (!(e.toElement.closest('div').id === 'contextmenu')) {
                            contentmenu.close();
                        }
                    });

                    var contentmenu = {
                        close: function () {
                            if (menu) {
                                menu.remove();
                                menu = null;
                            }
                        },
                        open: function (options) {
                            menuOptions = angular.extend(menuOptions, options);
                            if (menuOptions.clientX * 2 > menuOptions.winWidth) {
                                leftOrRight = 'right: ' + (menuOptions.winWidth - menuOptions.clientX) + 'px'
                            } else {
                                leftOrRight = 'left: ' + menuOptions.clientX + 'px'
                            }
                            if (menuOptions.clientY * 2 > menuOptions.winHeight) {
                                topOrBottom = 'bottom: ' + (menuOptions.winHeight - menuOptions.clientY) + 'px'
                            } else {
                                topOrBottom = 'top: ' + menuOptions.clientY + 'px'
                            }
                            switch (menuOptions.type) {
                                case 'default':

                                    menu = angular.element('<div id="contextmenu" class="list-group" context-menu></div>');
                                    menu.attr({
                                        'style': leftOrRight + ';' + topOrBottom
                                    }).html('' +
                                        '<ul>' +
                                        '<li ng-repeat="menu in menus" class="list-group-item">' +
                                        '<a href="javascript:void(0)" ng-click = "$Func()"><i class="fa fa-copy"></i>&nbsp;{{menu.text}}</a>' +
                                        '</li>' +
                                        '</ul>');

                                    var menuScope = $rootScope.$new();
                                    menuScope.menus = menuOptions.menus;
                                    menuScope.$Func = menuScope.menus[0].event;

                                    body.append($compile(menu)(menuScope));
                                    $rootScope.$apply();
                                    break;
                            }

                        }
                    };

                    return contentmenu
                }]);

});
