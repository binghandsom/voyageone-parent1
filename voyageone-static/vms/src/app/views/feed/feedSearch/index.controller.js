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
        }

        FeedInfoSearchController.prototype = {
            init: function () {
                var main = this;
                main.t = [{
                    catName: "Jon－Sofia－Soifa",
                    link: "#",
                    children: [{
                        catName: "on－Sofia－Soi",
                        link: "#"
                    }]
                }];
                main.feedInfoSearchService.init().then(function (res) {
                    main.categories = res.feedCategoryTree;
                    // console.log(main.categories);
                    main.categoryPath = [{level: 1, categories: main.categories}];
                    main.search();
                });
            },
            getFeedInfoList: function () {
                var main = this;
                main.feedInfoSearchService.search({
                    "code": main.parentSku,
                    "name": main.name,
                    "category": main.category,
                    "priceStart": main.priceStart,
                    "priceEnd": main.priceEnd,
                    "curr": main.pageOption.curr,
                    "size": main.pageOption.size
                }).then(function (res) {
                    main.pageOption.total = res.total;
                    main.feedInfoList = res.feedInfoList.map(function (item) {
                        item.className = 'bg-default';
                        item.subClassName = 'bg-sub-default';
                        item.collapse = main.collapse;
                        if (item.skus != undefined) {
                            main.showAll = true;
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
                var main = this;
                var collapse = (main.collapse = !main.collapse);
                main.feedInfoList.forEach(function (item) {
                    item.collapse = collapse;
                });
            }
        };

        return FeedInfoSearchController;

    }()));
});