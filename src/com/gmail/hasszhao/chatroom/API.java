package com.gmail.hasszhao.chatroom;

public interface API {
    public static final long   TIME_OUT            = 10 * 1000;
    public static final String HOST                = "http://chatroomer.appspot.com";
    public static final String REG                 = HOST + "/msgreg";
    public static final String UNREG               = HOST + "/msgunreg";
    public static final String SEND                = HOST + "/msgcm";
    public static final int    API_OK              = 900;
    public static final int    API_DUPLICATED_NAME = 901;
    public static final int    API_ACTION_FAILED   = 902;
}
