package com.example.admin.tetris;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private ImageButton rotateButton;
    private ImageButton rightButton;
    private ImageButton downButton;
    private ImageButton leftButton;

    private TextView pointTextView;
    private TextView highscoreLevelTextView;
    private TextView currentLevelTextView;
    private Tetris tetris;
    private boolean pause = true;
    private MediaPlayer mediaPlayer;
    private int stopMediaplayer;
    private final GameBoard gameBoard = new GameBoard();
    private Bundle arguments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse("android.resource://com.example.admin.tetris/raw/tetrismusik"));
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }


        buttonStart = (Button) findViewById(R.id.buttonstart);
        Button buttonReset = (Button) findViewById(R.id.buttonreset);
        rotateButton = (ImageButton) findViewById(R.id.rotateButton);
        rightButton = (ImageButton) findViewById(R.id.rightButton);
        downButton = (ImageButton) findViewById(R.id.downButton);
        leftButton = (ImageButton) findViewById(R.id.leftButton);
        pointTextView = (TextView) findViewById(R.id.textViewPunkte);
        highscoreLevelTextView = (TextView) findViewById(R.id.textViewHighscore);
        currentLevelTextView = (TextView) findViewById(R.id.levelText);

        tetris = new Tetris(this, gameBoard);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(805, 1505);
        tetris.setLayoutParams(params);
        ConstraintLayout relativeTetris = (ConstraintLayout) findViewById(R.id.gamefield);

        tetris.setBackgroundColor(Color.rgb(250, 130, 228));
        relativeTetris.addView(tetris);
        buttonStart.setOnClickListener(new View.OnClickListener() {

            int tmp = 0;

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                stopMediaplayer = mediaPlayer.getCurrentPosition();
                tmp++;

                if (buttonStart.getText().equals("Start")) {
                    buttonStart.setText("Pause");
                    pause = false;

                    if (tmp == 1) {
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                    } else if (tmp > 1) {
                        mediaPlayer.seekTo(stopMediaplayer);
                        mediaPlayer.start();
                    }
                } else if (buttonStart.getText().equals("Pause")) {
                    buttonStart.setText("Start");
                    pause = true;
                    mediaPlayer.pause();
                }

            }
        });

        buttonReset.setOnClickListener(v -> tetris.resetGame());
    }

    @Override
    public void onRestart() {
        super.onRestart();
        pause = false;
        mediaPlayer.seekTo(stopMediaplayer);
        mediaPlayer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        pause = true;
        mediaPlayer.stop();
        mediaPlayer.pause();
        stopMediaplayer = mediaPlayer.getCurrentPosition();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    public ImageButton getRightButton() {
        return rightButton;
    }

    public ImageButton getDownButton() {
        return downButton;
    }

    public ImageButton getLeftButton() {
        return leftButton;
    }

    public ImageButton getRotateButton() {
        return rotateButton;
    }

    public boolean getPause() {
        return !pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public TextView getHighscoreLevelTextView() {
        return highscoreLevelTextView;
    }

    public TextView getPointTextView() {
        return pointTextView;
    }

    public TextView getCurrentLevelTextView() {
        return currentLevelTextView;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public long getArguments(){
        long arg;
        if(arguments != null){
            arg = arguments.getLong("speed");
        }else{
            arg = 170;
        }
        return arg;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() > 400 && event.getAction() == MotionEvent.ACTION_MOVE) {
            rightButton.performClick();
        } else if (event.getX() < 400 && event.getAction() == MotionEvent.ACTION_MOVE) {
            leftButton.performClick();
        }
        return super.onTouchEvent(event);
    }


}
