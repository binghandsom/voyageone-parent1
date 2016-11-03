/**
 * 拓展高级检索JS端controller
 */
define([
    'cms'
],function(cms){

    cms.controller('adSearchAppendCtl',(function(){

        function AdSearchAppendCtl($scope){
            this.parentScope = $scope.$parent;
        }

        AdSearchAppendCtl.prototype.test = function(){
            var self = this;


        };

        return AdSearchAppendCtl;

    })());

});