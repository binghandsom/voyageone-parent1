/**
 * 简单下载工具，通过 form 封装 post 请求下载文件
 */

(function ($) {

    if (!$) {
        console.warn('jQuery is not defined');
        return;
    }

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
            var $frame = createIframe(id);
            $('body').append($form.append($hiddens), $frame);
            $frame.on('load', function () {
                var responseContent = $frame.contents().find('body').text();
                if (callback) callback(responseContent, param, context);
                $form.remove();
                $frame.remove();
            });
            $form.submit();
        },
        'get': function (url, param, callback, context) {

            var id = newId();
            var $frame = createIframe(id);
            $('body').append($frame);
            $frame.on('load', function () {
                var responseContent = $frame.contents().find('body').text();
                if (callback) callback(responseContent, param, context);
                $frame.remove();
            });
            $frame[0].contentWindow.href = url + '?' + $.param(param);
        }
    };

})(window['jQuery']);
