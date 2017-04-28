package com.example.ponomarevlad.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private ProgressBar playSeekBar;

    private ImageButton buttonPlay;

    private Button buttonStopPlay;

    private ImageButton shareButton;

    private MediaPlayer player;

    private boolean StopPlay=true;

    /** Called when the activity is first created. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();

        initializeMediaPlayer();
    }

    private void initializeUIElements() {

        playSeekBar = (ProgressBar) findViewById(R.id.progressBar);
        playSeekBar.setMax(100);
        playSeekBar.setVisibility(View.INVISIBLE);

        buttonPlay = (ImageButton) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        shareButton = (ImageButton) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(this);

        //buttonStopPlay = (Button) findViewById(R.id.buttonStopPlay);
        //buttonStopPlay.setEnabled(false);
        //buttonStopPlay.setOnClickListener(this);

    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            if (StopPlay) {
                startPlaying();
                StopPlay=false;
            } else {
                stopPlaying();
                StopPlay=true;
            }

        } else if(v == shareButton){
            initShare();
        }
    }

    private void startPlaying() {
        //buttonStopPlay.setEnabled(true);
        //buttonPlay.setEnabled(false);

        playSeekBar.setVisibility(View.VISIBLE);

        player.prepareAsync();

        player.setOnPreparedListener(new OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }

        //buttonPlay.setEnabled(true);
        //buttonStopPlay.setEnabled(false);
        playSeekBar.setVisibility(View.INVISIBLE);
    }

    private void initializeMediaPlayer() {
        player = new MediaPlayer();
        try {
            player.setDataSource("http://s2.radioboss.fm:8043/stream");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                playSeekBar.setSecondaryProgress(percent);
                Log.i("Buffering", "" + percent);
            }
        });
    }

    private void initShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Я слушаю лучшее молодежное радио TemaFm http://app54.ru");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.stop();
        }
    }



}
