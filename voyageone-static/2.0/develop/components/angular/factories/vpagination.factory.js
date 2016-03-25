(function() {
    /**
     * @User: Edward
     * @Version: 2.0.0, 2015-12-09
     */
    angular.module("voyageone.angular.factories.vpagination", []).factory("vpagination", function() {
        /**
         * 创建一个分页服务
         * @param {{ curr: number, size: number, total: number, fetch: function }} config 配置
         */
        return function(config) {
            var _pages, _lastTotal = 0, _showPages = [];
            /**
             * 返回总件数
             * @returns {*}
             */
            this.getTotal = function() {
                return config.total;
            };
            /**
             * 返回当前页的开始和结束号
             * @returns {{start: number, end: number}}
             */
            this.getCurr = function() {
                return {
                    pageNo: curr(),
                    start: getCurrStartItems(),
                    end: getCurrEndItems(),
                    isFirst: isFirst(),
                    isLast: isLast(),
                    pages: createShowPages(),
                    isShowStart: isShowStart(),
                    isShowEnd: isShowEnd()
                };
            };
            // 跳转到指定页
            this.goPage = load;
            // 返回总页数
            this.getPageCount = getPages;
            // 是否是当前页
            this.isCurr = isCurr;
            /**
             * 跳转到指定页
             * @param {number} page 页号
             */
            function load(page) {
                page = page || config.curr;
                if (page < 1 || page > getPages() || isCurr(page)) return;
                config.curr = page;
                config.fetch(page, config.size);
            }
            /**
             * 初始化page列表
             * @returns {Array}
             */
            function createShowPages() {
                var minPage, maxPage, _showPages = [];
                if (config.curr < config.showPageNo) {
                    minPage = 1;
                    if (_pages <= config.showPageNo) maxPage = _pages; else maxPage = config.showPageNo;
                } else if (config.curr + 2 > _pages) {
                    minPage = _pages + 1 - config.showPageNo;
                    maxPage = _pages;
                } else {
                    minPage = config.curr + 3 - config.showPageNo;
                    maxPage = config.curr + 2;
                }
                // 按照指定数量创建按钮
                for (var i = minPage; i <= maxPage; i++) {
                    //scope.pages.push({num: 1, active: "", show: false});
                    _showPages.push(i);
                }
                return _showPages;
            }
            /**
             * 获取当前总页数
             * @returns {number}
             */
            function getPages() {
                if (_lastTotal != config.total) {
                    _pages = parseInt(config.total / config.size) + (config.total % config.size > 0 ? 1 : 0);
                    _lastTotal = config.total;
                }
                return _pages;
            }
            /**
             * 返回当前页的起始号
             * @returns {number}
             */
            function getCurrStartItems() {
                return (config.curr - 1) * config.size + 1;
            }
            /**
             * 返回当前页的结束号
             * @returns {number}
             */
            function getCurrEndItems() {
                var currEndItems = config.curr * config.size;
                return currEndItems <= config.total ? currEndItems : config.total;
            }
            /**
             * 是否是最后一页
             * @returns {boolean}
             */
            function isLast() {
                return config.curr == getPages();
            }
            /**
             * 是否是第一页
             * @returns {boolean}
             */
            function isFirst() {
                return config.curr == 1;
            }
            /**
             * 是否是当前页
             * @param page 页码
             * @returns {boolean}
             */
            function isCurr(page) {
                return config.curr == page;
            }
            function curr() {
                return config.curr;
            }
            /**
             * 是否显示开始...项目
             * @returns {boolean}
             */
            function isShowStart() {
                _showPages = createShowPages();
                return _showPages[0] > 1;
            }
            /**
             * 是否显示结束...项目
             * @returns {boolean}
             */
            function isShowEnd() {
                _showPages = createShowPages();
                return _showPages[_showPages.length - 1] < _pages;
            }
        };
    });
})();