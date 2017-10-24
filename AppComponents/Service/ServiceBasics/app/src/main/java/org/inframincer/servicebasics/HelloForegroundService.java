package org.inframincer.servicebasics;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.Toast;

public class HelloForegroundService extends Service {

    private static final String TAG = HelloForegroundService.class.getSimpleName();

    private static final int ONGOING_NOTIFICATION_ID = 1000;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

            Intent notificationIntent = new Intent(HelloForegroundService.this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(HelloForegroundService.this, 0, notificationIntent, 0);
            Notification notification =
                    new Notification.Builder(HelloForegroundService.this)
                            .setContentTitle(getText(R.string.notification_title))
                            .setContentText(getText(R.string.notification_message))
                            .setSmallIcon(android.R.drawable.sym_def_app_icon)
                            .setContentIntent(pendingIntent)
                            .setTicker(getText(R.string.ticker_text))
                            .build();

            startForeground(ONGOING_NOTIFICATION_ID, notification);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            stopSelf(msg.arg1);
        }
    }

    public HelloForegroundService() {
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ForegroundServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Foreground Service Starting", Toast.LENGTH_SHORT).show();

        Message msg = mServiceHandler.obtainMessage();
        msg.obj = intent;
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();
        Toast.makeText(this, "Foreground Service Done", Toast.LENGTH_SHORT).show();
    }
}
