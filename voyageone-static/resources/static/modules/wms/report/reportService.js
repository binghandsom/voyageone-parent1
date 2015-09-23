/**
 * @author sky
 * @date 20150720
 * @version 0.0.1
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {
    wms.service("reportService", [
        'ajaxService',
        'wmsActions',
        function (ajaxService, wmsActions) {

            this.doInit = function (data, scope) {
                return ajaxService.ajaxPost(data, wmsActions.report.init, scope);
            };

        }]);
});