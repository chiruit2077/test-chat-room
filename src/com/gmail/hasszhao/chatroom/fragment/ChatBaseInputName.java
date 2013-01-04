package com.gmail.hasszhao.chatroom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.hasszhao.chatroom.R;
import com.gmail.hasszhao.chatroom.dataset.ChatContext;

public abstract class ChatBaseInputName extends ChatBaseDialog implements OnClickListener {
    private OnInputName mOnInputName;

    public interface OnInputName {
        void onInputName();
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
            ChatContext.getInstance( getActivity().getApplicationContext() ).setUseName( tv.getText().toString().trim() );
            mOnInputName.onInputName();
        }
        doCommand();
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

    protected abstract void doCommand();

}