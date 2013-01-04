package com.gmail.hasszhao.chatroom.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.activities.ChatRoom;

public final class ChatLoadHistory extends ChatBaseDialog {
    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_load_history, _container, false );
    }

    @Override
    public void onActivityCreated( Bundle _arg0 ) {
        super.onActivityCreated( _arg0 );
        setCancelable( true );
        View v = getView();
        Button btn = (Button) v.findViewById( R.id.btn_load_history );
        btn.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View _v ) {
                // I am sure!
                ChatRoom cr = (ChatRoom) getActivity();
                cr.loadHistory();
                dismiss();
            }
        } );
        btn = null;
        getDialog().setTitle( R.string.chat_smile );
        TextView tv = (TextView) v.findViewById( R.id.tv_load_history );
        tv.setText( R.string.chat_load_history );
        tv = null;

        v = null;
    }
}
