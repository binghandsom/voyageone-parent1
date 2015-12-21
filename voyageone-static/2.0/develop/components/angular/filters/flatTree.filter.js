/**
 * 将传入的树形转为扁平结构
 * @User: Jonas
 * @Date: 2015-12-20 16:49:21
 * @Version: 2.0.0
 */

angular.module('voyageone.angular.filters.flatTree', [])
  .filter('flatTree', function () {

    return function (rows, arg1) {

      var flatten = [];
      angular.forEach(rows, function (row) {

        flatten.push(row);

        var children = row[arg1];
        if (children && children.length) {
          angular.forEach(children, function (child) {
            flatten.push(child);
          });
        }
      });
      return flatten;
    }
  });