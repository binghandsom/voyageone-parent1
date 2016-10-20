package com.voyageone.common.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 11/24/15.
 */
public class CommonUtilTest {
    @Test
    public void testSplitList() {

        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        list.add(9);
        list.add(0);

        List<List<Integer>> lists = CommonUtil.splitList(list, 5);

        assert lists.size() == 2 && lists.get(1).get(0).equals(6);
    }

//    @Test
//    public void test1() {
//        Map<String, Object> doc2 = new HashMap<>();
//        doc2.put("c", "123");
//        Map<String, Object> doc1 = new HashMap<>();
//        doc1.put("b", doc2);
//        Map<String, Object> doc = new HashMap<>();
//        doc.put("a", doc1);
//
//        System.out.println(getDataFromDocument(doc, "a.b.c"));
//    }
//
//    private Object getDataFromDocument(Map<String, Object> doc, String path) {
//        return getDataFromDocument(doc, path, new ArrayList<>());
//    }
//
//
//    @SuppressWarnings("ConstantConditions")
//    private Object getDataFromDocument(Map<String, Object> doc, String path, List<String> rightPathList) {
//        if (path == null || doc == null) {
//            return null;
//        }
//        if (doc.containsKey(path)) {
//            if (rightPathList.isEmpty()) {
//                return doc.get(path);
//            } else {
//
//                Object docMap = doc.get(path);
//                if (docMap instanceof Map) {
//                    for (int i=0; i<rightPathList.size()-1; i++) {
//                        docMap = ((Map) docMap).get(rightPathList.get(i));
//                        if (!Map.class.isInstance(docMap)) {
//                            return null;
//                        }
//                    }
//                    return ((Map) docMap).get(rightPathList.get(rightPathList.size()-1));
//                }
//            }
//        } else {
//            if (path.contains(".")) {
//                String newPath = path.substring(0, path.lastIndexOf("."));
//                String newRightPath = path.substring(path.lastIndexOf(".") + 1);
//                List<String> newRightPathList = new ArrayList<>();
//                newRightPathList.add(newRightPath);
//                newRightPathList.addAll(rightPathList);
//                return getDataFromDocument(doc, newPath, newRightPathList);
//            }
//        }
//        return null;
//    }
}