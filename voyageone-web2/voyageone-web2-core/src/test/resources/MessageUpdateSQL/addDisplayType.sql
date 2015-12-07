ALTER TABLE `Synship`.`ct_message_info` 
ADD COLUMN `displayType` INT NOT NULL DEFAULT 1 COMMENT '/**\n * 弹出提示框\n */\nALERT(1),\n/**\n * 顶端弹出自动关闭\n */\nNOTIFY(2),\n/**\n * 右下弹出自动关闭\n */\nPOP(3),\n/**\n * 用户自定义处理\n */\nCUSTOM(4);' AFTER `type`;
COMMIT;