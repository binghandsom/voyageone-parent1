package com.voyageone.common.components.issueLog.beans;

import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;

/**
 * 对应 com_issue_log 表。包内数据库专用，不对外开放
 * Created by Tester on 5/6/2015.
 *
 * @author Jonas
 */
public class IssueLogBean {
    private int id;
    private String error_location;
    private SubSystem sub_system;
    private ErrorType error_type;
    private String description;
    private String description_add;
    private String date_time;
    private int send_flg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getError_location() {
        return error_location;
    }

    public void setError_location(String error_location) {
        this.error_location = error_location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public int getSend_flg() {
        return send_flg;
    }

    public void setSend_flg(int send_flg) {
        this.send_flg = send_flg;
    }

    public ErrorType getError_type() {
        return error_type;
    }

    public void setError_type(ErrorType error_type) {
        this.error_type = error_type;
    }

    public SubSystem getSub_system() {
        return sub_system;
    }

    public void setSub_system(SubSystem sub_system) {
        this.sub_system = sub_system;
    }

    int getError_type_value() {
        return getError_type().getId();
    }

    void setError_type_value(int error_type_value) {
        setError_type(ErrorType.valueOf(error_type_value));
    }

    int getSub_system_value() {
        return getSub_system().getId();
    }

    void setSub_system_value(int sub_system_value) {
        setSub_system(SubSystem.valueOf(sub_system_value));
    }

    public String getDescription_add() {
        return description_add;
    }

    public void setDescription_add(String description_add) {
        this.description_add = description_add;
    }
}
