package com.voyageone.task2.cms.job;

import com.voyageone.task2.Context;
import com.voyageone.task2.base.BaseTaskJob;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.cms.service.putaway.UploadProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by Leo on 2015/5/27.
 */
@Component("UploadProductJob")
public class UploadProductJob extends BaseTaskJob{

    @Autowired
    private UploadProductService uploadProductService;

    public static void main(String[] args) {
        Context conext = Context.getContext();
        ApplicationContext ctx = new GenericXmlApplicationContext("applicationContext.xml");
        conext.putAttribute("springContext", ctx);

        UploadProductService uploadProductService = ctx.getBean(UploadProductService.class);

        uploadProductService.do_upload();
    }

    @Override
    protected BaseTaskService getTaskService() {
        return uploadProductService;
    }
}
