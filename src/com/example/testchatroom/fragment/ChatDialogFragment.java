package com.example.testchatroom.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.testchatroom.R;

public class ChatDialogFragment extends DialogFragment {

    @Override
    public void onCreate( Bundle _savedInstanceState ) {
        super.onCreate( _savedInstanceState );
        setCancelable( true );
        setStyle( DialogFragment.STYLE_NO_TITLE, R.style.Theme_Dialog_Translucent );
    }

}
