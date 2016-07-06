define(function () {
    return {
        otherwise: '/home',
        routes: [
            {
                "hash": "/home",
                "templateUrl": "views/home/index.html",
                "controllerUrl": "./views/home/index.controller",
                "controller": "HomeController"
            },
            {
                "hash": "/order/order_info",
                "templateUrl": "views/order/order_info.html",
                "controllerUrl": "./views/order/order_info.controller",
                "controller": "OrderInfoController as ctrl"
            },
            {
                "hash": "/feed/feed_file_upload",
                "templateUrl": "./views/feed/fileUpload/index.html",
                "controllerUrl": "./views/feed/fileUpload/index.controller",
                "controller": "FeedFileUploadController as ctrl"
            },
            {
                "hash": "/feed/feed_import_result",
                "templateUrl": "./views/feed/feedImport/index.html",
                "controllerUrl": "./views/feed/feedImport/index.controller",
                "controller": "FeedImportResultController as ctrl"
            }
        ]
    };
});