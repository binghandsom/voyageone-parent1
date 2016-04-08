package com.voyageone.components.gilt.bean;

import java.util.List;

/**
 * @author aooer 2016/2/1.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltSizeChart {

    private Long id;//	The size chart id

    private String name;//	The display name of the size chart

    private List<GiltSizeChartEntry> entries;//	See Size Chart Entry

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GiltSizeChartEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<GiltSizeChartEntry> entries) {
        this.entries = entries;
    }
}
