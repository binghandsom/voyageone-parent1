package com.voyageone.batch.oms.dao;

import java.util.List;

import com.voyageone.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public class PostOrderDao extends BaseDao {
	
	public List<Object> selectList(String statement, Object parameter) {
		return super.selectList(statement, parameter);
	}
	
	public int updateTable(String statement, Object parameter)
	{
		return updateTemplate.update(statement, parameter);
	}

}
