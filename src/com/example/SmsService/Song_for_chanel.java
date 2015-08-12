package com.example.SmsService;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 20.03.2015.
 */
public class Song_for_chanel extends Activity implements SeekBar.OnSeekBarChangeListener {
    public List<String> songs;
    public ArrayList<Uri> storage;
    MediaPlayer mMedia;
    static Uri storage_fromMusic;

    int mSelectedItem_forsong = -1;
    int mLastItem_forsong = -1;
    int save = -1;
    SeekBar sb;
   static AudioManager am;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_for_chanel);
        ListView listview = (ListView) findViewById(R.id.listView_INsONG);
        sb=(SeekBar)findViewById(R.id.seekBar);
        am=(AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        Button button_song = (Button) findViewById(R.id.button4);
        int maxV = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curV = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        sb.setMax(maxV);
        sb.setProgress(curV);
        sb.setOnSeekBarChangeListener(this);
        alllistmusic();
        listview.setAdapter(mAdapter);
        mMedia = new MediaPlayer();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {

                        mMedia.stop();
                        mMedia.reset();
                        mMedia = null;
                        mMedia = new MediaPlayer();

                    Uri m = storage.get(position);
                    try {
                        mMedia.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMedia.setDataSource(getApplicationContext(), m);
                        storage_fromMusic = m;
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


               try {
                   mSelectedItem_forsong = position;
                   mAdapter.notifyDataSetChanged();
                } catch (Exception e){
//
                   Toast.makeText(getApplicationContext(),"Упс, попробуйте еще раз",Toast.LENGTH_LONG).show();
               }
            }


            });
        button_song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = 1;
                if ( mMedia.isPlaying()){
                    mMedia.stop();
                }
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",result);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

    }
    public void alllistmusic(){
        RingtoneManager manager=new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_ALARM | RingtoneManager.TYPE_RINGTONE|RingtoneManager.TYPE_ALL);

        songs = new ArrayList<String>();
        storage = new ArrayList<Uri>();
        Cursor cursor=manager.getCursor();
        int i=0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext(), i++) {
            String title=cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            songs.add(title);

            storage.add(manager.getRingtoneUri(i));
        }



    }
    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return storage.size();

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
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_song, parent, false);
            TextView title = (TextView) retval.findViewById(R.id.textVie_forSong);

                title.setText(songs.get(position));
            if (position == mSelectedItem_forsong) {
                retval.setBackgroundColor(Color.BLACK);
            }
            return retval;
        };




    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        am.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}