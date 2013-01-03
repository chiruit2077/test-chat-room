package com.gmail.hasszhao.chatroom.dataset;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.gmail.hasszhao.chatroom.ChatRoom;

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

    @Override
    public String toString() {
        StringBuilder outputBuilder = new StringBuilder();
        for( ChatLine line : mLines ) {
            outputBuilder
                    .append( String.format( ChatContext.getInstance().getTemplate(), line.getName(), line.toString() ) )
                    .append( '\n' );
        }
        return outputBuilder.toString();
    }
}
