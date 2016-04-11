package com.voyageone.components.gilt.bean;

import java.util.List;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltSizeChartEntry {

    private String	size_type;//The name of the size type

    private List<String>	values;//The array of values for the size type

    public String getSize_type() {
        return size_type;
    }

    public void setSize_type(String size_type) {
        this.size_type = size_type;
    }

    public  List<String> getValues() {
        return values;
    }

    public void setValues( List<String> values) {
        this.values = values;
    }
}
