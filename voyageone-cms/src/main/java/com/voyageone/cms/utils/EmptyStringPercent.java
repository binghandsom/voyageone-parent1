package com.voyageone.cms.utils;

import com.voyageone.common.util.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmptyStringPercent implements TypeHandler<String> {

	@Override
	public String getResult(ResultSet rs, String columnName) throws SQLException {
		return (StringUtils.isEmpty(rs.getString(columnName))) ? "" : rs.getString(columnName) + " %";
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		return (StringUtils.isEmpty(rs.getString(columnIndex))) ? "" : rs.getString(columnIndex) + " %";
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return (StringUtils.isEmpty(cs.getString(columnIndex))) ? "" : cs.getString(columnIndex) + " %";
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, String arg2, JdbcType arg3) throws SQLException {
		
	}

}
