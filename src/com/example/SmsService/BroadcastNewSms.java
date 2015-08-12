package com.example.SmsService;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.*;

import java.io.IOException;

public class BroadcastNewSms extends Activity implements SurfaceHolder.Callback {
    /**
     * Called when the activity is first created.
     */


  //  private static final String FILE_NAME = "big_buck_bunny.mp4";
   // private static final String FILE_URL = "http://www.w3schools.com/html/mov_bbb.mp4";
  //  String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";//http://127.0.0.1:59777/smb/192.168.0.250/Files/Interstellar.mp4
    Context context;
    private MediaPlayer mp;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    public static String filepath="";//http://127.0.0.1:59777/smb/192.168.0.250/Files/Interstellar.mp4
    private View decorView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);   //new
        getActionBar().hide();                                   //new
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mPreview = (SurfaceView) findViewById(R.id.video_surface);
            holder = mPreview.getHolder();
            holder.addCallback(this);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mp = new MediaPlayer();
        decorView = findViewById(R.id.firstsettings_two_layout);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        registerReceiver(new ScreenReceiver(), new IntentFilter("android.intent.action.USER_PRESENT"));
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
    protected void onPause(){


        mp.stop();
        mp.reset();
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();

        if (!isScreenOn) {
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TEST");
            wakeLock.acquire();
            surfaceCreated(holder);
        }
        super.onPause();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        mp.setDisplay(holder);
        mp.setLooping(true);
        play();

    }
        public void play(){
            try {
                mp.setDataSource(filepath);

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





}
