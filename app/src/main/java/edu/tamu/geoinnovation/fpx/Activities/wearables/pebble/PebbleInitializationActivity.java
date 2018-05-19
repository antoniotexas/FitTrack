package edu.tamu.geoinnovation.fpx.activities.wearables.pebble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.getpebble.android.kit.PebbleKit;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import edu.tamu.geoinnovation.fpx.R;

public class PebbleInitializationActivity extends AppCompatActivity {
    public static final UUID PEBBLE_APP_UUID = UUID.fromString("bb039a8e-f72f-43fc-85dc-fd2516c7f328");//"b32d0883-4ed5-445a-a4ea-cac457f48c12");

    private final static int REQUEST_ENABLE_BT = 1;
    private final static int VERIFY_ANDROID_APP_INSTALLED = 2;

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_initialization);

        // Chained method -> onActivityResult()
        pebbleInitializationChain(0);
    }

    // We create a chain of functions so that not all the dialogs will appear at once and nothing will hopefully conflict.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) pebbleInitializationChain(1);
            else {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        } else if (requestCode == VERIFY_ANDROID_APP_INSTALLED) {
            if (resultCode == RESULT_OK) pebbleInitializationChain(2);
            else {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        }
    }

    private void pebbleInitializationChain(int chainNum) {
        switch (chainNum) {
            case 0:
                requestBluetoothActivation();
                break;
            case 1:
                verifyPebbleAndroidInstalled();
                break;
            case 2:
                verifyPebbleWatchConnected();
                break;
            case 3:
                verifyPebbleWatchAppActive();
                break;
            case 4:
            default:
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    private void requestBluetoothActivation() {
        if (mBluetoothAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Incompatible Device")
                    .setMessage("Your device does not support Bluetooth so we cannot connect to a watch to track your workouts!")
                    .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            finish();
                        }
                    })
                    .show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                pebbleInitializationChain(1);
            }
        }
    }

    private void verifyPebbleAndroidInstalled() {
        try {
            final PackageManager pm = this.getApplicationContext().getPackageManager();
            pm.getPackageInfo("com.getpebble.android.basalt", 0);
            pebbleInitializationChain(2);
        } catch (PackageManager.NameNotFoundException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You do not have the Pebble app installed. To track your workout you need the Pebble android app. Would you like to install it?")
                    .setTitle("Install Pebble Android App");

            builder.setPositiveButton("Install", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                            .setData(Uri.parse("market://details?id=com.getpebble.android.basalt"));
                    startActivityForResult(goToMarket, VERIFY_ANDROID_APP_INSTALLED);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                    finish();
                }
            });

            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, resultIntent);
                        finish();
                    }
                    return true;
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void verifyPebbleWatchConnected() {
        if (!PebbleKit.isWatchConnected(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Pebble Watch is connected! To track workouts a Pebble Watch must be connected. Please connect a watch and press 'Retry' or go back to the main menu.")
                    .setTitle("Connect Pebble Watch");

            // Add the buttons
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    verifyPebbleWatchConnected();
                }
            });

            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                    finish();
                }
            });

            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, resultIntent);
                        finish();
                    }
                    return true;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            pebbleInitializationChain(3);
        }
    }

    // TODO: Verify watch is actually on.
    // TODO: Verify app is on watch.
    private void verifyPebbleWatchAppActive() {
        try {
            PebbleKit.closeAppOnPebble(this, PEBBLE_APP_UUID);
            TimeUnit.SECONDS.sleep(1);
            PebbleKit.startAppOnPebble(this, PEBBLE_APP_UUID);
            pebbleInitializationChain(4);
        } catch (IllegalArgumentException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your Pebble Watch does not have the Habitual app downloaded. Please install it and try again!")
                    .setTitle("Install Pebble Watch App");

            // Add the buttons
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    verifyPebbleWatchAppActive();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                    finish();
                }
            });

            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_CANCELED, resultIntent);
                        finish();
                    }
                    return true;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (InterruptedException e) {

        }
    }
}
