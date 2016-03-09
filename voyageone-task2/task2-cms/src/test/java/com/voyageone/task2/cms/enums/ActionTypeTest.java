package com.voyageone.task2.cms.enums;

import com.voyageone.common.configs.Enums.ActionType;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lewis on 15-12-9.
 */
public class ActionTypeTest {

    @Test
    public void testValueOf() throws Exception {
        ActionType actionType = ActionType.valueOf(2);
        ActionType actionType1 = ActionType.valueOf(0);
        ActionType actionType2 = ActionType.valueOf(1);
        ActionType actionType3 = ActionType.valueOf(1);

        Assert.assertTrue(actionType2.equals(actionType3));
        Assert.assertTrue(actionType2==actionType3);

        Boolean isTrue = Boolean.valueOf("false");

        System.out.println(isTrue);
    }
}