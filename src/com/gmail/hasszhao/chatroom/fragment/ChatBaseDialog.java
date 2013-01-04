package com.gmail.hasszhao.chatroom.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockDialogFragment;

public abstract class ChatBaseDialog extends SherlockDialogFragment {
    private static final String DLG = "TEST-CHAT-ROOM-DLG";

    @Override
    public void onCreate( Bundle _savedInstanceState ) {
        super.onCreate( _savedInstanceState );
        setCancelable( false );
    }

    /*
     * Show only one dialog on the UI, the old one will be dismissed.
     */
    public static void showOneDialog( FragmentManager _mgr, DialogFragment _dlgFrg ) {
        if( _dlgFrg != null ) {
            DialogFragment dialogFragment = (DialogFragment) _dlgFrg;
            FragmentTransaction ft = _mgr.beginTransaction();
            // Ensure that there's only one dialog to the user.
            Fragment prev = _mgr.findFragmentByTag( DLG );
            if( prev != null ) {
                ft.remove( prev );
            }
            try {
                dialogFragment.show( ft, DLG );
            }
            catch( Exception _e ) {
            }
        }
    }
}
