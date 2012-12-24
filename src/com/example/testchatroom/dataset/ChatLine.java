package com.example.testchatroom.dataset;

public final class ChatLine {
    private byte[] data;

    public ChatLine( String _line ) {
        super();
        data = _line.getBytes();
    }

    public byte[] getData() {
        return data;
    }

}
