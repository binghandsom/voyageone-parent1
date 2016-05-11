package com.voyageone.common.util.excel;

import java.util.ArrayList;
import java.util.List;

public class ListHelp {

    public static <T> List<List<T>> getPageList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        List<T> page = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i % size == 0) {
                page = new ArrayList<T>();
                result.add(page);
            }
            page.add(list.get(i));
        }
        return result;
    }
}


