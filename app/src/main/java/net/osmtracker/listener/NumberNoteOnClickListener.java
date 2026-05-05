package net.osmtracker.listener;

import android.view.View;
import android.view.View.OnClickListener;

import net.osmtracker.activity.TrackLogger;

public class NumberNoteOnClickListener implements OnClickListener {
    private TrackLogger tl;

    public NumberNoteOnClickListener(TrackLogger trackLogger) {
        tl = trackLogger;
    }

    @Override
    public void onClick(final View v) {
        // let the TrackLogger activity open and control the dialog
        tl.showDialog(TrackLogger.DIALOG_NUMBER_INPUT);
    }
}
