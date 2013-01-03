package com.gmail.hasszhao.chatroom.dataset;

public final class ChatLine {
    // private byte[] data;
    private String mName;
    private String mText;

    public ChatLine( String _name, String _line ) {
        super();
        // data = _line.getBytes();
        mName = _name;
        mText = _line;
    }

    // public byte[] getData() {
    // return data;
    // }

    @Override
    public String toString() {
        // String.valueOf( data );
        return mText;
    }

    public String getName() {
        return mName;
    }
}
