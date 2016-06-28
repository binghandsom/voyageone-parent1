define(function () {
    return angular.module('vms.menu', []).component('voMenu', {
        templateUrl: 'components/menu.html',
        controller: function MenuController() {
            this.menus = [
                {name: 'Feed', items: [{name: 'Feed File Upload', url: ''}, {name: 'Feed Import Result', url: ''}, {name: 'Feed Info Search', url: ''}]},
                {name: 'Order', items: [{name: 'Order Info', url: ''}, {name: 'Order Info Sku', url: ''}]},
                {name: 'Reports', items: [{name: 'Financial Report', url: ''}]},
                {name: 'Inventory', items: [{name: 'Inventory Upload', url: ''}]},
                {name: 'Shipment', items: [{name: 'Shipment Info ', url: ''}]}
            ];
        }
    });
});