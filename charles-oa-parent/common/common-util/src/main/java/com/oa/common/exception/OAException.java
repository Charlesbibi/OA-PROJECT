package com.oa.common.exception;

import org.junit.Test;

/**
 * @author Charles
 * @create 2023-05-09-0:36
 */
public class OAException extends Exception{
    private static final long serialVersionUID = 1L;

    public OAException(){}

    public OAException(String message){
        super(message);
    }
}
