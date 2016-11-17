define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    function ShelvesListController(shelvesService, popups, menuService, $scope) {
        var self = this;

        self.shelvesService = shelvesService;
        self.popups = popups;
        self.$scope = $scope;

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

            return shelvesService.search(params).then(function (resp) {
                if (self.shelves) {
                    self.shelves.forEach(function (s) {
                        s.stopIsOpenWatch();
                    });
                }

                self.shelves = resp.data;

                self.getShelvesInfo(true).then(function () {
                    self.shelves.forEach(function (s) {
                        self.watchShelvesIsOpen(s);
                    });
                });
            });
        },
        getShelvesInfo: function(force) {
            var self = this;
            var shelves = self.shelves;
            var shelvesService = self.shelvesService;

            var needInfoShelvesId = shelves.filter(function (s) {
                return force || s.$isOpen;
            }).map(function (s) {
                return s.id;
            });

            if (!needInfoShelvesId.length)
                return;

            return shelvesService.getShelvesInfo({
                shelvesIds: needInfoShelvesId,
                isLoadPromotionPrice: false
            }).then(function (resp) {
                var infoBeanList = resp.data;
                var map = {};
                shelves.forEach(function (s) {
                    map[s.id] = s;
                });
                infoBeanList.forEach(function (i) {
                    var s = i.shelvesModel;
                    angular.merge(map[s.id], s);
                    map[s.id].products = i.shelvesProductModels;
                });
                self.lastShelvesInfoTime = new Date();
            });
        },
        addShelves: function () {
            var self = this;
            var context = {
                cartName: self.cartList.find(function (i) {
                    return i.value === self.cart;
                }).name,
                cartId: +self.cart,
                clientName: self.clientType === self.clientTypes.PC ? "PC" : "APP",
                clientType: self.clientType
            };
            this.popups.popNewShelves(context).then(function (insertedModel) {
                self.shelves.push(insertedModel);
            });
        },
        watchShelvesIsOpen: function (shelves) {
            var self = this;
            var $scope = self.$scope;

            if (shelves.stopIsOpenWatch)
                return;

            shelves.stopIsOpenWatch = $scope.$watch(function () {
                return shelves.$isOpen;
            }, function ($isOpen) {
                if ($isOpen) {
                    self.getShelvesInfo();
                }
            });
        },
        expandAll: function () {
            this.shelves.forEach(function (s) {
                s.$isOpen = true;
            });
        },
        collapseAll: function () {
            this.shelves.forEach(function (s) {
                s.$isOpen = false;
            });
        }
    };

    cms.controller('ShelvesListController', ShelvesListController);
});

