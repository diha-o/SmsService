package com.example.SmsService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by dd on 07.03.2015.
 */
public class FirstSettings_two extends Activity {

    CheckBox ch1;
    CheckBox ch2;
    private EditText first_string;
    private  EditText two_string;
    private  EditText time_interval;
    private Boolean first_activity=false;
    public static String file_video = "";
    public static int tt_video=0;
    static String first_ssid="";
    private CheckBox volume;
    static Boolean volume_key=false;
    static String filepath = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstsettings_two);

        Button nextStep = (Button) findViewById(R.id.button);
        // rg = (RadioGroup) findViewById(R.id.rg);
        Button setsettings = (Button) findViewById(R.id.button2);
        Button song_settings = (Button) findViewById(R.id.button3);
        ch1 = (CheckBox) findViewById (R.id.checkBox);
        ch2 = (CheckBox) findViewById (R.id.checkBox2);
        first_string = (EditText) findViewById(R.id.editText3);
        two_string = (EditText) findViewById(R.id.editText4);
        time_interval = (EditText) findViewById(R.id.editText5);
        file_video = two_string.getText().toString();
        volume=(CheckBox) findViewById(R.id.checkBox3);
        try {
            first_ssid=getFirst_Ssid(getApplicationContext());
        }catch (Exception e){
            Log.d("Exeption",e.getMessage());
        }

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (first_activity){
                    startActivity(new Intent(FirstSettings_two.this, BroadcastNewSms_Two.class));
                    finish();
                }
                else {
                    startActivity(new Intent(FirstSettings_two.this, BroadcastNewSms.class));
                    finish();
                }
            }
        });
        setsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filepath= first_string.getText().toString();
                BroadcastNewSms.filepath = first_string.getText().toString() + two_string.getText().toString();
                if ((ch1.isChecked()) && (ch2.isChecked() == false)) {
                    first_activity = false;
                    Toast.makeText(getApplicationContext(), "Постоянно воспроизводить с сервера ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Загружать на устройство", Toast.LENGTH_LONG).show();
                    String t = time_interval.getText().toString();
                    tt_video = Integer.parseInt(t);
                    first_activity = true;
                }
                EditText file_path = (EditText)findViewById(R.id.editText_for_file_path);
                EditText file_image =  (EditText)findViewById(R.id.editText2_for_image);
                EditText file_video = (EditText)findViewById(R.id.editText6_for_video);
                EditText nomer_machina_for_server = (EditText)findViewById(R.id.editText_nomer_for_server);
                VideoView.file_path_on_image_or_video =file_path.getText().toString() ;
                VideoView.name_file_on_image = file_image.getText().toString();
                VideoView.name_file_on_video = file_video.getText().toString();
                HttpRequestTask.nomer_machina = nomer_machina_for_server.getText().toString();
                if (volume.isChecked()==true){
                    volume_key=true;
                }
            }
        });

    song_settings.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivityForResult(new Intent(FirstSettings_two.this, Song_for_chanel.class), 1);
        }
    });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result=data.getStringExtra("result");
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResul
    public static String getFirst_Ssid(Context context) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            BroadcastNewSms_Two.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = BroadcastNewSms_Two.wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }
}