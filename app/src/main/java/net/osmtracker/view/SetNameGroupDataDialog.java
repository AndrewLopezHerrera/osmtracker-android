package net.osmtracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;

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
        input.setHint("Nombre del grupo");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Cambie el nombre del grupo")
                .setView(input)
                .setCancelable(true)
                .setPositiveButton(context.getString(android.R.string.ok), (dialog, which) -> {
                    String response = input.getText().toString();
                    groupedDataManager.setName(context, response);
                })
                .setNegativeButton(context.getString(android.R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}