/**
 * @description 高级检索加入活动
 * @author piao
 */
define([
    'cms'
], function (cms) {

    /**通过状态递归查询树节点*/
    function flatCategories(categories, flag) {

        return _.reduce(categories, function (map, curr) {
            map[curr.id] = (curr.checked == flag);
            if (curr.children && curr.children.length)
                map = _.extend(map, flatCategories(curr.children, flag));
            return map;
        }, {});

    }

    /**对象拍成array*/
    function objToArr(obj) {

        return _.map(obj, function (value, key) {
            if (value)
                return key;
        }).filter(function (item) {
            return item != undefined && item != null && item != '';
        });
    }

    cms.controller('joinPromotionCtl', (function () {

        function JoinPromotionCtl(context, addProductToPromotionService, alert, $translate, notify, $uibModalInstance) {
            this.context = context;
            this.cartBean = context.cartBean;
            this.alert = alert;
            this.notify = notify;
            this.$translate = $translate;
            this.$uibModalInstance = $uibModalInstance;
            this.addProductToPromotionService = addProductToPromotionService;
            this.nowTime = new Date();
            this.allCheckNodes;     //原始全选状态
            this.halfCheckNodes;    //原始半选状态
            this.checkNodes = {};
            this.toggle = {};
            this.groupInfo = {
                priceType: 'priceSale',
                optType: '=',
                roundType: "1",
                skuUpdType: "1"
            };
        }

        JoinPromotionCtl.prototype.getCodeList = function () {
            var codeList = [];
            _.forEach(this.context.selList, function (object) {
                codeList.push(object.code);
            });
            return codeList;
        };

        JoinPromotionCtl.prototype.getProductIdList = function () {
            var idList = [];
            _.forEach(this.context.selList, function (object) {
                idList.push(object.id);
            });
            return idList;
        };

        JoinPromotionCtl.prototype.init = function () {
            this.chkPriceType('3', 'TXT_SALE_PRICE');
            this.chkOptionType();
            this.search();
        };

        JoinPromotionCtl.prototype.search = function () {
            var self = this,
                addProductToPromotionService = self.addProductToPromotionService;

            self.listTreeNode = [];
            addProductToPromotionService.init({
                codeList: self.getCodeList(),
                cartId: self.cartBean.value,
                isSelAll: self.context.isSelAll,
                activityStart: self.groupInfo.startTime,
                activityEnd: self.groupInfo.endTime,
                searchInfo: self.context.searchInfo
            }).then(function (res) {
                self.listTreeNode = res.data.listTreeNode;

                self.initTreeNode = angular.copy(res.data.listTreeNode);

                //清空原来选中状态
                self.allCheckNodes = self.halfCheckNodes = {};

                //设置全选
                self.allCheckNodes = flatCategories(self.listTreeNode, 2);

                //设置半选
                self.halfCheckNodes = flatCategories(self.listTreeNode, 1);
            });

        };

        JoinPromotionCtl.prototype.canSelectChild = function (entity) {

            var self = this,
                checkNodes = self.checkNodes;

            if (checkNodes[entity.id])
                entity.checked = 2;
            else
                entity.checked = 0;

            if (entity.children && entity.children.length > 0 && !checkNodes[entity.id]) {
                //父节点勾掉后子节点也都清掉
                angular.forEach(entity.children, function (ele) {
                    ele.checked = 0;
                });
            }

            if (entity.children && entity.children.length > 0 && checkNodes[entity.id]) {

                _.some(entity.children, function (item) {
                    //只判断全选状态
                    return item.checked == 2;
                });
            }
        };

        JoinPromotionCtl.prototype.chkPriceType = function (priceTypeVal, typeTxt) {
            var self = this,
                groupInfo = self.groupInfo,
                $translate = self.$translate;

            groupInfo.priceTypeId = priceTypeVal;
            groupInfo.priceValue = null;
            groupInfo.optType = "=";
            groupInfo.roundType = "1";
            groupInfo._opeText = '';

            if (priceTypeVal == 4) {
                // 基准价格为None时只允许选等于号
                groupInfo._optStatus = true;
                groupInfo.optType = '=';
                groupInfo._typeText = '';
                groupInfo.priceInputFlg = true;
                //groupInfo.skuUpdType = "0";
            } else {
                groupInfo._optStatus = false;
                // groupInfo.optType = '';
                groupInfo._typeText = $translate.instant(typeTxt);
                groupInfo.priceInputFlg = false;
                groupInfo.skuUpdType = "1";
            }
        };

        JoinPromotionCtl.prototype.chkOptionType = function () {
            var self = this,
                groupInfo = self.groupInfo;

            groupInfo.priceValue = null;
            if (groupInfo.optType == '') {
                groupInfo._opeText = '';
                groupInfo.priceInputFlg = false;
            } else if (groupInfo.optType == '=') {
                groupInfo._opeText = '';
                if (groupInfo.priceTypeId == 4) {
                    // 基准价格为None时才可以输入
                    groupInfo.priceInputFlg = true;
                } else {
                    groupInfo.priceInputFlg = false;
                }
            } else {
                groupInfo._opeText = self.groupInfo.optType;
                groupInfo.priceInputFlg = true;
            }
        };

        JoinPromotionCtl.prototype.save = function () {
            var self = this,
                checkNodes = self.checkNodes,
                groupInfo = self.groupInfo,
                alert = self.alert,
                notify = self.notify,
                $uibModalInstance = self.$uibModalInstance,
                context = self.context;

            /**至少要选择一个活动*/
            var eachP = _.some(self.listTreeNode, function (item) {
                return item.checked != 0 && item.checked != 1;
            });

            if (!eachP) {
                alert("请至少选择一个活动");
                return
            }

            var isPass = _.every(self.listTreeNode, function (item) {
                if (!checkNodes[item.id])
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
                } else {
                    alert(item.name + "活动下至少选择一个标签！");
                    return false;
                }

            });

            if (!isPass)
                return;

            self.addProductToPromotionService.save(_.extend({
                cartId: context.cartBean.value,
                isSelAll: context.isSelAll,
                codeList: self.getCodeList(),
                idList: self.getProductIdList(),
                listTagTreeNode: self.listTreeNode,
                searchInfo: context.searchInfo
            }, groupInfo)).then(function () {
                notify.success("添加成功！");
                $uibModalInstance.close();
            });

        };

        JoinPromotionCtl.prototype.getPromotion = function () {
            var self = this;

            self.listTreeNode = _.filter(self.initTreeNode,function (node) {
                    return node.id === self.selPromotionName;
            });

        };

        JoinPromotionCtl.prototype.initTags = function () {
            var self = this,
                childNode = self.listTreeNode[0];

            self.checkNodes[childNode.id] = true;

            self.categoryContainer = childNode.children;
            self.parentNode = childNode;

            self.canSelectChild(self.parentNode);
        };

        return JoinPromotionCtl;

    })());

});
