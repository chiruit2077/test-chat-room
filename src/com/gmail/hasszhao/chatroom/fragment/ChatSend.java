package com.gmail.hasszhao.chatroom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.hasszhao.chatroom.API;
import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.Util;
import com.gmail.hasszhao.chatroom.dataset.ChatContext;
import com.google.android.gcm.GCMRegistrar;

public final class ChatSend extends Fragment {
    public static final String KEY_YOUR_NAME = "Your name";

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
        btnSend = null;

        Bundle data = getArguments();
        String name = data.getString( KEY_YOUR_NAME );
        TextView tvYourName = (TextView) v.findViewById( R.id.tv_your_name );
        if( !TextUtils.isEmpty( name ) ) {
            tvYourName.setText( String.format( getString( R.string.chat_name_say ), name, "" ) );
        }
        data = null;
        name = null;
        tvYourName = null;
        v = null;
    }

    private void onSendMessage( String _msg ) {
        final TextView tvLines = (TextView) getView().findViewById( R.id.et_chat_text );
        tvLines.setText( "" );
        notifyServer( getActivity().getApplicationContext(), _msg );
    }

    private void notifyServer( final Context _cxt, final String _msg ) {
        Util.Connector conn = new Util.Connector( _cxt ) {
            @Override
            protected String onSetBody() {
                return _msg;
            }

            @Override
            protected String onCookie() {
                return "user=" + ChatContext.getInstance( _cxt ).getUseName() + ";regid=" + GCMRegistrar.getRegistrationId( _cxt );
            }

            @Override
            protected int onSetConnectTimeout() {
                return (int) API.TIME_OUT;
            }

            @Override
            protected void onConnectorInvalidConnect( Exception _e ) {
                super.onConnectorInvalidConnect( _e );
                onErr();
            }

            @Override
            protected void onConnectorError( int _status ) {
                super.onConnectorError( _status );
                onErr();
            }

            protected void onConnectorConnectTimout() {
                super.onConnectorConnectTimout();
                onErr();
            }

            private void onErr() {
            };
        };
        conn.submit( API.SEND );
        conn = null;
    }
}
