/**
 * Created by edward-pc1 on 2015/9/18.
 */

define(function (require) {

    var cmsApp = require('modules/cms/cms.module');
    require('components/services/ajax.service');

    cmsApp.service('popSetProductColumnsService',['$q', '$rootScope', 'cmsAction', 'ajaxService', 'userService',
        function ($q, $rootScope, cmsAction, ajaxService, userService) {

            /**
             * 根据Type取得不同的产品属性列表.
             * @param type
             * @returns {*}
             */
            this.doGetProductColumns = function (type) {
                var defer = $q.defer();
                switch (type) {
                    case 'us':
                        defer.resolve($rootScope.cmsMaster.USProductAttributes);
                        break;
                    case 'cn':
                        defer.resolve($rootScope.cmsMaster.CNProductAttributes);
                        break;
                }

                return defer.promise;
            };

            /**
             * 更新该用户的产品显示属性列表.
             * @param data
             * @returns {*}
             */
            this.doSaveProductColumns = function (data) {
                var defer = $q.defer();
                ajaxService.ajaxPostWithData(data, cmsAction.popSetProductColumns.doSaveProductColumns)
                    .then(function (response) {
                        defer.resolve(response.data);
                    });

                return defer.promise;
            };

            // TODO 测试时使用的数据
            //function testGetProductColumns (type) {
            //    if (type === 'us') {
            //        return [{attributeValueId: '1', attributeValue: 'Code', attributeValue2: 'CMS_TXT_CODE', attributeValue3: '1'},
            //            {attributeValueId: '2', attributeValue: 'Name', attributeValue2: 'CMS_TXT_NAME', attributeValue3: '1'},
            //            {attributeValueId: '3', attributeValue: 'Display Order', attributeValue2: 'CMS_TXT_DISPLAY_ORDER', attributeValue3: '1'},
            //            {attributeValueId: '4', attributeValue: 'Is High Light Product ?', attributeValue2: 'CMS_TXT_IS_HIGH_LIGHT_PRODUCT', attributeValue3: '1'},
            //            {attributeValueId: '5', attributeValue: 'Product Type', attributeValue2: 'CMS_TXT_PRODUCT_TYPE', attributeValue3: '1'},
            //            {attributeValueId: '6', attributeValue: 'Brand', attributeValue2: 'CMS_TXT_BRAND', attributeValue3: '1'},
            //            {attributeValueId: '7', attributeValue: 'Size Type', attributeValue2: 'CMS_TXT_SIZE_TYPE', attributeValue3: '1'},
            //            {attributeValueId: '8', attributeValue: 'Is New Arrival ?', attributeValue2: 'CMS_TXT_IS_NEW_ARRIVAL', attributeValue3: '1'},
            //            {attributeValueId: '9', attributeValue: 'Is Reward Eligible ?', attributeValue2: 'CMS_TXT_IS_REWARD_ELIGIBLE', attributeValue3: '1'},
            //            {attributeValueId: '10', attributeValue: 'Is Discount Eligible ?', attributeValue2: 'CMS_TXT_IS_DISCOUNT_ELIGIBLE', attributeValue3: '1'},
            //            {attributeValueId: '11', attributeValue: 'Is Phone Order Only ?', attributeValue2: 'CMS_TXT_IS_PHONE_ORDER_ONLY', attributeValue3: '1'},
            //            {attributeValueId: '12', attributeValue: 'Is Approved Description ?', attributeValue2: 'CMS_TXT_IS_APPROVED_DESCRIPTION', attributeValue3: '1'},
            //            {attributeValueId: '13', attributeValue: 'Color Map', attributeValue2: 'CMS_TXT_COLOR_MAP', attributeValue3: '1'},
            //            {attributeValueId: '14', attributeValue: 'Color', attributeValue2: 'CMS_TXT_COLOR', attributeValue3: '1'},
            //            {attributeValueId: '15', attributeValue: 'Made In', attributeValue2: 'CMS_TXT_MADE_IN_COUNTRY', attributeValue3: '1'},
            //            {attributeValueId: '16', attributeValue: 'Material Fabric', attributeValue2: 'CMS_TXT_MATERIAL_FABRIC', attributeValue3: '1'},
            //            {attributeValueId: '17', attributeValue: 'Abstract', attributeValue2: 'CMS_TXT_ABSTRACT', attributeValue3: '1'},
            //            {attributeValueId: '18', attributeValue: 'Accessory', attributeValue2: 'CMS_TXT_ACCESSORY', attributeValue3: '1'},
            //            {attributeValueId: '19', attributeValue: 'Short Description', attributeValue2: 'CMS_TXT_SHORT_DESCRIPTION', attributeValue3: '1'},
            //            {attributeValueId: '20', attributeValue: 'Long Description', attributeValue2: 'CMS_TXT_LONG_DESCRIPTION', attributeValue3: '1'},
            //            {attributeValueId: '21', attributeValue: 'Order Limit Count', attributeValue2: 'CMS_TXT_ORDER_LIMIT_COUNT', attributeValue3: '1'},
            //            {attributeValueId: '22', attributeValue: 'Promotion Tag', attributeValue2: 'CMS_TXT_PROMOTION_TAG', attributeValue3: '1'},
            //            {attributeValueId: '23', attributeValue: 'Quantity', attributeValue2: 'CMS_TXT_QUANTITY', attributeValue3: '1'},
            //            {attributeValueId: '24', attributeValue: 'URL Key', attributeValue2: 'CMS_TXT_URL_KEY', attributeValue3: '1'},
            //            {attributeValueId: '25', attributeValue: 'Created On', attributeValue2: 'CMS_TXT_CREATED_ON', attributeValue3: '1'},
            //            {attributeValueId: '26', attributeValue: 'Last Updated On', attributeValue2: 'CMS_TXT_LAST_UPDATED_ON', attributeValue3: '1'},
            //            {attributeValueId: '27', attributeValue: 'MSRP', attributeValue2: 'CMS_TXT_MSRP', attributeValue3: '2'},
            //            {attributeValueId: '28', attributeValue: 'U.S Official Price', attributeValue2: 'CMS_TXT_US_OFFICIAL_PRICE', attributeValue3: '2'},
            //            {attributeValueId: '29', attributeValue: 'U.S Official Shipping Type', attributeValue2: 'CMS_TXT_US_OFFICIAL_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '30', attributeValue: 'U.S Official Is On Sale ?', attributeValue2: 'CMS_TXT_US_OFFICIAL_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '31', attributeValue: 'U.S Official Is Approved ?', attributeValue2: 'CMS_TXT_US_OFFICIAL_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '32', attributeValue: 'U.S Official Sneaker RX Sales In 7 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '33', attributeValue: 'U.S Official Sneaker RX Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '34', attributeValue: 'U.S Official Sneaker RX Sales In 30 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '35', attributeValue: 'U.S Official Sneaker RX Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '36', attributeValue: 'U.S Official Sneaker RX Sales In This Year', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '37', attributeValue: 'U.S Official Sneaker RX Sales In This Year Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_RX_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '38', attributeValue: 'U.S Official Sneaker Sales In 7 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '39', attributeValue: 'U.S Official Sneaker Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '40', attributeValue: 'U.S Official Sneaker Sales In 30 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '41', attributeValue: 'U.S Official Sneaker Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '42', attributeValue: 'U.S Official Sneaker Sales In This Year', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '43', attributeValue: 'U.S Official Sneaker Sales In This Year Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '44', attributeValue: 'U.S Official Sneaker WS Sales In 7 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '45', attributeValue: 'U.S Official Sneaker WS Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '46', attributeValue: 'U.S Official Sneaker WS Sales In 30 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '47', attributeValue: 'U.S Official Sneaker WS Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '48', attributeValue: 'U.S Official Sneaker WS Sales In This Year', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '49', attributeValue: 'U.S Official Sneaker WS Sales In This Year Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_WS_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '50', attributeValue: 'U.S Official Sneaker Mobile Sales In 7 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '51', attributeValue: 'U.S Official Sneaker Mobile Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '52', attributeValue: 'U.S Official Sneaker Mobile Sales In 30 Days', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '53', attributeValue: 'U.S Official Sneaker Mobile Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '54', attributeValue: 'U.S Official Sneaker Mobile Sales In This Year', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '55', attributeValue: 'U.S Official Sneaker Mobile Sales In This Year Percent', attributeValue2: 'CMS_TXT_US_OFFICIAL_SNEAKER_MOBILE_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '56', attributeValue: 'U.S Amazon Price', attributeValue2: 'CMS_TXT_US_AMAZON_PRICE', attributeValue3: '2'},
            //            {attributeValueId: '57', attributeValue: 'U.S Amazon Shipping Type', attributeValue2: 'CMS_TXT_US_AMAZON_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '58', attributeValue: 'U.S Amazon Is On Sale ?', attributeValue2: 'CMS_TXT_US_AMAZON_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '59', attributeValue: 'U.S Amazon Is Approved ?', attributeValue2: 'CMS_TXT_US_AMAZON_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '60', attributeValue: 'U.S Amazon Pre Publish DateTime', attributeValue2: 'CMS_TXT_US_AMAZON_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '61', attributeValue: 'U.S Amazon Sales In 7 Days', attributeValue2: 'CMS_TXT_US_AMAZON_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '62', attributeValue: 'U.S Amazon Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_US_AMAZON_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '63', attributeValue: 'U.S Amazon Sales In 30 Days', attributeValue2: 'CMS_TXT_US_AMAZON_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '64', attributeValue: 'U.S Amazon Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_US_AMAZON_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '65', attributeValue: 'U.S Amazon Sales In This Year', attributeValue2: 'CMS_TXT_US_AMAZON_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '66', attributeValue: 'U.S Amazon Sales In This Year Percent', attributeValue2: 'CMS_TXT_US_AMAZON_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '67', attributeValue: 'CN Price: $', attributeValue2: 'CMS_TXT_CN_PRICE', attributeValue3: '2'},
            //            {attributeValueId: '68', attributeValue: 'CN Default Price: ￥', attributeValue2: 'CMS_TXT_CN_PRICE_RMB', attributeValue3: '2'},
            //            {attributeValueId: '69', attributeValue: 'CN Final Price: ￥', attributeValue2: 'CMS_TXT_CN_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '70', attributeValue: 'CN Shipping Type', attributeValue2: 'CMS_TXT_CN_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '71', attributeValue: 'CN Is On Sale ?', attributeValue2: 'CMS_TXT_CN_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '72', attributeValue: 'CN Is Approved ?', attributeValue2: 'CMS_TXT_CN_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '73', attributeValue: 'CN Pre Publish Date Time', attributeValue2: 'CMS_TXT_CN_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '74', attributeValue: 'CN Sales In 7 Days', attributeValue2: 'CMS_TXT_CN_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '75', attributeValue: 'CN Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_CN_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '76', attributeValue: 'CN Sales In 30 Days', attributeValue2: 'CMS_TXT_CN_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '77', attributeValue: 'CN Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_CN_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '78', attributeValue: 'CN Sales In This Year', attributeValue2: 'CMS_TXT_CN_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '79', attributeValue: 'CN Sales In This Year Percent', attributeValue2: 'CMS_TXT_CN_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'}
            //        ]
            //    } else if (type === 'cn') {
            //        return [{attributeValueId: '1', attributeValue: 'Code', attributeValue2: 'CMS_TXT_CODE', attributeValue3: '1'},
            //            {attributeValueId: '2', attributeValue: 'Name', attributeValue2: 'CMS_TXT_NAME', attributeValue3: '1'},
            //            {attributeValueId: '3', attributeValue: 'Display Order', attributeValue2: 'CMS_TXT_DISPLAY_ORDER', attributeValue3: '1'},
            //            {attributeValueId: '4', attributeValue: 'Is High Light Product ?', attributeValue2: 'CMS_TXT_IS_HIGH_LIGHT_PRODUCT', attributeValue3: '1'},
            //            {attributeValueId: '5', attributeValue: 'Product Type', attributeValue2: 'CMS_TXT_PRODUCT_TYPE', attributeValue3: '1'},
            //            {attributeValueId: '6', attributeValue: 'Brand', attributeValue2: 'CMS_TXT_BRAND', attributeValue3: '1'},
            //            {attributeValueId: '7', attributeValue: 'Size Type', attributeValue2: 'CMS_TXT_SIZE_TYPE', attributeValue3: '1'},
            //            {attributeValueId: '8', attributeValue: 'Color Map', attributeValue2: 'CMS_TXT_COLOR_MAP', attributeValue3: '1'},
            //            {attributeValueId: '9', attributeValue: 'Color', attributeValue2: 'CMS_TXT_COLOR', attributeValue3: '1'},
            //            {attributeValueId: '10', attributeValue: 'Made In', attributeValue2: 'CMS_TXT_MADE_IN_COUNTRY', attributeValue3: '1'},
            //            {attributeValueId: '11', attributeValue: 'Material Fabric', attributeValue2: 'CMS_TXT_MATERIAL_FABRIC', attributeValue3: '1'},
            //            {attributeValueId: '12', attributeValue: 'Abstract', attributeValue2: 'CMS_TXT_ABSTRACT', attributeValue3: '1'},
            //            {attributeValueId: '13', attributeValue: 'Short Description', attributeValue2: 'CMS_TXT_SHORT_DESCRIPTION', attributeValue3: '1'},
            //            {attributeValueId: '14', attributeValue: 'Long Description', attributeValue2: 'CMS_TXT_LONG_DESCRIPTION', attributeValue3: '1'},
            //            {attributeValueId: '15', attributeValue: 'Quantity', attributeValue2: 'CMS_TXT_QUANTITY', attributeValue3: '1'},
            //            {attributeValueId: '16', attributeValue: 'URL Key', attributeValue2: 'CMS_TXT_URL_KEY', attributeValue3: '1'},
            //            {attributeValueId: '17', attributeValue: 'Created On', attributeValue2: 'CMS_TXT_CREATED_ON', attributeValue3: '1'},
            //            {attributeValueId: '18', attributeValue: 'Last Updated On', attributeValue2: 'CMS_TXT_LAST_UPDATED_ON', attributeValue3: '1'},
            //            {attributeValueId: '19', attributeValue: 'Reference MSRP: ￥', attributeValue2: 'CMS_TXT_REFERENCE_MSRP', attributeValue3: '2'},
            //            {attributeValueId: '20', attributeValue: 'Reference Price: ￥', attributeValue2: 'CMS_TXT_REFERENCE_PRICE', attributeValue3: '2'},
            //            {attributeValueId: '21', attributeValue: 'CN Price: $', attributeValue2: 'CMS_TXT_CN_PRICE', attributeValue3: '2'},
            //            {attributeValueId: '22', attributeValue: 'CN Default Price: ￥', attributeValue2: 'CMS_TXT_CN_PRICE_RMB', attributeValue3: '2'},
            //            {attributeValueId: '23', attributeValue: 'CN Final Price: ￥', attributeValue2: 'CMS_TXT_CN_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '24', attributeValue: 'CN Is On Sale ?', attributeValue2: 'CMS_TXT_CN_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '25', attributeValue: 'CN Is Approved ?', attributeValue2: 'CMS_TXT_CN_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '26', attributeValue: 'CN Sales In 7 Days', attributeValue2: 'CMS_TXT_CN_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '27', attributeValue: 'CN Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_CN_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '28', attributeValue: 'CN Sales In 30 Days', attributeValue2: 'CMS_TXT_CN_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '29', attributeValue: 'CN Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_CN_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '30', attributeValue: 'CN Sales In This Year', attributeValue2: 'CMS_TXT_CN_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '31', attributeValue: 'CN Sales In This Year Percent', attributeValue2: 'CMS_TXT_CN_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '32', attributeValue: 'CN Official Final Price: ￥', attributeValue2: 'CMS_TXT_CN_OFFICIAL_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '33', attributeValue: 'CN Official Shipping Type', attributeValue2: 'CMS_TXT_CN_OFFICIAL_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '34', attributeValue: 'CN Official Is On Sale ?', attributeValue2: 'CMS_TXT_CN_OFFICIAL_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '35', attributeValue: 'CN Official Is Approved ?', attributeValue2: 'CMS_TXT_CN_OFFICIAL_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '36', attributeValue: 'CN Official Pre Publish DateTime', attributeValue2: 'CMS_TXT_CN_OFFICIAL_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '37', attributeValue: 'CN Official Sales In 7 Days', attributeValue2: 'CMS_TXT_CN_OFFICIAL_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '38', attributeValue: 'CN Official Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_CN_OFFICIAL_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '39', attributeValue: 'CN Official Sales In 30 Days', attributeValue2: 'CMS_TXT_CN_OFFICIAL_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '40', attributeValue: 'CN Official Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_CN_OFFICIAL_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '41', attributeValue: 'CN Official Sales In This Year', attributeValue2: 'CMS_TXT_CN_OFFICIAL_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '42', attributeValue: 'CN Official Sales In This Year Percent', attributeValue2: 'CMS_TXT_CN_OFFICIAL_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '43', attributeValue: 'TB Final Price: ￥', attributeValue2: 'CMS_TXT_TB_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '44', attributeValue: 'TB Shipping Type', attributeValue2: 'CMS_TXT_TB_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '45', attributeValue: 'TB Is On Sale ?', attributeValue2: 'CMS_TXT_TB_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '46', attributeValue: 'TB Is Approved ?', attributeValue2: 'CMS_TXT_TB_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '47', attributeValue: 'TB Pre Publish DateTime', attributeValue2: 'CMS_TXT_TB_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '48', attributeValue: 'TB Sales In 7 Days', attributeValue2: 'CMS_TXT_TB_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '49', attributeValue: 'TB Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_TB_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '50', attributeValue: 'TB Sales In 30 Days', attributeValue2: 'CMS_TXT_TB_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '51', attributeValue: 'TB Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_TB_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '52', attributeValue: 'TB Sales In This Year', attributeValue2: 'CMS_TXT_TB_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '53', attributeValue: 'TB Sales In This Year Percent', attributeValue2: 'CMS_TXT_TB_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '54', attributeValue: 'TM Final Price: ￥', attributeValue2: 'CMS_TXT_TM_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '55', attributeValue: 'TM Shipping Type', attributeValue2: 'CMS_TXT_TM_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '56', attributeValue: 'TM Is On Sale ?', attributeValue2: 'CMS_TXT_TM_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '57', attributeValue: 'TM Is Approved ?', attributeValue2: 'CMS_TXT_TM_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '58', attributeValue: 'TM Pre Publish DateTime', attributeValue2: 'CMS_TXT_TM_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '59', attributeValue: 'TM Sales In 7 Days', attributeValue2: 'CMS_TXT_TM_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '60', attributeValue: 'TM Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_TM_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '61', attributeValue: 'TM Sales In 30 Days', attributeValue2: 'CMS_TXT_TM_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '62', attributeValue: 'TM Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_TM_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '63', attributeValue: 'TM Sales In This Year', attributeValue2: 'CMS_TXT_TM_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '64', attributeValue: 'TM Sales In This Year Percent', attributeValue2: 'CMS_TXT_TM_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '65', attributeValue: 'TG Final Price: ￥', attributeValue2: 'CMS_TXT_TG_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '66', attributeValue: 'TG Shipping Type', attributeValue2: 'CMS_TXT_TG_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '67', attributeValue: 'TG Is On Sale ?', attributeValue2: 'CMS_TXT_TG_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '68', attributeValue: 'TG Is Approved ?', attributeValue2: 'CMS_TXT_TG_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '69', attributeValue: 'TG Pre Publish DateTime', attributeValue2: 'CMS_TXT_TG_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '70', attributeValue: 'TG Sales In 7 Days', attributeValue2: 'CMS_TXT_TG_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '71', attributeValue: 'TG Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_TG_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '72', attributeValue: 'TG Sales In 30 Days', attributeValue2: 'CMS_TXT_TG_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '73', attributeValue: 'TG Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_TG_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '74', attributeValue: 'TG Sales In This Year', attributeValue2: 'CMS_TXT_TG_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '75', attributeValue: 'TG Sales In This Year Percent', attributeValue2: 'CMS_TXT_TG_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '76', attributeValue: 'JD Final Price: ￥', attributeValue2: 'CMS_TXT_JD_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '77', attributeValue: 'JD Shipping Type', attributeValue2: 'CMS_TXT_JD_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '78', attributeValue: 'JD Is On Sale ?', attributeValue2: 'CMS_TXT_JD_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '79', attributeValue: 'JD Is Approved ?', attributeValue2: 'CMS_TXT_JD_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '80', attributeValue: 'JD Pre Publish DateTime', attributeValue2: 'CMS_TXT_JD_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '81', attributeValue: 'JD Sales In 7 Days', attributeValue2: 'CMS_TXT_JD_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '82', attributeValue: 'JD Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_JD_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '83', attributeValue: 'JD Sales In 30 Days', attributeValue2: 'CMS_TXT_JD_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '84', attributeValue: 'JD Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_JD_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '85', attributeValue: 'JD Sales In This Year', attributeValue2: 'CMS_TXT_JD_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '86', attributeValue: 'JD Sales In This Year Percent', attributeValue2: 'CMS_TXT_JD_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '87', attributeValue: 'JG Final Price: ￥', attributeValue2: 'CMS_TXT_JG_PRICE_FINAL_RMB', attributeValue3: '2'},
            //            {attributeValueId: '88', attributeValue: 'JG Shipping Type', attributeValue2: 'CMS_TXT_JG_SHIPPING_TYPE', attributeValue3: '2'},
            //            {attributeValueId: '89', attributeValue: 'JG Is On Sale ?', attributeValue2: 'CMS_TXT_JG_IS_ON_SALE', attributeValue3: '2'},
            //            {attributeValueId: '90', attributeValue: 'JG Is Approved ?', attributeValue2: 'CMS_TXT_JG_IS_APPROVED', attributeValue3: '2'},
            //            {attributeValueId: '91', attributeValue: 'JG Pre Publish DateTime', attributeValue2: 'CMS_TXT_JG_PRE_PUBLISH_DATE_TIME', attributeValue3: '2'},
            //            {attributeValueId: '92', attributeValue: 'JG Sales In 7 Days', attributeValue2: 'CMS_TXT_JG_SALES_7_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '93', attributeValue: 'JG Sales In 7 Days Percent', attributeValue2: 'CMS_TXT_JG_SALES_7_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '94', attributeValue: 'JG Sales In 30 Days', attributeValue2: 'CMS_TXT_JG_SALES_30_DAYS', attributeValue3: '3'},
            //            {attributeValueId: '95', attributeValue: 'JG Sales In 30 Days Percent', attributeValue2: 'CMS_TXT_JG_SALES_30_DAYS_PERCENT', attributeValue3: '3'},
            //            {attributeValueId: '96', attributeValue: 'JG Sales In This Year', attributeValue2: 'CMS_TXT_JG_SALES_THIS_YEAR', attributeValue3: '3'},
            //            {attributeValueId: '97', attributeValue: 'JG Sales In This Year Percent', attributeValue2: 'CMS_TXT_JG_SALES_THIS_YEAR_PERCENT', attributeValue3: '3'}
            //        ]
            //    }
            //}
        }]);
    return cmsApp;
});
