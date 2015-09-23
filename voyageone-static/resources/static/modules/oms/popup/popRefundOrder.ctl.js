/**
 * @Name:    popRefundService.js
 * @Date:    2015/5/23
 *
 * @User:    Will
 * @Version: 1.0.0
 */

define (function (require) {
    var omsApp = require ('modules/oms/oms.module');
    require ('modules/oms/popup/popup.service');
    require ('components/services/message.service');

    omsApp.controller ('popRefundOrderController', ['$scope', 'omsType', 'popRefundOrderService','omsCommonService','messageService','messageKeys','cartId',
        function ($scope, omsType, popRefundOrderService, omsCommonService, messageService, messageKeys, cartId) {
            var _ = require ('underscore');

            /**
             * 退款页面初始化
             */
            var data = {};
            $scope.appContent = true;
            //未发货，同意退款
            $scope.appNotSend = false;
            //未发货，拒绝退款
            $scope.refuseNotSend = false;
            //已发货仅退款，同意退款
            $scope.appSentNoReturn = false;
            //已发货仅退款，拒绝退款
            $scope.refuseSentNoReturn = false;
            //已发货退货退款，同意退货
            $scope.appReturnGoods = false;
            //已发货退货退款，拒绝退货
            $scope.refuseReturnGoods = false;
            //已发货退货退款，同意退款
            $scope.appReturnRefund = false;
            //已发货退货退款，同意退款(同步OMS)
            $scope.appReturnRefundSynOMS = false;
            //已发货退货退款，拒绝退款
            $scope.refuseReturnRefund = false;
            //已发货退货退款，回补退货信息
            $scope.refillReturnAddress = false;
            //（阿里以外）
            //同意退款
            $scope.refundAgreeInLocal = false;
            //拒绝
            $scope.refundRefuseInLocal = false;

            //验证码
            $scope.verifyCode = "";
            $scope.showVerifyCode = false;

            //拒绝退款按钮
            $scope.isShowRefundRefuse = false;
            //同意退款按钮
            $scope.isShowRefundAgree = false;
            //同意退款按钮(同步OMS)
            $scope.isShowRefundAgreeSynOms = false;
            //同意退货按钮
            $scope.isShowReturnGoodsAgree = false;
            //拒绝退货按钮
            $scope.isShowReturnGoodsRefuse = false;
            //回填物流信息
            $scope.isShowReturnGoodsRefill = false;
            //发货按钮
            $scope.isShowShipping = false;

            //（阿里以外）
            //  同意退款按钮
            $scope.isShowLocalRefundAgree = false;
            //  拒绝退款按钮
            $scope.isShowLocalRefundRefuse = false;

            data.sourceOrderId = $scope.popRefundOrderToUseInfo.sourceOrderId;
            data.cartId = $scope.popRefundOrderToUseInfo.cartId;
            data.isShowHistoryOnly = $scope.popRefundOrderToUseInfo.isShowHistoryOnly;
            //popRefundOrderService.doInitRefund (data)
            //    .then (function (data) {
            //    $scope.popRefundOrderToUseInfo.refundList = data.orderRefundsList;
            //    $scope.popRefundOrderToUseInfo.refundInfo = data.refundInfo;
            //    $scope.popRefundOrderToUseInfo.refundMessagesList = data.refundMessagesList;
            //    $scope.popRefundOrderToUseInfo.refundsStatus = data.refundsStatus;
            //    $scope.currentrefundId = data.refundInfo.refundId;
            //});

            popRefundOrderService.doGetCodeList ()
                .then (function (codeData) {
                $scope.expressCodeList = omsCommonService.doGetOneCodeFromList (codeData, omsType.expressCode);

                popRefundOrderService.doInitRefund (data)
                        .then (function (data) {
                        $scope.popRefundOrderToUseInfo.refundList = data.orderRefundsList;
                        $scope.popRefundOrderToUseInfo.refundInfo = data.refundInfo;
                        $scope.popRefundOrderToUseInfo.refundMessagesList = data.refundMessagesList;
                        $scope.popRefundOrderToUseInfo.refundsStatus = data.refundsStatus;
                        $scope.currentrefundId = data.refundInfo.refundId;

                        //  画面按钮显示
                        $scope.showButton();
                    })
                });

            /**
             * 画面按钮显示
             */
            $scope.showButton = function () {
                //阿里的场合
                //if (cartId.Tmall == data.cartId || cartId.TmallG == data.cartId || cartId.Taobao == data.cartId) {
                    //URL 分开操作
                    //已发货（退货，退款）
                    if ($scope.popRefundOrderToUseInfo.refundsStatus.isShipped && $scope.popRefundOrderToUseInfo.refundsStatus.isHasGoodReturn) {
                        if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_SELLER_AGREE") {
                            //同意退货按钮
                            $scope.isShowReturnGoodsAgree = true;
                            //拒绝退款按钮
                            $scope.isShowRefundRefuse = true;
                        } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_BUYER_RETURN_GOODS") {
                            //回填物流信息
                            $scope.isShowReturnGoodsRefill = true;
                        } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_SELLER_CONFIRM_GOODS") {
                            //拒绝退货按钮
                            $scope.isShowReturnGoodsRefuse = true;
                            //同意退款按钮
                            $scope.isShowRefundAgree = true;
                        } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "SELLER_REFUSE_BUYER") {
                            //同意退款按钮
                            $scope.isShowRefundAgree = true;
                        } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "SUCCESS" && !$scope.popRefundOrderToUseInfo.refundInfo.processFlag) {
                            //同步OMS按钮
                            $scope.isShowRefundAgreeSynOms = true;
                        }
                        //已发货（仅退款）
                    } else if ($scope.popRefundOrderToUseInfo.refundsStatus.isShipped && !$scope.popRefundOrderToUseInfo.refundsStatus.isHasGoodReturn) {
                        if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_SELLER_AGREE") {
                            //同意退款按钮
                            $scope.isShowRefundAgree = true;
                            //拒绝退款按钮
                            $scope.isShowRefundRefuse = true;
                        } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "SELLER_REFUSE_BUYER") {
                            //同意退款按钮
                            $scope.isShowRefundAgree = true;
                        } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "SUCCESS" && !$scope.popRefundOrderToUseInfo.refundInfo.processFlag) {
                            //同步OMS按钮
                            $scope.isShowRefundAgreeSynOms = true;
                        }
                        //未发货
                    } else if (!$scope.popRefundOrderToUseInfo.refundsStatus.isShipped) {
                        if (!$scope.popRefundOrderToUseInfo.refundInfo.processFlag) {
                            if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "SUCCESS") {
                                //同步OMS按钮
                                $scope.isShowRefundAgreeSynOms = true;
                            } else {
                                //同意退款按钮
                                $scope.isShowRefundAgree = true;
                                //发货按钮
                                $scope.isShowShipping = true;
                            }
                        }
                    }

                ////阿里以外的场合
                //} else {
                //    if (!$scope.popRefundOrderToUseInfo.refundsStatus.isEndProcess) {
                //        //同意退款按钮
                //        $scope.isShowLocalRefundAgree = true;
                //        //拒绝退款按钮
                //        $scope.isShowLocalRefundRefuse = true;
                //    }
                //}
            }

            /**
             * 点击同意退款按钮操作，输入同意退款信息
             */
            $scope.showAgreeRefund = function () {
                $scope.appContent = false;
                ////阿里的场合
                //if (cartId.Tmall == data.cartId || cartId.TmallG == data.cartId || cartId.Taobao == data.cartId) {
                    //URL 分开无需判定
                    //根据状态显示不同内容
                    if (!$scope.popRefundOrderToUseInfo.refundsStatus.isShipped && $scope.popRefundOrderToUseInfo.refundsStatus.status != "SUCCESS") {
                        //如果未发货
                        $scope.verifyCode = "";
                        $scope.appNotSend = true;
                    } else if (!$scope.popRefundOrderToUseInfo.refundsStatus.isHasGoodReturn && $scope.popRefundOrderToUseInfo.refundsStatus.status != "SUCCESS") {
                        //如果已发货，但仅退款
                        $scope.verifyCode = "";
                        $scope.appSentNoReturn = true;
                    } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_SELLER_AGREE") {
                        //如果已发货，退货退款，同意退货
                        $scope.appReturnGoods = true;
                    } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_BUYER_RETURN_GOODS") {
                        //回补退货信息
                        $scope.refillReturnAddress = true;
                    } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_SELLER_CONFIRM_GOODS") {
                        //如果已发货，退货退款，同意退款
                        $scope.verifyCode = "";
                        $scope.appReturnRefund = true;
                    } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "SELLER_REFUSE_BUYER") {
                        // 拒绝退货后，同意退款
                        $scope.verifyCode = "";
                        $scope.appReturnRefund = true;
                    } else if ($scope.popRefundOrderToUseInfo.refundsStatus.status == "SUCCESS") {
                        // 后台已退款，同步OMS
                        $scope.appReturnRefundSynOMS = true;
                    }
                ////阿里以外的场合
                //} else {
                //    $scope.refundAgreeInLocal = true;
                //}
            }

            /**
             * 点击拒绝退款按钮操作
             */
            $scope.showRefuseRefund = function () {
                $scope.appContent = false;
                //阿里的场合
                if (cartId.Tmall == data.cartId || cartId.TmallG == data.cartId || cartId.Taobao == data.cartId) {
                    //根据状态显示不同内容
                    if(!$scope.popRefundOrderToUseInfo.refundsStatus.isShipped){
                        //如果未发货
                        $scope.refuseNotSend = true;
                    }else if(!$scope.popRefundOrderToUseInfo.refundsStatus.isHasGoodReturn){
                        //如果已发货，但仅退款，拒绝退款
                        $scope.refuseSentNoReturn = true;
                    }else if($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_SELLER_AGREE"){
                        //如果已发货，退货退款，拒绝退款
                        $scope.refuseReturnRefund = true;
                    }else if($scope.popRefundOrderToUseInfo.refundsStatus.status == "WAIT_SELLER_CONFIRM_GOODS"){
                        //如果已发货，退货退款，拒绝退货
                        $scope.refuseReturnGoods = true;
                    }
                    //阿里以外的场合
                } else {
                    $scope.refundRefuseInLocal = true;
                }
            }

            /**
             * 点击取消按钮操作，取消输入
             */
            $scope.doCancel = function () {
                //阿里
                $scope.appNotSend = false;
                $scope.refuseNotSend = false;
                $scope.appSentNoReturn = false;
                $scope.refuseSentNoReturn = false;
                $scope.appReturnGoods = false;
                $scope.refuseReturnGoods = false;
                $scope.appReturnRefund = false;
                $scope.appReturnRefundSynOMS = false;
                $scope.refuseReturnRefund = false;
                $scope.refillReturnAddress = false;
                $scope.showVerifyCode = false;
                //阿里以外
                $scope.refundAgreeInLocal = false;
                $scope.refundRefuseInLocal = false;

                $scope.appContent = true;
            }

            /**
             * 点击OK按钮操作，同意退款
             */
            $scope.doAgreeRefund = function () {
                var data = {};
                data.refundInfo = $scope.popRefundOrderToUseInfo.refundInfo;
                data.refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;

                popRefundOrderService.doAgreeRefund (data)
                    .then (function (data) {
                    // 验证码发送成功，请输入验证码
                    if (data.refundsStatus != undefined && data.refundsStatus.msgCode == '10000') {
                        $scope.showVerifyCode = true;
                    } else {
                        $scope.$parent.setPopupOrderActionForRefund (data);
                        $scope.doClose();
                    }
                });
            };

            /**
             * 点击OK按钮操作，同意退款（同步ＯＭＳ）
             */
            $scope.doAgreeRefundSynOMS = function () {
                var data = {};
                data.refundInfo = $scope.popRefundOrderToUseInfo.refundInfo;
                data.refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;

                popRefundOrderService.doAgreeRefundSynOMS (data)
                    .then (function (data) {
                        $scope.$parent.setPopupOrderActionForRefund (data);
                        $scope.doClose();
                });
            };

            /**
             * 点击OK按钮操作，拒绝退款
             */
            $scope.doRefundRefuse = function () {

                if (!$scope.frmRefundRefuse.$valid) {
                    // TODO 显示请输入reason的message.

                } else if($scope.uploader.flow.files.length == 0) {
                    messageService.alertMessage (messageKeys.ID_200007);
                }
                // 需要输入的场合，必须输入项校验
                else {
                    var data = {};
                    data.refundInfo = $scope.popRefundOrderToUseInfo.refundInfo;
                    data.refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;
                    data.refundInfo.image = $scope.uploader.flow.files[0].imgData;
                    popRefundOrderService.doRefundRefuse (data)
                        .then (function (data) {

                        $scope.$parent.setPopupOrderActionForRefund (data);
                        $scope.doClose();
                    });
                }
            };

            /**
             * 点击OK按钮操作，拒绝退款(阿里以外)
             */
            $scope.doRefundRefuseOther = function () {

                var data = {};
                data.refundInfo = $scope.popRefundOrderToUseInfo.refundInfo;
                data.refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;
                data.refundInfo.image = "";
                popRefundOrderService.doRefundRefuse (data)
                    .then (function (data) {

                    $scope.$parent.setPopupOrderActionForRefund (data);
                    $scope.doClose();
                });
            };

            /**
             * 点击OK按钮操作，同意退货
             */
            $scope.doAgreeReturnGoods = function () {

                var data = {};
                data.refundInfo = $scope.popRefundOrderToUseInfo.refundInfo;
                data.refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;
                popRefundOrderService.doAgreeReturnGoods (data)
                    .then (function (data) {

                    $scope.$parent.setPopupOrderActionForRefund (data);
                    $scope.doClose();
                });
            };

            /**
             * 点击OK按钮操作，回补退货信息
             */
            $scope.doRefillReturnGoods = function () {

                if (!$scope.frmFillReturnAddress.$valid) {
                    // TODO 显示请输入reason的message.
                }
                // 需要输入的场合，必须输入项校验
                else {
                    var data = {};
                    data.refundInfo = $scope.popRefundOrderToUseInfo.refundInfo;
                    data.refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;
                    popRefundOrderService.doRefillReturnGoods (data)
                        .then (function (data) {

                        $scope.$parent.setPopupOrderActionForRefund (data);
                        $scope.doClose();
                    });
                }
            };

            /**
             * 点击OK按钮操作，拒绝退货
             */
            $scope.doRefuseReturnGoods = function () {

                if (!$scope.frmRefuseReturnGoods.$valid) {
                    // TODO 显示请输入reason的message.

                } else if($scope.uploader.flow2.files.length == 0) {
                    messageService.alertMessage (messageKeys.ID_200007);
                }
                // 需要输入的场合，必须输入项校验
                else {
                    var data = {};
                    data.refundInfo = $scope.popRefundOrderToUseInfo.refundInfo;
                    data.refundInfo.orderNumber = $scope.popRefundOrderToUseInfo.orderNumber;
                    data.refundInfo.image = $scope.uploader.flow2.files[0].imgData;
                    popRefundOrderService.doRefuseReturnGoods (data)
                        .then (function (data) {

                        $scope.$parent.setPopupOrderActionForRefund (data);
                        $scope.doClose();
                    });
                }
            };

            /**
             * 切换退款.
             */
            $scope.changeRefund = function (refundInfo) {

                var data = {};
                data.refundInfo = refundInfo;
                popRefundOrderService.doChangeRefund (data)
                    .then (function (data) {
                    $scope.popRefundOrderToUseInfo.refundInfo = data.refundInfo;
                    $scope.popRefundOrderToUseInfo.refundMessagesList = data.refundMessagesList;
                    $scope.currentrefundId = data.refundInfo.refundId;
                    $scope.popRefundOrderToUseInfo.refundsStatus = data.refundsStatus;
                    //变量初期化
                    $scope.doInitVar();
                    //  画面按钮显示
                    $scope.showButton();
                });
            }

            /**
             * 切换退款申请，变量初期化。
             */
            $scope.doInitVar = function () {
                $scope.appContent = true;
                //阿里
                //未发货，同意退款
                $scope.appNotSend = false;
                //未发货，拒绝退款
                $scope.refuseNotSend = false;
                //已发货仅退款，同意退款
                $scope.appSentNoReturn = false;
                //已发货仅退款，拒绝退款
                $scope.refuseSentNoReturn = false;
                //已发货退货退款，同意退货
                $scope.appReturnGoods = false;
                //已发货退货退款，拒绝退货
                $scope.refuseReturnGoods = false;
                //已发货退货退款，同意退款
                $scope.appReturnRefund = false;
                //已发货退货退款，拒绝退款
                $scope.refuseReturnRefund = false;
                //已发货退货退款，回补退货信息
                $scope.refillReturnAddress = false;
                //同意退款（阿里以外）
                $scope.refundAgreeInLocal = false;

                //验证码
                $scope.verifyCode = "";
                $scope.showVerifyCode = false;

                //拒绝退款按钮
                $scope.isShowRefundRefuse = false;
                //同意退款按钮
                $scope.isShowRefundAgree = false;
                //同步OMS退款按钮
                $scope.isShowRefundAgreeSynOms = false;
                //同意退货按钮
                $scope.isShowReturnGoodsAgree = false;
                //拒绝退货按钮
                $scope.isShowReturnGoodsRefuse = false;
                //回填物流信息
                $scope.isShowReturnGoodsRefill = false;
                //发货按钮
                $scope.isShowShipping = false;

                //（阿里以外）
                //同意退款按钮
                $scope.isShowLocalRefundAgree = false;
                //拒绝退款按钮
                $scope.isShowLocalRefundRefuse = false;
            }

            /**
             * 关闭窗口，并初始化该页面输入内容.
             */
            $scope.doClose = function () {
                $scope.$parent.doPopupClose();
                $scope.closeThisDialog();
            };
        }])
});
