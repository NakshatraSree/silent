package com.example.sielent;

import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;



public class SilentModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SilentModeReceiver", "Received silent mode alarm");

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Log.d("SilentModeReceiver", "Phone set to silent mode");
        } else {
            Log.e("SilentModeReceiver", "AudioManager is null");
        }
    }
}
