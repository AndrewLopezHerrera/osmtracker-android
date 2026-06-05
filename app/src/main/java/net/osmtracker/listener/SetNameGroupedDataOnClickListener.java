package net.osmtracker.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import net.osmtracker.view.SetNameGroupDataDialog;

public class SetNameGroupedDataOnClickListener implements View.OnClickListener {
    Context context;
    SetNameGroupDataDialog dialog;
    public SetNameGroupedDataOnClickListener(Context context){
        this.context = context;
        this.dialog = new SetNameGroupDataDialog(context);
    }

    @Override
    public void onClick(View view) {
        dialog.show();
    }
}
