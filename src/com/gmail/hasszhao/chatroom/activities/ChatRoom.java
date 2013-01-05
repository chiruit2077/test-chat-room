package com.gmail.hasszhao.chatroom.activities;

import static com.gmail.hasszhao.chatroom.GCMIntentService.ACTION_REGISTERED_ID;
import static com.gmail.hasszhao.chatroom.GCMIntentService.ACTION_UNREGISTERED_ID;

import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.gmail.hasszhao.chatroom.API;
import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.Util;
import com.gmail.hasszhao.chatroom.dataset.ChatContext;
import com.gmail.hasszhao.chatroom.fragment.ChatBaseDialog;
import com.gmail.hasszhao.chatroom.fragment.ChatBaseInputName.OnInputName;
import com.gmail.hasszhao.chatroom.fragment.ChatInputName;
import com.gmail.hasszhao.chatroom.fragment.ChatLines;
import com.gmail.hasszhao.chatroom.fragment.ChatReInputName;
import com.gmail.hasszhao.chatroom.fragment.ChatSend;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;

public final class ChatRoom extends SherlockFragmentActivity implements OnInputName {
    public static final String TAG              = "TEST-CHAT-ROOM";
    // public static final String KEY_RESTORED_LINES = "Restored Lines";
    private ProgressDialog     mIndicator;
    private BroadcastReceiver  mReg             = new BroadcastReceiver() {
                                                    @Override
                                                    public void onReceive( final Context _context, Intent _intent ) {
                                                        synchronized( ChatRoom.this ) {
                                                            while( !mCanNotifyServer ) {
                                                                try {
                                                                    ChatRoom.this.wait();
                                                                }
                                                                catch( InterruptedException _e ) {
                                                                }
                                                            }
                                                            notifyServer( false, _context, API.REG );
                                                        }
                                                    }
                                                };
    private IntentFilter       mFilterReg       = new IntentFilter( ACTION_REGISTERED_ID );

    private BroadcastReceiver  mUnreg           = new BroadcastReceiver() {
                                                    @Override
                                                    public void onReceive( Context _context, Intent _intent ) {
                                                        notifyServer( true, _context, API.UNREG );
                                                    }
                                                };
    private IntentFilter       mFilterUnreg     = new IntentFilter( ACTION_UNREGISTERED_ID );
    private volatile boolean   mCanNotifyServer = false;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.chat_room );
        registerReceiver( mReg, mFilterReg );
        registerReceiver( mUnreg, mFilterUnreg );
        registerPushOrInit();
    }

    private void registerPushOrInit() {
        Context cxt = getApplicationContext();
        if( !GCMRegistrar.isRegistered( cxt ) && GCMRegistrar.getRegistrationId( cxt ).equals( "" ) ) {
            ChatBaseDialog.showOneDialog( getSupportFragmentManager(), (DialogFragment) Fragment.instantiate( cxt, ChatInputName.class.getName() ) );
        } else {
            init( this );
            initFinished( getApplicationContext() );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver( mReg );
        unregisterReceiver( mUnreg );
    }

    @Override
    public void onInputName() {
        mIndicator = ProgressDialog.show( this, "", getString( R.string.chat_registering ) );
        init( this );
    }

    private static void init( ChatRoom _chatRoom ) {
        Context cxt = _chatRoom.getApplicationContext();
        FragmentManager fm = _chatRoom.getSupportFragmentManager();
        try {

            initChatLines( cxt, fm );
            initChatSend( cxt, fm );
            synchronized( _chatRoom ) {
                _chatRoom.mCanNotifyServer = true;
                _chatRoom.notify();
            }
        }
        catch( Exception _e ) {
            Log.e( TAG, _e.getMessage() );
        }
        finally {
            cxt = null;
            fm = null;
        }
    }

    private void initFinished( Context _context, ChatContext ccxt ) {
        ccxt.saveUseName();
    }

    private void initFinished( Context _context ) {
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

    private void exitChat() {
        try {
            mIndicator = ProgressDialog.show( this, "", getString( R.string.chat_unregistering ) );
            unregister();
        }
        catch( Exception _e ) {
            Log.e( TAG, "Error in exitChat: " + _e.getMessage() );
        }
        finally {
        }
    }

    private void terminate() {
        ChatContext.getInstance( getApplicationContext() ).clearAll();
        ChatRoom.this.finish();
    }

    public void unregister() {
        String regId = null;
        Context cxt = getApplicationContext();
        try {
            regId = GCMRegistrar.getRegistrationId( cxt );
            if( regId.equals( "" ) ) {
                Toast.makeText( getApplicationContext(), "Already been registered on server. Unregister first. ", Toast.LENGTH_LONG ).show();
            }
            else {
                // Device is already registered on GCM, check server.
                if( GCMRegistrar.isRegisteredOnServer( cxt ) ) {
                    GCMRegistrar.unregister( getApplicationContext() );
                }
            }
        }
        catch( Exception _e ) {
            Log.e( TAG, "Error in unregister: " + _e.getMessage() );
        }
        finally {
            regId = null;
            cxt = null;
        }
    }

    public void notifyServer( final boolean _isUnregistered, final Context _context, final String _api ) {
        Util.Connector conn = new Util.Connector( getApplicationContext() ) {
            @Override
            protected String onCookie() {
                return "user=" + ChatContext.getInstance( _context ).getUseName() + ";regid=" + GCMRegistrar.getRegistrationId( _context );
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
                mIndicator.dismiss();
                mIndicator = ProgressDialog.show( ChatRoom.this, "", getString( R.string.chat_retry ) );
                notifyServer( _isUnregistered, _context, _api );
            };

            @Override
            protected void onConnectorFinished() {
                mIndicator.dismiss();
                if( _isUnregistered ) {
                    terminate();
                }
            }

            /*
             * Read data here. The function runs in thread. To hook on UI thread use onConnectorFinished()
             */
            protected void onConnectorInputStream( InputStream _in ) {
                final String json = Util.streamToString( _in );
                Gson gson = new Gson();
                final com.gmail.hasszhao.chatroom.dataset.Status status = gson.fromJson( json, com.gmail.hasszhao.chatroom.dataset.Status.class );
                ChatRoom.this.runOnUiThread( new Runnable() {
                    @Override
                    public void run() {
                        ChatContext ccxt = ChatContext.getInstance( _context );
                        switch( status.getCode() )
                        {
                            case API.API_OK:
                                if( !_isUnregistered ) {
                                    initFinished( _context, ccxt );
                                }
                            break;
                            case API.API_DUPLICATED_NAME:
                                ccxt.setWrongUseName( ccxt.getUseName() );
                                ChatBaseDialog.showOneDialog( getSupportFragmentManager(), (DialogFragment) Fragment.instantiate( getApplicationContext(), ChatReInputName.class.getName() ) );
                                ccxt = null;
                            break;
                        }
                    }
                } );
                gson = null;
            }
        };
        conn.submit( _api );
        conn = null;
    }

    @Override
    public boolean onCreateOptionsMenu( Menu _menu ) {
        getSupportMenuInflater().inflate( R.menu.main, _menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem _item ) {
        switch( _item.getItemId() )
        {
            case R.id.menu_exit:
                exitChat();
            break;
        }
        return true;
    }
}