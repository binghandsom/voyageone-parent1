package com.voyageone.cms.modelbean;

import java.util.Date;

import com.voyageone.cms.formbean.BaseBean;

public class SizeChartDetail extends BaseBean {
	
	private String channelId;

	private Integer sizeChartId;

	private String sizeValue; 
	
    private String sizeCn;

    private String tempValue1;

    private String tempValue2;

    private String tempValue3;

    private String tempValue4;

    private String tempValue5;

	public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public Integer getSizeChartId() {
        return sizeChartId;
    }

    public void setSizeChartId(Integer sizeChartId) {
        this.sizeChartId = sizeChartId;
    }
   
	public String getSizeValue() {
		return sizeValue;
	}

	public void setSizeValue(String sizeValue) {
		this.sizeValue = sizeValue;
	}

	public void setTempValue5(String tempValue5) {
		this.tempValue5 = tempValue5;
	}

	public String getSizeCn() {
        return sizeCn;
    }

    public void setSizeCn(String sizeCn) {
        this.sizeCn = sizeCn == null ? null : sizeCn.trim();
    }

    public String getTempValue1() {
        return tempValue1;
    }

    public void setTempValue1(String tempValue1) {
        this.tempValue1 = tempValue1 == null ? null : tempValue1.trim();
    }

    public String getTempValue2() {
        return tempValue2;
    }

    public void setTempValue2(String tempValue2) {
        this.tempValue2 = tempValue2 == null ? null : tempValue2.trim();
    }

    public String getTempValue3() {
        return tempValue3;
    }

    public void setTempValue3(String tempValue3) {
        this.tempValue3 = tempValue3 == null ? null : tempValue3.trim();
    }

    public String getTempValue4() {
        return tempValue4;
    }

    public void setTempValue4(String tempValue4) {
        this.tempValue4 = tempValue4 == null ? null : tempValue4.trim();
    }

    public String getTempValue5() {
        return tempValue5;
    }

  
}