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
}