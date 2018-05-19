package edu.tamu.geoinnovation.fpx.Modules.Pebble;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.getpebble.android.kit.PebbleKit;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.getpebble.android.kit.PebbleKit.isWatchConnected;

/**
 * Created by Abanoub Doss on 4/14/2018.
 */

public class PebbleAdapter {

    public static void verifyPebbleWatchAppActive(final UUID PEBBLE_APP_UUID) {
        try {
            PebbleKit.closeAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
            TimeUnit.SECONDS.sleep(1);
            PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
        } catch(IllegalArgumentException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            builder.setMessage("Your Pebble Watch does not have the Habitual app downloaded. Please install it and try again!")
                    .setTitle("Install Pebble Watch App");

            // Add the buttons
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    verifyPebbleWatchAppActive(PEBBLE_APP_UUID);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ((Activity)getApplicationContext()).finish();
                }
            });

            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ((Activity)getApplicationContext()).finish();
                        dialog.dismiss();
                    }
                    return true;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch(InterruptedException e) {

        }
    }

    public static boolean verfiyPebbleWatchConnected() {
        if(!isWatchConnected(getApplicationContext())) {
            return false;
        }
        return true;
    }
}
