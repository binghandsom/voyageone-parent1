/**
 * controller FeedMappingController
 */

define([
    'cms',
    'modules/cms/controller/popup.ctl'
], function (cms) {
    "use strict";
    return cms.controller('feedMappingController', (function () {

        /**
         * @description
         * Feed Mapping 画面的 Controller 类
         * @param {FeedMappingService} feedMappingService
         * @param {function} notify
         * @param {function} confirm
         * @constructor
         */
        function FeedMappingController(feedMappingService, notify, confirm) {

            this.confirm = confirm;
            this.notify = notify;
            this.feedMappingService = feedMappingService;

            /**
             * feed 类目集合
             * @type {object[]}
             */
            this.feedCategories = null;
            /**
             * 当前选择的 TOP 类目
             * @type {object}
             */
            this.selectedTop = null;
            /**
             * 表格的数据源
             * @type {object[]}
             */
            this.tableSource = null;
            /**
             * 匹配情况筛选条件
             * @type {{category: boolean|null, prop: boolean|null}}
             */
            this.matched = {
                category: null,
                prop: null
            };

            // 将被 popup 调用,需要强制绑定
            this.bindCategory = this.bindCategory.bind(this);
        }

        FeedMappingController.prototype = {
            /**
             * 画面初始化时
             */
            init: function () {

                this.feedMappingService.getFeedCategories().then(function (res) {
                    this.feedCategories = res.data.categoryTree;
                    // 如果有数据就默认选中
                    if (this.feedCategories.length) {
                        this.selectedTop = this.feedCategories[0];
                    }

                    this.refreshTable();
                }.bind(this));
            },
            /**
             * 继承其父类目的 Mapping
             * @param {object} feedCategory Feed 类目
             */
            extendsMapping: function (feedCategory) {

                this.confirm('确定要继承吗? 如果是已经匹配过,那么之前的会被覆盖掉.').result.then(function () {
                    this.feedMappingService.extendsMapping(feedCategory).then(function (res) {
                        if (res.data) {
                            // 从后台获取更新后的 mapping
                            feedCategory.mapping = res.data;
                        } else {
                            this.notify.warning('没有找到可以继承的设置.');
                        }
                    }.bind(this));
                }.bind(this));
            },
            /**
             * 获取类目的默认 Mapping 类目
             * @param {{mapping:object[]}} feedCategory
             * @returns {string}
             */
            getDefaultMapping: function (feedCategory) {

                if (!feedCategory) {
                    return '?';
                }

                var defMapping = this.findDefaultMapping(feedCategory);

                if (defMapping) {
                    return defMapping.mainCategoryPath;
                }

                var parent = null;
                while (!defMapping && (parent = this.findParent(parent || feedCategory))) {
                    defMapping = this.findDefaultMapping(parent);
                }

                return defMapping ? ('可继承: ' + defMapping.mainCategoryPath) : '[未设定]';
            },
            /**
             * 查找父级类目
             * @param {object} category
             * @returns {object}
             */
            findParent: function (category) {

                var path = category.path.split('-');

                if (path === 1) return null;

                // 从截取的类目路径中删除最后一个,即自己
                path.splice(path.length - 1);

                return this.findCategory(path);
            },
            /**
             * 在树中查找类目
             * @param {string[]} path 类目路径
             * @return {object} Feed 类目对象
             */
            findCategory: function (path) {

                var category = null;
                var categories = this.feedCategories;

                _.each(path, function (v, i) {

                    category = _.find(categories, function (cate) {
                        return cate.name === v;
                    });

                    if (i == path.length - 1)
                        return;

                    categories = category.child;
                });

                return category;
            },
            /**
             * 在类目中查找默认的 Mapping 关系
             * @param {{mapping:object[]}} category
             * @return {object} Mapping 对象
             */
            findDefaultMapping: function (category) {

                return _.find(category.mapping, function (mapping) {
                    return mapping.defaultMapping === 1;
                });
            },
            /**
             * 在类目 Popup 确定关闭后, 为相关类目进行绑定
             * @param {{from:object, selected:object}} context Popup 返回的结果信息
             */
            bindCategory: function (context) {

                this.feedMappingService.setMapping({
                    from: context.from.path,
                    to: context.selected.catPath
                }).then(function (res) {
                    // 从后台获取更新后的 mapping
                    context.from.mapping = res.data;
                });
            },
            /**
             * 刷新表格
             */
            refreshTable: function () {

                function multi(children) {
                    var flatten = [];
                    angular.forEach(children, function (child) {
                        flatten = flatten.concat(single(child));
                    });
                    return flatten;
                }

                function single(child) {
                    child.level = child.path.split('-').length;
                    return child.child && child.child.length
                        ? [child].concat(multi(child.child))
                        : [child];
                }

                function has(val) {
                    return val !== null && val !== "";
                }

                function boolean(val) {
                    return val === "1";
                }

                var _default = function (row) {
                    var bool = boolean(this.matched.category);
                    return bool === !!this.findDefaultMapping(row);
                }.bind(this);

                var matchOver = function (row) {
                    var bool = boolean(this.matched.prop);
                    var def = this.findDefaultMapping(row);
                    return bool === (!!def && def.matchOver);
                }.bind(this);

                // 拍平
                var rows = single(this.selectedTop);
                // 过滤
                rows = _.filter(rows, function (row) {

                    var result = true;
                    if (has(this.matched.category)) {
                        result = _default(row);
                    }
                    if (has(this.matched.prop)) {
                        result = matchOver(row);
                    }
                    return result;
                }.bind(this));
                // 显示
                this.tableSource = rows;
            }
        };

        return FeedMappingController
    })());
});