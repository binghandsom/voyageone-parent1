CREATE TABLE voyageone_cms.cms_mt_feed_master
(
  id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  channel_id VARCHAR(3) NOT NULL COMMENT '渠道',
  master_attr VARCHAR(50) NOT NULL COMMENT '属性名称',
  value VARCHAR(200) NOT NULL COMMENT '属性值',
  label VARCHAR(200) NOT NULL COMMENT '属性显示值',
  comment VARCHAR(200) NOT NULL COMMENT '描述/备注'
);
