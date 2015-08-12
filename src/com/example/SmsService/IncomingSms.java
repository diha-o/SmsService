package com.example.SmsService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by dd on 13.11.2014.
 */
public class IncomingSms extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    static boolean check=false;
    static String strMsgBody="";
    static String strMsgSrc="";
    static String[] time;
    static Integer tt;
    static Boolean timer_tick =true;
    @Override
    public void onReceive(final Context context, Intent intent) {

        Bundle extras = intent.getExtras();
        strMsgBody="";
        strMsgSrc="";
        String strMessage = "";

        if ( extras != null )
        {
            Object[] smsextras = (Object[]) extras.get( "pdus" );

            for ( int i = 0; i < smsextras.length; i++ )
            {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                strMsgBody = smsmsg.getMessageBody().toString();
                strMsgSrc = smsmsg.getOriginatingAddress();

                strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;
            }
            try{
           time = strMsgBody.split("m");
            tt=Integer.parseInt(Character.toString(time[1].charAt(0)));}
            catch (Exception e){//Toast.makeText(context,"Не вверно введена смс",Toast.LENGTH_LONG).show();
             }
        }




        /*   Intent go = new Intent(context, com.example.SmsService.VideoView.class);
            go.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (BroadcastNewSms_Two.first_activity_bool){
            BroadcastNewSms_Two.reconnect1();
            BroadcastNewSms_Two.first_activity.finish();
            timer_tick =false;

        }
            context.startActivity(go);
            CountDownTimer time = new CountDownTimer(60000*2, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    if (BroadcastNewSms_Two.first_activity_bool){
                        Intent go2=new Intent(VideoView.fa, BroadcastNewSms_Two.class);
                        go2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(go2);
                        timer_tick = true;
                    }
                    VideoView.fa.finish();

                }
            }.start (); */
        if (BroadcastNewSms_Two.first_activity_bool) {
            BroadcastNewSms_Two.reconnect1();
            CountDownTimer time = new CountDownTimer(10000, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    new HttpRequestTask(context).execute(context);
                }
            }.start ();


        } else {
            new HttpRequestTask(context).execute(context);
        };

    }
    static void ddd(final Context context){


        if (check == true) {
            try{
            final Intent go = new Intent(context, com.example.SmsService.VideoView.class);
            go.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (BroadcastNewSms_Two.first_activity_bool) {
                context.startActivity(go);
               BroadcastNewSms_Two.first_activity.finish();
                IncomingSms.timer_tick = false;}else{ context.startActivity(go);}
            CountDownTimer time = new CountDownTimer(60000*tt, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    if (BroadcastNewSms_Two.first_activity_bool){
                        Intent go2=new Intent(VideoView.fa, BroadcastNewSms_Two.class);
                        go2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(go2);
                        timer_tick = true;
                    }

                    VideoView.fa.finish();
                }
            }.start (); } catch (Exception e){e.printStackTrace();};
        }else {
            Toast.makeText(context, "Ошибка при отправке SMS"+String.valueOf(check), Toast.LENGTH_LONG).show();
        }
    }}


