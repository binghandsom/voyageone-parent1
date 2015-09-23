package com.voyageone.common.components.transaction.exceptions;

/**
 * 在事务包裹的业务逻辑里出现异常时，通过该异常包裹，则会忽略不会继续向上抛出
 *
 * Created by Jonas on 7/6/15.
 */
public class IgnoreException extends RuntimeException {

}
