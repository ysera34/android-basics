package org.inframincer.servicebasics;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

public class HelloService extends Service {

    private static final String TAG = HelloService.class.getSimpleName();

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "handleMessage: ");
            /*
//            super.handleMessage(msg);
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
            */
            PendingIntent receiver = ((Intent) msg.obj).getParcelableExtra("hello_receiver");
            if (receiver != null) {
                try {
                    Intent intent = new Intent();
                    intent.putExtra("helloServiceResult", "ok");
                    receiver.send(getApplicationContext(), 0, intent);
                } catch (PendingIntent.CanceledException e) {
                    Log.e(TAG, "handleMessage: ", e.getCause());
                }
            }
            stopSelf(msg.arg1);
        }
    }

    public HelloService() {
    }

    @Override
    public void onCreate() {
//        super.onCreate();
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

//    @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.obj = intent;
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        mServiceLooper.quit();
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
