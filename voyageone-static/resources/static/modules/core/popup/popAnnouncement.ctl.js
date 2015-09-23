/**
 * @Name:    popAnnouncement.ctl.js
 * @Date:    2015/5/14
 *
 * @User:    Edward
 * @Version: 1.0.0
 */

define (function (require) {
    var coreApp = require ('modules/core/core.module');
    require ('modules/core/popup/popup.service');

    coreApp.controller ('popAnnouncementController',
        ['$scope', 'popAnnouncementService',
            function ($scope, popAnnouncementService) {
                //var _ = require ('underscore');

                popAnnouncementService.doGetAnnouncementContent ($scope.popupAnnouncement.id)
                    .then (function (data) {
                        $scope.popupAnnouncement.announcementInfo = data.contentInfo;
                    });

            }])
});
