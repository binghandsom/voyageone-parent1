/**
 * Created by linanbin on 15/12/4.
 */

define([
    'modules/cms/service/search.service'
], function () {

    // controller
    return function ($scope, $location, searchService) {
        var vm = this;
        vm.name = "";

        $scope.initialize = initialize;

        function initialize () {
            searchService.search({"test": "1"})
                .then(function (data) {
                    vm.name = data;
                })
        }
    };
});