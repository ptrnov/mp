package com.cudo.mproject.Menu.Audio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cudo.mproject.BaseActivity;
import com.cudo.mproject.Model.MActivity;
import com.cudo.mproject.Model.OfflineDataTransaction;
import com.cudo.mproject.Model.Photo;
import com.cudo.mproject.Model.Project;
import com.cudo.mproject.Model.WorkPkg;
import com.cudo.mproject.R;
import com.cudo.mproject.Utils.FileUtils;

import java.io.IOException;

import butterknife.ButterKnife;

import static com.cudo.mproject.Utils.FileUtils.rootpath;

/*
    source : https://medium.com/@ssaurel/create-an-audio-recorder-for-android-94dc7874f3d
 */
public class AudioActivity  extends BaseActivity{//} AppCompatActivity {
    String TAG = getClass().getSimpleName();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_audio);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
    Project project;
    MActivity activity;
    WorkPkg workPkg;
    OfflineDataTransaction dataOffline;
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;

    String getFolderPath() {
        return rootpath(this, project.getProject_id());
    }

    String getPathPhoto() throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + Photo.getNextID(realm), ".jpg");
    }

    String getPathExt() throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + project.getId() + "_" + Photo.getNextID(realm), ".ext");
    }

    String getPathPhoto(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + Photo.getNextID(realm), ".jpg");
    }

    String getPathExt(int id) throws IOException {
        return FileUtils.path(this, getFolderPath(), project.getProject_id() + "_" + id + "_" + Photo.getNextID(realm), ".ext");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
//
        ButterKnife.bind(this);
//
        Intent intent = getIntent();
        String sProjectid = intent.getStringExtra("sProjectid");
        String swp_id = intent.getStringExtra("swp_id");
        String sactivity_id = intent.getStringExtra("sactivity_id");
        String isi_statusPhoto = intent.getStringExtra("status_photo");
        String status_photo = "";
        String date_photo = "";
        String time_photo = "";
        String idOffline = "";
//
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        record = (Button) findViewById(R.id.record);
        stop.setEnabled(false);
        play.setEnabled(false);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IllegalStateException ise) {
                    // make something ...
                    Toast.makeText(getApplicationContext(), "IllegalStateException"+ise, Toast.LENGTH_LONG).show();
                } catch (IOException ioe) {
                    Toast.makeText(getApplicationContext(), "IllegalStateException"+ioe, Toast.LENGTH_LONG).show();
                    // make something
                }
                record.setEnabled(false);
                stop.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;
                record.setEnabled(true);
                stop.setEnabled(false);
                play.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Audio Recorder successfully", Toast.LENGTH_LONG).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(outputFile);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // make something
                }
            }
        });
    }
}

