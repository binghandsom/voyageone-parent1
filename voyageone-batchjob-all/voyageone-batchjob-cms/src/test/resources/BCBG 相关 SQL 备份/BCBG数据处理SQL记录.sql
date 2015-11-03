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
SET MATKL = label;

# 转换完 master 之后,看看是不是都转了
SELECT *
FROM voyageone_cms.cms_zz_worktable_bcbg_superfeed;

# 标记数据
UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b
  LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.update_flg = 1
WHERE bf.MATNR IS NULL;



UPDATE voyageone_cms.cms_zz_worktable_bcbg_superfeed b
  LEFT JOIN voyageone_cms.cms_zz_worktable_bcbg_superfeed_full bf
    ON b.MATNR = bf.MATNR
SET b.update_flg = 2
WHERE bf.MATNR IS NOT NULL;