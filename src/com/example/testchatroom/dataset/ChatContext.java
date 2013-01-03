package com.example.testchatroom.dataset;

import android.content.Context;

import com.example.testchatroom.R;

public final class ChatContext {
    private static ChatContext sInstance;
    private String             mUseName;
    private ChatDataLines      mLines = new ChatDataLines(); ;
    private String             mTemplate;

    private ChatContext() {
    }

    private ChatContext( Context _cxt ) {
        mTemplate = _cxt.getString( R.string.chat_name_said );
    }

    public static final ChatContext getInstance( Context _cxt ) {
        if( sInstance == null ) {
            sInstance = new ChatContext( _cxt );
        }
        return sInstance;
    }

    static final ChatContext getInstance() {
        return sInstance;
    }

    public String getUseName() {
        return mUseName;
    }

    public void setUseName( String _useName ) {
        mUseName = _useName;
    }

    public ChatDataLines getLines() {
        return mLines;
    }

    String getTemplate() {
        return mTemplate;
    }
}
