package com.voyageone.bi.dao.mondrian;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mondrian.olap4j.MondrianOlap4jDriver;

import org.olap4j.Cell;
import org.olap4j.CellSet;
import org.olap4j.OlapConnection;
import org.olap4j.OlapStatement;
import org.olap4j.Position;
import org.springframework.core.io.Resource;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

import com.voyageone.bi.disbean.ChartGridDisBean;

public class MondrianDaoSupport extends DaoSupport {

	private String connectStr;	
	public void setConnectStr(String connectStr) {
		this.connectStr = connectStr;
	}

	private Resource schemaXml;
	public void setSchemaXml(Resource schemaXml) {
		this.schemaXml = schemaXml;
	}
	private String getCubeXmlPath() {
		try {
			return schemaXml.getFile().getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(
					"Could not get absolute path to schema: " + e.getMessage());
		}
	}

//	private  OlapConnection olapConnection = null;
	public OlapConnection getConnection() throws ClassNotFoundException, SQLException {
		OlapConnection olapConnection = null;
		if (olapConnection == null) {
			Class.forName(MondrianOlap4jDriver.class.getName());
			Connection connection = DriverManager.getConnection(
            		"jdbc:mondrian:"
					+ "Jdbc="+ connectStr
					+ ";Catalog=file://" + getCubeXmlPath() + ";"); 
			olapConnection = connection.unwrap(OlapConnection.class);
		} 
		return olapConnection;
	}

	private Lock lock = new ReentrantLock();
	
	public CellSet query(String queryStr) {
		OlapConnection olapConnection = null;
		OlapStatement statement = null;
		try {
			lock.lock();
			
			olapConnection = getConnection();
			statement = olapConnection.createStatement();
			return statement.executeOlapQuery(queryStr);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
				if (olapConnection != null) {
					olapConnection.close();
				}
			} catch (Exception e) {}
			lock.unlock();
		}
	}
	
	public ChartGridDisBean queryWithBean(String queryStr) {
		CellSet cellSet = query(queryStr);
		ChartGridDisBean result = new ChartGridDisBean();
		int i=0;
		for (Position column : cellSet.getAxes().get(0)) {
		    String columnName = column.getMembers().get(0).getUniqueName();
		    Cell cell = cellSet.getCell(i);
		    MondrianSalesUtils.setColumnValue(columnName, cell, result);
		    i++;
		}
		return result;
	}
	
	public List<ChartGridDisBean> queryWithBeanList(String queryStr) {
		CellSet cellSet = query(queryStr);
		List<ChartGridDisBean> result = getDisBeanList(cellSet);
		return result;
	}
	
	private List<ChartGridDisBean> getDisBeanList(CellSet cellSet) {
		List<ChartGridDisBean> lstSalesDisBean = new ArrayList<ChartGridDisBean>();
		for (Position row : cellSet.getAxes().get(1)) {
			ChartGridDisBean chartGridDisBean = new ChartGridDisBean();
		    for (Position column : cellSet.getAxes().get(0)) {
	            String columnName = column.getMembers().get(0).getUniqueName();
		        Cell cell = cellSet.getCell(column, row);
		        MondrianSalesUtils.setColumnValue(columnName, cell, chartGridDisBean);
		    }
		    lstSalesDisBean.add(chartGridDisBean);
		}
		return lstSalesDisBean;
	}

	@Override
	protected void checkDaoConfig() throws IllegalArgumentException {
		Assert.notNull(schemaXml, "Requires a schemaXml");
		Assert.notNull(connectStr, "Requires a connectStr");
		try {
			File cube = schemaXml.getFile();
			if (cube == null || !cube.exists()) {
				throw new IllegalArgumentException(
						"file not found: " + cube.getAbsolutePath());
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"Could not open schema on filesytem: " + e.getMessage());
		}
	}

}
