ALTER TABLE `voyageone_cms`.`cms_mt_feed_config`
CHANGE COLUMN `cfg_val2` `cfg_val2` VARCHAR(2000) NULL DEFAULT NULL;


ALTER TABLE `voyageone_cms`.`cms_zz_worktable_bcbg_superfeed`
DROP COLUMN `update_flg`;
ALTER TABLE `voyageone_cms`.`cms_zz_worktable_bcbg_superfeed_full`
ADD COLUMN `status` INT NOT NULL DEFAULT 10 COMMENT '10：插入，20：插入完成，30：更新，40：更新完成' AFTER `A073_KONWA`;
ALTER TABLE `voyageone_cms`.`cms_zz_worktable_bcbg_superfeed`
ADD COLUMN `status` INT(11) NOT NULL DEFAULT '10' COMMENT '10：插入，20：插入完成，30：更新，40：更新完成' AFTER `A073_KONWA`;


UPDATE `voyageone_cms`.`cms_mt_feed_config` SET `cfg_val2`='UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf ON b.MATNR = bf.MATNR SET b.status = 10 WHERE bf.MATNR IS NULL;' WHERE `id`='1759';
UPDATE `voyageone_cms`.`cms_mt_feed_config` SET `cfg_val2`='UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf ON b.MATNR = bf.MATNR SET b.status = 30 WHERE bf.MATNR IS NOT NULL;' WHERE `id`='1760';


INSERT INTO `voyageone_cms`.`cms_mt_feed_config` (`order_channel_id`, `cfg_name`, `cfg_val1`, `cfg_val2`, `is_attribute`, `attribute_type`, `comment`, `display_sort`) VALUES
  ('012', 'transform', 'update', 'UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf ON b.MATNR = bf.MATNR SET b.status = 10 WHERE bf.status = 10;', '0', '0', '原来表里有, 但是插入没成功的. 从新标记为插入', '20'),
  ('012', 'transform', 'delete', 'DELETE bf FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed b ON b.MATNR = bf.MATNR WHERE bf.status = 10;', '0', '0', '删除与这次新来有关系的', '30'),
  ('012', 'transform', 'insert', 'INSERT INTO voyageone_cms.cms_zz_worktable_bcbg_superfeed_full(MATNR, EAN11, BRAND_ID, MATKL, MATKL_ATT1, ZZCODE1, ZZCODE2, ZZCODE3, MEINS, BSTME, COLOR, COLOR_ATWTB, SIZE1,SIZE1_ATWTB, SIZE1_ATINN, ATBEZ, SAISO, SAISJ, SAITY, SATNR, MAKTX, WLADG, WHERL, MEAN_EAN11,A304_DATAB, A304_DATBI, A304_KBETR, A304_KONWA, A073_DATAB, A073_DATBI, A073_KBETR, A073_KONWA) SELECT MATNR, EAN11, BRAND_ID, MATKL, MATKL_ATT1, ZZCODE1, ZZCODE2, ZZCODE3, MEINS, BSTME, COLOR, COLOR_ATWTB, SIZE1, SIZE1_ATWTB, SIZE1_ATINN, ATBEZ, SAISO, SAISJ, SAITY, SATNR, MAKTX, WLADG, WHERL, MEAN_EAN11, A304_DATAB, A304_DATBI, A304_KBETR, A304_KONWA, A073_DATAB, A073_DATBI, A073_KBETR, A073_KONWA FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed WHERE status = 10;', '0', '0', '把标记好的数据统统丢进 full 里', '40'),
  ('012', 'transform', 'delete', 'DELETE sf FROM voyageone_cms.cms_zz_worktable_bcbg_styles_full sf JOIN voyageone_cms.cms_zz_worktable_bcbg_styles s ON sf.styleID = s.styleID;', '0', '0', '删除与这次新来有关系的', '50'),
  ('012', 'transform', 'insert', 'INSERT INTO voyageone_cms.cms_zz_worktable_bcbg_styles_full (styleID, productDesc, productImgURLs) SELECT styleID, productDesc, productImgURLs FROM voyageone_cms.cms_zz_worktable_bcbg_styles', '0', '0', '丢这次新来的', '60');


# 这部分的 Update 使用的 id 是正式库上的数据
# 删除所有与 table 有关的定义.全部定义在代码中,因为这部分太容易变更了
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='168';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='169';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='130';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='146';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='147';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='127';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='131';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='132';
DELETE FROM `voyageone_cms`.`cms_mt_feed_config` WHERE `id`='148';

# =================================================================== #
#   优化变更
# =================================================================== #

ALTER TABLE `voyageone_cms`.`cms_zz_worktable_bcbg_superfeed`
ADD COLUMN `feedStyleID` VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'CONCAT(SATNR, \'-\', COLOR)' AFTER `status`;

ALTER TABLE `voyageone_cms`.`cms_zz_worktable_bcbg_superfeed_full`
ADD COLUMN `feedStyleID` VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'CONCAT(SATNR, \'-\', COLOR)' AFTER `status`;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed SET feedStyleID = CONCAT(SATNR, '-', COLOR);
UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed_full SET feedStyleID = CONCAT(SATNR, '-', COLOR);

# 304 是正式服务器上的 ID
UPDATE `voyageone_cms`.`cms_mt_feed_config` SET `cfg_val2`='INSERT INTO voyageone_cms.cms_zz_worktable_bcbg_superfeed_full(MATNR, EAN11, BRAND_ID, MATKL, MATKL_ATT1, ZZCODE1, ZZCODE2, ZZCODE3, MEINS, BSTME, COLOR, COLOR_ATWTB, SIZE1,SIZE1_ATWTB, SIZE1_ATINN, ATBEZ, SAISO, SAISJ, SAITY, SATNR, MAKTX, WLADG, WHERL, MEAN_EAN11,A304_DATAB, A304_DATBI, A304_KBETR, A304_KONWA, A073_DATAB, A073_DATBI, A073_KBETR, A073_KONWA, feedStyleID) SELECT MATNR, EAN11, BRAND_ID, MATKL, MATKL_ATT1, ZZCODE1, ZZCODE2, ZZCODE3, MEINS, BSTME, COLOR, COLOR_ATWTB, SIZE1, SIZE1_ATWTB, SIZE1_ATINN, ATBEZ, SAISO, SAISJ, SAITY, SATNR, MAKTX, WLADG, WHERL, MEAN_EAN11, A304_DATAB, A304_DATBI, A304_KBETR, A304_KONWA, A073_DATAB, A073_DATBI, A073_KBETR, A073_KONWA, feedStyleID FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed WHERE status = 10;' WHERE `id`='304';

# 152 是正式服务器上的 ID
UPDATE `voyageone_cms`.`cms_mt_feed_config` SET `cfg_val1`='feedStyleID' WHERE `id`='152';
