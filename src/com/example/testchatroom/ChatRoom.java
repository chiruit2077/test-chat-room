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

import com.example.testchatroom.dataset.ChatDataLines;
import com.example.testchatroom.dataset.ChatLine;
import com.example.testchatroom.fragment.ChatBaseDialog;
import com.example.testchatroom.fragment.ChatInputName;
import com.example.testchatroom.fragment.ChatInputName.OnInputName;
import com.example.testchatroom.fragment.ChatLines;
import com.example.testchatroom.fragment.ChatSend;
import com.example.testchatroom.fragment.ChatSend.OnChatLineSentListener;

public final class ChatRoom extends FragmentActivity implements OnChatLineSentListener, OnInputName {
    public static final String TAG    = "TEST-CHAT-ROOM";
    // public static final String KEY_RESTORED_LINES = "Restored Lines";
    private ChatDataLines      mLines = new ChatDataLines();

    public interface OnAddLineListener {
        void onAddLine( ChatDataLines _lines );
    }

    private OnAddLineListener mOnAddLine;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.chat_room );
        ChatBaseDialog.showOneDialog( getSupportFragmentManager(), (DialogFragment) Fragment.instantiate( getApplicationContext(), ChatInputName.class.getName() ) );
    }

    @Override
    public void onInputName( String _name ) {
        init( this, _name );
    }

    private static void init( FragmentActivity _activity, String _name ) {
        Context cxt = _activity.getApplicationContext();
        FragmentManager fm = _activity.getSupportFragmentManager();
        try {
            initChatLines( cxt, fm );
            initChatSend( cxt, fm, _name );
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

    private static void initChatSend( Context _cxt, FragmentManager _fm, String _name ) {
        Bundle args = new Bundle();
        args.putString( ChatSend.KEY_YOUR_NAME, _name );
        FragmentTransaction trans = _fm.beginTransaction();
        trans.replace( R.id.chat_send_container, Fragment.instantiate( _cxt, ChatSend.class.getName(), args ) );
        trans.commit();
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
                mLines.addLine( new ChatLine( getString( R.string.chat_from_me ), _line ) );
                OnAddLine();
            }
        }
        catch( Exception _e ) {
            Log.e( TAG, _e.getMessage() );
        }
        finally {
        }
    }

    private void OnAddLine() {
        if( mOnAddLine != null ) {
            mOnAddLine.onAddLine( mLines );
        }
    }

    public void setOnAddLineListener( OnAddLineListener _onAddLine ) {
        mOnAddLine = _onAddLine;
    }
}
