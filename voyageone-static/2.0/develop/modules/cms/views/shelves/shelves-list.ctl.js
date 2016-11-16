define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    function ShelvesListController(shelvesService, popups, menuService) {
        var self = this;

        self.shelvesService = shelvesService;
        self.popups = popups;

        menuService.getPlatformType().then(function (data) {
            self.cartList = data.filter(function (i) {
                return i.value >= 20 && i .value < 900;
            });

            self.cart = self.cartList[0].value;
            self.clientType = self.clientTypes.PC;

            self.getShelves();
        });
    }

    ShelvesListController.prototype = {
        clientTypes: {
            PC: 1,
            APP: 2
        },
        getShelves: function () {
            var self = this;
            var shelvesService = self.shelvesService;
            var params = {
                cartId: +self.cart,
                clientType: self.clientType
            };

            if (!params.cartId && !params.clientType) {
                return;
            }

            shelvesService.search(params).then(function (resp) {
                self.shelves = resp.data;
            });
        },
        addShelves: function () {
            this.popups.popNewShelves();
        }
    };

    cms.controller('ShelvesListController', ['shelvesService', 'popups', 'menuService', ShelvesListController]);
});

