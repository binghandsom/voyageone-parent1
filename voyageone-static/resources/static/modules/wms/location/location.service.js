/**
 * @author Jonas
 * @date 2015-05-15 15:29:28
 * @version 0.0.1
 */

define([
    "modules/wms/wms.module",
    "components/services/ajax.service"
], function (wms) {

    wms.service("locationService", [
        'ajaxService', 'wmsActions',
        locationService]);

    function locationService(http, wmsActions) {
        var location = wmsActions.location;

        /**
         * 一览页面初始化
         */
        this.doListInit = function (data, scope) {
            return http.ajaxPost(data, location.list.init, scope);
        };

        this.doSearchLocation = function(location_name, store_id, page, size) {
            if (store_id == "") {
                store_id = 0;
            }
            return http.ajaxPost({location_name: location_name, store_id: store_id, page: page, size: size}, location.list.search).then(function (res){
                return res.data;
            });
        };

        this.doAddLocation = function(location_name, store_id) {
            if (store_id == "") {
                store_id = 0;
            }
            return http.ajaxPost({location_name: location_name, store_id: store_id}, location.list.add);
        };

        this.doDeleteLocation = function(location_id, store_id, modified) {
            if (store_id == "") {
                store_id = 0;
            }
            return http.ajaxPost({location_id: location_id, store_id: store_id, modified: modified}, location.list.delete);
        };

        /**
         * 货架绑定页面初始化
         */
        this.doBindInit = function (data, scope) {
            return http.ajaxPost(data, location.bind.init, scope);
        };

        /**
         * 获取商品货位，返回 { itemLocations, itemLocationLogs }
         * @param code 商品的 code 或 Barcode
         * @param store_id 仓库 id
         * @returns {*}
         */
        this.doSearchItemLocation = function(code, store_id){
            if (store_id == "") {
                store_id = 0;
            }
            return http.ajaxPost({code: code, store_id: store_id}, location.bind.search).then(function (res){
                return res.data;
            });
        };

        /**
         * 在仓库，添加商品到货位。返回 ItemLocationBean 和 itemLocationLog
         * @param location_name 货位名
         * @param code 商品的 Code
         * @param store_id 仓库 id
         * @returns {*}
         */
        this.doAddItemLocation = function(location_name, code, store_id){
            if (store_id == "") {
                store_id = 0;
            }
            return http.ajaxPost({location_name: location_name, code: code, store_id: store_id}, location.bind.add).then(function (res) {
                return res.data;
            });
        };

        /**
         * 删除，返回 itemLocationLog
         */
        this.doDeleteItemLocation = function(item_location_id, modified){
            return http.ajaxPost({item_location_id: item_location_id, modified: modified}, location.bind.delete).then(function (res) {
                return res.data;
            });
        };
    }
});