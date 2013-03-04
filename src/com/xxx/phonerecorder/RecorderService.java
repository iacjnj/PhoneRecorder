package com.xxx.phonerecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecorderService extends Service {
    private static final String TAG = "RecorderService";
    private boolean mIsRecording = false;
    private MediaRecorder mMR;
    private File amrFile;

    private void recordStream() throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        String time = formatter.format(new Date());

        mMR.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mMR.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mMR.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // File.separator;
        amrFile = File.createTempFile(time, ".amr",
                Environment.getExternalStorageDirectory());
        mMR.setOutputFile(amrFile.getAbsolutePath());
        mMR.prepare();
        mMR.start();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate(){
        super.onCreate();
    }

    public void onDestroy(){
        if(mIsRecording){
            mIsRecording = false;
            mMR.stop();
        }
        super.onDestroy();
    }

    public void onStart(Intent it, int startId){
        if(it.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
                .endsWith("10086")){
            mIsRecording = true;
            mMR = new MediaRecorder();
            try{
                recordStream();
            }catch(Exception e){
                Log.e(TAG, e.toString());
            }
        }
        super.onStart(it, startId);
    }
}
