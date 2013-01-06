package com.gmail.hasszhao.chatroom.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gmail.hasszhao.chatroom.R;

public class ChatSignal extends ChatBaseDialog {

    @Override
    public void onCreate( Bundle _savedInstanceState ) {
        super.onCreate( _savedInstanceState );
        setStyle( DialogFragment.STYLE_NO_TITLE, R.style.Theme_Dialog_Translucent );
    }

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_signal, _container, false );
    }

}
