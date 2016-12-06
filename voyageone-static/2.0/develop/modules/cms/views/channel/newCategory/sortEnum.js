define(function () {

    return {
        sortList: [
            {
                sName: '创建时间',
                sValue: 'created'
            },
            {
                sName: '库存',
                sValue: 'common.fields.quantity'
            },
            {
                sName: '最终售价',
                sValue: 'platforms.p✓.pPriceSaleEd'
            },
            {
                sName: '7天销售排序',
                sValue: 'codeSum7.cartId✓'
            },
            {
                sName: '30天销售排序',
                sValue: 'codeSum30.cartId✓'
            },
            {
                sName: '总销售降序',
                sValue: 'codeSumYear.cartId✓'
            }
        ],
        getSortByCd: function (cartId) {
            var self = this;

            if (!cartId)
                return;

            return _.each(self.sortList, function (element) {
                element.sValue = element.sValue.replace("✓", cartId);
            });
        }
    }

});