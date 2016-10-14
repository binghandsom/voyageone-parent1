define(['./spedit.directive', './sp.data.service'], function () {

    function SpDetailPageController(spDataService) {
        this.spDataService = spDataService;
    }



    return SpDetailPageController;
});