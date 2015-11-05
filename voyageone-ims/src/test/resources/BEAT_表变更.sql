ALTER TABLE `voyageone_ims`.`ims_bt_beat`
ADD COLUMN `targets` VARCHAR(9) NOT NULL
COMMENT '目标图片的位置,以逗号分隔,最大为5.如 1,2,3 代表更新第一张第二张和第三张'
AFTER `template_url`;
