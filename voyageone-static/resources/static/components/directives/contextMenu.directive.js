/**
 * @Name:    context menu
 * @Date:    2015-05-13 12:55:42
 *
 * @User:    Jonas
 * @Version: 0.0.2
 */

define(["components/app"], function(app){
    var isBound = false;
    var opener;

    app.directive("contextMenu", ["$document", "$position", contextMenu]);

    function contextMenu($document, $position) {
        return {
            restrict: "A",
            link: function(scope, elem, attr) {
                var elemId = attr.contextMenu;
                if (!elemId) return;

                var menu = $document.find("#" + elemId);

                elem.click(openMenu);

                function openMenu(evt) {

                    var host = $position.offset(elem);

                    menu.css({display: 'block'});

                    var menuWidth = menu.prop('offsetWidth');

                    menu.css({
                        top: host.top + host.height + 'px',
                        left: host.left - (menuWidth > host.width ? (menuWidth - host.width) : 0) + 'px'
                    });

                    opener = evt.target;

                    doBind(false);
                }

                function closeMenu(evt) {
                    if (evt.target == opener) {
                        return;
                    }

                    menu.css({
                        display: 'none'
                    });

                    doBind(true);
                }

                function doBind(isUn) {
                    if (!isUn && isBound) return;
                    $document[isUn ? 'unbind':'bind']('click', closeMenu);
                    isBound = !isUn;
                }
            }
        }
    }

});
