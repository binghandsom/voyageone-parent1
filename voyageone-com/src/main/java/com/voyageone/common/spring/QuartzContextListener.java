package com.voyageone.common.spring;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by admin on 2015/4/28.
 */
public class QuartzContextListener implements ServletContextListener {

    /*
     * ���Դ���д�����
     *
     * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
     * ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
//        MyLogger.error("contextDestroyed:��ȡscheduler begin");
//        try{
//            MyLogger.error("scheduler-Begin");
//            StdScheduler scheduler= (StdScheduler) SpringContextUtil.getBean("schedulerFactoryBean");;
//            MyLogger.error("scheduler-End");
//            if (scheduler != null) {
//                try {
//                    scheduler.shutdown();
//                    // scheduler.getObject().shutdown(false);//��������ִ���� ֹͣ
//                    MyLogger.error("scheduler.shutdown");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            MyLogger.error("��ͣ����,scheduler");
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
     * .ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent arg0) {

    }

}