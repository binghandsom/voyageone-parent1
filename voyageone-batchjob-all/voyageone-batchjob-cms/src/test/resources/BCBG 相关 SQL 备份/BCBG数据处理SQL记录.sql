# 先把 master 无效数据查询出来.汇总用来发送警告邮件
SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON BRAND_ID = m.value AND m.channel_id = '012' AND m.master_attr = 'BRAND_ID'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON SAISO = m.value AND m.channel_id = '012' AND m.master_attr = 'SAISO'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON SAITY = m.value AND m.channel_id = '012' AND m.master_attr = 'SAITY'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON WLADG = m.value AND m.channel_id = '012' AND m.master_attr = 'WLADG'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL'
WHERE m.value IS NULL;

# 开始删除关联不到 master 数据的内容
DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON BRAND_ID = m.value AND m.channel_id = '012' AND m.master_attr = 'BRAND_ID'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON SAISO = m.value AND m.channel_id = '012' AND m.master_attr = 'SAISO'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON SAITY = m.value AND m.channel_id = '012' AND m.master_attr = 'SAITY'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON WLADG = m.value AND m.channel_id = '012' AND m.master_attr = 'WLADG'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL'
WHERE m.att_val1 = '';

# 删除 DO NOT USE 类目的数据
DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL' AND m.label = 'DO NOT USE';

# 看看现在有多少行数据
SELECT count(1)
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed;

# 下边开始转换各种 master 数据
UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON BRAND_ID = m.value AND m.master_attr = 'BRAND_ID' AND m.channel_id = '012'
SET BRAND_ID = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON SAISO = m.value AND m.master_attr = 'SAISO' AND m.channel_id = '012'
SET SAISO = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON SAITY = m.value AND m.master_attr = 'SAITY' AND m.channel_id = '012'
SET SAITY = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON WLADG = m.value AND m.master_attr = 'WLADG' AND m.channel_id = '012'
SET WLADG = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON MATKL = m.value AND m.master_attr = 'MATKL' AND m.channel_id = '012'
SET MATKL = label, MATKL_ATT1 = att_val1;

# 转换完 master 之后,看看是不是都转了
SELECT *
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed;

# 原来表里没有的, 说明是新来的. 标记为插入
UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b
  LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.status = 10
WHERE bf.MATNR IS NULL;
# 原来表里有的, 暂时全部标记为更新.
UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b
  LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.status = 30
WHERE bf.MATNR IS NOT NULL;
# 原来表里有, 但是插入没成功的. 从新标记为插入
UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b
  LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.status = 10
WHERE bf.status = 10;

# 删除即将从新插入的(上次没成功,这次又来了的
DELETE bf FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
  JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed b ON b.MATNR = bf.MATNR
WHERE bf.status = 10;
# 把即将进行插入的数据, 插入到 full 表
INSERT INTO voyageone_cms.cms_zz_worktable_bcbg_superfeed_full
(MATNR, EAN11, BRAND_ID, MATKL, MATKL_ATT1, ZZCODE1, ZZCODE2, ZZCODE3, MEINS, BSTME, COLOR, COLOR_ATWTB, SIZE1,
 SIZE1_ATWTB, SIZE1_ATINN, ATBEZ, SAISO, SAISJ, SAITY, SATNR, MAKTX, WLADG, WHERL, MEAN_EAN11,
 A304_DATAB, A304_DATBI, A304_KBETR, A304_KONWA, A073_DATAB, A073_DATBI, A073_KBETR, A073_KONWA)
SELECT
  MATNR, EAN11, BRAND_ID, MATKL, MATKL_ATT1, ZZCODE1, ZZCODE2, ZZCODE3, MEINS, BSTME, COLOR, COLOR_ATWTB, SIZE1,
  SIZE1_ATWTB, SIZE1_ATINN, ATBEZ, SAISO, SAISJ, SAITY, SATNR, MAKTX, WLADG, WHERL, MEAN_EAN11,
  A304_DATAB, A304_DATBI, A304_KBETR, A304_KONWA, A073_DATAB, A073_DATBI, A073_KBETR, A073_KONWA
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
WHERE status = 10;

# 删除与这次新来有关系的
DELETE sf FROM voyageone_cms.cms_zz_worktable_bcbg_styles_full sf
  JOIN voyageone_cms.cms_zz_worktable_bcbg_styles s ON sf.styleID = s.styleID;

# 丢这次新来的
INSERT INTO voyageone_cms.cms_zz_worktable_bcbg_styles_full
(styleID, productDesc, productImgURLs)
SELECT
  styleID, productDesc, productImgURLs
FROM voyageone_cms.cms_zz_worktable_bcbg_styles