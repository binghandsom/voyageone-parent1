/**
 * Created by edward-pc1 on 2015/8/14.
 */

define (function (require) {
    var mainApp = require ("modules/app/app.module");

    mainApp.service ('masterService', ['sessionStorageType',
        function (sessionStorageType) {

            this.setCmsMasterData = function (value) {
                sessionStorage.setItem (sessionStorageType.CMS_MASTER_DATA, JSON.stringify (value));
            };

            this.getCmsMasterData = function () {
                if (!_.isUndefined (sessionStorage.getItem (sessionStorageType.CMS_MASTER_DATA)))
                    return JSON.parse (sessionStorage.getItem (sessionStorageType.CMS_MASTER_DATA));
                else
                    return [];
            };

        }]);
});
