package org.inframincer.servicebasics;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static android.app.PendingIntent.FLAG_ONE_SHOT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_foo_button).setOnClickListener(this);
        findViewById(R.id.start_baz_button).setOnClickListener(this);
        findViewById(R.id.start_service).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_foo_button:
                HelloIntentService.startActionFoo(this, "fooParam1", "fooParam2");
                break;
            case R.id.start_baz_button:
                HelloIntentService.startActionBaz(this, "BazParam1", "BazParam2");
                break;
            case R.id.start_service:
                Intent intent = new Intent(this, HelloService.class);
//                startService(intent);

                Intent broadcastReceiverIntent = new Intent(this, HelloReceiver.class);
                PendingIntent pendingIntent =
                        PendingIntent.getBroadcast(this, 100, broadcastReceiverIntent, FLAG_ONE_SHOT);
                intent.putExtra("hello_receiver", pendingIntent);
                startService(intent);
                break;
        }
    }
}
