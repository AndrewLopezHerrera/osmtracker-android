package net.osmtracker.listener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import net.osmtracker.activity.TrackLogger;

import java.util.List;

public class SpinnerNoteOnClickListener implements OnClickListener {
    private TrackLogger tl;
    private List<String> tempOptions;
    private String tempTag;

    public SpinnerNoteOnClickListener(TrackLogger trackLogger) {
        tl = trackLogger;
        tempOptions = null;
        tempTag = null;
    }

    @Override
    public void onClick(final View v) {
        // let the TrackLogger activity open and control the dialog
        if(tempOptions == null  || tempTag == null)
            return;
        tl.setTagDialogSpinnerInput(tempTag);
        tl.setTempSpinnerOptions(tempOptions);
        tl.showDialog(TrackLogger.DIALOG_SPINNER_INPUT);
    }

    public void setOption(List<String> options){
        tempOptions = options;
    }

    public void setTag(String tag){
        tempTag = tag;
    }
}
