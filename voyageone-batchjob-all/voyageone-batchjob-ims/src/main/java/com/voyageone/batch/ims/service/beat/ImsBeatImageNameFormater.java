package com.voyageone.batch.ims.service.beat;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于根据模板构建自定义的 imageName
 * Created by Jonas on 10/30/15.
 */
public class ImsBeatImageNameFormater {

    private final static SpelParserConfiguration config = new SpelParserConfiguration(true, true);

    private final static ExpressionParser parser = new SpelExpressionParser(config);

    private final static Pattern keyPattern = Pattern.compile("\\{(.+?)\\}");

    private static List<TaskControlBean> taskControls;

    public static void setTaskControls(List<TaskControlBean> taskControls) {
        taskControls = taskControls;
    }

    public static String formatImageName(ImsBeatImageInfo imageInfo) {

        String template = TaskControlUtils.getVal2(taskControls, TaskControlEnums.Name.image_name_template, imageInfo.getChannel_id());

        Matcher matcher = keyPattern.matcher(template);

        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {

            String key = matcher.group(1);

            Object value = parser.parseExpression(key).getValue(imageInfo);

            matcher.appendReplacement(buffer, String.valueOf(value));
        }

        matcher.appendTail(buffer);

        return buffer.toString();
    }
}
