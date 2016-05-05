/**
 * Created by 123 on 2016/4/28.
 */
define([
    'angularAMD',
    'modules/cms/controller/popup.ctl'
], function (angularAMD) {

    function tabmove(){
        //上移
        var $up = $(".up")
        $up.click(function() {
            var $tr = $(this).parents("tr");
            if ($tr.index() != 0) {
                $tr.fadeOut().fadeIn();
                $tr.prev().before($tr);

            }
        });
        //下移
        var $down = $(".down");
        var len = $down.length;
        $down.click(function() {
            var $tr = $(this).parents("tr");
            if ($tr.index() != len - 1) {
                $tr.fadeOut().fadeIn();
                $tr.next().after($tr);
            }
        });
    }
});