package com.gmail.hasszhao.chatroom.dataset;

public final class Status {

    private int    Code;
    private String Message;
    private String Data;

    public Status( int _code, String _message, String _data ) {
        super();
        Code = _code;
        Message = _message;
        Data = _data;
    }

    public int getCode() {
        return Code;
    }

    public String getMessage() {
        return Message;
    }

    public String getData() {
        return Data;
    }
}
