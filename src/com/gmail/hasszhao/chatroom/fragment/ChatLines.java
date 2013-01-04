package com.gmail.hasszhao.chatroom.fragment;

import static com.gmail.hasszhao.chatroom.GCMIntentService.ACTION_MESSAGE_COMING;
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

import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.activities.ChatRoom;
import com.gmail.hasszhao.chatroom.dataset.ChatContext;
import com.gmail.hasszhao.chatroom.dataset.ChatDataLines;
import com.gmail.hasszhao.chatroom.dataset.ChatLine;

public final class ChatLines extends Fragment {
    private BroadcastReceiver mMsgCom       = new BroadcastReceiver() {
                                                @Override
                                                public void onReceive( final Context _context, Intent _intent ) {
                                                    onSendLine( _intent.getStringExtra( "sender" ), _intent.getStringExtra( "msg" ) );
                                                }
                                            };
    private IntentFilter      mFilterMsgCom = new IntentFilter( ACTION_MESSAGE_COMING );

    public void onSendLine( String _sender, String _line ) {
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

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_lines, _container, false );
    }

    @Override
    public void onAttach( Activity _activity ) {
        super.onAttach( _activity );
        // I am sure!
        _activity.registerReceiver( mMsgCom, mFilterMsgCom );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver( mMsgCom );
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
}
