package com.voyageone.batch.bi.etl.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.voyageone.batch.bi.bean.modelbean.ChannelBean;
import com.voyageone.batch.bi.etl.service.BaseBiKettleService;
import com.voyageone.batch.bi.etl.service.KettleExecuterService;
import com.voyageone.batch.bi.mapper.ChannelMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.configs.FileProperties;

public abstract class BaseBiTaskJob {

    private final Log logger = LogFactory.getLog(getClass());

    private boolean running = false;
    
    @Autowired
    private ChannelMapper channelMapper;

    protected Log getLogger() {
        return logger;
    }

    protected abstract BaseBiKettleService[] getTaskServices();

    public void run() {
        if (running) {
            //getLogger().info(getClass() + "正在运行，忽略");
        	getLogger().info("正在运行，忽略");
            return;
        }

        running = true;
        
        KettleExecuterService.init();
        
        BaseBiKettleService[] taskServices = getTaskServices();
        for (int i=0; i<taskServices.length; i++) {
        	BaseBiKettleService service = taskServices[i];
            String taskCheck = service.getTaskName();
        	logger.info(taskCheck + "任务开始");
        	service.startup();
            getLogger().info(taskCheck + "任务结束");
        }

        running = false;
    }
    
    public void repRun() {

        if (running) {
            //getLogger().info(getClass() + "正在运行，忽略");
        	getLogger().info("正在运行，忽略");
            return;
        }

        running = true;
        
        KettleExecuterService.init();
        Map<String, String> mapParameter = new HashMap<String, String>();
        mapParameter.put("enable", String.valueOf(Constants.ENABLE));
        List<ChannelBean> listChannel = channelMapper.select_list_vm_channel(mapParameter);

        for (ChannelBean channelBean : listChannel) {
        	String code = channelBean.getCode();
//        	if (!"006".equals(code)) {
//        		continue;
//        	}
    		String jobXmlPath = FileProperties.readValue("job_xml_path_channel_"+code);
        	if (jobXmlPath!= null && !"".equals(jobXmlPath.trim())) {
        		BaseBiKettleService.setJobXmlPath(jobXmlPath);
    	        BaseBiKettleService[] taskServices = getTaskServices();
    	        for (int i=0; i<taskServices.length; i++) {
    	        	BaseBiKettleService service = taskServices[i];
    	            String taskCheck = service.getTaskName();
    	        	logger.info(taskCheck + " channel:" + code + "任务开始:" + jobXmlPath);
    	        	service.startup();
    	            getLogger().info(taskCheck + "channel:" + code  + "任务结束");
    	        }
        	}
        }

        running = false;
    }
}
