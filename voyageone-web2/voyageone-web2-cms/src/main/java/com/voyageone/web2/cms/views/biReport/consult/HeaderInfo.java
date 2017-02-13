package com.voyageone.web2.cms.views.biReport.consult;

import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Created by dell on 2017/1/13.
 * x_low 最低列，最小值为0，位第一列
 * x_high 最长列
 * y_low  最低行数，最小值为0,
 * y_high 最长行数
 */
public class HeaderInfo {
    String headerName;
    CellRangeAddress address;

    public HeaderInfo()
    {

    }
    public HeaderInfo(String headerName,CellRangeAddress address )
    {
        this.headerName=headerName;
        this.address=address;
    }

    public String getHeaderName() {
        return headerName;
    }

    public CellRangeAddress getAddress() {
        return address;
    }

    public void setAddress(CellRangeAddress address) {
        this.address = address;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

}
