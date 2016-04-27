/**
 * Created by pwj on 2016/4/25.
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
        return cms.controller('storeOperationController', (function(){
            function storeOperationController(){

            }
            storeOperationController.prototype = {
                clear:function(){
                    alert("young king young boss");
                }
            }
            return storeOperationController;

        })());

});