package com.example.testchatroom.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.testchatroom.ChatRoom;
import com.example.testchatroom.ChatRoom.OnAddLineListener;
import com.example.testchatroom.R;
import com.example.testchatroom.dataset.ChatDataLines;

public final class ChatLines extends Fragment implements OnAddLineListener {
    @Override
    public View onCreateView( LayoutInflater _inflater, ViewGroup _container, Bundle _savedInstanceState ) {
        return _inflater.inflate( R.layout.chat_lines, _container, false );
    }

    @Override
    public void onAttach( Activity _activity ) {
        super.onAttach( _activity );
        // I am sure!
        ChatRoom room = (ChatRoom) getActivity();
        room.setOnAddLineListener( this );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ChatRoom room = (ChatRoom) getActivity();
        room.setOnAddLineListener( null );
    }

    @Override
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
