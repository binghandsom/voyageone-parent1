define(function () {
    return {
        home: {
            hash: "/home",
            templateUrl: "views/home/welcome/datachart.tpl.html",
            controllerUrl: "modules/cms-us/views/home/welcome/datachart.ctl",
            controller: 'datachartController as ctrl'
        },
        feed:{
            hash: "/feed/usa/search",
            templateUrl: "views/feed/search/index.tpl.html",
            controllerUrl: "modules/cms-us/views/feed/search/index.ctl",
            controller: 'feedSearchController as ctrl'
        },
        itemDetail:{
            hash: "/feed/detail/:id?",
            templateUrl: "views/feed/detail/index.tpl.html",
            controllerUrl: "modules/cms-us/views/feed/detail/index.ctl",
            controller: 'feedDetailController as ctrl'
        }
    };
});