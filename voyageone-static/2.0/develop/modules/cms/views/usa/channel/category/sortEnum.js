define(function () {

    return {
        1: {
            sortName: 'Newest',
            sortValue: 'common.fields.lastReceivedOn',
            sortType: '-1'
        },
        2: {
            sortName: 'Name(A~Z)',
            sortValue: 'common.fields.productNameEn',
            sortType: '1'
        },
        3: {
            sortName: 'Name(Z~A)',
            sortValue: 'common.fields.productNameEn',
            sortType: '-1'
        },
        4: {
            sortName: 'Highest Price',
            sortValue: 'usPlatforms.P8.pPriceSaleEd',
            sortType: '-1'
        },
        5: {
            sortName: 'Lowest Price',
            sortValue: 'usPlatforms.P8.pPriceSaleSt',
            sortType: '1'
        },
        6: {
            sortName: 'Highest Qty',
            sortValue: 'common.fields.quantity',
            sortType: '-1'
        },
        7: {
            sortName: 'Lowest Qty',
            sortValue: 'common.fields.quantity',
            sortType: '1'
        }
    }


});