define(function () {
    return {
        home: {
            hash: "/home",
            templateUrl: "views/home/welcome/datachart.tpl.html",
            controllerUrl: "modules/cms-us/views/home/welcome/datachart.ctl",
            controller: 'datachartController as ctrl'
        },
        newItems:{
            hash: "/items/newItems",
            templateUrl: "views/items/newItems/index.tpl.html",
            controllerUrl: "modules/cms-us/views/items/newItems/index.ctl",
            controller: 'newItemController as ctrl'
        },
        itemDetail:{
            hash: "/items/itemDetail",
            templateUrl: "views/items/itemDetail/index.tpl.html",
            controllerUrl: "modules/cms-us/views/items/itemDetail/index.ctl",
            controller: 'itemDetailController as ctrl'
        }
    };
});