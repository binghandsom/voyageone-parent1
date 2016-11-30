/**
 * Created by 123 on 2016/11/29.
 */
define([
    'cms',
    './data'
], function (cms, data) {

    cms.service("productTopService", function ($q) {

            this.init = function () {
                var defer = $q.defer();

                defer.resolve({
                    brandList: [{
                        "id": 150096,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "//out",
                        "name": "//out",
                        "add_name1": "//out",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 145629,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "1 madison",
                        "name": "1 madison",
                        "add_name1": "1 madison",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 152100,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "1 voice",
                        "name": "1 voice",
                        "add_name1": "1 voice",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 145317,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "1. state",
                        "name": "1. state",
                        "add_name1": "1. state",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 145745,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "143 girl",
                        "name": "143 girl",
                        "add_name1": "143 girl",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 149466,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "1883 by lucchese",
                        "name": "1883 by lucchese",
                        "add_name1": "1883 by lucchese",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 145345,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "1st sight",
                        "name": "1st sight",
                        "add_name1": "1st sight",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 147820,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "2 lips too",
                        "name": "2 lips too",
                        "add_name1": "2 lips too",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }, {
                        "id": 150014,
                        "type_id": 41,
                        "channel_id": "928",
                        "type_code": "brand",
                        "value": "2(x)ist",
                        "name": "2(x)ist",
                        "add_name1": "2(x)ist",
                        "add_name2": null,
                        "lang_id": "cn",
                        "display_order": 0,
                        "comment": "品牌",
                        "cartType": null
                    }],
                    sortColumnName: 'created',
                    sortType: 1    //1 升序  -1 降序
                });

                return defer.promise;

            };

            this.getPage = function () {
                var defer = $q.defer();

                defer.resolve(data);

                return defer.promise;
            };

            this.getCount = function () {
                var defer = $q.defer();

                defer.resolve(7);

                return defer.promise;
            };

            this.getTopList = function(){
                var defer = $q.defer();

                defer.resolve(data);

                return defer.promise;
            }

        });

});