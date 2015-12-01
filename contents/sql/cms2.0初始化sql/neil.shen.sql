--batch任务配置
INSERT INTO `com_mt_task` (`task_id`, `task_type`, `task_name`, `task_comment`, `task_freq`) VALUES ((select max(task_id)+1 from com_mt_task) task_id, 'cms', 'wmsSynInventoryToCmsJob', '同步mongodb库存任务', '');
INSERT INTO `tm_task_control` (`task_id`, `cfg_name`, `cfg_val1`, `cfg_val2`, `end_time`, `comment`) VALUES ('wmsSynInventoryToCmsJob', 'order_channel_id', '006', '', NULL, '允许运行的渠道');
INSERT INTO `tm_task_control` (`task_id`, `cfg_name`, `cfg_val1`, `cfg_val2`, `end_time`, `comment`) VALUES ('wmsSynInventoryToCmsJob', 'run_flg', '1', '', NULL, '同步mongodb库存任务');
INSERT INTO `tm_task_control` (`task_id`, `cfg_name`, `cfg_val1`, `cfg_val2`, `end_time`, `comment`) VALUES ('wmsSynInventoryToCmsJob', 'thread_count', '1', '', NULL, '线程数');
