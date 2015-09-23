package com.voyageone.ims;

/**
 * 管理所有 IMS 集成环境系统使用的 Message ( ct_message_info ) 的 Code
 * <p>
 * Created by Jonas on 15/6/30.
 */
public interface ImsMsgConstants {

    // 各功能模块使用内部类分别细分管理

    interface BeatMsg {

        String NO_UPLOAD_FILE = "3000036";

        /**
         * 提交的价格表没有系统信息
         */
        String EXCEL_NO_BEAT_ID = "3000037";

        /**
         * 提交创建的 Beat 有错误。
         * 1: html 格式的错误信息
         */
        String BEAT_UNVALID = "3000038";

        /**
         * 没有找到任何信息
         */
        String NO_BEAT = "3000039";

        /**
         * 读取价格表出现错误，已生成错误文档。请下载和您提交的文档对比查看。
         */
        String READ_PRICE_DOC_ERR = "3000040";

        /**
         * 任务已经启动，不能在进行全量重置的操作。
         */
        String CANT_REUPLOAD = "3000041";

        /**
         * 插入数据时出现了未知的错误，可能没有将所有数据成功插入。如果确定失败，请重新处理
         */
        String PART_INSERT_ERR = "3000042";

        /**
         * 读取文档出现错误 [ %s ]
         */
        String READ_EXCEL_ERR = "3000043";

        /**
         * 没有从文件里读取到任何内容。
         */
        String NO_DATA_IN_FILE = "3000044";

        /**
         * 产品信息有错误，已生成错误文档。请下载查看。
         */
        String HAS_ERR_PRODUCT = "3000045";

        /**
         * 控制参数错误
         */
        String CONTROL_ERR = "3000047";

        /**
         * 这价格不对啊
         */
        String PRICE_ERR = "3000048";

        /**
         * 没有找到任何信息
         */
        String NO_BEAT_ITEM = "3000049";

        String UN_USE_1 = "3000051";

        String UN_USE_2 = "3000046";

        String UN_USE_3 = "3000052";

        String UN_USE_4 = "3000053";

        String UN_USE_5 = "3000054";

        String UN_USE_6 = "3000055";

        String UN_USE_7 = "3000056";
    }

}
