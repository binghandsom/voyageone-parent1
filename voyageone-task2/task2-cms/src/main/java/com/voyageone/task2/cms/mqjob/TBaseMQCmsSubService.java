package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.CommonUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanNameAll;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.CmsBtOperationLogService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.base.TBaseMQAnnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TBaseMQCmsService
 *
 * @author aooer 2016/12/27.
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class TBaseMQCmsSubService<TMQMessageBody extends IMQMessageBody> extends TBaseMQCmsService<TMQMessageBody>  implements IMQSubBeanNameAll {

    @Autowired
    private MongoTemplate mongoTemplate;

    public String[] getAllSubBeanName() {
        Set<String> colList = mongoTemplate.getCollectionNames();
        List<String> orderChannelIdList = colList.stream().filter(s -> s.indexOf("cms_bt_product_c") != -1 && s.length() == 19).map(s1 -> s1.substring(16)).collect(Collectors.toList());
        String[] strings = new String[orderChannelIdList.size()];
        orderChannelIdList.toArray(strings);
        return strings;
    }
}
