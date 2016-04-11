package com.voyageone.task2.base.Enums;

/**
 * Created by Jack on 4/19/2015.
 */
public class TaskControlEnums {

    /**
     * 对应 tm_task_control 表中存在的所有配置名称
     */
    public enum Name {
        /**
         * 运行标志位，0为不运行，1为运行
         */
        run_flg,
        /**
         * 订单渠道
         */
        order_channel_id,
        /**
         * 渠道
         */
        cart_id,
        /**
         * 邮件分组标志位，0为分组，1为不分组
         */
        mail_group_flg,
        /**
         * 线程数
         */
        thread_count,
        /**
         * 抽出件数
         */
        row_count,
        /**
         * 不需要同步的状态
         */
        sync_ignore_status,
        /**
         * 第三方平台类目id
         */
        platform_category_id,
        /**
         * 价格披露任务的提前量配置
         */
        advance,
        /**
         * 商品图片,标识的处理模板
         */
        image_name_template,
        /**
         * 每线程的原子数
         */
        atom_count,
        /**
         * 历史数据转移表
         */
        history_table,
        /**
         * 验证身份证间隔
         */
        valid_interval,
        /**
         * 删除历史记录间隔
         */
        delete_time_interval,
        /**
         * 重试时间间隔
         */
        retry_time_interval,

        scheduled_time
    }

    /**
     * 标志位
     */
    public enum Flag {
        /**
         * 不
         */
        NO("0"),
        /**
         * 是
         */
        YES("1");

        private String is;

        Flag(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }
    }

    /**
     * 任务监控状态
     */
    public enum Status {

        /**
         * 任务启动
         */
        START("0"),

        /**
         * 任务正常结束
         */
        SUCCESS("1"),

        /**
         * 任务异常结束
         */
        ERROR("2");

        private String is;

        Status(String is) {
            this.is = is;
        }

        public String getIs() {
            return is;
        }

    }
}
