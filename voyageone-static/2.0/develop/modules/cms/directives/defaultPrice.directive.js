/**
 * @description popover出默认价格设置
 */
define([
    'cms'
], function (cms) {

    cms.directive('defaultPrice', function () {

        class DefaultPriceController {

            constructor($scope, $attrs, $element, notify,$parse) {
                this.$scope = $scope;
                this.$attrs = $attrs;
                this.$element = $element;
                this.notify = notify;
                this.$parse = $parse;
                this.isOpen = true;
                this.dynamicPopover = {
                    title: 'Defaults',
                    templateUrl: 'priceTemplate.html'
                };
            }

            delivery(){
                let self = this,
                    paramStr = angular.toJson({
                        type:self.$scope.type,
                        price:self.price
                    });

                let exp = self.$parse(`${self.$scope.func}(${paramStr})`);

                // param{type,price}
                exp(self.$scope.$parent);

            }

        }

        return {
            restrict: 'E',
            transclude: true,
            scope: {
                func: "@func",
                type:"@type"
            },
            controller: ['$scope', '$attrs', '$element', 'notify', '$parse',DefaultPriceController],
            controllerAs: 'ctrl',
            templateUrl:"directives/defaultPrice.directive.html"
        }
    });

});
