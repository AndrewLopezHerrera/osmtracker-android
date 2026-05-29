package net.osmtracker.listener;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.osmtracker.OSMTracker;
import net.osmtracker.groupeddata.GroupedDataManager;
import net.osmtracker.db.TrackContentProvider;

import java.util.UUID;

import android.view.View.OnClickListener;

public class GroupedDataOnClickListener implements OnClickListener {
    private long currentTrackId;
    private GroupedDataManager GroupedData;
    private boolean isActivated;

    public GroupedDataOnClickListener(long trackId) {
        currentTrackId = trackId;
        GroupedData = GroupedDataManager.getInstance();
        isActivated = false;
    }

    @Override
    public void onClick(View view) {
        Button button = (Button) view;
        String label = button.getText().toString().replaceAll("\n", " ");
        if(isActivated && GroupedData.isRecordingGroupedData()){
            isActivated = false;
            GroupedData.deactivateRecordGroupedData();
            button.setText(label);
            Toast.makeText(view.getContext(), "Record deactivated: " + label, Toast.LENGTH_SHORT).show();
        }
        else if(!isActivated && !GroupedData.isRecordingGroupedData()){
            activateRecord(label, view);
            String text = label + " / recording";
            button.setText(text);
            Toast.makeText(view.getContext(), "Record activated: " + label, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(view.getContext(), "It cannot be turned on or off because another recording is in progress.", Toast.LENGTH_SHORT).show();
        }
    }

    private void activateRecord(String label, View view){
        String uuid = UUID.randomUUID().toString();
        Intent intent = new Intent(OSMTracker.INTENT_TRACK_WP);
        intent.putExtra(TrackContentProvider.Schema.COL_TRACK_ID, currentTrackId);
        intent.putExtra(OSMTracker.INTENT_KEY_NAME, label);
        intent.putExtra(OSMTracker.INTENT_KEY_UUID, uuid);
        String packageName = view.getContext().getPackageName();
        intent.setPackage(packageName);
        view.getContext().sendBroadcast(intent);
        GroupedData.activateRecordGroupedData(currentTrackId, uuid);
        isActivated = true;
    }
}
