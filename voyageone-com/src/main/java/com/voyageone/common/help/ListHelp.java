package com.voyageone.common.help;


import com.voyageone.common.func.Function2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by admin on 2014/12/4.
 */
public class ListHelp {
    public static <T, TTarget> List<T> getSourceListExistsTarget(List<T> sourceList, List<TTarget> targetList, ListEquals<T, TTarget> listequals) {
        List<T> list = new ArrayList<T>();
        for (T source : sourceList) {
            for (TTarget target : targetList) {
                if (listequals.equals(source, target)) {
                    list.add(source);
                    break;
                }
            }
        }
        return list;
    }

    public static <T, TTarget> boolean Exists(List<T> list, TTarget t, ListEquals<T, TTarget> equals) {
        for (T source : list) {
            if (equals.equals(source, t)) {
                return true;
            }
        }
        return false;
    }

    public static <T, TTarget> List<T> getSourceListNotExistsTarget(List<T> sourceList, List<TTarget> targetList, ListEquals<T, TTarget> listequals) {
        List<T> list = new ArrayList<T>();
        boolean exists = false;
        for (T source : sourceList) {
            exists = false;
            for (TTarget target : targetList) {
                if (listequals.equals(source, target)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                list.add(source);
            }
        }
        return list;
    }

    public static <T, TReturn> List<TReturn> getTReturnList(List<T> sourceList, ListGetTReturn<T, TReturn> getTReturn) {
        List<TReturn> list = new ArrayList<TReturn>();
        for (T t : sourceList) {
            TReturn ret = getTReturn.getReturnT(t);
            if (ret != null) {
                list.add(ret);
            }
        }
        return list;
    }

    public static <T, TReturn> List<TReturn> getListByFN(List<T> sourceList, ListGetTReturn<T, TReturn> getTReturn) {
        List<TReturn> list = new ArrayList<TReturn>();
        for (T t : sourceList) {
            TReturn ret = getTReturn.getReturnT(t);
            if (ret != null) {
                list.add(ret);
            }
        }
        return list;
    }

    public static <K, T> Map<K, List<T>> GroupBy(List<T> sourceList, ListGroupBy<T, K> gb) {
        Map<K, List<T>> map = new HashMap<K, List<T>>();
        if (sourceList == null) return map;
        if (gb == null) return map;
        for (T item : sourceList) {
            K key = gb.GroupBy(item);
            if (map.containsKey(key)) {
                map.get(key).add(item);
            } else {
                List<T> list = new ArrayList<T>();
                list.add(item);
                map.put(key, list);
            }
        }
        return map;
    }

    public static <T> List<T> concat(List<T> sourceList, List<T> targetList, ListEquals<T, T> equals) {
        List<T> list = getSourceListNotExistsTarget(sourceList, targetList, equals);
        list.addAll(targetList);
        return list;
    }

    public static <T> List<T> distinct(List<T> sourceList, ListEquals<T, T> equals) {
        List<T> list = new ArrayList<T>();
        for (T t : sourceList) {
            if (!Exists(list, t, equals)) {
                list.add(t);
            }
        }
        return list;
    }

    public static boolean IsNullOrEmpty(List list) {
        if (list == null) return true;
        if (list.size() == 0) return true;
        return false;
    }

    public static <T> Long SumLong(List<T> sourceList, ListGet<T, Long> get) {
        Long ret = 0L;
        if (sourceList == null) return ret;
        if (get == null) return ret;
        for (T item : sourceList) {
            ret += get.Get(item);
        }
        return ret;
    }

    public static <T, Target> T Frist(List<T> sourceList, Target tar, ListFilter<T, Target> select) {
        if (sourceList == null) return null;
        if (select == null) return null;
        for (T item : sourceList) {
            if (select.Select(item, tar)) {
                return item;
            }
        }
        return null;
    }

    public static <T> String Join(List<T> sourceList, ListGet<T, String> get) {
        String str = "";
        if (sourceList == null) return str;
        if (get == null) return str;
        for (T item : sourceList) {
            str += get.Get(item);
        }
        return str;
    }

    public static <T> Double SumDouble(List<T> sourceList, ListGet<T, Double> get) {
        Double ret = 0D;
        if (sourceList == null) return ret;
        if (get == null) return ret;
        for (T item : sourceList) {
            ret += get.Get(item);
        }
        return ret;
    }

    public static <T, Target> List<T> Where(List<T> sourceList, Target tar, ListFilter<T, Target> select) {
        List<T> list = new ArrayList<T>();
        if (sourceList == null) return list;
        if (select == null) return list;
        for (T item : sourceList) {
            if (select.Select(item, tar)) {
                list.add(item);
            }
        }
        return list;
    }

    public static <T, Target> void Remove(List<T> sourceList, Target tar, ListEquals<T, Target> equals) {
        List<T> list = new ArrayList<T>();
        if (sourceList == null) return;
        if (equals == null) return;
        for (T source : list) {
            if (equals.equals(source, tar)) {
                list.add(source);
            }
        }
        sourceList.removeAll(list);
    }

    public static <T> List<List<T>> getPageList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<List<T>>();
        List<T> page = new ArrayList<T>();
        for (int i = 0; i < list.size(); i++) {
            if (i % size == 0) {
                page = new ArrayList<T>();
                result.add(page);
            }
            page.add(list.get(i));
        }
        return result;
    }

    public static <T> T Max(List<T> list, Function2<T, T, Boolean> fun) {
        T result = null;
        for (T t : list) {
            if (fun.apply(t, result)) {
                result = t;
            }
        }
        return result;
    }

    public static <T> T firstOrDefault(List<T> list, Function<T, Boolean> fun) {
        for (T t : list) {
            if (fun.apply(t)) {
                return t;
            }
        }
        return null;
    }
public  static <T>  List<T> toList(Iterable<T> arr) {
    List<T> list = new ArrayList<T>();
    for (T t : arr) {
        list.add(t);
    }
    return list;
}
}


