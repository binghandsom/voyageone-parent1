package com.voyageone.service.impl.cms;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.bean.cms.TaskSummary;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by Ethan Shi on 2016/6/28.
 *
 * @author Ethan Shi
 * @version 2.2.0
 * @since 2.2.0
 */

@Service
public class TranslationTaskService extends BaseService {

    @Autowired
    private ProductService productService;

    public TaskSummary getTaskSummary(String channelId, String userName) throws BusinessException
    {
        Date date = DateTimeUtil.addHours(DateTimeUtil.getDate(), -48);
        String translateTimeStr = DateTimeUtil.format(date, null);

        TaskSummary taskSummary = new TaskSummary();

        //未分配的任务
        String queryStr = String.format("{'common.fields.translateStatus':'0'," +
                "'common.fields.isMasterMain':1," +
                " '$or': [{'common.fields.translator':''} ,  " +
                "{'common.fields.translateTime':{'$gt':'%s'}} , " +
                "{'common.fields.translator':{'$exists' : false}}, " +
                "{'common.fields.translateTime':{'$exists' : false}}]}", translateTimeStr);

        taskSummary.setUnassginedCount(Long.valueOf(productService.getCnt(channelId, queryStr)).intValue());

        //已分配但未完成的任务
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                        "'common.fields.translateStatus':'0'," +
                        "'common.fields.translator':{'$exists' : true}," +
                        "'common.fields.translator':{'$ne' : ''}," +
                        "'common.fields.translateTime':{'$gt':'%s'}}", translateTimeStr);
        taskSummary.setImcompeleteCount(Long.valueOf(productService.getCnt(channelId, queryStr)).intValue());

        //完成翻译总商品数
        queryStr = "{'common.fields.isMasterMain':1," +
                   "'common.fields.translateStatus':'1'}";
        taskSummary.setCompeleteCount(Long.valueOf(productService.getCnt(channelId, queryStr)).intValue());

        //个人完成翻译商品数
        queryStr = String.format("{'common.fields.isMasterMain':1," +
                        "'common.fields.translateStatus':'1'," +
                        "'common.fields.translator':'%s'}", userName);
        taskSummary.setUserCompeleteCount(Long.valueOf(productService.getCnt(channelId, queryStr)).intValue());
        return  taskSummary;
    }

}
