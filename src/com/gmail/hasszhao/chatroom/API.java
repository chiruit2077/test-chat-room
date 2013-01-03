package com.gmail.hasszhao.chatroom;

public interface API {
    public static final long   TIME_OUT = 10 * 1000;
    public static final String HOST     = "http://chatroomer.appspot.com";
    public static final String REG      = HOST + "/msgreg";
    public static final String UNREG    = HOST + "/msgunreg";
    public static final String SEND     = HOST + "/msgcm";
}
