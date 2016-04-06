package com.voyageone.web2.base;

import com.voyageone.common.Constants;

import static java.lang.String.format;

/**
 * 为前台程序的 Service 层提供基类
 * Created by Jonas on 15/6/26.
 * @author Jonas
 */
public abstract class BaseAppService extends BaseAppComponent {

    /**
     * 预先定义好长度的缩进字符串。辅助缩进
     */

    private final static String SPACE1 = "   ";

    private final static String SPACE2 = "      ";

    private final static String SPACE3 = "         ";

    private final static String SPACE4 = "            ";

    /**
     * 辅助生成制定长度的缩进空格
     */
    private String getInfoSpace(int lev) {
        if (lev < 1)
            return Constants.EmptyString;

        switch (lev) {
            case 1:
                return SPACE1;
            case 2:
                return SPACE2;
            case 3:
                return SPACE3;
            case 4:
                return SPACE4;
            default:
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < lev; i++) builder.append(SPACE1);
                return builder.toString();
        }
    }
}
