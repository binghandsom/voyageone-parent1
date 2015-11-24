/**
 * @Name:    popInventoryService.js
 * @Date:    2015/06/02
 * @User:    sky
 * @Version: 1.0.0
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {

    wms.service("popChangeReservationService", [
        'ajaxService',
        'wmsActions',
        popChangeReservationService]);

    function popChangeReservationService(http, wmsActions) {

        var popChangeReservation = wmsActions.reservation.popChangeReservation;

        this.popInit = function (order_channel_id, store_id) {
            return http.ajaxPost({
                orderChannelId: order_channel_id,
                storeId: store_id
            }, popChangeReservation.init).then(function (res) {
                return res.data;
            });
        };

        this.change = function (reservationID, changeKind, notes, store, modified) {
            return  http.ajaxPost({
                reservationID : reservationID.toString(),
                changeKind : changeKind.toString(),
                notes : notes,
                store : store.toString(),
                modified : modified
            }, popChangeReservation.change).then(function (res) {
                return res.data;
            });
        };

    };
});