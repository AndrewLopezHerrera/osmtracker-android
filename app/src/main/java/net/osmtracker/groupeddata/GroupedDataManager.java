package net.osmtracker.groupeddata;

import android.content.Context;
import android.content.Intent;
import net.osmtracker.OSMTracker;
import net.osmtracker.R;
import net.osmtracker.db.TrackContentProvider;

import java.util.UUID;

public class GroupedDataManager {

    private boolean recordingGroupedData;
    private long currentTrackID;
    private String currentUUID;
    private int IDGroupData;
    private String name;
    private int amountElements;
    private static final GroupedDataManager instance = new GroupedDataManager();

    private GroupedDataManager(){
        recordingGroupedData = false;
        currentTrackID = 0;
        currentUUID = "";
        name = "";
        amountElements = 0;
    }

    public static GroupedDataManager getInstance(){
        return instance;
    }

    public void activateRecordGroupedData(Context context){
        IDGroupData++;
        name = context.getResources().getString(R.string.id_data_group) + IDGroupData;
        String uuid = UUID.randomUUID().toString();
        this.currentUUID = uuid;
        this.recordingGroupedData = true;
        Intent intent = new Intent(OSMTracker.INTENT_TRACK_WP);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, this.currentTrackID);
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, name);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, uuid);
        intent.putExtra(OSMTracker.INTENT_KEY_IS_GROUPED, true);
        String packageName = context.getPackageName();
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
    }

    public void deactivateRecordGroupedData(){
        this.currentUUID = "";
        this.recordingGroupedData = false;
        this.name = "";
        this.amountElements = 0;
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

    public void addGroupedData(Context context, String data, String link){
        if (context == null) return;
        Intent intent = new Intent(OSMTracker.INTENT_ADD_GROUPED_DATA);
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, data);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, getCurrentUUID());
        intent.putExtra(OSMTracker.INTENT_KEY_LINK, link);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
        amountElements++;
        sendToUpdateUI(context);
    }

    public void updateCoordinateGroupedData(Context context){
        if (context == null) return;
        Intent intent = new Intent(OSMTracker.INTENT_UPDATE_COORDINATES_WAYPOINT);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, getCurrentTrackID());
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, getCurrentUUID());
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    public void setCurrentTrackID(long currentTrackID) {
        this.currentTrackID = currentTrackID;
        IDGroupData = 0;
    }

    public int getIDGroupData(){
        return IDGroupData;
    }

    public String getName() {
        return name;
    }

    public int getAmountElements(){
        return amountElements;
    }

    public void setName(Context context, String name){
        this.name = name;
        Intent intent = new Intent(OSMTracker.INTENT_UPDATE_WP);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, getCurrentTrackID());
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, name);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, currentUUID);
        String packageName = context.getPackageName();
        intent.setPackage(packageName);
        context.sendBroadcast(intent);
        sendToUpdateUI(context);
    }

    private void sendToUpdateUI(Context context){
        Intent updateUiIntent = new Intent(OSMTracker.INTENT_UPDATE_UI_DATA_GROUP);
        updateUiIntent.setPackage(context.getPackageName());
        context.sendBroadcast(updateUiIntent);
    }
}