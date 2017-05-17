/**
 * 编辑产品originalTitleCn
 * @param productInfo 产品信息
 * @author piao
 */
angular.module("voyageone.angular.directives").directive("editTitle", function () {

    function EditTitleController($scope, $attrs, $element, notify, $translate, productDetailService) {
        this.$attrs = $attrs;
        this.$scope = $scope;
        this.$element = $element;
        this.notify = notify;
        this.$translate = $translate;
        this.productDetailService = productDetailService;
    }

    EditTitleController.prototype.init = function () {
        var self = this;

        self.productInfo = self.$scope.data;

        self.dynamicPopover = {
            title: '产品名称 ',
            templateUrl: 'myPopoverTemplate.html'
        };
    };

    EditTitleController.prototype.save = function () {
        var self = this,
            productInfo = self.productInfo;

        var prodId = productInfo.prodId,
            originalTitleCn = productInfo.common.fields.originalTitleCn.replace(/[.\n]/g, '');

        if (prodId && originalTitleCn) {
            self.productDetailService.updateOriginalTitleCn({
                prodId: prodId,
                originalTitleCn: originalTitleCn
            }).then(function () {
                self.isOpen = false;
                productInfo.common.fields.originalTitleCn = originalTitleCn;
                self.notify.success(self.$translate.instant('TXT_MSG_UPDATE_SUCCESS'));
            });
        }

    };

    return {
        restrict: 'E',
        scope: {
            data: "=data"
        },
        controller: ['$scope', '$attrs', '$element', 'notify', '$translate', 'productDetailService', EditTitleController],
        controllerAs: 'ctrl',
        template: '<div ng-init="ctrl.init()">'
        + '<script type="text/ng-template" id="myPopoverTemplate.html">'
        + '<div class="form-group">'
        + '<textarea class="form-control no-resize" style="min-height: 70px;width: 200px" ng-model="ctrl.productInfo.common.fields.originalTitleCn"></textarea>'
        + '</div>'
        + '<div class="form-group pull-right">'
        + '<button class="btn btn-success" ng-click="ctrl.save()"><i class="fa fa-save"></i></button>'
        + '</div>'
        + '</script>'
        + '<button uib-popover-template="ctrl.dynamicPopover.templateUrl" popover-title="{{ctrl.dynamicPopover.title}}" popover-is-open="ctrl.isOpen" type="button" class="btn btn-default" title="{{ctrl.productInfo.common.fields.originalTitleCn}}" ng-if="ctrl.productInfo.common.fields.originalTitleCn">{{ctrl.productInfo.common.fields.originalTitleCn  | limitTo: 25}}</button>'
        + '</div>'

    }
});
