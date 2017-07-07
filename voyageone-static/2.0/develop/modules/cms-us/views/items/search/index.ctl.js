define([
    'cms'
], function (cms) {

    cms.controller('newItemController', class newItemController {

        constructor(popups) {
            this.popups = popups;
        }

        popBatchApprove() {
            let self = this;

            self.popups.openBatchApprove();
        }

    });

});