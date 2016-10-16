define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {

    function SpImagesDirectiveController($routeParams, jmPromotionService, alert, confirm, $translate, $filter) {
        this.$routeParams = $routeParams;
        this.jmPromotionService = jmPromotionService;
        this.alert = alert;
        this.confirm = confirm;
        this.$translate = $translate;
        this.$filter = $filter;
        this.vm = {"jmMasterBrandList": []};
        this.editModel = {model: {}};
        this.datePicker = [];
    }

    SpImagesDirectiveController.prototype.init = function () {


    };

    cms.directive('spImages', [function spImagesDirectiveFactory() {
        return {
            restrict: 'E',
            controller: ['$routeParams', 'jmPromotionService', 'alert', 'confirm', '$translate', '$filter', SpImagesDirectiveController],
            controllerAs: 'ctrl',
            templateUrl: '/modules/cms/views/jmpromotion/sp.images.directive.html'
        }
    }]);
});