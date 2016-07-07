package com.voyageone.base.dao.mysql.paginator.domain;

import com.github.miemiedev.mybatis.paginator.domain.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * MySqlOrder
 *
 * @author miemiedev
 */
public class MySqlOrder extends Order {

    public MySqlOrder(String property, Direction direction, String orderExpr) {
        super(property, direction, orderExpr);
    }

    public static List<Order> formString(String orderSegment) {
        return formString(orderSegment, null);
    }

    public static List<Order> formString(String orderSegment, String orderExpr) {
        if (orderSegment == null || orderSegment.trim().equals("")) {
            return new ArrayList<>(0);
        }

        List<Order> results = new ArrayList<>();
        String[] orderSegments = orderSegment.trim().split(",");
        for (String sortSegment : orderSegments) {
            Order order = _formString(sortSegment, orderExpr);
            if (order != null) {
                results.add(order);
            }
        }
        return results;
    }

    private static Order _formString(String orderSegment, String orderExpr) {
        if (orderSegment == null || "".equals(orderSegment.trim()) ||
                orderSegment.startsWith("null.") || orderSegment.startsWith(".")) {
            return null;
        }
        orderSegment = orderSegment.replaceAll("\\r\\n", " ");
        orderSegment = orderSegment.replaceAll("\\t", " ");
        orderSegment = orderSegment.replaceAll("\\n", " ");
        while (orderSegment.indexOf("  ") > 0) {
            orderSegment = orderSegment.replaceAll("  ", " ");
        }
        orderSegment = orderSegment.trim();

        String[] array = orderSegment.trim().split(" ");
        if (array.length != 1 && array.length != 2) {
            throw new IllegalArgumentException("orderSegment pattern must be {property}.{direction}, input is: " + orderSegment);
        }

        return create(array[0], array.length == 2 ? array[1] : "asc", orderExpr);
    }

    /**
     * create
     *
     * @param property  String
     * @param direction String
     * @param orderExpr placeholder is "?", in oracle like: "nlssort( ? ,'NLS_SORT=SCHINESE_PINYIN_M')".
     *                  Warning: you must prevent orderExpr SQL injection.
     */
    public static Order create(String property, String direction, String orderExpr) {
        return new Order(property, Order.Direction.fromString(direction), orderExpr);
    }
}