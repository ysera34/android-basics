package org.inframincer.servicebasics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HelloReceiver extends BroadcastReceiver {

    private static final String TAG = HelloReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        String result = intent.getStringExtra("helloServiceResult");
        Log.i(TAG, "onReceive: result: " + result);
        HelloObservable.getInstance().updateValue(result);
    }
}
