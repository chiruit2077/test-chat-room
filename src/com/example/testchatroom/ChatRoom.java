package com.example.testchatroom;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChatRoom extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.chat_room );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.chat_room, menu );
        return true;
    }

}
