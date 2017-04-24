/**
 * @description 高级检索自定义列
 * @author edward.
 * @date 2015-12-7
 */
define([
    'cms'
], function (cms) {

    cms.controller('popColumnForDownloadCtl', (function (){

        function ColumnForDownloadCtl($searchAdvanceService2, $modalInstance){
           this.$searchAdvanceService2 = $searchAdvanceService2;
           this.$modalInstance = $modalInstance;
        }

        return ColumnForDownloadCtl;
    })() );

});