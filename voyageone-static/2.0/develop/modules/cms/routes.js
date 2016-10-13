define(function () {
    return {
        "home": {
            "hash": "/home",
            "templateUrl": "views/home/welcome/datachart.tpl.html",
            "controllerUrl": "modules/cms/views/home/welcome/datachart.ctl"
        },
        "feed_product_list": {
            "url": "/feed/product_list",
            "hash": "/feed/product_list",
            "templateUrl": "views/search/feedsearch.tpl.html",
            "controllerUrl": "modules/cms/views/search/feedsearch.ctl"
        },
        "feed_product_list_param": {
            "url": "/feed/product_list/",
            "hash": "/feed/product_list/:type/:value",
            "templateUrl": "views/search/feedsearch.tpl.html",
            "controllerUrl": "modules/cms/views/search/feedsearch.ctl"
        },
        "promotion_list": {
            "hash": "/promotion/list",
            "templateUrl": "views/promotion/list/index.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/list/index.ctl"
        },
        "promotion_list_param": {
            "hash": "/promotion/list/:cartId",
            "templateUrl": "views/promotion/list/index.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/list/index.ctl"
        },
        "promotion_detail": {
            "hash": "/promotion/detail/:promotionId",
            "templateUrl": "views/promotion/list/detail.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/list/detail.ctl"
        },
        "promotion_task_list": {
            "hash": "/promotion/task_list",
            "templateUrl": "views/promotion/task/index.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/task/index.ctl",
            "controller": "taskIndexController as ctrl"
        },
        "promotion_task_price": {
            "url": "/promotion/task/price/",
            "hash": "/promotion/task/price/:promotionId",
            "templateUrl": "views/promotion/task/price.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/task/price.ctl"
        },
        "promotion_task_beat": {
            "url": "/promotion/task/beat/",
            "hash": "/promotion/task/beat/:task_id",
            "templateUrl": "views/promotion/task/beat.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/task/beat.ctl",
            "controller": "taskBeatController as ctrl"
        },
        "promotion_task_stock_detail": {
            "url": "/promotion/task_stock/",
            "hash": "/promotion/task_stock/:task_id",
            "templateUrl": "views/promotion/task/stock.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/task/stock.ctl",
            "controller": "taskStockController as ctrl"
        },
        "promotion_task_stock_increment": {
            "url": "/promotion/task_stock_increment/",
            "hash": "/promotion/task_stock_increment/:task_id",
            "templateUrl": "views/promotion/task/stockIncrement.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/task/stockIncrement.ctl",
            "controller": "taskStockIncrementController as ctrl"
        },
        "promotion_task_stock_increment_detail": {
            "url": "/promotion/task_stock_increment_detail/",
            "hash": "/promotion/task_stock_increment_detail/:sub_task_id",
            "templateUrl": "views/promotion/task/stockIncrementDetail.tpl.html",
            "controllerUrl": "modules/cms/views/promotion/task/stockIncrementDetail.ctl",
            "controller": "taskStockIncrementDetailController as ctrl"
        },
        "image_dictionary_list": {
            "hash": "/image/dictionary_list",
            "templateUrl": "views/mapping/dictionary/index.tpl.html",
            "controllerUrl": "modules/cms/views/mapping/dictionary/index.ctl"
        },
        "mapping_dict_item_new": {
            "hash": "/mapping/dictionary/item",
            "templateUrl": "views/mapping/dictionary/item.tpl.html",
            "controllerUrl": "modules/cms/views/mapping/dictionary/item.ctl"
        },
        "mapping_dict_item_edit": {
            "url": "/mapping/dictionary/item/",
            "hash": "/mapping/dictionary/item/:id",
            "templateUrl": "views/mapping/dictionary/item.tpl.html",
            "controllerUrl": "modules/cms/views/mapping/dictionary/item.ctl"
        },
        "image_template_list": {
            "hash": "/image/template_list",
            "templateUrl": "views/channel/imagetemplatelist/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/imagetemplatelist/index.ctl"
        },
        "image_common_list": {
            "url": "/image/common_list",
            "hash": "/image/common_list/",
            "templateUrl": "views/channel/imagegrouplist/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/imagegrouplist/index.ctl",
            "controller": "imageGroupController as ctrl"
        },
        "channel_listing_imagegrouplist_detail": {
            "url": "/channel/imagegrouplist/detail/",
            "hash": "/channel/imagegrouplist/detail/:imageGroupId",
            "templateUrl": "views/channel/imagegrouplist/detail.tpl.html",
            "controllerUrl": "modules/cms/views/channel/imagegrouplist/detail.ctl",
            "controller": "imageGroupDetailController as ctrl"
        },
        "image_size_chart_list": {
            "hash": "/image/size_chart_list",
            "templateUrl": "views/channel/sizechart/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/sizechart/index.ctl"
        },
        "channel_listing_sizechart_detail": {
            "hash": "/channel/sizechart/detail/:sizeChartId",
            "templateUrl": "views/channel/sizechart/detail.tpl.html",
            "controllerUrl": "modules/cms/views/channel/sizechart/detail.ctl"
        },
        "image_product_label_list": {
            "hash": "/image/product_label_list",
            "templateUrl": "views/channel/taglist/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/taglist/index.ctl"
        },
        "channel_brand_mapping_list": {
            "hash": "/channel/brand_mapping_list",
            "templateUrl": "views/channel/brandMapping/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/brandMapping/index.ctl",
            "controller": "BrandMappingController as ctrl"
        },
        "channel_brand_mapping_param": {
            "hash": "/channel/brand_mapping_list/:cartId",
            "templateUrl": "views/channel/brandMapping/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/brandMapping/index.ctl",
            "controller": "BrandMappingController as ctrl"
        },
        "channel_cart_category_list": {
            "hash": "/channel/cart_category_list",
            "templateUrl": "views/channel/category/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/category/index.ctl",
            "controller": "categoryController as ctrl"
        },
        "channel_default_attribute_list": {
            "hash": "/channel/default_attribute_list",
            "templateUrl": "views/channel/defaultAttribute/index.tpl.html",
            "controllerUrl": "modules/cms/views/channel/defaultAttribute/index.controller",
            "controller": "DefaultAttributeController as ctrl"
        },
        "channel_default_attribute_detail": {
            "hash": "/channel/default_attribute_detail/:upEntity?",
            "templateUrl": "views/channel/defaultAttribute/detail.tpl.html",
            "controllerUrl": "modules/cms/views/channel/defaultAttribute/detail.ctl",
            "controller": "attributeDetailController as ctrl"
        },
        "search_advance_new": {
            "hash": "/search/advanceSearch",
            "templateUrl": "views/search/advanceSearch.tpl.html",
            "controllerUrl": "modules/cms/views/search/advanceSearch.ctl"
        },
        "search_advance_param": {
            "url": "/search/advanceSearch/",
            "hash": "/search/advanceSearch/:type/:value1/:value2/:value3",
            "templateUrl": "views/search/advanceSearch.tpl.html",
            "controllerUrl": "modules/cms/views/search/advanceSearch.ctl"
        },
        "marketing_error_upload_error_list": {
            "hash": "/marketing_error/upload_error_list/:type",
            "templateUrl": "views/system/error/list.tpl.html",
            "controllerUrl": "modules/cms/views/system/error/list.ctl"
        },
        "marketing_error_upload_error_list_param": {
            "hash": "/marketing_error/upload_error_list/:type/:cartId",
            "templateUrl": "views/system/error/list.tpl.html",
            "controllerUrl": "modules/cms/views/system/error/list.ctl"
        },
        "maintain_error_import_error_list": {
            "hash": "/maintain_error/import_error_list/:type",
            "templateUrl": "views/system/error/list.tpl.html",
            "controllerUrl": "modules/cms/views/system/error/list.ctl"
        },
        "maintain_error_import_error_list_param": {
            "hash": "/maintain_error/import_error_list/:type/:cartId",
            "templateUrl": "views/system/error/list.tpl.html",
            "controllerUrl": "modules/cms/views/system/error/list.ctl"
        },
        "marketing_setting_common_custom_attribute_list": {
            "url": "/marketing_setting/common_custom_attribute_list/",
            "hash": "/marketing_setting/common_custom_attribute_list/:catPath",
            "templateUrl": "views/channel/custom/attribute.tpl.html",
            "controllerUrl": "modules/cms/views/channel/custom/attribute.ctl"
        },
        "marketing_setting_common_custom_value_list": {
            "url": "/marketing_setting/common_custom_value_list/",
            "hash": "/marketing_setting/common_custom_value_list/:catPath",
            "templateUrl": "views/channel/custom/value.tpl.html",
            "controllerUrl": "modules/cms/views/channel/custom/value.ctl"
        },
        "maintain_setting_role_channel": {
            "hash": "/maintain_setting/role_channel",
            "templateUrl": "views/system/channel/index.tpl.html",
            "controllerUrl": "modules/cms/views/system/channel/index.ctl"
        },
        "system_cart_list": {
            "hash": "/maintain_setting/role_cart",
            "templateUrl": "views/system/cart/index.tpl.html",
            "controllerUrl": "modules/cms/views/system/cart/index.ctl"
        },
        "maintain_setting_category_main_category_list": {
            "hash": "/maintain_setting/category_main_category_list",
            "templateUrl": "views/system/category/index.tpl.html",
            "controllerUrl": "modules/cms/views/system/category/index.ctl"
        },
        "system_category_edit": {
            "hash": "/system/category/:catId",
            "templateUrl": "views/system/category/edit.tpl.html",
            "controllerUrl": "modules/cms/views/system/category/edit.ctl"
        },
        "maintain_setting_current_channel_total_update": {
            "hash": "/maintain_setting/current_channel_total_update",
            "templateUrl": "views/system/storeoperation/index.tpl.html",
            "controllerUrl": "modules/cms/views/system/storeoperation/index.ctl",
            "controller": "storeOperationController as store"
        },
        "tools_common_master_brand_mapping": {
            "hash": "/tools/common/masterBrandMapping",
            "templateUrl": "views/tools/common/masterBrandMapping/index.tpl.html",
            "controllerUrl": "modules/cms/views/tools/common/masterBrandMapping/index.ctl",
            "controller": "MasterBrandMappingController as ctrl"
        },
        "tools_product_translation_setting": {
            "hash": "/tools/product/translation_setting",
            "templateUrl": "views/tools/product/property-translation.tpl.html",
            "controllerUrl": "modules/cms/views/tools/product/property-translation.ctl",
            "controller": "translationManageController as ctrl"
        },
        "tools_category_feed_to_master_mapping": {
            "hash": "/tools_category/feed_to_master_mapping",
            "templateUrl": "views/mapping/feed/list.tpl.html",
            "controllerUrl": "modules/cms/views/mapping/feed/list.ctl",
            "controller": "feedMappingController as ctrl"
        },
        "mapping_feed_prop": {
            "hash": "/mapping/feed/:params*",
            "templateUrl": "views/mapping/feed/prop.tpl.html",
            "controllerUrl": "modules/cms/views/mapping/feed/prop.ctl",
            "controller": "feedPropMappingController as ctrl"
        },
        "tools_category_master_to_platform_mapping": {
            "hash": "/tools_category/master_to_platform_mapping",
            "templateUrl": "views/mapping/platform/list.tpl.html",
            "controllerUrl": "modules/cms/views/mapping/platform/list.ctl",
            "controller": "platformMappingController as ctrl"
        },
        "mapping_platform_prop": {
            "hash": "/mapping/platform/:cartId/:mainCategoryId",
            "templateUrl": "views/mapping/platform/prop.list.tpl.html",
            "controllerUrl": "modules/cms/views/mapping/platform/prop.list.ctl",
            "controller": "platformPropMappingController as ctrl"
        },
        "dashboard": {
            "hash": "/home/welcome",
            "templateUrl": "views/home/welcome/datachart.tpl.html",
            "controllerUrl": "modules/cms/views/home/welcome/datachart.ctl"
        },
        "group_detail": {
            "hash": "/group/detail/:id",
            "templateUrl": "views/group/detail.tpl.html",
            "controllerUrl": "modules/cms/views/group/detail.ctl"
        },
        "product_detail": {
            "hash": "/product/detail/:productId",
            "templateUrl": "views/product/detail.tpl.html",
            "controllerUrl": "modules/cms/views/product/detail.ctl",
            "controller": "productDetailController as ctrl"
        },
        "product_detail_cart": {
            "hash": "/product/detail/:productId/:cartId",
            "templateUrl": "views/product/detail.tpl.html",
            "controllerUrl": "modules/cms/views/product/detail.ctl",
            "controller": "productDetailController as ctrl"
        },
        "channel_common": {
            "hash": "/channel/common",
            "templateUrl": "views/channel/common/attribute.tpl.html",
            "controllerUrl": "modules/cms/views/channel/common/attribute.ctl"
        },
        "channel_feedImportRule_feedImportRuleList": {
            "hash": "/channel/feedImportRule/feedImportRuleList",
            "templateUrl": "views/channel/feedImportRule/feedImportRuleList.tpl.html",
            "controllerUrl": "modules/cms/views/channel/feedImportRule/feedImportRuleList.ctl"
        },
        "channel_authority": {
            "hash": "/channel/authority",
            "templateUrl": "views/channel/authority/authority.tpl.html",
            "controllerUrl": "modules/cms/views/channel/authority/authority.ctl"
        },
        "channel_configuration": {
            "hash": "/channel/configuration",
            "templateUrl": "views/channel/configuration/configuration.tpl.html",
            "controllerUrl": "modules/cms/views/channel/configuration/configuration.ctl"
        },
        "tools_product": {
            "hash": "/tools/product/hscode_setting",
            "templateUrl": "views/tools/product/hsCodeList.tpl.html",
            "controllerUrl": "modules/cms/views/tools/product/hsCodeList.ctl",
            "controller": "HsCodeController as ctrl"
        },
        "system_cache_set": {
            "hash": "/system/cache/index",
            "templateUrl": "views/system/cache/index.tpl.html"
        },
        "system_mq_send": {
            "hash": "/system/mq/index",
            "templateUrl": "views/system/mq/index.tpl.html"
        },
        "system_category_setting": {
            "hash": "/system/categorysetting/:type",
            "templateUrl": "views/system/categorysetting/maincatunion.tpl.html",
            "controllerUrl": "modules/cms/views/system/categorysetting/maincatunion.ctl"
        },
        "system_category_union": {
            "hash": "/system/categorysetting_catunion",
            "templateUrl": "views/system/categorysetting/catunion.tpl.html",
            "controllerUrl": "modules/cms/views/system/categorysetting/catunion.ctl"
        },
        "system_value_channel": {
            "hash": "/system/valuechannel",
            "templateUrl": "views/system/valueChannel/index.tpl.html",
            "controllerUrl": "modules/cms/views/system/valueChannel/index.ctl"
        },
        "jm_promotion_detail_list": {
            "hash": "/jm/promotion_detail_list/:parentId",
            "templateUrl": "views/jm/jmpromotiondetaillist.tpl.html",
            "controllerUrl": "modules/cms/views/jm/jmpromotiondetaillist.ctl"
        },
        "jm_promotion_manage": {
            "hash": "/jm/promotion_manage",
            "templateUrl": "views/jm/jmpromotionmanage.tpl.html",
            "controllerUrl": "modules/cms/views/jm/jmpromotionmanage.ctl"
        },
        "jm_image_manage": {
            "hash": "/jm/image_manage",
            "templateUrl": "views/jm/jmimagemanage.tpl.html",
            "controllerUrl": "modules/cms/views/jm/jmimagemanage.ctl",
            "controller": "jmImageManageController as ctrl"
        },
        "image_create": {
            "hash": "/imagecreate/index",
            "templateUrl": "views/imagecreate/index.tpl.html",
            "controllerUrl": "modules/cms/views/imagecreate/index.ctl"
        },
        "re-price": {
            "hash": "/tools/re/price",
            "templateUrl": "views/tools/reprice/re.price.tpl.html",
            "controllerUrl": "modules/cms/views/tools/reprice/re.price.controller",
            "controller": "RePriceController as ctrl"
        },
        "black-brand": {
            "hash": "/marketing/black-brand/:params?",
            "templateUrl": "views/channel/black_brand/list.tpl.html",
            "controllerUrl": "modules/cms/views/channel/black_brand/list.controller",
            "controller": "BlackBrandListController as ctrl"
        },
        "maintain_common_master_brand_application": {
            "hash": "/maintain/common/masterBrandApplication/",
            "templateUrl": "views/maintain/common/masterBrandApplication/index.tpl.html",
            "controllerUrl": "modules/cms/views/maintain/common/masterBrandApplication/index.ctl",
            "controller": "masterBrandApplicationController as ctrl"
        }
    };
});