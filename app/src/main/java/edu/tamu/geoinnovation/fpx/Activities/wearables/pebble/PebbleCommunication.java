package edu.tamu.geoinnovation.fpx.activities.wearables.pebble;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

public class PebbleCommunication extends Service {
    private static final int SAMPLES_PER_UPDATE = 5;   // Must match the watchapp value
    private static final int ELEMENTS_PER_PACKAGE = 10;
    private static final int OPTIMIZED_ELEMENTS_PER_PACKAGE = ELEMENTS_PER_PACKAGE - 5;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PebbleCommunication", "STARTED SERVICE");
        pebbleCommunication();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public void pebbleCommunication() {
        PebbleKit.PebbleDataReceiver mDataReceiver = new PebbleKit.PebbleDataReceiver(PebbleInitializationActivity.PEBBLE_APP_UUID) {
            @Override
            public void receiveData(Context context, int transactionID, PebbleDictionary data) {
                Log.d("PebbleCommunication", "RECEIVED PACKET");
                PebbleKit.sendAckToPebble(context, transactionID);
                for (int i = 0; i < SAMPLES_PER_UPDATE; i++) {
                    int row = i * ELEMENTS_PER_PACKAGE;
                    double[] info = new double[OPTIMIZED_ELEMENTS_PER_PACKAGE];
                    for (int j = 0; j < OPTIMIZED_ELEMENTS_PER_PACKAGE; j++) {
                        if (j == 0 || j == 1) {
                            if (data.getUnsignedIntegerAsLong(row + j) != null) {
                                info[j] = (double) data.getUnsignedIntegerAsLong(row + j).intValue();
                            }
                        } else {
                            if (data.getInteger(row + j) != null) {
                                info[j] = (double) data.getInteger(row + j).intValue();
                            }
                        }
                    }

                    if (data.getUnsignedIntegerAsLong(row) != null && data.getUnsignedIntegerAsLong(row + 1) != null) {
                        double timeSeconds = info[0];
                        double timeMilliseconds = info[1];
                        double time = timeSeconds + (timeMilliseconds / 1000);

                        Intent intent = new Intent("wearable-communication");
                        intent.putExtra("time", time);
                        intent.putExtra("x-value", info[2]);
                        intent.putExtra("y-value", info[3]);
                        intent.putExtra("z-value", info[4]);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                    }
                }
            }
        };

        // Register DataLogging Receiver
        PebbleKit.registerReceivedDataHandler(this, mDataReceiver);
    }

}
