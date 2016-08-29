/**
 * Created by sofia on 7/6/2016.
 */
define([
    'vms'
], function (vms) {
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
                main.feedInfoSearchService.init().then(function (res) {
                    main.categories = res.feedCategoryTree;
                    main.categoryPath = [{level: 1, categories: main.categories}];
                    main.search();
                });
            },
            openCategory: function (category, categoryItem) {
                if (categoryItem.selectedCat == undefined) {
                    categoryItem.selectedCat = [];
                }
                categoryItem.selectedCat = category.catName;

                // 标记选中
                this.selected = category;

                // 查询当前选中的是第几级
                var levelIdx = categoryItem.level - 1;

                // 获取这一级别的数据
                var pathItem = this.categoryPath[levelIdx + 1];
                if (pathItem) {
                    // 如果有数据,那么当前级别和后续级别都需要清空
                    this.categoryPath.splice(levelIdx + 1);
                }
                if (!category.children || !category.children.length) return;
                this.categoryPath.push({level: levelIdx + 2, categories: category.children});
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