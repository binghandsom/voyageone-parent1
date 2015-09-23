package com.voyageone.batch.bi.etl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.bi.bean.modelbean.ChannelBean;
import com.voyageone.batch.bi.mapper.ChannelMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.configs.FileProperties;

@Service
public class VtStockService extends BaseBiKettleService {
	private final Log logger = LogFactory.getLog(getClass());

	private final static String TASK_NAME = "vt_stock_s";
	private final static String KBJ_FILE_NAME = "vt_stock_s";

	private final static String TASK_NAME_V = "vt_stock_v";
	private final static String KBJ_FILE_NAME_V = "vt_stock_v";

	@Autowired
	private ChannelMapper channelMapper;

	public VtStockService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

	@Override
	public boolean execute() throws Exception {
		// get listChannel
		List<ChannelBean> listChannel = getChannelList();
		execute_source(listChannel, -2);
		execute_taget(listChannel, -2);
		return true;
	}
	
	/**
	 * getChannelList
	 * @return
	 */
	private List<ChannelBean> getChannelList() {
		Map<String, String> mapParameter = new HashMap<String, String>();
		mapParameter.put("enable", String.valueOf(Constants.ENABLE));
		List<ChannelBean> listChannel = channelMapper.select_list_vm_channel(mapParameter);
		return listChannel;
	}
	
	/**
	 * execute_source
	 * @param listChannel
	 * @param preDate
	 * @return
	 * @throws Exception
	 */
	private void execute_source(List<ChannelBean> listChannel, int preDate) throws Exception {
		int pre = preDate;
		Map<String, String> params = createParams(pre);
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
		for (ChannelBean channelBean : listChannel) {
			String code = channelBean.getCode();
			setParams(params);
			// if (!"006".equals(code)) {
			// continue;
			// }
			String jobXmlPath = FileProperties.readValue("job_xml_path_channel_" + code);
			if (jobXmlPath != null && !"".equals(jobXmlPath.trim())) {
				BaseBiKettleService.setJobXmlPath(jobXmlPath);
				logger.info(taskName + " channel:" + code + "service开始:"+ jobXmlPath);
				super.execute();
				logger.info(taskName + "channel:" + code + "service结束");
			}
		}
	}
	
	/**
	 * execute_taget
	 * @param listChannel
	 * @param preDate
	 * @return
	 * @throws Exception
	 */
	private void execute_taget(List<ChannelBean> listChannel, int preDate) throws Exception {
		
		int pre = preDate;
		super.taskName = TASK_NAME_V;
		super.jobXmlFileName = KBJ_FILE_NAME_V;
		for (ChannelBean channelBean : listChannel) {
			String code = channelBean.getCode();
			// if (!"006".equals(code)) {
			// continue;
			// }
			String jobXmlPath = FileProperties.readValue("job_xml_path_channel_" + code);
			if (jobXmlPath != null && !"".equals(jobXmlPath.trim())) {
				BaseBiKettleService.setJobXmlPath(jobXmlPath);
				logger.info(taskName + " channel:" + code + "service开始:"+ jobXmlPath);
				for (int i = pre; i <= 0; i++) {
					Map<String, String> params = createParams(i);
					setParams(params);
					super.execute();
				}
				logger.info(taskName + "channel:" + code + "service结束");
			}
		}
	}
}
