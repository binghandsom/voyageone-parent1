define(function () {
    return {
        otherwise: '/home',
        routes: [
            {
                "hash": "/home",
                "templateUrl": "views/home/index.html",
                "controllerUrl": "./views/home/index.controller",
                "controller": "HomeController as ctrl"
            }
        ]
    };
});