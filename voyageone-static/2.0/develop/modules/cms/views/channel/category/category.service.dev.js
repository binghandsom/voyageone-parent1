define([
    'cms',
    'json!./categorys.dev.json'
], function (cms, categories) {
    cms.service('categoryService', (function(){

        function CategoryService($q) {
            this.$q = $q;
        }

        CategoryService.prototype = {
            getCategories: function () {
                return this.$q(function(resolve) {
                    resolve(categories);
                });
            }
        };

        return CategoryService;

    })());
});