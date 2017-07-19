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
}];

//noinspection JSUnusedGlobalSymbols
const imgByName = ['imageDirectiveService', function imgByNameFactory(imageDirectiveService) {
    const srcBindTemplate = imageDirectiveService.imageUrlTemplate
        .replace('{channel}', '{{channel || selectedChannel}}')
        .replace('{image_name}', '{{name}}{{::s7Options?\'?\'+s7Options:\'\'}}');
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

    const hrefBindTemplate = imageDirectiveService.imageUrlTemplate
        .replace('{channel}', '{{channel || selectedChannel}}')
        .replace('{image_name}', '{{name}}?wid=500&hei=245&r={{::r}}');

    const textBindTemplate = imageDirectiveService.imageUrlTemplate
        .replace('{channel}', '{{channel || selectedChannel}}')
        .replace('{image_name}', '{{name}}?wid=500&hei=245');

    return {
        restrict: 'E',
        replace: true,
        scope: {
            name: '=',
            channel: '=',
        },
        template: `<a ng-href="${hrefBindTemplate}">${textBindTemplate}</a>`,
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
