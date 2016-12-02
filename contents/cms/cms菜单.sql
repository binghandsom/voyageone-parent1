
-- DELETE FROM com_resource  WHERE id>8000;

-- feed管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8001,'cms','feed管理','cms_FEED_MANAGER',1,'','glyphicon glyphicon-star icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
 INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
 VALUES (800101,'cms','Feed商品一览','cms_FEED_PRODUCT_LIST',1,'#/feed/product_list','fa fa-reorder icon text-vo',8001,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
 VALUES (800102,'cms','feed商品分类','cms_FEED_PRODUCT_CATEGORY',1,'','fa fa-reorder icon text-vo',8001,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);



-- 商品管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8002,'cms','商品管理','cms_product_MANAGER',1,'','glyphicon glyphicon-th-list icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

-- 活动管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8003,'cms','活动管理','cms_PROMOTION_MANAGER',1,'','glyphicon glyphicon-star icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

	INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
	VALUES (800301,'cms','活动一览','cms_PROMOTION_LIST',1,'#/promotion/list','fa fa-plus icon-small text-vo',8003,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
		INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
		VALUES (80030101,'cms','活动总揽','cms_PROMOTION_LIST_ALL',1,'','fa fa-minus icon text-vo',800301,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800302,'cms','任务一览','cms_PROMOTION_TASK_LIST',1,'#/promotion/task_list','fa fa-minus icon text-vo',8003,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

-- 素材管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8004,'cms','素材管理','cms_IMAGE_MANAGER',1,'','fa fa-database icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800401,'cms','详情页模板一览','cms_IMAGE_DICTIONARY_LIST',1,'#/image/dictionary_list','fa fa-minus icon text-vo',8004,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800402,'cms','商品图片模板一览','cms_IMAGE_TEMPLATE_LIST',1,'#/image/template_list','fa fa-minus icon text-vo',8004,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800403,'cms','共通图片一览','cms_IMAGE_COMMON_LIST',1,'#/image/common_list','fa fa-minus icon text-vo',8004,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800404,'cms','尺码对照表一览','cms_IMAGE_SIZE_CHART_LIST',1,'#/image/size_chart_list','fa fa-minus icon text-vo',8004,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800405,'cms','自定义标签一览','cms_IMAGE_PRODUCT_LABEL_LIST',1,'#/image/product_label_list','fa fa-minus icon text-vo',8004,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

-- 店铺管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8005,'cms','店铺管理','cms_CHANNEL_MANAGER',1,'','fa fa-bank icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800501,'cms','店铺内分类一览','cms_CHANNEL_CART_CATEGORY_LIST',1,'#/channel/cart_category_list','fa fa-minus icon text-vo',8005,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800502,'cms','平台品牌设置一览','cms_BRAND_MAPPING_LIST',1,'#/channel/brand_mapping_list','fa fa-minus icon text-vo',8005,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800503,'cms','平台默认属性设置一览','cms_DEFAULT_MAPPING_LIST',1,'#/channel/default_attribute_list','fa fa-minus icon text-vo',8005,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800504,'cms','货架一览','cms_shelves_MAPPING_LIST',1,'#/shelves/management','fa fa-minus icon text-vo',8005,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);


-- 高级搜索
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8006,'cms','高级检索','cms_ADVANCE_SEARCH',1,'','glyphicon glyphicon-search icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800601,'cms','高级检索','cms_ADVANCE_SEARCH_a',1,'#/search/advanceSearch','fa fa-minus icon text-vo',8006,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);


-- 运营错误管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8007,'cms','运营错误管理','cms_MARKETING_ERROR_MANAGER',1,'','fa fa-cogs icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800701,'cms','上新错误总览','cms_MARKETING_ERROR_LIST_ALL',1,'#/marketing_error/upload_error_list/1','fa fa-minus icon text-vo',8007,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

-- 维护错误管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8008,'cms','维护错误管理','cms_MAINTAIN_ERROR_MANAGER',1,'','fa fa-cogs icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800801,'cms','导入错误总览','cms_MAINTAIN_ERROR_LIST_ALL',1,'#/maintain_error/import_error_list/2','fa fa-minus icon text-vo',8008,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

-- 运营设置管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8009,'cms','运营设置管理','cms_MARKETING_SETTING_MANAGER',1,'','fa fa-gear icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (800901,'cms','共通设置','cms_MARKETING_SETTING_COMMON',1,'','fa fa-plus icon-small text-vo',8009,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

	INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
	VALUES (80090101,'cms','黑名单一览','cms_MARKETING_SETTING_COMMON_BLACK_LIST',1,'#/marketing/black-brand','fa fa-minus icon text-vo',800901,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
        INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
	VALUES (80090102,'cms','自定义属性一览','TXT_MARKETING_SETTING_COMMON_CUSTOM_ATTRIBUTE_LIST',1,'#/marketing_setting/common_custom_attribute_list/0','fa fa-minus icon text-vo',800901,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

-- 维护设置管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8010,'cms','维护设置管理','cms_MAINTAIN_SETTING_MANAGER',1,'','fa fa-gear icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (801001,'cms','当前店铺设置','cms_CURRENT_CHANNEL_SETTING',1,'','fa fa-plus icon-small text-vo',8010,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

	INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
	VALUES (80100101,'cms','货架模板一览','cms_SHELVES_TEMPLATE_MANAGEMENT',1,'#/shelves/template/management/','fa fa-minus icon text-vo',801001,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
 
 -- 工具管理
INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (8011,'cms','工具管理','cms_TOOLS_MANAGER',1,'','fa fa-briefcase icon text-vo',519,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
VALUES (801101,'cms','商品处理','cms_TOOLS_PRODUCT',1,'','fa fa-plus icon-small text-vo',8011,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);

	INSERT  INTO `com_resource`(`id`,`application`,`res_name`,`res_key`,`res_type`,`res_url`,`icon`,`parent_id`,`parent_ids`,`weight`,`active`,`description`,`created`,`creater`,`modified`,`modifier`,`origin_id`,`origin_table`,`origin_name`,`show_in_menu`,`menu_title`) 
	VALUES (80110101,'cms','商品属性翻译','cms_TOOLS_PRODUCT_TRANSLATION_SETTING',1,'#/tools/product/translation_setting','fa fa-minus icon text-vo',801101,'0',1,1,'ADMIN','2000-01-01 00:00:00','SYSTEM','2016-10-14 11:41:05','SYSTEM',NULL,NULL,NULL,NULL,NULL);
           
-- select * from com_resource;

SELECT COUNT(*) FROM com_resource WHERE id >8000;