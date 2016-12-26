package com.voyageone.web2.cms.views.system.mq;

import com.voyageone.components.rabbitmq.utils.MQConfigUtils;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.com.mq.MqSender;
import com.voyageone.components.rabbitmq.config.AnnotationProcessorByIP;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;

/**
 * MqManagerController
 *
 * @author aooer 2016/6/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping(value = CmsUrlConstants.SYSTEM.MQ.ROOT, method = {RequestMethod.POST, RequestMethod.GET})
public class MqManagerController extends CmsController {

    @Autowired
    private MqSender mqSender;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AnnotationProcessorByIP annotationProcessorByIP;

    @RequestMapping(CmsUrlConstants.SYSTEM.MQ.INIT)
    public AjaxResponse init(HttpServletRequest request) throws Exception {
        List<String> result = new ArrayList<>();
        Field[] fields = CmsMqRoutingKey.class.getDeclaredFields();
        for (Field field : fields) {
            result.add((String) field.get(CmsMqRoutingKey.class));
        }
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.SYSTEM.MQ.SEND)
    public AjaxResponse send(HttpServletRequest request) throws Exception {
        Set<Object> result = new HashSet<>();
        try {
            // mq routingKey
            String routingKey = request.getParameter("messageQueue");
            // mq Body
            String messageBody = request.getParameter("messageBody");

            //check input param
            if (StringUtils.isEmpty(routingKey) || StringUtils.isEmpty(messageBody) || MapUtils.isEmpty(JacksonUtil.jsonToMap(messageBody))) {
                result.add("参数未通过校验，requestMap:" + JacksonUtil.bean2Json(request.getParameterMap()));
                return success(result);
            }

            //check routingKey exist
            try {
                this.rabbitTemplate.execute(channel -> {
                    channel.basicGet(annotationProcessorByIP.isLocal() ? MQConfigUtils.getAddStrQueneName(routingKey) : routingKey, false);
                    return channel.basicRecover();
                });
            } catch (Exception e) {
                result.add("发送的队列没有激活(需要提供消费者激活)，" + e.getMessage());
                return success(result);
            }

            // send message to mq
            mqSender.sendMessage(routingKey, JacksonUtil.jsonToMap(messageBody));

            //set success result
            result.add(1);
            return success(result);
        } catch (Exception e) {
            result.add(e.getMessage());
            return success(result);
        }
    }
}
