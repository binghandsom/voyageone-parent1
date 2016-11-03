define([
    'cms'
],function(cms){

    cms.controller('adSearchAppendCtl',(function(){

        function AdSearchAppendCtl(){

        }

        AdSearchAppendCtl.prototype.test = function(){
            var self = this;

            console.log(self.parent());
        };

        return AdSearchAppendCtl;

    })());

});