define([
    'cms',
    './joinPromotion.dev'
], function (cms) {

    function flatCategories(categories, flag) {

        return _.reduce(categories, function (map, curr) {
            map[curr.id] = (curr.checked == flag);
            if (curr.children && curr.children.length)
                map = _.extend(map, flatCategories(curr.children, flag));
            return map;
        }, {});

    }

    function objToArr(obj) {

        return _.map(obj, function (value, key) {
            if (value)
                return key;
        }).filter(function (item) {
            return item != undefined && item != null && item != '';
        });
    }

    cms.controller('joinPromotionCtl', (function () {

        function JoinPromotionCtl(context, addProductToPromotionService2, alert) {
            this.cartBean = context;
            this.alert = alert;
            this.addProductToPromotionService = addProductToPromotionService2;
            this.nowTime = new Date();
            this.allCheckNodes;     //原始全选状态
            this.halfCheckNodes;    //原始半选状态
            this.checkNodes = {};
            this.groupInfo = {};
        }

        /**
         * cartId isSelAll codeList    addProductToPromotionService.init
         */
        JoinPromotionCtl.prototype.init = function () {
            var self = this;

            self.addProductToPromotionService.init().then(function (res) {
                self.channelCategoryList = res.data;

                //设置全选
                self.allCheckNodes = flatCategories(self.channelCategoryList, 2);
                //设置半选
                self.halfCheckNodes = flatCategories(self.channelCategoryList, 1);
            });
        };

        JoinPromotionCtl.prototype.canSelectChild = function (entity) {
            var self = this,
                alert = self.alert,
                checkNodes = self.checkNodes;

            if (checkNodes[entity.id])
                entity.checked = 2;
            else
                entity.checked = 0;

            if (entity.children.length > 0 && checkNodes[entity.id]) {

                var exit = _.some(entity.children, function (item) {
                    //只判断全选状态
                    return item.checked == 2;
                });

                if (!exit) {
                    alert("至少勾选一个活动标签！");
                    return;
                }
            }
        };

        JoinPromotionCtl.prototype.save = function () {
            var self = this,
                checkNodes = self.checkNodes,
                alert = self.alert,
                userArr = objToArr(checkNodes),
                allCheckArr = objToArr(self.allCheckNodes),
                halfCheckArr = objToArr(self.halfCheckNodes);

            //判断用户是否选择了新的活动标签

            var isClick = _.every(userArr, function (item) {
                return allCheckArr.indexOf(item) < 0;
            });

            if (userArr.length == 0 || !isClick) {
                alert("未做任何改变！");
                return;
            }

            var isPass = _.every(self.channelCategoryList, function (item) {
                if(!checkNodes[item.id])
                    return true;
                if (item.children.length > 0) {

                    var exit = _.some(item.children, function (element) {
                        //只判断全选状态
                        return element.checked == 2;
                    });

                    if (!exit) {
                        alert(item.name + "活动下至少选择一个标签！");
                        return false;
                    } else
                        return true;
                }else
                    return true;
            });

            if (isPass) {
                alert("调用存储接口");
            }
        };

        return JoinPromotionCtl;

    })());

});
