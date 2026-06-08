package net.osmtracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import net.osmtracker.R;
import net.osmtracker.groupeddata.GroupedDataManager;

public class SetNameGroupDataDialog {
    private final Context context;
    private final GroupedDataManager groupedDataManager;

    public SetNameGroupDataDialog(Context context) {
        this.context = context;
        this.groupedDataManager = GroupedDataManager.getInstance();
    }

    public void show() {
        EditText input = new EditText(context);
        input.setHint(context.getResources().getString(R.string.group_name_placeholder));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.change_group_name_dialog_title))
                .setView(input)
                .setCancelable(true)
                .setPositiveButton(context.getString(android.R.string.ok), (dialog, which) -> {
                    String response = input.getText().toString();
                    groupedDataManager.setName(context, response);
                    Toast.makeText(context, context.getResources().getString(R.string.message_group_name_changed), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(context.getString(android.R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                    Toast.makeText(context, context.getResources().getString(R.string.message_group_name_not_changed), Toast.LENGTH_SHORT).show();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}