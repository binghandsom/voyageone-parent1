/**
 * @description 格林威治时间转换为当地时区时间
 */
angular.module("voyageone.angular.filter").filter("gmtDate", function ($filter) {

    return function (input,format) {

        var miliTimes;

        if (!input){
            console.warn("没有要转换的日期");
            return '';
        }

        input = typeof input === 'string' ? new Date(input) : input;

        miliTimes = input.getTime() + new Date().getTimezoneOffset() * 60 * 1000 * (-1);

        return $filter('date')(new Date(miliTimes), format);

    };

});
