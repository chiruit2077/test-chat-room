package com.gmail.hasszhao.chatroom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gmail.hasszhao.chatroom.API;
import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.Util;
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
        View v = getView();
        Context cxt = getActivity().getApplicationContext();
        String wrongName = ChatContext.getInstance( cxt ).getWrongUseName();
        final TextView tvDupName = (TextView) v.findViewById( R.id.tv_duplicated_name );
        tvDupName.setText( String.format( cxt.getString( R.string.chat_duplicated_name ), wrongName ) );
        final EditText tvSuggestedName = (EditText) v.findViewById( R.id.et_your_name );
        String[] randoms = Util.generateRandomWords( wrongName.length() / 2 );
        if( randoms != null ) {
            tvSuggestedName.setText( new StringBuilder().append( wrongName ).append( randoms[0] ) );
        } else {
            tvSuggestedName.setText( new StringBuilder().append( wrongName ).append( "a" ) );
        }
        cxt = null;
        wrongName = null;
        getDialog().setTitle( getString( R.string.chat_retry ) );
    }

    @Override
    protected void doCommand() {
        ChatRoom cr = (ChatRoom) getActivity();
        cr.notifyServer( false, cr.getApplicationContext(), API.REG );
        cr = null;
    }
}