define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    function ShelvesListController(shelvesService, popups, menuService, $scope, notify, confirm, cActions) {
        var self = this;

        self.shelvesService = shelvesService;
        self.popups = popups;
        self.$scope = $scope;
        self.notify = notify;
        self.confirm = confirm;
        self.downloadAppUrl = cActions.cms.shelvesService.root + "/exportAppImage";

        menuService.getPlatformType().then(function (data) {
            self.cartList = data.filter(function (i) {
                return i.value >= 20 && i.value < 900;
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

                (self.shelves = resp.data).forEach(function (s) {
                    self.watchShelvesIsOpen(s);
                });
            });
        },
        refreshInfo: function () {
            var self = this;
            var count = 30;
            var i = 30 * 1000;

            if (self.$interval)
                return;

            // 第一次
            count--;
            self.getShelvesInfo();

            self.$interval = setInterval(function () {
                if (count <= 0) {
                    clearInterval(self.$interval);
                    self.$interval = null;

                    self.confirm('自动刷新，已停止。是否继续自动刷新？').then(function () {
                        self.refreshInfo();
                    });

                    return;
                }
                count--;
                self.getShelvesInfo();
            }, i);
        },
        getShelvesInfo: function () {
            var self = this;
            var shelves = self.shelves;
            var shelvesService = self.shelvesService;

            var opened = shelves.filter(function (_s) {
                return _s.$isOpen;
            });

            var needInfoShelvesId = opened.map(function (_s) {
                return _s.id;
            });

            var needPrice = opened.some(function (_s) {
                return !_s.$pMap;
            });

            if (!needInfoShelvesId.length)
                return;

            return shelvesService.getShelvesInfo({
                shelvesIds: needInfoShelvesId,
                isLoadPromotionPrice: needPrice
            }).then(function (resp) {
                var infoBeanList = resp.data;
                var map = {};

                shelves.forEach(function (s) {
                    map[s.id] = s;
                });

                infoBeanList.forEach(function (i) {
                    var s = i.shelvesModel;
                    var pList = i.shelvesProductModels;
                    s = angular.merge(map[s.id], s);
                    s.products = pList;

                    var pMap;

                    // 保存价格到 $pMap
                    if (!s.$pMap) {
                        pMap = s.$pMap = {};
                        pList.forEach(function (p) {
                            pMap[p.productCode] = p.promotionPrice;
                        });
                    } else {
                        pMap = s.$pMap;
                        pList.forEach(function (p) {
                            p.promotionPrice = pMap[p.productCode];
                        });
                    }
                });
                self.lastShelvesInfoTime = new Date();
            });
        },
        addShelves: function (s) {
            var self = this;
            var context;

            if (!s) {
                context = {
                    cartId: +self.cart,
                    clientType: self.clientType
                };
            } else {
                context = angular.extend({}, s);
            }

            context.cartName = self.cartList.find(function (i) {
                return i.value === self.cart;
            }).name;

            context.clientName = self.clientType === self.clientTypes.PC ? "PC" : "APP";

            this.popups.popNewShelves(context).then(function (insertedModel) {
                self.shelves.push(insertedModel);
            });
        },
        watchShelvesIsOpen: function (s) {
            var self = this;
            var $scope = self.$scope;

            if (s.stopIsOpenWatch)
                return;

            s.stopIsOpenWatch = $scope.$watch(function () {
                return s.$isOpen;
            }, function ($isOpen) {
                if ($isOpen) {
                    self.refreshInfo();
                } else {
                    // 使用 setTimeout 来延迟执行
                    setTimeout(function () {
                        s.$e = false;
                    }, 0);
                }
            });
        },
        sortProduct: function (s) {
            var self = this;
            var shelvesService = self.shelvesService;

            if (s.$e) {
                var needSaveSort = s.products.some(function (p, index) {
                    return p.sort != index;
                });
                if (needSaveSort) {
                    s.products.forEach(function (p, index) {
                        p.sort = index;
                    });
                    shelvesService.updateProductSort(s.products).then(function () {
                        self.notify.success('TXT_SUCCESS');
                    });
                }
            }
            s.$e = !s.$e;
        },
        removeOne: function (s, i) {
            var removed = s.products.splice(i, 1);
            var removedItem = removed[0];
            var self = this;
            var shelvesService = self.shelvesService;

            shelvesService.removeProduct(removedItem).then(function () {
                self.notify.success('TXT_SUCCESS');
            });
        },
        removeAll: function (s) {
            var self = this;
            var shelvesService = self.shelvesService;

            self.confirm('确定要删除[ ' + s.shelvesName + ' ]货架的<strong>所有商品</strong>么？').then(function () {
                s.products = [];

                shelvesService.clearProduct({
                    shelvesId: s.id
                }).then(function () {
                    self.notify.success('TXT_SUCCESS');
                });
            });
        },
        deleteShelves: function (s, i) {
            var self = this;
            var shelvesService = self.shelvesService;

            self.confirm('确定要删除[ ' + s.shelvesName + ' ]货架么？').then(function () {
                self.shelves.splice(i, 1);

                shelvesService.deleteShelves(s).then(function () {
                    self.notify.success('TXT_SUCCESS');
                });
            });
        },
        releaseImage: function(s) {
            var self = this;
            self.shelvesService.releaseImage(s.id).then(function () {
                self.notify.success('TXT_SUCCESS');
            });
        },
        canPreview: function (s) {
            return s.products && s.products.every(function (p) {
                return !!p.platformImageUrl;
            });
        },
        preview: function (s) {
            switch (s.clientType) {
                case this.clientTypes.APP:
                    this.release(s);
                    break;
                case this.clientTypes.PC:
                    var previewWindow = window.open('about:blank');
                    var d = previewWindow.document;
                    d.write('<strong>内容正在下载，请稍等... </strong>');

                    this.shelvesService.getShelvesHtml({
                        shelvesId: s.id,
                        preview: true
                    }).then(function (resp) {
                        d.body.innerHTML = resp.data;
                    });
                    break;
            }
        },
        release: function (s) {
            switch (s.clientType) {
                case this.clientTypes.APP:
                    window.open(this.downloadAppUrl + "?shelvesId=" + s.id);
                    break;
                case this.clientTypes.PC:
                    var newW = window.open('about:blank');
                    var d = newW.document;
                    d.write('<strong>内容正在下载，请稍等... </strong>');

                    this.shelvesService.getShelvesHtml({
                        shelvesId: s.id,
                        preview: true
                    }).then(function (resp) {
                        d.body.innerHTML = '<pre>' + resp.data + '</pre>';
                    });
                    break;
            }
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

