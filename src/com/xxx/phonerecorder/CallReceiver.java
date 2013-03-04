package com.xxx.phonerecorder;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";
    private String mPhoneNumber = null;
    private boolean mIsIncomingCall = false;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
        if(arg1.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            mPhoneNumber = arg1.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            arg0.startService(new Intent(arg0, RecorderService.class)
                .putExtra(Intent.EXTRA_PHONE_NUMBER, mPhoneNumber));
        }else{
            TelephonyManager tm = (TelephonyManager)arg0
                    .getSystemService(Service.TELEPHONY_SERVICE);

            switch(tm.getCallState()){
                case TelephonyManager.CALL_STATE_IDLE:{
                    arg0.stopService(new Intent(arg0, RecorderService.class));
                    mIsIncomingCall = false;
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK:{
                    if(mIsIncomingCall){
                        arg0.startService(new Intent(arg0, RecorderService.class)
                            .putExtra(Intent.EXTRA_PHONE_NUMBER, mPhoneNumber));
                    }
                    break;
                }
                case TelephonyManager.CALL_STATE_RINGING:{
                    mPhoneNumber = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    mIsIncomingCall = true;
                    break;
                }
                default:
                    break;
            }
        }
    }

}
