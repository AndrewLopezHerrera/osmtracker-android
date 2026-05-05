package net.osmtracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import net.osmtracker.OSMTracker;
import net.osmtracker.R;
import net.osmtracker.db.TrackContentProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpinnerNoteDialog extends AlertDialog {

    private static final String KEY_SPINNER_SELECTION = "SPINNER_SELECTION";
    private static final String KEY_WAYPOINT_UUID = "WAYPOINT_UUID";
    private static final String KEY_NOTE_UUID = "NOTE_UUID";
    private static final String KEY_TRACK_ID = "TRACK_ID";
    private static final Logger log = LoggerFactory.getLogger(SpinnerNoteDialog.class);

    private Spinner spinner;
    private List<String> items;
    private String wayPointUuid;
    private String noteUuid;
    private long trackId;

    private boolean saveAsWayPoint, saveAsNote;
    private Context context;

    public SpinnerNoteDialog(Context context, long trackId) {
        super(context);
        this.context = context;
        this.trackId = trackId;

        // Configuración del Spinner
        spinner = new Spinner(context);
        items = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        this.setTitle(R.string.gpsstatus_record_textnote);
        this.setCancelable(true);
        this.setView(spinner);

        // Botón Aceptar (Positive)
        this.setButton(DialogInterface.BUTTON_POSITIVE,
                context.getString(android.R.string.ok),
                (dialog, which) -> {
                    String selectedText = spinner.getSelectedItem().toString();

                    if (saveAsWayPoint) {
                        Intent intent = new Intent(OSMTracker.INTENT_UPDATE_WP);
                        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, this.trackId);
                        intent.putExtra(OSMTracker.INTENT_KEY_NAME, selectedText);
                        intent.putExtra(OSMTracker.INTENT_KEY_UUID, this.wayPointUuid);
                        intent.setPackage(getContext().getPackageName());
                        context.sendBroadcast(intent);
                    }

                    if (saveAsNote) {
                        Intent noteIntent = new Intent(OSMTracker.INTENT_UPDATE_NOTE);
                        noteIntent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, this.trackId);
                        noteIntent.putExtra(OSMTracker.INTENT_KEY_NAME, selectedText);
                        noteIntent.putExtra(OSMTracker.INTENT_KEY_UUID, this.noteUuid);
                        noteIntent.setPackage(getContext().getPackageName());
                        context.sendBroadcast(noteIntent);
                    }
                });

        // Botón Cancelar (Negative)
        this.setButton(DialogInterface.BUTTON_NEGATIVE,
                context.getString(android.R.string.cancel),
                (dialog, which) -> dialog.cancel());

        // Manejo de cancelación (Eliminar el waypoint temporal)
        this.setOnCancelListener(dialog -> {
            if (wayPointUuid != null) {
                Intent intent = new Intent(OSMTracker.INTENT_DELETE_WP);
                intent.putExtra(OSMTracker.INTENT_KEY_UUID, wayPointUuid);
                intent.setPackage(getContext().getPackageName());
                context.sendBroadcast(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String prefSaveAs = prefs.getString(
                OSMTracker.Preferences.KEY_USE_NOTES,
                OSMTracker.Preferences.VAL_USE_NOTES);
        switch (prefSaveAs) {
            case "waypoint":
                saveAsWayPoint = true;
                saveAsNote = false;
                break;
            case "osm_note":
                saveAsWayPoint = false;
                saveAsNote = true;
                break;
            default:
                saveAsWayPoint = true;
                saveAsNote = true;
                break;
        }
        if (saveAsWayPoint && wayPointUuid == null) {
            wayPointUuid = UUID.randomUUID().toString();
            sendTrackIntent(OSMTracker.INTENT_TRACK_WP, wayPointUuid);
        }
        if (saveAsNote && noteUuid == null) {
            noteUuid = UUID.randomUUID().toString();
            sendTrackIntent(OSMTracker.INTENT_TRACK_NOTE, noteUuid);
        }
    }

    private void sendTrackIntent(String action, String uuid) {
        Intent intent = new Intent(action);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, trackId);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, uuid);
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, context.getString(R.string.gpsstatus_record_textnote));
        intent.setPackage(getContext().getPackageName());
        context.sendBroadcast(intent);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        wayPointUuid = savedInstanceState.getString(KEY_WAYPOINT_UUID);
        noteUuid = savedInstanceState.getString(KEY_NOTE_UUID);
        trackId = savedInstanceState.getLong(KEY_TRACK_ID);
        spinner.setSelection(savedInstanceState.getInt(KEY_SPINNER_SELECTION));
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        Bundle extras = super.onSaveInstanceState();
        extras.putString(KEY_WAYPOINT_UUID, wayPointUuid);
        extras.putString(KEY_NOTE_UUID, noteUuid);
        extras.putLong(KEY_TRACK_ID, trackId);
        extras.putInt(KEY_SPINNER_SELECTION, spinner.getSelectedItemPosition());
        return extras;
    }

    public void resetValues() {
        wayPointUuid = null;
        noteUuid = null;
    }

    public void setItems(List<String> newItems) {
        this.items = newItems;
        if (spinner != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    android.R.layout.simple_spinner_item,
                    newItems
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }
    }
}