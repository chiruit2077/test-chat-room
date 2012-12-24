package com.example.testchatroom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.testchatroom.R;

public final class ChatSend extends Fragment {
    private OnChatLineListener mOnSend;

    public interface OnChatLineListener {
        void onSendLine( String _line );
    }

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_send, _container, false );
    }

    @Override
    public void onActivityCreated( Bundle _savedInstanceState ) {
        super.onActivityCreated( _savedInstanceState );
        View v = getView();
        final TextView tvLines = (TextView) v.findViewById( R.id.et_chat_text );
        Button btnSend = (Button) v.findViewById( R.id.btn_send );
        btnSend.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick( View _v ) {
                onSendMessage( tvLines.getText().toString().trim() );
            }
        } );
        v = null;
        btnSend = null;
    }

    private void onSendMessage( String _msg ) {
        if( mOnSend != null ) {
            mOnSend.onSendLine( _msg );
        }
    }

    @Override
    public void onAttach( Activity _activity ) {
        super.onAttach( _activity );
        // don't use try-catch , I am sure.
        mOnSend = (OnChatLineListener) _activity;
    }

    @Override
    public void onDetach() {
        mOnSend = null;
    }
}
