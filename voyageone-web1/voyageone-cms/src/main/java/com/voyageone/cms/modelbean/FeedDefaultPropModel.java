package com.voyageone.cms.modelbean;

public class FeedDefaultPropModel extends BaseModel{
	/**
	 * 渠道id.
	 */
	private String channelId;
	/**
	 * 属性名.
	 */
	private String propName;
	/**
	 * 属性类型.
	 */
	private int propType;
	/**
	 * 属性值.
	 */
	private String propValue;


	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public int getPropType() {
		return propType;
	}

	public void setPropType(int propType) {
		this.propType = propType;
	}

	public String getPropValue() {
		return propValue;
	}

	public void setPropValue(String propValue) {
		this.propValue = propValue;
	}

	@Override
	public boolean equals(Object obj) {
		FeedDefaultPropModel model = (FeedDefaultPropModel) obj;
		if (this.propName.equals(model.getPropName())) {
			return true;
		}
		return false;
	}

}
