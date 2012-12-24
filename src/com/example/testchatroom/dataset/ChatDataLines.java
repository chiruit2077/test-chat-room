package com.example.testchatroom.dataset;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.testchatroom.ChatRoom;

public final class ChatDataLines {
    private List<ChatLine> mLines = new ArrayList<ChatLine>();

    public void addLine( ChatLine _line ) {
        try {
            mLines.add( _line );
        }
        catch( Exception _e ) {
            Log.e( ChatRoom.TAG, "Error in addLine: " + _e.getMessage() );
        }
        finally {
        }
    }
}
