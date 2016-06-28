define(function (require) {
    
    var _ = require('underscore');
    
    function CommonDataService(root, actions) {
        this.root = root;
        this.actions = actions;
    }

    CommonDataService.prototype.getDeclare = function () {

        var _class;

        var self = this,
            root = self.root || "",
            actions = self.actions;

        if (!actions)
            return null;

        _class = function (ajaxService) {
            this.ajaxService = ajaxService;
        };

        _.each(actions, function (content, key) {
            
            var _url, _root, _customCallback;
            
            if (_.isString(content))
                _url = content;
            else if (_.isObject(content)) {
                _url = content.url;
                _customCallback = content.then;
                _root = content.root;
            }
            
            if (!_url) {
                console.error('URL is undefined', content);
                return;
            }
            
            if (_root === false)
                _root = "";
            else if (_root === null || _root === undefined || _root === true)
                _root = root;
            
            _class.prototype[key] = function (args) {
                return this.ajaxService.post(_root + _url, args).then(_customCallback || function (res) {
                        return res.data;
                    });
            };
        });

        return ['ajaxService', _class];
    };
    
    return {
        testService: new CommonDataService('/test/', {
            action1: 'getSomeData',
            action2: {
                url: 'getSomeData2',
                then: function (res) {
                    return res.message;
                }
            }
        })
    }
});