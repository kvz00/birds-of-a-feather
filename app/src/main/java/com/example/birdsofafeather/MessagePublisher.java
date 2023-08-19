package com.example.birdsofafeather;


import com.google.android.gms.nearby.messages.Message;

public interface MessagePublisher {
    public void sendMessage(String messageStr);
    public void cancelMessage(Message message);
}
