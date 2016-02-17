/**
 * 简单下载工具，通过 form 封装 post 请求下载文件
 */

(function ($) {

    if (!$) throw 'require jQery';

    function newId() {

        return "DOWN" + Math.random().toString().substr(2, 5);
    }

    function createIframe(newId) {

        return $('<iframe>').attr({
            id: newId,
            name: newId,
            style: 'display:none'
        });
    }

    function onloadHandler($frame, $form, callback, context) {

        return function () {
            // 在文档开始下载后
            // 使用给定的上下文调用回调
            if (callback) callback($frame, $form, context);
            // 删除创建的元素
            $form.remove();
            $frame.remove();
        };
    }

    $.download = {
        'post': function (url, param, callback, context) {

            var id = newId();
            var $form = $('<form>').attr({
                action: url,
                method: 'POST',
                style: 'display:none',
                target: id
            });
            var $hiddens = $.map(param, function (val, name) {
                return $('<input type="hidden">').attr({
                    id: name,
                    name: name,
                    value: val
                });
            });
            var $frame = createIframe(id).on('load', onloadHandler($frame, $form, callback, context));
            $('body').append($form.append($hiddens), $frame);
            $form.submit();
        },
        'get': function (url, param, callback, context) {

            var id = newId();
            var $frame = createIframe(id).on('load', onloadHandler($frame, null, callback, context));
            $('body').append($frame);
            $frame[0].contentWindow.href = url + '?' + $.param(param);
        }
    };

})(jQuery);
