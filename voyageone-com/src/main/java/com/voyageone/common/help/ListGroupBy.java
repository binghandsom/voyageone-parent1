package com.voyageone.common.help;

/**
 * Created by xyyz150 on 2014/12/12.
 */
public interface ListGroupBy<T,TReturn>{
    TReturn GroupBy(T obj);
}
