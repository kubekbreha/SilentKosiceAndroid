package sk.spacecode.silentkosice;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class RecordActivity extends AppCompatActivity {

    float volume = 10000;
    private MyMediaRecorder mRecorder;
    private TextView text;
    private ImageView progressCircle;
    private static final int msgWhat = 0x1001;
    private static final int refreshTime = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        mRecorder = new MyMediaRecorder();
        text = findViewById(R.id.textView_currentDB);
        progressCircle = findViewById(R.id.imageView_circleProgress);
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
                text.setText((int)World.dbCount+" DB");

                progressCircle.getLayoutParams().height = (((int)World.dbCount*100)/140)*15;
                progressCircle.getLayoutParams().width = (((int)World.dbCount*100)/140)*15;

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
        File file = FileUtil.createFile("temp.amr");
        if (file != null) {
            Log.v("file", "file =" + file.getAbsolutePath());
            startRecord(file);
        } else {
            Toast.makeText(getApplicationContext(), "Something went terribly wrong", Toast.LENGTH_LONG).show();
        }
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
