package net.osmtracker.layout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import net.osmtracker.OSMTracker;
import net.osmtracker.R;
import net.osmtracker.groupeddata.GroupedDataManager;
import net.osmtracker.listener.SetNameGroupedDataOnClickListener;

public class GroupedDataStatusLayout extends LinearLayout {
    private GroupedDataManager groupedDataManager;
    private ImageView imageGroupedData;
    private Switch switchGroupedData;
    private TextView nameGroup;
    private TextView amountElements;
    private Button buttonSetName;
    private Button buttonUpdateCoordinates;

    private final BroadcastReceiver uiUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && OSMTracker.INTENT_UPDATE_UI_DATA_GROUP.equals(intent.getAction())) {
                updateUiTexts();
            }
        }
    };

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
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.grouped_data_layout, this, true);

        setEnabled(false);
        groupedDataManager = GroupedDataManager.getInstance();

        switchGroupedData = findViewById(R.id.grouped_data_switch);
        switchGroupedData.setChecked(false);

        imageGroupedData = findViewById(R.id.grouped_data_image);
        imageGroupedData.setImageResource(R.drawable.grouped_data_diactivated);

        nameGroup = findViewById(R.id.name_group_data);
        amountElements = findViewById(R.id.amount_grouped_data);

        buttonSetName = findViewById(R.id.button_set_name_group_data);
        SetNameGroupedDataOnClickListener listener = new SetNameGroupedDataOnClickListener(context);
        buttonSetName.setOnClickListener(listener);

        buttonUpdateCoordinates = findViewById(R.id.button_update_coordinates);

        updateUiTexts();
        setFunctionSwitch();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter(OSMTracker.INTENT_UPDATE_UI_DATA_GROUP);
        getContext().registerReceiver(uiUpdateReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(uiUpdateReceiver);
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
            updateUiTexts();
        });
    }

    private void updateUiTexts() {
        if (nameGroup != null) {
            nameGroup.setText(createName());
        }
        if (amountElements != null) {
            amountElements.setText(createAmountElements());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(switchGroupedData != null)
            switchGroupedData.setEnabled(enabled);
        if(buttonSetName != null)
            buttonSetName.setEnabled(enabled);
        if(buttonUpdateCoordinates != null)
            buttonUpdateCoordinates.setEnabled(enabled);
    }

    private String createName(){
        if (!groupedDataManager.isRecordingGroupedData()) {
            return groupedDataManager.getName();
        }
        return "Grupo #" + groupedDataManager.getIDGroupData() + " - \"" + groupedDataManager.getName() + "\"";
    }

    private String createAmountElements(){
        return "Cantidad datos: " + groupedDataManager.getAmountElements();
    }
}