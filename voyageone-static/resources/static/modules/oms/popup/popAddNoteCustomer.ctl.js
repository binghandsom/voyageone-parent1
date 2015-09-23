/**
 * @Name:    popCustomerNotes.ctl.js
 * @Date:    2015/5/3
 * @User:    will
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');
    require ('modules/oms/customer/customerDetail.service');

    omsApp.controller ('popAddNoteCustomerController'
        , ['$scope', 'popAddNoteCustomerService', 'customerDetailService'
            , function ($scope, popAddNoteCustomerService, customerDetailService) {
                $scope.saveItem = function () {
                    var data = {
                        id: customerDetailService.getNotesId ()
                        , numericKey: $scope.customerDetail.customerId
                        , notes: $scope.newNotes
                    };

                    popAddNoteCustomerService.doSaveNote (data).then (function () {
                        $scope.closeThisDialog ();
                        $scope.initialize ();
                    });
                }
            }])
});																					
