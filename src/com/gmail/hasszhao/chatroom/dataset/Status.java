package com.gmail.hasszhao.chatroom.dataset;

public final class Status {
    private int     Code;
    private String  Message;
    private boolean Retry;

    public Status( int _code, String _message, boolean _retry ) {
        super();
        Code = _code;
        Message = _message;
        Retry = _retry;
    }

    public int getCode() {
        return Code;
    }

    public String getMessage() {
        return Message;
    }

    public boolean isRetry() {
        return Retry;
    }
}
