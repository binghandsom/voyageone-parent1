package com.voyageone.task2.cms.service.promotion.beat;

/**
 * Created by jonasvlag on 16/5/25.
 * 用于打断价格披露任务的 Job 执行, 仅用于价格披露
 *
 * @version 2.0.0
 * @since 2.0.0
 */
class BreakBeatJobException extends RuntimeException {

    BreakBeatJobException(String message) {
        super(message);
    }

    BreakBeatJobException(String message, Throwable cause) {
        super(message, cause);
    }
}
