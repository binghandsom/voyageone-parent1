package com.voyageone.components.gilt.bean;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltSize {

    private String type;//	The Size Type (eg. Women's Rings)

    private String value;//	The Size Value (eg. 7)

    private Long size_chart_id;//	Used to get complete size chart in /size_charts/:id

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getSize_chart_id() {
        return size_chart_id;
    }

    public void setSize_chart_id(Long size_chart_id) {
        this.size_chart_id = size_chart_id;
    }
}
