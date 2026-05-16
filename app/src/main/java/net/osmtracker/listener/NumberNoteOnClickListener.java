package net.osmtracker.listener;

import android.view.View;
import android.view.View.OnClickListener;

import net.osmtracker.activity.TrackLogger;

public class NumberNoteOnClickListener implements OnClickListener {
    private TrackLogger tl;
    private String tag;

    public NumberNoteOnClickListener(TrackLogger trackLogger) {
        tag = null;
        tl = trackLogger;
    }

    @Override
    public void onClick(final View v) {
        if (tag == null)
            return;
        tl.setTagDialogNumberInput(tag);
        tl.showDialog(TrackLogger.DIALOG_NUMBER_INPUT);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
