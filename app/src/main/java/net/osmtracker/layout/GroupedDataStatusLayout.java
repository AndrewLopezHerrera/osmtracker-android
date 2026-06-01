package net.osmtracker.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import net.osmtracker.R;
import net.osmtracker.groupeddata.GroupedDataManager;

public class GroupedDataStatusLayout extends LinearLayout {
    private GroupedDataManager groupedDataManager;
    private ImageView imageGroupedData;
    Switch switchGroupedData;

    public GroupedDataStatusLayout(Context context) {
        super(context);
        init(context);
    }

    public GroupedDataStatusLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GroupedDataStatusLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.grouped_data_layout, this, true);
        setEnabled(false);
        groupedDataManager = GroupedDataManager.getInstance();
        switchGroupedData = findViewById(R.id.grouped_data_switch);
        switchGroupedData.setChecked(false);
        imageGroupedData = findViewById(R.id.grouped_data_image);
        imageGroupedData.setImageResource(R.drawable.grouped_data_diactivated);
        setFunctionSwitch();
    }

    private void setFunctionSwitch(){
        switchGroupedData.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                groupedDataManager.activateRecordGroupedData(getContext());
                imageGroupedData.setImageResource(R.drawable.grouped_data_activated);
                Toast.makeText(getContext(), "Data grouping activated", Toast.LENGTH_SHORT).show();
            }
            else{
                groupedDataManager.deactivateRecordGroupedData();
                imageGroupedData.setImageResource(R.drawable.grouped_data_diactivated);
                Toast.makeText(getContext(), "Data grouping deactivated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setEnable(boolean enabled) {
        super.setEnabled(enabled);
        if (switchGroupedData != null) {
            switchGroupedData.setEnabled(enabled);
        }
    }
}
