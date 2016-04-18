package com.voyageone.common.spring;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by xyyz150 on 2015/1/23.
 */
public class SysInitBean implements InitializingBean, ServletContextAware {
   // @Autowired
 //   CacheService service;
  //  @Autowired
   // TenantLoginDaoExt daoExtTenantLogin;

    //ExecutorService executor = Executors.newFixedThreadPool(4);
    @Override
    public void afterPropertiesSet()  {
//        TenantLogin tl= daoExtTenantLogin.GetTenantLogin();
//        if(tl!=null) {
//            ContextSession.setTenantId(Long.parseLong(tl.getTenantIdentity()));
//            ContextSession.setTenantCode(tl.getTenantCode());
//            MyLogger.error("初始化租户信息");
//            service.InitCache();
//            MyLogger.error("初始化地区信息");
//        }
        return;
    }


    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
