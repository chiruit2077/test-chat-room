package com.example.testchatroom;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.testchatroom.dataset.ChatDataLines;
import com.example.testchatroom.dataset.ChatLine;
import com.example.testchatroom.fragment.ChatLines;
import com.example.testchatroom.fragment.ChatSend;
import com.example.testchatroom.fragment.ChatSend.OnChatLineListener;

public final class ChatRoom extends FragmentActivity implements OnChatLineListener {
    public static final String  TAG                = "TEST-CHAT-ROOM";
    private static final String DLG                = "TEST-CHAT-ROOM-DLG";
    public static final String  KEY_RESTORED_LINES = "Restored Lines";
    private ChatDataLines       mLines;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.chat_room );
        init( this );

    }

    @Override
    protected void onRestoreInstanceState( Bundle _savedInstanceState ) {
        super.onRestoreInstanceState( _savedInstanceState );
    }

    private static void init( FragmentActivity _activity ) {
        Context cxt = _activity.getApplicationContext();
        FragmentManager fm = _activity.getSupportFragmentManager();
        try {
            initChatLines( cxt, fm );
            initChatSend( cxt, fm );
        }
        catch( Exception _e ) {
            Log.e( TAG, _e.getMessage() );
        }
        finally {
            cxt = null;
            fm = null;
        }
    }

    private static void initChatLines( Context _cxt, FragmentManager _fm ) {
        FragmentTransaction trans = _fm.beginTransaction();
        trans.replace( R.id.chat_lines_container, Fragment.instantiate( _cxt, ChatLines.class.getName() ) );
        trans.commit();
    }

    private static void initChatSend( Context _cxt, FragmentManager _fm ) {
        FragmentTransaction trans = _fm.beginTransaction();
        trans.replace( R.id.chat_send_container, Fragment.instantiate( _cxt, ChatSend.class.getName() ) );
        trans.commit();
    }

    private void showDialog( DialogFragment _dlgFrg ) {
        if( _dlgFrg != null ) {
            DialogFragment dialogFragment = (DialogFragment) _dlgFrg;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            // Ensure that there's only one dialog to the user.
            Fragment prev = getSupportFragmentManager().findFragmentByTag( DLG );
            if( prev != null ) {
                ft.remove( prev );
            }
            try {
                dialogFragment.show( ft, DLG );
            }
            catch( Exception _e ) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitChat();
    }

    private void exitChat() {
        try {
        }
        catch( Exception _e ) {
            Log.e( TAG, "Error in exitChat: " + _e.getMessage() );
        }
        finally {
        }
    }

    @Override
    public void onSendLine( String _line ) {
        try {
            if( !TextUtils.isEmpty( _line ) ) {
                Toast.makeText( getApplicationContext(), _line, Toast.LENGTH_LONG ).show();
                mLines.addLine( new ChatLine( _line ) );
            }
        }
        catch( Exception _e ) {
            Log.e( TAG, _e.getMessage() );
        }
        finally {
        }
    }

}
