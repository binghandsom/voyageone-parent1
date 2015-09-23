package com.voyageone.batch.oms.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.oms.dao.TradeRateDao;
import com.voyageone.batch.oms.modelbean.TradeRateBean;
import com.voyageone.common.Constants;

@Service
public class TradeRateService {

	@Autowired
	TradeRateDao tradeRateDao;
	public void inseretTradeRateList(List<TradeRateBean> tradeRateList) {
		for (TradeRateBean tradeRateBean : tradeRateList) {
			tradeRateDao.insertTradeRate(tradeRateBean);
		}
	}

}
