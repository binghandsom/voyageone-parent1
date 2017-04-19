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

        self.status = null;
        self.op = self.opList.G;

        $scope.$on("$destroy", function () {
            self.resetInterval();
        });
    }

    ShelvesListController.prototype = {
        clientTypes: {
            PC: 1,
            APP: 2
        },
        opList: {
            G: '大于',
            L: '小于',
            E: '等于'
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

                self.resetInterval();

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
        resetInterval: function () {
            clearInterval(this.$interval);
            this.$interval = null;
        },
        refreshInfo: function (s) {
            var self = this;

            if (!self.delayTarget)
                self.delayTarget = [];

            self.delayTarget.push(s);

            clearTimeout(self.timeoutRefresh);
            self.timeoutRefresh = setTimeout(function () {
                self.intervalRefreshInfo(self.delayTarget);
                self.delayTarget = [];
            }, 0);
        },
        intervalRefreshInfo: function (ss) {
            var self = this,
                i = 30 * 1000;

            if (self.count <= 0 || !self.count) {
                self.count = 30;
                self.getShelvesInfo(ss);
            }

            self.count--;

            if (self.$interval)
                return;

            self.$interval = setInterval(function () {


                if (self.count <= 0) {
                    self.resetInterval();

                    self.confirm('自动刷新，已停止。是否继续自动刷新？').then(function () {
                        self.intervalRefreshInfo();
                    });

                    return;
                }

                self.count--;
                self.getShelvesInfo();
            }, i);
        },
        stEditItem: function () {
            this.resetInterval();
        },
        spEditItem: function () {
            this.intervalRefreshInfo();
        },
        getShelvesInfo: function (ss) {
            var self = this;
            var shelves = self.shelves;
            var shelvesService = self.shelvesService;
            var opened = ss || shelves.filter(function (_s) {
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

                // 更新数据后触发一次过滤
                self.filterProduct();
            });
        },
        clearFilter: function () {
            this.inventory = null;
            this.status = null;
            this.code = null;

            this.shelves.forEach(function (s) {
                if (!s.products || !s.products.length) {
                    return;
                }
                s.products.forEach(function (p) {
                    p.$hide = false;
                });
            });
        },
        filterProduct: function () {
            var i = this.inventory;
            i = i ? parseInt(i) : -1;
            var o = this.op;
            var t = this.status;
            var c = this.code;

            var ops = this.opList;

            this.shelves.forEach(function (s) {

                if (!s.products || !s.products.length) {
                    return;
                }

                s.products.forEach(function (p) {

                    p.$hide = false;

                    if (i > -1) {
                        var pass = true;

                        switch (o) {
                            case ops.G:
                                pass = p.cartInventory > i;
                                break;
                            case ops.L:
                                pass = p.cartInventory < i;
                                break;
                            case ops.E:
                                pass = p.cartInventory == i;
                                break;
                        }

                        if (!pass) {
                            p.$hide = true;
                            return;
                        }
                    }

                    if (t !== null) {
                        p.$hide = ((p.status || 0) == t);
                    }

                    if (c) {
                        p.$hide = p.productCode.indexOf(c) < 0;
                    }
                });
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

            this.popups.popNewShelves(context).then(function (model) {
                if (!s) {
                    self.shelves.push(model);
                } else {
                    angular.merge(s, model);
                }
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
                // 使用 setTimeout 来延迟执行
                setTimeout(function () {
                    if ($isOpen) {
                        self.refreshInfo(s);
                    } else {
                        s.$e = false;
                    }
                }, 0);
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
        releaseImage: function (s) {
            var self = this;
            s._uped = true;
            self.shelvesService.releaseImage(s.id).then(function () {
                self.notify.success('上传请求发送成功。请等待。。。');
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
                    }, function () {
                        previewWindow.close();
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
                        preview: false
                    }).then(function (resp) {
                        var t = document.createElement('textarea');
                        t.innerHTML = resp.data;
                        d.body.innerHTML = '<code>' + t.innerHTML + '</code>';
                    }, function () {
                        newW.close();
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

    cms.filter('shelvesProductClass', function () {
        return function (p) {
            switch (p.status) {
                case 1:
                    return 'label-danger';
                case 0:
                    return 'label-success';
                default:
                    return 'label-default';
            }
        };
    });

    cms.filter('shelvesProductName', function () {
        return function (p) {
            switch (p.status) {
                case 1:
                    return '已下架';
                case 0:
                    return '已上架';
                default:
                    return '---';
            }
        };
    });

    cms.controller('ShelvesListController', ShelvesListController);
});

