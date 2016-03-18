/**
 * controller FeedMappingController
 */

define([
    'cms',
    'underscore',
    'modules/cms/controller/popup.ctl'
], function (cms, _) {
    "use strict";
    return cms.controller('feedMappingController', (function () {
        /**
         * @typedef {{parentPath:string,level:int,model:object,mapping:object}} FeedCategoryBean
         */
        /**
         * @description
         * Feed Mapping 画面的 Controller 类
         * @param $scope
         * @param feedMappingService
         * @param feedMappingListService
         * @param {voNotify} notify
         * @param {function} confirm
         * @param $translate
         * @param blockUI
         * @constructor
         */
        function FeedMappingController($scope, feedMappingService, feedMappingListService, notify, confirm, $translate, blockUI) {

            this.confirm = confirm;
            this.notify = notify;
            this.$translate = $translate;
            this.feedMappingService = feedMappingService;
            this.feedMappingListService = feedMappingListService;
            this.blockUI = blockUI;

            this.currCategoryMap = null;

            this.topCategories = null;
            /**
             * 当前选择的 TOP 类目
             * @type {string}
             */
            this.selectedTop = null;
            /**
             * 表格的数据源
             * @type {object[]}
             */
            this.tableSource = null;
            /**
             * 匹配情况筛选条件
             * @type {{category: boolean, property: boolean}}
             */
            this.matched = {
                category: null,
                property: null,
                keyWord: null
            };

            // 在 Window 上注册回调, 用于子页面通知切换 MatchOver
            var self = this;

            window.feedMappingController = {
                setMatchOver: function(mappingScope, matchOver) {
                    $scope.$apply(function() {
                        var map = self.currCategoryMap;
                        // 更新目标 Feed Mapping
                        map[mappingScope.feedCategoryPath].mapping.matchOver = matchOver;
                        // 更新目标通用 Mapping
                        Object.keys(map).forEach(function (key) {
                            var mapping = map[key].mainMapping;
                            if (mapping && mapping.scope.mainCategoryPath === mappingScope.mainCategoryPath)
                                mapping.matchOver = matchOver;
                        });
                    });
                }
            };
        }

        FeedMappingController.prototype = {
            /**
             * select 的绑定数据源
             */
            options: {
                category: {
                    '类目匹配情况(所有)': null,
                    '类目已匹配': true,
                    '类目未匹配': false
                },
                property: {
                    '匹配属性情况(所有)': null,
                    '属性已匹配完成': true,
                    '属性未匹配完成': false
                }
            },
            /**
             * 画面初始化时
             */
            init: function () {

                var ttt = this;

                ttt.feedMappingService.getTopCategories().then(function (res) {
                    ttt.topCategories = res.data;
                    ttt.selectedTop = ttt.topCategories[0].cid;

                    ttt.refreshTable();
                });
            },

            /**
             * 刷新表格
             */
            refreshTable: function () {

                var ttt = this;
                var keyWord = ttt.matched.keyWord;

                ttt.blockUI.start();

                ttt.feedMappingListService.getCategoryMap(ttt.selectedTop).then(function (categoryMap) {

                    ttt.currCategoryMap = categoryMap;

                    var rows = _.filter(categoryMap, function (feedCategoryBean) {
                        var result = true;

                        // 如果关键字存在, 先进行关键字过滤
                        if (keyWord && feedCategoryBean.model.path.indexOf(keyWord) < 0)
                            return false;

                        if (ttt.matched.category !== null)
                            result = ttt.matched.category === ttt.isCategoryMatched(feedCategoryBean);
                        else if (ttt.matched.property !== null)
                            result = ttt.matched.property === ttt.isPropertyMatched(feedCategoryBean);

                        if (result && !feedCategoryBean.classes) {
                            // 如果没有计算过样式, 就计算
                            feedCategoryBean.classes = {
                                background: feedCategoryBean.model.isChild == 1 ? 'badge-empty' : 'badge-success',
                                icon: feedCategoryBean.model.isChild == 1 ? 'fa-level-up' : 'fa-level-down'
                            };
                        }

                        return result;
                    });

                    // 绑定&显示
                    ttt.tableSource = rows.sort(function (a, b) {
                        return a.seq > b.seq ? 1 : -1;
                    });

                    ttt.blockUI.stop();
                });
            },

            /**
             * 继承其父类目的 Mapping
             * @param {object} feedCategory Feed 类目
             */
            extendsMapping: function (feedCategory) {

                var ttt = this;

                ttt.confirm('TXT_MSG_CONFIRM_FROWARD_PARENT_CATEGORY')
                    .result
                    .then(function () {
                        return ttt.feedMappingService.extendsMapping(feedCategory);
                    })
                    .then(function (res) {
                        if (!res.data)
                            ttt.notify.warning({id: 'TXT_MSG_NO_FIND_FORWARD_CATEGORY'});
                        else
                            ttt.resetBeanMapping(feedCategory.path, res.data);
                    });
            },
            /**
             * 获取类目的默认 Mapping 类目
             * @param {FeedCategoryBean} feedCategory
             * @returns {string}
             */
            getDefaultMapping: function (feedCategory) {

                if (!feedCategory) {
                    return '?';
                }

                var defMapping = feedCategory.mapping;

                if (defMapping) {
                    return defMapping.scope.mainCategoryPath;
                }

                var parent = null;
                while (!defMapping && (parent = this.findParent(parent || feedCategory))) {
                    defMapping = parent.mapping;
                }

                return defMapping
                    ? (this.$translate.instant('TXT_FORWARD_WITH_COLON') + defMapping.scope.mainCategoryPath)
                    : this.$translate.instant('TXT_UN_SETTING');
            },
            /**
             * 查找父级类目
             * @param {object} category
             * @returns {object}
             */
            findParent: function (category) {

                var path = category.model.path;
                var index = path.lastIndexOf('-');
                return index < 0
                    ? null
                    : this.findCategory(path.substring(0, index));
            },
            /**
             * 在树中查找类目
             * @param {string} path 类目路径
             * @return {object|undefined} Feed 类目对象
             */
            findCategory: function (path) {
                return this.currCategoryMap[path];
            },

            /**
             * 在类目 Popup 确定关闭后, 为相关类目进行绑定
             * @param {{from:string, selected:object}} context Popup 返回的结果信息
             */
            bindCategory: function (context) {
                var self = this;
                self.feedMappingService.setMapping({
                    from: context.from,
                    to: context.selected.catPath
                }).then(function (res) {
                    self.resetBeanMapping(context.from, res.data);
                });
            },

            /**
             * 重新设置 Bean 对象的 Mapping 和 MainMapping 属性
             * @param feedCategoryPath
             * @param mapping
             */
            resetBeanMapping: function (feedCategoryPath, mapping) {
                var self = this;
                var feedCategoryBean = self.findCategory(feedCategoryPath);
                feedCategoryBean.mapping = mapping;
                self.feedMappingListService.getMainMapping(feedCategoryBean.mapping.scope.mainCategoryPath)
                    .then(function (mainMapping) {
                        feedCategoryBean.mainMapping = mainMapping;
                    });
            },

            /**
             * 检查当前类目是否已进行类目匹配
             * @param {FeedCategoryBean} feedCategoryBean
             * @return {boolean}
             */
            isCategoryMatched: function (feedCategoryBean) {
                // 只要默认 mapping 存在即已匹配
                return !!feedCategoryBean.mapping;
            },

            /**
             * 检查当前类目是否已经完成了属性匹配
             * @param {FeedCategoryBean} feedCategoryBean
             * @return {boolean}
             */
            isPropertyMatched: function (feedCategoryBean) {
                // 如果有匹配并且确实匹配完了,才算是属性匹配完成
                return this.isCategoryMatched(feedCategoryBean)
                    && !!feedCategoryBean.mapping.matchOver;
            },

            openCategoryMapping: function (categoryModel, popupNewCategory) {

                this.feedMappingService.getMainCategories()
                    .then(function (res) {

                        popupNewCategory({

                            categories: res.data,
                            from: categoryModel.path

                        }).then(this.bindCategory.bind(this));

                    }.bind(this));
            },

            /**
             * 重置搜索条件
             */
            clear: function () {
                this.matched.category = null;
                this.matched.property = null;
                this.matched.keyWord = null;
                this.refreshTable();
            }
        };

        return FeedMappingController;

    })()).service('feedMappingListService', (function () {

        function FeedMappingListService(feedMappingService) {
            this.feedMappingService = feedMappingService;
        }

        FeedMappingListService.prototype = {
            getCategoryMap: function (categoryId) {
                var self = this;
                return self.feedMappingService.getFeedCategoryTree({topCategoryId: categoryId})
                    .then(function (res) {
                        return res.data;
                    });
            },
            getMainMapping: function (mainCategoryPath) {
                var self = this;
                return self.feedMappingService.getMainMapping({to: mainCategoryPath}).then(function (res) {
                    return res.data;
                });
            }
        };

        return FeedMappingListService;

    })());
});