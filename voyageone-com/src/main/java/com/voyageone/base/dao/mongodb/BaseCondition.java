package com.voyageone.base.dao.mongodb;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * BaseConditionObject
 *
 * @author chuanyu.liang, 12/11/15.
 * @author Jonas, 2015-12-18.
 * @version 2.0.1
 * @since 2.0.0
 */
public abstract class BaseCondition {

    public String buildProjection(String... projection) {
        if (projection == null) {
            return null;
        }

        // 当没有参数时,不需要设定任何内容
        if (projection.length == 0)
            return null;

        // 先过滤掉所有空
        List<String> fields = Arrays.stream(projection)
                .filter(i -> !StringUtils.isEmpty(i))
                .map(String::trim)
                .collect(toList());

        // 没有任何参数..
        if (fields.isEmpty())
            return null;

        // 当有参数时,检查第一个是否是 json 格式,如果是,则忽略后续所有参数
        // 如果不是,则默认后续所有参数都是列名,而非 json
        String first = fields.get(0);
        if (first.startsWith("{") && first.endsWith("}")) {
            return first;
        }

        // 格式为 {"a":1,"b":1,"c":1},则重复的间隔为 ":1," 则 {"joining(?:1,?:1,?)":1} => {"a":1,"b":1,"c":1}
        return String.format("{\"%s\":1}", fields.stream().collect(joining("\":1,\"")));
    }

}
