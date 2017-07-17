INSERT INTO synship.tm_code (id, code, name, name1, des, active, created, creater, modified, modifier) VALUES
('IS_MID_FTP', 'Domain', 'image.voyageone.com.cn', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'Port', '22', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'UserName', 'voyageone_cms_bulk', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'Password', '68%#2kLS2F@r', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IS_MID_FTP', 'Encoding', 'utf-8', '', 'Image Server 的 Ftp 设置', 1, now(), 'system', now(), 'system'),
('IMAGE_SERVER', 'domain', 'image.voyageone.com.cn', '', 'Image Server 的域名配置', 1, now(), 'system', now(), 'system'),
('IMAGE_SERVER', 'maintainer', 'jonas.gao@voyageone.cn', '', 'Image Server 维护人', 1, now(), 'system', now(), 'system');

SELECT * FROM synship.tm_code WHERE id = 'IS_MID_FTP';

-- 为每个渠道创建路径配置
SELECT
  order_channel_id,
  'image_server_bulk_path',
  concat('/vdb/imgunzip/', order_channel_id, '/'),
  '',
  ''
FROM tm_order_channel;