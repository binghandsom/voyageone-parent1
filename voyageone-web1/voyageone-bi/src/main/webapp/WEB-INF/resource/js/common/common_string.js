// 字符串共通组件
String.prototype.trim = function() {
    // 删除左右两端的空格
    return this.replace(/(^\s*)|(\s*$)/g, "");
};

String.prototype.ltrim = function() {
    // 删除左边的空格
    return this.replace(/(^\s*)/g, "");
};

String.prototype.rtrim = function() {
    // 删除右边的空格
    return this.replace(/(\s*$)/g, "");
};
