/**
 * @description 价格显示directive
 *              输入一组价格，显示出 min ~ max 价格区间
 * @author Piao
 */
angular.module("voyageone.angular.directives").directive("price", function () {
    return {
        restrict: "E",
        scope: {
            prices: "=prices"
        },
        link: function (scope, element) {
            var prices = scope.prices;

            if (!prices || prices.length === 0) {
                console.warn('directive:price=> 请输入要显示的价格');
                return;
            }

            if (prices.length === 1)
                element.html(prices[0]);

            var min = _.min(prices),
                max = max = _.max(prices),
                compiled = _.template("<%= min %> ~ <%= max %>");

            if (min === max)
                element.html(min);
            else
                element.html(compiled({min: min, max: max}));
        }
    };
});