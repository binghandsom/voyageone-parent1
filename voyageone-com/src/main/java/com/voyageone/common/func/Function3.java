package com.voyageone.common.func;

/**
 * Created by admin on 2015/10/22.
 */
@FunctionalInterface
public interface Function3<T1,T2,T3,R> {
    R apply(T1 t1, T2 t2, T3 t3);
}
//@FunctionalInterface
//public interface FunctionCallBack3<T1,T2,T3,R> {
//    R apply( T1 t2, T2 t1,T3 t3);
//}
