package com.voyageone.base.dao.mongodb.test.dao;

import com.voyageone.base.dao.mongodb.model.BaseMongoModel;

/**
 * Created by DELL on 2015/10/19.
 */
public class Customer extends BaseMongoModel {

    private String firstName;

    private String lastName;

    public Customer(String firstName, String lastName) {
        //super(channelID);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s']",
                _id, firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
