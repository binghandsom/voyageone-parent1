define(function () {
    return {
        otherwise: '/order/list',
        routes: [{
            "hash": "/order/list",
            "templateUrl": "./views/order/list.html",
            "controllerUrl": "./views/order/list.controller",
            "controller": "OrderListController as ctrl"
        },
        {
            "hash": "/feed/feed_file_upload",
            "templateUrl": "./views/feed/fileUpload/index.html",
            "controllerUrl": "./views/feed/fileUpload/index.controller",
            "controller": "FeedFileUploadController as ctrl"
        }
        ]
    };
});