define([
    'cms',
    './data'
],function(cms,data){

    cms.service("channelConfigService",function($q){

        this.getChannelConfigs = function(){
            var defer = $q.defer();

            defer.resolve(data);

            return defer.promise;
        };

        this.save = function(){
            var defer = $q.defer();

            defer.resolve(true);

            return defer.promise;
        }

    });

});