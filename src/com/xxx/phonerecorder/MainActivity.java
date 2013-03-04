
package com.xxx.phonerecorder;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private TelephonyManager mTM;
    private MediaRecorder mMR;
    private File amrFile;
    private boolean isRecording = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTM = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        //mTM.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        mMR = new MediaRecorder();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

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

    public class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: {
                    Log.e(TAG, "CALL_STATE_IDLE = " + incomingNumber);
                    if (isRecording) {
                        isRecording = false;
                        mMR.stop();
                    }
                    break;
                }
                case TelephonyManager.CALL_STATE_RINGING: {
                    Log.e(TAG, "CALL_STATE_RINGING = " + incomingNumber);
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK: {
                    Log.e(TAG, "CALL_STATE_OFFHOOK = " + incomingNumber);
                    try {
                        recordStream();
                        isRecording = true;
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    break;
                }
                default:
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
}
