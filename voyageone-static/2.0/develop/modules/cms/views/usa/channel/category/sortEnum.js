define(function () {

    return {
        1: {
            sortName: 'Newest',
            sortValue: 'lastReceivedOn',
            sortType: '-1'
        },
        2: {
            sortName: 'Name(A~Z)',
            sortValue: 'nameEn',
            sortType: '1'
        },
        3: {
            sortName: 'Name(Z~A)',
            sortValue: 'nameEn',
            sortType: '-1'
        },
        4: {
            sortName: 'Highest Price',
            sortValue: 'usPlatforms.P8.pPriceSaleSt,usPlatforms.P8.pPriceSaleEd',
            sortType: '-1'
        },
        5: {
            sortName: 'Lowest Price',
            sortValue: 'usPlatforms.P8.pPriceSaleSt,usPlatforms.P8.pPriceSaleEd',
            sortType: '1'
        },
        6: {
            sortName: 'Highest Qty',
            sortValue: 'quantity',
            sortType: '-1'
        },
        7: {
            sortName: 'Lowest Qty',
            sortValue: 'quantity',
            sortType: '1'
        }
    }


});