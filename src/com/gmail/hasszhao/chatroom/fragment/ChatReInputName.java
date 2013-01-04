package com.gmail.hasszhao.chatroom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.hasszhao.chatroom.API;
import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.activities.ChatRoom;
import com.gmail.hasszhao.chatroom.dataset.ChatContext;

public final class ChatReInputName extends ChatBaseInputName implements OnClickListener {
    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_reinput_name, _container, false );
    }

    @Override
    public void onActivityCreated( Bundle _arg0 ) {
        super.onActivityCreated( _arg0 );
        Context cxt = getActivity().getApplicationContext();
        final TextView tvDupName = (TextView) getView().findViewById( R.id.tv_duplicated_name );
        tvDupName.setText( String.format( cxt.getString( R.string.chat_duplicated_name ), ChatContext.getInstance( cxt ).getWrongUseName() ) );
        cxt = null;
        getDialog().setTitle( getString( R.string.chat_retry ) );
    }

    @Override
    protected void doCommand() {
        ChatRoom cr = (ChatRoom) getActivity();
        cr.notifyServer( false, cr.getApplicationContext(), API.REG );
        cr = null;
    }
}