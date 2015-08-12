package com.example.SmsService;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by dd on 16.06.2015.
 */

public  class  ScreenReceiver  extends BroadcastReceiver {

    @Override
    public  void onReceive ( Context context ,  Intent intent )  {

        KeyguardManager keyguardManager =  (KeyguardManager) context . getSystemService ( Context. KEYGUARD_SERVICE );
        if  ( keyguardManager . inKeyguardRestrictedInputMode())  {

            //phone был разблокирован, делать вещи здесь

        }
    }
}

//public class ScreenReceiver extends  BroadcastReceiver {
//    PowerManager.WakeLock wakeLock;
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF))) {
//
//            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TEST");
//            wakeLock.acquire();
//
//            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            Intent inten = new Intent(context,BroadcastNewSms.class);
//            PendingIntent pi = PendingIntent.getActivity(context, 0, inten, 0);
//            alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,  100, pi);
//        }
//    }
//
//    // Finish your WakeLock HERE. call this method after U put the activity in front or when u exit from the new activity.
//    public void finishWakeLocker(){
//        if (wakeLock != null)
//            wakeLock.release();
//    }
//}
