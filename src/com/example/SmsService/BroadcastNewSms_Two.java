package com.example.SmsService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.text.TextUtils;
import android.view.*;
import android.widget.Toast;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by dd on 10.03.2015.
 */
public class BroadcastNewSms_Two extends Activity implements SurfaceHolder.Callback{
    Context context;
    private MediaPlayer mp;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    public static Activity first_activity;
    public  static WifiManager wifiManager;
    static Boolean first_activity_bool=false;
    public String exeption="";
    private View decorView;
    static Boolean reconekting =false;
    private static final int HIDER_FLAGS = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //new
        getActionBar().hide();                                   //new
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       setContentView(R.layout.main);
        first_activity_bool = true;
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        first_activity = this;
        context = getApplicationContext();
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        try {
            if ((wifi.isConnected() == false)) {
                Toast.makeText(getApplicationContext(), "подключение к сети Wi-Fi", Toast.LENGTH_LONG).show();
                reconnect5();
            } else {
              //  Toast.makeText(getApplicationContext(), "Уже соеденено", Toast.LENGTH_LONG).show();
               File_Video downloadFile_video = new File_Video();
                downloadFile_video.execute(BroadcastNewSms.filepath);
           }

       } catch (Exception e) {Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();}


        mPreview = (SurfaceView) findViewById(R.id.video_surface);
        holder = mPreview.getHolder();
        holder.addCallback(BroadcastNewSms_Two.this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mp = new MediaPlayer();

        decorView = findViewById(R.id.firstsettings_two_layout);

        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        decorView.setSystemUiVisibility(uiOptions);
        View decorView2 = getWindow().getDecorView();
        decorView2.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {

                            timer1();

                        } else {

                        }
                        }});

                    timer2();

                }



    public  void timer1(){
        CountDownTimer time_activity = new CountDownTimer(10000, 100) {
            public  void onTick(long millisUntilFinished) {
                decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
            public void onFinish() {
                decorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

            }
        }.start ();
    }
public void  timer2(){
   // Intent go = new Intent(context, com.example.SmsService.VideoView.class);
    //go.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //context.startActivity(go);
   CountDownTimer time_activity = new CountDownTimer(60000*FirstSettings_two.tt_video, 1000) {
        public  void onTick(long millisUntilFinished) {
            if (IncomingSms.timer_tick==false){
                this.cancel();
            }
        }
        public void onFinish() {
          //  try {
                if (IncomingSms.timer_tick==false) {
                    this.cancel();
                } else {
                    startActivity(new Intent(getApplicationContext(), BroadcastNewSms_Two.class));
                    first_activity.finish();
                }
            //}catch (Exception e) {Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_LONG).show();}
        }
    }.start ();
}
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        return false;
    }
    protected void onPause(){

        super.onPause();
        mp.stop();
        mp.reset();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();

        if (!isScreenOn) {
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TEST");
            wakeLock.acquire();
            surfaceCreated(holder);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mp.setDisplay(holder);
        mp.setLooping(true);
        if (mp.isPlaying()==false){
            play();
        }

    }



    public void play(){


        try {
            mp.setDataSource(Environment.getExternalStorageDirectory()
                    + "/pathofthefile/"+FirstSettings_two.file_video);

            mp.prepare();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }






    private class File_Video extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {

            File folders = new File(Environment.getExternalStorageDirectory()
                    + "/pathofthefile/");
            folders.mkdirs();

            File file;
            file = new File(Environment.getExternalStorageDirectory()
                    + "/pathofthefile/"+FirstSettings_two.file_video);


            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            try {
                URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a typical 0-100%
                // progress bar
                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
                return "Downloaded";
            } catch (Exception e) {
             //   Toast.makeText(context,
               //         "exeption",Toast.LENGTH_LONG).show();

                return null;

            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result.equals("Downloaded")) {
                    Toast.makeText(getApplicationContext(), "Загрузка видео завершена", Toast.LENGTH_LONG).show();
                    play();
                    Toast.makeText(getApplicationContext(), "отключение Wi-Fi", Toast.LENGTH_LONG).show();
                    disconnect();
                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка с загрузкой видео", Toast.LENGTH_LONG).show();
                }
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
            }
        }
    public static void reconnect1(){
        try {
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if (i.SSID.equals(FirstSettings_two.first_ssid)) {
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        } } catch (Exception e){e.printStackTrace();};
        }
    public void reconnect5(){
        try {
            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for( WifiConfiguration i : list ) {
                if (i.SSID.equals(FirstSettings_two.first_ssid)) {
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                   File_Video downloadFile_video = new File_Video();
                   downloadFile_video.execute( BroadcastNewSms.filepath);
                    break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage().toString(),Toast.LENGTH_LONG).show();};
    }

    public static void disconnect(){
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID.equals( FirstSettings_two.first_ssid )) {
                WifiConfiguration conf =  new WifiConfiguration();
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wifiManager.disconnect();
                break;
            }
        }
    }
    public static String getTwo_Ssid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
             wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }


    }

