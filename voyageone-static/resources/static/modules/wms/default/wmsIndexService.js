/**
 * @User: Jonas
 * @Date: 2015/3/22
 * @Version: 0.0.3
 */

define(['modules/wms/wms.module'], function(wms) {
  return wms.service('wmsIndexService', [
    'wmsActions', 'ajaxService', function(wmsActions, ajaxService) {
      this.doInit = function(data) {
        return ajaxService.ajaxPost(data, wmsActions.wms_default_index_doInit);
      };
    }
  ]);
});