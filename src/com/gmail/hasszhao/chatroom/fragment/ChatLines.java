package com.gmail.hasszhao.chatroom.fragment;

import static com.gmail.hasszhao.chatroom.GCMIntentService.ACTION_MESSAGE_COMING;

import java.io.InputStream;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gmail.hasszhao.chatroom.API;
import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.Util;
import com.gmail.hasszhao.chatroom.activities.ChatRoom;
import com.gmail.hasszhao.chatroom.activities.ChatRoom.OnLoadHistoryListener;
import com.gmail.hasszhao.chatroom.dataset.ChatContext;
import com.gmail.hasszhao.chatroom.dataset.ChatDataLines;
import com.gmail.hasszhao.chatroom.dataset.ChatLine;
import com.gmail.hasszhao.chatroom.dataset.ChatText;
import com.gmail.hasszhao.chatroom.dataset.ChatTexts;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

public final class ChatLines extends Fragment implements OnLoadHistoryListener {
    private BroadcastReceiver mMsgCom       = new BroadcastReceiver() {
                                                @Override
                                                public void onReceive( final Context _context, Intent _intent ) {
                                                    onSendLine( _intent.getStringExtra( "sender" ), _intent.getStringExtra( "msg" ) );
                                                }
                                            };
    private IntentFilter      mFilterMsgCom = new IntentFilter( ACTION_MESSAGE_COMING );

    private void onSendLine( String _sender, String _line ) {
        try {
            Context cxt = getActivity().getApplicationContext();
            if( !TextUtils.isEmpty( _line ) ) {
                ChatContext.getInstance( cxt ).getLines().addLine( new ChatLine( _sender, _line ) );
                onAddLine( ChatContext.getInstance( cxt ).getLines() );
            }
            cxt = null;
        }
        catch( Exception _e ) {
            Log.e( ChatRoom.TAG, _e.getMessage() );
        }
        finally {
        }
    }

    private void onSendLine( ChatText _ct ) {
        onSendLine( _ct.getSender(), _ct.getText() );
    }

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_lines, _container, false );
    }

    @Override
    public void onAttach( Activity _activity ) {
        super.onAttach( _activity );
        // I am sure!
        ChatRoom chatRoom = (ChatRoom) _activity;
        chatRoom.registerReceiver( mMsgCom, mFilterMsgCom );
        chatRoom.setOnLoadHistoryListener( this );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // I am sure!
        ChatRoom chatRoom = (ChatRoom) getActivity();
        chatRoom.unregisterReceiver( mMsgCom );
        chatRoom.setOnLoadHistoryListener( null );
    }

    public void onAddLine( ChatDataLines _lines ) {
        View v = getView();
        TextView tvLines = (TextView) v.findViewById( R.id.tv_chat_lines );
        tvLines.setText( "" );
        tvLines.setText( _lines.toString() );

        ScrollView svLines = (ScrollView) v.findViewById( R.id.sv_chat_lines );
        svLines.scrollTo( 0, tvLines.getBottom() );
        svLines = null;
        tvLines = null;
    }

    private void notifyServer( final Context _cxt ) {
        Util.Connector conn = new Util.Connector( _cxt ) {

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
                    final ChatTexts cts = gson.fromJson( json, ChatTexts.class );
                    getActivity().runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            ChatText[] data = cts.getTexts();
                            if( data != null && data.length > 0 ) {
                                for( ChatText ct : data ) {
                                    onSendLine( ct );
                                }
                            }
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
        conn.submit( API.HISTORY );
        conn = null;
    }

    @Override
    public void onLoadHistory() {
        notifyServer( getActivity().getApplicationContext() );
    }
}
