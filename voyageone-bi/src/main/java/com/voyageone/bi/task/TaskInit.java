package com.voyageone.bi.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.bi.base.BiException;
import com.voyageone.bi.dao.ChannelInfoDao;
import com.voyageone.bi.dao.ProductInfoDao;

@Service
public class TaskInit {

	@Autowired
	private ChannelInfoDao channelInfoDao;

	@Autowired
	private ProductInfoDao productInfoDao;
	
	public void  init() throws BiException {
		List<String>  channelDBList = channelInfoDao.selectChannelDBList();
		for(String dbName:channelDBList) {
			productInfoDao.selectAllProductIDAndCode(dbName);
		}
	}
}
