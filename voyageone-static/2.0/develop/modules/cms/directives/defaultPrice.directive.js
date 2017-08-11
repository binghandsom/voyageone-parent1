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
            template:'<div><script type="text/ng-template"id="priceTemplate.html"><div class="panel panel-default"><div class="panel-heading"style="background: #71d0ee;">{{ctrl.dynamicPopover.title}}</div><div class="panel-body"><input type="number"scale="11,2"class="form-control"ng-enter="ctrl.delivery()"ng-model="ctrl.price"></div></div></script><button uib-popover-template="ctrl.dynamicPopover.templateUrl"popover-is-open="ctrl.isOpen"type="button"class="btn btn-default btn-pop"title="{{ctrl.$scope.data.common.fields.originalTitleCn}}"ng-transclude></button></div>'
        }
    });

});
