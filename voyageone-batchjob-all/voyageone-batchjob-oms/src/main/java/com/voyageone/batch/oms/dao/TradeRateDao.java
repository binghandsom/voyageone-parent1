package com.voyageone.batch.oms.dao;

import java.util.List;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;

import org.springframework.stereotype.Repository;

@Repository
public class TradeRateDao extends BaseDao {
	
	public List<Object> selectList(String statement, Object parameter) {
		return super.selectList(statement, parameter);
	}
	
	public int insertTradeRate(Object parameter)
	{
		return updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "insert_oms_tb_traderate", parameter);
	}

}
