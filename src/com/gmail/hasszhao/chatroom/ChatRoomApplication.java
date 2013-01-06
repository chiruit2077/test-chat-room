package com.gmail.hasszhao.chatroom;

import android.app.Application;
import android.view.ViewConfiguration;

public class ChatRoomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        try {
            setForceOverflow();
        }
        catch( Exception _e ) {
        }
        finally {
        }
    }

    public void setForceOverflow() throws NoSuchFieldException, IllegalAccessException {
        ViewConfiguration config = ViewConfiguration.get( this );
        java.lang.reflect.Field menuKeyField = ViewConfiguration.class.getDeclaredField( "sHasPermanentMenuKey" );
        if( menuKeyField != null ) {
            menuKeyField.setAccessible( true );
            menuKeyField.setBoolean( config, false );
        }
    }
}
