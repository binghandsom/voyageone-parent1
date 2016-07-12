package com.voyageone.common.components.transaction;

import com.voyageone.common.components.transaction.exceptions.IgnoreException;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * 简单封装事务的 commit 和 rollback 处理
 *
 * Created by Jonas on 7/4/15.
 */
@Service
public class TransactionRunner {

    private SimpleTransaction simpleTransaction;

    public void setSimpleTransaction(SimpleTransaction simpleTransaction) {
        this.simpleTransaction = simpleTransaction;
    }

    /**
     * 调用业务逻辑，当出现异常时 rollback，否则 commit，commit 之后返回调用结果
     * @param callable 业务逻辑
     * @param <T> 返回值类型
     * @return 业务逻辑的返回值
     */
    public <T> T runWithTran(Callable<T> callable) {
        simpleTransaction.openTransaction();

        try {
            T result = callable.call();
            simpleTransaction.commit();
            return result;
        } catch (IgnoreException ignore) {
            simpleTransaction.rollback();
            return null;
        } catch (RuntimeException re) {
            simpleTransaction.rollback();
            throw re;
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw new RuntimeException(e);
        }
    }

    /**
     * 调用业务逻辑，当出现异常时 rollback，否则 commit
     * @param runnable 业务逻辑
     */
    public void runWithTran(Runnable runnable) {
        simpleTransaction.openTransaction();

        try {
            runnable.run();
            simpleTransaction.commit();
        } catch (IgnoreException ignore) {
            simpleTransaction.rollback();
        } catch (Exception e) {
            simpleTransaction.rollback();
            throw e;
        }
    }
}
