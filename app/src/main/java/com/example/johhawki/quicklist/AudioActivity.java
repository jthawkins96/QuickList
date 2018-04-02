package com.example.johhawki.quicklist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class AudioActivity extends AppCompatActivity {
    private Button recordbtn;
    private Button stoprecbtn;
    private Button playbtn;
    private Button stopbtn;
    private TextView debug;
    MediaPlayer myPlayer;
    MediaRecorder myRecorder;
    String afp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        recordbtn=findViewById(R.id.record);
        stoprecbtn=findViewById(R.id.stoprec);
        playbtn=findViewById(R.id.play);
        stopbtn=findViewById(R.id.stop);
        debug=findViewById(R.id.debug);

        stoprecbtn.setEnabled(false);
        playbtn.setEnabled(false);
        stopbtn.setEnabled(false);

        PackageManager myMan = this.getPackageManager();
        if(myMan.hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
            recordbtn.setEnabled(true);
        else
            recordbtn.setEnabled(false);

        afp= Environment.getExternalStorageDirectory().getAbsolutePath()+"/myAudio.3gp";
        debug.setText("File: "+afp+"\nState: "+Environment.getExternalStorageState());
    }
    public void Play(View v) {
        playbtn.setEnabled(false);
        stopbtn.setEnabled(true);
        myPlayer=new MediaPlayer();
        try {
            myPlayer.setDataSource(afp);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        try {
            myPlayer.prepare();
            myPlayer.start();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void Stop(View v) {
        stopbtn.setEnabled(false);
        playbtn.setEnabled(true);
        myPlayer.stop();
    }

    public void recordClick(View v) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.RECORD_AUDIO },
                    10);
        } else {
            recordbtn.setEnabled(false);
            stoprecbtn.setEnabled(true);

            myRecorder=new MediaRecorder();
            myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            myRecorder.setOutputFile(afp);
            try {
                myRecorder.prepare();
                myRecorder.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
    public void stopRecordClick(View v) {
        stoprecbtn.setEnabled(false);
        playbtn.setEnabled(true);
        myRecorder.stop();
    }
}

