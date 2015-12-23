package com.fedor.pavel.goodssearcher.dialogs;


import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.fedor.pavel.goodssearcher.FoundGoodsActivity;
import com.fedor.pavel.goodssearcher.R;

public class AlertDialogManager {


    public static void createSettingsDialog(final AppCompatActivity activity) {

        AlertDialog.Builder settingsAlert = new AlertDialog.Builder(activity);

        settingsAlert.setTitle(activity.getResources().getText(R.string.no_internet_alert_title));
        settingsAlert.setIcon(R.drawable.ic_alert);

        settingsAlert.setCancelable(false);


        settingsAlert.setMessage(activity.getResources().getText(R.string.no_internet_alert_message));

        settingsAlert.setPositiveButton(activity.getResources().getText(R.string.settings_dialog_positive_btn)
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        settingsAlert.setNegativeButton(activity.getResources().getText(R.string.settings_dialog_negative_btn)
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (activity instanceof FoundGoodsActivity) {
                    activity.finish();
                }

            }
        });


        settingsAlert.show();

    }





}
