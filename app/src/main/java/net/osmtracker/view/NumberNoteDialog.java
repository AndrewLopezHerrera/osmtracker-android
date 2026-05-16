package net.osmtracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import net.osmtracker.OSMTracker;
import net.osmtracker.R;
import net.osmtracker.db.TrackContentProvider;

import java.util.UUID;

public class NumberNoteDialog extends AlertDialog {

    private static final String KEY_INPUT_TEXT = "INPUT_TEXT";
    private static final String KEY_WAYPOINT_UUID = "WAYPOINT_UUID";
    private static final String KEY_NOTE_UUID = "NOTE_UUID";
    private static final String KEY_TRACK_ID = "TRACK_ID";

    private EditText input;
    private String wayPointUuid;
    private String noteUuid;
    private long trackId;

    private boolean saveAsWayPoint, saveAsNote;
    private Context context;

    private String tag;

    public NumberNoteDialog(Context context, long trackId) {
        super(context);
        this.context = context;
        this.trackId = trackId;

        input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("0.0");

        tag = "Empty";

        this.setTitle(tag);
        this.setCancelable(true);
        this.setView(input);
        this.setButton(DialogInterface.BUTTON_POSITIVE,
                context.getString(android.R.string.ok),
                (dialog, which) -> {
                    String noteText = tag + ": " + input.getText().toString();

                    if (saveAsWayPoint) {
                        sendUpdateIntent(OSMTracker.INTENT_UPDATE_WP, wayPointUuid, noteText);
                    }

                    if (saveAsNote) {
                        sendUpdateIntent(OSMTracker.INTENT_UPDATE_NOTE, noteUuid, noteText);
                    }
                });
        this.setButton(DialogInterface.BUTTON_NEGATIVE,
                context.getString(android.R.string.cancel),
                (dialog, which) -> dialog.cancel());
        this.setOnCancelListener(dialog -> {
            if (wayPointUuid != null) {
                Intent intent = new Intent(OSMTracker.INTENT_DELETE_WP);
                intent.putExtra(OSMTracker.INTENT_KEY_UUID, wayPointUuid);
                intent.setPackage(getContext().getPackageName());
                context.sendBroadcast(intent);
            }
        });
    }

    private void sendUpdateIntent(String action, String uuid, String value) {
        Intent intent = new Intent(action);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, trackId);
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, value);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, uuid);
        intent.setPackage(getContext().getPackageName());
        context.sendBroadcast(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prefSaveAs = prefs.getString(
                OSMTracker.Preferences.KEY_USE_NOTES,
                OSMTracker.Preferences.VAL_USE_NOTES);

        saveAsWayPoint = !prefSaveAs.equals("osm_note");
        saveAsNote = !prefSaveAs.equals("waypoint");

        if (saveAsWayPoint && wayPointUuid == null) {
            wayPointUuid = UUID.randomUUID().toString();
            sendTrackIntent(OSMTracker.INTENT_TRACK_WP, wayPointUuid);
        }

        if (saveAsNote && noteUuid == null) {
            noteUuid = UUID.randomUUID().toString();
            sendTrackIntent(OSMTracker.INTENT_TRACK_NOTE, noteUuid);
        }

        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private void sendTrackIntent(String action, String uuid) {
        Intent intent = new Intent(action);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, trackId);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, uuid);
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, ""); // Nombre inicial vacío
        intent.setPackage(getContext().getPackageName());
        context.sendBroadcast(intent);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        input.setText(savedInstanceState.getString(KEY_INPUT_TEXT));
        wayPointUuid = savedInstanceState.getString(KEY_WAYPOINT_UUID);
        noteUuid = savedInstanceState.getString(KEY_NOTE_UUID);
        trackId = savedInstanceState.getLong(KEY_TRACK_ID);
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        Bundle extras = super.onSaveInstanceState();
        extras.putString(KEY_INPUT_TEXT, input.getText().toString());
        extras.putString(KEY_WAYPOINT_UUID, wayPointUuid);
        extras.putString(KEY_NOTE_UUID, noteUuid);
        extras.putLong(KEY_TRACK_ID, trackId);
        return extras;
    }

    public void resetValues() {
        this.wayPointUuid = null;
        this.noteUuid = null;
        if (input != null) input.setText("");
    }

    public void setTag(String tag){
        this.tag = tag;
        if (input != null) {
            input.post(() -> setTitle(tag));
        } else {
            setTitle(tag);
        }
    }
}