package com.voyageone.bi.commonutils;

/* Proxool中这三个Fields是int型，但是Spring注入时作为long型，所以会报错
 * 重写三个set方法
 */

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class ProxoolDataSource extends org.logicalcobwebs.proxool.ProxoolDataSource {

    public void setHouseKeepingSleepTime(long houseKeepingSleepTime) {
        super.setHouseKeepingSleepTime((int) houseKeepingSleepTime);
    }

    public void setMaximumConnectionLifetime(long maximumConnectionLifetime) {
        super.setMaximumConnectionLifetime((int) maximumConnectionLifetime);
    }

    public void setOverloadWithoutRefusalLifetime(long overloadWithoutRefusalLifetime) {
        super.setOverloadWithoutRefusalLifetime((int) overloadWithoutRefusalLifetime);
    }

    public void setRecentlyStartedThreshold(long recentlyStartedThreshold) {
        super.setRecentlyStartedThreshold((int) recentlyStartedThreshold);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
