/**
 * @description 高级检索
 * @author piao
 */
define([
    'cms',
    'modules/cms/directives/navBar.directive'
], function (cms) {

    cms.controller('usProductSearchController', class UsProductSearchController {

        constructor(popups) {
            let self = this;

            self.popups = popups;
        }

        init() {

        }

        popCustomAttributes() {
            let self = this;

            self.popups.openCustomAttributes().then(res => {

            })
        }

        popBatchPrice() {
            let self = this;

            self.popups.openBatchPrice().then(res => {

            });
        }

        popUsFreeTag(){
            let self = this;

            self.popups.openUsFreeTag().then(res => {

            })
        }

    });

});