package edu.msu.becketta.dialog_simple;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Aaron Beckett on 1/11/2016.
 */
public class LoadDlg extends DialogFragment {

    /**
     * Create the dialog box
     * @param savedInstanceState The saved instance bundle
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle(R.string.open_dialog_title);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because it's going in the dialog layout
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.catalog_dlg, null);
        builder.setView(view);

        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel just closes the dialog box
            }
        });

        final AlertDialog dlg = builder.create();

        final MainLogActivity mainLogActivity = (MainLogActivity) getActivity();

        // Find the list view
        ListView list = (ListView)view.findViewById(R.id.listHattings);

        // Create an adapter
        final CatalogAdapter adapter = new CatalogAdapter(list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // Get the id of the one we want to load
                String catId = adapter.getId(position);

                // Dismiss the dialog box
                dlg.dismiss();

                /*
                 * TODO: load the diaLog, maybe use ProgressDialogBox
                 */
                LocalBase localBase = LocalBase.getInstance(view.getContext());
                diaLog log = localBase.loadLog(catId);
                mainLogActivity.loadLog(log);
            }

        });

        return dlg;
    }
}
