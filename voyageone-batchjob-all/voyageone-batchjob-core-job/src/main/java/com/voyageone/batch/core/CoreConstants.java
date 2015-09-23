package com.voyageone.batch.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface CoreConstants {

    /**
     * 从库状态
     */
    final class SlaveStatus {
        //可用
        public final static String  YES = "Yes";
    }

    /**
     * 从库状态取得错误时邮件（CheckSlaveStatusJob）
     */
    final class EmailCheckSlaveStatusError {

        // 表格式
        public final static String TABLE = "<div>"
                + "<table><tr>"
                + "<th>Slave_IO_Running</th><th>Slave_IO_State</th><th>Slave_SQL_Running</th><th>Slave_SQL_Running_State</th><th>Last_Errno</th><th>Last_Error</th>"
                + "</tr>%s</table></div>";
        // 行格式
        public final static String ROW = "<tr>"
                + "<td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td>"
                + "</tr>";
        // 邮件名
        public final static String SUBJECT = "[Core] 从库同步出现问题";
        // 概要说明
        public final static String HEAD = "<font color='red'>从库同步出现问题，请确认</font>";
        // 错误内容
        public final static String EMPTY = "<div>从库同步状态取得为空，无法判断是否同步成功</div>";
        // 错误内容
        public final static String ERROR = "<div>从库同步状态取得失败</br>：%s</div>";

    }

}
