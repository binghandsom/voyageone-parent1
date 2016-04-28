package com.voyageone.service.dao;

import com.voyageone.common.message.enums.DisplayType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库端(MyBatis)枚举处理
 * @author Jonas
 * @version 2.0.0, 12/5/15
 */
public class DisplayTypeHandler extends BaseTypeHandler<DisplayType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DisplayType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public DisplayType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return DisplayType.valueOf(rs.getInt(columnName));
    }

    @Override
    public DisplayType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return DisplayType.valueOf(rs.getInt(columnIndex));
    }

    @Override
    public DisplayType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return DisplayType.valueOf(cs.getInt(columnIndex));
    }
}
