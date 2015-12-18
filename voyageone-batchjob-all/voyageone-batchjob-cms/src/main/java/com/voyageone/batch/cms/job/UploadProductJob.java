package com.voyageone.batch.cms.job;

import com.voyageone.batch.Context;
import com.voyageone.batch.base.BaseTaskJob;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.cms.service.UploadProductService;
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
