package com.example.testchatroom.fragment;

import static com.example.testchatroom.GCMIntentService.PRJ_ID;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testchatroom.ChatRoom;
import com.example.testchatroom.R;
import com.google.android.gcm.GCMRegistrar;

public final class ChatInputName extends ChatBaseDialog implements OnClickListener {
    private OnInputName mOnInputName;

    public interface OnInputName {
        void onInputName( String _name );
    }

    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_input_name, _container, false );
    }

    @Override
    public void onActivityCreated( Bundle _arg0 ) {
        super.onActivityCreated( _arg0 );
        View v = getView();
        Button btnSave = (Button) v.findViewById( R.id.btn_input_name );
        btnSave.setOnClickListener( this );
        btnSave = null;
        v = null;
    }

    @Override
    public void onAttach( Activity _activity ) {
        super.onAttach( _activity );
        // I am sure !
        mOnInputName = (OnInputName) _activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnInputName = null;
    }

    private void onInputName() {
        TextView tv = (TextView) getView().findViewById( R.id.et_your_name );
        if( mOnInputName != null ) {
            mOnInputName.onInputName( tv.getText().toString().trim() );
        }
        register();
        tv = null;
        dismiss();
    }

    @Override
    public void onClick( View _v ) {
        switch( _v.getId() )
        {
            case R.id.btn_input_name:
                onInputName();
            break;
        }
    }

    public void register() {
        String regId = null;
        Context cxt = getActivity().getApplicationContext();
        try {
            GCMRegistrar.checkDevice( cxt );
            GCMRegistrar.checkManifest( cxt );
            regId = GCMRegistrar.getRegistrationId( cxt );
            if( regId.equals( "" ) ) {
                GCMRegistrar.register( cxt, PRJ_ID );
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