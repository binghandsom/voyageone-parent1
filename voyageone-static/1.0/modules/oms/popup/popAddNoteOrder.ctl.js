/**
 * @Name:    popNewNotesDirective.js
 * @Date:    2015/5/4
 *
 * @User:    Eric
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');

//    require ('plUpload');
    omsApp.controller ('popAddNoteOrderController',
        ['$scope', 'popAddNoteOrderService',
            function ($scope, popAddNoteOrderService) {

                $scope.popInfo = {};
                $scope.doOk = function () {

                    var data = {};
                    if ($scope.uploader.flow.files.length > 0) {
                        // 文件内容
                        data.fileBase64 = $scope.uploader.flow.files[0].imgData;
                        // 文件名
                        data.filename = $scope.uploader.flow.files[0].name;
                    }
                    // Notes内容
                    data.notes = $scope.popInfo.reason;
                    // 订单号
                    data.orderNumber = $scope.popAddNoteToUseInfo.orderNumber;
                    data.sourceOrderId=$scope.popAddNoteToUseInfo.sourceOrderId
                    // 保存Notes
                    popAddNoteOrderService.doSaveNotes (data)
                        .then (function (data) {
                        $scope.$parent.setPopupAddNote (data.orderNotesList);

                        $scope.closeThisDialog ();
                    });
                };
            }])

});		