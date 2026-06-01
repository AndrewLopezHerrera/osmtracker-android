package net.osmtracker.groupeddata;

import android.content.Context;
import android.content.Intent;
import net.osmtracker.OSMTracker;
import net.osmtracker.db.TrackContentProvider;

import java.util.UUID;

public class GroupedDataManager {

    private boolean recordingGroupedData;
    private long currentTrackID;
    private String currentUUID;
    private int IDGroupData;

    private static final GroupedDataManager instance = new GroupedDataManager();

    private GroupedDataManager(){
        recordingGroupedData = false;
        currentTrackID = 0;
        currentUUID = "";
    }

    public static GroupedDataManager getInstance(){
        return instance;
    }

    public void activateRecordGroupedData(Context context){
        IDGroupData++;
        String name = "ID_Data_Group" + IDGroupData;
        String uuid = UUID.randomUUID().toString();
        this.currentUUID = uuid;
        this.recordingGroupedData = true;
        Intent intent = new Intent(OSMTracker.INTENT_TRACK_WP);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, this.currentTrackID);
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, name);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, uuid);
        String packageName = context.getPackageName();
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public void deactivateRecordGroupedData(){
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

    public void setCurrentTrackID(long currentTrackID) {
        this.currentTrackID = currentTrackID;
        IDGroupData = 0;
    }
}