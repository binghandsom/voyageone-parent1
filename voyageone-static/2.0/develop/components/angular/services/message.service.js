(function() {
    /**
     * 对后台的信息进行自动处理
     * @Date:    2015-11-19 14:47:23
     * @User:    Jonas
     * @Version: 0.2.0
     */
    angular.module("voyageone.angular.services.message", []).service("messageService", MessageService);
    // 同步,DisplayType 枚举
    var DISPLAY_TYPES = {
        /**
         * 弹出提示
         */
        ALERT: 1,
        /**
         * 顶端弹出自动关闭
         */
        NOTIFY: 2,
        /**
         * 右下弹出自动关闭
         */
        POP: 3,
        /**
         * 用户自定义处理
         */
        CUSTOM: 4
    };
    function MessageService(alert, confirm, notify) {
        this.alert = alert;
        this.confirm = confirm;
        this.notify = notify;
    }
    MessageService.prototype = {
        /**
         * 根据类型自动显示信息
         * @param {{displayType:Number, message:String}} res
         */
        show: function(res) {
            var displayType = res.displayType;
            var message = res.message;
            switch (displayType) {
              case DISPLAY_TYPES.ALERT:
                this.alert(message);
                break;

              case DISPLAY_TYPES.NOTIFY:
                this.notify(message);
                break;

              case DISPLAY_TYPES.POP:
                this.notify({
                    message: message,
                    position: "right bottom"
                });
                break;
            }
        }
    };
})();