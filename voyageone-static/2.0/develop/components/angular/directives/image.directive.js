/*
 * @Description: 用于替换成cms2中可显示的图片url
 *
 * @User: linanbin
 * @Version: 2.0.0, 16/5/12
 */

// noinspection JSUnusedGlobalSymbols
const imageDirectiveService = ['$localStorage', class ImageDirectiveService {
    constructor($localStorage) {
        this.imageUrlTemplate = 'http://localhost:2080/{channel}/is/image/sneakerhead/{image_name}';
        this.userInfo = $localStorage.user;
        this.selectedChannel = this.userInfo.channel
    }

    getImageUrlByName(name, channel = this.selectedChannel) {
        return this.imageUrlTemplate
            .replace('{channel}', channel)
            .replace('{image_name}', name);
    }
}];

//noinspection JSUnusedGlobalSymbols
const imgByName = ['imageDirectiveService', function imgByNameFactory(imageDirectiveService) {

    const srcBindTemplate = imageDirectiveService.getImageUrlByName(
        '{{name}}{{::s7Options?\'?\'+s7Options:\'\'}}',
        '{{channel || selectedChannel}}');

    return {
        restrict: 'E',
        replace: true,
        template: `<img data-by="img-by-name" ng-src="${srcBindTemplate}">`,
        scope: {
            name: '=',
            channel: '=',
            s7Options: '@'
        },
        link: function ($scope) {
            $scope.selectedChannel = imageDirectiveService.selectedChannel;
        }
    }
}];

//noinspection JSUnusedGlobalSymbols
const aProductImg = ['imageDirectiveService', function aProductImgFactory(imageDirectiveService) {

    const hrefBindTemplate = imageDirectiveService.getImageUrlByName(
        '{{name}}?{{::r}}&{{::s7Options}}',
        '{{channel || selectedChannel}}');

    const textBindTemplate = imageDirectiveService.getImageUrlByName(
        '{{name}}?{{::s7Options}}',
        '{{channel || selectedChannel}}');

    return {
        restrict: 'E',
        replace: true,
        transclude: true,
        scope: {
            name: '=',
            channel: '=',
            s7Options: '@'
        },
        template: `<a ng-href="${hrefBindTemplate}"><ng-transclude>${textBindTemplate}</ng-transclude></a>`,
        link: function ($scope) {
            $scope.r = Math.random();
            $scope.selectedChannel = imageDirectiveService.selectedChannel;
        }
    }
}];

angular.module('voyageone.angular.directives')
    .service('imageDirectiveService', imageDirectiveService)
    .directive('imgByName', imgByName)
    .directive('aProductImg', aProductImg);
