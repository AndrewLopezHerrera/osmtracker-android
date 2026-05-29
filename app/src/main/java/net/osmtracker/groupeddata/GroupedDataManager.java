package net.osmtracker.groupeddata;

import android.content.Context;
import android.content.Intent;
import net.osmtracker.OSMTracker;
import net.osmtracker.db.TrackContentProvider;

public class GroupedDataManager {

    // Nombres corregidos a minúscula inicial (CamelCase estándar de Java)
    private boolean recordingGroupedData;
    private long currentTrackID;
    private String currentUUID;

    private static final GroupedDataManager instance = new GroupedDataManager();

    private GroupedDataManager(){
        recordingGroupedData = false;
        currentTrackID = 0;
        currentUUID = "";
    }

    public static GroupedDataManager getInstance(){
        return instance;
    }

    public void activateRecordGroupedData(long trackID, String uuid){
        this.currentTrackID = trackID;
        this.currentUUID = uuid;
        this.recordingGroupedData = true;
    }

    public void deactivateRecordGroupedData(){
        this.currentTrackID = 0;
        this.currentUUID = "";
        this.recordingGroupedData = false;
    }

    public boolean isRecordingGroupedData() {
        return recordingGroupedData;
    }

    public long getCurrentTrackID() {
        return currentTrackID;
    }

    public String getCurrentUUID() {
        return currentUUID;
    }

    public void addGroupedData(Context context, String data){
        if (context == null) return;
        Intent intent = new Intent(OSMTracker.INTENT_ADD_GROUPED_DATA);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, getCurrentTrackID());
        intent.putExtra(TrackContentProvider.Schema.COL_NAME, data);
        intent.putExtra(TrackContentProvider.Schema.COL_UUID_REFERENCE, getCurrentUUID());
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }
}