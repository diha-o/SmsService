package com.example.SmsService;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.*;
import com.google.vrtoolkit.cardboard.*;
import com.google.vrtoolkit.cardboard.sensors.MagnetSensor;
import com.meetme.android.horizontal.HorizontalListView;
import com.squareup.picasso.Picasso;

import javax.microedition.khronos.egl.EGLConfig;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by dd on 23.11.2014.
 */
public class VideoView extends CardboardActivity implements MagnetSensor.OnCardboardTriggerListener,CardboardView.StereoRenderer {
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;
    public static Activity fa;
    ImageView image_list_icon;
    int mSelectedItem=0;
    android.widget.VideoView vidView;
    private SensorManager mSensorManager;
    final int[] x = {0};
    private Vibrator mVibrator;
    LinearLayout anim;
    ArrayList<String> lines_video = new ArrayList<String>();
    ArrayList<String> lines_image = new ArrayList<String>();
     HorizontalListView listview ;
     HorizontalListView listview2;
    MediaPlayer mMedia;
    static String file_path_on_image_or_video="";//http://127.0.0.1:59777/smb/192.168.0.250/Files/
    static String name_file_on_image="";
    static String name_file_on_video="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        fa=this;
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        vidView = (android.widget.VideoView)findViewById(R.id.videoView);
        String vidAddress = BroadcastNewSms.filepath;
        Uri vidUri = Uri.parse(vidAddress);
        vidView.setVideoURI(vidUri);
        vidView.start();
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)&&(FirstSettings_two.volume_key==true)) {
                    onCardboardTrigger();
            }
                return true;
            }
        });
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        // Associate a CardboardView.StereoRenderer with cardboardView.
        cardboardView.setRenderer(this);
        // Associate the cardboardView with this activity.
        setCardboardView(cardboardView);
        mMedia = new MediaPlayer();
        anim = (LinearLayout)findViewById(R.id.by_anim);

         listview = (HorizontalListView) findViewById(R.id.listview);
         listview2 = (HorizontalListView) findViewById(R.id.listview2);

        listview.setAdapter(mAdapter);
        listview2.setAdapter(mAdapter);

        listview.setScrollContainer(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mSelectedItem = position;
                mAdapter.notifyDataSetChanged();

                listview.post(new Runnable() {

                    public void run() {
                        View v = listview.getChildAt(position);
                        if (v != null) {
                            int centerX = v.getLeft();
                            Log.e("ee", String.valueOf(centerX));
                        }


                            listview.scrollTo(x[0]);
                            listview2.scrollTo(x[0]);
                        x[0] += 192;
                        CountDownTimer time = new CountDownTimer(5000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }
                            public void onFinish() {
                                viseb_gone();
                            }
                        }.start ();

                    }

                });
                try {

                    try {
                        mMedia.stop();
                        mMedia.reset();
                        mMedia = new MediaPlayer();
                        mMedia.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMedia.setDataSource(getApplicationContext(), Song_for_chanel. storage_fromMusic );
                        mMedia.prepare();
                        mMedia.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    mMedia = null;
                    mMedia = new MediaPlayer();
                    Toast.makeText(getApplicationContext(),"Извините, но я не могу воспроизвести эту мелодию",Toast.LENGTH_LONG).show();
                }

            }

        });
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = position;
                mAdapter.notifyDataSetChanged();


            }
        });



        File_Video downloadFile_video = new File_Video();
        File_Image downloadFile_image = new File_Image();
       // downloadFile_image.execute("http://127.0.0.1:59777/smb/192.168.0.250/Files/images.txt");
       // downloadFile_video.execute("http://127.0.0.1:59777/smb/192.168.0.250/Files/video.txt");
        downloadFile_image.execute(file_path_on_image_or_video + name_file_on_image);
        downloadFile_video.execute(file_path_on_image_or_video + name_file_on_video);
        }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
            return true;
        else
            return false;
    }
    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;//

        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_leyout, parent, false);
            TextView title = (TextView) retval.findViewById(R.id.title);
            image_list_icon = (ImageView)retval.findViewById(R.id.image_fromlist);


             title.setText(lines_image.get(position%(lines_image.size())));
             title.setVisibility(View.GONE);

              if (title.getText() == lines_image.get(position% lines_image.size())) {
                  Picasso.with(retval.getContext()).load( lines_image.get(position % lines_image.size())).into(image_list_icon);// url_first + lines_image.get(position % lines_image.size())
              }



                    ///// another, for video /////
                    if (position == mSelectedItem) {
                        retval.setBackgroundColor(Color.WHITE);
                        retval.getBackground().setAlpha(80);
                        retval.setMinimumHeight(retval.getHeight() + 15);
                        image_list_icon.getLayoutParams().height = image_list_icon.getLayoutParams().height + 40;
                        image_list_icon.getLayoutParams().width = image_list_icon.getLayoutParams().width + 40;


                        if (title.getText() == lines_image.get( position% lines_image.size())) {
                            String vidAddress = lines_video.get(position% lines_image.size());
                            Uri vidUri = Uri.parse(vidAddress);
                            vidView.setVideoURI(vidUri);

                             vidView.start();
                        }
//

                    } else if (position != mSelectedItem)

                    {

                        image_list_icon.getLayoutParams().height = image_list_icon.getLayoutParams().height - 10;

                    }
                    return retval;
                }




    };


    @Override
    public void onCardboardTrigger() {
       // Toast.makeText(getApplicationContext(),"Work",Toast.LENGTH_LONG).show();
        viseb_show();
       listview.performItemClick(listview.getAdapter().getView(mSelectedItem+1, null, (ViewGroup)listview.getParent()),mSelectedItem+1, listview.getAdapter().getItemId(mSelectedItem+1));
        mVibrator.vibrate(50);
    }


    public void viseb_gone(){
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(1.0f, 0.0f);

        animation.setDuration(3500);
        set.addAnimation(animation);

        anim.startAnimation(animation);
        anim.setVisibility(View.GONE);
    }
    public void viseb_show(){
        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);

        animation.setDuration(500);
        set.addAnimation(animation);

        anim.startAnimation(animation);
        anim.setVisibility(View.VISIBLE);
    }
    @Override
    public void onNewFrame(HeadTransform headTransform) {

    }

    @Override
    public void onDrawEye(Eye eye) {

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

    }

    @Override
    public void onRendererShutdown() {

    }

  /*  protected void onPause(){

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean isScreenOn = powerManager.isScreenOn();

        if (!isScreenOn) {
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "TEST");
            wakeLock.acquire();

        }
        super.onPause();
    }*/
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {
//            Toast.makeText(this, "Search key pressed", Toast.LENGTH_SHORT).show();
//
//            return false;
//        }
//
//        return super.dispatchKeyEvent(event);
//    }



    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_POWER) {
            Toast.makeText(this, "wefwefewf", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }
    private class File_Video extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {

            File folders = new File(Environment.getExternalStorageDirectory()
                    + "/pathofthefile/");
            folders.mkdirs();

            File file;
            file = new File(Environment.getExternalStorageDirectory()
                    + "/pathofthefile/file_video.txt");


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

            if (result.equals("Downloaded")) {
                Toast.makeText(getApplicationContext(),"Download file video",Toast.LENGTH_LONG).show();
                File fl = new File(Environment.getExternalStorageDirectory()
                        + "/pathofthefile/","file_video.txt");
                BufferedReader buf = null;
                try {
                    buf = new BufferedReader(new FileReader(fl));


                    boolean hasNextLine = true;
                    while (hasNextLine) {
                        String line = null;

                        line = buf.readLine();
                        lines_video.add(FirstSettings_two.filepath+line);
                        hasNextLine = line != null;
                    }
                }catch (Exception e) {e.printStackTrace();}

            }
        }
    }
    private class File_Image extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... sUrl) {

            File folders = new File(Environment.getExternalStorageDirectory()
                    + "/pathofthefile/");
            folders.mkdirs();

            File file;
            file = new File(Environment.getExternalStorageDirectory()
                    + "/pathofthefile/file_image.txt");


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

            if (result!= null && result.equals("Downloaded")) {
                Toast.makeText(getApplicationContext(),"Download file image",Toast.LENGTH_LONG).show();
                File fl = new File(Environment.getExternalStorageDirectory()
                        + "/pathofthefile/","file_image.txt");
                BufferedReader buf = null;
                try {
                    buf = new BufferedReader(new FileReader(fl));


                    boolean hasNextLine = true;
                    while (hasNextLine) {
                        String line = null;
                        line = buf.readLine();
                        lines_image.add(FirstSettings_two.filepath+line);
                        hasNextLine = line != null;
                    }
                }catch (Exception e) {e.printStackTrace();}
                lines_image.remove(lines_image.size() - 1);
            }
        }
    }

}