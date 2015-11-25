/**
 * Created by edward-pc1 on 2015/8/3.
 */


define (function (require) {

    var _ = require ('underscore');
    var cmsCommonUtil = {};

    /**
     * 通过tempDiscount 计算出当前价格基础上的折扣价格.
     * @param paramCurrentPrice
     * @param paramTempDiscount
     * @returns {string}
     */
    cmsCommonUtil.accountPriceByTempDiscount = function (paramCurrentPrice, paramTempDiscount) {

        var currentPrice = isNaN(parseFloat(paramCurrentPrice)) ? 0.00 : parseFloat(paramCurrentPrice);
        var tempDiscount = isNaN(parseFloat(paramTempDiscount)) ? 0.00 : parseFloat(paramTempDiscount);
        var price = '0.00';

        if (tempDiscount >= 0.00 && tempDiscount <= 100.00) {
            price = parseFloat(currentPrice * ( 1 - tempDiscount / 100)).toFixed(2).toString();
        }

        return price;
    };

    /**
     * 通过price计算出在当前价格基础上的折扣信息.
     * @param paramCurrentPrice
     * @param paramPrice
     * @returns {string}
     */
    cmsCommonUtil.accountTempDiscountByPrice = function (paramCurrentPrice, paramPrice) {

        var currentPrice = isNaN(parseFloat(paramCurrentPrice)) ? 0.00 : parseFloat(paramCurrentPrice);
        var price = isNaN(parseFloat(paramPrice)) ? 0.00 : parseFloat(paramPrice);
        var tempDiscount = '0.00';

        if (price >= 0.00 && price <= currentPrice) {
            tempDiscount = parseFloat((currentPrice - price) / currentPrice * 100).toFixed(2).toString();
        }

        return tempDiscount;
    };

    /**
     * 通过CN Price计算出Default Price.
     * @param paramCnPrice
     * @param paramExchangeRate
     * @returns {string}
     */
    cmsCommonUtil.accountDefaultPriceByPrice = function (paramCnPrice, paramExchangeRate) {

        var cnPrice = isNaN(parseFloat(paramCnPrice)) ? 0.00 : parseFloat(paramCnPrice);
        var exchangeRate = isNaN(parseFloat(paramExchangeRate)) ? 0.00 : parseFloat(paramExchangeRate);
        var defaultPrice = '0.00';

        if (cnPrice >= 0.00 && exchangeRate >= 0.00) {
            defaultPrice = parseFloat(cnPrice * exchangeRate).toFixed(2).toString();
        }

        return defaultPrice;
    };

    /**
     * 计算出最终售价.
     * @param paramDefaultPrice
     * @param paramAdjustment
     * @returns {*}
     */
    cmsCommonUtil.accountFinalPrice = function (paramDefaultPrice, paramAdjustment) {

        var defaultPrice = isNaN(parseFloat(paramDefaultPrice)) ? 0.00 : parseFloat(paramDefaultPrice);
        var adjustmentPrice = isNaN(parseFloat(paramAdjustment)) ? 0.00 : parseFloat(paramAdjustment);
        var price = defaultPrice + adjustmentPrice;

        if (price < 0.00) {
            price = '0.00';
        }

        return price.toString();
    };

    /**
     * 计算adjustmentPrice的信息.
     * @param paramDefaultPrice
     * @param paramFinalPrice
     * @returns {string}
     */
    cmsCommonUtil.accountAdjustmentPrice = function (paramDefaultPrice, paramFinalPrice) {

        var defaultPrice = isNaN(parseFloat(paramDefaultPrice)) ? 0.00 : parseFloat(paramDefaultPrice);
        var finalPrice = isNaN(parseFloat(paramFinalPrice)) ? 0.00 : parseFloat(paramFinalPrice);
        var adjustmentPrice = finalPrice - defaultPrice;

        //if (adjustmentPrice < 0.00) {
        //    adjustmentPrice = '0.00';
        //}

        return adjustmentPrice.toString();
    };

    /**
     * 计算出effectivePrice信息.
     * @param paramFinalPrice
     * @param paramOverHead1
     * @param paramOverHead2
     * @param paramPriceRate
     * @param paramShippingOffset
     * @returns {string}
     */
    cmsCommonUtil.accountEffectivePrice = function(paramFinalPrice, paramOverHead1, paramOverHead2, paramPriceRate, paramShippingOffset) {

        var finalPrice = isNaN(parseFloat(paramFinalPrice)) ? 0.00 : parseFloat(paramFinalPrice);
        var overhead1 = isNaN(parseFloat(paramOverHead1)) ? 0.00 : parseFloat(paramOverHead1);
        var overhead2 = isNaN(parseFloat(paramOverHead2)) ? 0.00 : parseFloat(paramOverHead2);
        var priceRate = isNaN(parseFloat(paramPriceRate)) ? 0.00 : parseFloat(paramPriceRate);
        var shippingOffset = isNaN(parseFloat(paramShippingOffset)) ? 0.00 : parseFloat(paramShippingOffset);
        var effectivePrice = '0.00';

        if (finalPrice > 0.00) {
            effectivePrice = parseFloat(finalPrice * (1 - overhead1 - overhead2) / priceRate - shippingOffset).toFixed(2);
            effectivePrice = effectivePrice > 0.00 ? effectivePrice.toString() : '0.00';
        }

        return effectivePrice;
    };

    return cmsCommonUtil;
});
