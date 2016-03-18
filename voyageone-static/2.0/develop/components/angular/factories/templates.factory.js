(function () {

/**
 * 同意管理模板代码, 例如 directive 中 schema 使用的模板
 * 发布时, 该文件内容会被替换
 */


    function Template(key) {
        this.url = '/components/angular/factories/templates/' + key.replace('.', '/') + '.html';
    }

    Template.prototype = {
        getHtml: function () {
            var html = this.$cache.get(this.url);
            if (html) return this.$q(function (resolve) {
                resolve(html);
            });
            return this.$req(this.url);
        }
    };

    angular.module('voyageone.angular.factories.templates', [])
        .run(function ($templateCache, $templateRequest, $q) {
            Template.prototype.$cache = $templateCache;
            Template.prototype.$req = $templateRequest;
            Template.prototype.$q = $q;
        })
        .constant('templates', {
            schema: {
                header: new Template('schema.header'),
                label: new Template('schema.label'),
                input: new Template('schema.input'),
                date: new Template('schema.date'),
                datetime: new Template('schema.datetime'),
                textarea: new Template('schema.textarea'),
                select: new Template('schema.select'),
                radio: new Template('schema.radio'),
                checkbox: new Template('schema.checkbox'),
                multiComplex: new Template('schema.multicomplex'),
                complex: new Template('schema.complex'),
                multi_in_complex: new Template('schema.multiincomplex'),
                multiComplex_tip: new Template('schema.multicomplextip')
            }
        });
})();