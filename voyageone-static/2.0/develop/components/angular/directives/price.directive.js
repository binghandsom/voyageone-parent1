/**
 * @description 价格显示directive
 *              输入一组价格，显示出 min ~ max 价格区间
 * @author Piao
 */
(function () {

    function _priceScale(prices) {
        if (!prices || prices.length === 0) {
            console.warn('directive:price=> 请输入要显示的价格');
            return;
        }

        if (prices.length === 1)
            return prices[0];

        var min = _.min(prices),
            max  = _.max(prices),
            compiled = _.template("<%= min %> ~ <%= max %>");

        if (min === max)
            return min;
        else
            return compiled({min: min, max: max});
    }

    angular.module("voyageone.angular.directives").directive("price", function () {
        return {
            restrict: "E",
            scope: {
                prices: "=prices"
            },
            link: function (scope, element) {
                element.html(_priceScale(scope.prices));
            }
        };
    }).directive("clientMsrpPrice", function ($compile) {
        return {
            restrict: "E",
            scope: {
                data: "=data"
            },
            link: function (scope, element) {

                var skuList = scope.data,
                    final = [],rangArr = [],
                    buttonPopover = angular.element('<button  type="button">');

                buttonPopover.attr('ng-controller','showPopoverCtrl');
                buttonPopover.attr('popover-title','客户建议零售价');
                buttonPopover.attr('popover-placement','left');
                buttonPopover.addClass('btn btn-default btn-xs');

                if (!skuList){
                    console.warn('没有提供sku数据！');
                    return;
                }


                if (skuList instanceof Array) {

                    angular.forEach(skuList, function (element) {

                        var str = element.skuCode + ' : ' + element.clientMsrpPrice,
                            cmcf = element.clientMsrpPriceChgFlg,
                            labelStr = '';

                        if (cmcf && cmcf != 0 && !/^\w{1}0%$/.test(cmcf)) {

                            if (cmcf.indexOf('U') >= 0) {
                                labelStr += '<label class="text-u-red font-bold">&nbsp;(↑' + cmcf.substring(1) + ')</label>';
                            } else {
                                labelStr += '<label class="text-u-green font-bold">&nbsp;(↓' + cmcf.substring(1) + ')</label>';
                            }
                            //记录标识涨幅的label标签
                            rangArr.push(labelStr);
                        }

                        final.push(str + labelStr);

                    });

                } else {
                    console.warn('传入的数据结构应该是数组！');
                }

                buttonPopover.attr('popover-html', 'showInfo(' + JSON.stringify(final) + ')');

                if(rangArr[0])
                    buttonPopover.html(_priceScale(_.pluck(skuList, 'clientMsrpPrice')) + rangArr[0]);
                else
                    buttonPopover.html(_priceScale(_.pluck(skuList, 'clientMsrpPrice')));

                element.html($compile(buttonPopover)(scope.$new()));
            }
        }
    });

})();