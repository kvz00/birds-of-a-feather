package com.example.birdsofafeather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import com.example.birdsofafeather.Utilities.Utility;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Utility.showAlert(this,"Your device does not support BlueTooth, so app may not function properly");
            nextActivity();
        } else {
            // Check if user has Bluetooth enabled, if not ask them
            if (!bluetoothAdapter.isEnabled()) {

                Utility.showAlert(this,"You will need to enable BlueTooth for this app to work");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            } else {
                nextActivity();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onStart();
        nextActivity();
    }

    public void nextActivity() {
        Intent intent = new Intent(this, GoogleLoginActivity.class);
        startActivity(intent);
    }

    public void useCSV(View view) {
    }
}