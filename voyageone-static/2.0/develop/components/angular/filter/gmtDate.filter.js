/**
 * @description 格林威治时间转换为当地时区时间
 */
angular.module("voyageone.angular.filter").filter("gmtDate", function ($filter) {

    return function (input, format) {

        var miliTimes;

        if (!input) {
            console.warn("没有要转换的日期");
            return '';
        }

        switch (typeof input) {
            case 'string':
                input = new Date(input);
                miliTimes = input.getTime() + new Date().getTimezoneOffset() * 60 * 1000 * (-1);
                break;
            case 'number':
                miliTimes = new Date(input);
                break;
            default:
                console.error("传入了未知类型数据！！！");
        }

        return $filter('date')(new Date(miliTimes), format);

    };

});
