package com.gmail.hasszhao.chatroom.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.gmail.hasszhao.chatroom.GCMIntentService;
import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.Util;
import com.gmail.hasszhao.chatroom.activities.ChatRoom;
import com.google.android.gcm.GCMRegistrar;

public final class ChatInputName extends ChatBaseInputName implements OnClickListener {
    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_input_name, _container, false );
    }

    @Override
    public void onActivityCreated( Bundle _arg0 ) {
        super.onActivityCreated( _arg0 );
        getDialog().setTitle( getString( R.string.chat_plz_register ) );
        final EditText yourName = (EditText) getView().findViewById( R.id.et_your_name );
        yourName.setText( Util.getGmailNameBeforeAt( getActivity().getApplicationContext() ) );
    }

    @Override
    protected void doCommand() {
        register();
    }

    public void register() {
        String regId = null;
        Context cxt = getActivity().getApplicationContext();
        try {
            GCMRegistrar.checkDevice( cxt );
            GCMRegistrar.checkManifest( cxt );
            regId = GCMRegistrar.getRegistrationId( cxt );
            if( regId.equals( "" ) ) {
                GCMRegistrar.register( cxt, GCMIntentService.PRJ_ID );
            }
            else {
                // Device is already registered on GCM, check server.
                if( GCMRegistrar.isRegisteredOnServer( cxt ) ) {
                    Toast.makeText( cxt, "Already been registered on server. Unregister first. ", Toast.LENGTH_LONG ).show();
                }
            }
        }
        catch( Exception _e ) {
            Log.e( ChatRoom.TAG, "Error in register: " + _e.getMessage() );
        }
        finally {
            regId = null;
            cxt = null;
        }
    }
}