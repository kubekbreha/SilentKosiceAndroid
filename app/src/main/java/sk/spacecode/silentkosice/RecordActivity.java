package sk.spacecode.silentkosice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;


public class RecordActivity extends AppCompatActivity {

    float volume = 10000;
    private MyMediaRecorder mRecorder;
    private TextView actualDB;
    private TextView resultDB;
    private ImageView progressCircle2;
    private ImageButton imageButton;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;

    private boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        mRecorder = new MyMediaRecorder();

        progressCircle2 = findViewById(R.id.imageView_circleProgress2);
        imageButton = findViewById(R.id.imageView_circleProgress);

        actualDB = findViewById(R.id.textView_currentDB);
        resultDB = findViewById(R.id.textView_result);

        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            Log.v("file", "file =" + file.getAbsolutePath());
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), "Something went terribly wrong", Toast.LENGTH_LONG).show();
        }


        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!recording){
                    resultDB.setText("");

                    recording = true;
                    File file = FileUtil.createFile("temp.amr");
                    if (file != null) {
                        Log.v("file", "file =" + file.getAbsolutePath());
                        startRecord(file);
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went terribly wrong", Toast.LENGTH_LONG).show();
                    }
                }else {
                    progressCircle2.getLayoutParams().height = 340;
                    progressCircle2.getLayoutParams().width = 340;
                    mRecorder.delete();
                    handler.removeMessages(msgWhat);
                    actualDB.setText("Record");

                    resultDB.setText("Average DB was 99");
                }
            }
        });

        GPSHelper gps = new GPSHelper(this);
        gps.getMyLocation();
        Log.e("TVOJAMATKA", gps.getLatitude());
        Log.e("TVOJAMATKA", gps.getLongitude());

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (this.hasMessages(msgWhat)) {
                return;
            }
            volume = mRecorder.getMaxAmplitude();
            if(volume > 0 && volume < 1000000) {
                World.setDbCount(20 * (float)(Math.log10(volume)));
                actualDB.setText((int)World.dbCount+" DB");

                imageButton.getLayoutParams().height = (((int)World.dbCount*100)/140)*12;
                imageButton.getLayoutParams().width = (((int)World.dbCount*100)/140)*12;
                progressCircle2.getLayoutParams().height = (((int)World.dbCount*100)/90)*11;
                progressCircle2.getLayoutParams().width = (((int)World.dbCount*100)/90)*11;
            }

            handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
        }
    };

    private void startListenAudio() {
        handler.sendEmptyMessageDelayed(msgWhat, refreshTime);
    }


    public void startRecord(File fFile){
        try{
            mRecorder.setMyRecAudioFile(fFile);
            if (mRecorder.startRecorder()) {
                startListenAudio();
            }else{
                Toast.makeText(this, "Recording already started", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Toast.makeText(this, "Something went terribly wrong", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mRecorder.delete();
        handler.removeMessages(msgWhat);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(msgWhat);
        mRecorder.delete();
        super.onDestroy();
    }
}
