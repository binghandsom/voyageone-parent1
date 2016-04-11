package com.voyageone.components.gilt.bean;

import java.util.Date;
import java.util.List;

/**
 * @author aooer 2016/2/3.
 * @version 2.0.0
 * @since 2.0.0
 */
public class GiltSale {

    private Long id;//	Sale id

    private String name;//	Sale name

    private String description;//	Sale description

    private List<GiltImage> editorial;//	Editorials

    private Date start_datetime;//	sale start time in UTC

    private Date end_datetime;//	sale end time in UTC

    private List<Long> sku_ids;//	List of sku ids that are associated with the sale

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GiltImage> getEditorial() {
        return editorial;
    }

    public void setEditorial(List<GiltImage> editorial) {
        this.editorial = editorial;
    }

    public Date getStart_datetime() {
        return start_datetime;
    }

    public void setStart_datetime(Date start_datetime) {
        this.start_datetime = start_datetime;
    }

    public Date getEnd_datetime() {
        return end_datetime;
    }

    public void setEnd_datetime(Date end_datetime) {
        this.end_datetime = end_datetime;
    }

    public List<Long> getSku_ids() {
        return sku_ids;
    }

    public void setSku_ids(List<Long> sku_ids) {
        this.sku_ids = sku_ids;
    }
}
