package com.voyageone.core.emum;

import com.voyageone.common.Constants;

public enum UserEditEnum {
    // ct_user_role_property
    UserRole(Constants.DAO_NAME_SPACE_CORE + "ct_user_select_UserRole",
            Constants.DAO_NAME_SPACE_CORE + "ct_user_update_UserRole",
            Constants.DAO_NAME_SPACE_CORE + "ct_user_insert_UserRole"),
    // ct_user_permission
    UserPermission(Constants.DAO_NAME_SPACE_CORE
            + "ct_user_select_UserPermission", Constants.DAO_NAME_SPACE_CORE
            + "ct_user_update_UserPermission", Constants.DAO_NAME_SPACE_CORE
            + "ct_user_insert_UserPermission"),
    // ct_role
    Role(Constants.DAO_NAME_SPACE_CORE
            + "ct_role_select", Constants.DAO_NAME_SPACE_CORE
            + "ct_role_update", Constants.DAO_NAME_SPACE_CORE
            + "ct_role_insert"),
    // ct_role_permission
    RolePermission(Constants.DAO_NAME_SPACE_CORE
            + "ct_role_getRoleInfo", Constants.DAO_NAME_SPACE_CORE
            + "ct_role_permission_update", Constants.DAO_NAME_SPACE_CORE
            + "ct_role_permission_insert"),
    // ct_application
    Application(Constants.DAO_NAME_SPACE_CORE
            + "ct_select_application", Constants.DAO_NAME_SPACE_CORE
            + "ct_application_update", Constants.DAO_NAME_SPACE_CORE
            + "ct_application_insert"),
    // ct_Module
    Module(Constants.DAO_NAME_SPACE_CORE
            + "ct_select_module", Constants.DAO_NAME_SPACE_CORE
            + "ct_module_update", Constants.DAO_NAME_SPACE_CORE
            + "ct_module_insert"),
    // ct_Controller
    Controller(Constants.DAO_NAME_SPACE_CORE
            + "ct_user_select_all_controller", Constants.DAO_NAME_SPACE_CORE
            + "ct_controller_update", Constants.DAO_NAME_SPACE_CORE
            + "ct_controller_insert"),
    // ct_Action
    Action(Constants.DAO_NAME_SPACE_CORE
            + "ct_select_all_action", Constants.DAO_NAME_SPACE_CORE
            + "ct_action_update", Constants.DAO_NAME_SPACE_CORE
            + "ct_action_insert");

    private String sql_select;
    private String sql_update;
    private String sql_insert;

    private UserEditEnum(String sql_select, String sql_update, String sql_insert) {
        this.sql_select = sql_select;
        this.sql_update = sql_update;
        this.sql_insert = sql_insert;
    }

    /**
     * 获取查询的sql
     *
     * @return
     */
    public String getSql_select() {
        return sql_select;
    }

    public void setSql_select(String sql_select) {
        this.sql_select = sql_select;
    }

    /**
     * 获取更新的sql
     *
     * @return
     */
    public String getSql_update() {
        return sql_update;
    }

    public void setSql_update(String sql_update) {
        this.sql_update = sql_update;
    }

    /**
     * 获取插入的sql
     *
     * @return
     */
    public String getSql_insert() {
        return sql_insert;
    }

    public void setSql_insert(String sql_insert) {
        this.sql_insert = sql_insert;
    }

}
