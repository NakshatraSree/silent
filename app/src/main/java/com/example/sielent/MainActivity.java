package com.example.sielent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AudioManager audioManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        TimePicker startTimePicker = findViewById(R.id.startTimePicker);
        Button setTimeButton = findViewById(R.id.setTimeButton);
        Button cancelTimeButton = findViewById(R.id.cancelTimeButton);

        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Get the selected time from the TimePicker
                    int startHour = startTimePicker.getHour();
                    int startMinute = startTimePicker.getMinute();

                    // Set up the alarm to put the phone in silent mode
                    setSilentModeAlarm(startHour, startMinute);

                    // Show a toast message to indicate that the silent mode is set
                    showToast("Silent mode set at " + startHour + ":" + startMinute);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("SetTimeButton", "Error setting silent mode", e);
                }
            }
        });


        cancelTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel the previously set alarm
                cancelSilentModeAlarm();

                // Show a toast message to indicate that the silent mode is canceled
                showToast("Silent mode canceled");
            }
        });
    }

    private void setSilentModeAlarm(int startHour, int startMinute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent that will be triggered when the alarm goes off
        Intent intent = new Intent(this, SilentModeReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set up the alarm to trigger at the specified start time
        long startTime = getAlarmTimeInMillis(startHour, startMinute);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + startTime,
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    private void cancelSilentModeAlarm() {
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    private long getAlarmTimeInMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long currentTime = System.currentTimeMillis();
        long selectedTime = calendar.getTimeInMillis();

        // If the selected time has already passed, set it for the next day
        if (currentTime > selectedTime) {
            selectedTime += AlarmManager.INTERVAL_DAY;
        }

        return selectedTime - currentTime;
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
