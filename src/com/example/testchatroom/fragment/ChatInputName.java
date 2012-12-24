package com.example.testchatroom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.testchatroom.R;

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
        tv = null;
        this.dismiss();
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
}