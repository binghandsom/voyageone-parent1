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

        function JoinPromotionCtl(context, addProductToPromotionService, alert, $translate) {
            console.log(context);
            this.context=context;
            this.cartBean = context.cartBean;
            this.alert = alert;
            this.$translate = $translate;
            this.addProductToPromotionService = addProductToPromotionService;
            this.nowTime = new Date();
            this.allCheckNodes;     //原始全选状态
            this.halfCheckNodes;    //原始半选状态
            this.checkNodes = {};
            this.groupInfo = {};
        }

        JoinPromotionCtl.prototype.getCodeList=function () {
            var codeList=[];
            _.forEach(this.context.selList, function (object) {
                codeList.push(object.code);
            });
            return codeList;
        };

        /**
         * cartId isSelAll codeList    addProductToPromotionService.init
         */
        JoinPromotionCtl.prototype.init = function () {
           // this.search();
        };

        JoinPromotionCtl.prototype.search=function () {
            var self=this,
                addProductToPromotionService = self.addProductToPromotionService;

            self.listTreeNode = [];
            addProductToPromotionService.init({
                codeList : self.getCodeList(),
                cartId : self.cartBean.value,
                isSelAll : self.context.isSelAll,
                activityStart:self.groupInfo.startTime,
                activityEnd:self.groupInfo.endTime
            }).then(function (res) {
                self.listTreeNode = res.data.listTreeNode;

                //清空原来选中状态
                self.allCheckNodes = self.halfCheckNodes = {};

                //设置全选
                self.allCheckNodes = flatCategories(self.listTreeNode, 2);

                //设置半选
                self.halfCheckNodes = flatCategories(self.listTreeNode, 1);
            });

        }
        JoinPromotionCtl.prototype.canSelectChild = function (entity) {
            console.log(entity);
            var self = this,
                alert = self.alert,
                checkNodes = self.checkNodes;

            if (checkNodes[entity.id])
                entity.checked = 2;
            else
                entity.checked = 0;

            if (entity.children&&entity.children.length > 0 && checkNodes[entity.id]) {

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

        // 选择基准价格时的画面检查
        JoinPromotionCtl.prototype.chkPriceType = function (priceTypeVal, typeTxt) {
            var self = this,
                groupInfo = self.groupInfo,
                $translate = self.$translate;

            groupInfo.priceTypeId = priceTypeVal;
            groupInfo.priceValue = null;
            groupInfo.roundType = "1";
            groupInfo._opeText = '';

            if (priceTypeVal == 4) {
                // 基准价格为None时只允许选等于号
                groupInfo._optStatus = true;
                groupInfo.optType = '=';
                groupInfo._typeText = '';
                groupInfo.priceInputFlg = true;
                groupInfo.skuUpdType = "0";
            } else {
                groupInfo._optStatus = false;
                groupInfo.optType = '';
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
                if(groupInfo.priceTypeId == 4) {
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

        JoinPromotionCtl.prototype.saveBasePrice = function(){
            if (listPromotionProduct.length == 0) {
                alert("请选择修改价格的商品!");
                return;
            }
            // 检查输入
            if ($scope.vm.priceTypeId == 0) {
                alert("未选择基准价格，请选择后再操作。");
                return;
            }
            if ($scope.vm.optType == undefined || $scope.vm.optType == '') {
                alert("未选择表达式，请选择后再操作。");
                return;
            }
            if ($scope.vm.optType != '=' && ($scope.vm.priceValue == undefined || $scope.vm.priceValue == '')) {
                alert("未填写价格，请填写后再操作。");
                return;
            }
            if ($scope.vm.priceTypeId == 4 && ($scope.vm.priceValue == undefined || $scope.vm.priceValue == '')) {
                alert("未填写价格，请填写后再操作。");
                return;
            }
            // 检查输入数据
            var intVal = $scope.vm.priceValue;
            if (!(intVal == null || intVal == undefined || intVal == '')) {
                if (isNaN(intVal)) {
                    alert("价格必须是数字");
                    return;
                }
            }

            //  if(预热已开始&&synch_status==2)//包含已上新的商品 提示
            var isUpdate=true;
            if(isBegin) {
                for (var i = 0; i < listPromotionProduct.length; i++) {
                    if (listPromotionProduct[i].synchStatus == 2) {
                        isUpdate=false;
                        confirm("专场已上线，变更价格一旦同步至平台将引起客诉，点击确认继续操作。").then(function () {
                            isUpdate=true;
                        });
                    }
                }
            }

            if(!isUpdate) return;
            var parameter={};
           // parameter.listPromotionProductId = $scope.getSelectedPromotionProductIdList(listPromotionProduct);
            //parameter.jmPromotionId=jmPromotionId;

            parameter.priceTypeId=$scope.vm.priceTypeId;
            parameter.priceValue=$scope.vm.priceValue;
            parameter.skuUpdType=$scope.vm.skuUpdType;
            parameter.optType=$scope.vm.optType;
            parameter.roundType=$scope.vm.roundType;


        };

        JoinPromotionCtl.prototype.save = function () {
            var self = this,
                checkNodes = self.checkNodes,
                groupInfo = self.groupInfo,
                alert = self.alert,
                context = self.context,
                userArr = objToArr(checkNodes),
                allCheckArr = objToArr(self.allCheckNodes);

            //判断用户是否选择了新的活动标签
            var isClick = _.every(userArr, function (item) {
                return allCheckArr.indexOf(item) < 0;
            });

            if (userArr.length == 0 || !isClick) {
                alert("未做任何改变！");
                return;
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
                } else
                    return true;
            });

            if (isPass) {
                // int cartId;
                //
                // int isSelAll;
                //
                // List<String> codeList;
                //
                // List<TagTreeNode> listTagTreeNode;
                //
                //
                // //价格类型    1 建议售价   2指导售价  3最终售价  4固定售价
                // int priceTypeId;
                //
                // // 1小数点向上取整    2个位向下取整    3个位向上取整    4无特殊处理
                // int roundType;
                //
                // // 1.商品内，SKU统一最高价    2.商品内，SKU统一最低价    3.商品内，SKU价格不统一
                // int skuUpdType;
                // // + -  *  =
                // String optType;
                // //price value
                // double priceValue;
                var upEntity = {};
                upEntity.cartId = context.cartBean.value;
                upEntity.isSelAll = context.isSelAll;
                upEntity.codeList = self.getCodeList();
                upEntity.listTagTreeNode = self.listTreeNode;

                upEntity.priceTypeId = groupInfo.priceType;
                upEntity.roundType = groupInfo.roundType;
                upEntity.skuUpdType = groupInfo.skuUpdType;
                upEntity.optType = groupInfo.optType;
                upEntity.priceValue = groupInfo.priceValue;

                console.log("upEntity",upEntity);

                alert("调用存储接口");
            }
        };

        return JoinPromotionCtl;

    })());

});
