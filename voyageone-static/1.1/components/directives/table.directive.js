/**
 * @Name:    table.directive.js
 * @Date:    2015/5/5
 *
 * @User:    Edward
 * @Version: 1.0.0
 */


 define (function (require) {

    var mainApp = require ('components/app');
    var _ = require ('underscore');

    mainApp.directive ('singleOnSelect', function () {

        return {
            restrict: 'A',
            scope: {singleOnSelect: "="},
            link: function (scope, element, attrs) {

                $ (element[0]).parents ().find ('tr').addClass ("carousel");

                element[0].onclick = function () {

                    var selectedItem = {id: attrs.index};

                    if (_.isEqual (scope.singleOnSelect, selectedItem)) {
                        $ (element[0]).removeClass ("success");
                        scope.singleOnSelect = null;
                    } else {
                        $ (element[0]).parents ().find ('tr').removeClass ("success");
                        $ (element[0]).addClass ("success");
                        scope.singleOnSelect = selectedItem;
                    }
                    scope.$apply ();
                }
            }
        };
    });

    mainApp.directive ('multiOnSelect', function () {

        return {
            restrict: 'A',
            scope: {multiOnSelect: "="},
            link: function (scope, element, attrs) {

                $ (element[0]).parents ().find ('tr').addClass ("carousel");

                element[0].onclick = function () {

                    var selectIndex = attrs.index;

                    // 如果该记录被选中,则从list删除.
                    if (_.contains (scope.multiOnSelect, selectIndex)) {
                        $ (element[0]).removeClass ("success");

                        scope.multiOnSelect.splice(_.indexOf(scope.multiOnSelect, selectIndex), 1);
                        //scope.multiOnSelect = _.without (scope.multiOnSelect, selectIndex);
                    }
                    // 如果该记录以前没有被选中，而标示为选中该记录.
                    else {
                        $ (element[0]).addClass ("success");
                        scope.multiOnSelect.push (selectIndex);
                    }
                    scope.$apply ();
                }
            }
        };
    });
});