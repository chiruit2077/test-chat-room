package com.gmail.hasszhao.chatroom.fragment;

import java.io.InputStream;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.hasszhao.chatroom.API;
import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.Util;
import com.gmail.hasszhao.chatroom.activities.ChatRoom;
import com.gmail.hasszhao.chatroom.dataset.ChatContext;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

public final class ChatSend extends Fragment implements OnClickListener {
    private View     mSend;
    private View     mSending;
    private TextView mMsgLines;

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_send, _container, false );
    }

    @Override
    public void onActivityCreated( Bundle _savedInstanceState ) {
        super.onActivityCreated( _savedInstanceState );
        View v = getView();
        mMsgLines = (TextView) v.findViewById( R.id.et_chat_text );
        mSending = v.findViewById( R.id.pb_sending );
        mSend = (Button) v.findViewById( R.id.btn_send );
        mSend.setOnClickListener( this );

        String name = ChatContext.getInstance( getActivity().getApplicationContext() ).getUseName();
        TextView tvYourName = (TextView) v.findViewById( R.id.tv_your_name );
        if( !TextUtils.isEmpty( name ) ) {
            tvYourName.setText( String.format( getString( R.string.chat_name_say ), name, "" ) );
        }
        name = null;
        tvYourName = null;
        v = null;
    }

    private void onSendMessage( String _msg ) {
        if( !TextUtils.isEmpty( _msg ) ) {
            mSending.setVisibility( View.VISIBLE );
            mSend.setVisibility( View.GONE );
            notifyServer( getActivity().getApplicationContext(), _msg );
        }
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

            @Override
            protected void onConnectorInputStream( InputStream _in ) {
                final String json = Util.streamToString( _in );
                Gson gson = new Gson();
                try {
                    final com.gmail.hasszhao.chatroom.dataset.Status status = gson.fromJson( json, com.gmail.hasszhao.chatroom.dataset.Status.class );
                    getActivity().runOnUiThread( new Runnable() {

                        @Override
                        public void run() {
                            switch( status.getCode() )
                            {
                                case API.API_OK:
                                    mMsgLines.setText( "" );
                                break;
                                case API.API_ACTION_FAILED:
                                    mMsgLines.setSelectAllOnFocus( true );
                                break;
                            }
                            mSend.setVisibility( View.VISIBLE );
                            mSending.setVisibility( View.GONE );
                        }
                    } );
                }
                catch( Exception _e ) {
                    Log.e( ChatRoom.TAG, _e.getMessage() );
                }
                finally {
                    gson = null;
                }
            }
        };
        conn.submit( API.SEND );
        conn = null;
    }

    @Override
    public void onClick( View _v ) {
        switch( _v.getId() )
        {
            case R.id.btn_send:
                onSendMessage( mMsgLines.getText().toString().trim() );
            break;
        }
    }
}