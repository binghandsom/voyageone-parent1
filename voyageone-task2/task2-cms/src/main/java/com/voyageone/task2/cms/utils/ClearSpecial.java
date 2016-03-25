package com.voyageone.task2.cms.utils;

import com.voyageone.common.Constants;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ClearSpecial implements TypeHandler<String> {

	Pattern special_symbol;
	public ClearSpecial(){
		super();
		special_symbol = Pattern.compile("[~@'\\s.#$%&*_''/‘’^\\()]");
	}
	@Override
	public String getResult(ResultSet rs, String columnName) throws SQLException {

		return (rs.getString(columnName) == null) ? "" :special_symbol.matcher(rs.getString(columnName).toLowerCase()).replaceAll(Constants.EmptyString) ;
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		return (rs.getString(columnIndex) == null) ? "" : special_symbol.matcher(rs.getString(columnIndex).toLowerCase()).replaceAll(Constants.EmptyString) ;
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
		return (cs.getString(columnIndex) == null) ? "" : special_symbol.matcher(cs.getString(columnIndex).toLowerCase()).replaceAll(Constants.EmptyString) ;
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, String arg2, JdbcType arg3) throws SQLException {
		
	}

}
