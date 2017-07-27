define(function () {

    return {
        1: {
            sortName: 'Newest',
            arrowName:'lastReceivedOn',
            sortValue: 'common.fields.lastReceivedOn',
            sortType: '-1'
        },
        2: {
            sortName: 'Name(A~Z)',
            arrowName:'productNameEn',
            sortValue: 'common.fields.productNameEn',
            sortType: '1'
        },
        3: {
            sortName: 'Name(Z~A)',
            arrowName:'productNameEn',
            sortValue: 'common.fields.productNameEn',
            sortType: '-1'
        },
        4: {
            sortName: 'Highest Price',
            arrowName:'Sneakerhead Price',
            sortValue: 'usPlatforms.P8.pPriceSaleSt,usPlatforms.P8.pPriceSaleEd',
            sortType: '-1'
        },
        5: {
            sortName: 'Lowest Price',
            arrowName:'Sneakerhead Price',
            sortValue: 'usPlatforms.P8.pPriceSaleSt,usPlatforms.P8.pPriceSaleEd',
            sortType: '1'
        },
        6: {
            sortName: 'Highest Qty',
            arrowName:'quantity',
            sortValue: 'common.fields.quantity',
            sortType: '-1'
        },
        7: {
            sortName: 'Lowest Qty',
            arrowName:'quantity',
            sortValue: 'common.fields.quantity',
            sortType: '1'
        }
    }


});