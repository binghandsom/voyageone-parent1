package com.voyageone.common.components.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 事务管理帮助类，封装了事务处理的相关内容
 *
 * Created by Jonas on 7/3/15.
 */
@Service
public class SimpleTransaction {

    private DataSourceTransactionManager transactionManager;

    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    private ThreadLocal<TransactionStatus> transactionStatusThreadLocal;

    private static DefaultTransactionDefinition definition = new DefaultTransactionDefinition();

    public void openTransaction() {
        if (transactionStatusThreadLocal == null)
            transactionStatusThreadLocal = new ThreadLocal<>();

        if (transactionStatusThreadLocal.get() == null)
            transactionStatusThreadLocal.set(transactionManager.getTransaction(definition));
    }

    public void commit() {
        transactionManager.commit(transactionStatusThreadLocal.get());
        transactionStatusThreadLocal.remove();
    }

    public void rollback() {
        transactionManager.rollback(transactionStatusThreadLocal.get());
        transactionStatusThreadLocal.remove();
    }
}
