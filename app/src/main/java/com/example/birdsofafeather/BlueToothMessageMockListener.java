package com.example.birdsofafeather;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlueToothMessageMockListener extends MessageListener {
    private static BlueToothMessageMockListener single_instance = null;
    public final ScheduledExecutorService executor;
    public final MessageListener messageListener;

    public static BlueToothMessageMockListener BlueToothMessageMockListener(MessageListener realMessageListener) {
        if (single_instance == null || single_instance.messageListener == null) {
            single_instance = new BlueToothMessageMockListener(realMessageListener);
        }
        return single_instance;
    }

    public static BlueToothMessageMockListener BlueToothMessageMockListener() {
        if (single_instance == null) {
            single_instance = new BlueToothMessageMockListener(null);
        }
        return single_instance;
    }

    private BlueToothMessageMockListener(MessageListener realMessageListener) {
        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void connectedUser(String student) {
        Message message = new Message(("user/" + student).getBytes(StandardCharsets.UTF_8));
        this.messageListener.onFound(message);
    }

    public void actionFromUser(String user, String action) {
        Message message = new Message(("action/" + user + "\n" + action).getBytes(StandardCharsets.UTF_8));
        this.messageListener.onFound(message);
    }
}
