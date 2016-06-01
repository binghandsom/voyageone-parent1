/**
 * @Description:
 * 可以在textarea当中使用tab键，
 * @example: <textarea class="form-control no-resize" rows="10" placeholder="请输入导入文字" tab-in-textarea></textarea>
 * @User:    tony-piao
 * @Version: 0.1.0, 2016-5-5
 */
angular.module("voyageone.angular.directives.tabInTextarea", []).directive("tabInTextarea", function () {
    return {
        restrict: "A",
        link: function (scope, element) {
            $(element).keydown(function (e) {
                if (e.keyCode === 9) { // tab was pressed
                    // get caret position/selection
                    var start = this.selectionStart;
                    var end = this.selectionEnd;

                    var $this = $(this);
                    var value = $this.val();

                    // set textarea value to: text before caret + tab + text after caret
                    $this.val(value.substring(0, start)
                        + "\t"
                        + value.substring(end));

                    // put caret at right position again (add one for the tab)
                    this.selectionStart = this.selectionEnd = start + 1;

                    // prevent the focus lose
                    e.preventDefault();
                }
            });
        }
    };
});
