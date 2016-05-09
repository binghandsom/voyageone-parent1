/**
 * Created by sofia on 5/8/2016.
 */
define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    return cms.controller('CatUnionController',(function () {

        function CatUnionController(){

            var ctrl = this;

            require(['json!/mock/catunion.json'], function(data){
                ctrl.property = data.data;
            });

        }

        return CatUnionController;

    })())
});