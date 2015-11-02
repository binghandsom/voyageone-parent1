package com.voyageone.batch.ims.service.beat;

import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;

/**
 * 用于根据模板构建自定义的 imageName
 * Created by Jonas on 10/30/15.
 */
public class ImsBeatImageNameFormat {

    private final static ExpressionParser parser = new SpelExpressionParser();

    private static FormatParserContext formatParserContext = new FormatParserContext();

    private static List<TaskControlBean> taskControls;

    public static void setTaskControls(List<TaskControlBean> taskControls) {

        ImsBeatImageNameFormat.taskControls = taskControls;
    }

    public static String formatImageName(ImsBeatImageInfo imageInfo) {

        String template = TaskControlUtils.getVal2(taskControls, TaskControlEnums.Name.image_name_template, imageInfo.getChannel_id());

        if (template == null) return null;

        return parser.parseExpression(template, formatParserContext).getValue(imageInfo, String.class);
    }

    private static class FormatParserContext implements ParserContext {

        @Override
        public boolean isTemplate() {
            return true;
        }

        @Override
        public String getExpressionPrefix() {
            return "#{";
        }

        @Override
        public String getExpressionSuffix() {
            return "}";
        }
    }
}
