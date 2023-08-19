package com.example.birdsofafeather;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlueToothMessageListener extends MessageListener {
    private final MessageListener messageListener;
    private Activity activity;
    public BlueToothMessageListener(Activity activity, MessageListener realMessageListener, int frequency, String messageStr) {
        this.messageListener = realMessageListener;
        this.activity = activity;
    }

    public void sendMessage(String messageStr) {
        Message message = new Message(messageStr.getBytes(StandardCharsets.UTF_8));
        Nearby.getMessagesClient(activity).publish(message);
    }

    public void cancelMessage(Message message) {
        Nearby.getMessagesClient(activity).unpublish(message);
    }
}
