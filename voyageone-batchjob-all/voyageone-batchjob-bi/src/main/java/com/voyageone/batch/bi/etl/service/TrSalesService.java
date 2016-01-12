package com.voyageone.batch.bi.etl.service;

import com.voyageone.batch.bi.bean.modelbean.ChannelBean;
import com.voyageone.batch.bi.mapper.ChannelMapper;
import com.voyageone.batch.bi.spider.constants.Constants;
import com.voyageone.batch.configs.FileProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrSalesService extends BaseBiKettleService {
	private final Log logger = LogFactory.getLog(getClass());

	private final static String JOB_XML_PATH = "job_xml_path_transfer";

	private final static String TASK_NAME = "cr_cms_bt_sales_product";
	private final static String KBJ_FILE_NAME = "cr_cms_bt_sales_product";

	private final static String TASK_NAME_V = "tr_cms_bt_sales_product";
	private final static String KBJ_FILE_NAME_V = "tr_cms_bt_sales_product";

	@Autowired
	private ChannelMapper channelMapper;

	public TrSalesService() {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
	}

	@Override
	public boolean execute() throws Exception {
		// get listChannel
		List<ChannelBean> listChannel = getChannelList();
		executeSource(listChannel);
		executeTarget();
		return true;
	}

	/**
	 * getChannelList
	 * @return List<ChannelBean>
	 */
	private List<ChannelBean> getChannelList() {
		Map<String, String> mapParameter = new HashMap<>();
		mapParameter.put("enable", String.valueOf(Constants.ENABLE));
		return channelMapper.select_list_vm_channel(mapParameter);
	}

	/**
	 * executeSource
	 * @param listChannel listChannel
	 * @throws Exception
	 */
	private void executeSource(List<ChannelBean> listChannel) throws Exception {
		super.taskName = TASK_NAME;
		super.jobXmlFileName = KBJ_FILE_NAME;
		for (ChannelBean channelBean : listChannel) {
			String code = channelBean.getCode();
			Map<String, String> params = createParam(code);
			setParams(params);
			String jobXmlPath = FileProperties.readValue("job_xml_path_channel_" + code);
			if (jobXmlPath != null && !"".equals(jobXmlPath.trim())) {
				String trJobXmlPath = FileProperties.readValue(JOB_XML_PATH);
				BaseBiKettleService.setJobXmlPath(trJobXmlPath);
				logger.info(taskName + " channel:" + code + "service开始:"+ trJobXmlPath);
				super.execute();
				logger.info(taskName + "channel:" + code + "service结束");
			}
		}
	}

	/**
	 * executeTarget
	 * @throws Exception
	 */
	private void executeTarget() throws Exception {
		super.taskName = TASK_NAME_V;
		super.jobXmlFileName = KBJ_FILE_NAME_V;
		String jobXmlPath = FileProperties.readValue(JOB_XML_PATH);
		if (jobXmlPath != null && !"".equals(jobXmlPath.trim())) {
			BaseBiKettleService.setJobXmlPath(jobXmlPath);
			logger.info(taskName + "service开始:" + jobXmlPath);
			Map<String, String> params = new HashMap<>();
			setParams(params);
			super.execute();
			logger.info(taskName + "service结束");
		}
	}

	/**
	 * createParam
	 * @param channel_id channel_id
	 * @return
	 */
	public Map<String, String> createParam(String channel_id) {
		Map<String, String> params = new HashMap<>();
		params.put("channel_id", channel_id);
		return params;
	}
}
