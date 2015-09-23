/**
 * @Name:    homeController.js
 * @Date:    2015/2/3
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var accountApp = require ('modules/core/core.module');
    require ('modules/core/menu/menu.service');
    require ('modules/core/popup/popAnnouncement.ctl');

    accountApp.controller ('homeController', ['$scope', 'homeService', 'ngDialog', 'corePopupPages',
        function ($scope, homeService, ngDialog, corePopupPages) {

            $scope.popupAnnouncement = {};

            /**
             * get the public and company announcement info.
             */
            $scope.initialize = function () {
                homeService.doGetAnnouncement ()
                    .then (function (data) {
                        $scope.publicAnnouncementList = data.publicAnnouncementList;
                        $scope.companyAnnouncementList = data.companyAnnouncementList;
                    })
            };

            /**
             * get one announcement info by id.
             * @param id
             */
            $scope.doGetAnnouncementContent = function (id) {
                $scope.popupAnnouncement.id = id;
                ngDialog.open ({
                    template: corePopupPages.popAnnouncement.page,
                    controller: corePopupPages.popAnnouncement.controller,
                    scope: $scope,
                    popWindowSize: "650px"
                });
            };
        }]);
});
