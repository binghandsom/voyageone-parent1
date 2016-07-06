define(function () {
    return {
        otherwise: '/order/order_info',
        routes: [
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
            }
        ]
    };
});