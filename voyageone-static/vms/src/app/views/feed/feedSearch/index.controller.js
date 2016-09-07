/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
    function deleteCat(value){
        var target = document.getElementsByClassName('selectedCat');

    };
    vms.controller('FeedInfoSearchController', (function () {

        function FeedInfoSearchController(feedInfoSearchService, popups) {
            this.feedInfoSearchService = feedInfoSearchService;
            this.feedInfoList = [];
            this.collapse = false;
            this.feedCategoryTree;
            this.showAll = false;
            this.parentSku = "";
            this.name = "";
            this.category = "";
            this.priceStart = "";
            this.priceEnd = "";
            this.pageOption = {
                curr: 1,
                total: 0,
                fetch: this.getFeedInfoList.bind(this)
            };
            this.popups = popups;
            this.categories = null;
            this.selected = null;
            this.selectedCat = {};
            this.categoryPath = [];
            this.divType = "-";
            this.searchCats = [];
        }

        FeedInfoSearchController.prototype = {
            init: function () {
                var self = this;
                self.rr = [{
                    catName: "Jon－Sofia－Soifa",
                    link: "#",
                    children: [{
                        catName: "on－Sofia－Soi",
                        link: "#"
                    }]
                }];
                // for (var i = 0; i < self.categories.length; i++) {
                //     self.replaceScore(self.categories[i]);
                // }
                self.feedInfoSearchService.init().then(function (res) {
                    self.categories = res.feedCategoryTree;
                    for (var i = 0; i < self.categories.length; i++) {
                        self.replaceScore(self.categories[i]);
                    }
                    self.categoryPath = [{level: 1, categories: self.categories}];
                    self.search();
                });
            },
            getFeedInfoList: function () {
                var self = this;
                self.feedInfoSearchService.search({
                    "code": self.parentSku,
                    "name": self.name,
                    "category": self.category,
                    "priceStart": self.priceStart,
                    "priceEnd": self.priceEnd,
                    "curr": self.pageOption.curr,
                    "size": self.pageOption.size
                }).then(function (res) {
                    self.pageOption.total = res.total;
                    self.feedInfoList = res.feedInfoList.map(function (item) {
                        item.className = 'bg-default';
                        item.subClassName = 'bg-sub-default';
                        item.collapse = self.collapse;
                        if (item.skus != undefined) {
                            self.showAll = true;
                        }
                        return item;
                    })
                })
            },

            deleCat:function(value){
              var self = this;
                console.log(value);
                deleteCat(value);
            },

            search: function () {
                this.pageOption.curr = 1;
                if (this.pageOption.size == undefined) {
                    this.pageOption.size = 10;
                }
                this.getFeedInfoList();
            },
            open: function (context) {
                this.popups.openImagePreview(context);
            },

            toggleAll: function () {
                var self = this;
                var collapse = (self.collapse = !self.collapse);
                self.feedInfoList.forEach(function (item) {
                    item.collapse = collapse;
                });
            },

            replaceScore: function (category) {
                var self = this;
                category.catName = category.catName.replace(/－/g, '-');
                if (category.children) {
                    for (var i = 0; i < category.children.length; i++) {
                        self.replaceScore(category.children[i]);
                    }
                }
            }

        };

        return FeedInfoSearchController;

    }()));
});