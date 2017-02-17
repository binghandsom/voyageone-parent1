define([
    'cms'
], function (cms) {

    return cms.directive("noticeTip", function (notify) {

        return {
            restrict: 'A',
            link: function ($scope, $element, $attr) {

                var content = $attr.noticeContent,
                    direction = $attr.noticeDirection || 'right';

                $element.on('click', function () {
                    if (!content) {
                        console.warn('属性notice-content内没有内容！');
                        return;
                    }

                    notify.noticeTip($element, content, {
                        position: direction,
                        arrowShow: true,
                        autoHide: false
                    });
                });

            }
        }
    });
});
