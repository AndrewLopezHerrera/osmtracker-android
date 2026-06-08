package net.osmtracker.listener;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import net.osmtracker.R;
import net.osmtracker.groupeddata.GroupedDataManager;

public class UpdateCoordinatesGroupedDataOnClickListener implements View.OnClickListener {
    Context context;
    GroupedDataManager groupedDataManager;
    public UpdateCoordinatesGroupedDataOnClickListener(Context context){
        this.context = context;
        this.groupedDataManager = GroupedDataManager.getInstance();
    }
    @Override
    public void onClick(View view) {
        if(!groupedDataManager.isRecordingGroupedData()){
            Toast.makeText(context, context.getResources().getString(R.string.message_data_grouping_disabled), Toast.LENGTH_SHORT).show();
            return;
        }
        groupedDataManager.updateCoordinateGroupedData(context);
        Toast.makeText(context, context.getResources().getString(R.string.message_group_coordinates_updated), Toast.LENGTH_SHORT).show();
    }
}
