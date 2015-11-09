# 2015-11-09 14:13:32 从正式同步
SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON BRAND_ID = m.value AND m.channel_id = '012' AND m.master_attr = 'BRAND_ID'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAISO = m.value AND m.channel_id = '012' AND m.master_attr = 'SAISO'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAITY = m.value AND m.channel_id = '012' AND m.master_attr = 'SAITY'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON WLADG = m.value AND m.channel_id = '012' AND m.master_attr = 'WLADG'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL'
WHERE m.value IS NULL;

# 邮件:BCBG 没有 Master 数据的 SKU
DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL'
WHERE m.att_val1 = '';

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m
    ON BRAND_ID = m.value AND m.channel_id = '012' AND m.master_attr = 'BRAND_ID'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAISO = m.value AND m.channel_id = '012' AND m.master_attr = 'SAISO'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON SAITY = m.value AND m.channel_id = '012' AND m.master_attr = 'SAITY'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON WLADG = m.value AND m.channel_id = '012' AND m.master_attr = 'WLADG'
WHERE m.value IS NULL;

DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  LEFT JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL'
WHERE m.value IS NULL;

SELECT MATNR
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL' AND m.label = 'DO NOT USE';

# 邮件:BCBG 类目为 "DO NOT USE" 的 SKU
DELETE voyageone_cms.cms_zz_worktable_bcbg_superfeed FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m
    ON MATKL = m.value AND m.channel_id = '012' AND m.master_attr = 'MATKL' AND m.label = 'DO NOT USE';

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m ON BRAND_ID = m.value AND m.master_attr = 'BRAND_ID' AND m.channel_id = '012'
SET BRAND_ID = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m ON SAISO = m.value AND m.master_attr = 'SAISO' AND m.channel_id = '012'
SET SAISO = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m ON SAITY = m.value AND m.master_attr = 'SAITY' AND m.channel_id = '012'
SET SAITY = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m ON WLADG = m.value AND m.master_attr = 'WLADG' AND m.channel_id = '012'
SET WLADG = label;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed
  JOIN voyageone_cms.cms_mt_feed_master m ON MATKL = m.value AND m.master_attr = 'MATKL' AND m.channel_id = '012'
SET MATKL = label, MATKL_ATT1 = att_val1;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.status = 10
WHERE bf.MATNR IS NULL;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.status = 30
WHERE bf.MATNR IS NOT NULL;

UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.status = 10
WHERE bf.status = 10;

DELETE bf FROM
  voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed b
    ON b.MATNR = bf.MATNR
WHERE bf.status = 10;

INSERT INTO voyageone_cms.cms_zz_worktable_bcbg_superfeed_full
(MATNR, EAN11, BRAND_ID, MATKL, MATKL_ATT1, ZZCODE1, ZZCODE2, ZZCODE3, MEINS, BSTME, COLOR, COLOR_ATWTB, SIZE1, SIZE1_ATWTB,
 SIZE1_ATINN, ATBEZ, SAISO, SAISJ, SAITY, SATNR, MAKTX, WLADG, WHERL, MEAN_EAN11, A304_DATAB, A304_DATBI, A304_KBETR,
 A304_KONWA, A073_DATAB, A073_DATBI, A073_KBETR, A073_KONWA)
  SELECT
    MATNR,
    EAN11,
    BRAND_ID,
    MATKL,
    MATKL_ATT1,
    ZZCODE1,
    ZZCODE2,
    ZZCODE3,
    MEINS,
    BSTME,
    COLOR,
    COLOR_ATWTB,
    SIZE1,
    SIZE1_ATWTB,
    SIZE1_ATINN,
    ATBEZ,
    SAISO,
    SAISJ,
    SAITY,
    SATNR,
    MAKTX,
    WLADG,
    WHERL,
    MEAN_EAN11,
    A304_DATAB,
    A304_DATBI,
    A304_KBETR,
    A304_KONWA,
    A073_DATAB,
    A073_DATBI,
    A073_KBETR,
    A073_KONWA
  FROM
    voyageone_cms.cms_zz_worktable_bcbg_superfeed
  WHERE
    status = 10;

DELETE sf FROM voyageone_cms.cms_zz_worktable_bcbg_styles_full sf JOIN voyageone_cms.cms_zz_worktable_bcbg_styles s
    ON sf.styleID = s.styleID;

INSERT INTO voyageone_cms.cms_zz_worktable_bcbg_styles_full (styleID, productDesc, productImgURLs)
  SELECT
    styleID,
    productDesc,
    productImgURLs
  FROM
    voyageone_cms.cms_zz_worktable_bcbg_styles;