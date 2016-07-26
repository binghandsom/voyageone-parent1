package com.voyageone.components.jumei.bean;

/**
 * Created by Ethan Shi on 2016/6/21.
 *
 * @author Ethan Shi
 * @version 2.1.0
 *
 */
public class ServerErrorException extends  Exception {

    public  ServerErrorException(String msg)
    {
        super(msg);
    }
}
