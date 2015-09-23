package com.voyageone.batch.bi.util;

import com.voyageone.batch.bi.bean.formbean.FormUser;

/**
 * Created by Kylin on 2015/6/4.
 */
public class UtilCheckData {
    
	//private static final Log logger = LogFactory.getLog(UtilCheckData.class);

    /**
     * 检查店铺抽取数据用的用户和密码
     *
     * @param formUser
     * @return
     */
    public static boolean checkUser(FormUser formUser) {

        if (formUser.getUser_name() == null || "".equals(formUser.getUser_name().trim())) {
            return false;
        }

        if (formUser.getUser_ps() == null || "".equals(formUser.getUser_ps().trim())) {
            return false;
        }
        
        if (formUser.getDb_name() == null || "".equals(formUser.getDb_name().trim())) {
            return false;
        }
        return true;
    }

    /**
     * 设置该进程下driver，并返回。
     *
     * @return
     */
    public static FormUser getLocalUser() {
        //设置该进程下用户
        return DataThreadLocal.getUser();
    }

    /**
     * 设置该进程下用户，并返回。
     *
     * @param user
     * @return
     */
    public static void setLocalUser(FormUser user) {
        //设置该进程下用户
        DataThreadLocal.addUser(user);
    }

    /**
     * 检查设定项目是否为共同项目
     *
     * @param strColumn
     * @return
     */
    public static boolean checkColumn(String strColumn) {

        boolean boolColumn = true;

        if (strColumn.equals("shop_id")) {
            boolColumn = false;
        }

        if (strColumn.equals("num_iid")) {
            boolColumn = false;
        }

        return boolColumn;
    }

    public static boolean checkString(String strCheck){
        if (strCheck == null) {
            return false;
        }

        if ("".equals(strCheck.trim())) {
            return false;
        }

        return true;
    }

}
