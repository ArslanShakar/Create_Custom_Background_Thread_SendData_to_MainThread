package com.practice.coding.create_custom_background_thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private Looper looper;

    private TextView textView;
    private ProgressBar progressBar;
    private static final String MESSAGE_KEY = "messageKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBarHorizontal);

        /*
        --> Handler and looper must attched with the specific Thread like here i am attached with the main or UI thread
        --> Looper :- Looper actually loop over the Message/Work Queue of the specific thread like here main thread. Looper loops over
        the message queue and assign tasks one by one to the worker thread and when thread execution the one task completed
        then looper assign a new task/message from the work/message queue.
        another words when one data packet is completed by thread then loop send the next data packet from the message queue to
        to corresponding thread for execution.

        --> Handler :- Handler Actually manages the Message/Work Queue of any Thread.For managing the Message Queue of any thread
        handler must be attached with that thread. Handler send the messages/data packets in the MessageQueue At any position
        , or send data packet on the time delay basis, like after specified time like after 4 seconds delay the handler put the
        specific message or data packet in the message queue.
         */
        //getMainLooper() this method return a looper object.

        handler = new Handler(getMainLooper())
        {
            @Override
            public void handleMessage(Message msg) {

                //get the bundle argument

                String data = msg.getData().getString(MESSAGE_KEY);
                textView.setText(data);

                //When our background thread complete its execution i am hide the progress bar
                progressBar.setVisibility(View.GONE);
            }
        };

    }

    public void runCode(View view) {

        /*
        --> Thread are not Memory safe..Working with thread we cannot directly holds the views reference or update the views
        directly.
       --> On the background thread we cannot access our activity views references because may be our activty destroyed like
        orientation change then activity destroyed the view reference is also destroyed but The Background thread can not
        destroy it still continue their working and when the activity recreated then the views references also recreated
        but the background thread contain the previous reference of the view then in this case the app crash problem we faces
        means ANR application not responding thats error occurs. and the garbage collector holds many references like activity
        destroy and then recreated views references also destroyed and recreated..
        View contain the whole activity reference till the activity is alive when activity destroyed its reference also destroyed
        --> So , directly cannot access the views references on the background threads because background threads are not destroyed
        till they complete their execution so Holding the view reference is very Risky any time The activty is destroyed or
        orientation change or may be some exception come and app crashes, So dont do like that...

        --> We have some approuches to send the data or update the views ... Keep in mind only main/UI Thread have the responsibility
        to handle and update the views So we send data from background thread to the main / UI Thread then the on the main thread
         updating the views...
        --> Send data from background thread to main thread the very basic technique is we use Handler object for sending bundle
        or Integer argument with the help of Message Class object to the Main / UI Thread.
         let do the code. . .
         */

        textView.setText("Downloading Start. . .");
        progressBar.setVisibility(View.VISIBLE);
        //Now i am create the background thread
        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {

                }

                Message message = new Message();
                String mesg = "Download Completed. . .";
                Bundle bundle = new Bundle();
                bundle.putString(MESSAGE_KEY, mesg);
                message.setData(bundle);

                handler.sendMessage(message);
            }
        };

        Thread t1 = new Thread(runnable);
        t1.start();

    }
}
