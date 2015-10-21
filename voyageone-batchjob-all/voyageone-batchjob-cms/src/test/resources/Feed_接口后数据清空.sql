DELETE FROM voyageone_cms.cms_mt_feed_config
WHERE order_channel_id = '012';
DELETE FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed_full;

DELETE FROM voyageone_cms.cms_work_product_feed_category WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_work_product_feed_model WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_work_product_feed_product WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_work_product_feed_item WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_work_product_image_url WHERE channel_id = '012';
DELETE FROM Synship.wms_bt_item_details WHERE order_channel_id = '012';

TRUNCATE voyageone_cms.cms_sequence_category_id;
TRUNCATE voyageone_cms.cms_sequence_model_id;
TRUNCATE voyageone_cms.cms_sequence_product_id;

ALTER TABLE `voyageone_cms`.`cms_sequence_category_id`
AUTO_INCREMENT = 501;

DELETE FROM voyageone_cms.cms_bt_category WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_category_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_amazon_category_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_pricegrabber_category_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_category WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_tm_category_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_jd_category_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_category_price_setting WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_relation_category WHERE channel_id = '012';

DELETE FROM voyageone_cms.cms_bt_model WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_model_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_amazon_model_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_google_model_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_pricegrabber_model_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_model WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_model_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_tm_model_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_jd_model_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_model_price_setting WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_relation_category_model WHERE channel_id = '012';


DELETE FROM voyageone_cms.cms_bt_product WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_product_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_amazon_product_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_google_product_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_pricegrabber_product_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_product WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_product_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_tm_product_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_jd_product_extend WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_product_image WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_product_price_setting WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_product_share WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_cn_product_share WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_relation_model_product WHERE channel_id = '012';
DELETE FROM voyageone_cms.cms_bt_relation_category_product WHERE channel_id = '012';

DELETE FROM voyageone_cms.cms_mt_product_attribute WHERE channel_id = '012';
