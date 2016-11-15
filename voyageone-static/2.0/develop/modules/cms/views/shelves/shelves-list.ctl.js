define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    function ShelvesListController(shelvesService, popups) {
        // shelvesService.search();
        // this.
        var self = this;

        self.popups = popups;
    }

    ShelvesListController.prototype = {
        addShelves: function () {
            this.popups.popNewShelves();
        }
    };

    cms.controller('ShelvesListController', ['shelvesService', 'popups', ShelvesListController]);
});

