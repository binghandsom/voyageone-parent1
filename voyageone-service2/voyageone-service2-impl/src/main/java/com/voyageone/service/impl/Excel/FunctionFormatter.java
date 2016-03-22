package com.voyageone.service.impl.Excel;
/**
 * Created by admin on 2015/10/10.
 */
@FunctionalInterface
public interface FunctionFormatter<TValue,TRow,TIndex,R> {
    R apply(TValue value, TRow row, TIndex index);
}
