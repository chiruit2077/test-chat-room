package com.example.testchatroom;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

public class GCMIntentService extends GCMBaseIntentService {
    public static final String PRJ_ID                 = "740651081890";
    public static final String ACTION_REGISTERED_ID   = "action.registered.id";
    public static final String ACTION_UNREGISTERED_ID = "action.unregistered.id";
    public static final String ACTION_MESSAGE_COMING  = "action.message_coming";
    private static final int   NOTIFY_REGISTERED      = 0;

    private BroadcastReceiver  mUnReg                 = new BroadcastReceiver() {
                                                          @Override
                                                          public void onReceive( final Context _context, Intent _intent ) {
                                                              Toast.makeText( _context, "unreg", Toast.LENGTH_LONG ).show();
                                                              mConn = new Util.Connector( getApplicationContext() ) {
                                                                  @Override
                                                                  protected String onCookie() {
                                                                      return "user=" + ChatRoom.sUserName + ";regid=" + GCMRegistrar.getRegistrationId( _context );
                                                                  }

                                                                  @Override
                                                                  protected int onSetConnectTimeout() {
                                                                      return 10 * 1000;
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
                                                              mConn.submit( API.UNREG );
                                                          }
                                                      };
    private IntentFilter       mFilterUnreg           = new IntentFilter( ACTION_UNREGISTERED_ID );

    private Util.Connector     mConn;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver( mUnReg );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver( mUnReg, mFilterUnreg );
    }

    public GCMIntentService() {
        super( PRJ_ID );
    }

    @Override
    protected void onError( Context _arg0, String _arg1 ) {
        Log.e( GCMIntentService.class.getName(), _arg1 );
    }

    @Override
    protected void onMessage( Context _arg0, Intent _arg1 ) {
        // notify( _arg0, ACTION_MESSAGE_COMING, _arg1.getStringExtra( "title" ), _arg1.getStringExtra( "content" ) );
    }

    @Override
    protected void onRegistered( Context _arg0, String _arg1 ) {
        notify( _arg0, getString( R.string.chat_registered ) );
        GCMRegistrar.setRegisteredOnServer( _arg0, true );
        sendBroadcast( new Intent( ACTION_REGISTERED_ID ) );
    }

    @Override
    protected void onUnregistered( Context _arg0, String _arg1 ) {
        notify( _arg0, getString( R.string.chat_unregistered ) );
        sendBroadcast( new Intent( ACTION_UNREGISTERED_ID ) );
        GCMRegistrar.setRegisteredOnServer( _arg0, false );
    }

    private static void notify( Context _context, String _title ) {
        NotificationManager notificationManager = null;
        Notification notification = null;
        try {
            notificationManager = (NotificationManager) _context.getSystemService( Context.NOTIFICATION_SERVICE );
            notification = new NotificationCompat.Builder( _context )
                    .setWhen( System.currentTimeMillis() )
                    .setTicker( _title )
                    .setContentTitle( _title )
                    .setAutoCancel( true )
                    .setOnlyAlertOnce( true )
                    .setSmallIcon( R.drawable.ic_launcher )
                    .build();
            notificationManager.notify( NOTIFY_REGISTERED, notification );
        }
        catch( Exception _e ) {
            Log.e( TAG, "Error in notifyRegisteredID: " + _e.getMessage() );
        }
        finally {
            notificationManager = null;
            notification = null;
        }
    }

    // private static void notify( Context _context, String _action, String _title, String _content ) {
    // PendingIntent pIntent = null;
    // Intent intent = null;
    // NotificationManager notificationManager = null;
    // Notification notification = null;
    // try {
    // intent = getIntent( _context, _action, _title, _content );
    // intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP );
    // pIntent = PendingIntent.getActivity( _context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );// Important to add last flag, otherwise the extras data will not be changed in intent received by Activty.
    // notificationManager = (NotificationManager) _context.getSystemService( Context.NOTIFICATION_SERVICE );
    // notification = new NotificationCompat.Builder( _context )
    // .setWhen( System.currentTimeMillis() )
    // .setTicker( _title )
    // .setContentTitle( _title )
    // .setContentText( _content )
    // .setAutoCancel( true )
    // .setContentIntent( pIntent )
    // .setSmallIcon( R.drawable.ic_launcher )
    // .build();
    // notificationManager.notify( NOTIFY_REGISTERED, notification );
    // }
    // catch( Exception _e ) {
    // Log.e( TAG, "Error in notifyRegisteredID: " + _e.getMessage() );
    // }
    // finally {
    // pIntent = null;
    // intent = null;
    // notificationManager = null;
    // notification = null;
    // }
    // }
    //
    // private static void openView( Context _context, String _action, String _title, String _content ) {
    // Intent intent = null;
    // try {
    // intent = getIntent( _context, _action, _title, _content );
    // intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
    // _context.startActivity( intent );
    // }
    // catch( Exception _e ) {
    // Log.e( TAG, "Error in openView: " + _e.getMessage() );
    // }
    // finally {
    // intent = null;
    // }
    // }
    //
    // private static Intent getIntent( Context _context, String _action, String _title, String _content ) {
    // Intent intent = null;
    // try {
    // intent = new Intent( _context, MainActivity.class );
    // intent.setAction( _action );
    // intent.putExtra( "title", _title );
    // intent.putExtra( "content", _content );
    // }
    // catch( Exception _e ) {
    // Log.e( TAG, "Error in getIntent: " + _e.getMessage() );
    // }
    // finally {
    // }
    // return intent;
    // }
}